package org.firstinspires.ftc.teamcode;
import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldDetector;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


public class CVTestOp extends T10_Library {
    // Declare constants and instances
    GoldDetector gold = null;
    @Override
    public void init() {
        initialize_robot();
        gold = new GoldDetector();
        gold.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
    }

    @Override
    public void loop() {
        float linear = gamepad1.left_stick_y;
        float side = -gamepad1.right_stick_x;
        float rotation = gamepad1.left_stick_x;
        //defining the stuff. linear = straight, rotation = turning, side = skating.
        //Linear - rotation will compensate one side to allow the other side to overrotate

        omni(linear, rotation, side);
        telemetry.addData("Is Found:", gold.isFound());
    }

    @Override
    public void stop() {
        super.stop();
    }
}
