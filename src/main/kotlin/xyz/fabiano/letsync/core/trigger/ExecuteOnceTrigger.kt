package xyz.fabiano.letsync.core.trigger

import xyz.fabiano.letsync.api.Trigger

class ExecuteOnceTrigger : Trigger {
    override suspend fun manage(function: suspend () -> Unit) {
        function.invoke()
    }
}