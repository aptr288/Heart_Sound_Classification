package unt.cse.nsl.smartauscultation;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import unt.cse.nsl.smartauscultation.lung.LungSoundClassification;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Anurag Chitnis on 12/9/2016.
 */


@RunWith(AndroidJUnit4.class)
public class LungSoundClassificationTest {

    private LungSoundClassification lungSoundClassification;

    @Before
    public void setUp() {
        lungSoundClassification = LungSoundClassification.getInstance(InstrumentationRegistry.getTargetContext());
        lungSoundClassification.readHMMFull();
    }

    @Test
    public void wheezingTestCase_1() {
        int dataPoints;
        double[] lungSound;
        String line;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("test_wheezing_1.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] numbersArray = line.split("; ");
                for(String num : numbersArray)
                    signal.add(Double.parseDouble(num));

                dataPoints = signal.size();
                lungSound = new double[dataPoints];
                for (int j = 0; j < dataPoints; j++) {
                    lungSound[j] = signal.get(j);
                }
                lungSoundClassification.setAuscultationArea(AuscultationArea.ANT_TRACHEA);

                String result = lungSoundClassification.getClassificationResult(lungSound);
                String classificationIndex = lungSoundClassification.getIndexClassification();
                assertEquals("Specific classification: " + result, "lung_wheezing", classificationIndex);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
