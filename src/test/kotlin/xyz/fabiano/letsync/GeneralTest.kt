package xyz.fabiano.letsync

import xyz.fabiano.letsync.core.source.StringMemoryReader
import xyz.fabiano.letsync.dsl.seconds
import xyz.fabiano.letsync.dsl.sync

fun main() {
    val sync = sync<String, String> {
        name = "Console Printer"

//        trigger = ExecuteOnceTrigger()

        trigger every 2.seconds()

        motor {
            fixedThreads = 3
        }

        source = StringMemoryReader(
            listOf("foo", "bar", "Hello", "world", "bla", "test")
        )

        transformer { it.toUpperCase() }

        sink {
//            delay(100L)
//            println(threadSummary("sink1") + it)
        }
//        sink { println(threadSummary("sink2") + "**$it**") }
    }

    sync.start()

    Thread.sleep(10_000L)
}