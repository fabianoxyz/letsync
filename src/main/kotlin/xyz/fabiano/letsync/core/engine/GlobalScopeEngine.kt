package xyz.fabiano.letsync.core.engine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import xyz.fabiano.letsync.api.LetSyncEngine

/**
 *  It will create the an engine with the coroutine's GlobalScope
 *  Be careful, since the [GlobalScope] is not bounded to your syncing mechanism and
 *      it is intended to follow the application lifecycle.
 *
 *  It will use `n` worker threads when the system runs on a `n` cores CPU.
 */
class GlobalScopeEngine(private val bufferSize : Int = 1) : LetSyncEngine {

    override fun coroutineScope(): CoroutineScope {
        return GlobalScope
    }

    override fun bufferSize(): Int {
        return bufferSize
    }
}