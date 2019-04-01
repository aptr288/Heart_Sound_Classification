package unt.cse.nsl.smartauscultation;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

import static android.content.ContentValues.TAG;

public class SignalProcessingActivity extends Activity {

    private Button StartButton;
    private Button StopButton;

    private PotentialMaxima S1 = new PotentialMaxima(0.0, 0);

    /**
     * audioTimeSeries = norm,diff,sq signal
     * audioSignalSeries = original signal
     * audioPeakSeries = peak finding
     */
    private TimeSeries audioTimeSeries;
    private TimeSeries audioSignalSeries;
    private XYSeries audioPeakSeries;
    private XYMultipleSeriesRenderer seriesRenderer;
    private GraphicalView graphicalView;
    private boolean isPlaying;

    private static final int RECORD_TIME = 7000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signal_processing);

        isPlaying = false;
        StartButton = (Button) findViewById(R.id.StartButton);
        StopButton = (Button) findViewById(R.id.StopButton);

        FrameLayout graphContainer = (FrameLayout) findViewById(R.id.graphContainer);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.recordingProgressBar);
        TextView progressTextView = (TextView) findViewById(R.id.recordingProgressText);

        progressBar.setMax(RECORD_TIME);
        progressBar.setProgress(0);
        progressTextView.setText(Messages
                .getString("BloodPressureActivity.finding_steth_instruction_text"));

        XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();
        seriesRenderer = new XYMultipleSeriesRenderer();
        audioTimeSeries = new TimeSeries("Audio");
        audioSignalSeries = new TimeSeries("Signal");
        audioPeakSeries = new XYSeries("Peaks");
        XYSeriesRenderer audioRenderer = new XYSeriesRenderer();
        XYSeriesRenderer audioSignalRenderer = new XYSeriesRenderer();
        XYSeriesRenderer audioPeakRenderer = new XYSeriesRenderer();

        dataSet.addSeries(audioTimeSeries);
        dataSet.addSeries(audioSignalSeries);
        dataSet.addSeries(audioPeakSeries);

        audioRenderer.setColor(Color.BLUE);
        audioRenderer.setFillBelowLine(false);

        audioSignalRenderer.setColor(Color.YELLOW);
        audioSignalRenderer.setFillBelowLine(false);

        audioPeakRenderer.setColor(Color.GREEN);
        audioPeakRenderer.setPointStyle(PointStyle.CIRCLE);
        audioPeakRenderer.setFillBelowLine(false);
        audioPeakRenderer.setFillPoints(true);

        seriesRenderer.addSeriesRenderer(audioRenderer);
        seriesRenderer.addSeriesRenderer(audioSignalRenderer);
        seriesRenderer.addSeriesRenderer(audioPeakRenderer);

        seriesRenderer.setAxisTitleTextSize(12);
        seriesRenderer.setChartTitleTextSize(12);
        seriesRenderer.setLabelsTextSize(15);
        seriesRenderer.setLegendTextSize(15);
        seriesRenderer.setPointSize(15);
        seriesRenderer.setMargins(new int[] { 0, 0, 0, 0 });
        seriesRenderer.setApplyBackgroundColor(false);
        seriesRenderer.setShowLegend(false);
        seriesRenderer.setZoomEnabled(false);

        graphicalView = ChartFactory
                .getLineChartView(this, dataSet, seriesRenderer);
        graphContainer.addView(graphicalView);
    }


    public void start(View view) {
        if(!isPlaying) {
            isPlaying = true;
            StartButton.setEnabled(false);
            StopButton.setEnabled(true);
            startPlayback();
        }
    }

    public void stop(View view) {
        if(isPlaying) {
            isPlaying = false;
            StopButton.setEnabled(false);
            StartButton.setEnabled(true);
            stopPlayback();
        }
    }

    private void startPlayback() {
        String s;
        String line;

        int i;
        int length;

        int chunkData;

        StringTokenizer st;
        InputStreamReader is = null;

        /**
         * TODO: Change containers to maps for smoother integration with microphone
         * audio. Audio will come in as a timestamp with 2048 elements of shorts
         */
        ArrayList<Double> Timestamps = new ArrayList<>();
        ArrayList<Integer> Chunks = new ArrayList<>();

        try {
            /**
             * Open file for parsing
             */
            is = new InputStreamReader(getAssets()
                    .open("anurag- 2016.12.14- 15.28.06.csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader reader = null;
        try {
            /**
             * Create parser
             */
            if (is != null)
                reader = new BufferedReader(is);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            /**
             * Read in file line by line, tokenize, and parse
             */
            while ((line = reader.readLine()) != null) {
                st = new StringTokenizer(line, ",");
                s = st.nextToken();
                if (s.compareTo("AUDIO") == 0) {
                    double timestamp = Long.parseLong(st.nextToken());
                    Timestamps.add(timestamp);
                    length = Integer.parseInt(st.nextToken());
                    for (i = 0; i < length; ++i) {
                        chunkData = Integer.parseInt(st.nextToken());
                        Chunks.add(chunkData);
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        /**
         * Downsample
         */
        int size = Chunks.size();
        ArrayList<Double> d_Chunks = new ArrayList<>();
        for(i = 0; i < size; i+=16)
            d_Chunks.add((double)Chunks.get(i));
        /**
         * Normalize
         */
        double sum = 0;
        int n = d_Chunks.size();
        for(double d : d_Chunks)
            sum += d;
        double mean = sum/(double) n;
        sum = 0;
        ArrayList<Double> nor = new ArrayList<>(n);
        for(i = 0; i < n; ++i)
            nor.add(i,d_Chunks.get(i)-mean);
        for(double a : d_Chunks)
            sum += (a-mean) * (a-mean);
        double variance = sum/n;
        double std = Math.sqrt(variance);
        for(i = 0; i < n; ++i)
            d_Chunks.set(i, nor.get(i)/std);
        /**
         * Differentiate
         */
        ArrayList<Double> diff_Chunks = new ArrayList<>(n);
        diff_Chunks.add((d_Chunks.get(1) - d_Chunks.get(0)));
        for(i = 1; i < n-1; ++i)
            diff_Chunks.add((d_Chunks.get(i+1)-d_Chunks.get(i-1))/2);
        diff_Chunks.add((d_Chunks.get(n-1) - d_Chunks.get(n-2)));
        /**
         * Square
         */
        for(i = 0; i < n; ++i) {
            diff_Chunks.set(i, diff_Chunks.get(i) * diff_Chunks.get(i));
            double potentialMax = diff_Chunks.get(i);
            //double potentialMax_ts = d_Chunks_ts.get(i);
            if(S1.getValue() == 0.0) {
                S1.setIndex(i);
                S1.setValue(potentialMax);
                S1.setOriginal(d_Chunks.get(i));
            }
            else if(i < S1.getIndex() + 110) {
                if(potentialMax > S1.getValue()) {
                    S1.setIndex(i);
                    S1.setValue(potentialMax);
                    S1.setOriginal(d_Chunks.get(i));
                }
            }
            else {
                //performCallbacks(new NewPeak(S1.getOriginal(), S1.getIndex()));
                audioPeakSeries.add((double)S1.getIndex(), S1.getOriginal());
                S1.setIndex(i);
                S1.setValue(potentialMax);
                S1.setOriginal(d_Chunks.get(i));
            }
        }

        audioPeakSeries.add((double)S1.getIndex(), S1.getValue());

        seriesRenderer.setRange(new double[] { 0, 4000, Collections.min(diff_Chunks)-5,
                Collections.max(diff_Chunks) });
        /**
         * Plot norm,diff,sq signal
         */
//        for(i = 0; i < n; ++i)
//            audioTimeSeries.add(i, diff_Chunks.get(i));
        for(i = 0; i < n; ++i)
            audioSignalSeries.add(i, d_Chunks.get(i));

        graphicalView.repaint();
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

    private void stopPlayback() {

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
