package xyz.fabiano.letsync.mongodb

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

class MongoClient(
    private val connectionString : ConnectionString,
    private val databaseName : String,
    private val user : String,
    private val password : String?
) {
    private fun clientSettings() : MongoClientSettings {

        return MongoClientSettings
            .builder()
            .applyConnectionString(connectionString)
            .credential(credential())
            .build()
    }

    private fun credential() : MongoCredential {
        val pwd = password?.toCharArray() ?: charArrayOf()
        return MongoCredential.createCredential(user, "admin", pwd)
    }

    fun database() : CoroutineDatabase {
        return KMongo.createClient(clientSettings()).coroutine.getDatabase(databaseName)
    }
}