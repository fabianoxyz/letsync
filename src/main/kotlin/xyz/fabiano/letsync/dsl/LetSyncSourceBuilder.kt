package xyz.fabiano.letsync.dsl

import xyz.fabiano.letsync.api.LetSyncSource

@SyncDsl
class LetSyncSourceBuilder<T> {
    var hasNext = { false }
    lateinit var retrieve : () -> T
    var extension : (() ->  LetSyncSource<T>)? = null

    fun hasNext(block : () -> Boolean) {
        hasNext = block
    }

    fun retrieve(block : () -> T) {
        retrieve = block
    }

    fun with(source : () -> LetSyncSource<T>) {
        extension = source
    }

    private fun buildWithExtension() : LetSyncSource<T>? {
        return extension?.invoke()
    }

    fun build() : LetSyncSource<T> {
        return buildWithExtension() ?: throw IllegalStateException()
    }
}