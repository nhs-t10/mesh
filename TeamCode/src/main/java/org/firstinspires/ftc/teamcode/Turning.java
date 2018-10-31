package org.firstinspires.ftc.teamcode;

public class Turning{
    double currentAngle;
    double destination;
    double pComponent;
    double dComponent;
    boolean turning=false;
    double preverror = 0.0;;
    double prevtime = 0.0;
    final double P = 0.03;
    final double D = 0.3;//big D for big boys

    public Turning(){
        destination=0;
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
            if (Math.abs(error) < 3) {
                stopTurning();
                return false;
            }
            T10_Library.omni(0f, 0f, (float) (pComponent));

        }
        return true;


    }

    public double getError(){
        return currentAngle- destination ;
    }
    
    public double getElapsedTimeFromStart() {
        return System.currentTimeMillis();
    }
}
