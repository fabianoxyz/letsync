package xyz.fabiano.letsync.core

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import xyz.fabiano.letsync.api.SourceReader
import xyz.fabiano.letsync.api.SyncMotor
import xyz.fabiano.letsync.api.Trigger

class Synchronization<R, S>(
    private val name: String,
    private val trigger : Trigger,
    private val syncMotor: SyncMotor,
    private val transformer: (R) -> S,
    private val source: SourceReader<R>,
    private val sinks: MutableList<(S) -> Unit>
) {

    private var job : Job? = null

    fun start() {
        job = syncMotor.coroutineScope().launch {
            trigger.manage(this@Synchronization::execute)
        }
    }

    fun stop() {
        syncMotor.coroutineScope().launch { job?.cancelAndJoin() }
        syncMotor.coroutineScope().cancel()
    }

    private suspend fun execute() {
        flow<R> {
            while (source.hasNext()) source.read(this::emit)
        }.collect {
            val value = transformer.invoke(it)
            sinks.forEach { s -> s.invoke(value) }
        }
    }
}