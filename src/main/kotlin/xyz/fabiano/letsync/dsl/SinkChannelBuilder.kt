package xyz.fabiano.letsync.dsl

import xyz.fabiano.letsync.api.SinkChannel
import xyz.fabiano.letsync.core.sink.FunctionSink

@SyncDsl
class SinkChannelBuilder<T> {

    private var foreignBuilder : (() -> SinkChannel<T>)? = null
    private var function : ((T) -> Unit)? = null

    fun with(builder: () -> SinkChannel<T>) {
        foreignBuilder = builder
    }

    fun function(function : (T) -> Unit) {
        this.function = function
    }

    fun build() : SinkChannel<T> {
        return foreignBuilder?.invoke() ?: buildWithoutForeigner()
    }

    private fun buildWithoutForeigner() : SinkChannel<T> {
        return FunctionSink(function!!)
    }
}