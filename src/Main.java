import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        // Define semaphores and shared variables inside main
        Semaphore mutex = new Semaphore(1);     // Mutual exclusion for shared resource "waiting"
        Semaphore bus = new Semaphore(0);       // Semaphore for the bus arrival
        Semaphore boarded = new Semaphore(0);   // Semaphore to signal when riders have boarded
        int[] waiting = {0};  // Array to allow modification from different threads (mutable container)

        // Start Rider threads
        for (int i = 0; i < 200; i++) {  // 100 riders
            new Rider(mutex, bus, boarded, waiting,i).start();
            try {
                Thread.sleep((int) (Math.random() * 100));  // Random arrival times for riders
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Start Bus threads (simulate buses arriving at different intervals)
        for (int i = 0; i < 3; i++) {  // Simulate 3 buses arriving
            try {
                Thread.sleep(2000);  // Bus arrival interval
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new Bus(mutex, bus, boarded, waiting,i).start();
        }
    }
}
