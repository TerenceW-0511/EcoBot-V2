package teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robot.Robot;

@TeleOp
public class SeedbotTele extends OpMode {
    Hardware Robothardware = new Hardware();

    private Constants.TeleOpState state[] = new Constants.TeleOpState[1];

    public void init(){
        Robothardware.bucket = hardwareMap.get(Servo.class, "bucket");
        Robothardware.bucket.setPosition(0.0);
        state[0] = Constants.TeleOpState.FREE;

        //hardware.changeSeed();
    }

    public void loop()
    {
        telemetry.addData("Seed section : ", Robothardware.GetAngle());
        boolean input = gamepad1.a;
        boolean input2 = gamepad1.b;

        if (input)
        {
            Robothardware.changeSeed(state);
            Methods.SLEEP(250);
            state[0] = Constants.TeleOpState.FREE;
        }
        else if (input2)
        {
            Robothardware.ShootSeed(state);
            state[0] = Constants.TeleOpState.FREE;
        }
    }
    /**
    public void loop(){
        double Horizontal = gamepad1.left_stick_x;
        double Vertical = gamepad1.left_stick_y;

        double Rotation = gamepad1.right_stick_x;

        boolean DrillInput = gamepad1.b;
        boolean ChangeSeedInput = gamepad1.a;;

        if (state[0] != Constants.TeleOpState.FREE)
        {
            return;
        }

        if (DrillInput)
        {
            Robothardware.DrillandPlant(state);
        }
        else if (ChangeSeedInput)
        {
            Robothardware.changeSeed(state);
        }
        Robothardware.drive(Vertical, Horizontal, Rotation);
    }
    **/
}
