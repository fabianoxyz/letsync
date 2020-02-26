package xyz.fabiano.letsync.dsl

import xyz.fabiano.letsync.api.SourceReader

@SyncDsl
class SourceReaderBuilder<T> {
    var hasNext = { false }
    lateinit var retrieve : () -> T
    var extension : (() ->  SourceReader<T>)? = null

    fun hasNext(block : () -> Boolean) {
        hasNext = block
    }

    fun retrieve(block : () -> T) {
        retrieve = block
    }

    fun with(source : () -> SourceReader<T>) {
        extension = source
    }

    private fun buildWithExtension() : SourceReader<T>? {
        return extension?.invoke()
    }

    fun build() : SourceReader<T> {
        return buildWithExtension() ?: throw IllegalStateException()
    }
}