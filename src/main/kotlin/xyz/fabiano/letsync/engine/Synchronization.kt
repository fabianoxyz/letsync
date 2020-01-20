package xyz.fabiano.letsync.engine

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import xyz.fabiano.letsync.api.SinkChannel
import xyz.fabiano.letsync.api.SourceChannel
import xyz.fabiano.letsync.api.SyncMotor

class Synchronization<R, S>(
    val name : String,
    val syncMotor: SyncMotor,
    val transformer : (R) -> S,
    val source: () -> R,
    val sink : (S) -> Unit
//    val source: SourceChannel<R>,
//    val sink : Set<SinkChannel<S>>
) {

    fun start() {
        runBlocking {
            syncMotor.coroutineScope().launch {
                flow {
                    //                while (source.hasNext()) emit(source.emit())
                    emit(source.invoke())
                }.collect {
                    val value = transformer.invoke(it)
//                sink.forEach { it.accept(value) }
                    sink.invoke(value)
                }
            }
        }
    }
}