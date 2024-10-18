import java.util.concurrent.Semaphore;
import java.util.Random;

public class Main {

    // Function to generate exponentially distributed random time
    public static long getExponentialTime(double mean) {
        Random random = new Random();
        double u = random.nextDouble();
        return (long) (-mean * Math.log(1 - u));
    }

    public static void initialize_bus(Semaphore mutex, Semaphore bus, Semaphore boarded, int[] waiting, int index) {
        new Bus(mutex, bus, boarded, waiting, index).start();
    }

    public static void initialize_rider(Semaphore mutex, Semaphore bus, Semaphore boarded, int[] waiting, int index) {
        new Rider(mutex, bus, boarded, waiting, index).start();
    }

    public static void main(String[] args) {
        // Define semaphores and shared variables inside main
        Semaphore mutex = new Semaphore(1); // Mutual exclusion for shared resource "waiting"
        Semaphore bus = new Semaphore(0); // Semaphore for the bus arrival
        Semaphore boarded = new Semaphore(0); // Semaphore to signal when riders have boarded
        int[] waiting = { 0 }; // Array to allow modification from different threads (mutable container)

        // Define means for the exponential distribution in milliseconds
        double busMeanInterArrivalTime = 20 * 60 * 1000; // 20 minutes in milliseconds
        double riderMeanInterArrivalTime = 30 * 1000; // 30 seconds in milliseconds

        // // Timing for testing
        // double busMeanInterArrivalTime = 20 * 60 * 14; // 20 minutes in milliseconds
        // double riderMeanInterArrivalTime = 30 * 10; // 30 seconds in milliseconds

        // Threads for generating buses and riders based on their respective
        // inter-arrival times
        new Thread(() -> {
            int busIndex = 0;
            while (busIndex < 5) { // You can change the condition to allow buses to run continuously
                long busArrivalTime = getExponentialTime(busMeanInterArrivalTime);
                try {
                    Thread.sleep(busArrivalTime); // Bus arrival based on exponential distribution
                    initialize_bus(mutex, bus, boarded, waiting, busIndex);
                    busIndex++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            int riderIndex = 0;
            while (riderIndex < 150) { // You can change the condition to allow riders to keep arriving
                long riderArrivalTime = getExponentialTime(riderMeanInterArrivalTime);
                try {
                    Thread.sleep(riderArrivalTime); // Rider arrival based on exponential distribution
                    initialize_rider(mutex, bus, boarded, waiting, riderIndex);
                    riderIndex++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
