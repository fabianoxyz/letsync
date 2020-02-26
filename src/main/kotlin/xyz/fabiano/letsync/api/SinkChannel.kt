package xyz.fabiano.letsync.api

interface SinkChannel<T> {
    suspend fun sink(entity: T)
}