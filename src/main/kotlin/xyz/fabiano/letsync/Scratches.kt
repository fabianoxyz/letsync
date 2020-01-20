package xyz.fabiano.letsync

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

fun main() {

    val dispatcher = Executors.newFixedThreadPool(4).asCoroutineDispatcher()

    CoroutineScope(dispatcher).launch {

    }
}


suspend fun testIO() {

}