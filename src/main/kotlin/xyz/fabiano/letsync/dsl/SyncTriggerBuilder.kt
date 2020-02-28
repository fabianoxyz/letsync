package xyz.fabiano.letsync.dsl

import xyz.fabiano.letsync.api.LetSyncTrigger
import xyz.fabiano.letsync.core.trigger.IntervalTrigger
import java.time.Duration

@SyncDsl
class SyncTriggerBuilder {

    private var trigger: LetSyncTrigger? = null

    infix fun every(duration: Duration) {
        trigger = IntervalTrigger(duration)
    }

    fun build(): LetSyncTrigger {
        if (trigger != null) {
            return trigger!!
        } else {
            throw IllegalStateException("Not valid state")
        }
    }
}

fun Number.minutes(): Duration {
    return Duration.ofMinutes(this.toLong())
}

fun Number.seconds(): Duration {
    return Duration.ofSeconds(this.toLong())
}

fun Number.hours(): Duration {
    return Duration.ofHours(this.toLong())
}