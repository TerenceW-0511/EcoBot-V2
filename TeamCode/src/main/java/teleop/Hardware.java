package teleop;

import android.app.usage.ConfigurationStats;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.opencv.core.Mat;

import java.util.concurrent.TimeUnit;

public class Hardware {
    public DcMotor frontRightMotor, frontLeftMotor, backRightMotor, backLeftMotor, slides, drill;
    public Servo bucket, waterTank;
    private double angle;
    private int aboveLength;

    private String seed=Constants.seeds[0];

    public void init(HardwareMap hwMap) {
        //Configs
        frontLeftMotor = hwMap.get(DcMotor.class, "FrontLeftMotor");
        frontRightMotor = hwMap.get(DcMotor.class, "FrontRightMotor");
        backLeftMotor = hwMap.get(DcMotor.class, "BackLeftMotor");
        backRightMotor = hwMap.get(DcMotor.class, "BackRightMotor");
        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);


        drill = hwMap.get(DcMotor.class, "drill");

        //Slides Initialize
        slides=hwMap.get(DcMotor.class, "slides");
        slides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slides.setTargetPosition(0);
        slides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slides.setPower(1);

        bucket= hwMap.get(Servo.class, "bucket");
        //waterTank= hwMap.get(Servo.class, "water");
        bucket.setPosition(-1);
        DcMotor motors [] = {frontRightMotor,frontLeftMotor,backLeftMotor,backRightMotor};

        for (DcMotor singleMotor:motors){
            singleMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            singleMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

    }

    public void drive(double forward, double strafe, double rotate) {
        DcMotor motors [] = {frontRightMotor,frontLeftMotor,backLeftMotor,backRightMotor};

        for (DcMotor singleMotor:motors){
            singleMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        /**
        double maxPower = 1.0;
        /**
        maxPower = Math.max(maxPower, Math.abs(forward + strafe + rotate));
        maxPower = Math.max(maxPower, Math.abs(forward - strafe + rotate));
        maxPower = Math.max(maxPower, Math.abs(forward - strafe - rotate));
        maxPower = Math.max(maxPower, Math.abs(forward + strafe - rotate));
        **/
        double denominator = Math.max(Math.abs(forward) + Math.abs(strafe) + Math.abs(rotate), 1);
        /**
        double frontLeftPower = (forward + strafe + rotate) / maxPower;
        double backLeftPower = (forward - strafe + rotate) / maxPower;
        double frontRightPower = (forward - strafe - rotate) / maxPower;
        double backRightPower = (forward + strafe - rotate) / maxPower;
         **/
        double frontLeftPower = (forward + strafe + rotate) / denominator;
        double backLeftPower = (forward - strafe + rotate) / denominator;
        double frontRightPower = (forward - strafe - rotate) / denominator;
        double backRightPower = (forward + strafe - rotate) / denominator;



        frontLeftMotor.setPower(frontLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backLeftMotor.setPower(backLeftPower);
        backRightMotor.setPower(backRightPower);

    }

        // Seed mechanism
    public void changeAngle(Constants.TeleOpState[] state) {
        state[0] = Constants.TeleOpState.ACTION_OCCUPIED;
        aboveLength++;
        if (aboveLength > Constants.values.length-1) {
            aboveLength = 0;
        }
        angle = Constants.values[aboveLength];
        seed = Constants.seeds[aboveLength];
        state[0] = Constants.TeleOpState.FREE;
    }

    public void moveToPlant(Constants.TeleOpState[] state){
        state[0]= Constants.TeleOpState.ACTION_OCCUPIED;

        DcMotor motors [] = {frontRightMotor,frontLeftMotor,backLeftMotor,backRightMotor};
        for (DcMotor singleMotor: motors){
            singleMotor.setTargetPosition(singleMotor.getCurrentPosition()-Constants.sequenceTargetTicks);
        }
        for(DcMotor singleMotor: motors){
            singleMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        for(DcMotor singleMotor : motors){
            singleMotor.setPower(1);
        }
        while (frontRightMotor.isBusy() & frontLeftMotor.isBusy()){

        }
        for (DcMotor singleMotor: motors){
            singleMotor.setPower(0);
        }
        state[0]=Constants.TeleOpState.FREE;
    }

    public void drillSequence(Constants.TeleOpState[] state){
        state[0]= Constants.TeleOpState.ACTION_OCCUPIED;
        slides.setTargetPosition(Constants.SlideLoweredPosition);
        drill.setPower(1);
        slides.setPower(-0.5); // Lowering Slide
        Methods.SLEEP(5000); //Wait for it to finish
        slides.setTargetPosition(0);
        drill.setPower(0);
        slides.setPower(0.8); // Go back to 0
        Methods.SLEEP(1000);
        slides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slides.setPower(0.0); //Hold Position Up
        state[0]=Constants.TeleOpState.FREE;
    }

   // Not in use currently, need to reprogram the entire thing
    public void plantSequence(Constants.TeleOpState[] state)
    {
        state[0] = Constants.TeleOpState.ACTION_OCCUPIED;
        drillSequence(state);
        moveToPlant(state);
        Methods.SLEEP(100);
        dispenseSeed(state);
        state[0] = Constants.TeleOpState.FREE;
    }

    public void dispenseSeed(Constants.TeleOpState[] state){
        state[0] = Constants.TeleOpState.ACTION_OCCUPIED;
        bucket.setPosition(angle);
        Methods.SLEEP(1000);
        bucket.setPosition(0);
        state[0] = Constants.TeleOpState.FREE;
    }

    public double getAngle()
    {
        return angle;

    }
    public String getSeed()
    {
        return seed;

    }
    public int getAboveLength()
    {
        return aboveLength;
    }

}
