package com.disnodeteam.dogecv.detectors.roverrukus;

import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.DogeCVDetector;
import com.disnodeteam.dogecv.filters.DogeCVColorFilter;
import com.disnodeteam.dogecv.filters.HSVColorFilter;
import com.disnodeteam.dogecv.scoring.MaxAreaScorer;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class CraterDetector extends DogeCVDetector {

    public DogeCV.AreaScoringMethod areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA;

    public DogeCVColorFilter black = new HSVColorFilter(new Scalar(35,43,43), new Scalar(0,0,0));
    public MaxAreaScorer big_black = new MaxAreaScorer(10);

    private Mat workingMat  = new Mat();
    private Mat displayMat  = new Mat();
    private Mat blackMask  = new Mat();
    private Mat hiarchy     = new Mat();

    public Mat process(Mat input){
        // Copy input mat to working/display mats
        input.copyTo(displayMat);
        input.copyTo(workingMat);
        input.release();

        // Generate Masks
        black.process(workingMat.clone(), blackMask);
        List<MatOfPoint> contoursBlack= new ArrayList<>();
        Imgproc.blur(blackMask,blackMask,new Size(2,2));


        Imgproc.findContours(blackMask, contoursBlack, hiarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(displayMat,contoursBlack,-1,new Scalar(0,128,0),3);

        Rect chosenBlack  = null;
        double chosenBlackScore = Integer.MAX_VALUE;

        for(MatOfPoint cont : contoursBlack){
            double score = calculateScore(cont); // Get the diffrence score using the scoring API

            // Get bounding rect of contour
            Rect rect = Imgproc.boundingRect(cont);
            Imgproc.rectangle(displayMat, rect.tl(), rect.br(), new Scalar(0,0,255),2); // Draw rect
            if(rect.width < 180 && rect.height < 180 && rect.width > 30 && rect.height > 30) {
                // If the result is better then the previously tracked one, set this rect as the new best
                if (score < chosenBlackScore) {
                    chosenBlackScore = score;
                    chosenBlack = rect;
                }
            }
        }

        if(chosenBlack != null){
            Imgproc.rectangle(displayMat, chosenBlack.tl(), chosenBlack.br(), new Scalar(255,0,0),4);
            Imgproc.putText(displayMat, "Chosen", chosenBlack.tl(),0,1,new Scalar(255,255,255));


        }

        return displayMat;
    }

    public void useDefaults() {
        addScorer(big_black);
    }





}
