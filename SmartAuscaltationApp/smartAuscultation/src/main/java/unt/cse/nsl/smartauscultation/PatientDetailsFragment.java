package unt.cse.nsl.smartauscultation;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Anurag Chitnis on 10/10/2016.
 */

public class PatientDetailsFragment extends Fragment {

    /**
     * TAG for logging purpose
     */
    private static final String TAG = PatientDetailsFragment.class.getSimpleName();

    private SmartAuscultationActivity smartAuscultationActivity;
    EditText physicianNameText;
    EditText patientNameText;
    EditText patientAgeText;
    EditText notesInputText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getActivity() instanceof SmartAuscultationActivity) {
            smartAuscultationActivity = (SmartAuscultationActivity) getActivity();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_patient_details, container, false);
        physicianNameText = (EditText) v.findViewById(R.id.physicianName);
        patientNameText = (EditText) v.findViewById(R.id.patientName);
        patientAgeText = (EditText) v.findViewById(R.id.patientAge);
        notesInputText = (EditText) v.findViewById(R.id.notesInput);

        Button submitButton = (Button) v.findViewById(R.id.btnSubmit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //write all the entered patient details to file
                writePatientDetailsToFile();
                Log.d(TAG, "submitButton call trace");
                smartAuscultationActivity.goToState(SmartAuscultationActivity.ActivityState.STOPPED);
                smartAuscultationActivity.goToState(SmartAuscultationActivity.ActivityState.MODULE_SELECTION);
            }
        });

        return v;
    }

    private File getLogFile() {
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
                + "Patient" + "." + "-" + smartAuscultationActivity.getSavedTimeString() + ".csv"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        return new File(filePath);
    }

    private void writePatientDetailsToFile() {
        StringBuilder patientString = new StringBuilder();
        patientString.append(physicianNameText.getText().toString());
        patientString.append(", "+ patientNameText.getText().toString());
        patientString.append(", "+ patientAgeText.getText().toString());
        patientString.append(", "+ notesInputText.getText().toString());

        BufferedWriter bw = null;
        try {
            // Open the existing file for appending the data
            bw = new BufferedWriter(new FileWriter(getLogFile(), true));
            bw.write(patientString.toString());
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }finally {
            try {
                if(bw!=null)
                    bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
