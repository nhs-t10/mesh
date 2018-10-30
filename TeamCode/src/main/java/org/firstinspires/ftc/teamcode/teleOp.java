package org.firstinspires.ftc.teamcode;
//import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.Turning;
//import java.util.Arrays;

@TeleOp(name = "teleOp")
public class teleOp extends T10_Library
{

    Turning test;
    imuData imu;
    boolean turn = false;
    public void init()
    {
        initialize_robot();
        imu = new imuData(hardwareMap);
        test = new Turning();
    }

    public void loop() {
        /*
        float linear = gamepad1.left_stick_y;
        float side = -gamepad1.right_stick_x;
        float rotation = gamepad1.left_stick_x;
        //defining the stuff. linear = straight, rotation = turning, side = skating.
        //Linear - rotation will compensate one side to allow the other side to overrotate

        omni(linear, rotation, side);

        if(gamepad1.a){ // any button, chose a just because
            leftIntake.setPosition(1); //setposition is the same as setpower when declaring regular servos
            rightIntake.setPosition(0);
        }

        // getHue();

        telemetry.addData("left_y",linear);
        telemetry.addData("right_x",linear);
        telemetry.addData("right_y",linear);
        telemetry.addData("Servo's moving?", servosMoving);
*/
        //sending inputs to omni code
        if(gamepad1.a && !turn){
            turn = true;
            test.setDestination(30);
        }
        if(turn){
            boolean isTurning = test.update(imu);
            if(!isTurning){
                turn = false;
            }
        }

    }

    public void stop()
    {}

}
