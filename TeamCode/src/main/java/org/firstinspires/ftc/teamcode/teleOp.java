package org.firstinspires.ftc.teamcode;
//import com.qualcomm.robotcore.util.Range;
import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldDetector;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.Turning;
//import java.util.Arrays;

@TeleOp(name = "teleOp")
public class teleOp extends T10_Library
{
    Turning test = new Turning(0);
    boolean turn = false;
    GoldDetector gold = null;
    public void init()
    {
        initialize_robot();
        gold.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
    }

    public void loop() {
        float linear = gamepad1.left_stick_y;
        float side = -gamepad1.right_stick_x;
        float rotation = gamepad1.left_stick_x;
        //defining the stuff. linear = straight, rotation = turning, side = skating.
        //Linear - rotation will compensate one side to allow the other side to overrotate

        omni(linear, rotation, side);
        telemetry.addData("Gold?", gold.isFound());
//        telemetry.addData("left_y",linear);
//        telemetry.addData("right_x",linear);
//        telemetry.addData("right_y",linear);
//        telemetry.addData("Servo's moving?", servosMoving);

        //sending inputs to omni code
        if(gamepad1.a && !turn){
            turn = true;
            test.setDestination(30);
        }
        if(turn){
            boolean isTurning = test.update();
            if(!isTurning){
                turn = false;
            }
        }

    }

    public void stop()
    {}

}
