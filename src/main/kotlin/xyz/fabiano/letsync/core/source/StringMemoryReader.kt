package xyz.fabiano.letsync.core.source

import xyz.fabiano.letsync.api.SourceReader

class StringMemoryReader(
    values: List<String>
) : SourceReader<String> {

    private val values = values.iterator()

    override suspend fun read(emitter: suspend (String) -> Unit) {
        emitter.invoke(values.next())
    }

    override suspend fun hasNext(): Boolean {
        return values.hasNext()
    }

    override fun close() {}
}