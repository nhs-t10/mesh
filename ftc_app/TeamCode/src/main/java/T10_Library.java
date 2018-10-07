import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.ColorSensor;


public abstract class T10_Library extends OpMode{
    /**
     *  Library for the upcoming 2018-2019 FTC Competition
     *  Usage: contains methods and initializations of hardware components for both
     *  autonomous and teleop usage.
     */

    public DcMotor frontRight, frontLeft, backRight, backLeft;
    // public DcMotor hanger1, hanger2;
    public void init() {
        frontLeft = hardwareMap.dcMotor.get("m0");
        frontRight = hardwareMap.dcMotor.get("m1");
        backLeft = hardwareMap.dcMotor.get("m2");
        backRight = hardwareMap.dcMotor.get("m3");

    }

    public void drive(float lf, float rf, float lb, float rb) {
        frontLeft.setPower(lf);
        frontRight.setPower(rf);
        backLeft.setPower(lb);
        backRight.setPower(rb);

        //power settings for motors. Takes input from omni
    }

}
