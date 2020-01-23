package xyz.fabiano.letsync

import xyz.fabiano.letsync.dsl.sync
import xyz.fabiano.letsync.core.sink.DefaultOutputPrinterSink

fun main() {
    val sync = sync<String, String> {
        name = "Console Printer"

        motor {
            fixedThreads = 1
        }

        source {
            var i = 5
            hasNext {
                i -= 1
                i > 0
            }
            retrieve { "hello world! $i" }
        }

        transformer { it.toUpperCase() }

        sink { println(it) }
        sink { println(it) }
        sink { println(it) }
        sink = DefaultOutputPrinterSink()
    }

    sync.start()
}