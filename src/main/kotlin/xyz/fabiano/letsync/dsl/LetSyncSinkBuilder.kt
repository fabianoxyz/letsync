package xyz.fabiano.letsync.dsl

import xyz.fabiano.letsync.api.LetSyncSink
import xyz.fabiano.letsync.core.sink.FunctionSink

@SyncDsl
class LetSyncSinkBuilder<T> {

    private var foreignBuilder : (() -> LetSyncSink<T>)? = null
    private var function : ((T) -> Unit)? = null

    fun with(builder: () -> LetSyncSink<T>) {
        foreignBuilder = builder
    }

    fun function(function : (T) -> Unit) {
        this.function = function
    }

    fun build() : LetSyncSink<T> {
        return foreignBuilder?.invoke() ?: buildWithoutForeigner()
    }

    private fun buildWithoutForeigner() : LetSyncSink<T> {
        return FunctionSink(function!!)
    }
}