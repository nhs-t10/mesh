package org.firstinspires.ftc.teamcode;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Hardware;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class imuData {

    static BNO055IMU imu;
    Orientation angle = new Orientation();

    public imuData (HardwareMap hardwareMap){
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        initImu();
    }

    public void initImu(){

        BNO055IMU.Parameters parameters= new BNO055IMU.Parameters();
        parameters.angleUnit=BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;

        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();


        imu.initialize(parameters);

    }

    public float getAngle(){
        angle =imu.getAngularOrientation();
        return angle.firstAngle;
    }
    public double getXVelocity(){
        double velocity = imu.getVelocity().xVeloc;
        return velocity;
    }
    public double getYVelocity(){
        double velocity = imu.getVelocity().yVeloc;
        return velocity;
    }

    public double getZVelocity(){
        double velocity = imu.getVelocity().zVeloc;
        return velocity;
    }


}
