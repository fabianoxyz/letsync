package xyz.fabiano.letsync.engine.motor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import xyz.fabiano.letsync.api.SyncMotor

class DefaultGlobalSyncMotor : SyncMotor {

    override fun coroutineScope(): CoroutineScope {
        return GlobalScope
    }
}