import java.util.concurrent.Semaphore;
import java.util.Random;

public class Rider extends Thread {
    private Semaphore mutex;
    private Semaphore bus;
    private Semaphore boarded;
    private int[] waiting;
    private int index;
    Random random = new Random();

    public Rider(Semaphore mutex, Semaphore bus, Semaphore boarded, int[] waiting, int index) {
        this.mutex = mutex;
        this.bus = bus;
        this.boarded = boarded;
        this.waiting = waiting;
        this.index = index;
    }

    @Override
    public void run() {
        try {
            // Acquire the mutex to increment the number of waiting riders
            mutex.acquire();
            waiting[0]++;
            System.out.println("Rider arrives. Riders waiting: " + waiting[0]);
            mutex.release();

            // Wait for a bus to signal that it is ready for boarding
            bus.acquire();
            board(random);
            boarded.release(); // Signal that the rider has boarded the bus

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void board(Random random) throws InterruptedException {
        int randomTime = 800 + random.nextInt(401);
        Thread.sleep(randomTime);
        System.out.println(index + 1 + " Rider boards the bus.");
    }
}
