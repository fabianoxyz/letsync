package xyz.fabiano.letsync

import xyz.fabiano.letsync.core.sink.FunctionSink
import xyz.fabiano.letsync.csv.CsvLine
import xyz.fabiano.letsync.csv.csv
import xyz.fabiano.letsync.dsl.seconds
import xyz.fabiano.letsync.dsl.sync
import xyz.fabiano.letsync.mongodb.mongo
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun main() {
    val sync = sync<CsvLine, Person> {
        name = "Console Printer"

        engine {
            globalScope { false }
            firstInFirstOut { false }
            bufferSize { 400 }
            fixedThreadNumber { 4 }
        }

        trigger every 1000.seconds()

        source {
            csv {
                filename { "/Users/fabiano/sample-10k.csv" }
                header { true }
                separator { ',' }
            }
        }

        transformer {
            val dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
            Person(
                sequence = it[0].toLong(),
                firstName = it[1],
                lastName = it[2],
                age = it[3].toInt(),
                phoneNumber = it[4],
                street = it[5],
                city = it[6],
                state = it[7],
                zipCode = it[8],
                date = LocalDate.parse(it[9], dateFormatter)
            )
        }

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

    Thread.sleep(10_000)
}

data class Person(
    val sequence: Long,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val phoneNumber: String,
    val street: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val date: LocalDate
)