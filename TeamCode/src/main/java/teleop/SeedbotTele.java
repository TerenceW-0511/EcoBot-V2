package teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class SeedbotTele extends OpMode {
    Hardware Robothardware = new Hardware();
    private ElapsedTime drillTimer = new ElapsedTime();
    private enum DrillState { IDLE, LOWERING, RETRACTING }
    private DrillState drillState = DrillState.IDLE;
    private Constants.TeleOpState state[] = new Constants.TeleOpState[1];

    public void init() {
        Robothardware.bucket = hardwareMap.get(Servo.class, "bucket");
        Robothardware.bucket.setPosition(0.0);
        Robothardware.init(hardwareMap);
        state[0] = Constants.TeleOpState.FREE;

        //hardware.changeSeed();
    }

    public void loop() {

        boolean input = gamepad1.a;
        boolean input2 = gamepad1.b;
        boolean input3 = gamepad1.dpad_up;
       // boolean input4 = gamepad1.right_bumper;
        boolean input5= gamepad1.left_bumper;
        boolean input6 = gamepad1.dpad_down;
        //Drive inputs
        double forward = gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x * 1.1;
        double rotate = gamepad1.right_stick_x;

        if (state[0]== Constants.TeleOpState.FREE) {
            Robothardware.drive(-forward, strafe, rotate);
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

       // if(input4){
       //     Robothardware.drillSequence(state);
       // }

        if(input5){
            Robothardware.plantSequence(state);
        }
        if (input6){
            Robothardware.retractZero(state);
        }
        if (gamepad1.dpad_left){
            Robothardware.spitWater(state);
        }

        switch (drillState) {
            case IDLE:
                if (gamepad1.right_bumper) {
                    Robothardware.startDrillDown();
                    drillTimer.reset();
                    drillState = DrillState.LOWERING;
                }
                break;

            case LOWERING:
                if (drillTimer.seconds() >= 5.0) {
                    Robothardware.retractDrill();
                    drillTimer.reset();
                    drillState = DrillState.RETRACTING;
                }
                break;

            case RETRACTING:
                if (drillTimer.seconds() >= 0.5) {
                    drillState = DrillState.IDLE;
                }
                break;
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
        telemetry.addData("Seed section ", Robothardware.getAngle());
        telemetry.addData("Array Part ", Robothardware.getAboveLength());
        telemetry.addData("Seed ", Robothardware.getSeed());
        telemetry.addData("Target Ticks", Robothardware.slides.getCurrentPosition());
        telemetry.addData("Front Left Motor Location", Robothardware.frontLeftMotor.getCurrentPosition());
        telemetry.addData("Front Right Motor Location", Robothardware.frontRightMotor.getCurrentPosition());
        telemetry.addData("Front left power",Robothardware.frontLeftMotor.getPower());
        telemetry.addData("Front Right power",Robothardware.frontRightMotor.getPower());
        telemetry.addData("Back left power",Robothardware.backLeftMotor.getPower());
        telemetry.addData("Back Right power",Robothardware.backRightMotor.getPower());
        telemetry.addData("slide amprage", Robothardware.getSlideCurrent());
        telemetry.update();
        }
    }

