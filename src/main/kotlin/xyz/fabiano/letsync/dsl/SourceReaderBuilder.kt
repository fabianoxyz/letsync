package xyz.fabiano.letsync.dsl

import xyz.fabiano.letsync.api.SourceReader

@SyncDsl
class SourceReaderBuilder<T> {
    var hasNext = { false }
    lateinit var retrieve : () -> T

    fun hasNext(block : () -> Boolean) {
        hasNext = block
    }

    fun retrieve(block : () -> T) {
        retrieve = block
    }

    fun build() : SourceReader<T> {
        return null!!
    }
}