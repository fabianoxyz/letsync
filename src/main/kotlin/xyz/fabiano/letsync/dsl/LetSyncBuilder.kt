package xyz.fabiano.letsync.dsl

import xyz.fabiano.letsync.api.*
import xyz.fabiano.letsync.core.FirstInFirstOutSync
import xyz.fabiano.letsync.core.ParallelSync
import xyz.fabiano.letsync.core.sink.FunctionSink
import kotlin.reflect.KClass

@SyncDsl
class LetSyncBuilder<R, S> {
    var name = "Default Sync"
    var engine = LetSyncEngineBuilder()
    var trigger = SyncTriggerBuilder()
    var source = LetSyncSourceBuilder<R>()

    var sink: LetSyncSink<S>? = null
    private val sinks: MutableList<LetSyncSink<S>> = mutableListOf()

    lateinit var transformer: ((R) -> S)

    private var preserveOrder = false

    infix fun name(name: String) {
        this.name = name
    }

    fun name(name: () -> String) {
        this.name = name.invoke()
    }

    fun trigger(block: SyncTriggerBuilder.() -> Unit) {
        trigger.apply(block)
    }

    fun preserveOrder(block: () -> Boolean) {
        preserveOrder = block.invoke()
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

    fun sink(block: LetSyncSinkBuilder<S>.() -> Unit) {
        val sinkChannel = LetSyncSinkBuilder<S>().apply(block).build()
        sinks.add(sinkChannel)
    }

    fun functionSink(block: (S) -> Unit) {
        sinks.add(FunctionSink(block))
    }

    fun build(): LetSync<R, S> {
        sink?.let { sinks.add(it) }

        val builderType = if (preserveOrder) SyncBuilderType.FIFO else SyncBuilderType.PARALLEL

        return builderType.create(name, trigger.build(), engine.build(), source.build(), sinks, transformer)
    }

    private enum class SyncBuilderType {
        FIFO {
            override fun <R, S> create(
                name: String,
                trigger: LetSyncTrigger,
                engine: LetSyncEngine,
                source: LetSyncSource<R>,
                sinks: MutableList<LetSyncSink<S>>,
                transformer: (R) -> S
            ): LetSync<R, S> {
                return FirstInFirstOutSync(name, trigger, engine, source, sinks, transformer)
            }
        },
        PARALLEL {
            override fun <R, S> create(
                name: String,
                trigger: LetSyncTrigger,
                engine: LetSyncEngine,
                source: LetSyncSource<R>,
                sinks: MutableList<LetSyncSink<S>>,
                transformer: (R) -> S
            ): LetSync<R, S> {
                return ParallelSync(name, trigger, engine, source, sinks, transformer)
            }
        };

        abstract fun <R, S> create(
            name: String,
            trigger: LetSyncTrigger,
            engine: LetSyncEngine,
            source: LetSyncSource<R>,
            sinks: MutableList<LetSyncSink<S>>,
            transformer: (R) -> S
        ): LetSync<R, S>
    }
}

@SyncDsl
fun <R, S> sync(block: LetSyncBuilder<R, S>.() -> Unit): LetSync<R, S> {
    return LetSyncBuilder<R, S>().apply(block).build()
}