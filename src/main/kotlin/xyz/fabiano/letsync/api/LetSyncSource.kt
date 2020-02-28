package xyz.fabiano.letsync.api

interface LetSyncSource<T> : AutoCloseable {
    suspend fun read(emitter : suspend (T) -> Unit)
}
