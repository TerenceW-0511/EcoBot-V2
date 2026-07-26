package teleop;

import android.app.usage.ConfigurationStats;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

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
        slides=hwMap.get(DcMotor.class, "slides");
        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
       // drill = hwMap.get(DcMotor.class, "Drill");
        slides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        bucket= hwMap.get(Servo.class, "bucket");
        //waterTank= hwMap.get(Servo.class, "water");
        bucket.setPosition(-1);
        DcMotor motors [] = {frontRightMotor,frontLeftMotor,backLeftMotor,backRightMotor};

        for (DcMotor singleMotor:motors){
            singleMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            singleMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            singleMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

    }

    public void drive(double forward, double strafe, double rotate) {

        double denominator = Math.max(Math.abs(forward) + Math.abs(strafe) + Math.abs(rotate), 1);

        double frontLeftPower = (forward + strafe + rotate)/denominator;
        double backLeftPower = (forward - strafe + rotate)/denominator;
        double frontRightPower = (forward - strafe - rotate)/denominator;
        double backRightPower = (forward + strafe - rotate)/denominator;

        frontLeftMotor.setPower(frontLeftPower);
        frontRightMotor.setPower(backLeftPower);
        backLeftMotor.setPower(frontRightPower);
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

    public void extend(Constants.TeleOpState[] state){
        slides.setTargetPosition(1);
        slides.setPower(1);
        while (slides.isBusy()){

        }
        slides.setPower(0.0);
    }

  /*  // Not in use currently, need to reprogram the entire thing
    public void PlantProcess(Constants.TeleOpState[] state)
    {
        int curr = slides.getCurrentPosition();
        state[0] = Constants.TeleOpState.ACTION_OCCUPIED;

        slides.setTargetPosition(Constants.SlideLoweredPosition); // Fixed location, not offset
        slides.setPower(1.0);

        drill.setPower(1.0);
        while (slides.isBusy()) {}
        slides.setPower(0.0);

        Methods.SLEEP(5000); // 5 seconds of drilling

        drill.setPower(0.0);
        slides.setTargetPosition(curr);
        slides.setPower(1.0);

        // Planting process
        moveToPlant(state);
        dispenseSeed(state);

        state[0] = Constants.TeleOpState.FREE;
    }
*/
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
