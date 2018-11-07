package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.HardwareMap;


@Autonomous(name= "auto_v0")
public class autoV0 extends T10_Library {
    boolean stop = false;
    boolean turn = false;
    imuData imu;
    Turning turner = new Turning(0);

    public void changeP() {
        boolean confirmed = false;
        while (!confirmed) {
            if(gamepad2.dpad_up){
                Turning.P += 0.01;
            }
            else if(gamepad2.dpad_down){
                Turning.P -= 0.01;
            }
            telemetry.addData("Current P", Turning.P);
            if (gamepad2.left_stick_button && gamepad2.right_stick_button) {
                confirmed = true;
                telemetry.addData("Confirmed!", "");
            }
            telemetry.update();
        }
    }

    public void init() {
        initialize_robot();
        imu = new imuData(hardwareMap);
        // changeP();
        telemetry.addData("IMU: ",imu.toString());
    }

    public void loop() {
        if(gamepad1.b){
            stop = true;
        }
//        if(!stop) {
//            if (!gold.getAligned()) {
//                omni(0, .1f, 0);
//            } else {
//                omni(.1f, 0, 0);
//            }
//        }
//        else {
//            stopDrive();
//        }

        if(gamepad1.a && !turn){
            turn = true;
            turner.setDestination(30);
        }
        if(turn){
            boolean isTurning = turner.update(imu);
            if(!isTurning){
                turn = false;
            }
        }

        telemetry.addData("Gold Aligned?", gold.getAligned());
        telemetry.addData("Current?", imu.getAngle());
        telemetry.addData("Error?", turner.getError());
        telemetry.addData("P?", Turning.P);
    }

    public void stop() {
        gold.disable();
    }
}
