package xyz.fabiano.letsync.core.sink

import xyz.fabiano.letsync.api.LetSyncSink

class FunctionSink<T>(
    private val function: (T) -> Unit
) : LetSyncSink<T> {
    override suspend fun sink(entity: T) {
        function.invoke(entity)
    }
}