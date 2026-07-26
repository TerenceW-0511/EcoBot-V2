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
        telemetry.addData("Seed section : ", Robothardware.getAngle());
        telemetry.addData("Array Part: ", Robothardware.getAboveLength());
        telemetry.addData("Sequance Target Ticks", Constants.sequenceTargetTicks);
        telemetry.addData("Front Left Motor Location", Robothardware.frontLeftMotor.getCurrentPosition());
        telemetry.addData("Front Right Motor Location", Robothardware.frontRightMotor.getCurrentPosition());

        boolean input = gamepad1.a;
        boolean input2 = gamepad1.b;
        boolean input3 = gamepad1.dpad_up;

        //Drive inputs
        double forward = gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double rotate = gamepad1.right_stick_x;

        if (state[0]== Constants.TeleOpState.FREE) {
            Robothardware.drive(forward, strafe, rotate);
        }

        if(input3){
            Robothardware.moveToPlant(state);
        }

        if (input) {
            Robothardware.changeAngle(state);
            Methods.SLEEP(250);
            state[0] = Constants.TeleOpState.FREE;
        }
        if (input2){
            Robothardware.dispenseSeed(state);
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

