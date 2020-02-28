package xyz.fabiano.letsync.core.engine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import xyz.fabiano.letsync.api.LetSyncEngine
import java.util.concurrent.ExecutorService

class ExecutorServiceEngine(
    executorService: ExecutorService,
    private val bufferSize: Int
) : LetSyncEngine {

    private val coroutineScope = CoroutineScope(executorService.asCoroutineDispatcher())

    override fun coroutineScope(): CoroutineScope {
        return coroutineScope
    }

    override fun bufferSize(): Int {
        return bufferSize
    }
}