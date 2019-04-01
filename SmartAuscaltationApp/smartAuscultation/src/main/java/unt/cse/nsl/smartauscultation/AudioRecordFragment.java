package unt.cse.nsl.smartauscultation;

import android.app.Fragment;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import org.unt.cse.nsl.audio.AudioData;
import org.unt.cse.nsl.audio.AudioInput;
import org.unt.cse.nsl.audio.AudioPeak;
import org.unt.cse.nsl.audio.AudioPeakListener;
import org.unt.cse.nsl.audio.MicrophonePoller;
import org.unt.cse.nsl.bloodpressure.BloodPressureDataRecorder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import unt.cse.nsl.smartauscultation.heart.NewAudioPeakListener;
import unt.cse.nsl.smartauscultation.heart.NewPeak;
import unt.nsl.util.i18n.NSLLocalizer;

/**
 * Created by Anurag Chitnis on 10/5/2016.
 */

public class AudioRecordFragment extends Fragment implements AudioInput, AudioPeakListener, NewAudioPeakListener {

    private static final String TAG = AudioRecordFragment.class.getName();

    private XYMultipleSeriesDataset dataSet;
    private TimeSeries audioTimeSeries;
    private XYSeriesRenderer audioRenderer;
    private XYSeries audioPeakSeries;
    private XYSeriesRenderer audioPeakRenderer;
    private XYMultipleSeriesRenderer seriesRenderer;
    //private AudioPeakFinder audioPeakFinder;
    private GraphicalView graphicalView;
    private static long dataCount;
    private Button recordButton;
    TextView recordingProgressTextView;
    private ProgressBar progressBar;
    private FrameLayout graphContainer;
    private ImageView instructionImage;
    private Button startStopButton;
    private BloodPressureDataRecorder bloodPressureDataRecorder;
    private boolean isCancled;
    private boolean isRecording;
    private boolean isPlaying;
    private String recentFileName;
    private SmartAuscultationActivity smartAuscultationActivity;
    private CopyOnWriteArrayList<NewPeak> audioPeakQ = new CopyOnWriteArrayList<>();
    private List<AudioData> audioDataList = new ArrayList<>();

    String savedTimeString;

    private static int heightValueForGraph;

    public static int getHeightValueForGraph() {
        return heightValueForGraph;
    }

    public void setHeightValueForGraph(short[] array) {
        List<Short> list = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);
        }
        //Add the 1000 to the actual height of the graph so that the graph fits inside the box
        int heightValueForGraph = Collections.max(list) + 2000;
        //If we already have the max value, ignore the new value
        if(heightValueForGraph > this.heightValueForGraph)
            this.heightValueForGraph = heightValueForGraph;
    }

    public String getSavedTimeString() {
        if(savedTimeString == null)
            savedTimeString = getTimeString();
        return savedTimeString;
    }

    private AudioTrack audioTrack;
    private String selectedModule;

    private boolean isReplaying;

    private static final int RECORDING_TIME = 10000; //Recording time in milliseconds
    private static final int INTERVAL_TIME = 1000; //Recording time in milliseconds
    private static final int SAMPLING_RATE = 8000;
    private static final int CHART_LENGTH_MS = 4000;

    public String getRecentFileName() {
        return recentFileName;
    }

    public List<AudioData> getAudioDataList() {
        return audioDataList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bloodPressureDataRecorder = new BloodPressureDataRecorder();
//        audioPeakFinder = new AudioPeakFinder();
        if(getActivity() instanceof SmartAuscultationActivity) {
            smartAuscultationActivity = (SmartAuscultationActivity) getActivity();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_audio_record, container, false);

        this.isReplaying = getArguments().getBoolean("isReplaying");
        this.selectedModule = getArguments().getString("selectedModule");

        graphContainer = (FrameLayout) v.findViewById(R.id.graphContainer);
        //instructionImage = new ImageView(getActivity());
        //instructionImage.setImageResource(R.drawable.inst_record_heart);
        recordButton = (Button) v.findViewById(R.id.recordButton);
        startStopButton = (Button) v.findViewById(R.id.startStopButton);
        recordingProgressTextView = (TextView) v.findViewById(R.id.recordingProgressText);
        recordingProgressTextView.setText(Messages
                .getString("BloodPressureActivity.finding_steth_instruction_text"));
        progressBar = (ProgressBar) v.findViewById(R.id.recordingProgressBar);
        progressBar.setMax(RECORDING_TIME); // progress bar should show the values in percentage, that is max value is 100
        progressBar.setProgress(0); //Initially set the progress to zero

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRecording) {
                    isCancled = true;
                    //Enable the start-stop button for playback
                    startStopButton.setEnabled(true);
                    finishAndSave();
                }
                else {
                    // Enter the state machine into the new state RECORDING
                    smartAuscultationActivity.goToState(SmartAuscultationActivity.ActivityState.RECORDING);
                    //Disable the start-stop button on playback as we are recording the audio
                    startStopButton.setEnabled(false);

                    //Start the operations of bloodPressureDataRecorder to record the audio data
                    bloodPressureDataRecorder.start(getSaveFile());
                    MicrophonePoller
                            .registerListener(bloodPressureDataRecorder);
                    /**
                     * Start the countdown to display the progress of audio recording on the UI
                     */
                    new CountDownTimer(RECORDING_TIME, INTERVAL_TIME) {
                        int progress;

                        public void onTick(long millisUntilFinished) {
                            progressBar.setProgress(progress);
                            recordingProgressTextView.setText("Recording... Wait for "+millisUntilFinished/1000+" Sec");
                            progress = progress + INTERVAL_TIME;
                            if(isCancled)
                                this.cancel();
                            //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                        }

                        public void onFinish() {
                            finishAndSave();
                        }
                    }.start();

                    isRecording = true;
                    recordButton.setText("Finish");
                }
            }
        });

        startStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying) {
                    startStopButton.setText("Start");
                    //Disable the record button when the audio playback is stopped
                    recordButton.setEnabled(false);
                    stopPlayback();
                }
                else {
                    startStopButton.setText("Stop");
                    //Enable the record button when audio playback starts
                    recordButton.setEnabled(true);
                    startPlayback();
                }
            }
        });

        dataSet = new XYMultipleSeriesDataset();
        seriesRenderer = new XYMultipleSeriesRenderer();
        audioTimeSeries = new TimeSeries("Audio");
        audioPeakSeries = new XYSeries("Audio Peaks");
        audioRenderer = new XYSeriesRenderer();
        audioPeakRenderer = new XYSeriesRenderer();

        dataSet.addSeries(audioPeakSeries);
        dataSet.addSeries(audioTimeSeries);

        audioRenderer.setColor(Color.BLUE);
        audioRenderer.setFillBelowLine(false);

        audioPeakRenderer.setColor(Color.GREEN);
        audioPeakRenderer.setPointStyle(PointStyle.CIRCLE);
        audioPeakRenderer.setFillBelowLine(false);
        audioPeakRenderer.setFillPoints(true);

        seriesRenderer.addSeriesRenderer(audioPeakRenderer);
        seriesRenderer.addSeriesRenderer(audioRenderer);

        // Setup the renderer
        seriesRenderer.setAxisTitleTextSize(12);
        seriesRenderer.setChartTitleTextSize(12);
        seriesRenderer.setLabelsTextSize(15);
        seriesRenderer.setLegendTextSize(15);
        seriesRenderer.setPointSize(15);
        seriesRenderer.setMargins(new int[] { 0, 0, 0, 0 });
        seriesRenderer.setApplyBackgroundColor(false);
//		seriesRenderer.setRange(new double[] { 0, 5000, 0, 100 });
        seriesRenderer.setShowLegend(false);
//        seriesRenderer.setShowAxes(false);
//        seriesRenderer.setShowGrid(true);
//        seriesRenderer.setShowLabels(false);
//		seriesRenderer.setXAxisMin(0.5);
//		seriesRenderer.setXAxisMax(10.5);
//        seriesRenderer.setYAxisMin(-7000);
//        seriesRenderer.setYAxisMax(7000);
        seriesRenderer.setZoomEnabled(false);
        // Make a view
        graphicalView = ChartFactory
                .getLineChartView(getActivity(), dataSet, seriesRenderer);
        graphContainer.addView(graphicalView);

        //Start the playback from the microphone if we are not in the replay mode
        if(!isReplaying)
            startPlayback();


        return v;
    }

    private void finishAndSave() {
        progressBar.setProgress(10 *10);
        recordingProgressTextView.setText(R.string.recording_finished);
        MicrophonePoller
                .unregisterListener(bloodPressureDataRecorder);
        stopPlayback();
        bloodPressureDataRecorder.completeAndStop();

        // Recording is finished, go to the next state POST_RECORDING
        smartAuscultationActivity.goToState(SmartAuscultationActivity.ActivityState.POST_RECORDING);
        isRecording =  false;
        recordButton.setText("Record");
    }

    @Override
    public void onPause() {
        super.onPause();
        if(isRecording)
            isCancled = true;

        audioTrack.pause();

        isRecording = false;
        progressBar.setProgress(0);
        recordingProgressTextView.setText(Messages
                .getString("BloodPressureActivity.finding_steth_instruction_text"));
    }

    @Override
    public void onResume() {
        super.onResume();

        // Reset the variables to initial values
        isCancled = false;
        isRecording = false;

        int minSize = AudioTrack.getMinBufferSize(SAMPLING_RATE,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        // configure an audio track
        audioTrack = new AudioTrack(
                AudioManager.STREAM_MUSIC, SAMPLING_RATE,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                minSize,
                AudioTrack.MODE_STREAM);
        Log.d(TAG, "created AudioTrack, minSize = "+minSize);

        // play the audio track
        audioTrack.play();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //release audio track resource
        Log.d(TAG, "onDestroy()");
        audioTrack.release();
        //Enter the stopped state as audio record fragment no more exists
        smartAuscultationActivity.goToState(SmartAuscultationActivity.ActivityState.STOPPED);
    }

    @Override
    public void inputAudio(final AudioData data) {
        //For playing the audio at the same time when it is being recorded.
        if(audioTrack != null && audioTrack.getState() != AudioTrack.STATE_UNINITIALIZED)
            audioTrack.write(data.getChunkData(), 0, data.getLength());

        if(isRecording || isReplaying)
            audioDataList.add(data);

        if(smartAuscultationActivity != null) {
            smartAuscultationActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    double timeStampModified = data.getTimestamp_ms();

//                    NewPeak peak;
//                    while ((peak = audioPeakQ.peek()) != null) {
//                        if (peak.getTimestampMillis() + CHART_LENGTH_MS < timeStampModified) {
//                            audioPeakQ.poll();
//                        } else {
//                            break;
//                        }
//                    }


                    short chunkData[] = data.getChunkData();
                    setHeightValueForGraph(chunkData);

                    // Begin drawing routine
                    seriesRenderer.setRange(new double[] { timeStampModified - CHART_LENGTH_MS,
                            timeStampModified, -heightValueForGraph, heightValueForGraph });
                    for (int i = 0; i < data.getLength(); i++) {
                        audioTimeSeries.add(timeStampModified, chunkData[i]);
                        timeStampModified = timeStampModified + data.getSingleShortLength_ms();
                    }
                    Iterator<NewPeak> iterator = audioPeakQ.iterator();
                    while (iterator.hasNext()) {
                        NewPeak p = iterator.next();
                        audioPeakSeries.add(p.getTimestampMillis(), p.getValue());
                    }
                    graphicalView.repaint();
                }
            });
        }
    }

    @Override
    public void onPeak(AudioPeak peak) {
        //audioPeakQ.add(peak);
    }

    private File getSaveFile() {
        String rootPath = Environment.getExternalStorageDirectory()
                .getAbsolutePath()
                + File.separator
                + getActivity().getString(R.string.app_name)
                + File.separator
                + Messages.getString("BloodPressureActivity.recordings_text"); //$NON-NLS-1$
        /**
         * Add the audio data to heart or lungs directory based on the selected module
         */
        if(smartAuscultationActivity.getSelectedModule() == ModuleListFragment.Modules.HEART_SOUND) {
            rootPath = rootPath + File.separator + "Heart";
        }
        else {
            rootPath = rootPath + File.separator + "Lungs";
        }
        File rootDir = new File(rootPath);
        if (!rootDir.exists())
            rootDir.mkdirs();
        String filePath = rootPath + File.separator
                + PreferenceDatabase.getCurrentlyLoadedUser().name + "-" //$NON-NLS-1$
                + smartAuscultationActivity.getSelectedAuscultationArea().getAreaCode() + "-"
                + getTimeString()
                + ".csv"; //$NON-NLS-1$
        File file = new File(filePath);

        recentFileName = filePath;

        return file;
    }

    private String getTimeString() {
        savedTimeString = NSLLocalizer.formatMessage(
                "BloodPressureActivity.time_string_format",
                Messages.RESOURCE_BUNDLE, new Date());
        return savedTimeString;
    }

    private void stopPlayback() {
        MicrophonePoller.unregisterListener(this);
//        MicrophonePoller.unregisterListener(audioPeakFinder);
//        audioPeakFinder.removeListener(this);
        isPlaying = false;
    }

    private void startPlayback() {
        //Reset the dataCount every time the playback starts
        // This is to avoid the crash due to IllegalArgumentException
        dataCount=0;
        // Clear the audio data list to record new session
        audioDataList.clear();
        MicrophonePoller.registerListener(this);
//        MicrophonePoller.registerListener(audioPeakFinder);
//        audioPeakFinder.registerListener(this);
        isPlaying = true;
    }

    @Override
    public void onPeak(NewPeak peak) {
        audioPeakQ.add(peak);
    }
}
