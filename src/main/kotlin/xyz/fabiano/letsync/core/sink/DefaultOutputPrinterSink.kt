package xyz.fabiano.letsync.core.sink

import xyz.fabiano.letsync.api.LetSyncSink

class DefaultOutputPrinterSink : LetSyncSink<Any> {

    override suspend fun sink(entity: Any) {
        println(entity)
    }
}