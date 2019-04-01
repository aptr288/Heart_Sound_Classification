package unt.cse.nsl.smartauscultation.heart;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import jwt.CWT;
import jwt.PeakDetector;

/**
 * Calculates S1 and S2 split
 * @author Shanti R Thiyagaraja, Anurag Chitnis
 *
 */
public class HeartSplitSound {

    private double[] s1;
    private double[] s2;
    private static double[] val_WAV;
    private static double[] xWAV;
    private float s1SplitTime;
    private float s2SplitTime;
    private CWT cwt;
    private final int scale = 500;
    private int windowSize = 150;
    private double stringency = 1;
    private final float frequency = 8000;


    public float getS2SplitTime() {
        return s2SplitTime;
    }

    public void setS2SplitTime(float s2SplitTime) {
        this.s2SplitTime = s2SplitTime;
    }

    public float getS1SplitTime() { return s1SplitTime; }

    public void setS1SplitTime(float s1SplitTime) {
        this.s1SplitTime = s1SplitTime;
    }

    public static void readS2Files(Context context) {
        try {
            val_WAV = getValuesFromCSV(context.getAssets().open("db2_val_WAV.csv"));
            xWAV = getValuesFromCSV(context.getAssets().open("db2_val_WAV.csv"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static double[] getValuesFromCSV(InputStream inputStream) {
        List<Double> values = new ArrayList<Double>();

        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        try {

            br = new BufferedReader(new InputStreamReader(inputStream));//new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                String[] input = line.split(cvsSplitBy);
                values.add(Double.parseDouble(input[0]));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        double[] returnValues = new double[values.size()];

        for (int i = 0; i < values.size(); i++) {
            returnValues[i] = values.get(i);
        }

        return returnValues;
    }

    public HeartSplitSound(double[] heartSound, int s2Index) {
//        this.val_WAV = val_WAV_;
//        this.xWAV = xWAV_;

        this.s1 = new double[s2Index];
        for (int i = 0; i < s2Index; i++) {
            s1[i] = heartSound[i];
        }
        int s2Size = heartSound.length - s2Index;
        this.s2 = new double[s2Size];
        for (int i = 0; i < s2Size; i++) {
            s2[i] = heartSound[s2Index];
            s2Index++;
        }

        cwt = new CWT(val_WAV, xWAV);

    }

    public void calculateSplit() {

        long StartSplitTime = System.currentTimeMillis();

        // calculate s1 split
        cwt.setSignal(s1);
        s1SplitTime = process();

        // calculate s2 split
        cwt.setSignal(s2);
        s2SplitTime = process();

        long EndSplitTime = System.currentTimeMillis();

        long totalTimeTaken = EndSplitTime - StartSplitTime;
        Log.i("calculation time", "Time taken to calculate both splits: " + Long.toString(totalTimeTaken));
    }

    private float process() {

        cwt.ContinuousWaveletTransform(scale);
        double[][] scalogramData = cwt.scalogramdata();
        double[] scalogramSorted = scalogramSort(scalogramData);

        PeakDetector peakDetector = new PeakDetector(scalogramSorted);
        int[] peakIndex = peakDetector.process(windowSize, stringency);
        double[] peak = new double[peakIndex.length];
        for (int i = 0; i < peak.length; i++) {
            peak[i] = scalogramSorted[peakIndex[i]];
        }
        float split = calculateSplit(peakIndex, peak);

        return split;
    }

    private double[] scalogramSort(final double[][] data) {
        double[] returnValues = new double[data[0].length];

        for (int i = 0; i < data[0].length; i++) {
            returnValues[i] = Double.MIN_VALUE;
            for (int j = 0; j < scale; j++) {
                if (data[j][i] > returnValues[i]) {
                    returnValues[i] = data[j][i];
                }
            }
        }

        return returnValues;
    }

    private float calculateSplit(int[] index, double[] peak) {

        int len = index.length;
        double temp1;
        int temp2;

        for (int i = 0; i < len; i++) {

            for (int j = 1; j < (len-i); j++) {

                if (peak[j-1] < peak[j]) {
                    temp1 = peak[j-1];
                    peak[j-1] = peak[j];
                    peak[j] = temp1;

                    temp2 = index[j-1];
                    index[j-1] = index[j];
                    index[j] = temp2;

                }
            }

        }
		/*
		for(int i=0; i<len; i++) {
			Log.i("split", String.valueOf(index[i]) + "   " + String.valueOf(peak[i]));
		}*/

        float split=0;

        try {

             split = Math.abs(index[0] - index[1]) / frequency * 1000;
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            Log.e("HeartSplitSound",ex.toString());
        }

        //Log.i("split", String.valueOf(split));
        // System.out.println(tm.get(tm.lastKey()));
        // System.out.println(tm.get(tm.lowerKey(tm.lastKey())));
        // System.out.println(split + " ms");

        return split;
    }

}
