package xyz.fabiano.letsync.api

import kotlinx.coroutines.CoroutineScope

interface LetSyncEngine {

    /**
     * Provides the coroutine scope for the entire synchronization mechanism.
     */
    fun coroutineScope() : CoroutineScope

    /**
     * Size of the buffer for the channels that bridges the different coroutines.
     */
    fun bufferSize() : Int
}