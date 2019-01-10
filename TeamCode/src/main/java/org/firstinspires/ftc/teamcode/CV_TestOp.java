package org.firstinspires.ftc.teamcode;
import com.disnodeteam.dogecv.scoring.MaxAreaScorer;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name= "CV OpMode")
public class CV_TestOp extends T10_Library {

    public void init() {

        init_cv();
    }

    double threshold = -1;
    double incriment = .001;

    public void loop() {

        if(gamepad1.dpad_left){
            incriment/=10;
            sleep(500);
        }
        if(gamepad1.dpad_right){
            incriment*=10;
            sleep(500);
        }

        if(gamepad1.dpad_up){
            threshold += 5*incriment;
            threshold = Math.round(threshold * 100.0)/100.0;
            gold.yellowFilter.updateThreshold(threshold);
        }
        else if (gamepad1.dpad_down){
            threshold -= 5*incriment;
            threshold = Math.round(threshold * 100.0)/100.0;
            gold.yellowFilter.updateThreshold(threshold);
        }

        if(gamepad1.a){
            gold.yellowFilter.updateThreshold(-1);
        }
        else if(gamepad1.y){
            gold.yellowFilter.updateThreshold(threshold);
        }

        telemetry.addData("Current Threshold: ", threshold);
        telemetry.addData("Current Increment: ", incriment);
        telemetry.addData("Gold Aligned? ", gold.getAligned());
        telemetry.addData("Gold Width?", gold.getBestRect().width);
        telemetry.addData("Gold Height?", gold.getBestRect().height);
        telemetry.addData("Gold Area?", gold.getBestRect().height*gold.getBestRect().width);
        telemetry.addData("Gold Position?", gold.position);
    }

    public void stop(){
        gold.disable();
    }

}
