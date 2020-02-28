package xyz.fabiano.letsync.dsl

import xyz.fabiano.letsync.api.LetSyncSink
import xyz.fabiano.letsync.core.Synchronization
import xyz.fabiano.letsync.core.sink.FunctionSink

@SyncDsl
class SyncBuilder<R, S> {
    var name = "Default Sync"
    var engine = LetSyncEngineBuilder()
    var trigger = SyncTriggerBuilder()
    var source = LetSyncSourceBuilder<R>()

    var sink: LetSyncSink<S>? = null
    private val sinks: MutableList<LetSyncSink<S>> = mutableListOf()

    lateinit var transformer: ((R) -> S)

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

    fun engine(block: LetSyncEngineBuilder.() -> Unit): LetSyncEngineBuilder {
        engine.apply(block).build()
        return engine
    }

    fun source(block: LetSyncSourceBuilder<R>.() -> Unit): LetSyncSourceBuilder<R> {
        source.apply(block)
        return source
    }

    fun sink(sinkChannel: LetSyncSink<S>) {
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
        return Synchronization(name, trigger.build(), engine.build(), source.build(), sinks, transformer)
    }
}

@SyncDsl
fun <R, S> sync(block: SyncBuilder<R, S>.() -> Unit): Synchronization<R, S> {
    return SyncBuilder<R, S>().apply(block).build()
}