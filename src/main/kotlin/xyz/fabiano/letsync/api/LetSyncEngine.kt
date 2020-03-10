package xyz.fabiano.letsync.api

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface LetSyncEngine {

    /**
     * Provides the coroutine scope for the entire synchronization mechanism.
     */
    fun coroutineScope(): CoroutineScope

    /**
     * Size of the buffer for the channels that bridges the different coroutines.
     */
    fun bufferSize(): Int

    /**
     * Runs a suspended task inside our [CoroutineScope]
     */
    fun run(block: suspend (LetSyncEngine) -> Unit) {
        coroutineScope().launch {
            block.invoke(this@LetSyncEngine)
        }
    }
}