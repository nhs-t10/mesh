//package org.firstinspires.ftc.teamcode;
//import org.firstinspires.ftc.teamcode.T10_Library;
//import org.firstinspires.ftc.teamcode.imuData;
//
//public class Turning{
//    double current;
//    double destination;
//    double speed;
//    boolean turning=false;
//    final double P = 0.25;
//    imuData imu = new imuData();
//    public Turning(double d){
//
//        if(d>180) this.destination=d-360;
//        else this.destination=d;
//    }
//
//    public void setDestination(float degrees){
//        destination=degrees;
//        turning=true;
//    }
//
//    public void stopTurning(){
//        turning = false;
//        T10_Library.omni(0f, 0f, 0f);
//    }
//
//    public boolean update() {
//        current = imu.getAngle();
//        speed = getError() * P;
//        if (turning) {
//            if (Math.abs(getError()) < 10) {
//                stopTurning();
//                return false;
//            }
//            T10_Library.omni(0f, (float) speed, 0f);
//        }
//        return true;
//    }
//
//    public double getError(){
//        return destination-current;
//    }
//}
