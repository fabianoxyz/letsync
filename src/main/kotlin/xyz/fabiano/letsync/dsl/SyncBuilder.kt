package xyz.fabiano.letsync.dsl

import xyz.fabiano.letsync.api.SinkChannel
import xyz.fabiano.letsync.api.SourceReader
import xyz.fabiano.letsync.api.SyncMotor
import xyz.fabiano.letsync.core.Synchronization
import xyz.fabiano.letsync.core.motor.DefaultGlobalSyncMotor
import xyz.fabiano.letsync.core.sink.FunctionSink

@SyncDsl
class SyncBuilder<R, S> {
    var name = "Default Sync"
    var motor: SyncMotor = DefaultGlobalSyncMotor()
    val trigger = SyncTriggerBuilder()
    lateinit var transformer: ((R) -> S)
    lateinit var source: SourceReader<R>
    var sink: SinkChannel<S>? = null

    private val sinks: MutableList<SinkChannel<S>> = mutableListOf()

    infix fun name(name : String) {
        this.name = name
    }

    fun name(name : () -> String) {
        this.name = name.invoke()
    }

    fun trigger(block: SyncTriggerBuilder.() -> Unit) {
        trigger.apply(block)
    }

    fun transformer(block: (R) -> S) {
        transformer = block
    }

    fun motor(block: SyncMotorBuilder.() -> Unit): SyncMotor {
        motor = SyncMotorBuilder().apply(block).build()
        return motor
    }

    fun source(block: SourceReaderBuilder<R>.() -> Unit): SourceReader<R> {
        source = SourceReaderBuilder<R>().apply(block).build()
        return source
    }

    fun sink(sinkChannel: SinkChannel<S>) {
        sinks.add(sinkChannel)
    }

    fun sink(block : SinkChannelBuilder<S>.() -> Unit) {
        val sinkChannel = SinkChannelBuilder<S>().apply(block).build()
        sinks.add(sinkChannel)
    }

    fun functionSink(block: (S) -> Unit) {
        sinks.add(FunctionSink(block))
    }

    fun build(): Synchronization<R, S> {
        sink?.let { sinks.add(it) }

        return Synchronization(name, trigger.build(), motor.coroutineScope(), transformer, source, sinks)
    }
}

@SyncDsl
fun <R, S> sync(block: SyncBuilder<R, S>.() -> Unit): Synchronization<R, S> {
    return SyncBuilder<R, S>().apply(block).build()
}