package xyz.fabiano.letsync.csv

import xyz.fabiano.letsync.api.SourceReader
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Stream

class CsvFileSourceReader(
    private val filename : String,
    private val skipFirstLine : Boolean,
    private val separator : Char
) : SourceReader<CsvLine> {

    private val skip = if (skipFirstLine) 1L else 0L
    private val lineStream : Stream<String> = Files.lines(Paths.get(filename)).skip(skip)

    override fun close() { }

    override suspend fun read(emitter: suspend (CsvLine) -> Unit) {
        lineStream.iterator().forEach { emitter.invoke(CsvLine(it, separator)) }
    }
}