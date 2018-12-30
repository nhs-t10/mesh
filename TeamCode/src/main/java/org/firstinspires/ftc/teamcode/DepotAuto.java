package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.ViewDisplay;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;

@Autonomous(name= "Depot")
public class DepotAuto extends T10_Library {
    /*
        T-10 Preliminary Autonomous

        This is based on the assumption that we are:
            - On the red team
            - Starting such that we are facing the depot first
     */

    // constants and state declaration

    // should be the exact same as red depot code
    double angleTurned = 0;
    imuData imu;
    boolean moving = false;
    boolean turnRight = true;
    Turning turner;
    enum state {
        START, TURNING, SAMPLING, MARKING, WALL, TURNPARK, STOP, CRATER;
    }
    state currentState = null;
    ElapsedTime clock = new ElapsedTime();

    public void init() {
        initialize_robot();
        imu = new imuData(hardwareMap);
        turner = new Turning();
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
            stopDrive();
        }
        if(currentState == state.MARKING){
            mark();
        }
        if(currentState == state.WALL){
            wall();
        }

        if(currentState == state.CRATER){
            crater();
        }
        telemetry.addData("Current State: ", currentState);
        telemetry.addData("Millis since run: ", clock.milliseconds());
        telemetry.addData("Current Angle: ", imu.getAngle());
        telemetry.addData("Gold Position:", gold.position);
    }

    public void start_auto() {
        if (!moving) {
            clock.reset();
            moving = true;
        } else if (clock.seconds() < 3) {
            latchMotor.setPower(1f);
        }
    }

    public void turnToGold(){
        boolean aligned = gold.getAligned(); // get if gold block is aligned
        if(aligned){
            omni(0,0,0);
            gold.takeScreenshot();
            angleTurned = imu.getAngle();
            moving=false;
            currentState = state.SAMPLING;
        }
        else{
            if(turnRight){
                omni(0,.15f,0);
            }
            else{
                omni(0,-.15f,0);
            }
            if(Math.abs(imu.getAngle()) > 220.0 && !gold.isFound()){
                turnRight = false;
            }
        }
        telemetry.addData("xPos", gold.getXPosition());
        telemetry.addData("Current Gold Position: ", gold.position);
    }

    // Knock gold off (for now)
    public void sample() {
        // Logic for bestRect
        Rect best = gold.getBestRect();
        if (best.height < 150 || best.width < 150) {
            if(!moving){
                clock.reset();
                moving=true;
            } else if (clock.seconds()<2.7) {
                omni(-.4f,0,0);
            } else if (clock.seconds() > 2.7 && clock.seconds() < 4.7) {
                if(gold.position == GoldAlignDetector.gold_position.LEFT) {
                    turner.setDestination(45);
                    turner.update(imu);
                }
                else if (gold.position == GoldAlignDetector.gold_position.RIGHT){
                    turner.setDestination(-45);
                    turner.update(imu);
                }
            } else {
                stopDrive();
                moving = false;
                currentState=state.MARKING;
            }
        }
    }

    public void mark() {
        if (!moving) {
            clock.reset();
            moving = true;
        } else if (clock.seconds() < 1) {
            omni(-.5f, 0, 0);
        } else if (clock.seconds() > 1 && clock.seconds() < 2.5) {
            stopDrive();
            //markServo.setPosition(180);
        } else {
            moving = false;
            currentState = state.WALL;
        }
    }

    public void wall(){
        if(!moving){
            clock.reset();
            moving=true;
        } else if (clock.seconds()<1.7){
            omni(0,0,.5f);
        } else {
            omni(0,0,0);
            moving = false;
            currentState=state.CRATER;
        }
    }


    public void crater(){
        if(!moving){
            clock.reset();
            moving=true;
        } else if (clock.seconds()<10 && imu.getPitch()<-83){

            omni(-.66f,0,0f);
        } else {
            omni(0,0,0);
            moving = false;
            currentState=state.STOP;
        }
        telemetry.addData("Pitch: ", imu.getPitch());
    }

    public void stop() {
        gold.disable();
    }


}