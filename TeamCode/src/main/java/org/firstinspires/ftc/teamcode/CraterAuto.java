package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.opencv.core.Rect;

@Autonomous(name= "Crater")
public class CraterAuto extends T10_Library {
    /*
        T-10 Preliminary Autonomous

        This is based on the assumption that we are:
            - On the red team
            - Starting such that we are facing the depot first
     */

    // constants and state declaration

    // for the crater facing auto, it's not worth it to mark at the moment, so sampling and driving into crater should do
    // as of right now, blue should do the same thing as red
    double angleTurned = 0;
    imuData imu;
    boolean startedMove = false;
    boolean startedWall = false;
    boolean startedCrater = false;
    boolean hasturned = false;
    boolean hasTurnedToCrater = false;
    boolean moving = false;
    boolean turnRight = true;
    Turning turner;
    enum state {
        START, TURNING, SAMPLING, STOP, TURNCRATER, PARKCRATER;
        // MARKING, WALL, TURNPARK,
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
        if(currentState == state.TURNCRATER){
            turn_crater();
        }
//        if(currentState == state.WALL){
//            wall();
//        }
//        if(currentState == state.TURNPARK){
//            turnpark();
//        }
        if(currentState == state.PARKCRATER){
            park_crater();
        }
        telemetry.addData("Current State: ", currentState);
        telemetry.addData("Millis since run: ", clock.milliseconds());
        telemetry.addData("Current Angle: ", imu.getAngle());

    }

    public void start_auto() {
        if (!moving && latchLimit.isPressed()) {
            clock.reset();
            moving = true;
        } else if (!latchLimit.isPressed()) {
            latchMotor.setPower(-1f);
        }
        else if (moving && clock.seconds() < .5){
            omni(-.25f,0,0);
            latchMotor.setPower(0);
        }
        else{
            latchMotor.setPower(0f);
            currentState = state.TURNING;
        }
    }

    public void turnToGold(){
        boolean aligned = gold.getAligned(); // get if gold block is aligned
        if(aligned){
            omni(0,0,0);
            gold.takeScreenshot();
            angleTurned = imu.getAngle();
            startedMove=false;
            currentState = state.SAMPLING;
        }
        else{
            if(turnRight){
                omni(0,.3f,0);
            }
            else{
                omni(0,-.3f,0);
            }
            if(Math.abs(imu.getAngle()) > 40.0 && !gold.isFound()){
                turnRight = false;
            }
        }
        telemetry.addData("Current Gold Position: ", gold.position);
    }

    // Knock gold off (for now)
    public void sample() {
        // Logic for bestRect
        Rect best = gold.getBestRect();
        if (best.height < 150 || best.width < 150) {
            if(!startedMove){
                clock.reset();
                startedMove=true;
            } else if (clock.seconds()<2.2){
                omni(-.42f,0,0);
            } else {
                stopDrive();
                currentState= state.TURNCRATER; //temporary, while lift is slow
            }
        }

    }

    public void turn_crater(){
        if(!hasturned){
            clock.reset();
            hasturned=true;
        } else if (clock.seconds()<2){
            latchMotor.setPower(.8);
            turner.setDestination(90);
            turner.update(imu);
        } else {
            stopDrive();
            currentState= state.PARKCRATER;
        }
    }

    public void park_crater(){
        if(!startedWall){
            clock.reset();
            startedWall=true;
        } else if (clock.seconds()<2.7){
            scoreMotor.setPower(-1f);
        } else {
            latchMotor.setPower(0);
            scoreMotor.setPower(0);
            omni(0,0,0);
            currentState= state.STOP;
        }
    }

//    public void turnpark(){
//        if(!hasTurnedToCrater){
//            clock.reset();
//            hasTurnedToCrater=true;
//        } else if (clock.seconds()<2){
//            turner.setDestination(135);
//            turner.update(imu);
//        } else {
//            stopDrive();
//            currentState= state.CRATER;
//        }
//    }
//
//    public void crater(){
//        if(!startedCrater){
//            clock.reset();
//            startedCrater=true;
//        } else if (clock.seconds()<10){
//            omni(-.65f,0,0);
//        } else {
//            omni(0,0,0);
//            currentState= state.STOP;
//        }
//    }

    public void stop() {
        gold.disable();
    }


}