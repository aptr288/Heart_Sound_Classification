package unt.cse.nsl.smartauscultation.lung;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;

import unt.cse.nsl.smartauscultation.AuscultationArea;
import unt.cse.nsl.smartauscultation.R;
import unt.cse.nsl.smartauscultation.SmartAuscultationActivity;

/**
 * Created by Anurag Chitnis on 10/6/2016.
 */

public class LungAuscultationFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private AuscultationArea selectedArea = AuscultationArea.NONE;
    private Button nextButton;
    private ImageView lungAuscultationImage;
    private SmartAuscultationActivity smartAuscultationActivity;
    private RadioButton anteriorRadioButton;
    private RadioButton posteriorRadioButton;
    ArrayAdapter<CharSequence> adapter;

    public AuscultationArea getSelectedArea() {
        return selectedArea;
    }

    public void setSelectedArea(AuscultationArea selectedArea) {
        this.selectedArea = selectedArea;
    }

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
        View v = inflater.inflate(R.layout.fragment_lung_auscultation, container, false);
        nextButton = (Button) v.findViewById(R.id.lungAuscultationNextButton);
        lungAuscultationImage = (ImageView) v.findViewById(R.id.lungAuscultationImageView);
        anteriorRadioButton = (RadioButton) v.findViewById(R.id.radio_anterior);
        posteriorRadioButton = (RadioButton) v.findViewById(R.id.radio_posterior);
        anteriorRadioButton.setOnClickListener(this);
        posteriorRadioButton.setOnClickListener(this);

        Spinner spinner = (Spinner) v.findViewById(R.id.auscultationSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item);
//        adapter = ArrayAdapter.createFromResource(getActivity(),
//                R.array.lung_anterior, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smartAuscultationActivity.goToState(SmartAuscultationActivity.ActivityState.AUDIO_GRAPHING);
            }
        });

        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(anteriorRadioButton.isChecked()) {
            switch(position) {
                case 0:
                    selectedArea = AuscultationArea.ANT_TRACHEA;
                    break;
                case 1:
                    selectedArea = AuscultationArea.ANT_UPPER_RIGHT_LUNG_FIELD;
                    break;
                case 2:
                    selectedArea = AuscultationArea.ANT_UPPER_LEFT_LUNG_FIELD;
                    break;
                case 3:
                    selectedArea = AuscultationArea.ANT_MIDDLE_RIGHT_LUNG_FIELD;
                    break;
                case 4:
                    selectedArea = AuscultationArea.ANT_MIDDLE_LEFT_LUNG_FIELD;
                    break;
                case 5:
                    selectedArea = AuscultationArea.ANT_LOWER_RIGHT_LUNG_FIELD;
                    break;
                case 6:
                    selectedArea = AuscultationArea.ANT_LOWER_LEFT_LUNG_FIELD;
                    break;

                default:
                    selectedArea = AuscultationArea.NONE;
            }
        }
        else if(posteriorRadioButton.isChecked()) {
            switch(position) {
                case 0:
                    selectedArea = AuscultationArea.POS_UPPER_LEFT_LUNG_FIELD;
                    break;
                case 1:
                    selectedArea = AuscultationArea.POS_UPPER_RIGHT_LUNG_FIELD;
                    break;
                case 2:
                    selectedArea = AuscultationArea.POS_MIDDLE_RIGHT_LUNG_FIELD;
                    break;
                case 3:
                    selectedArea = AuscultationArea.POS_MIDDLE_LEFT_LUNG_FIELD;
                    break;
                case 4:
                    selectedArea = AuscultationArea.POS_LOWER_LEFT_LUNG_FIELD;
                    break;
                case 5:
                    selectedArea = AuscultationArea.POS_LOWER_RIGHT_LUNG_FIELD;
                    break;
                case 6:
                    selectedArea = AuscultationArea.POS_RIGHT_COSTOPHRENIC_ANGLE;
                    break;
                case 7:
                    selectedArea = AuscultationArea.POS_LEFT_COSTOPHRENIC_ANGLE;
                    break;

                default:
                    selectedArea = AuscultationArea.NONE;
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_anterior:
                if (checked) {
                    lungAuscultationImage.setImageResource(R.drawable.lung_auscultation_anterior);
                    adapter.clear();
                    adapter.addAll(getResources().getStringArray(R.array.lung_anterior));
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.radio_posterior:
                if (checked) {
                    lungAuscultationImage.setImageResource(R.drawable.lung_auscultation_posterior);
                    adapter.clear();
                    adapter.addAll(getResources().getStringArray(R.array.lung_posterior));
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }
}