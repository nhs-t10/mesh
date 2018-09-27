package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Controler_Values")
public class Controler_Values extends OpMode{

    public void init ()
    {}

    public void loop()
    {
        double lefty = gamepad1.left_stick_y;
        double leftx = gamepad1.left_stick_x;
        double righty = gamepad1.right_stick_y;
        double rightx = gamepad1.right_stick_x;
        double rightTrigger = gamepad1.right_trigger;
        double leftTrigger = gamepad1.left_trigger;
        boolean ButtonA = gamepad1.a;
        boolean ButtonB = gamepad1.b;
        boolean ButtonX = gamepad1.x;
        boolean ButtonY = gamepad1.y;
        boolean leftBumper = gamepad1.left_bumper;
        boolean rightBumper = gamepad1.right_bumper;
        boolean dpadUp = gamepad1.dpad_up;
        boolean dpadDown = gamepad1.dpad_down;
        boolean dpadRight = gamepad1.dpad_right;
        boolean dpadLeft = gamepad1.dpad_left;
        boolean Back = gamepad1.back;
        boolean Start = gamepad1.start;
        boolean leftStick = gamepad1.left_stick_button;
        boolean rightStick = gamepad1.right_stick_button;
        // This is a variable list of all of the values we can receive from controller1

        double lefty2 = gamepad2.left_stick_y;
        double leftx2 = gamepad2.left_stick_x;
        double righty2 = gamepad2.right_stick_y;
        double rightx2 = gamepad2.right_stick_x;
        double rightTrigger2 = gamepad2.right_trigger;
        double leftTrigger2 = gamepad2.left_trigger;
        boolean ButtonA2 = gamepad2.a;
        boolean ButtonB2 = gamepad2.b;
        boolean ButtonX2 = gamepad2.x;
        boolean ButtonY2 = gamepad2.y;
        boolean leftBumper2 = gamepad2.left_bumper;
        boolean rightBumper2 = gamepad2.right_bumper;
        boolean dpadUp2 = gamepad2.dpad_up;
        boolean dpadDown2 = gamepad2.dpad_down;
        boolean dpadRight2 = gamepad2.dpad_right;
        boolean dpadLeft2 = gamepad2.dpad_left;
        boolean Back2 = gamepad2.back;
        boolean Start2 = gamepad2.start;
        boolean leftStick2 = gamepad2.left_stick_button;
        boolean rightStick2 = gamepad2.right_stick_button;
        // This is a variable list of all of the values we can receive from controller2

    }

    public void stop()
        {}
}



