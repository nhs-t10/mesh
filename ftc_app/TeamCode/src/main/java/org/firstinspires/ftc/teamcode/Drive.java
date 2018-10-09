package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.Arrays;

//import com.qualcomm.robotcore.hardware.Servo;
@SuppressWarnings("all")
public abstract class Drive extends T10_Library
{

    public DcMotor leftF, leftB, rightF, rightB;
    //public Servo


    public void init() {
        leftF = hardwareMap.dcMotor.get("m0");
        rightF = hardwareMap.dcMotor.get("m1");
        leftB = hardwareMap.dcMotor.get("m2");
        rightB = hardwareMap.dcMotor.get("m3");

        //rightF.setDirection(DcMotor.Direction.REVERSE);
        //rightB.setDirection(DcMotor.Direction.REVERSE);

    }

    public void drive(float lf, float rf, float lb, float rb) {
        leftF.setPower(lf);
        rightF.setPower(rf);
        leftB.setPower(lb);
        rightB.setPower(rb);

        //power settings for motors. Takes input from omni
    }


    /*public void side(double side)
    {
        leftF.setPower(-side);
        leftB.setPower(side);
        rightF.setPower(side);
        rightB.setPower(-side);

        //power settings for skating. Also takes input from omni
    }
    */
    public void omni(float l, float r, float s) {
        float[] forwardMultiplier = {1f, 1f, 1f, 1f};
        float[] rotationalMultiplier = {1f, -1f, -1f, 1f};
        float[] eastwestMultiplier = {-1f, 1f, -1f, 1f};

        float[] forwardComponent = new float[4];
        float[] rotationalComponent = new float[4];
        float[] eastwestComponent = new float[4];

        for (int i = 0; i < 4; i++) {
            forwardComponent[i] = forwardMultiplier[i] * l;
            rotationalComponent[i] = rotationalMultiplier[i] * r;
            eastwestComponent[i] = eastwestMultiplier[i] * s;

        }

        float lfsum = forwardComponent[0] + rotationalComponent[0] + eastwestComponent[0];
        float rfsum = forwardComponent[1] + rotationalComponent[1] + eastwestComponent[1];
        float lbsum = forwardComponent[2] + rotationalComponent[2] + eastwestComponent[2];
        float rbsum = forwardComponent[3] + rotationalComponent[3] + eastwestComponent[3];

        float[] sums = {lfsum, rfsum, lbsum, rbsum};

        Arrays.sort(sums);
        float highest = sums[sums.length - 1];
        float attenuationfactor;

        if (Math.abs(highest) > 1) {
            attenuationfactor = highest;
        } else {
            attenuationfactor = 1f;
        }

        for (int i = 0; i < 4; i++) {
            sums[i] = sums[i] / attenuationfactor;
        }

        drive(sums[0], sums[1], sums[2], sums[3]);

        telemetry.addData("Motor Values: ", Float.toString(sums[0]), Float.toString(sums[1]),
                                                    Float.toString(sums[2]), Float.toString(sums[3]));
        //This is the drive control. It creates arrays with a slot for each motor
    }

}