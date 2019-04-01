package unt.cse.nsl.smartauscultation.heart;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.ObservationReal;
import be.ac.ulg.montefiore.run.jahmm.ViterbiCalculator;
import be.ac.ulg.montefiore.run.jahmm.io.FileFormatException;
import be.ac.ulg.montefiore.run.jahmm.io.HmmReader;
import be.ac.ulg.montefiore.run.jahmm.io.OpdfGaussianReader;
import unt.cse.nsl.smartauscultation.AuscultationArea;
import unt.cse.nsl.smartauscultation.GenLloyd;
import unt.cse.nsl.smartauscultation.GeneralClassification;
import unt.cse.nsl.smartauscultation.MFCC;

/**
 * Created by Anurag Chitnis on 10/13/2016.
 */

public class HeartSoundClassification extends GeneralClassification {

    private int hmmIndex = 1;

    private static final String TAG = HeartSoundClassification.class.getName();

    private static AuscultationArea auscultationArea;
    private double[] probability;
    private static final int NUMBER_OF_HMM_MODEL = 8;
    private static Hmm<ObservationReal>[] hmm = new Hmm[NUMBER_OF_HMM_MODEL];
    private static String[] hmmName = new String [NUMBER_OF_HMM_MODEL];


    double[] getProbability() {
        return probability;
    }

    String[] getHmmName() {
        return hmmName;
    }

    /**
     * This method finds out the broad classification of the murmur, without considering split and auscultation area.
     * @return String of the broad classification. It returns one of these values "as_ps", "mvp","mr_tr","ar_pr","pda","flow","ms_ts","normal";
     */
    public String getIndexClassification() {
        return hmmName[hmmIndex];
    }

    int getHmmIndex() {
        return hmmIndex;
    }

    public static void setAuscultationArea(AuscultationArea auscultationArea) {
        HeartSoundClassification.auscultationArea = auscultationArea;
    }

    public static void readHMMFull(Context context) {
        hmmName[1] = "as_ps";
        hmmName[2] = "mvp";
        hmmName[3] = "mr_tr";
        hmmName[4] = "ar_pr";
        hmmName[5] = "pda";
        hmmName[6] = "flow";
        hmmName[7] = "ms_ts";
        hmmName[0] = "normal";

        for (int i = 0; i < NUMBER_OF_HMM_MODEL; i++) {
            try {
                Reader hmmReader = new InputStreamReader(context.getAssets().open(hmmName[i]));
//                System.out.println(i);
                hmm[i] = HmmReader.read(hmmReader, new OpdfGaussianReader());
                hmmReader.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (FileFormatException e) {
                e.printStackTrace();
            }
        }
    }


    public String getClassificationResult(boolean hasSplit, double heartSound[]) {
        double[] data = normalize(heartSound);
        String classificationResult;

        double[][] coeffs = null;

        MFCC mfcc = new MFCC(8000, 512, 14, false, 20, 450, 20);
        try {
            coeffs = mfcc.process(heartSound);
        } catch (IOException e) {
            e.printStackTrace();
        }

        GenLloyd gl = new GenLloyd(transpose(coeffs));
        gl.calcClusters(1);
        double[][] sResult = gl.getClusterPoints();


//        try {
//            // log file
//            BufferedWriter bw = new BufferedWriter(
//                    new FileWriter(getLogFile("LogClassification")));
            hmmIndex = classifyFull(sResult[0]);
//
//            bw.append("Estimated murmur: " + hmmName[hmmIndex] + "\n");
//
//            bw.append(recentlyRecordedFilePath + ".jpg\n");
//            bw.flush();
//            bw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        switch (hmmIndex) {
            case 0: classificationResult = "Normal";  break;
            case 1:
                if (auscultationArea.equals(AuscultationArea.AORTIC))
                    classificationResult = "Aortic stenosis";
                else if (hasSplit)
                    classificationResult = "Atrial septal defect";
                else
                    classificationResult = "Pulmonary stenosis";
                break;
            case 2: classificationResult = "Mitral valve prolapse"; break;
            case 3:
                if (auscultationArea.equals(AuscultationArea.MITRAL))
                    classificationResult = "Mitral regurgitation";
                else if (hasSplit)
                    classificationResult = "Ventricular septal defect";
                else
                    classificationResult = "Tricuspid regurgitation";
                break;
            case 4:
                if (auscultationArea.equals(AuscultationArea.PULMONARY))
                    classificationResult = "Pulmonary regurgitation";
                else
                    classificationResult = "Aortic regurgitation";
                break;
            case 5: classificationResult = "Patent Ductus Arteriosus"; break;
            case 6: classificationResult = "Flow murmur"; break;
            default:
                if (auscultationArea.equals(AuscultationArea.MITRAL))
                    classificationResult = "Mitral stenosis";
                else
                    classificationResult = "Tricuspid stenosis";
        }

        return classificationResult;
    }

    private int classifyFull (double[] heart) {
        List<ObservationReal> testSequence = new ArrayList<>();
        probability = new double[NUMBER_OF_HMM_MODEL];

        for (double i:heart) {
            testSequence.add(new ObservationReal(i));
        }

        StringBuilder probabilityString = new StringBuilder();

        //calculate the probability of each systolic HMM
        for (int i = 0; i < NUMBER_OF_HMM_MODEL; i++) {
            ViterbiCalculator vc = new ViterbiCalculator(testSequence, hmm[i]);
            probability[i] = vc.lnProbability();
            probabilityString.append(hmmName[i]);
            probabilityString.append(" = ");
            probabilityString.append(probability[i]+", ");
//            try {
//                bw.append(i + ": " + probability[i] + "\n");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

        Log.d(TAG,probabilityString.toString());

        //determine the maximum probability
        double max = probability[0];
        int index = 0;
        if (auscultationArea.equals(AuscultationArea.AORTIC)) {
            if(probability[1] > max || probability[6] > max) {
                index = getMaximum(1,6);
            }
//            if (probability[1] > max)
//                index = 1;
//            if (probability[6] > max)
//                index = 6;
        }
        else if (auscultationArea.equals(AuscultationArea.PULMONARY)) {
            if(probability[1] > max || probability[4] > max || probability[5] > max) {
                index = getMaximum(1,4,5);
            }
//            if (probability[1] > max)
//                index = 1;
//            if (probability[4] > max)
//                index = 4;
//            if (probability[5] > max)
//                index = 5;
        }
        else if (auscultationArea.equals(AuscultationArea.LLSB)) {
            if(probability[3] > max || probability[4] > max || probability[7] > max) {
                index = getMaximum(3,4,7);
            }
//            if (probability[3] > max)
//                index = 3;
//            if (probability[4] > max)
//                index = 4;
//            if (probability[7] > max)
//                index = 7;
        }
        else {
            if(probability[2] > max || probability[3] > max || probability[7] > max) {
                index = getMaximum(2,3,7);
            }
//            if (probability[2] > max)
//                index = 2;
//            if (probability[3] > max)
//                index = 3;
//            if (probability[7] > max)
//                index = 7;
        }

//            for (int i = 1; i < NUMBER_OF_HMM_MODEL; i++) {
//                if (probability[i] > max) {
//                    index = i;
//                }
//            }

        return  index;
    }

    private int getMaximum(int... index) {
        int max = index[0];
        double maxProb = probability[max];

        for(int item : index) {
            if(probability[item] > maxProb) {
                maxProb = probability[item];
                max = item;
            }
        }
        return max;
    }
}
