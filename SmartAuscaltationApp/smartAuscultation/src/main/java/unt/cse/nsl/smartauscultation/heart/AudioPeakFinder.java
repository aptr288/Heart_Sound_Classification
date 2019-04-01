package unt.cse.nsl.smartauscultation.heart;

import android.util.Log;

import org.unt.cse.nsl.audio.AudioData;
import org.unt.cse.nsl.audio.AudioInput;
import org.unt.cse.nsl.audio.AudioPeak;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Anurag Chitnis on 11/23/2016.
 */

public class AudioPeakFinder implements AudioInput {

    private PotentialMaxima S1 = new PotentialMaxima(0.0, 0);

    private LinkedList<NewAudioPeakListener> listeners = new LinkedList<>();
    private AudioPeak potentialPeak = null;

    public void registerListener(NewAudioPeakListener listener) {
        listeners.remove(listener);
        listeners.add(listener);
    }

    public void removeListener(NewAudioPeakListener listener) {
        listeners.remove(listener);
    }

    private void performCallbacks(NewPeak peak) {
        for (NewAudioPeakListener listener : listeners) {
            listener.onPeak(peak);
        }
    }

    private ArrayList<Double> lowPassFilter(ArrayList<Double> signalList) {

        // coefficients for bandpass filter 50Hz - 450Hz
        double a[] = {1,-8.92174918006447,35.9066467138819,-85.8547234855195,135.073075636373,-146.114103360802,110.065403393961,-57.0120252332688,19.4346111722794,-3.93706390158405,0.359928245063557};
        double b[] = {5.97957997553802e-05,0,-0.000298978998776901,0,0.000597957997553802,0,-0.000597957997553802,0,0.000298978998776901,0,-5.97957997553802e-05};

        ArrayList<Double> filteredAudio = new ArrayList<>();
        double filtered_audio[] = new double[signalList.size()];
        //Double heartSound[] = (Double[]) signalList.toArray();
        //audioData = new float[dataPoints];
        int M = b.length-1, N = a.length-1;
        int m, n;
        double yi, yj, mean = 0;

        //filter the audio data using a rational transfer function defined
        //by the numerator and denominator coefficients b and a, respectively
        for (int i = 0; i < signalList.size(); i++) {
            m = Math.min(M, i);
            yi = 0;
            for (int k = 0; k <= m; k++) {
                yi += b[k] * signalList.get(i-k);
            }
            n = Math.min(N, i);
            yj = 0;
            for (int k = 1; k <= n; k++) {
                yj += a[k] * filteredAudio.get(i-k);//filtered_audio[i-k];
            }
            //filtered_audio[i] = yi - yj;
            filteredAudio.add(i,yi-yj);

            //calculate sum
            //mean += filtered_audio[i];
        }
        Log.i(TAG, "audio data is filtered using lowpass Butterworth filter");
        return filteredAudio;

    }

    private void processAudio(List<Double> timeStampList, List<Double> valuesList) {
        /**
         * Downsample
         */
        int size = valuesList.size();
        ArrayList<Double> d_Chunks = new ArrayList<>();
        ArrayList<Double> d_Chunks_ts = new ArrayList<>();
        for (int i = 0; i < size; i += 16) {
            d_Chunks.add(valuesList.get(i));
            d_Chunks_ts.add(timeStampList.get(i));
        }

        /**
         * Low pass filtering
         */
        //d_Chunks = lowPassFilter(d_Chunks);
        /**
         * Normalize
         */
        double sum = 0;
        int n = d_Chunks.size();
        for (double d : d_Chunks)
            sum += d;
        double mean = sum / (double) n;
        sum = 0;
        ArrayList<Double> nor = new ArrayList<>(n);
        for (int i = 0; i < n; ++i)
            nor.add(i, d_Chunks.get(i) - mean);
        for (double a : d_Chunks)
            sum += (a - mean) * (a - mean);
        double variance = sum / n;
        double std = Math.sqrt(variance);
        for (int i = 0; i < n; ++i)
            d_Chunks.set(i, nor.get(i) / std);
        /**
         * Differentiate
         */
        ArrayList<Double> diff_Chunks = new ArrayList<>(n);
        diff_Chunks.add((d_Chunks.get(1) - d_Chunks.get(0)));
        for (int i = 1; i < n - 1; ++i)
            diff_Chunks.add((d_Chunks.get(i + 1) - d_Chunks.get(i - 1)) / 2);
        diff_Chunks.add((d_Chunks.get(n - 1) - d_Chunks.get(n - 2)));
        /**
         * Square
         */
        for(int i = 0; i < n; ++i) {
            diff_Chunks.set(i, diff_Chunks.get(i) * diff_Chunks.get(i));
            double potentialMax = diff_Chunks.get(i);
            double potentialMax_ts = d_Chunks_ts.get(i);
            if(S1.getValue() == 0.0) {
                S1.setIndex(potentialMax_ts);
                S1.setValue(potentialMax);
                S1.setOriginal(valuesList.get(i));
            }
            else if(potentialMax_ts < S1.getIndex() + 180) {
                if(potentialMax > S1.getValue()) {
                    S1.setIndex(potentialMax_ts);
                    S1.setValue(potentialMax);
                    S1.setOriginal(valuesList.get(i));
                }
            }
            else {
                if(S1.getValue() > 5) {
                    performCallbacks(new NewPeak(S1.getOriginal(), S1.getIndex()));
                    Log.i(TAG, "potential Max= "+S1.getValue());
                }
                //audioPeakSeries.add((double)S1.getIndex(), S1.getValue());
                S1.setIndex(potentialMax_ts);
                S1.setValue(potentialMax);
                S1.setOriginal(valuesList.get(i));
            }
        }

        /**
         * Integral
         */
//        ArrayList<Double> integrated_chunk = new ArrayList<>();
//        for(int i=0; i<n; i++) {
//            double integral = 0.0;
//            for(int j=i; j==n; j--) {
//                integral = integral + diff_Chunks.get(j);
//            }
//            integrated_chunk.add(integral);
//        }

//        audioPeakSeries.add((double)S1.getIndex(), S1.getValue());
//
//        seriesRenderer.setRange(new double[] { 0, n, Collections.min(diff_Chunks)-5,
//                Collections.max(diff_Chunks) });
//        /**
//         * Plot norm,diff,sq signal
//         */
//        for(i = 0; i < n; ++i)
//            audioTimeSeries.add(i, diff_Chunks.get(i));
//        for(i = 0; i < n; ++i)
//            audioSignalSeries.add(i, d_Chunks.get(i));
//        /**
//         * Square
//         */
//        for (int i = 0; i < n; ++i)
//            diff_Chunks.set(i, diff_Chunks.get(i) * diff_Chunks.get(i));
//
////        seriesRenderer.setRange(new double[] { 0, n, Collections.min(diff_Chunks)-5,
////                Collections.max(diff_Chunks) });
//        /**
//         * Plot norm,diff,sq signal
//         */
//        for (int i = 0; i < n; ++i) {
//            /**
//             * add element to peak series if above threshold
//             * TODO: implement more accurate local maxima algorithm
//             */
//            if (diff_Chunks.get(i) > 7)
//                performCallbacks(new NewPeak(diff_Chunks.get(i), d_Chunks_ts.get(i)));
//            //audioPeakSeries.add(i, diff_Chunks.get(i));
//            //audioTimeSeries.add(i, diff_Chunks.get(i));
//        }
//        for(i = 0; i < n; ++i)
//            audioSignalSeries.add(i, d_Chunks.get(i));
    }

    @Override
    public void inputAudio(AudioData data) {
        List<Double> timeStamp, values;
        timeStamp = new ArrayList<>();
        values = new ArrayList<>();
        double ts = data.getTimestamp_ms();
        short[] shortData = data.getChunkData();
        for (short item : shortData) {
            timeStamp.add(ts);
            values.add((double) item);

            //Increment the timestamp with the length of single short value
            ts = ts + data.getSingleShortLength_ms();
        }

        processAudio(timeStamp, values);
    }

    private class PotentialMaxima {
        private double value;
        private double index;
        private double original;

        private PotentialMaxima(double v, double i) {
            this.value = v;
            this.index = i;
        }

        public double getOriginal() {
            return original;
        }

        public void setOriginal(double original) {
            this.original = original;
        }

        private double getValue() {
            return this.value;
        }

        private double getIndex() {
            return this.index;
        }

        private void setValue(double v) {
            this.value = v;
        }

        private void setIndex(double i) {
            this.index = i;
        }
    }
}
