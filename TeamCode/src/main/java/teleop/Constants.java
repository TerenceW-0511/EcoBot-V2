package teleop;

// Stores all predetermined angles of the seedbot
public class Constants {

     public enum TeleOpState {ACTION_OCCUPIED, FREE};
     static final double values[] = {-1.0, -0.75, -0.5, -0.25, 0.0, 0.25, 0.5, 0.75, 1}; // angle values of each section

     //static final double angle1=
     static final int TimeOfSlideExtending = 10000; // In Miliseconds
     static final int TimeOfDrilling = 10000; // In Miliseconds
}
