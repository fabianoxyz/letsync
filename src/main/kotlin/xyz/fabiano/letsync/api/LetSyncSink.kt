package xyz.fabiano.letsync.api

interface LetSyncSink<T> {
    suspend fun sink(entity: T)
}