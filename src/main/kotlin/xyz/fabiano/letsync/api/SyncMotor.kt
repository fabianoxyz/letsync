package xyz.fabiano.letsync.api

import kotlinx.coroutines.CoroutineScope

interface SyncMotor {
    fun coroutineScope() : CoroutineScope

}