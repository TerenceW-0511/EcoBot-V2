package autonomus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous (name = "SeedbotAuto")
public class Autonomus extends OpMode {
    private DcMotor frontLeftMotor;

    @Override
    public void init()
    {
        frontLeftMotor = HardwareMap.get(DcMotor.class, "FrontLeftMotor");
        frontRightMotor = HardwareMap.get(DcMotor.class, "FrontRightMotor");
        backLeftMotor = HardwareMap.get(DcMotor.class, "BackLeftMotor");
        backRightMotor = HardwareMap.get(DcMotor.class, "BackRightMotor");
        drill = HardwareMap.get(DcMotor.class, "drill");
        slides=HardwareMap.get(DcMotor.class, "slides");
        bucket= HardwareMap.get(Servo.class, "bucket");

    }
    @Override
    public void loop()
    {

    }
}
