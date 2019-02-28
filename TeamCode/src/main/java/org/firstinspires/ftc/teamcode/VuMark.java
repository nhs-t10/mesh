package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaLocalizerImpl;

public class VuMark extends VuforiaLocalizerImpl{

    boolean closed = false;

    public VuMark(Parameters parameters){
        super(parameters);
    }

    @Override

    public void close(){
        if(!closed) super.close();
        closed = true;
    }
}
