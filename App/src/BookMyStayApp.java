import java.util.HashMap;
import java.util.Map;

/**
 * =====================================================
 * Book My Stay App
 * Use Case 3: Centralized Room Inventory Management
 * =====================================================
 *
 * Demonstrates abstraction, inheritance,
 * polymorphism, and centralized inventory using HashMap.
 *
 * @version 3.1
 */

// =====================================================
// ABSTRACT CLASS - Room
// =====================================================
abstract class Room {

    protected int numberOfBeds;
    protected int squareFeet;
    protected double pricePerNight;

    public Room(int numberOfBeds, int squareFeet, double pricePerNight) {
        this.numberOfBeds = numberOfBeds;
        this.squareFeet = squareFeet;
        this.pricePerNight = pricePerNight;
    }

    public void displayRoomDetails() {
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Size: " + squareFeet + " sqft");
        System.out.println("Price per night: " + pricePerNight);
    }
}

// =====================================================
// CLASS - SingleRoom
// =====================================================
class SingleRoom extends Room {
    public SingleRoom() {
        super(1, 250, 1500.0);
    }
}

// =====================================================
// CLASS - DoubleRoom
// =====================================================
class DoubleRoom extends Room {
    public DoubleRoom() {
        super(2, 400, 2500.0);
    }
}

// =====================================================
// CLASS - SuiteRoom
// =====================================================
class SuiteRoom extends Room {
    public SuiteRoom() {
        super(3, 750, 5000.0);
    }
}

// =====================================================
// CLASS - RoomInventory
// =====================================================
class RoomInventory {

    // Key -> Room Type
    // Value -> Available Count
    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        initializeInventory();
    }

    private void initializeInventory() {
        roomAvailability.put("Single", 5);
        roomAvailability.put("Double", 3);
        roomAvailability.put("Suite", 2);
    }

    public Map<String, Integer> getRoomAvailability() {
        return roomAvailability;
    }

    public void updateAvailability(String roomType, int count) {
        roomAvailability.put(roomType, count);
    }
}

// =====================================================
// MAIN CLASS - BookMyStayApp
// =====================================================
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Hotel Room Inventory Status\n");

        // Domain objects (Polymorphism)
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        // Centralized Inventory
        RoomInventory inventory = new RoomInventory();

        // Display Single Room
        System.out.println("Single Room:");
        singleRoom.displayRoomDetails();
        System.out.println("Available Rooms: "
                + inventory.getRoomAvailability().get("Single"));
        System.out.println();

        // Display Double Room
        System.out.println("Double Room:");
        doubleRoom.displayRoomDetails();
        System.out.println("Available Rooms: "
                + inventory.getRoomAvailability().get("Double"));
        System.out.println();

        // Display Suite Room
        System.out.println("Suite Room:");
        suiteRoom.displayRoomDetails();
        System.out.println("Available Rooms: "
                + inventory.getRoomAvailability().get("Suite"));
    }
}