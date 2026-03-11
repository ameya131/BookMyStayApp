import java.util.*;

/* Reservation Class */
class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

/* Room Inventory */
class RoomInventory {

    private Map<String, Integer> availability;

    public RoomInventory() {
        availability = new HashMap<>();
        availability.put("Single", 2);
        availability.put("Suite", 1);
    }

    public int getAvailableRooms(String type) {
        return availability.getOrDefault(type, 0);
    }

    public void decrementRoom(String type) {
        availability.put(type, availability.get(type) - 1);
    }
}

/* Room Allocation Service */
class RoomAllocationService {

    private Set<String> allocatedRoomIds;
    private Map<String, Set<String>> assignedRoomsByType;
    private Map<String, Integer> counters;

    public RoomAllocationService() {
        allocatedRoomIds = new HashSet<>();
        assignedRoomsByType = new HashMap<>();
        counters = new HashMap<>();
    }

    public void allocateRoom(Reservation reservation, RoomInventory inventory) {

        String type = reservation.getRoomType();

        if (inventory.getAvailableRooms(type) <= 0) {
            System.out.println("No rooms available for " + type);
            return;
        }

        String roomId = generateRoomId(type);

        allocatedRoomIds.add(roomId);

        assignedRoomsByType
                .computeIfAbsent(type, k -> new HashSet<>())
                .add(roomId);

        inventory.decrementRoom(type);

        System.out.println("Booking confirmed for Guest: "
                + reservation.getGuestName()
                + ", Room ID: "
                + roomId);
    }

    private String generateRoomId(String roomType) {

        int count = counters.getOrDefault(roomType, 0) + 1;
        counters.put(roomType, count);

        return roomType + "-" + count;
    }
}

/* Main Class */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Room Allocation Processing");

        RoomInventory inventory = new RoomInventory();

        RoomAllocationService allocator = new RoomAllocationService();

        Reservation r1 = new Reservation("Abhi", "Single");
        Reservation r2 = new Reservation("Subha", "Single");
        Reservation r3 = new Reservation("Vanmathi", "Suite");

        allocator.allocateRoom(r1, inventory);
        allocator.allocateRoom(r2, inventory);
        allocator.allocateRoom(r3, inventory);
    }
}