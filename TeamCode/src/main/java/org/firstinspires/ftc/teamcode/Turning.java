package org.firstinspires.ftc.teamcode;

public class Turning{
    double currentAngle;
    double destination;
    double pComponent;
    double dComponent;
    double iComponent;
    boolean turning=false;
    double prevError = 0.0;
    double sumError = 0.0;
    double prevTime = 0.0;
    final double P = 0.03;
    final double D = 0;//big D for big boys
    final double I = 0.00000;
    double savedTime;



    public Turning(){
        destination=0;
    }

    public void setDestination(float degrees){
        savedTime=getCurrTime();
        if(degrees>180) destination=degrees-360;
        else destination=degrees;
        prevTime = getCurrTime();
        destination=degrees;
        turning=true;
    }

    public void stopTurning(){
        turning = false;
        sumError=0.0;
        T10_Library.omni(0f, 0f, 0f);
    }

    public void update(imuData sean) {
        currentAngle = sean.getAngle();
        double error = getError();
        pComponent = error * P;
        double currTime = getCurrTime();
        dComponent = -Math.abs(D*(error-prevError)/(currTime- prevTime));

        sumError += error*(currTime-prevTime);
        iComponent = I * sumError;
        if (turning) {
            if (getCurrTime()-savedTime>2000) {
                stopTurning();
            }else {
                T10_Library.omni(0f, 0f, (float) (pComponent + dComponent + iComponent));
            }

        }
        prevTime = currTime;
    }

    public double getError(){
        return currentAngle-destination ;
    }
    
    public double getCurrTime() {
        return System.currentTimeMillis();
    }
}
