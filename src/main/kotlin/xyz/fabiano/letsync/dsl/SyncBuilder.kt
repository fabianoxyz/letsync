package xyz.fabiano.letsync.dsl

import xyz.fabiano.letsync.api.SourceReader
import xyz.fabiano.letsync.api.SyncMotor
import xyz.fabiano.letsync.api.Trigger
import xyz.fabiano.letsync.core.Synchronization
import xyz.fabiano.letsync.core.motor.DefaultGlobalSyncMotor

@SyncDsl
class SyncBuilder<R, S> {
    var name = "Default Sync"
    var motor: SyncMotor = DefaultGlobalSyncMotor()
    lateinit var trigger: Trigger
    lateinit var transformer: ((R) -> S)
    lateinit var source: SourceReader<R>
    lateinit var sink: (S) -> Unit

    var sinks: MutableList<(S) -> Unit> = mutableListOf()

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

    fun sink(block: (S) -> Unit) {
        sinks.add(block)
    }

    fun build(): Synchronization<R, S> {
        sinks.add(sink)
        return Synchronization(name, trigger, motor, transformer, source, sinks)
    }
}

@SyncDsl
fun <R, S> sync(block: SyncBuilder<R, S>.() -> Unit): Synchronization<R, S> {
    return SyncBuilder<R, S>().apply(block).build()
}