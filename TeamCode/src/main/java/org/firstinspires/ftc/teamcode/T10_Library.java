package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.ColorSensor;
//import org.firstinspires.ftc.teamcode.imuData;

import java.util.Arrays;
import java.util.Collections;


public abstract class T10_Library extends OpMode {
    /**
     *  Library for the upcoming 2018-2019 FTC Competition
     *  Usage: contains methods and initializations of hardware components for both
     *  autonomous and teleop usage.
     */
    public static DcMotor frontRight, frontLeft, backRight, backLeft;
    // public static ColorSensor color1;
    // public static Servo leftIntake, rightIntake;


    // Constants
    static float attenuationfactor;
    static double initial_position = 0;
    static double moveRate = .005;
    static boolean servosMoving = false;

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

        //leftIntake = hardwareMap.servo.get("s0");
        //rightIntake = hardwareMap.servo.get("s1");
        // csensor1 = hardwareMap.colorSensor.get("c1");

        telemetry.addData("Working","All systems go!");
        // init sensors
        // insert sensors here
        //color1 = hardwareMap.colorSensor.get("color1");
        }


    public static void drive(float lf, float rf, float lb, float rb) {
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

//    public void getColorValues(){
//        telemetry.addData("Red", csensor1.red());
//        telemetry.addData("Blue", csensor1.blue());
//        telemetry.addData("argb", csensor1.argb());
//    }

//    public void moveServos(){
//        servosMoving = true;
//        leftIntake.setPosition(1); //setposition is the same as setpower when declaring regular servos
//        rightIntake.setPosition(0); // 0 means max speed counter-clockwise, 1 means max speed clockwise
//    }
//
//    public void restServos(){
//        servosMoving = false;
//        leftIntake.setPosition(.5);
//        rightIntake.setPosition(.5);
//    }


    public static float maxValue(float array[]){
        float max = 0f;
        for (float i: array){
            if(i>max){ max = i; }
        }
        return max;
    }

    public static void omni(float l, float r, float s) {
        /*
        Omni-driving function.
        @param: l, linear component, r, rotational component, and s, horizontal component
        outputs motor vals
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

        float highest = maxValue(sums);

        if (Math.abs(highest) > 1) { attenuationfactor = highest;
        } else { attenuationfactor = 1f; }

        for (int i = 0; i < 4; i++) {
            sums[i] = sums[i] / attenuationfactor;
        }

        drive(sums[0], sums[1], sums[2], sums[3]);

    }

}
