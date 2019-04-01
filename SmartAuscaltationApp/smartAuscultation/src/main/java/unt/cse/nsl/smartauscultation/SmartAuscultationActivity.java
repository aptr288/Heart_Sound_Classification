package unt.cse.nsl.smartauscultation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.unt.cse.nsl.audio.AudioData;
import org.unt.cse.nsl.audio.MicrophonePoller;
import org.unt.cse.nsl.bloodpressure.BloodPressureDataRecorder;
import org.unt.cse.nsl.bloodpressure.BloodPressureDataReplayer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import unt.cse.nsl.smartauscultation.heart.AudioPeakFinder;
import unt.cse.nsl.smartauscultation.heart.FinalResultFragment;
import unt.cse.nsl.smartauscultation.heart.HeartAuscultationFragment;
import unt.cse.nsl.smartauscultation.heart.HeartSoundClassification;
import unt.cse.nsl.smartauscultation.heart.HeartSplitSound;
import unt.cse.nsl.smartauscultation.heart.LubDubSignal;
import unt.cse.nsl.smartauscultation.heart.NewAudioPeakListener;
import unt.cse.nsl.smartauscultation.heart.NewPeak;
import unt.cse.nsl.smartauscultation.lung.LungAuscultationFragment;
import unt.cse.nsl.smartauscultation.lung.LungResultFragment;
import unt.cse.nsl.smartauscultation.lung.LungSoundClassification;

import static unt.cse.nsl.smartauscultation.R.id.mainFragmentContainer;
import static unt.cse.nsl.smartauscultation.SmartAuscultationActivity.ActivityState.AUDIO_GRAPHING;
import static unt.cse.nsl.smartauscultation.SmartAuscultationActivity.ActivityState.MODULE_SELECTION;
import static unt.cse.nsl.smartauscultation.SmartAuscultationActivity.ActivityState.POST_RECORDING;

/**
 * Created by Anurag Chitnis on 9/28/2016.
 */
public class SmartAuscultationActivity extends Activity implements NewAudioPeakListener {

    /**
     * The number of audio peaks to track when trying to decide whether a signal
     * is good. This is just two times pulse due to the fact that there should
     * be two audio peaks (lub, dub) for each video peak.
     */
    private static final int AUDIO_PEAKS_TO_TRACK = 30;
    /**
     * Build flag for whether a replay option is included in the menu when
     * stopped.
     */
    private static final boolean ENABLE_REPLAY = true;
    /**
     * Build flag for whether there is a graph output while the application is
     * taking measurements.
     */
    private static final boolean ENABLE_GRAPHING = true;
    /**
     * Build flag for whether the session is recorded and saved for replay
     * later. This may significantly slow down the application on slower single
     * core phones.
     */
    private static final boolean ENABLE_SESSION_RECORD = true;

    @Override
    public void onPeak(NewPeak peak) {
        switch (currentState) {
            case MODULE_SELECTION:
            case PATIENT_DETAILS:
            case AUSCULTATION_AREA:
                break;

            case AUDIO_GRAPHING:
            case RECORDING:
                audioFindingPeaks.add(peak);
                Log.i("Peak Data", "RUNNING: " + peak.getTimestampMillis() + " " + peak.getValue());
                if (audioFindingPeaks.size() > AUDIO_PEAKS_TO_TRACK)
                    audioFindingPeaks.remove();
                break;

        }

    }


    /**
     * The states that the activity may be in.
     */
    public enum ActivityState {
        MODULE_SELECTION, AUSCULTATION_AREA, AUDIO_GRAPHING, RECORDING, POST_RECORDING, PATIENT_DETAILS,STOPPED
    }

    Context context;

    //variable to gather data from each fragment
    private ModuleListFragment.Modules selectedModule;
    private AuscultationArea selectedAuscultationArea;

    public ModuleListFragment.Modules getSelectedModule() {
        return selectedModule;
    }

    public AuscultationArea getSelectedAuscultationArea() {
        return selectedAuscultationArea;
    }

    private HeartAuscultationFragment heartAuscultationFragment;
    private LungAuscultationFragment lungAuscultationAreaFragment;
    private ModuleListFragment moduleListFragment;
    private AudioRecordFragment audioRecordFragment;
    private PatientDetailsFragment patientDetailsFragment;
    private FinalResultFragment finalResultFragment;
    private LungResultFragment lungResultFragment;

//    private AudioToFFTTransformer audioToFFftTransformer = null;
//    private FFTSumAndSplit fftSumAndSplit = null;
//    private FFTSumAveragePeakFinder fftSumAveragePeakFinder = null;
    private AudioPeakFinder audioPeakFinder;
//    private HeartSoundClassification heartSoundClassification;
    private LungSoundClassification lungSoundClassification;

    // BEGIN PREFERENCES
//    private boolean prefs_continuous;
//    private int prefs_lowerFreq;
//    private int prefs_upperFreq;
    // END PREFERENCES


    public ArrayList<LubDubSignal> getLubDubSignalArrayList() {
        return lubDubSignalArrayList;
    }

    private ArrayList<LubDubSignal> lubDubSignalArrayList;

    /**
     * Stores data from the current session to a log file for analysis and
     * replay.
     */
    private BloodPressureDataRecorder bloodPressureDataRecorder;
    /** Replays data from a log file. */
    private BloodPressureDataReplayer bloodPressureDataReplayer;
    /** Whether we are running in replay mode at the moment. */
    private boolean replayMode = false;
    /** The file that we will replay from if in replay mode */
    private File replayFile = null;
    /** File path of the recorded heart sound*/
    private String heartSoundFilePath = null;
    /** Holds the peak of the first S1*/
    private Double firstS1;
    /** Holds the peak of the second S1*/
    private Double secondS1;
    /** Holds the peak of the first S2*/
    private Double firstS2;

    /**
     * The audio peaks used when determining the audio signal strength.
     */
    private LinkedList<NewPeak> audioFindingPeaks = new LinkedList<>();
    private ActivityState currentState;

    public LinkedList<NewPeak> getAudioFindingPeaks() {
        return audioFindingPeaks;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_smart_auscultation);

        // Create new fragment for this activity


        moduleListFragment = new ModuleListFragment();
        audioRecordFragment = new AudioRecordFragment();
        finalResultFragment = new FinalResultFragment();
        lungResultFragment = new LungResultFragment();
        patientDetailsFragment = new PatientDetailsFragment();
        this.enterState(MODULE_SELECTION);
        //heart_sound_button.setEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get our data from the preferences.
        //updatePreferences();

        //this.enterState(ActivityState.MODULE_SELECTION);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Free any used resources
//        releaseResources();

        // Save our preferences to the database
        PreferenceDatabase.open(this);
        PreferenceDatabase.savePreferencesForUser(this,
                PreferenceDatabase.getCurrentlyLoadedUser());
        PreferenceDatabase.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_smart_auscultation, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menu_replay = menu.findItem(R.id.menu_replay);
//        if (ENABLE_REPLAY
//                && (myState == BloodPressureActivity.bpState.STOPPED || myState == BloodPressureActivity.bpState.COMPLETED)) {
//            menu_replay.setVisible(true);
//        } else {
//            menu_replay.setVisible(false);
//        }
        return super.onPrepareOptionsMenu(menu);
    }


    public void goToState(ActivityState goToState) {

        enterState(goToState);

    }

    public List<AudioData> getAudioDataList() {
        return audioRecordFragment.getAudioDataList();
    }

    public String getRecentFileName() {
        //Fetch the name of the recently recorded file
        return audioRecordFragment.getRecentFileName();
    }

    private void enterState(ActivityState activityState) {
        Log.d("SmartAuscultation", "Entering state : " + activityState.toString());
        switch(activityState) {
            case MODULE_SELECTION:
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getFragmentManager()
                        .beginTransaction()
                        .replace(mainFragmentContainer, moduleListFragment)
                        .commit();
                break;

            case AUSCULTATION_AREA:
                selectedModule = moduleListFragment.getSelectedModule();
                releaseResources();

                if(selectedModule == ModuleListFragment.Modules.HEART_SOUND) {
                    //Read the files needed for split detection and murmur classification
                    //heartSoundClassification = HeartSoundClassification.getInstance(this);
                    HeartSplitSound.readS2Files(context);
                    HeartSoundClassification.readHMMFull(context);
                    //heartSoundClassification.readS2Files();

                    heartAuscultationFragment = new HeartAuscultationFragment();
                    getFragmentManager()
                            .beginTransaction()
                            .replace(mainFragmentContainer, heartAuscultationFragment)
                            .addToBackStack("module")
                            .commit();
                }
                else {

                    lungSoundClassification = LungSoundClassification.getInstance(this);
                    //Read the files needed for lung sound classification
                    lungSoundClassification.readHMMFull();
                    lungAuscultationAreaFragment = new LungAuscultationFragment();
                    getFragmentManager()
                            .beginTransaction()
                            .replace(mainFragmentContainer, lungAuscultationAreaFragment)
                            .addToBackStack("module")
                            .commit();
                }
                break;

            case AUDIO_GRAPHING:
                if(selectedModule == ModuleListFragment.Modules.HEART_SOUND) {
                    if(!replayMode)
                        selectedAuscultationArea = heartAuscultationFragment.getSelectedArea();
                    else
                        selectedAuscultationArea = readAuscultationArea();

                    // Set the selected Asuscultation area for heart sound classification
                    HeartSoundClassification.setAuscultationArea(selectedAuscultationArea);
                }
                else {
                    selectedAuscultationArea = lungAuscultationAreaFragment.getSelectedArea();
                    LungSoundClassification.getInstance(this).setAuscultationArea(selectedAuscultationArea);
                }

                //send the replay mode status to the audio record fragment as an argument
                Bundle args = new Bundle();
                args.putBoolean("isReplaying",replayMode);
                args.putString("selectedModule", selectedModule.toString());
                audioRecordFragment.setArguments(args);

                getFragmentManager()
                        .beginTransaction()
                        .replace(mainFragmentContainer, audioRecordFragment)
                        .addToBackStack("auscultation")
                        .commit();
                //initialize the audio peak finder
                audioPeakFinder = new AudioPeakFinder();
                if (!replayMode) {
                    MicrophonePoller.start(Settings.MIC_RATE_Hz,
                            Settings.MIC_BUFFER_SIZE);
                } else {
                    bloodPressureDataReplayer = new BloodPressureDataReplayer();
                    // Register for FFT transform only if the heart sound module is selected
                    if(selectedModule == ModuleListFragment.Modules.HEART_SOUND) {
//                        audioToFFftTransformer = new AudioToFFTTransformer(128, 128);
                        bloodPressureDataReplayer
                                .registerAudioInput(audioPeakFinder);
                    }
                    bloodPressureDataReplayer
                            .registerAudioInput(audioRecordFragment);
                    bloodPressureDataReplayer.start(replayFile, new Runnable() {
                        @Override
                        public void run() {
                            releaseResources();
                            enterState(POST_RECORDING);
                            replayMode = false;
                        }
                    }, this);
                }

                //audioFindingPeaks.clear();
                // We need the microphone to start up
                if(selectedModule == ModuleListFragment.Modules.HEART_SOUND) {
//                    fftSumAndSplit = new FFTSumAndSplit(prefs_lowerFreq,
//                            prefs_upperFreq);
//                    fftSumAveragePeakFinder = new FFTSumAveragePeakFinder(
//                            Settings.FFTPF_SMOOTH_LENGTH, Settings.FFTPF_SHIFT,
//                            Settings.FFTPF_MIN_PEAK_DIFF_ms);

                    if (!replayMode) {
//                        audioToFFftTransformer = new AudioToFFTTransformer(
//                                Settings.A2FFT_LENGTH, Settings.A2FFT_INC_SIZE);
                        //MicrophonePoller.registerListener(audioToFFftTransformer);
                        MicrophonePoller.registerListener(audioPeakFinder);
                    }
                    //audioToFFftTransformer.registerListener(fftSumAndSplit);
                    //fftSumAndSplit.registerListener(fftSumAveragePeakFinder);
                    audioPeakFinder.registerListener(this);
                    audioPeakFinder.registerListener(audioRecordFragment);
                }
                break;

            case RECORDING:
                audioFindingPeaks.clear();
                break;

            case POST_RECORDING:
                //Release all the resources as we don't need them anymore
                releaseResources();

                if(selectedModule == ModuleListFragment.Modules.HEART_SOUND) {

                    if(choseAudioPeaks()) {

                        getFragmentManager()
                                .beginTransaction()
                                .replace(mainFragmentContainer, finalResultFragment)
                                .addToBackStack("graphing")
                                .commit();
                    }
                    else {
                        //TODO: Handle the Error message
                    }
                }
                else {
                    Bundle argsFilePath = new Bundle();
                    argsFilePath.putString("FilePath", getRecentFileName());

                    lungResultFragment.setArguments(argsFilePath);
                    getFragmentManager()
                            .beginTransaction()
                            .replace(mainFragmentContainer, lungResultFragment)
                            .addToBackStack(null)
                            .commit();
                }

// TODO: This code can be used to introduce the new fragment for entering patient details, like name and diagnosis.
//                getFragmentManager()
//                        .beginTransaction()
//                        .replace(mainFragmentContainer, patientDetailsFragment)
//                        .commit();
                break;

            case PATIENT_DETAILS:

                getFragmentManager()
                        .beginTransaction()
                        .replace(mainFragmentContainer, patientDetailsFragment)
                        .addToBackStack(null)
                        .commit();

                break;

            case STOPPED:
                releaseResources();

                break;
        }

        currentState = activityState;

    }

    /**
     * This module chooses the appropriate peaks from the recording and
     */
    private boolean choseAudioPeaks() {

        lubDubSignalArrayList = new ArrayList<>();
        /**
         * New Peak selection algorithm
         */
        Double previousPeakTs = 0.0;
        List<Double> diffList = new ArrayList<>();
        for(NewPeak peak : audioFindingPeaks) {
            double tsDifference = peak.getTimestampMillis() - previousPeakTs;
            diffList.add(tsDifference);
            previousPeakTs = peak.getTimestampMillis();
        }
        Double previousItem = 0.0;
        int i = 0;
        for (Double item : diffList) {
            //Log.i("Time difference","previousItem: "+previousItem + " item = "+item);
            if(previousItem>250 && previousItem < 350 && item > 300 && item < 800) {
                //create the new instance of lub-dub signal
                LubDubSignal lubDubSignal = new LubDubSignal();
                lubDubSignal.setFirstS1Timestamp(audioFindingPeaks.get(i-2).getTimestampMillis());
                lubDubSignal.setFirstS2Timestamp(audioFindingPeaks.get(i-1).getTimestampMillis());
                lubDubSignal.setSecondS1Timestamp(audioFindingPeaks.get(i).getTimestampMillis());

                //Add the recently created lubdub signal to the array list
                lubDubSignalArrayList.add(lubDubSignal);
            }
            previousItem = item;
            i++;
        }

        //TODO: Check for the detected peaks and display a pop up message if we could not find the peak.

        Log.i("SmartAuscultaiton", "No. of Lub-dubs: "+String.valueOf(lubDubSignalArrayList.size()));
        //If we could not find two or more peaks from the recording we return false
        if(lubDubSignalArrayList.size() < 2)
            return false;

        Bundle args = new Bundle();
        args.putString("FilePath", getRecentFileName());
        args.putString("AuscultationArea", selectedAuscultationArea.toString());

        finalResultFragment.setArguments(args);

        StringBuilder stringToWrite = new StringBuilder();

        try {
            //write the last 12 peaks to the log file
            BufferedWriter bw = new BufferedWriter(new FileWriter(getLogFile()));
            for (NewPeak ap : audioFindingPeaks) {
//                bw.append(","+ap.getTimestampMillis() + "\n");
                stringToWrite.append("Peak timestamp,"+ap.getTimestampMillis() + "\n");
            }
            stringToWrite.append("AuscultationArea - "+selectedAuscultationArea.toString()+"," + selectedAuscultationArea.getAreaCode() + "\n");
            bw.write(stringToWrite.toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * This function releases the resources being used in any particular state
     * in proper order. If you have a memory leak, the problem is likely here.
     */
    private void releaseResources() {
        logi(this.getClass(), "Releasing resources from state " + currentState); //$NON-NLS-1$
        switch (currentState) {
            case AUDIO_GRAPHING:
            case RECORDING:
            case POST_RECORDING:
            case PATIENT_DETAILS:
            case STOPPED:

//                if(null != audioToFFftTransformer) {
//                    audioToFFftTransformer.unregisterListener(fftSumAndSplit);
//                }
//                if(null != fftSumAndSplit) {
//                    fftSumAndSplit.unregisterListener(fftSumAveragePeakFinder);
//                    fftSumAndSplit = null;
//                }
//                if(null != fftSumAveragePeakFinder) {
//                    fftSumAveragePeakFinder.unregisterListener(this);
//                    fftSumAveragePeakFinder = null;
//                }
                if (!replayMode) {
                    //MicrophonePoller.unregisterListener(audioToFFftTransformer);
                    MicrophonePoller.unregisterListener(audioPeakFinder);
                    MicrophonePoller.stop();
//                    audioToFFftTransformer = null;
                }
                if(null != audioPeakFinder) {
                    audioPeakFinder.removeListener(this);
                    audioPeakFinder = null;
                }

                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("SmartAuscultation","onBackPressed() : "+currentState);
//        releaseResources();
//        switch(currentState) {
//            case POST_RECORDING:
//                getFragmentManager().popBackStack("auscultation",0);
//                break;
//        }
    }

//    @Override
//    public void onPeak(AudioPeak peak) {
//        RunningStdDev evenStdDev = new RunningStdDev(false);
//        RunningStdDev oddStdDev = new RunningStdDev(false);
//
//        switch (currentState) {
//            case MODULE_SELECTION:
//            case PATIENT_DETAILS:
//            case AUSCULTATION_AREA:
//                break;
//
//            case AUDIO_GRAPHING:
//            case RECORDING:
//                audioFindingPeaks.add(peak);
//                Log.i("Peak Data", "RUNNING: " + peak.getTimestampMillis() + " " + peak.getValue());
//                if (audioFindingPeaks.size() > AUDIO_PEAKS_TO_TRACK)
//                    audioFindingPeaks.remove();
//                break;
//
//    }
//
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:

                startActivity(new Intent(this, Preferences.class));
                return true;

            case R.id.menu_replay:
                runReplaySelect();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Fill the local variables with data from the shared preferences.
     */
//    private void updatePreferences() {
//        SharedPreferences prefs = PreferenceManager
//                .getDefaultSharedPreferences(this);
//
//        // From menu
//        prefs_continuous = prefs.getBoolean(
//                PreferenceDatabase.SETTING_NAME_CONTINUOUS, false);
//        logi(this.getClass(),
//                NSLLocalizer
//                        .formatMessage(
//                                "BloodPressureActivity.set_cont", Messages.LOG_BUNDLE, prefs_continuous)); //$NON-NLS-1$
//
//        // From scan
//        prefs_lowerFreq = prefs.getInt(
//                PreferenceDatabase.SETTING_NAME_LOWERFREQ,
//                Settings.DEF_LOWER_FREQ);
//        logi(this.getClass(),
//                NSLLocalizer
//                        .formatMessage(
//                                "BloodPressureActivity.set_lower_freq", Messages.LOG_BUNDLE, prefs_lowerFreq)); //$NON-NLS-1$
//        prefs_upperFreq = prefs.getInt(
//                PreferenceDatabase.SETTING_NAME_UPPERFREQ,
//                Settings.DEF_UPPER_FREQ);
//        logi(this.getClass(),
//                NSLLocalizer
//                        .formatMessage(
//                                "BloodPressureActivity.set_upper_freq", Messages.LOG_BUNDLE, prefs_upperFreq)); //$NON-NLS-1$
//    }

    /**
     * Create a dialog for the user to input data for the calibration of the
     * application
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

//    private static String getTimeString() {
//        return NSLLocalizer.formatMessage(
//                "BloodPressureActivity.time_string_format",
//                Messages.RESOURCE_BUNDLE, new Date());
//    }
//
    public static void logi(@SuppressWarnings("rawtypes") Class source,
                            String message) {
        Log.i(source.getSimpleName()
                + "." //$NON-NLS-1$
                + Thread.currentThread().getStackTrace()[3].getMethodName()
                + "()", message); //$NON-NLS-1$
    }
//
//    private File getSaveFile() {
//        String rootPath = Environment.getExternalStorageDirectory()
//                .getAbsolutePath()
//                + File.separator
//                + context.getString(R.string.app_name)
//                + File.separator
//                + Messages.getString("BloodPressureActivity.recordings_text"); //$NON-NLS-1$
//        File rootDir = new File(rootPath);
//        if (!rootDir.exists())
//            rootDir.mkdirs();
//        String filePath = rootPath + File.separator
//                + PreferenceDatabase.getCurrentlyLoadedUser().name + "-" //$NON-NLS-1$
//                + getTimeString() + ".csv"; //$NON-NLS-1$
//        File file = new File(filePath);
//
//        this.heartSoundFilePath = filePath;
//
//        return file;
//    }

    public String getSavedTimeString() {
        return audioRecordFragment.getSavedTimeString();
    }

    public File getLogFile() {
        String rootPath = Environment.getExternalStorageDirectory()
                .getAbsolutePath()
                + File.separator
                + getString(R.string.app_name)
                + File.separator
                + Messages.getString("BloodPressureActivity.log_text"); //$NON-NLS-1$
        File rootDir = new File(rootPath);
        if (!rootDir.exists())
            rootDir.mkdirs();

        String filePath = rootPath + File.separator
                + PreferenceDatabase.getCurrentlyLoadedUser().name + "." //$NON-NLS-1$
                + "Log" + "." + "-" + getSavedTimeString() + ".csv"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        return new File(filePath);
    }

    private void runReplaySelect() {
        // Begin building the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Messages
                .getString("BloodPressureActivity.replay_select_title")); //$NON-NLS-1$
        builder.setCancelable(true);

        // Get all of the available files
        String rootPath = Environment.getExternalStorageDirectory()
                .getAbsolutePath()
                + File.separator
                + getString(R.string.app_name)
                + File.separator
                + Messages.getString("BloodPressureActivity.recordings_text"); //$NON-NLS-1$
        /**
         * Show the Heart or lung sound directory based on the selected module
         */
        if(selectedModule == ModuleListFragment.Modules.HEART_SOUND) {
            rootPath = rootPath + File.separator + "Heart";
        }
        else {
            rootPath = rootPath + File.separator + "Lungs";
        }

        File rootDir = new File(rootPath);
        final File[] files = rootDir.listFiles();
        if (files != null) {
            final String[] options = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                options[i] = files[i].getName();
            }

            // Fill the dialog with the file options
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Do the replay thing
                    replayWithFile(files[which]);
                }
            });
        }
        builder.setNegativeButton(
                Messages.getString("BloodPressureActivity.cancel_option"), //$NON-NLS-1$
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Nothing
                    }
                });

        // Create and start the dialog
        Dialog dialog = builder.create();
        dialog.show();
    }

    private void replayWithFile(File f) {
        replayMode = true;
        replayFile = f;
        enterState(AUDIO_GRAPHING);
    }

    private AuscultationArea readAuscultationArea() {

        String fileName = replayFile.getName();
        String[] splitString = fileName.split("-");
        int areaCode = Integer.parseInt(splitString[1]);
        return AuscultationArea.getHeartAuscultationArea(areaCode);
    }
}
