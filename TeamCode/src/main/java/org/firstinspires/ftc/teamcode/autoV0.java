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
    }

    public void loop() {
        /*
        Loop constantly checks state, and then executes a command based on this.
         */
        start_auto();
        if(currentState == state.TURNING){
            turnToGold();
        }
        if(currentState == state.SAMPLING){
            sample();
        }
        if(currentState == state.STOP){
            omni(0,0,0);
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
        if(!aligned && gold.isLeft){ // area constant needs to be tuned, such that robot doesn't drive towards far away blocks.
            omni(0,-.14f,0); // turn until detected, once aligned, then sample
        }
        else if(!aligned && !gold.isLeft){
            omni(0,.14f,0);
        }
        else{
            currentState = state.SAMPLING;
        }
        telemetry.addData("xPos", gold.getXPosition());
        telemetry.addData("isLeft?", gold.isLeft);
    }


    // Knock gold off (for now)
    public void sample(){
        // Logic for bestRect
        boolean stopped = false;
        boolean scored = false;
        Rect best = gold.getBestRect();
        if(best.height < 120 || best.width < 120) {
            omni(.2f,0,0);
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

    public void stop() {
        gold.disable();
    }


}
