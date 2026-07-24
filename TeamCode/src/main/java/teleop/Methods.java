package teleop;

public class Methods {
    public static void switchPosition (){

        /*switch(){

        }

        bucket.setPosition();*/
    }
    public static boolean SLEEP(int milliseconds)
    {
        try {
            Thread.sleep(milliseconds); // Compiler forced me to use a try-catch approach
        } catch (InterruptedException e)
        {
            // return false if failed
             return false;
        }
        return true;
    }
}
