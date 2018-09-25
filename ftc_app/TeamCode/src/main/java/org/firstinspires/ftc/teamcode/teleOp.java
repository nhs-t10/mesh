package org.firstinspires.ftc.teamcode;
//import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
@TeleOp(name = "teleOp")
public class teleOp extends tSuper
{
    public void prepare()
    {}

    public void loop()
    {
        double linear = gamepad1.left_stick_y;
        double rotation = gamepad1.left_stick_x;
        double side = gamepad1.right_stick_x;
        //defining the stuff

        if (side == 0)
        {
            drive(.7*(linear - rotation), .7*(linear + rotation));
        } else {
            side(.7*side);
        }
    }
        //linear = straight, rotation = turning, side = skating. Linear - rotation will compensate one side to allow the other side to overrotate
    public void stop()
    {}

}
