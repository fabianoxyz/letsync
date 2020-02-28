package xyz.fabiano.letsync.dsl

import xyz.fabiano.letsync.api.LetSyncEngine
import xyz.fabiano.letsync.core.engine.ExecutorServiceEngine
import xyz.fabiano.letsync.core.engine.GlobalScopeEngine
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@SyncDsl
class LetSyncEngineBuilder {

    private var bufferSize: Int = 1

    private var firstInFirstOut: Boolean = false

    private var globalScope: Boolean = false

    private var fixedThreadNumber: () -> Int = { Runtime.getRuntime().availableProcessors() }

    private var threadPoolSize: (() -> IntRange)? = null

    private var executorService: (() -> ExecutorService)? = null

    fun bufferSize(block: () -> Int) {
        bufferSize = block.invoke()
    }

    fun firstInFirstOut(block: () -> Boolean) {
        firstInFirstOut = block.invoke()
    }

    fun globalScope(block: () -> Boolean) {
        globalScope = block.invoke()
    }

    fun fixedThreadNumber(block: () -> Int) {
        fixedThreadNumber = block
    }

    fun threadPoolSize(block: () -> IntRange) {
        threadPoolSize = block
    }

    fun executorService(block: () -> ExecutorService) {
        executorService = block
    }

    fun build(): LetSyncEngine {

        if (globalScope) {
            return GlobalScopeEngine(bufferSize)
        }

        val myService = (executorService ?: {
            if (threadPoolSize != null) {
                threadPoolSize!!.invoke()
                throw NotImplementedError()
            } else {
                Executors.newFixedThreadPool(fixedThreadNumber.invoke())
            }
        }).invoke()

        return ExecutorServiceEngine(myService, bufferSize)
    }
}
