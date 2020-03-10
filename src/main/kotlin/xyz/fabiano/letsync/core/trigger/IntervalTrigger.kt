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

    override fun trigger(function: () -> Unit) {
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

    private fun holdOn() {
        throw NotImplementedError()
    }
}