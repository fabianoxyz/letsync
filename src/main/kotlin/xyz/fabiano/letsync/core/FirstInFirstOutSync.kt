package xyz.fabiano.letsync.core

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import xyz.fabiano.letsync.api.LetSyncEngine
import xyz.fabiano.letsync.api.LetSyncSink
import xyz.fabiano.letsync.api.LetSyncSource
import xyz.fabiano.letsync.api.LetSyncTrigger
import java.time.Instant

class FirstInFirstOutSync<R, S>(
    name: String,
    trigger: LetSyncTrigger,
    engine: LetSyncEngine,
    source: LetSyncSource<R>,
    sinks: MutableList<LetSyncSink<S>>,
    transformer: (R) -> S
) : AbstractSynchronization<R, S>(
    name, trigger, engine, source, sinks, transformer
) {

    override fun execute() {
        val start = Instant.now()
        println("Starting execution at $start")

        engine.run {
            flow<R> {
                source.read(this::emit)
            }.map {
                transformer.invoke(it)
            }.collect {
                sinks.forEach { s -> s.sink(it) }
            }

            val end = Instant.now()
            println("Ending job at $end. It took ${end.minusMillis(start.toEpochMilli())} to run.")
        }
    }
}
