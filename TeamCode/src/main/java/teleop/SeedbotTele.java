package teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.robot.Robot;

@TeleOp
public class SeedbotTele extends OpMode {
    Hardware Robothardware = new Hardware();

    private Constants.TeleOpState state[] = new Constants.TeleOpState[1];

    public void init(){
        state[0] = Constants.TeleOpState.FREE;
        //hardware.changeSeed();
    }

    public void loop(){
        double Horizontal = gamepad1.left_stick_x;
        double Vertical = gamepad1.left_stick_y;

        double Rotation = gamepad1.right_stick_x;

        boolean DrillInput = gamepad1.b;
        boolean ChangeSeedInput = gamepad1.a;;

        if ((DrillInput || ChangeSeedInput) && (state[0] != Constants.TeleOpState.ACTION_OCCUPIED))
        {
            state[0] = Constants.TeleOpState.ACTION_OCCUPIED;
            if (DrillInput)
            {
                Robothardware.DrillandPlant(state);
            }
            else if (ChangeSeedInput)
            {
                Robothardware.changeSeed(state);
            }
        }

        if (((Horizontal == 0 && Vertical == 0) && Rotation == 0) || (state[0] == Constants.TeleOpState.ACTION_OCCUPIED))
        {
            return;
        }
        Robothardware.drive(Vertical, Horizontal, Rotation);
    }

}
