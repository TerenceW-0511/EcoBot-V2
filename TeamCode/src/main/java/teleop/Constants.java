package teleop;

// Stores all predetermined angles of the seedbot
public class Constants {

     public enum TeleOpState {ACTION_OCCUPIED, FREE};
     static final double values[] = new double[8]; // angle values of each section

     //static final double angle1=
     static final int TimeOfSlideExtending = 10000; // In Miliseconds
     static final int TimeOfDrilling = 10000; // In Miliseconds
}
