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
        boolean aligned = gold.getAligned();
        if(!aligned){
            omni(0,.14f,0);
        }
        else{
            currentState = state.SAMPLING;
        }
    }

    // Knock gold off (for now)
    public void sample(){
        // Logic for bestRect
        Rect best = gold.getBestRect();
        if(best.height < 120 || best.width < 120){
            omni(.2f,0,0);
        }
        else {
            currentState = state.STOP;
        }
    }

    public void stop() {
        gold.disable();
    }


}
