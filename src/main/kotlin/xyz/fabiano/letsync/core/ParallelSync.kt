package xyz.fabiano.letsync.core

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import xyz.fabiano.letsync.api.*
import java.time.Instant

class ParallelSync<R, S>(
    name: String,
    trigger: LetSyncTrigger,
    engine: LetSyncEngine,
    source: LetSyncSource<R>,
    sinks: MutableList<LetSyncSink<S>>,
    transformer: (R) -> S
) : AbstractSynchronization<R, S>(name, trigger, engine, source, sinks, transformer) {

    override fun start() {
        trigger.trigger(this::execute)
    }

    override fun execute() {
        val start = Instant.now()
        println("Starting execution at $start")

        engine.run {
            flow<R> {
                source.read(this::emit)
            }.transform(coroutineScope, engine.bufferSize()) {
                transformer.invoke(it)
            }.parallelCollect(coroutineScope, engine.bufferSize()) {
                sinks.forEach { s -> s.sink(it) }
            }

            val end = Instant.now()
            println("Ending job at $end. It took ${end.minusMillis(start.toEpochMilli())} to run.")
        }
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
