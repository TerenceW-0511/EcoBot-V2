package teleop;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.concurrent.TimeUnit;

public class Hardware {
    public DcMotor frontRightMotor, frontLeftMotor, backRightMotor, backLeftMotor, slides, drill;
    public Servo bucket, waterTank;
    Double angle;

    int aboveLength;

    public void init(HardwareMap hwMap) {
        //Configs
        frontLeftMotor = hwMap.get(DcMotor.class, "FrontLeftMotor");
        frontRightMotor = hwMap.get(DcMotor.class, "FrontRightMotor");
        backLeftMotor = hwMap.get(DcMotor.class, "BackLeftMotor");
        backRightMotor = hwMap.get(DcMotor.class, "BackRightMotor");

        bucket= hwMap.get(Servo.class, "bucket");
        //waterTank= hwMap.get(Servo.class, "water");

        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        bucket.setPosition(-1);

    }
        public void drive(double forward, double strafe, double rotate) {
            double frontLeftPower = forward + strafe + rotate;
            double backLeftPower = forward - strafe + rotate;
            double frontRightPower = forward - strafe - rotate;
            double backRightPower = forward + strafe - rotate;

            double maxPower = 1;
            double maxSpeed = 1;

            maxPower = Math.max(maxPower, Math.abs(frontLeftPower));
            maxPower = Math.max(maxPower, Math.abs(frontRightPower));
            maxPower = Math.max(maxPower, Math.abs(backLeftPower));
            maxPower = Math.max(maxPower, Math.abs(backRightPower));

            frontLeftMotor.setPower(maxSpeed * (frontLeftPower/maxPower));
            frontRightMotor.setPower(maxSpeed * (frontRightPower/maxPower));
            backLeftMotor.setPower(maxSpeed * (backLeftPower/maxPower));
            backRightMotor.setPower(maxSpeed * (backRightPower/maxPower));
        }

        // Seed mechanism
        public void getAngle(Constants.TeleOpState[] state) {
            aboveLength++;
            if (aboveLength > Constants.values.length-1) {
                aboveLength = 0;
            }
            angle = Constants.values[aboveLength]
            ;
        }



        // Code might be a bit too verbose, but it will hold for now
        public void DrillandPlant(Constants.TeleOpState[] state)
        {
            state[0] = Constants.TeleOpState.ACTION_OCCUPIED;
            drill.setDirection(DcMotorSimple.Direction.FORWARD);
            drill.setPower(1.0);

            slides.setDirection(DcMotorSimple.Direction.FORWARD);
            slides.setPower(1.0);
            // until slide is revealed, We prob need a detector
            if (!Methods.SLEEP(Constants.TimeOfSlideExtending)) {
                // Halt function if fails
                state[0] = Constants.TeleOpState.FREE;
                return;
            }
            slides.setPower(0.0);

            if (!Methods.SLEEP(Constants.TimeOfDrilling)) {
                // Halt function if fails
                state[0] = Constants.TeleOpState.FREE;
                return;
            }

            slides.setPower(-1.0);
            drill.setPower(0);
            // until slide is revealed, We prob need a detector
            if (!Methods.SLEEP(Constants.TimeOfSlideExtending)) {
                // Halt function if fails
                state[0] = Constants.TeleOpState.FREE;
                return;
            }

            slides.setPower(0.0);
            state[0] = Constants.TeleOpState.FREE;
        }

        public void changeSeed(){
            bucket.setPosition(angle);
            Methods.SLEEP(1000);
            bucket.setPosition(0);
        }

}
