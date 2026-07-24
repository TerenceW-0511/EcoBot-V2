package teleop;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

import java.util.concurrent.TimeUnit;

public class Hardware {
    private DcMotor frontRightMotor, frontLeftMotor, backRightMotor, backLeftMotor, slides, drill;
    public Servo bucket;
    private Servo waterTank;

    private double angle = 0.0;
    private int section = 0;

    public double GetAngle()
    {
        return angle;
    }


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
        bucket.setPosition(0);

    }
        public void drive(double forward, double strafe, double rotate) {
            double frontLeftPower = forward + strafe + rotate;
            double backLeftPower = forward - strafe + rotate;
            double frontRightPower = forward - strafe - rotate;
            double backRightPower = forward + strafe - rotate;

            double maxPower = 1;

            maxPower = Math.max(maxPower, Math.abs(frontLeftPower));
            maxPower = Math.max(maxPower, Math.abs(frontRightPower));
            maxPower = Math.max(maxPower, Math.abs(backLeftPower));
            maxPower = Math.max(maxPower, Math.abs(backRightPower));

            frontLeftMotor.setPower((frontLeftPower/maxPower));
            frontRightMotor.setPower((frontRightPower/maxPower));
            backLeftMotor.setPower((backLeftPower/maxPower));
            backRightMotor.setPower((backRightPower/maxPower));
        }

        // Seed mechanism
        public void changeSeed(Constants.TeleOpState[] state){
            state[0] = Constants.TeleOpState.ACTION_OCCUPIED;
            angle += 0.125;
            if ((0.0 <= angle) && (angle <= 1.0))
            {
                return;
            }
            if (angle > 1.0)
            {
                angle = 0.0;
            }
            state[0] = Constants.TeleOpState.FREE;

        }

        public void ShootSeed(Constants.TeleOpState[] state)
        {
            state[0] = Constants.TeleOpState.ACTION_OCCUPIED;
            bucket.setPosition(0.0); // security feature: make sure that seeds aren't leaking out
            bucket.setPosition(angle);

            Methods.SLEEP(1000);

            bucket.setPosition(0.0);
            state[0] = Constants.TeleOpState.FREE;
        }

        // Code might be a bit too verbose, but it will hold for now
        public void DrillandPlant(Constants.TeleOpState[] state)
        {
            state[0] = Constants.TeleOpState.ACTION_OCCUPIED;

            state[0] = Constants.TeleOpState.FREE;
        }

}
