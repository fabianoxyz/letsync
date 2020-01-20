package xyz.fabiano.letsync.engine.motor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import xyz.fabiano.letsync.api.SyncMotor
import java.util.concurrent.ExecutorService

class ExecutorServiceSyncMotor(
    executor : ExecutorService
) : SyncMotor {

    private val coroutineScope = CoroutineScope(executor.asCoroutineDispatcher())

    override fun coroutineScope() : CoroutineScope {
        return coroutineScope
    }
}