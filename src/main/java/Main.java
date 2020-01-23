import xyz.fabiano.letsync.dsl.SyncBuilder;
import xyz.fabiano.letsync.core.motor.ExecutorServiceSyncMotor;

import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        SyncBuilder<String, String> syncBuilder = new SyncBuilder<>();
        syncBuilder.setMotor(new ExecutorServiceSyncMotor(Executors.newFixedThreadPool(2)));
    }
}
