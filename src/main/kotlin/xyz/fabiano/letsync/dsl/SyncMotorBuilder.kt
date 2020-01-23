package xyz.fabiano.letsync.dsl

import xyz.fabiano.letsync.api.SyncMotor
import xyz.fabiano.letsync.core.motor.ExecutorServiceSyncMotor
import java.util.concurrent.ForkJoinPool

@SyncDsl
class SyncMotorBuilder {

    var fixedThreads : Int? = null
    var minThreads : Int? = null
    var maxThreads : Int? = null

    fun build() : SyncMotor {
        if(fixedThreads != null) {
            val forkJoinPool = ForkJoinPool(fixedThreads!!)
            return ExecutorServiceSyncMotor(forkJoinPool)
//        } else if(minThreads != null && maxThreads != null) {
//            return ExecutorServiceSyncMotor(ThreadPoolExecutor())
        } else {
            throw IllegalArgumentException("You need to setup properly.")

        }
    }
}