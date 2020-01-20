package xyz.fabiano.letsync.api

@FunctionalInterface
interface SinkChannel<T> {
    fun accept(value : T)
}