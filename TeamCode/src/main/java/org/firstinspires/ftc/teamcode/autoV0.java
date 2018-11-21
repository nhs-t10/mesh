package org.firstinspires.ftc.teamcode;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.opencv.core.Rect;


@Autonomous(name= "auto_v0")
public class autoV0 extends T10_Library {
    /*
        T-10 Preliminary Autonomous

        This is based on the assumption that we are:
            - On the red team
            - Starting such that we are facing the depot first
     */

    // constants and state declaration
    imuData imu;
    Turning turner = new Turning();
    enum state {
        START, TURNING, SAMPLING, MARKING, PARKING, STOP;
    }
    state currentState = null;

    public void init() {
        initialize_robot();
        imu = new imuData(hardwareMap);
        // setTeam(color.blue());
        currentState = state.START;
        start_auto();
    }

    public void loop() {
        /*
        Loop constantly checks state, and then executes a command based on this.
         */
        if(currentState == state.TURNING){
            turnToGold();
        }
        if(currentState == state.SAMPLING){
            sample();
        }
        if(currentState == state.STOP){
            omni(0,0,0);
        }
        if(currentState == state.MARKING){
            mark();
        }
        telemetry.addData("Current State: ", currentState);
        telemetry.addData("Millis since run: ", clock.milliseconds());
    }

    public void start_auto(){
        sleep(1000);
        currentState = state.TURNING;
    }

    public void turnToGold(){
        boolean aligned = gold.getAligned(); // get if gold block is aligned
        if(aligned){
            omni(0,0,0);
            currentState = state.SAMPLING;
        }
        if(!aligned && gold.position == GoldAlignDetector.gold_position.LEFT){
            omni(0,.14f,0); // turn left until detected, once aligned, then sample
        }
        else if(!aligned && gold.position == GoldAlignDetector.gold_position.RIGHT){
            omni(0,-.14f,0); // alternatively, turn right
        }
        else{
            omni(0,.14f,0);
        }
        telemetry.addData("xPos", gold.getXPosition());
        telemetry.addData("Current Gold Position: ", gold.position);
    }

    // Knock gold off (for now)
    public void sample(){
        // Logic for bestRect
        boolean stopped = false;
        boolean scored = false;
        Rect best = gold.getBestRect();
        if(best.height < 120 || best.width < 120) {
            omni(-.2f,0,0);
        }
        else if(best.height > 120 || best.width > 120) {
            sleep(1000);
            stopped = true;
        }
        if(stopped && !scored){
            turner.setDestination((float) (90-turner.currentAngle)); // turn parallel to the cube
            driveFor(1.5,0,0,.2f); // move 1.5 seconds toward the cube, knocking it off
            scored = true;
            stopped = false;
        }
        if(scored && !stopped){
            driveFor(1.5,0,0,-.2f); // drive back for the same amount of seconds
            turner.setDestination((float) (90 + turner.currentAngle)); // turn to the original angle (for now)
            currentState = state.STOP;
        }
    }

    public void mark(){
        boolean marked = false;

        if(!marked) {
            if(!gold.isFound() || gold.getGoldCount() < 5) { // CV will detect if we are in front of crater or not by seeing if we have found any gold blocks
                if (team == TeamWeAreOn.RED && color.red() > 200) { // if we're on the red team and the color sensor detects that we're on red tape
                    stopDrive();
                    dispense_marker();
                    marked = true;
                } else if (team == TeamWeAreOn.BLUE && color.blue() > 200) { // if we're on blue team and we're on blue tape
                    stopDrive();
                    dispense_marker();
                    marked = true;
                } else {
                    omni(.5f, 0, 0);
                }
            }

            else if (gold.getGoldCount() > 5){ // constant for gold needs to be greater than 1, but not too great
                turner.setDestination(55); // this needs to be tuned to the angle that we want, or we can use cv
            }
        }
        else { // this implies that we are already marked

        }
    }

    public void stop() {
        gold.disable();
    }


}