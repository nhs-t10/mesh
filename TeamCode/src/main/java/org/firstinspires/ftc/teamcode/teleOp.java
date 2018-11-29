package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "teleOp")
public class teleOp extends T10_Library
{

    Turning test;
    imuData imu;
    public void init()
    {
        initialize_robot();
        imu = new imuData(hardwareMap);
        test = new Turning();
    }

    public void loop() {

        float linear = gamepad1.left_stick_y;
        float side = gamepad1.left_stick_x;
        float rotation = gamepad1.right_stick_x;
        //defining the stuff. linear = straight, rotation = turning, side = skating.
        //Linear - rotation will compensate one side to allow the other side to overrotate

        if(gamepad1.right_stick_button){
            mode = mode.getNext();
        }

        if(mode == DRIVING.Slow){omni(linear/2, rotation/2, side/2);} // slow driving
        if(mode == DRIVING.Medium) {omni(linear/1.5f, rotation/1.5f, side/1.5f);} // medium driving
        if(mode == DRIVING.Fast) {omni(linear, rotation, side);} // fast driving

        // gamepad2 commands
        float armMotorPower = gamepad2.right_stick_y;
        float extendPower = gamepad2.left_stick_y;

        setArmMotorPower(armMotorPower);
        setExtendServoPower(extendPower);

        if(gamepad2.left_bumper){
            setIntakePower(-.5f);
        }
        else if (gamepad2.right_bumper){
            setIntakePower(.5f);
        }
        else{
            setIntakePower(0f);
        }

        if(gamepad2.x){
            clock.reset();
            while(clock.seconds() < 2) {
                setIntakePower(.5f);
            }
            setIntakePower(0f);
        }

        //sending inputs to omni code

//        if(gamepad1.b){
//            test.setDestination(90);
//        }
//        if(gamepad1.x){
//            test.setDestination(-90);
//        }
//        if(gamepad1.y){
//            test.setDestination(0);
//        }
//        test.update(imu);
        telemetry.addData("Error: ", test.getError());
        telemetry.addData("P: ", test.pComponent);


        telemetry.addData("Gold Aligned?", gold.getAligned());
        telemetry.addData("Driving Mode:",mode);

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
