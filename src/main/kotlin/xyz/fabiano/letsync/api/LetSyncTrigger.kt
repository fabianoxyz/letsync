package xyz.fabiano.letsync.api

interface LetSyncTrigger {
    fun trigger(function: () -> Unit)
}