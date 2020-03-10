package xyz.fabiano.letsync.core

import xyz.fabiano.letsync.api.*

abstract class AbstractSynchronization<R, S>(
    protected val name: String,
    protected val trigger: LetSyncTrigger,
    protected val engine: LetSyncEngine,
    protected val source: LetSyncSource<R>,
    protected val sinks: MutableList<LetSyncSink<S>>,
    protected val transformer: (R) -> S
) : LetSync<R, S> {

    protected val coroutineScope = engine.coroutineScope()

    override fun start() {
        trigger.trigger(this::execute)
    }

    protected abstract fun execute()
}