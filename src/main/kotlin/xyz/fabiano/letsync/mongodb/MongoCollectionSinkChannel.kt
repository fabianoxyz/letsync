package xyz.fabiano.letsync.mongodb

import org.litote.kmongo.coroutine.CoroutineCollection
import xyz.fabiano.letsync.api.LetSyncSink

class MongoCollectionSinkChannel<T : Any>(
    private val collection: CoroutineCollection<T>
) : LetSyncSink<T> {

    override suspend fun sink(entity: T) {
        collection.save(entity)
    }
}