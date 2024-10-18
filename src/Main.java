import java.util.concurrent.Semaphore;
import java.util.Random;
import java.util.LinkedList;
import java.util.Queue;

public class Main {

    public static long getExponentialTime(double mean) {
        Random random = new Random();
        double u = random.nextDouble();
        return (long) (-mean * Math.log(1 - u));
    }

    public static void initialize_bus(Semaphore mutex, Semaphore bus, Semaphore boarded, Queue<Rider> riderQueue,
            int index) {
        new Bus(mutex, bus, boarded, riderQueue, index).start();
    }

    public static void initialize_rider(Semaphore mutex, Semaphore bus, Semaphore boarded, Queue<Rider> riderQueue,
            int index) {
        Rider rider = new Rider(mutex, bus, boarded, riderQueue, index);
        rider.start(); // Start the rider thread
    }

    public static void main(String[] args) {
        Semaphore mutex = new Semaphore(1); // Mutual exclusion for shared resource "queue"
        Semaphore bus_semaphore = new Semaphore(0); // Semaphore for the bus arrival
        Semaphore boarded_semaphore = new Semaphore(0); // Semaphore to signal when riders have boarded
        Queue<Rider> riderQueue = new LinkedList<>(); // Regular queue to hold riders waiting for the bus

        double busMeanInterArrivalTime = 20 * 60 * 12; // For testing
        double riderMeanInterArrivalTime = 30 * 10; // For testing

        new Thread(() -> {
            int busIndex = 0;
            while (busIndex < 5) {
                long busArrivalTime = getExponentialTime(busMeanInterArrivalTime);
                try {
                    Thread.sleep(busArrivalTime); // Bus arrival based on exponential distribution
                    initialize_bus(mutex, bus_semaphore, boarded_semaphore, riderQueue, busIndex);
                    busIndex++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            int riderIndex = 0;
            while (riderIndex < 150) {
                long riderArrivalTime = getExponentialTime(riderMeanInterArrivalTime);
                try {
                    Thread.sleep(riderArrivalTime); // Rider arrival based on exponential distribution
                    initialize_rider(mutex, bus_semaphore, boarded_semaphore, riderQueue, riderIndex);
                    riderIndex++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
