import java.util.concurrent.Semaphore;
import java.util.Random;
import java.util.Queue;

public class Rider extends Thread {
    private Semaphore mutex;
    private Semaphore bus_semaphore;
    private Semaphore boarded_semaphore;
    private Queue<Rider> riderQueue;
    private int index;
    Random random = new Random();

    public Rider(Semaphore mutex, Semaphore bus_semaphore, Semaphore boarded_semaphore, Queue<Rider> riderQueue,
            int index) {
        this.mutex = mutex;
        this.bus_semaphore = bus_semaphore;
        this.boarded_semaphore = boarded_semaphore;
        this.riderQueue = riderQueue;
        this.index = index;
    }

    @Override
    public void run() {
        try {
            // Acquire the mutex to safely add the rider to the queue
            mutex.acquire();
            riderQueue.add(this);
            System.out.println("Rider " + (index + 1) + " arrives. Waiting...");
            mutex.release();

            // Wait for a bus to signal that it is ready for boarding
            bus_semaphore.acquire();
            board(random);
            boarded_semaphore.release(); // Signal that the rider has boarded the bus

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void board(Random random) throws InterruptedException {
        int randomTime = 800 + random.nextInt(401);
        Thread.sleep(randomTime);
        System.out.println("Rider " + (index + 1) + " boards the bus.");
    }

    @Override
    public String toString() {
        return (index + 1) + " ";
    }
}
