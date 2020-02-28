package xyz.fabiano.letsync.core.trigger

import kotlinx.coroutines.delay
import xyz.fabiano.letsync.api.LetSyncTrigger
import java.time.Duration


class IntervalTrigger(
    private val interval : Duration
) : LetSyncTrigger {

    var running = false
        private set

    private var stop = false

    private var stoppingHooks : MutableList<() -> Any> = mutableListOf({})

    override suspend fun trigger(function: suspend () -> Unit) {
        running = true
        while (!stop) {
            function.invoke()
            holdOn() // There's an issue here as it will hold
        }
        running = false
//        stoppingHooks.forEach { it.invoke() }
    }

    private fun stop() {
        stop = true
    }

    private suspend fun holdOn() {
        delay(interval.toMillis())
    }
}