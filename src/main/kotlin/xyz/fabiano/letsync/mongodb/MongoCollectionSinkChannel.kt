package xyz.fabiano.letsync.mongodb

import org.litote.kmongo.coroutine.CoroutineCollection
import xyz.fabiano.letsync.api.SinkChannel

class MongoCollectionSinkChannel<T : Any>(
    private val collection: CoroutineCollection<T>
) : SinkChannel<T> {

    override suspend fun sink(entity: T) {
        collection.save(entity)
    }
}