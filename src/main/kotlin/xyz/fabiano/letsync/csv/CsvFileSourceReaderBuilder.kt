package xyz.fabiano.letsync.csv

import xyz.fabiano.letsync.dsl.SourceReaderBuilder

class CsvFileSourceReaderBuilder {
    var filename: String? = null
    var header = true
    var separator = ','

    fun separator(block: () -> Char) {
        separator = block.invoke()
    }

    fun header(block: () -> Boolean) {
        header = block.invoke()
    }

    fun filename(block: () -> String) {
        filename = block.invoke()
    }

    fun build(): CsvFileSourceReader {
        return CsvFileSourceReader(filename!!, header, separator)
    }
}

fun SourceReaderBuilder<CsvLine>.csv(block: CsvFileSourceReaderBuilder.() -> Unit) {
    this.with { CsvFileSourceReaderBuilder().apply(block).build() }
}

//inline fun <R, reified S : Any> SyncBuilder<R, S>.sinkOnMongo(block: MongoSinkChannelBuilder<S>.() -> Unit) {
//    this.sink(MongoSinkChannelBuilder<S>().apply(block).build())
//}
//
//inline fun <reified T : Any> MongoSinkChannelBuilder<T>.collection(collection: () -> CoroutineCollection<T>) {
//    val coll = collection.invoke()
//    SinkChannelBuilder<T>().with { this.buildWithCollection(coll) }
//}