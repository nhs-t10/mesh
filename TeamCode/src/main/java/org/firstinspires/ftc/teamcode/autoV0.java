package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.HardwareMap;


@Autonomous(name= "auto_v0")
public class autoV0 extends T10_Library {
    boolean stop = false;
    boolean turn = false;
    imuData imu;
    Turning turner = new Turning();
    enum state {
        START, TURNING, DRIVING, STOP;
    }
    state currentState = state.START;

    public void init() {
        initialize_robot();
        imu = new imuData(hardwareMap);
        telemetry.addData("IMU: ",imu.toString());
        setTeam(color.blue());
    }

    public void updateState(){
        int stopper = 0;
        if(!gold.getAligned()){
            currentState = state.TURNING;
            stopper++;
        }
        if(gold.getAligned()){
            currentState = state.DRIVING;
        }
        if(!gold.getAligned() && stopper > 1){
            currentState = state.STOP;
        }
    }

    public void loop() {
        updateState();
        if(currentState == state.TURNING){
            omni(0,.5f,0);
        }
        if(currentState == state.DRIVING){
            omni(1,0,0);
        }
        if(currentState == state.STOP){
            omni(0,0,0);
        }
        telemetry.addData("Gold Aligned?", gold.getAligned());
        telemetry.addData("Current?", imu.getAngle());
        telemetry.addData("Error?", turner.getError());
    }

    public void stop() {
        gold.disable();
    }
}
