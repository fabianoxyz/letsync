package xyz.fabiano.letsync.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import xyz.fabiano.letsync.api.SinkChannel
import xyz.fabiano.letsync.api.SourceReader
import xyz.fabiano.letsync.api.Trigger
import java.time.Instant

class Synchronization<R, S>(
    private val name: String,
    private val trigger : Trigger,
    private val coroutineScope: CoroutineScope,
    private val transformer: (R) -> S,
    private val source: SourceReader<R>,
    private val sinks: MutableList<SinkChannel<S>>
) {
    private var job : Job? = null

    fun start() {
        job = coroutineScope.launch {
            trigger.manage(this@Synchronization::execute)
        }
    }

    private suspend fun execute() {
        val start = Instant.now()
        println("Starting execution at $start")

        flow<R> {
            source.read(this::emit)
        }.collect {
            val value = transformer.invoke(it)
            sinks.forEach { s -> s.sink(value) }
        }

        val end = Instant.now()
        println("Ending job at $end. It took ${end.minusMillis(start.toEpochMilli())} to run.")
    }
}