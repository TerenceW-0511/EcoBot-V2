package teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robot.Robot;

@TeleOp
public class SeedbotTele extends OpMode {
    Hardware Robothardware = new Hardware();

    private Constants.TeleOpState state[] = new Constants.TeleOpState[1];

    public void init() {
        Robothardware.bucket = hardwareMap.get(Servo.class, "bucket");
        Robothardware.bucket.setPosition(0.0);
        Robothardware.init(hardwareMap);
        state[0] = Constants.TeleOpState.FREE;

        //hardware.changeSeed();
    }

    public void loop() {
        telemetry.addData("Seed section : ", Robothardware.angle);
        telemetry.addData("Array Part: ", Robothardware.aboveLength);
        boolean input = gamepad1.a;
        boolean input2 = gamepad1.b;

        //Drive inputs
        double forward = gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double rotate = gamepad1.right_stick_x;

        Robothardware.drive(forward,strafe,rotate);


if (input) {
            Robothardware.getAngle(state);
            Methods.SLEEP(250);
            state[0] = Constants.TeleOpState.FREE;
        }
        if (input2){
            Robothardware.changeSeed();
            Robothardware.bucket.setPosition(0);
        }
        /*        else if (input2) {
            Robothardware.ShootSeed(state);
            state[0] = Constants.TeleOpState.FREE;

            if (state[0] != Constants.TeleOpState.FREE) {
                return;
            }

            if (DrillInput) {
                Robothardware.DrillandPlant(state);
            } else if (ChangeSeedInput) {
                Robothardware.changeSeed(state);
            }
            Robothardware.drive(Vertical, Horizontal, Rotation);

            */

        }
    }

