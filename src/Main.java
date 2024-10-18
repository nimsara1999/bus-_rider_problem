import java.util.concurrent.Semaphore;

public class Main {

    public static void initialize_bus(Semaphore mutex, Semaphore bus, Semaphore boarded, int[] waiting, int index) {
        try {
            Thread.sleep(2000);  // Bus arrival interval
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Bus(mutex, bus, boarded, waiting, index).start();
    }

    public static void initialize_rider(Semaphore mutex, Semaphore bus, Semaphore boarded, int[] waiting, int index) {
        new Rider(mutex, bus, boarded, waiting, index).start();
        try {
            Thread.sleep((int) (Math.random() * 100));  // Random arrival times for riders
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Define semaphores and shared variables inside main
        Semaphore mutex = new Semaphore(1);     // Mutual exclusion for shared resource "waiting"
        Semaphore bus = new Semaphore(0);       // Semaphore for the bus arrival
        Semaphore boarded = new Semaphore(0);   // Semaphore to signal when riders have boarded
        int[] waiting = {0};  // Array to allow modification from different threads (mutable container)

        // Start Bus threads (simulate buses arriving at different intervals)
        for (int i = 0; i < 2; i++) {  // Simulate 3 buses arriving
            initialize_bus(mutex, bus, boarded, waiting, i);
        }


        // Start Rider threads
        for (int i = 0; i < 49; i++) {  // 100 riders
            initialize_rider(mutex, bus, boarded, waiting, i);
        }


        // Start Bus threads (simulate buses arriving at different intervals)
        for (int i = 0; i < 5; i++) {  // Simulate 3 buses arriving
            initialize_bus(mutex, bus, boarded, waiting, i);
        }
    }
}
