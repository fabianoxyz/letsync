package xyz.fabiano.letsync.csv

import xyz.fabiano.letsync.api.SourceReader
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Stream

class CsvFileSourceReader<T>(
    filename : String,
    private val parser : (String) -> T
) : SourceReader<T> {

    private val lineStream : Stream<String> = Files.lines(Paths.get(filename))

    override suspend fun read(emitter : suspend (T) -> Unit) {
        lineStream.iterator().forEach { emitter.invoke(parser.invoke(it)) }
    }

    override suspend fun hasNext() = false

    override fun close() { }
}