package unt.cse.nsl.smartauscultation.lung;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.unt.cse.nsl.audio.AudioData;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import unt.cse.nsl.smartauscultation.R;
import unt.cse.nsl.smartauscultation.SmartAuscultationActivity;

/**
 * Created by Anurag Chitnis on 10/31/2016.
 */

public class LungResultFragment extends Fragment{

    private TextView auscultationAreaText;
    private TextView patientName;
    private TextView dateTime;
    private TextView classificationResult;
    private RelativeLayout analysisBase;
    private double[] filtered_audio;
    private LungSoundClassification lungSoundClassification;
    private String filePath;
    private Bitmap mbitmap;
    private Button captureScreenShot;
    private Button patientDetailsButton;
    private float[] audioData;
    private Button playButton;
    /** Play the lung sound in a loop*/
    private boolean audioPlaying = false;

    /**
     * This variable will hold the series of data-points
     */
    private XYMultipleSeriesDataset dataSet;

    /**
     * This variable will hold the view related configuration
     */
    private XYMultipleSeriesRenderer seriesRenderer;
    private XYSeriesRenderer audioRendererGeneric;
    private XYSeries audioSeries;

    private SmartAuscultationActivity smartAuscultationActivity;

    private ArrayList<Short> chunkData = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getActivity() instanceof SmartAuscultationActivity) {
            smartAuscultationActivity = (SmartAuscultationActivity) getActivity();
        }

        lungSoundClassification = LungSoundClassification.getInstance(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lung_result, container, false);

        filePath = getArguments().getString("FilePath");

        auscultationAreaText = (TextView) v.findViewById(R.id.lungAuscultationAreaText);
        patientName = (TextView) v.findViewById(R.id.patientName);
        dateTime = (TextView) v.findViewById(R.id.dateTimeLabel);
        classificationResult = (TextView) v.findViewById(R.id.lungClassificationResult);

        auscultationAreaText.setText(smartAuscultationActivity.getSelectedAuscultationArea().toString());

        analysisBase = (RelativeLayout) v.findViewById(R.id.lungBaseLayout);
        dataSet = new XYMultipleSeriesDataset();
        audioSeries = new XYSeries("Filtered");
        seriesRenderer = new XYMultipleSeriesRenderer();
        audioRendererGeneric = new XYSeriesRenderer();

        dataSet.addSeries(audioSeries);

        // Setup the renderer
        seriesRenderer.setAxisTitleTextSize(35);
        seriesRenderer.setChartTitleTextSize(12);
        seriesRenderer.setLabelsTextSize(20);
        seriesRenderer.setLegendTextSize(15);
        seriesRenderer.setPointSize(30);
        seriesRenderer.setMargins(new int[]{0, 0, 0, 0});

        audioRendererGeneric.setColor(Color.BLUE);
        audioRendererGeneric.setFillBelowLine(false);

        seriesRenderer.addSeriesRenderer(audioRendererGeneric);

        seriesRenderer.setApplyBackgroundColor(false);
//        seriesRenderer.setRange(new double[]{TimeStampFirstS1-100, TimeStampSecondS1+1000, -8000, 8000});
        seriesRenderer.setShowLegend(false);
        seriesRenderer.setShowAxes(true);
        seriesRenderer.setAxesColor(Color.BLUE);
        seriesRenderer.setXLabels(15);
        seriesRenderer.setXTitle("Time (ms)");
        seriesRenderer.setXLabelsAlign(Paint.Align.CENTER);
        seriesRenderer.setShowGrid(true);
        seriesRenderer.setGridColor(Color.GRAY);
        seriesRenderer.setShowLabels(true);
        seriesRenderer.setZoomEnabled(false);
        seriesRenderer.setFitLegend(true);

        //take a screen shot button --> Ambu
        captureScreenShot = (Button) v.findViewById(R.id.shareLungResult);

        captureScreenShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mbitmap = getBitmapOFRootView(captureScreenShot);
//        imageView.setImageBitmap(mbitmap);
                File file = createImage(mbitmap);
                shareImage(file);
            }
        });

        patientDetailsButton = (Button) v.findViewById(R.id.lungPatientDetailsButton);
        patientDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smartAuscultationActivity.goToState(SmartAuscultationActivity.ActivityState.PATIENT_DETAILS);
            }
        });

        playButton = (Button) v.findViewById(R.id.playLungSound);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!audioPlaying) {
                    audioPlaying = true;
                    playButton.setText("Stop");
                    //generate audio and play it
                    AudioGeneratorTask AudioGenerator = new AudioGeneratorTask();
                    AudioGenerator.execute();
                } else {
                    audioPlaying = false;
                    playButton.setText("Play Lung sound");
                }

            }
        });

        new LowPassFilterTask().execute();

        return v;
    }

    public Bitmap getBitmapOFRootView(View v) {
        View root_view = v.getRootView();
        root_view.setDrawingCacheEnabled(true);
        Bitmap bitmap1 = root_view.getDrawingCache();
        return bitmap1;
    }

    public File createImage(Bitmap bmp) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
        File file = new File(filePath + ".jpg");
        try {
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(bytes.toByteArray());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    private void shareImage(File file){
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(intent, "Share Screenshot"));
    }

    /**
     * A class that filters audio data using 10th order Butterworth lowpass filter.
     * Next, the data is scaled down to avoid clipping by the AudioTrack class.
     * It extends AsyncTask, which is an utility class in Android
     * that provides a thread to run long-running tasks without
     * tying up the user interface or making an application unresponsive.
     *
     */
    private class LowPassFilterTask extends AsyncTask<Void, Void, Void> {
        private final String TAG = LowPassFilterTask.class.getSimpleName();

        @Override
        protected Void doInBackground(Void... params) {
            // transfer function coefficients of 10th order Butterworth lowpass filter
            // with 300Hz cutoff frequency (results obtained from MATLAB
           /* double a[] = {1,-8.49404585203764,32.5660116438407,
                    -74.2040555114125,111.264652543110,-114.702495477183,
                    82.3226284416802,-40.6121844053564,13.1785686733764,
                    -2.53981936603812,0.220739569563460};
            double b[] = {2.53459716051657e-10,2.53459716051657e-09,1.14056872223245e-08,
                    3.04151659261988e-08,5.32265403708479e-08,6.38718484450174e-08,
                    5.32265403708479e-08,3.04151659261988e-08,1.14056872223245e-08,
                    2.53459716051657e-09,2.53459716051657e-10};*/

            // coefficients for bandpass filter 50Hz - 450Hz
//            double a[] = {1,-8.92174918006447,35.9066467138819,-85.8547234855195,135.073075636373,-146.114103360802,110.065403393961,-57.0120252332688,19.4346111722794,-3.93706390158405,0.359928245063557};
//            double b[] = {5.97957997553802e-05,0,-0.000298978998776901,0,0.000597957997553802,0,-0.000597957997553802,0,0.000298978998776901,0,-5.97957997553802e-05};

            double a[] = {1,	-1.93386010022714,	1.17307074838362,	-0.405049025290700,	0.173070748383618};
            double b[] = {0.270258169560164,	0,	-0.540516339120327,	0,	0.270258169560164};
             // Clear the chunk data
            chunkData.clear();

            for(AudioData data : smartAuscultationActivity.getAudioDataList()) {
                for(short item : data.getChunkData()) {
                    //added this check to fix the issue in HTC, where blank space was getting added on the graph
                    if(item != 0)
                        chunkData.add(item);
                }
            }
            int dataPoints = chunkData.size();
            filtered_audio = new double[dataPoints];
            //audioData = new float[dataPoints];
            int M = b.length-1, N = a.length-1;
            int m, n;
            double yi, yj, mean = 0;

            //filter the audio data using a rational transfer function defined
            //by the numerator and denominator coefficients b and a, respectively
            for (int i = 0; i < dataPoints; i++) {
                m = Math.min(M, i);
                yi = 0;
                for (int k = 0; k <= m; k++) {
                    yi += b[k] * chunkData.get(i-k);
                }
                n = Math.min(N, i);
                yj = 0;
                for (int k = 1; k <= n; k++) {
                    yj += a[k] * filtered_audio[i-k];
                }
                filtered_audio[i] = yi - yj;

                //calculate sum
                mean += filtered_audio[i];
            }
            Log.i(TAG, "audio data is filtered using lowpass Butterworth filter");
            int i=0;
            for(double point : filtered_audio) {
                audioSeries.add(i,point);
                i++;
            }

            //calculate mean
            mean /= dataPoints;

            //calculate standard deviation
            double std = 0;
            for(double t : filtered_audio)
                std += Math.pow((mean-t), 2);
            std /= dataPoints;
            std = Math.sqrt(std);

            //scale down the filtered audio
            //convert double to float as AudioTrack generates audio from float values
            audioData = new float[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                audioData[j] = (float) ((filtered_audio[j] - mean) / std);
            }

            //addData();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Make a view
            GraphicalView graphicalView = ChartFactory
                    .getLineChartView(getActivity(), dataSet, seriesRenderer);

            analysisBase.addView(graphicalView, 0);

            new ClassificationTask().execute();
        }
    }

    /**
     * A class that classifies the heart sound into normal or specific murmur.
     * It extends AsyncTask, which is an utility class in Android
     * that provides a thread to run long-running tasks without
     * tying up the user interface or making an application unresponsive.
     *
     */
    private class ClassificationTask extends AsyncTask<Void, Void, String> {
        private int hmmIndex = 1;
        @Override
        protected String doInBackground(Void... params) {
            return lungSoundClassification.getClassificationResult(filtered_audio);
        }

        @Override
        protected void onPostExecute(String murmurResult) {
            super.onPostExecute(murmurResult);
            classificationResult.setText("Estimated classification: " + murmurResult);
            // As the result is available, write it to log file
            writeResultToLogFile();

            //dialog.dismiss();
        }
    }

    /**
     * This method writes all the results to the log file.
     * It should be invoked when all the results are available to be written on the file.
     * Results : Probabilities of all the classifications, classified murmur
     */
    private void writeResultToLogFile() {
        StringBuilder resultString = new StringBuilder();
        String[] hmmName = lungSoundClassification.getHmmName();
        double[] probabilities = lungSoundClassification.getProbability();
        for(int i=0; i< probabilities.length; i++) {
            resultString.append(hmmName[i] + "," + probabilities[i] + "\n");
        }

        resultString.append(classificationResult.getText() +"," + lungSoundClassification.getHmmIndex());

        BufferedWriter bw = null;
        try {
            // Open the existing file for appending the data
            bw = new BufferedWriter(new FileWriter(smartAuscultationActivity.getLogFile(), true));
            bw.write(resultString.toString());
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * A class that creates audio file and plays it.
     * It extends AsyncTask, which is an utility class in Android
     * that provides a thread to run long-running tasks without
     * tying up the user interface or making an application unresponsive.
     *
     */
    @TargetApi(21)
    private class AudioGeneratorTask extends AsyncTask<Void, Void, Void> {

        private final String TAG = AudioGeneratorTask.class.getSimpleName();

        @Override
        protected Void doInBackground(Void... params) {
            final int SAMPLE_RATE = 6500;

            int minSize = AudioTrack.getMinBufferSize(SAMPLE_RATE,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_FLOAT);
            Log.i(TAG, "AudioTrack.minSize = " + minSize);

            // configure an audio track
            AudioTrack audioTrack = new AudioTrack(
                    AudioManager.STREAM_MUSIC, SAMPLE_RATE,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_FLOAT,
                    minSize,
                    AudioTrack.MODE_STREAM);
            Log.i(TAG, "created AudioTrack");

            // play the audio track
            audioTrack.play();

            // write data into the player
            while (audioPlaying) {
                audioTrack.write(audioData, 0, audioData.length, AudioTrack.WRITE_BLOCKING);
            }

            return null;
        }
    }
}
