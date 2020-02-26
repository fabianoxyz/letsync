package xyz.fabiano.letsync.api

interface SourceReader<T> : AutoCloseable {
    suspend fun read(emitter : suspend (T) -> Unit)
}
