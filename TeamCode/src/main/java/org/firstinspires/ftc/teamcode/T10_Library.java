package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.view.View;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.CraterDetector;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public abstract class T10_Library extends OpMode {

    /**
     *  Library for the upcoming 2018-2019 FTC Competition
     *  Usage: contains methods and initializations of hardware components for both
     *  autonomous and teleop usage.
     */
    public static DcMotor frontRight, frontLeft, backRight, backLeft, armMotorLeft;
    //armMotorRight, intakeMotor;

    public static CRServo extendServo;

    GoldAlignDetector gold = null;
    CraterDetector crater = null;
    public static ColorSensor color;
    // public static Servo leftIntake, rightIntake;

    // Constants
    static float attenuationfactor;
    static double initial_position = 0;
    static double moveRate = .005;
    static boolean servosMoving = false;

    ElapsedTime clock = new ElapsedTime();


    public DRIVING mode;
    public TeamWeAreOn team;

    public enum DRIVING { Slow, Medium, Fast;
        public DRIVING getNext() {
            return values()[(ordinal() + 1) % values().length];
        } // change driving mode
    }

    public enum TeamWeAreOn { RED, BLUE };

    public void setTeam(int blue){
        if(blue > 200){
            this.team = TeamWeAreOn.BLUE;
        }
        else{
            this.team = TeamWeAreOn.RED;
        }
    }

    public void init_cv(){
        gold = new GoldAlignDetector();
        gold.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        gold.useDefaults();
        gold.enable();
    }

    public void init_crater(){
        crater = new CraterDetector();
        crater.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        crater.useDefaults();
        crater.enable();
    }

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
        armMotorLeft = hardwareMap.dcMotor.get("m4");
//        armMotorRight = hardwareMap.dcMotor.get("m5");
//        intakeMotor = hardwareMap.dcMotor.get("m6");
        extendServo = hardwareMap.crservo.get("s0");

        //leftIntake = hardwareMap.servo.get("s0");
        //rightIntake = hardwareMap.servo.get("s1");
        //armServo = hardwareMap.crservo.get("s0");

        init_cv();
        mode = DRIVING.Medium;
        telemetry.addData("Working","All systems go!");;
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
         */
        float[] forwardMultiplier = {1f, 1f, 1f, 1f};
        float[] rotationalMultiplier = {-1f, 1f, -1f, 1f};
        float[] horizontalMultiplier = {-1f, 1f, 1f, -1f};

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

    public void stopDrive(){
        omni(0,0,0);
    }

    public void sleep(int millis) {
        try {
            telemetry.addData("Sleep", "Sleeping for"+millis/1000+"seconds");
            Thread.sleep(millis);
        } catch (Exception err) {
            telemetry.addData("Sleep machine br0ke: ", err);
        }
    }

    public boolean driveFor(double seconds, float l, float r, float s){
        omni(l,r,s);

        clock.reset();
        if(clock.seconds() > seconds) {
            omni(0, 0, 0);
        } else {
            return false;
        }
        return true;
    }

//    public void setArmMotorPower(float power){
//        armMotorLeft.setPower(power);
//        armMotorRight.setPower(-power);
//    }
//
//    public void setExtendServoPower(float power){
//        extendServo.setPower(power);
//    }
//
//    public void setIntakePower(float power){
//        intakeMotor.setPower(power);
//    }
//
//    public void expelliarmus() {
//
//    }




    public void dispense_marker(){
        telemetry.addData("Marker: ", "Deployed");
    }

    public void sayHooray(){
        telemetry.addData("Hooray!", "hip hip");
    }

}
