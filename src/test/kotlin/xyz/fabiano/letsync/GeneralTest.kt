package xyz.fabiano.letsync

import kotlinx.coroutines.delay
import xyz.fabiano.letsync.core.sink.DefaultOutputPrinterSink
import xyz.fabiano.letsync.core.source.StringMemoryReader
import xyz.fabiano.letsync.core.trigger.ExecuteOnceTrigger
import xyz.fabiano.letsync.dsl.sync

fun main() {
    val sync = sync<String, String> {
        name = "Console Printer"

        trigger = ExecuteOnceTrigger()

        motor {
            fixedThreads = 2
        }

        source = StringMemoryReader(
            listOf("foo", "bar", "Hello", "world", "bla", "test")
        )

        transformer { it.toUpperCase() }

        sink { println(it) }
        sink { println("**$it**") }
        sink = DefaultOutputPrinterSink()
    }

    sync.start()

    Thread.sleep(1_000)
}