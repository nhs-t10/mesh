package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name= "CV OpMode")
public class CV_TestOp extends T10_Library {

    public void init() {
        init_cv();
    }

    public void loop() {
        telemetry.addData("Gold Aligned? ", gold.getAligned());
        telemetry.addData("Gold Width?", gold.getBestRect().width);
        telemetry.addData("Gold Height?", gold.getBestRect().height);
    }

    public void stop(){
        gold.disable();
    }
}
