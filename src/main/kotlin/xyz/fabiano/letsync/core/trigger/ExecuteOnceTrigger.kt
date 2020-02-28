package xyz.fabiano.letsync.core.trigger

import xyz.fabiano.letsync.api.LetSyncTrigger

class ExecuteOnceTrigger : LetSyncTrigger {
    override suspend fun trigger(function: suspend () -> Unit) {
        function.invoke()
    }
}