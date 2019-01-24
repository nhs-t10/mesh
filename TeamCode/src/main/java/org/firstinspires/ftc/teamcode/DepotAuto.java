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
    }

    public void loop() {
        /*
        Loop constantly checks state, and then executes a command based on this.
         */
        if(currentState == state.START){
            start_auto();
        }
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
        if(currentState == state.CRATER){
            crater();
        }
        telemetry.addData("Current State: ", currentState);
        telemetry.addData("Millis since run: ", clock.seconds());
        telemetry.addData("Current Angle: ", imu.getAngle());
        telemetry.addData("Gold Position:", gold.position);
    }

    public void start_auto() {
        if(!latchLimit.isPressed()){
            latchMotor.setPower(-1f);
        }
        else if (!moving) {
            clock.reset();
            moving = true;
        }
        else if(clock.seconds() < .25){
            omni(-.5f,0,0);
        }
        else {
            scoreMotor.setPower(0f);
            moving = false;
            currentState = state.TURNING;
        }
    }

    public void turnToGold(){
        boolean aligned = gold.getAligned(); // get if gold block is aligned
        if(aligned){
            omni(0,0,0);
            gold.takeScreenshot();
            angleTurned = imu.getAngle();
            moving=false;
            gold.disable();
            currentState = state.SAMPLING;
        }
        else{
            if(turnRight){
                omni(0,.35f,0);
            }
            else{
                omni(0,-.35f,0);
            }
            if(Math.abs(imu.getAngle()) > 45.0 && !gold.isFound()){
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
            } else if (clock.seconds()<1) {
                omni(-1f,0,0);
                //latchMotor.setPower(-1f);
            }
            else if (clock.seconds() > 1 && clock.seconds() < 3){
                turner.setDestination(45);
                turner.update(imu);
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
        } else if (clock.seconds() < 1.2) {
            omni(-.85f,0,0);
        } else if (clock.seconds() > 1.2 && clock.seconds() < 2.8) {
            omni(0,0,-.8f);
            //markServo.setPosition(1);
        } else if (clock.seconds() > 2.8 && !biLiftUp.isPressed()) {
            stopDrive();
            scoreMotor.setPower(1f);
        }
        else{
            moving = false;
            currentState = state.CRATER;
        }
    }

    public void crater(){
        if(!moving){
            clock.reset();
            moving=true;
        } else if (clock.seconds()<2){
            omni(0,0,-.5f);
            scoreMotor.setPower(-1f);
        } else if (clock.seconds() > 2.0 && clock.seconds() < 3.5) {
            scoreMotor.setPower(-1f);
        }
        else{
            scoreMotor.setPower(0f);
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