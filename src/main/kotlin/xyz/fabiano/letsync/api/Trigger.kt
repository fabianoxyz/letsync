package xyz.fabiano.letsync.api

interface Trigger {
    suspend fun manage(function: suspend () -> Unit)
}