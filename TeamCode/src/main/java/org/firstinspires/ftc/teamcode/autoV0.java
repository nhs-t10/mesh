package org.firstinspires.ftc.teamcode;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.ftccommon.SoundPlayer;

import org.opencv.core.Rect;


@Autonomous(name= "auto_v0")
public class autoV0 extends T10_Library {
    // Constants and enums
    boolean stop = false;
    boolean turn = false;
    imuData imu;
    Turning turner = new Turning();
    // Autonomous cycles through finite states, each state 'activates' a new function in loop
    enum state {
        START, TURNING_GOLD, SAMPLING, STOP;
    }
    enum SIDE { // what side we are on
        SAMPLING, DEPOT
    }
    state currentState = null;
    SIDE side = null;

    public void init() { // init robot
        initialize_robot();
        imu = new imuData(hardwareMap);
        telemetry.addData("IMU: ",imu.toString()); // make sure IMU is detected
        //setTeam(color.blue()); // Set our team color based on color sensor reading the tape
        currentState = state.START; // Start the robot
    }

    public void loop() {
        start_auto();
        if(currentState == state.TURNING_GOLD){
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
        currentState = state.TURNING_GOLD;
    }

    public void turnToGold(){
        boolean aligned = gold.getAligned(); // get if gold block is aligned
        if(!aligned && gold.getArea() > 25){ // area constant needs to be tuned, such that robot doesn't drive towards far away blocks.
            omni(0,.14f,0); // turn until detected, once aligned, then sample
        }
        else{
            currentState = state.SAMPLING;
        }
    }

    // Knock gold off (for now)
    public void sample(){
        // Logic for bestRect
        Rect best = gold.getBestRect();
        if(best.height < 120 || best.width < 120){ // drive until height or width of best rect is greater than 120
            omni(.2f,0,0);
        }
        else {
            currentState = state.STOP; // stop robot
        }
    }
    /*
    Things to do:
        Robot needs to knock off the gold block
        Turn to depot
        Drive to depot and place marker
        Park in crater
     */

    public void stop() {
        gold.disable();
    } // disable CV on stop to save battery


}
