import java.util.concurrent.Semaphore;

public class Bus extends Thread {
    private Semaphore mutex;
    private Semaphore bus;
    private Semaphore boarded;
    private int[] waiting;
    private static final int BUS_CAPACITY = 50;
    private  int index;

    public Bus(Semaphore mutex, Semaphore bus, Semaphore boarded, int[] waiting, int index) {
        this.mutex = mutex;
        this.bus = bus;
        this.boarded = boarded;
        this.waiting = waiting;
        this.index = index;
    }

    @Override
    public void run() {
        try {
            // Acquire mutex to access the shared variable "waiting"
            mutex.acquire();

            // Calculate how many riders can board, max capacity is 50
            int n = Math.min(waiting[0], BUS_CAPACITY);
            System.out.println();
            System.out.println(index+1 + " Bus arrives, riders waiting: " + waiting[0] + ", allowing " + n + " to board.");

            // Signal riders to board the bus
            for (int i = 0; i < n; i++) {
                bus.release();  // Signal to a rider that they can board
            }

            // After signaling, release the mutex for other riders to continue arriving
            mutex.release();

            // Wait for all signaled riders to board
            for (int i = 0; i < n; i++) {
                boarded.acquire();  // Wait until each rider signals that they have boarded
            }

            // After all riders have boarded, adjust the number of waiting riders
            mutex.acquire();
            waiting[0] = Math.max(waiting[0] - BUS_CAPACITY, 0);  // Update waiting riders
            mutex.release();

            // The bus can now depart
            depart();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void depart() {
        System.out.println("Bus departs.");
    }
}
