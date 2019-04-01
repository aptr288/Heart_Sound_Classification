package unt.cse.nsl.smartauscultation;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import unt.cse.nsl.smartauscultation.heart.HeartSplitSound;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Anurag Chitnis on 11/7/2016.
 */
@RunWith(AndroidJUnit4.class)
public class HeartSplitSoundTest {

    private HeartSplitSound heartSplitSound;

    @Before
    public void setUp() {
        HeartSplitSound.readS2Files(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void testSplit() {
        float s1SplitTime, s2SplitTime;
        int s2Index = 4000;
        boolean S1split = false;
        boolean S2split = false;

        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("splitS2_ori.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] numbersArray = line.split("; ");
                for(String num : numbersArray)
                    signal.add(Double.parseDouble(num));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }

            heartSplitSound = new HeartSplitSound(heartSound, s2Index);
            heartSplitSound.calculateSplit();
            s1SplitTime = heartSplitSound.getS1SplitTime();
            s2SplitTime = heartSplitSound.getS2SplitTime();

            if(s1SplitTime < 40.0 && s2SplitTime < 40.0) {
                //text = "S1 and S2 split time < 40 ms";
                S1split = false;
                S2split = false;
            }
            else if (s1SplitTime > 40.0 && s2SplitTime < 40.0) {
                S1split = true;
                // text = "S1 split time > 40 ms, " + "\n" + "You have a split in first heart sound";
            }
            else if (s1SplitTime < 40.0 && s2SplitTime > 40.0) {
                S2split = true;
                //text = "S2 Split time > 40 ms, " + "\n" + "You have a split in second heart sound";
            }
            else {
                S1split = true;
                S2split = true;
                // text = "S1 and S2 split time > 40 ms, " + "\n" + "You have splits in both the heart sounds";
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
        assertThat("Split not found in the s2 signal",S2split);
    }

    @Test
    public void testSplitS2_valsalva_1() {
        float s1SplitTime, s2SplitTime;
        int s2Index = 3000;
        boolean S1split = false;
        boolean S2split = false;

        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("splitS2_valsalva_1.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] numbersArray = line.split(";");
                for(String num : numbersArray)
                    signal.add(Double.parseDouble(num));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }

            heartSplitSound = new HeartSplitSound(heartSound, s2Index);
            heartSplitSound.calculateSplit();
            s1SplitTime = heartSplitSound.getS1SplitTime();
            s2SplitTime = heartSplitSound.getS2SplitTime();

            if(s1SplitTime < 40.0 && s2SplitTime < 40.0) {
                //text = "S1 and S2 split time < 40 ms";
                S1split = false;
                S2split = false;
            }
            else if (s1SplitTime > 40.0 && s2SplitTime < 40.0) {
                S1split = true;
                // text = "S1 split time > 40 ms, " + "\n" + "You have a split in first heart sound";
            }
            else if (s1SplitTime < 40.0 && s2SplitTime > 40.0) {
                S2split = true;
                //text = "S2 Split time > 40 ms, " + "\n" + "You have a split in second heart sound";
            }
            else {
                S1split = true;
                S2split = true;
                // text = "S1 and S2 split time > 40 ms, " + "\n" + "You have splits in both the heart sounds";
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
        assertThat("Split not found in the s2 signal",S2split);
    }

    @Test
    public void splitS2TestCase_1() {
        float s1SplitTime, s2SplitTime;
        int s2Index = 1800;
        boolean S1split = false;
        boolean S2split = false;

        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("splits2_2.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                    signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }

            heartSplitSound = new HeartSplitSound(heartSound, s2Index);
            heartSplitSound.calculateSplit();
            s1SplitTime = heartSplitSound.getS1SplitTime();
            s2SplitTime = heartSplitSound.getS2SplitTime();

            if(s1SplitTime < 40.0 && s2SplitTime < 40.0) {
                //text = "S1 and S2 split time < 40 ms";
                S1split = false;
                S2split = false;
            }
            else if (s1SplitTime > 40.0 && s2SplitTime < 40.0) {
                S1split = true;
                // text = "S1 split time > 40 ms, " + "\n" + "You have a split in first heart sound";
            }
            else if (s1SplitTime < 40.0 && s2SplitTime > 40.0) {
                S2split = true;
                //text = "S2 Split time > 40 ms, " + "\n" + "You have a split in second heart sound";
            }
            else {
                S1split = true;
                S2split = true;
                // text = "S1 and S2 split time > 40 ms, " + "\n" + "You have splits in both the heart sounds";
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
        assertThat("Split not found in the s2 signal",S2split);
    }
}
