package org.firstinspires.ftc.teamcode;

public class Turning{
    double currentAngle;
    double destination;
    double pComponent;
    double dComponent;
    boolean turning=false;
    double preverror = 0.0;
    double starttime = 0.0;
    double prevtime = 0.0;
    final double P = 0.25;
    final double D = 0.25;

    public Turning(double d){
        if(d>180) destination=d-360;
        else destination=d;
        prevtime = getElapsedTimeFromStart();
    }

    public void setDestination(float degrees){
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
        dComponent = Math.abs(D*(error-preverror)/elapsedtime);
        if (turning) {
            if (Math.abs(error) < 10) {
                stopTurning();
                return false;
            }
            T10_Library.omni(0f, (float) pComponent, 0f);
        }
        return true;
    }

    public double getError(){
        double dir1 = destination- currentAngle;
        return dir1;
    }
    
    public double getElapsedTimeFromStart(){
        return System.currentTimeMillis() - starttime;
    }
    
    public double getCurrentTime(){
        return System.currentTimeMillis();
    }
}
