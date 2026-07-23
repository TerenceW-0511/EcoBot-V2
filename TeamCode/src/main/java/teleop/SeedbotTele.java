package teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class SeedbotTele extends OpMode {
    Hardware hardware=new Hardware();
    Hardware bucket = new Hardware();

    public void init(){
        bucket.changeSeed();
    }

    public void loop(){

    }

}
