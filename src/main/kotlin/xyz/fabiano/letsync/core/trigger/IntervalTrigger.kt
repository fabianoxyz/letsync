package xyz.fabiano.letsync.core.trigger

import kotlinx.coroutines.delay
import xyz.fabiano.letsync.api.Trigger
import java.time.Duration

class IntervalTrigger(
    private val interval : Duration
) : Trigger {

    var running = false
        private set

    private var stop = false

    private var stoppingHooks : MutableList<() -> Any> = mutableListOf({})

    override suspend fun manage(function: suspend () -> Unit) {
        running = true
        while (!stop) {
            function.invoke()
            holdOn()
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