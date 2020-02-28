package xyz.fabiano.letsync.core.source

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import xyz.fabiano.letsync.api.LetSyncSource
import xyz.fabiano.letsync.threadSummary

class StringMemoryReader(
    private val values: List<String>
) : LetSyncSource<String> {

    override suspend fun read(emitter: suspend (String) -> Unit) {
        scan().collect {
            println(threadSummary("read"))
            emitter.invoke(it)
        }
    }

    private fun scan(): Flow<String> {
        return flow {
            values.forEach {
                println(threadSummary("scanner"))
                emit(it)
            }
        }
    }

    override fun close() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}