import java.util.concurrent.Semaphore;
import java.util.Queue;

public class Bus extends Thread {
    private Semaphore mutex;
    private Semaphore bus_semaphore;
    private Semaphore boarded_semaphore;
    private Queue<Rider> riderQueue;
    private static final int BUS_CAPACITY = 50;
    private int index;

    public Bus(Semaphore mutex, Semaphore bus_semaphore, Semaphore boarded_semaphore, Queue<Rider> riderQueue,
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
            // Acquire the mutex to safely access the queue
            mutex.acquire();

            int number_of_riders_selected_to_board_bus = Math.min(riderQueue.size(), BUS_CAPACITY);
            if (number_of_riders_selected_to_board_bus == 0) {
                System.out
                        .println("Bus " + (index + 1) + " arrives, but no riders are waiting. Departing immediately.");
                mutex.release();
                depart();
                return;
            }

            System.out.println("\nBus " + (index + 1) + " arrives, riders waiting: " + riderQueue.size() + ", allowing "
                    + number_of_riders_selected_to_board_bus + " to board.");

            // Signal riders to board the bus
            for (int i = 0; i < number_of_riders_selected_to_board_bus; i++) {
                Rider rider = riderQueue.poll(); // Safely dequeue the rider
                if (rider != null) {
                    bus_semaphore.release(); // Signal the rider to board
                }
            }

            mutex.release(); // Release the mutex after accessing the queue

            // Wait for all signaled riders to board
            for (int i = 0; i < number_of_riders_selected_to_board_bus; i++) {
                boarded_semaphore.acquire(); // Wait until each rider signals that they have boarded
            }

            depart();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void depart() {
        System.out.println("Bus " + (index + 1) + " departs. " + riderQueue.size() + " riders waiting for next bus.\n");
        String waitingRiders = String.join(", ", riderQueue.stream().map(Rider::toString).toArray(String[]::new));
        System.out.println("Riders still waiting for the next bus: [" + waitingRiders + "]");
        System.out.println("------------------------------------------------------------\n");
    }
}
