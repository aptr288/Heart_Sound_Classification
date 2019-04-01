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

import unt.cse.nsl.smartauscultation.heart.HeartSoundClassification;

import static junit.framework.Assert.assertEquals;

/**
 *  This class is for testing the HeartSoundClassification class.
 *  We are validating our murmur classification algorithm by running the various types of signals through it.
 *  Few test cases may fail from this class, as we don't have the accuracy of 100% in classification.<br>
 *  We have taken these test cases from Shanti's Heart Sound PC project<br>
 *      This data is taken from Dr. Sarma's recordings<br>
 *  area[] = {2,2,2,4,4,4,1,1,1,2,2,1,1,1,1,1,1,4,4,4,4,4,4,4,2,2,2,1,1,1,1,1,1,4,1,3,3,3,1,1,1,4};
 * Created by Anurag Chitnis on 10/13/2016.
 */

@RunWith(AndroidJUnit4.class)
public class HeartSoundClassificationTest_1 {
    private HeartSoundClassification heartSoundClassification;

    @Before
    public void setUp() {
        heartSoundClassification = new HeartSoundClassification();
        heartSoundClassification.readHMMFull(InstrumentationRegistry.getTargetContext());
    }


    @Test
    public void PRPSTestCase_1() {
        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
//        for (int i = 1; i <= 42; i++) {
            try {
                br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("1.csv")));
                ArrayList<Double> signal = new ArrayList<>();
                while ((line = br.readLine()) != null) {
                    signal.add(Double.parseDouble(line));
                }
                dataPoints = signal.size();
                heartSound = new double[dataPoints];
                for (int j = 0; j < dataPoints; j++) {
                    heartSound[j] = signal.get(j);
                }
                heartSoundClassification.setAuscultationArea(AuscultationArea.PULMONARY);

                String result = heartSoundClassification.getClassificationResult(false, heartSound);
                String classificationIndex = heartSoundClassification.getIndexClassification();
                assertEquals("Specific classification: "+result,"ar_pr", classificationIndex);
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
//        }
    }

    @Test
    public void PRPSTestCase_2() {
        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("2.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }
            heartSoundClassification.setAuscultationArea(AuscultationArea.PULMONARY);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("Specific classification: "+result,"ar_pr", classificationIndex);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
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

    @Test
    public void PRPSTestCase_3() {
        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("3.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }
            heartSoundClassification.setAuscultationArea(AuscultationArea.PULMONARY);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("Specific classification: "+result,"ar_pr", classificationIndex);
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

    @Test
    public void MSTestCase_1() {
        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("4.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }
            heartSoundClassification.setAuscultationArea(AuscultationArea.MITRAL);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("Specific classification: "+result,"ms_ts", classificationIndex);
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

    @Test
    public void MSTestCase_2() {
        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("5.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }
            heartSoundClassification.setAuscultationArea(AuscultationArea.MITRAL);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("Specific classification: "+result,"ms_ts", classificationIndex);
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

    @Test
    public void MSTestCase_3() {
        int dataPoints;
        double[] heartSound;
        String line;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("6.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }
            heartSoundClassification.setAuscultationArea(AuscultationArea.MITRAL);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("Specific classification: "+result,"ms_ts", classificationIndex);
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

    @Test
    public void ASTestCase_1() {
        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("7.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }
            heartSoundClassification.setAuscultationArea(AuscultationArea.AORTIC);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("as_ps", classificationIndex);
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
    }

    @Test
    public void ASTestCase_2() {
        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("8.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }
            heartSoundClassification.setAuscultationArea(AuscultationArea.AORTIC);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("as_ps", classificationIndex);
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
    }

    @Test
    public void ASTestCase_3() {
        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("9.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }
            heartSoundClassification.setAuscultationArea(AuscultationArea.AORTIC);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("Specific classification: "+result,"as_ps", classificationIndex);
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

    @Test
    public void PRPSTestCase_4() {
        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("10.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }
            heartSoundClassification.setAuscultationArea(AuscultationArea.PULMONARY);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("Specific classification: "+result,"ar_pr", classificationIndex);
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

    @Test
    public void PRPSTestCase_5() {
        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("11.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }
            heartSoundClassification.setAuscultationArea(AuscultationArea.PULMONARY);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("Specific classification: "+result,"ar_pr", classificationIndex);
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

    @Test
    public void SysTestCase_1() {
        int dataPoints;
        double[] heartSound;
        String line;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("12.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }
            heartSoundClassification.setAuscultationArea(AuscultationArea.AORTIC);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("Specific classification: "+result,"as_ps", classificationIndex);
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

    @Test
    public void SysTestCase_2() {
        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("13.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }
            heartSoundClassification.setAuscultationArea(AuscultationArea.AORTIC);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("Specific classification: "+result,"as_ps", classificationIndex);
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

    @Test
    public void ASTestCase_4() {
        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("14.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }
            heartSoundClassification.setAuscultationArea(AuscultationArea.AORTIC);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("Specific classification: "+result,"as_ps", classificationIndex);
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

    @Test
    public void ASTestCase_5() {
        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("15.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }
            heartSoundClassification.setAuscultationArea(AuscultationArea.AORTIC);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("Specific classification: "+result,"as_ps", classificationIndex);
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

    @Test
    public void ASTestCase_6() {
        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("16.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }
            heartSoundClassification.setAuscultationArea(AuscultationArea.AORTIC);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("Specific classification: "+result,"as_ps", classificationIndex);
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

    @Test
    public void ASTestCase_7() {
        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("17.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }
            heartSoundClassification.setAuscultationArea(AuscultationArea.AORTIC);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("Specific classification: "+result,"as_ps", classificationIndex);
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

    @Test
    public void MRorARTestCase_1() {
        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("18.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }
            heartSoundClassification.setAuscultationArea(AuscultationArea.MITRAL);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("Specific classification: "+result,"mr_tr", classificationIndex);
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

    @Test
    public void MRorARTestCase_2() {
        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("19.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }
            heartSoundClassification.setAuscultationArea(AuscultationArea.MITRAL);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("Specific classification: "+result,"mr_tr", classificationIndex);
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

    @Test
    public void MRorARTestCase_3() {
        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("20.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }
            heartSoundClassification.setAuscultationArea(AuscultationArea.MITRAL);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("Specific classification: "+result,"mr_tr", classificationIndex);
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

    @Test
    public void MRTestCase_1() {
        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("21.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }
            heartSoundClassification.setAuscultationArea(AuscultationArea.MITRAL);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("Specific classification: "+result,"mr_tr", classificationIndex);
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

    @Test
    public void MRTestCase_2() {
        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("22.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }
            heartSoundClassification.setAuscultationArea(AuscultationArea.MITRAL);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("Specific classification: "+result,"mr_tr", classificationIndex);
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

    @Test
    public void MRTestCase_3() {
        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("23.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }
            heartSoundClassification.setAuscultationArea(AuscultationArea.MITRAL);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("Specific classification: "+result,"mr_tr", classificationIndex);
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

    @Test
    public void MRTestCase_4() {
        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("24.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }
            heartSoundClassification.setAuscultationArea(AuscultationArea.MITRAL);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("Specific classification: "+result,"mr_tr", classificationIndex);
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

    @Test
    public void NormalTestCase_1() {
        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("25.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }
            heartSoundClassification.setAuscultationArea(AuscultationArea.PULMONARY);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("Specific classification: "+result,"normal", classificationIndex);
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

    @Test
    public void NormalTestCase_2() {
        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("26.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }
            heartSoundClassification.setAuscultationArea(AuscultationArea.PULMONARY);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("Specific classification: "+result,"normal", classificationIndex);
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

    @Test
    public void NormalTestCase_3() {
        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("27.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }
            heartSoundClassification.setAuscultationArea(AuscultationArea.PULMONARY);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("Specific classification: "+result,"normal", classificationIndex);
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

    @Test
    public void SoftMurmurTestCase_1() {
        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("28.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }
            heartSoundClassification.setAuscultationArea(AuscultationArea.AORTIC);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("Specific classification: "+result,"flow", classificationIndex);
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

    @Test
    public void SoftMurmurTestCase_2() {
        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("29.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }
            heartSoundClassification.setAuscultationArea(AuscultationArea.AORTIC);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("Specific classification: "+result,"flow", classificationIndex);
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

    @Test
    public void SoftMurmurTestCase_3() {
        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("30.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }
            heartSoundClassification.setAuscultationArea(AuscultationArea.AORTIC);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("Specific classification: "+result,"flow", classificationIndex);
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

    @Test
    public void SoftMurmurTestCase_4() {
        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("31.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }
            heartSoundClassification.setAuscultationArea(AuscultationArea.AORTIC);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("Specific classification: "+result,"flow", classificationIndex);
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

    @Test
    public void SoftMurmurTestCase_5() {
        int dataPoints;
        double[] heartSound;
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("32.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }
            heartSoundClassification.setAuscultationArea(AuscultationArea.AORTIC);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("Specific classification: "+result,"flow", classificationIndex);
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

    @Test
    public void SoftMurmurTestCase_6() {
        int dataPoints;
        double[] heartSound;
        String line;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("33.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.add(Double.parseDouble(line));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            for (int j = 0; j < dataPoints; j++) {
                heartSound[j] = signal.get(j);
            }
            heartSoundClassification.setAuscultationArea(AuscultationArea.AORTIC);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("Specific classification: "+result,"flow", classificationIndex);
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
