package unt.cse.nsl.smartauscultation;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

/**
 * Created by Anurag Chitnis on 9/28/2016.
 */
public class ModuleListFragment extends ListFragment implements AdapterView.OnItemClickListener {

    public enum Modules {
        LUNG_SOUND, HEART_SOUND
    }

    private Modules selectedModule;
    private SmartAuscultationActivity smartAuscultationActivity;

    public Modules getSelectedModule() {
        return selectedModule;
    }

    public void setSelectedModule(Modules selectedModule) {
        this.selectedModule = selectedModule;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getActivity() instanceof SmartAuscultationActivity) {
            smartAuscultationActivity = (SmartAuscultationActivity) getActivity();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_module_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Modules, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        //Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
        if (position == 0)
            selectedModule = Modules.HEART_SOUND;
        else
            selectedModule = Modules.LUNG_SOUND;

        smartAuscultationActivity.goToState(SmartAuscultationActivity.ActivityState.AUSCULTATION_AREA);
    }
}
