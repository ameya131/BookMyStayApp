import java.util.*;

/**
 * CLASS - Reservation
 */
class Reservation {
    String guestName;
    String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

/**
 * CLASS - BookingRequestQueue
 */
class BookingRequestQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.add(r);
    }

    public Reservation getNextRequest() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

/**
 * CLASS - RoomInventory
 */
class RoomInventory {

    private Map<String, Integer> rooms = new HashMap<>();

    public RoomInventory() {
        rooms.put("Single", 2);
        rooms.put("Double", 2);
        rooms.put("Suite", 1);
    }

    public boolean allocateRoom(String roomType) {
        int count = rooms.getOrDefault(roomType, 0);
        if (count > 0) {
            rooms.put(roomType, count - 1);
            return true;
        }
        return false;
    }

    public Map<String, Integer> getRooms() {
        return rooms;
    }
}

/**
 * CLASS - RoomAllocationService
 */
class RoomAllocationService {

    private Map<String, Integer> roomCounters = new HashMap<>();

    public String allocateRoom(Reservation r, RoomInventory inventory) {

        if (inventory.allocateRoom(r.roomType)) {

            int count = roomCounters.getOrDefault(r.roomType, 0) + 1;
            roomCounters.put(r.roomType, count);

            String roomId = r.roomType + "-" + count;

            System.out.println("Booking confirmed for Guest: " +
                    r.guestName + ", Room ID: " + roomId);

            return roomId;
        } else {
            System.out.println("No rooms available for " + r.roomType);
            return null;
        }
    }
}

/**
 * CLASS - ConcurrentBookingProcessor
 */
class ConcurrentBookingProcessor implements Runnable {

    private BookingRequestQueue bookingQueue;
    private RoomInventory inventory;
    private RoomAllocationService allocationService;

    public ConcurrentBookingProcessor(
            BookingRequestQueue bookingQueue,
            RoomInventory inventory,
            RoomAllocationService allocationService) {

        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
        this.allocationService = allocationService;
    }

    @Override
    public void run() {

        while (true) {
            Reservation reservation;

            // synchronized queue access
            synchronized (bookingQueue) {
                if (bookingQueue.isEmpty()) {
                    break;
                }
                reservation = bookingQueue.getNextRequest();
            }

            // synchronized inventory allocation
            synchronized (inventory) {
                allocationService.allocateRoom(reservation, inventory);
            }
        }
    }
}

/**
 * MAIN CLASS
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Concurrent Booking Simulation\n");

        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService allocationService = new RoomAllocationService();

        // Add booking requests
        bookingQueue.addRequest(new Reservation("Abhi", "Single"));
        bookingQueue.addRequest(new Reservation("Vannathi", "Double"));
        bookingQueue.addRequest(new Reservation("Kunal", "Suite"));
        bookingQueue.addRequest(new Reservation("Subha", "Single"));

        // Create threads
        Thread t1 = new Thread(
                new ConcurrentBookingProcessor(bookingQueue, inventory, allocationService)
        );

        Thread t2 = new Thread(
                new ConcurrentBookingProcessor(bookingQueue, inventory, allocationService)
        );

        // Start threads
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Thread execution interrupted.");
        }

        // Print remaining inventory
        System.out.println("\nRemaining Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.getRooms().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}