package com.disnodeteam.dogecv.scoring;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

public class CubeScorer extends DogeCVScorer {



    public CubeScorer(){

    }

    @Override
    public double calculateScore(Mat input) {
        if(!(input instanceof MatOfPoint)) return Double.MAX_VALUE;
        MatOfPoint contour = (MatOfPoint) input;
        double score = Double.MAX_VALUE;

        // Get bounding rect of contour
        Rect rect = Imgproc.boundingRect(contour);
        double w = rect.width;
        double h = rect.height;

        return Math.abs(1.0 - (w/h));
    }
}
