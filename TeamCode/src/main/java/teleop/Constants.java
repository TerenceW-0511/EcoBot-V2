package teleop;

// Stores all predetermined angles of the seedbot
public class Constants {

     public enum TeleOpState {ACTION_OCCUPIED, FREE};
     static final double values[] = {0, 0.075, 0.23, 0.35, 0.47, 0.6, 0.75, 0.9, 1}; // angle values of each section

     //static final double angle1=
     static final int TimeOfSlideExtending = 10000; // In Miliseconds
     static final int TimeOfDrilling = 10000; // In Miliseconds

     static final int sequenceTargetTicks = 500; // Tick rate cal: Wheel Diameter (104) /Distance of drill-seed dispenser (35)* Encoder Tick rate (537.8).

     static final int SlideLoweredPosition = 0; // Ticks for the slide (currently undetermined)
}
