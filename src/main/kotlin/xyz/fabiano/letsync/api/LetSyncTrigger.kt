package xyz.fabiano.letsync.api

interface LetSyncTrigger {
    suspend fun trigger(function: suspend () -> Unit)
}