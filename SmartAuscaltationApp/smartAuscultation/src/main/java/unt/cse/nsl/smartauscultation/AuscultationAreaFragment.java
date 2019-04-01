package unt.cse.nsl.smartauscultation;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by anurag on 1/12/16.
 */
public class AuscultationAreaFragment extends Fragment {

    //declaration of components of auscultation area fragment
    private ImageView aorticArea;
    private ImageView pulmonaryArea;
    private ImageView llsbArea;
    private ImageView mitralArea;

    private AuscultationArea selectedArea = AuscultationArea.NONE;

    public AuscultationArea getSelectedArea() {
        return selectedArea;
    }

    public void setSelectedArea(AuscultationArea selectedArea) {
        this.selectedArea = selectedArea;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.auscultation_area_fragment, container, false);



        //Resources of AuscultationAreaFragment

        aorticArea = (ImageView) v.findViewById(R.id.aorticArea);
        pulmonaryArea = (ImageView) v.findViewById(R.id.pulmonaryArea);
        llsbArea = (ImageView) v.findViewById(R.id.LLSBArea);
        mitralArea = (ImageView) v.findViewById(R.id.mitralArea);

        aorticArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetImageResources();
                aorticArea.setImageResource(R.drawable.red_selection_point);
                AuscultationAreaFragment.this.selectedArea = AuscultationArea.AORTIC;
            }
        });

        pulmonaryArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetImageResources();
                pulmonaryArea.setImageResource(R.drawable.red_selection_point);
                AuscultationAreaFragment.this.selectedArea = AuscultationArea.PULMONARY;
            }
        });

        llsbArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetImageResources();
                llsbArea.setImageResource(R.drawable.red_selection_point);
                AuscultationAreaFragment.this.selectedArea = AuscultationArea.LLSB;
            }
        });

        mitralArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetImageResources();
                mitralArea.setImageResource(R.drawable.red_selection_point);
                AuscultationAreaFragment.this.selectedArea = AuscultationArea.MITRAL;
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
