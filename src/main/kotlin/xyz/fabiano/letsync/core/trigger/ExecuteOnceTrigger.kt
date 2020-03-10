package xyz.fabiano.letsync.core.trigger

import xyz.fabiano.letsync.api.LetSyncTrigger

class ExecuteOnceTrigger : LetSyncTrigger {
    override fun trigger(function: () -> Unit) {
        function.invoke()
    }
}