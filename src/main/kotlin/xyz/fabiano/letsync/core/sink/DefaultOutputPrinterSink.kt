package xyz.fabiano.letsync.core.sink

import xyz.fabiano.letsync.api.SinkChannel

class DefaultOutputPrinterSink : SinkChannel<Any> {

    override suspend fun sink(entity: Any) {
        println(entity)
    }
}