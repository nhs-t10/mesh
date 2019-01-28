package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "teleOp")
public class teleOp extends T10_Library
{

    boolean bPressed, descending, intake  = false;
    Turning test;
    imuData imu;
    double time_millis = 0.0;
    ElapsedTime t = new ElapsedTime();
    int i = 1;
    int j = 1;
    double servoPosition = 0.5;

    public void init() {
        initialize_robot();
        imu = new imuData(hardwareMap);
        test = new Turning();
    }

    public void loop() {

        telemetry.addData("Imu pitch: ", imu.getPitch());
        telemetry.addData("Imu roll: ", imu.getRoll());
        float linear = gamepad1.left_stick_y;
        float side = gamepad1.left_stick_x;
        float rotation = gamepad1.right_stick_x;

        //defining the stuff. linear = straight, rotation = turning, side = skating.
        //Linear - rotation will compensate one side to allow the other side to overrotate

        if(gamepad1.right_stick_button){
            mode = mode.getNext();
        }

        if(mode == DRIVING.Slow){
            omni(linear/2, rotation/2, side/2);} // slow driving
        if(mode == DRIVING.Medium) {
            omni(linear/1.5f, rotation/1.5f, side/1.5f);} // medium driving
        if(mode == DRIVING.Fast) {
            omni(linear, rotation, side);} // fast driving

        if(gamepad2.left_bumper){
            latchMotor.setPower(1f); //changed this to gamepad 2
        }
        else if (gamepad2.right_bumper  && !latchLimit.isPressed()){ //this is also gamepad2
            latchMotor.setPower(-1f);
        }
        else{
            latchMotor.setPower(0f);
        }

        if(gamepad1.left_trigger > 0 && !biLiftUp.isPressed()){
            scoreMotor.setPower(gamepad1.left_trigger);
        }
        else if (gamepad1.right_trigger > 0){
            scoreMotor.setPower(-gamepad1.right_trigger);
        }
        else{
            scoreMotor.setPower(0f);
        }

        // Intake
        if(gamepad2.a){
            intakeMotor.setPower(.7f);
        }
        else if(gamepad2.y){
            intakeMotor.setPower(-.7f);
        }
        else{ intakeMotor.setPower(0f); }

        // Box Mechanics
        if(gamepad2.left_trigger > 0.1){
            servoPosition -= .025;
            servoPosition = Range.clip(servoPosition,0.25,.85);

            dumpServo.setPosition(servoPosition);
        }
        else if(gamepad2.right_trigger > 0.1){
            servoPosition += .025;
            servoPosition = Range.clip(servoPosition,0.25,.85);

            dumpServo.setPosition(servoPosition);
        }

        if(gamepad1.right_stick_button && gamepad1.left_stick_button){
            shutdown();
            telemetry.addData("SLOW DOWN PARTNER", "RESETING...");
        }

        if(gamepad1.dpad_left){
            markServo.setPosition(0);
        }
        else if (gamepad1.dpad_right){
            markServo.setPosition(1);
        }

        telemetry.addData("Current Angle?", imu.getAngle());
        telemetry.addData("Gold Aligned?", gold.getAligned());
        telemetry.addData("Driving Mode:",mode);
        telemetry.addData("Servo Position: ", servoPosition);
        telemetry.addData("Limit Pressed? ", biLiftUp.isPressed());



        //sending inputs to omni code
//        if(gamepad1.a && !turn){
//            turn = true;
//            test.setDestination(30);
//        }
//        if(turn){
//            boolean isTurning = test.update();
//            if(!isTurning){
//                turn = false;
//            }
//        }

    }

    public void stop() {
        gold.disable();
    }

}