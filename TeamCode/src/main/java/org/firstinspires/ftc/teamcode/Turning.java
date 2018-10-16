package org.firstinspires.ftc.teamcode;
import org.firstinspires.ftc.teamcode.T10_Library;
import org.firstinspires.ftc.teamcode.imuData;
public class Turning{
    double current;
    double destination;
    double speed;
    boolean turning=false;
    final double P = 0.25;
    public Turning(double d){
        if(d>180) destination=d-360;
        else destination=d;
    }

    public void setDestination(float degrees){
        destination=degrees;
        turning=true;
    }

    public void stopTurning(){
        turning = false;
        T10_Library.omni(0f, 0f, 0f);
    }

    public boolean update() {
        current = imuData.getAngle();
        speed = getError() * P;
        if (turning) {
            if (Math.abs(getError()) < 10) {
                stopTurning();
                return false;
            }
            T10_Library.omni(0f, (float) speed, 0f);
        }
        return true;
    }

    public double getError(){
        double dir1 = destination-current;
        return dir1;
    }
}
