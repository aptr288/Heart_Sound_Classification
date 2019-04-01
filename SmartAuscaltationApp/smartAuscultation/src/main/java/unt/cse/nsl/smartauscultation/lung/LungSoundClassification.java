package unt.cse.nsl.smartauscultation.lung;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.unt.cse.nsl.audio.AudioData;

import java.io.File;
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

import static android.content.ContentValues.TAG;

/**
 * Created by Anurag Chitnis on 10/13/2016.
 */

public class LungSoundClassification extends GeneralClassification {

    private static final int NUMBER_OF_HMM_MODEL = 2;
    private Hmm<ObservationReal>[] hmm = new Hmm[NUMBER_OF_HMM_MODEL];
    private String[] hmmName = new String [NUMBER_OF_HMM_MODEL];
    private static LungSoundClassification lungSoundClassification;
    private AuscultationArea auscultationArea;
    private int hmmIndex = 1;
    private Context context;
    private double[] probability;

    private double[] selectedLungSounds;

    public double[] getProbability() {
        return probability;
    }

    public String[] getHmmName() {
        return hmmName;
    }

    /**
     * This method finds out the broad classification of lung sounds.
     * @return String of the broad classification. It returns one of these values "lung_wheezing","lung_normal";
     */
    public String getIndexClassification() {
        return hmmName[hmmIndex];
    }

    public int getHmmIndex() {
        return hmmIndex;
    }

    private LungSoundClassification(Context context) {
        this.context = context;
    }

    public static LungSoundClassification getInstance(Context cntxt) {
        if(lungSoundClassification == null) {
            lungSoundClassification = new LungSoundClassification(cntxt);
        }

        return lungSoundClassification;
    }


    public void setAuscultationArea(AuscultationArea auscultationArea) {
        this.auscultationArea = auscultationArea;
    }

    public void readHMMFull() {
        hmmName[1] = "lung_wheezing";
//        hmmName[2] = "crackles";
        hmmName[0] = "lung_normal";

        File localFile = Environment.getExternalStorageDirectory();

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


    public void cutAudioSignals(List<AudioData> audioDataList,double startTimeStamp, double endTimeStamp) {

    }

    public String getClassificationResult(double lungSound[]) {
        double[] data = normalize(lungSound);
        String classificationResult;

        double[][] coeffs = null;

        MFCC mfcc = new MFCC(8000, 512, 13, false, 50, 1600, 20);
        try {
            coeffs = mfcc.process(data);
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
            case 0:
                classificationResult = "Normal";
                break;
            case 1:
                classificationResult = "Wheezing";
                break;
//            case 2:
//                classificationResult = "Crackles";
//                break;
            default:
                classificationResult = "Could not classify";
                break;
        }

        return classificationResult;
    }

    private int classifyFull (double[] lungSound) {
        List<ObservationReal> testSequence = new ArrayList<ObservationReal>();
        probability = new double[NUMBER_OF_HMM_MODEL];

        for (double i:lungSound) {
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
        }


        Log.d(TAG,probabilityString.toString());

        //determine the maximum probability
        double maxProbability = Math.max(probability[0],probability[1]);
        // Return the index of the maximum probability
        if(maxProbability == probability[0])
            return 0;
        else
            return 1;
    }


    public void bandPassFiltering() {

    }
}
