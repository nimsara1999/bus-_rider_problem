import java.util.concurrent.Semaphore;

public class Rider extends Thread {
    private Semaphore mutex;
    private Semaphore bus;
    private Semaphore boarded;
    private int[] waiting;
    private int index;

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
            board();
            boarded.release(); // Signal that the rider has boarded the bus

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void board() {
        System.out.println(index + 1 + " Rider boards the bus.");s
    }
}
