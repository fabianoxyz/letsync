package xyz.fabiano.letsync.core

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import xyz.fabiano.letsync.api.LetSyncEngine
import xyz.fabiano.letsync.api.LetSyncSink
import xyz.fabiano.letsync.api.LetSyncSource
import xyz.fabiano.letsync.api.LetSyncTrigger
import java.time.Instant

class Synchronization<R, S>(
    private val name: String,
    private val trigger: LetSyncTrigger,
    private val engine: LetSyncEngine,
    private val source: LetSyncSource<R>,
    private val sinks: MutableList<LetSyncSink<S>>,
    private val transformer: (R) -> S
) {
    private var job: Job? = null

    private val coroutineScope = engine.coroutineScope()

    fun start() {
        job = coroutineScope.launch {
            trigger.trigger(this@Synchronization::execute)
        }
    }

    private suspend fun execute() {
        val start = Instant.now()
        println("Starting execution at $start")

        flow<R> {
            source.read(this::emit)
        }.transform(coroutineScope, 400) {
            transformer.invoke(it)
        }.parallelCollect(coroutineScope, 400) {
            sinks.forEach { s -> s.sink(it) }
        }


//        flow<R> {
//            source.read(this::send)
//        }.map {
//            transformer.invoke(it)
//        }.collect {
//            sinks.forEach { s -> s.sink(it) }
//        }

        val end = Instant.now()
        println("Ending job at $end. It took ${end.minusMillis(start.toEpochMilli())} to run.")
    }
}


fun <R, S> Flow<R>.transform(scope: CoroutineScope, bufferSize: Int, transformer: suspend (R) -> S) =
    flow {
        val currCtx = scope.coroutineContext.minusKey(Job)

        coroutineScope {
            val channel = produce(currCtx, bufferSize) {
                collect { value ->
                    send(scope.async { transformer(value) })
                }
            }

            (channel as Job).invokeOnCompletion { if (it is CancellationException && it.cause == null) cancel() }

            for (defer in channel) {
                emit(defer.await())
            }
        }
    }

suspend fun <T> Flow<T>.parallelCollect(scope: CoroutineScope, bufferSize: Int, action: suspend (value: T) -> Unit) {
    val currCtx = scope.coroutineContext.minusKey(Job)

    coroutineScope {
        val channel = produce(currCtx, bufferSize) {
            collect { value -> send(scope.async { action(value) }) }
        }

        (channel as Job).invokeOnCompletion { if (it is CancellationException && it.cause == null) cancel() }

        for (defer in channel) {
            defer.await()
        }
    }
}
