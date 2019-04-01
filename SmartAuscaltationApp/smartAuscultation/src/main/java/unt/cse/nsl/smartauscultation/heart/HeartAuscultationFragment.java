package unt.cse.nsl.smartauscultation.heart;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import unt.cse.nsl.smartauscultation.AuscultationArea;
import unt.cse.nsl.smartauscultation.R;
import unt.cse.nsl.smartauscultation.SmartAuscultationActivity;

/**
 * Created by Anurag Chitnis on 10/7/2016.
 */

public class HeartAuscultationFragment extends Fragment {

    //declaration of components of auscultation area fragment
    private ImageView aorticArea;
    private ImageView pulmonaryArea;
    private ImageView llsbArea;
    private ImageView mitralArea;
    private Button nextButton;

    private AuscultationArea selectedArea = AuscultationArea.NONE;
    private SmartAuscultationActivity smartAuscultationActivity;

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
        View v = inflater.inflate(R.layout.fragment_heart_auscultation, container, false);



        //Resources of AuscultationAreaFragment

        aorticArea = (ImageView) v.findViewById(R.id.aorticArea);
        pulmonaryArea = (ImageView) v.findViewById(R.id.pulmonaryArea);
        llsbArea = (ImageView) v.findViewById(R.id.LLSBArea);
        mitralArea = (ImageView) v.findViewById(R.id.mitralArea);
        nextButton = (Button) v.findViewById(R.id.heartAuscultationNextButton);

        aorticArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetImageResources();
                aorticArea.setImageResource(R.drawable.red_selection_point);
                HeartAuscultationFragment.this.selectedArea = AuscultationArea.AORTIC;
            }
        });

        pulmonaryArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetImageResources();
                pulmonaryArea.setImageResource(R.drawable.red_selection_point);
                HeartAuscultationFragment.this.selectedArea = AuscultationArea.PULMONARY;
            }
        });

        llsbArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetImageResources();
                llsbArea.setImageResource(R.drawable.red_selection_point);
                HeartAuscultationFragment.this.selectedArea = AuscultationArea.LLSB;
            }
        });

        mitralArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetImageResources();
                mitralArea.setImageResource(R.drawable.red_selection_point);
                HeartAuscultationFragment.this.selectedArea = AuscultationArea.MITRAL;
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smartAuscultationActivity.goToState(SmartAuscultationActivity.ActivityState.AUDIO_GRAPHING);
            }
        });

        return v;

    }

    private void resetImageResources() {
        aorticArea.setImageResource(R.drawable.black_selection_point);
        pulmonaryArea.setImageResource(R.drawable.black_selection_point);
        llsbArea.setImageResource(R.drawable.black_selection_point);
        mitralArea.setImageResource(R.drawable.black_selection_point);
    }
}
