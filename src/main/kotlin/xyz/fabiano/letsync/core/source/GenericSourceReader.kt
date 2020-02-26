package xyz.fabiano.letsync.core.source

import xyz.fabiano.letsync.api.SourceReader

class GenericSourceReader<T> : SourceReader<T> {
    override suspend fun read(emitter: suspend (T) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun close() {

    }
}