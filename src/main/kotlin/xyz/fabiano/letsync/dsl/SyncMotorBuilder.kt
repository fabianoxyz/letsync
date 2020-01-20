package xyz.fabiano.letsync.dsl

import xyz.fabiano.letsync.api.SyncMotor
import xyz.fabiano.letsync.dsl.SyncDsl
import xyz.fabiano.letsync.engine.motor.ExecutorServiceSyncMotor
import java.util.concurrent.Executors

@SyncDsl
class SyncMotorBuilder {

    var fixedThreads : Int? = null
    var minThreads : Int? = null
    var maxThreads : Int? = null

    fun build() : SyncMotor {
        if(fixedThreads != null) {
            return ExecutorServiceSyncMotor(Executors.newFixedThreadPool(fixedThreads!!))
//        } else if(minThreads != null && maxThreads != null) {
//            return ExecutorServiceSyncMotor(ThreadPoolExecutor())
        } else {
            throw IllegalArgumentException("You need to setup properly.")

        }
    }
}