import java.util.concurrent.Semaphore;
import java.util.Random;

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

        Random random = new Random();

        // Define semaphores and shared variables inside main
        Semaphore mutex = new Semaphore(1);     // Mutual exclusion for shared resource "waiting"
        Semaphore bus = new Semaphore(0);       // Semaphore for the bus arrival
        Semaphore boarded = new Semaphore(0);   // Semaphore to signal when riders have boarded
        int[] waiting = {0};  // Array to allow modification from different threads (mutable container)

        int busIndex = 0;
        int riderIndex = 0;


        // start rider or bus thread randomly
        while (busIndex < 5 && riderIndex < 150) {
            int random_var = (random.nextInt(51) == 0 ? 1 : 0);
            // System.out.println("Random: " + random_var);
            if (random_var == 0) {
                initialize_rider(mutex, bus, boarded, waiting, riderIndex);
                riderIndex++;
            } else {
                initialize_bus(mutex, bus, boarded, waiting, busIndex);
                busIndex++;
            }
        }

    }
}
