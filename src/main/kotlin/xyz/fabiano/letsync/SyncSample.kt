package xyz.fabiano.letsync

import xyz.fabiano.letsync.dsl.sync

fun main() {
    val sync = sync<String, String> {
        name = "Console Printer"

        motor {
            fixedThreads = 1
        }

        sourceFunction = { "value" }

        transformer = { it.toUpperCase() }

        sinkFunction = { println(it) }
    }

    sync.start()

    Thread.sleep(1000)
}