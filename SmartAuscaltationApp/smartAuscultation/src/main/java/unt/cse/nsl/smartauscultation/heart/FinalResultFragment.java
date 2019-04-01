package unt.cse.nsl.smartauscultation.heart;

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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;

import unt.cse.nsl.smartauscultation.AudioRecordFragment;
import unt.cse.nsl.smartauscultation.Messages;
import unt.cse.nsl.smartauscultation.PreferenceDatabase;
import unt.cse.nsl.smartauscultation.R;
import unt.cse.nsl.smartauscultation.SmartAuscultationActivity;
import unt.nsl.util.i18n.NSLLocalizer;

/**
 * This Fragment shows the final split and Mur-Mur classification results on the display.
 * Additionally, it shows the graph of audio signals showing s1 and s2 sounds
 * Created by Anurag Chitnis on 10/14/2016.
 */

public class FinalResultFragment extends Fragment {
    /**
     * TAG for logging purpose
     */
    private static final String TAG = FinalResultFragment.class.getSimpleName();

    /**
     * Base layout of the graph container
     */
    private RelativeLayout analysisBase;
    /**
     * This variable will hold the series of data-points
     */
    private XYMultipleSeriesDataset dataSet;
    /**
     * This variable will hold the view related configuration
     */
    private XYMultipleSeriesRenderer seriesRenderer;
    private XYSeries audioPeakSeries;
    private XYSeries audioSeriesGeneric;
    private XYSeries audioSeriesS1;
    private XYSeries audioSeriesS2;
    private String filePath;
    TableLayout tableLayout;
    private static int signalCount;
//    /** number of data points in the heart sound */
//    private int dataPoints;
//    /** the starting index of S2*/
//    private int s2Index;
    /** This will hold the filtered heart sound which is to be played*/
    private float[] audioData;
    /** Button to play the heart sound*/
    private Button playButton;
    private Button patientDetailsButton;
    /** Play the heart sound in a loop*/
    private boolean audioPlaying = false;
    Bitmap mbitmap;
    Button captureScreenShot;

    private SmartAuscultationActivity smartAuscultationActivity;
    //private HeartSoundClassification heartSoundClassification;
    private ArrayList<LubDubSignal> lubDubSignalArrayList;

    private TextView auscultationAreaText;
    private TextView patientName;
    private TextView dateTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getActivity() instanceof SmartAuscultationActivity) {
            smartAuscultationActivity = (SmartAuscultationActivity) getActivity();
        }

        //heartSoundClassification = HeartSoundClassification.getInstance(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        XYSeriesRenderer audioRendererS1;
        XYSeriesRenderer audioRendererS2;
        XYSeriesRenderer audioRendererGeneric;
        XYSeriesRenderer audioPeakRenderer;
        String auscultationArea;

        // Fetch the list of lubdub signals from the smart auscultation activity
        lubDubSignalArrayList = smartAuscultationActivity.getLubDubSignalArrayList();

        // receive the filepath and auscultation area from the previous activity
        filePath = getArguments().getString("FilePath");
        auscultationArea = getArguments().getString("AuscultationArea");

        View v = inflater.inflate(R.layout.fragment_final_result, container, false);

        auscultationAreaText = (TextView) v.findViewById(R.id.auscultationAreaText);
        patientName = (TextView) v.findViewById(R.id.patient);
        dateTime = (TextView) v.findViewById(R.id.dateTime);

        tableLayout = (TableLayout) v.findViewById(R.id.statsTable);


        //take a screen shot button --> Ambu
        captureScreenShot = (Button) v.findViewById(R.id.capture_screen_shot);

        captureScreenShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mbitmap = getBitmapOFRootView(captureScreenShot);
//        imageView.setImageBitmap(mbitmap);
                File file = createImage(mbitmap);
                shareImage(file);
            }
        });

        playButton = (Button) v.findViewById(R.id.playButton);

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
                    playButton.setText("Play heart sound");
                }

            }
        });

        patientDetailsButton = (Button) v.findViewById(R.id.patientDetailsButton);
        patientDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smartAuscultationActivity.goToState(SmartAuscultationActivity.ActivityState.PATIENT_DETAILS);
            }
        });

        auscultationAreaText.setText("Auscultation Area : " + auscultationArea);
        patientName.setText("Physician Name: " + PreferenceDatabase.getCurrentlyLoadedUser().getName());

        playButton.setEnabled(false);
        analysisBase = (RelativeLayout) v.findViewById(R.id.analysis_base);
        audioSeriesS1 = new XYSeries("Audio S1");
        audioSeriesS2 = new XYSeries("Audio S2");
        audioSeriesGeneric = new XYSeries("Generic");
        audioPeakSeries = new XYSeries("Audio Peaks");
        dataSet = new XYMultipleSeriesDataset();
        seriesRenderer = new XYMultipleSeriesRenderer();
        audioRendererS1 = new XYSeriesRenderer();
        audioRendererS2 = new XYSeriesRenderer();
        audioRendererGeneric = new XYSeriesRenderer();
        audioPeakRenderer = new XYSeriesRenderer();

        dataSet.addSeries(audioSeriesS1);
        dataSet.addSeries(audioSeriesS2);
        dataSet.addSeries(audioSeriesGeneric);
        dataSet.addSeries(audioPeakSeries);

        // Setup the renderer
        seriesRenderer.setAxisTitleTextSize(35);
        seriesRenderer.setChartTitleTextSize(12);
        seriesRenderer.setLabelsTextSize(20);
        seriesRenderer.setLegendTextSize(15);
        seriesRenderer.setPointSize(30);
        seriesRenderer.setMargins(new int[]{0, 0, 0, 0});

        audioPeakRenderer.setColor(Color.GREEN);
        audioPeakRenderer.setPointStyle(PointStyle.CIRCLE);
        audioPeakRenderer.setFillBelowLine(false);
        audioPeakRenderer.setFillPoints(true);

        audioRendererS1.setColor(Color.MAGENTA);
        audioRendererS1.setFillBelowLine(false);
        audioRendererS2.setColor(Color.RED);
        audioRendererS2.setFillBelowLine(false);
        audioRendererGeneric.setColor(Color.BLUE);
        audioRendererGeneric.setFillBelowLine(false);

        seriesRenderer.addSeriesRenderer(audioRendererS1);
        seriesRenderer.addSeriesRenderer(audioRendererS2);
        seriesRenderer.addSeriesRenderer(audioRendererGeneric);
        seriesRenderer.addSeriesRenderer(audioPeakRenderer);

        seriesRenderer.setApplyBackgroundColor(false);
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

        // get S1 and S2 from file
        new ChunkDataTask().execute();

        return v;
    }

    private short maxValue(short array[]) {
        List<Short> list = new ArrayList<Short>();
        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);
        }
        return Collections.max(list);
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

    public static String getTimeString() {
        return NSLLocalizer.formatMessage(
                "BloodPressureActivity.time_string_format",
                Messages.RESOURCE_BUNDLE, new Date());
    }

    private class ChunkDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            createLubDubAudioSignals(lubDubSignalArrayList);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            new LowPassFilterTask().execute();
            //Reset the signal count as we are starting the new session
            signalCount = 0;
            /**
             * Send each lub-dub signal for split and classification
             */
            for(LubDubSignal lubDubSignal : lubDubSignalArrayList)
                new CalculateSplitTask().execute(lubDubSignal);
        }
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
            double a[] = {1,-8.92174918006447,35.9066467138819,-85.8547234855195,135.073075636373,-146.114103360802,110.065403393961,-57.0120252332688,19.4346111722794,-3.93706390158405,0.359928245063557};
            double b[] = {5.97957997553802e-05,0,-0.000298978998776901,0,0.000597957997553802,0,-0.000597957997553802,0,0.000298978998776901,0,-5.97957997553802e-05};


            ArrayList<Float> audioDataList = new ArrayList<>();

            int M = b.length-1, N = a.length-1;
            int m, n;
            double yi, yj, mean = 0;

            for(LubDubSignal lubDubSignal : lubDubSignalArrayList) {
                double[] heartSound = lubDubSignal.getLubDubSoundDoubleValueArray();
                int dataPoints = lubDubSignal.getLubDubSound().size();
                double filtered_audio[] = new double[dataPoints];

                //filter the audio data using a rational transfer function defined
                //by the numerator and denominator coefficients b and a, respectively
                for (int i = 0; i < dataPoints; i++) {
                    m = Math.min(M, i);
                    yi = 0;
                    for (int k = 0; k <= m; k++) {
                        yi += b[k] * heartSound[i-k];
                    }
                    n = Math.min(N, i);
                    yj = 0;
                    for (int k = 1; k <= n; k++) {
                        yj += a[k] * filtered_audio[i - k];
                    }
                    filtered_audio[i] = yi - yj;

                    //calculate sum
                    mean += filtered_audio[i];
                }
                Log.i(TAG, "audio data is filtered using lowpass Butterworth filter");

                //calculate mean
                mean /= dataPoints;

                //calculate standard deviation
                double std = 0;
                for (double t : filtered_audio)
                    std += Math.pow((mean - t), 2);
                std /= dataPoints;
                std = Math.sqrt(std);

                //scale down the filtered audio
                //convert double to float as AudioTrack generates audio from float values
                for (int i = 0; i < heartSound.length; i++) {
                    audioDataList.add((float) ((filtered_audio[i] - mean) / std));
                }
            }

            audioData = new float[audioDataList.size()];
            int i=0;
            for (float audioDataItem : audioDataList) {
                audioData[i] = audioDataItem;
                i++;
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
            playButton.setText("Play");
            playButton.setEnabled(true);
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

    /**
     * A class that calculates the split value in heart sound.
     * It extends AsyncTask, which is an utility class in Android
     * that provides a thread to run long-running tasks without
     * tying up the user interface or making an application unresponsive.
     *
     */
    private class CalculateSplitTask extends AsyncTask<LubDubSignal, Void, LubDubSignal> {
        private HeartSplitSound heartSplitSound;
        private float s1SplitTime, s2SplitTime;
        private boolean isS2Split, isS1Split;
        private String text;

        @Override
        protected LubDubSignal doInBackground(LubDubSignal... params) {
            LubDubSignal lubDubSignal = params[0];

            heartSplitSound = new HeartSplitSound(lubDubSignal.getLubDubSoundDoubleValueArray(), lubDubSignal.getS2index());
            heartSplitSound.calculateSplit();
            s1SplitTime = heartSplitSound.getS1SplitTime();
            s2SplitTime = heartSplitSound.getS2SplitTime();

//            if (s1SplitTime < 40.0 && s2SplitTime < 40.0)
//                text = "S1 and S2 split time < 40 ms";
//            else if (s1SplitTime > 40.0 && s2SplitTime < 40.0)
//                text = "S1 split time > 40 ms, " + "\n" + "You have a split in first heart sound";
//            else if (s1SplitTime < 40.0 && s2SplitTime > 40.0) {
//                isS2Split = true;
//                text = "S2 Split time > 40 ms, " + "\n" + "You have a split in second heart sound";
//            } else {
//                isS2Split = true;
//                text = "S1 and S2 split time > 40 ms, " + "\n" + "You have splits in both the heart sounds";
//            }
//            Log.d(TAG, "S1 split time = "+s1SplitTime+", S2 split time = "+s2SplitTime);
            if (s1SplitTime > 40.0)
                isS1Split = true;

            if (s2SplitTime > 40.0)
                isS2Split = true;


            // set the value of split detected so that it can be further used for classification
            lubDubSignal.setSplitDetected(isS2Split);

            return lubDubSignal;
        }

        @Override
        protected void onPostExecute(LubDubSignal lubDubSignal) {
            super.onPostExecute(lubDubSignal);
            dateTime.setText(new Date().toString());

            TextView S1SplitTimeDisplay = new TextView(smartAuscultationActivity);//(TextView) findViewById(R.id.S1splitTimeDisplay);
            TextView S2SplitTimeDisplay = new TextView(smartAuscultationActivity);//(TextView) findViewById(R.id.S2splitTimeDisplay);
            TextView splitMessageText = new TextView(smartAuscultationActivity);
            splitMessageText.setPadding(4,4,4,4);
            splitMessageText.setBackgroundResource(R.drawable.cell_shape);

            S1SplitTimeDisplay.setText(s1SplitTime + " ms");
            S2SplitTimeDisplay.setText(s2SplitTime + " ms");
            if(isS1Split && isS2Split) {
                splitMessageText.setText("S1, S2");
            }
            else if(isS1Split)
                splitMessageText.setText("S1");
            else if(isS2Split)
                splitMessageText.setText("S2");
            else
                splitMessageText.setText(" -- ");

            S1SplitTimeDisplay.setBackgroundResource(R.drawable.cell_shape);
            S1SplitTimeDisplay.setPadding(4,4,4,4);
            S2SplitTimeDisplay.setBackgroundResource(R.drawable.cell_shape);
            S2SplitTimeDisplay.setPadding(4,4,4,4);

            TableRow tableRow = new TableRow(smartAuscultationActivity);
            TextView countText = new TextView(smartAuscultationActivity);
            signalCount++;
            countText.setText("#"+signalCount);
            countText.setPadding(4,4,4,4);
            countText.setBackgroundResource(R.drawable.cell_shape);
            tableRow.addView(countText);
            tableRow.addView(S1SplitTimeDisplay);
            tableRow.addView(S2SplitTimeDisplay);
            tableRow.addView(splitMessageText);

            new ClassificationTask(tableRow).execute(lubDubSignal);
        }
    }

    /**
     * A class that classifies the heart sound into normal or specific murmur.
     * It extends AsyncTask, which is an utility class in Android
     * that provides a thread to run long-running tasks without
     * tying up the user interface or making an application unresponsive.
     *
     */
    private class ClassificationTask extends AsyncTask<LubDubSignal, Void, String> {
        private int hmmIndex = 1;
        private TableRow tableRow;
        private HeartSoundClassification heartSoundClassification;

        ClassificationTask(TableRow tableRow) {
            this.tableRow = tableRow;
            heartSoundClassification = new HeartSoundClassification();
        }

        @Override
        protected String doInBackground(LubDubSignal... params) {
            LubDubSignal lubDubSignal = params[0];
            return heartSoundClassification.getClassificationResult(lubDubSignal.isSplitDetected(), lubDubSignal.getLubDubSoundDoubleValueArray());
        }

        @Override
        protected void onPostExecute(String murmurResult) {
            super.onPostExecute(murmurResult);
            Log.d(TAG, "Estimated murmur: " + murmurResult);
            TextView murmurText = new TextView(smartAuscultationActivity);
            murmurText.setBackgroundResource(R.drawable.cell_shape);
            murmurText.setText(murmurResult);
            murmurText.setPadding(4,4,4,4);
            tableRow.addView(murmurText);
            tableLayout.addView(tableRow);
            // As the result is available, write it to log file
            writeResultToLogFile(murmurResult, heartSoundClassification);
        }
    }

    /**
     * This method writes all the results to the log file.
     * It should be invoked when all the results are available to be written on the file.
     * Results : Probabilities of all the classifications, classified murmur
     */
    private void writeResultToLogFile(String murMurResult, HeartSoundClassification heartSoundClassification) {
        StringBuilder resultString = new StringBuilder();
        String[] hmmName = heartSoundClassification.getHmmName();
        double[] probabilities = heartSoundClassification.getProbability();
        for(int i=0; i< probabilities.length; i++) {
            resultString.append(hmmName[i] + "," + probabilities[i] + "\n");
        }

        resultString.append("Classification Result : "+murMurResult +"," + heartSoundClassification.getHmmIndex()+"\n");

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
     * This method creates the lub dub signals required for split detection and Mur-Mur classification
     * It performs this task in a new worker thread
     * @param lubDubSignalList This is the list of chosen lubdub signals
     */
    private void createLubDubAudioSignals(final List<LubDubSignal> lubDubSignalList) {
        List<AudioData> audioDataList= smartAuscultationActivity.getAudioDataList();


        int i = 0;
        for (LubDubSignal lubDubSignal : lubDubSignalList) {
            ArrayList<Short> chunkData = new ArrayList<>();
            boolean isS2Reached = false;
            /**
             * Iterate through the audio data list
             */
            audioListLoop :
            while(i<audioDataList.size()) {
                //chunkData = new ArrayList<>();
                short[] shortData = audioDataList.get(i).getChunkData();
                double ts = audioDataList.get(i).getTimestamp_ms();
                for (short item : shortData) {

                    if (ts >= lubDubSignal.getFirstS1Timestamp()) {
                        //Checking the size of the chunk data so as to avoid the OutOfMemory Exception
                        if (chunkData.size() < 11000)
                            chunkData.add(item);
                        if (isS2Reached)
                            audioSeriesS2.add(ts, item);
                        else
                            audioSeriesS1.add(ts, item);
                    } else {
                        audioSeriesGeneric.add(ts, item);
                    }

                    if(ts >= lubDubSignal.getSecondS1Timestamp()) {
                        //First lubdub sound has been captured, let us break the audioList iteration loop
                        break audioListLoop;
                    }
                    /**
                     * Make note that s2 has reached
                     */
                    if (ts < lubDubSignal.getFirstS2Timestamp() + 5 && ts > lubDubSignal.getFirstS2Timestamp() - 5) {
                        lubDubSignal.setS2index(chunkData.size());
                        isS2Reached = true;
                    }
                    //Increment the timestamp with the length of single short value
                    ts = ts + audioDataList.get(i).getSingleShortLength_ms();
                }
                i++;
            }

            lubDubSignal.setLubDubSound(chunkData);
            Log.d(TAG, "Chunk data size: " + chunkData.size());
        }
        int heightValue = AudioRecordFragment.getHeightValueForGraph();
        seriesRenderer.setRange(new double[]{lubDubSignalArrayList.get(0).getFirstS1Timestamp()-100, lubDubSignalArrayList.get(0).getSecondS1Timestamp()+2000, -heightValue, heightValue});

        for (NewPeak peak : smartAuscultationActivity.getAudioFindingPeaks()) {
            audioPeakSeries.add(peak.getTimestampMillis(), 0);
        }

    }
    private void makeFinalDecision() {
        for(LubDubSignal lubDubSignal : lubDubSignalArrayList) {
            lubDubSignal.getMurmurResult();
        }
    }
}
