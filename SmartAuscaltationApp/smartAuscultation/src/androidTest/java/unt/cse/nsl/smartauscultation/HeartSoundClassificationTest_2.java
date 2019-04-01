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

import unt.cse.nsl.smartauscultation.heart.HeartSoundClassification;

import static junit.framework.Assert.assertEquals;

/**
 *  This class is for testing the HeartSoundClassification class.
 *  We are validating our murmur classification algorithm by running the various types of signals through it.
 *  Few test cases may fail from this class, as we don't have the accuracy of 100% in classification.<br>
 *  We have taken these test cases from Shanti's Murmur CD test data<br>
 *      This data is taken from the Murmur CD<br>
 * Created by Anurag Chitnis on 10/13/2016.
 */

@RunWith(AndroidJUnit4.class)
public class HeartSoundClassificationTest_2 {

    private HeartSoundClassification heartSoundClassification;

    @Before
    public void setUp() {
        heartSoundClassification = new HeartSoundClassification();
        heartSoundClassification.readHMMFull(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void MitralStenosisTestCase_1() {
        int dataPoints;
        double[] heartSound;
        String line;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("test (1).csv")));
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
            assertEquals("Specific classification: "+result,"mvp", classificationIndex);
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
    public void MitralStenosisTestCase_2() {
        int dataPoints;
        double[] heartSound;
        String line;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("test (2).csv")));
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
            assertEquals("Specific classification: "+result,"mvp", classificationIndex);
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
    public void MitralStenosisTestCase_3() {
        int dataPoints;
        double[] heartSound;
        String line;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("test (3).csv")));
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
            assertEquals("Specific classification: "+result,"mvp", classificationIndex);
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
    public void MitralStenosisTestCase_4() {
        int dataPoints;
        double[] heartSound;
        String line;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("test (4).csv")));
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
            assertEquals("Specific classification: "+result,"mvp", classificationIndex);
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
    public void MitralStenosisTestCase_5() {
        int dataPoints;
        double[] heartSound;
        String line;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("test (5).csv")));
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
            assertEquals("Specific classification: "+result,"mvp", classificationIndex);
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
    public void MitralStenosisTestCase_6() {
        int dataPoints;
        double[] heartSound;
        String line;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("test (6).csv")));
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
            assertEquals("Specific classification: "+result,"mvp", classificationIndex);
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
    public void MitralStenosisTestCase_7() {
        int dataPoints;
        double[] heartSound;
        String line;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("test (7).csv")));
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
            assertEquals("Specific classification: "+result,"mvp", classificationIndex);
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
    public void MitralStenosisTestCase_8() {
        int dataPoints;
        double[] heartSound;
        String line;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("test (8).csv")));
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
            assertEquals("Specific classification: "+result,"mvp", classificationIndex);
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
    public void MitralStenosisTestCase_9() {
        int dataPoints;
        double[] heartSound;
        String line;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("test (9).csv")));
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
            assertEquals("Specific classification: "+result,"mvp", classificationIndex);
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
    public void NormalTestCase_10() {
        int dataPoints;
        double[] heartSound;
        String line;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("test (10).csv")));
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
//
//    @Test
//    public void NormalTestCase_11() {
//        int dataPoints;
//        double[] heartSound;
//        String line;
//        BufferedReader br = null;
//        try {
//            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("test (11).csv")));
//            ArrayList<Double> signal = new ArrayList<>();
//            while ((line = br.readLine()) != null) {
//                signal.add(Double.parseDouble(line));
//            }
//            dataPoints = signal.size();
//            heartSound = new double[dataPoints];
//            for (int j = 0; j < dataPoints; j++) {
//                heartSound[j] = signal.get(j);
//            }
//            heartSoundClassification.setAuscultationArea(AuscultationArea.PULMONARY);
//
//            String result = heartSoundClassification.getClassificationResult(false, heartSound);
//            String classificationIndex = heartSoundClassification.getIndexClassification();
//            assertEquals("Specific classification: "+result,"flow", classificationIndex);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//    @Test
//    public void NormalTestCase_12() {
//        int dataPoints;
//        double[] heartSound;
//        String line;
//        BufferedReader br = null;
//        try {
//            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("test (12).csv")));
//            ArrayList<Double> signal = new ArrayList<>();
//            while ((line = br.readLine()) != null) {
//                signal.add(Double.parseDouble(line));
//            }
//            dataPoints = signal.size();
//            heartSound = new double[dataPoints];
//            for (int j = 0; j < dataPoints; j++) {
//                heartSound[j] = signal.get(j);
//            }
//            heartSoundClassification.setAuscultationArea(AuscultationArea.PULMONARY);
//
//            String result = heartSoundClassification.getClassificationResult(false, heartSound);
//            String classificationIndex = heartSoundClassification.getIndexClassification();
//            assertEquals("Specific classification: "+result,"flow", classificationIndex);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//    @Test
//    public void NormalTestCase_13() {
//        int dataPoints;
//        double[] heartSound;
//        String line;
//        BufferedReader br = null;
//        try {
//            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("test (13).csv")));
//            ArrayList<Double> signal = new ArrayList<>();
//            while ((line = br.readLine()) != null) {
//                signal.add(Double.parseDouble(line));
//            }
//            dataPoints = signal.size();
//            heartSound = new double[dataPoints];
//            for (int j = 0; j < dataPoints; j++) {
//                heartSound[j] = signal.get(j);
//            }
//            heartSoundClassification.setAuscultationArea(AuscultationArea.PULMONARY);
//
//            String result = heartSoundClassification.getClassificationResult(false, heartSound);
//            String classificationIndex = heartSoundClassification.getIndexClassification();
//            assertEquals("Specific classification: "+result,"flow", classificationIndex);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//    @Test
//    public void NormalTestCase_14() {
//        int dataPoints;
//        double[] heartSound;
//        String line;
//        BufferedReader br = null;
//        try {
//            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("test (14).csv")));
//            ArrayList<Double> signal = new ArrayList<>();
//            while ((line = br.readLine()) != null) {
//                signal.add(Double.parseDouble(line));
//            }
//            dataPoints = signal.size();
//            heartSound = new double[dataPoints];
//            for (int j = 0; j < dataPoints; j++) {
//                heartSound[j] = signal.get(j);
//            }
//            heartSoundClassification.setAuscultationArea(AuscultationArea.PULMONARY);
//
//            String result = heartSoundClassification.getClassificationResult(false, heartSound);
//            String classificationIndex = heartSoundClassification.getIndexClassification();
//            assertEquals("Specific classification: "+result,"flow", classificationIndex);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//    @Test
//    public void NormalTestCase_15() {
//        int dataPoints;
//        double[] heartSound;
//        String line;
//        BufferedReader br = null;
//        try {
//            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("test (15).csv")));
//            ArrayList<Double> signal = new ArrayList<>();
//            while ((line = br.readLine()) != null) {
//                signal.add(Double.parseDouble(line));
//            }
//            dataPoints = signal.size();
//            heartSound = new double[dataPoints];
//            for (int j = 0; j < dataPoints; j++) {
//                heartSound[j] = signal.get(j);
//            }
//            heartSoundClassification.setAuscultationArea(AuscultationArea.PULMONARY);
//
//            String result = heartSoundClassification.getClassificationResult(false, heartSound);
//            String classificationIndex = heartSoundClassification.getIndexClassification();
//            assertEquals("Specific classification: "+result,"normal", classificationIndex);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
    @Test
    public void MRTestCase_17() {
        int dataPoints;
        double[] heartSound;
        String line;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("test (17).csv")));
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
//
//    @Test
//    public void MRTestCase_18() {
//        int dataPoints;
//        double[] heartSound;
//        String line;
//        BufferedReader br = null;
//        try {
//            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("test (18).csv")));
//            ArrayList<Double> signal = new ArrayList<>();
//            while ((line = br.readLine()) != null) {
//                signal.add(Double.parseDouble(line));
//            }
//            dataPoints = signal.size();
//            heartSound = new double[dataPoints];
//            for (int j = 0; j < dataPoints; j++) {
//                heartSound[j] = signal.get(j);
//            }
//            heartSoundClassification.setAuscultationArea(AuscultationArea.MITRAL);
//
//            String result = heartSoundClassification.getClassificationResult(false, heartSound);
//            String classificationIndex = heartSoundClassification.getIndexClassification();
//            assertEquals("Specific classification: "+result,"mr_tr", classificationIndex);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    @Test
//    public void MRTestCase_19() {
//        int dataPoints;
//        double[] heartSound;
//        String line;
//        BufferedReader br = null;
//        try {
//            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("test (19).csv")));
//            ArrayList<Double> signal = new ArrayList<>();
//            while ((line = br.readLine()) != null) {
//                signal.add(Double.parseDouble(line));
//            }
//            dataPoints = signal.size();
//            heartSound = new double[dataPoints];
//            for (int j = 0; j < dataPoints; j++) {
//                heartSound[j] = signal.get(j);
//            }
//            heartSoundClassification.setAuscultationArea(AuscultationArea.MITRAL);
//
//            String result = heartSoundClassification.getClassificationResult(false, heartSound);
//            String classificationIndex = heartSoundClassification.getIndexClassification();
//            assertEquals("Specific classification: "+result,"mr_tr", classificationIndex);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    @Test
//    public void MRTestCase_20() {
//        int dataPoints;
//        double[] heartSound;
//        String line;
//        BufferedReader br = null;
//        try {
//            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("test (20).csv")));
//            ArrayList<Double> signal = new ArrayList<>();
//            while ((line = br.readLine()) != null) {
//                signal.add(Double.parseDouble(line));
//            }
//            dataPoints = signal.size();
//            heartSound = new double[dataPoints];
//            for (int j = 0; j < dataPoints; j++) {
//                heartSound[j] = signal.get(j);
//            }
//            heartSoundClassification.setAuscultationArea(AuscultationArea.MITRAL);
//
//            String result = heartSoundClassification.getClassificationResult(false, heartSound);
//            String classificationIndex = heartSoundClassification.getIndexClassification();
//            assertEquals("Specific classification: "+result,"mr_tr", classificationIndex);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    @Test
//    public void MRTestCase_21() {
//        int dataPoints;
//        double[] heartSound;
//        String line;
//        BufferedReader br = null;
//        try {
//            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("test (21).csv")));
//            ArrayList<Double> signal = new ArrayList<>();
//            while ((line = br.readLine()) != null) {
//                signal.add(Double.parseDouble(line));
//            }
//            dataPoints = signal.size();
//            heartSound = new double[dataPoints];
//            for (int j = 0; j < dataPoints; j++) {
//                heartSound[j] = signal.get(j);
//            }
//            heartSoundClassification.setAuscultationArea(AuscultationArea.MITRAL);
//
//            String result = heartSoundClassification.getClassificationResult(false, heartSound);
//            String classificationIndex = heartSoundClassification.getIndexClassification();
//            assertEquals("Specific classification: "+result,"mr_tr", classificationIndex);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    @Test
//    public void MRTestCase_22() {
//        int dataPoints;
//        double[] heartSound;
//        String line;
//        BufferedReader br = null;
//        try {
//            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("test (22).csv")));
//            ArrayList<Double> signal = new ArrayList<>();
//            while ((line = br.readLine()) != null) {
//                signal.add(Double.parseDouble(line));
//            }
//            dataPoints = signal.size();
//            heartSound = new double[dataPoints];
//            for (int j = 0; j < dataPoints; j++) {
//                heartSound[j] = signal.get(j);
//            }
//            heartSoundClassification.setAuscultationArea(AuscultationArea.MITRAL);
//
//            String result = heartSoundClassification.getClassificationResult(false, heartSound);
//            String classificationIndex = heartSoundClassification.getIndexClassification();
//            assertEquals("Specific classification: "+result,"mr_tr", classificationIndex);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    @Test
//    public void MRTestCase_23() {
//        int dataPoints;
//        double[] heartSound;
//        String line;
//        BufferedReader br = null;
//        try {
//            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("test (23).csv")));
//            ArrayList<Double> signal = new ArrayList<>();
//            while ((line = br.readLine()) != null) {
//                signal.add(Double.parseDouble(line));
//            }
//            dataPoints = signal.size();
//            heartSound = new double[dataPoints];
//            for (int j = 0; j < dataPoints; j++) {
//                heartSound[j] = signal.get(j);
//            }
//            heartSoundClassification.setAuscultationArea(AuscultationArea.MITRAL);
//
//            String result = heartSoundClassification.getClassificationResult(false, heartSound);
//            String classificationIndex = heartSoundClassification.getIndexClassification();
//            assertEquals("Specific classification: "+result,"mr_tr", classificationIndex);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    @Test
//    public void MRTestCase_24() {
//        int dataPoints;
//        double[] heartSound;
//        String line;
//        BufferedReader br = null;
//        try {
//            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("test (24).csv")));
//            ArrayList<Double> signal = new ArrayList<>();
//            while ((line = br.readLine()) != null) {
//                signal.add(Double.parseDouble(line));
//            }
//            dataPoints = signal.size();
//            heartSound = new double[dataPoints];
//            for (int j = 0; j < dataPoints; j++) {
//                heartSound[j] = signal.get(j);
//            }
//            heartSoundClassification.setAuscultationArea(AuscultationArea.MITRAL);
//
//            String result = heartSoundClassification.getClassificationResult(false, heartSound);
//            String classificationIndex = heartSoundClassification.getIndexClassification();
//            assertEquals("Specific classification: "+result,"mr_tr", classificationIndex);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    @Test
//    public void MRTestCase_25() {
//        int dataPoints;
//        double[] heartSound;
//        String line;
//        BufferedReader br = null;
//        try {
//            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("test (25).csv")));
//            ArrayList<Double> signal = new ArrayList<>();
//            while ((line = br.readLine()) != null) {
//                signal.add(Double.parseDouble(line));
//            }
//            dataPoints = signal.size();
//            heartSound = new double[dataPoints];
//            for (int j = 0; j < dataPoints; j++) {
//                heartSound[j] = signal.get(j);
//            }
//            heartSoundClassification.setAuscultationArea(AuscultationArea.MITRAL);
//
//            String result = heartSoundClassification.getClassificationResult(false, heartSound);
//            String classificationIndex = heartSoundClassification.getIndexClassification();
//            assertEquals("Specific classification: "+result,"mr_tr", classificationIndex);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
}
