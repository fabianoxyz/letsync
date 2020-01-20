package xyz.fabiano.letsync.dsl

import xyz.fabiano.letsync.api.SinkChannel
import xyz.fabiano.letsync.api.SourceChannel
import xyz.fabiano.letsync.api.SyncMotor
import xyz.fabiano.letsync.engine.Synchronization
import xyz.fabiano.letsync.engine.motor.DefaultGlobalSyncMotor

@SyncDsl
class SyncBuilder<R, S> {
    var name = ""
    var motor = DefaultGlobalSyncMotor()
    var transformer : ((R) -> S)? = null

    var source : SourceChannel<R>? = null
    var sourceFunction : (() -> R)? = null

    var sink : SinkChannel<S>? = null
    var sinkFunction : ((S) -> Unit)? = null

    var sinks : Set<SinkChannel<S>> = mutableSetOf()

    fun motor(block : SyncMotorBuilder.() -> Unit) : SyncMotor {
        return SyncMotorBuilder().apply(block).build()
    }

//    fun sink(block : SinkChannel) {
//
//    }

    fun build() : Synchronization<R, S> {
//        sink?.apply { sinks.add(it) }

        return Synchronization(name, motor, transformer!!, sourceFunction!!, sinkFunction!!)
    }
}

@SyncDsl
fun <R, S> sync(block : SyncBuilder<R, S>.() -> Unit) : Synchronization<R, S> {
    return SyncBuilder<R, S>().apply(block).build()
}