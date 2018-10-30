package org.firstinspires.ftc.teamcode;

public class Turning{
    double currentAngle;
    double destination;
    double pComponent;
    double dComponent;
    boolean turning=false;
    double preverror = 0.0;;
    double prevtime = 0.0;
    final double P = 0.25;
    final double D = 0.25;

    public Turning(){

    }

    public void setDestination(float degrees){
        if(degrees>180) destination=degrees-360;
        else destination=degrees;
        prevtime = getElapsedTimeFromStart();
        destination=degrees;
        turning=true;
    }

    public void stopTurning(){
        turning = false;
        T10_Library.omni(0f, 0f, 0f);
    }

    public boolean update(imuData sean) {
        currentAngle = sean.getAngle();
        double error = getError();
        pComponent = error * P;
        double currTime = getElapsedTimeFromStart();
        dComponent = -Math.signum(error)*Math.abs(D*(error-preverror)/(currTime-prevtime));
        prevtime = currTime;
        if (turning) {
            if (Math.abs(error) < 5) {
                stopTurning();
                return false;
            }
            T10_Library.omni(0f, (float) (pComponent + dComponent), 0f);

        }
        return true;

    }

    public double getError(){
        double dir1 = destination- currentAngle;
        return dir1;
    }
    
    public double getElapsedTimeFromStart() {
        return System.currentTimeMillis();
    }
}
