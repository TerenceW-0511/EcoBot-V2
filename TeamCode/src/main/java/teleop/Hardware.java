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


    public void init(HardwareMap hwMap) {
        //Configs
        frontLeftMotor = hwMap.get(DcMotor.class, "FrontLeftMotor");
        frontRightMotor = hwMap.get(DcMotor.class, "FrontRightMotor");
        backLeftMotor = hwMap.get(DcMotor.class, "BackLeftMotor");
        backRightMotor = hwMap.get(DcMotor.class, "BackRightMotor");

        drill = hwMap.get(DcMotor.class, "Drill");
        slides.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        bucket= hwMap.get(Servo.class, "bucket");
        //waterTank= hwMap.get(Servo.class, "water");
        bucket.setPosition(-1);

    }

    public void drive(double forward, double strafe, double rotate) {
        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);

        DcMotor motors [] = {frontRightMotor,frontLeftMotor,backRightMotor,backRightMotor};

        for (DcMotor singleMotor:motors){
            singleMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

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
    public void changeAngle(Constants.TeleOpState[] state) {
        state[0] = Constants.TeleOpState.ACTION_OCCUPIED;
        aboveLength++;
        if (aboveLength > Constants.values.length-1) {
            aboveLength = 0;
        }
        angle = Constants.values[aboveLength];
        state[0] = Constants.TeleOpState.FREE;
    }

    public void moveToPlant(Constants.TeleOpState[] state){
        state[0]= Constants.TeleOpState.ACTION_OCCUPIED;

        DcMotor motors [] = {frontRightMotor,frontLeftMotor,backRightMotor,backRightMotor};
        for (DcMotor singleMotor: motors){
            singleMotor.setTargetPosition(singleMotor.getCurrentPosition()-Constants.sequenceTargetTicks);
        }
        frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION); frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightMotor.setPower(1); frontLeftMotor.setPower(1);
        while (frontRightMotor.isBusy() & frontLeftMotor.isBusy()){

        }
        frontRightMotor.setPower(0); frontLeftMotor.setPower(0);
        state[0]=Constants.TeleOpState.FREE;

    }

    // Not in use currently, need to reprogram the entire thing
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
    public int getAboveLength()
    {
        return aboveLength;
    }

}
