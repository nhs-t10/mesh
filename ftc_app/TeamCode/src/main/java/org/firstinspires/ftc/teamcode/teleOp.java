package org.firstinspires.ftc.teamcode;
//import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;
//import java.util.Arrays;

@TeleOp(name = "teleOp")
public class teleOp extends T10_Library
{
    public void init()
    {
        initialize_robot();
    }

    public void loop() {
        float linear = gamepad1.left_stick_y;
        float side = gamepad1.right_stick_x;
        float rotation = gamepad1.left_stick_x;
        //defining the stuff. linear = straight, rotation = turning, side = skating.
        //Linear - rotation will compensate one side to allow the other side to overrotate

        omni(linear, rotation, -side);

        telemetry.addData("left_y",linear);
        telemetry.addData("right_x",linear);
        telemetry.addData("right_y",linear);

        //sending inputs to omni code
    }

        /*float[] forwardMultiplier = {1f, 1f, 1f, 1f};
        float[] rotationalMultiplier = {1f, -1f, -1f, 1f};
        float[] eastwestMultiplier = {-1f, 1f, -1f, 1f};

        float[] forwardComponent = new float[4];
        float[] rotationalComponent = new float[4];
        float[] eastwestComponent = new float[4];

        for (int i = 0; i<4; i++)
        {
            forwardComponent[i] = forwardMultiplier[i] * linear;
            rotationalComponent[i] = rotationalMultiplier[i] * rotation;
            eastwestComponent[i] = eastwestMultiplier[i] * side;


        }
        float lfsum = forwardComponent[0] + rotationalComponent[0] + eastwestComponent[0];
        float rfsum = forwardComponent[1] + rotationalComponent[1] + eastwestComponent[1];
        float lbsum = forwardComponent[2] + rotationalComponent[2] + eastwestComponent[2];
        float rbsum = forwardComponent[3] + rotationalComponent[3] + eastwestComponent[3];

        float[] sums = {lfsum, rfsum, lbsum, rbsum};

        Arrays.sort(sums);
        float highest = sums[sums.length - 1];
        float attenuationfactor;

        if(Math.abs(highest) >1 )
        {
            attenuationfactor = highest;
        } else
        {
            attenuationfactor = 1f;
        }

        for (int i = 0; i<4; i++)
        {
            sums[i] = sums[i] / attenuationfactor;
        }

        drive(sums[0], sums[1], sums[2], sums[3]);
        */



        /*if (side == 0)
        {
            drive(.7*(linear - rotation), .7*(linear + rotation));
        } else {
            side(.7*side);
        }
    }*/

    public void stop()
    {}

}