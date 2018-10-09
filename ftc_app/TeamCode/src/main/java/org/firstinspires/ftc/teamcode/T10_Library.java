package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.ColorSensor;

import java.util.Arrays;
import java.util.Collections;


public abstract class T10_Library extends OpMode {
    /**
     *  Library for the upcoming 2018-2019 FTC Competition
     *  Usage: contains methods and initializations of hardware components for both
     *  autonomous and teleop usage.
     */

    public DcMotor frontRight, frontLeft, backRight, backLeft;
    // public ColorSensor color1;

    // Constants
    float attenuationfactor;


    // public DcMotor hanger1, hanger2;
    public void initialize_robot() {
        /*
        Initializes the robot for both Tele-Op and Autonomous purposes.
        @param: none
        @return: void
         */
        // init motors
        frontLeft = hardwareMap.dcMotor.get("m0");
        frontRight = hardwareMap.dcMotor.get("m1");
        backLeft = hardwareMap.dcMotor.get("m2");
        backRight = hardwareMap.dcMotor.get("m3");

        telemetry.addData("Working","All systems go!");
        // init sensors
        // insert sensors here
        //color1 = hardwareMap.colorSensor.get("color1");
        }


    public void drive(float lf, float rf, float lb, float rb) {
        /*
        Provides basic motor power to all 4 motors.
        @param: float lf, rf, lb & rb all -1<x<1
        @return: void
         */
        frontLeft.setPower(-lf);
        frontRight.setPower(rf);
        backLeft.setPower(-lb);
        backRight.setPower(rb);

        //power settings for motors.
    }

//    public int getHue(){
//        /*
//        Method to get color hue from color sensor.
//        @param: none
//        @return: Color RGB values
//         */
//        telemetry.addData("RGB:", color1.argb());
//        return color1.argb();
//    }

    public float maxValue(float array[]){
        float max = 0f;
        for (float i: array){
            if(i>max){ max = i; }
        }
        return max;
    }

    public void omni(float l, float r, float s) {
        /*
        Omni-driving function.
        @param: l, linear component, r, rotational component, and s, horizontal component
         */
        float[] forwardMultiplier = {1f, 1f, 1f, 1f};
        float[] rotationalMultiplier = {1f, -1f, -1f, 1f};
        float[] horizontalMultiplier = {-1f, 1f, -1f, 1f};

        float[] forwardComponent = new float[4];
        float[] rotationalComponent = new float[4];
        float[] eastwestComponent = new float[4];

        for (int i = 0; i < 4; i++) {
            forwardComponent[i] = forwardMultiplier[i] * l;
            rotationalComponent[i] = rotationalMultiplier[i] * r;
            eastwestComponent[i] = horizontalMultiplier[i] * s;
        }

        float[] sums = new float[4];
        for(int i=0;i<4;i++){
            sums[i]+=forwardComponent[i]+rotationalComponent[i]+eastwestComponent[i];
        }

//        float lfsum = forwardComponent[0] + rotationalComponent[0] + eastwestComponent[0];
//        float rfsum = forwardComponent[1] + rotationalComponent[1] + eastwestComponent[1];
//        float lbsum = forwardComponent[2] + rotationalComponent[2] + eastwestComponent[2];
//        float rbsum = forwardComponent[3] + rotationalComponent[3] + eastwestComponent[3];
//
//        float[] sums = {lfsum, rfsum, lbsum, rbsum};

        // Arrays.sort(sums);

        float highest = maxValue(sums);

        telemetry.addData("max",highest);

        if (Math.abs(highest) > 1) { attenuationfactor = highest;
        } else { attenuationfactor = 1f; }

        for (int i = 0; i < 4; i++) {
            sums[i] = sums[i] / attenuationfactor;
        }

        drive(sums[0], sums[1], sums[2], sums[3]);

        telemetry.addData("Front Left: ", Float.toString(sums[0]));
        telemetry.addData("Front Right: ", Float.toString(sums[0]));
        telemetry.addData("Back Left: ", Float.toString(sums[0]));
        telemetry.addData("Back Right: ", Float.toString(sums[0]));
        //This is the drive control. It creates arrays with a slot for each motor
    }

}
