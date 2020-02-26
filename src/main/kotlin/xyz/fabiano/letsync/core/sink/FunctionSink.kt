package xyz.fabiano.letsync.core.sink

import xyz.fabiano.letsync.api.SinkChannel

class FunctionSink<T>(
    private val function: (T) -> Unit
) : SinkChannel<T> {
    override suspend fun sink(entity: T) {
        function.invoke(entity)
    }
}