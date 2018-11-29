package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.opencv.core.Rect;

@Autonomous(name= "auto_v0")
public class autoCraterRed extends T10_Library {
    /*
        T-10 Preliminary Autonomous

        This is based on the assumption that we are:
            - On the red team
            - Starting such that we are facing the depot first
     */

    // constants and state declaration
    double angleTurned = 0;
    imuData imu;
    boolean startedMove = false;
    boolean startedWall = false;
    boolean startedCrater = false;
    boolean hasturned = false;
    boolean hasTurnedToCrater = false;
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
        if(currentState == state.TURNPARK){
            turnpark();
        }
        if(currentState == state.CRATER){
            crater();
        }
        telemetry.addData("Current State: ", currentState);
        telemetry.addData("Millis since run: ", clock.milliseconds());
        telemetry.addData("Current Angle: ", imu.getAngle());
    }

    public void start_auto(){
        currentState = state.TURNING;
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
                omni(0,.2f,0);
            }
            else{
                omni(0,-.2f,0);
            }
            if(Math.abs(imu.getAngle()) > 40.0 && !gold.isFound()){
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
        if (best.height < 120 || best.width < 120) {
            if(!startedMove){
                clock.reset();
                startedMove=true;
            } else if (clock.seconds()<1.3){
                omni(-.25f,0,0);
            } else {
                stopDrive();
                currentState= state.MARKING;
            }

        }

    }

    public void mark(){
        if(!hasturned){
            clock.reset();
            hasturned=true;
        } else if (clock.seconds()<2){
            turner.setDestination(-45);
            turner.update(imu);
        } else {
            stopDrive();
            currentState= state.WALL;
        }
    }

    public void wall(){
        if(!startedWall){
            clock.reset();
            startedWall=true;
        } else if (clock.seconds()<5){
            omni(-.6f,0,0);
        } else {
            omni(0,0,0);
            currentState= state.STOP;
        }
    }

    public void turnpark(){
        if(!hasTurnedToCrater){
            clock.reset();
            hasTurnedToCrater=true;
        } else if (clock.seconds()<2){
            turner.setDestination(135);
            turner.update(imu);
        } else {
            stopDrive();
            currentState= state.CRATER;
        }
    }

    public void crater(){
        if(!startedCrater){
            clock.reset();
            startedCrater=true;
        } else if (clock.seconds()<10){
            omni(-.65f,0,0);
        } else {
            omni(0,0,0);
            currentState= state.STOP;
        }
    }

    public void stop() {
        gold.disable();
    }


}