package xyz.fabiano.letsync

import xyz.fabiano.letsync.core.sink.FunctionSink
import xyz.fabiano.letsync.core.source.StringMemoryReader
import xyz.fabiano.letsync.dsl.seconds
import xyz.fabiano.letsync.dsl.sync
import xyz.fabiano.letsync.mongodb.mongo
import xyz.fabiano.letsync.mongodb.sinkOnMongo

fun main() {
    val sync = sync<String, Person> {
        name = "Console Printer"

        motor {
            fixedThreads = 1
        }

        trigger every 2.seconds()

        source = StringMemoryReader(listOf("Fabiano#101", "Fabiano#102", "Fabiano#103"))

//        transformer {
////            val parsed = it.split("#")
////            Person(parsed[0], parsed[1])
//        }

        sink {
            mongo {
                host { "localhost" }
                databaseName { "fuse" }
                user { "admin" }
                password { "admin" }
            }
        }

        sink {
            with { FunctionSink { println(it) } }
        }
    }

    sync.start()

    Thread.sleep(10_000L)
}

//data class Person(val name: String, val document: String)