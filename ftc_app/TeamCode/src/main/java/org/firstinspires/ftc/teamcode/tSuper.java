package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.Servo;
@SuppressWarnings("all")
public abstract class tSuper extends OpMode
{

    public DcMotor leftF, leftB, rightF, rightB;
    //public Servo


    public void init() {
        leftF = hardwareMap.dcMotor.get("frontLeft");
        rightF = hardwareMap.dcMotor.get("frontRight");
        leftB = hardwareMap.dcMotor.get("backLeft");
        rightB = hardwareMap.dcMotor.get("backRight");

        //rightF.setDirection(DcMotor.Direction.REVERSE);
        //rightB.setDirection(DcMotor.Direction.REVERSE);

        prepare();

        //naming. Karel has ruined me

    }

    public abstract void prepare();

    public void drive(float lf, float rf, float lb, float rb) {
        leftF.setPower(lf);
        rightF.setPower(rf);
        leftB.setPower(lb);
        rightB.setPower(rb);

        //power settings for motors. Takes input from teleOp
    }


    public void side(double side)
    {
        leftF.setPower(-side);
        leftB.setPower(side);
        rightF.setPower(side);
        rightB.setPower(-side);

        //power settings for skating. Also takes input from teleOp
    }
}