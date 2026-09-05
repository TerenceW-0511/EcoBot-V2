package teleop; // make sure this aligns with class location

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import  com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.lynx.LynxModule;
import java.util.List;

import teleop.Constants;
import teleop.Hardware;

@Autonomous(name = "Blue", group = "Blue")
public class auton extends OpMode {

    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer, followerDoneTimer;
    private Hardware robot;
    private boolean followerDoneDelay = false;
    private double curr=0,rpmError = 0;
    private int pathState;
    private double targetTurret;
    private String seed=Constants.seeds[0];

    private double angle;
    private int aboveLength;
    private final Pose startPose = new Pose(33, 134.5, Math.toRadians(270)); // Start Pose of our robot.
    private final Pose scorePose = new Pose(49.5, 102.5, Math.toRadians(265));
    private final Pose toPickup1 = new Pose(41.6,  60.7);
    private final Pose pickup1Pose = new Pose(12, 60.7, Math.toRadians(180)); // -140 Highest (First  Set) of Artifacts from the Spike Mark.
    private final Pose controlPickup1 = new Pose(46,63.9);
//    private final Pose controlPickup1_2 = new Po\e(39.6,69.6);

    private final Pose scorePoses = new Pose(54.6,80,Math.toRadians(210));

    private final Pose openGatePose = new Pose(16,70,Math.toRadians(180));
    private final Pose controlGate = new Pose(35.7,70);
    private final Pose gateBackPose = new Pose(11,55,Math.toRadians(160));
    private final double rotDeg = Math.toRadians(140);
//    private final Pose controlGate = new Pose(29.2,65.2);




    private final Pose pickup2Pose = new Pose(16, 86.4, Math.toRadians(180)); // Middle (Second Set) of Artifacts from the Spike Mark.
//    private final Pose controlPickup2 = new Pose(46.5,60.7);


    private final Pose scorePickup5Pose = new Pose(53.4,112.7,Math.toRadians(180));


//    private final Pose toLoadingPose = new Pose(7,35.5,Math.toRadians(225));
//    private final Pose pickup4Pose = new Pose(9,50,Math.toRadians(270));
//    private final Pose controlPickup4 = new Pose(36.4,50.5);
//
    ////    private final Pose scorePickup4Pose = new Pose(51,117.6,Math.toRadians(180));
//    private final Pose controlScore4Pose = new Pose(38.8,76.6);
    private Path scorePreload;

    private PathChain grabPickup1, scorePickup1, openGate,gateBack,scoreGates,grabPickup2,scorePickup4;

    public void buildPaths() {

    }



    public void autonomousPathUpdate() {
        switch (pathState) {

        }
    }


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

    public void drillSequence(Constants.TeleOpState[] state){
        state[0]= Constants.TeleOpState.ACTION_OCCUPIED;
        robot.slides.setTargetPosition(Constants.SlideLoweredPosition);
        robot.drill.setPower(1);
        robot.slides.setPower(-0.5); // Lowering Slide
        Methods.SLEEP(5000); //Wait for it to finish
        robot.slides.setTargetPosition(0);
        robot.drill.setPower(0);
        robot.slides.setPower(0.5); // Go back to 0
        Methods.SLEEP(250);
        state[0]=Constants.TeleOpState.FREE;
    }
    public void spitWater(Constants.TeleOpState [] state){
        state[0]=Constants.TeleOpState.ACTION_OCCUPIED;
        robot.waterPump.setPower(1);
        Methods.SLEEP(1000);
        robot.waterPump.setPower(0);
        state[0]= Constants.TeleOpState.FREE;
    }
    public void retractZero(Constants.TeleOpState [] state){
        state[0]=Constants.TeleOpState.ACTION_OCCUPIED;
        robot.slides.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.slides.setPower(0.5);
        Methods.SLEEP(250);
        robot.slides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.slides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        state[0]=Constants.TeleOpState.FREE;
    }

    // Not in use currently, need to reprogram the entire thing
    public void plantSequence(Constants.TeleOpState[] state)
    {
        state[0] = Constants.TeleOpState.ACTION_OCCUPIED;
        drillSequence(state);
        robot.moveToPlant(state);
        Methods.SLEEP(100);
        dispenseSeed(state);
        spitWater(state);
        state[0] = Constants.TeleOpState.FREE;
    }

    public void dispenseSeed(Constants.TeleOpState[] state){
        state[0] = Constants.TeleOpState.ACTION_OCCUPIED;
        robot.bucket.setPosition(angle);
        Methods.SLEEP(1000);
        robot.bucket.setPosition(0);
        Methods.SLEEP(1000);
        robot.bucket.setPosition(Constants.values[7]);
        state[0] = Constants.TeleOpState.FREE;
    }

    /**
     * These change the states of the paths and actions. It will also reset the timers of the individual switches
     **/
    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
        followerDoneDelay=false;
    }

    /**
     * This is the main loop of the OpMode, it will run repeatedly after clicking "Play".
     **/
    @Override
    public void loop() {
        // These loop the movements of the robot, these must be called continuously in order to work
        follower.update();
    }

    /**
     * This method is called once at the init of the OpMode.
     **/
    @Override
    public void init() {
        pathTimer = new Timer();
        followerDoneTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();
    }

    /**
     * This method is called continuously after Init while waiting for "play".
     **/
    @Override
    public void init_loop() {
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
    }

    /**
     * This method is called once at the start of the OpMode.
     * It runs all the setup actions, including building paths and starting the path system
     **/
    @Override
    public void start() {
        Hardware robot = new Hardware();
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    /**
     * We do not use this because everything should automatically disable
     **/
    @Override
    public void stop() {
//        robot.ll.stop();
    }
}