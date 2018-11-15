package org.firstinspires.ftc.teamcode;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.opencv.core.Rect;


@Autonomous(name= "auto_v0")
public class autoV0 extends T10_Library {
    boolean stop = false;
    boolean turn = false;
    imuData imu;
    Turning turner = new Turning();
    enum state {
        START, TURNING, SAMPLING, STOP;
    }
    state currentState = null;

    public void init() {
        initialize_robot();
        imu = new imuData(hardwareMap);
        telemetry.addData("IMU: ",imu.toString());
        // setTeam(color.blue());
        currentState = state.START;
    }

    public void loop() {
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
        Rect best = gold.getBestRect();
        if(best.height < 120 || best.width < 120) {
            omni(.2f,0,0);
        }
        else if (turner.currentAngle < 30 || turner.currentAngle > -30) { // if our current angle is smaller than 30 degrees
            turner.setDestination((float) (90 - turner.currentAngle));
        }
        else {
            omni(0,0,.2f);
        }
    }

    public void stop() {
        gold.disable();
    }


}
