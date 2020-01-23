package xyz.fabiano.letsync.core.sink

import xyz.fabiano.letsync.api.SinkChannel

class DefaultOutputPrinterSink : SinkChannel<Any> {
    override fun invoke(value : Any) {
        println(value)
    }
}