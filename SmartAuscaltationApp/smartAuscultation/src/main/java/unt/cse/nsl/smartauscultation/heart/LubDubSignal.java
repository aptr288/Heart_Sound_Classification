package unt.cse.nsl.smartauscultation.heart;

import java.util.ArrayList;

/**
 * This is a model class used to store 1 lubdub signal data.
 * This class is added to handle the multiple lubdub signals
 * for split detection and murmur-classification
 * Created by Anurag Chitnis on 12/14/2016.
 */

public class LubDubSignal {

    private double firstS1Timestamp;
    private double firstS2Timestamp;
    private double secondS1Timestamp;
    private int s2index;
    private boolean isSplitDetected;
    private String murmurResult;
    private ArrayList<Short> lubDubSound;

    private float s1SplitTime;
    private float s2SplitTime;

    public int getS2index() {
        return s2index;
    }

    public void setS2index(int s2index) {
        this.s2index = s2index;
    }

    public boolean isSplitDetected() {
        return isSplitDetected;
    }

    public void setSplitDetected(boolean splitDetected) {
        isSplitDetected = splitDetected;
    }

    public String getMurmurResult() {
        return murmurResult;
    }

    public void setMurmurResult(String murmurResult) {
        this.murmurResult = murmurResult;
    }

    public double getFirstS1Timestamp() {
        return firstS1Timestamp;
    }

    public void setFirstS1Timestamp(double firstS1Timestamp) {
        this.firstS1Timestamp = firstS1Timestamp -80;
    }

    public double getFirstS2Timestamp() {
        return firstS2Timestamp;
    }

    public void setFirstS2Timestamp(double firstS2Timestamp) {
        this.firstS2Timestamp = firstS2Timestamp - 150;
    }

    public double getSecondS1Timestamp() {
        return secondS1Timestamp;
    }

    public void setSecondS1Timestamp(double secondS1Timestamp) {
        this.secondS1Timestamp = secondS1Timestamp - 80;
    }

    public ArrayList<Short> getLubDubSound() {
        return lubDubSound;
    }

    public double[] getLubDubSoundDoubleValueArray() {
        double[] lubDubDoubleValue = new double[lubDubSound.size()];
        for (int i=0; i< lubDubSound.size(); i++) {
            lubDubDoubleValue[i] = lubDubSound.get(i);
        }
        return lubDubDoubleValue;
    }

    public void setLubDubSound(ArrayList<Short> lubDubSound) {
        this.lubDubSound = lubDubSound;
    }
}
