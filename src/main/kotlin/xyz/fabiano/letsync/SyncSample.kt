package xyz.fabiano.letsync

import xyz.fabiano.letsync.dsl.sync
import xyz.fabiano.letsync.core.sink.DefaultOutputPrinterSink
import xyz.fabiano.letsync.core.sink.FunctionSink
import xyz.fabiano.letsync.mongodb.mongo
import xyz.fabiano.letsync.mongodb.sinkOnMongo

fun main() {
    val sync = sync<String, String> {
        name = "Console Printer"

        engine {
            globalScope { false }
            firstInFirstOut { false }
            bufferSize { 400 }
            fixedThreadNumber { 4 }
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

        sinkOnMongo {

        }

        sink {
            mongo {  }
            with { FunctionSink { println(it) } }
        }
    }

    sync.start()
}

fun threadSummary(component : String) : String {
    return "$component - thread: ${Thread.currentThread().name} # "
}
