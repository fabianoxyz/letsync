package xyz.fabiano.letsync.api

interface SourceChannel<T> {
    fun hasNext() : () -> Boolean
    fun emit() : () -> T
}