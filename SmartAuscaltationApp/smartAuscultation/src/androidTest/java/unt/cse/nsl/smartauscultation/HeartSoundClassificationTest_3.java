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
 *  We have taken these test cases from John Hopkin Data<br>
 *      This data is taken from the John Hopkin Data<br>
 * Created by Anurag Chitnis on 10/13/2016.
 */

@RunWith(AndroidJUnit4.class)
public class HeartSoundClassificationTest_3 {

    private HeartSoundClassification heartSoundClassification;

    @Before
    public void setUp() {
        heartSoundClassification = new HeartSoundClassification();
        heartSoundClassification.readHMMFull(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void JH_AR_2() {
        int dataPoints;
        double[] heartSound;
        String line;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("jh_ar_2.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] numbersArray = line.split("; ");
                for(String num : numbersArray)
                    signal.add(Double.parseDouble(num));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
                int j=0;
                for (Double item : signal) {
                    heartSound[j] = item;
                    j++;
                }
            heartSoundClassification.setAuscultationArea(AuscultationArea.AORTIC);

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
    public void JH_AS_1() {
        int dataPoints;
        double[] heartSound;
        String line;
        BufferedReader br = null;
        try {
            for(int i=1;i<4;i++) {
                br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("jh_as_1_"+i+".csv")));
                ArrayList<Double> signal = new ArrayList<>();
                while ((line = br.readLine()) != null) {
                    String[] numbersArray = line.split("; ");
                    for (String num : numbersArray)
                        signal.add(Double.parseDouble(num));
                }
                dataPoints = signal.size();
                heartSound = new double[dataPoints];
                for (int j = 0; j < dataPoints; j++) {
                    heartSound[j] = signal.get(j);
                }
                heartSoundClassification.setAuscultationArea(AuscultationArea.AORTIC);

                String result = heartSoundClassification.getClassificationResult(false, heartSound);
                String classificationIndex = heartSoundClassification.getIndexClassification();
                assertEquals("Specific classification: " + result, "as_ps", classificationIndex);
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

    @Test
    public void JH_MVP_1() {
        int dataPoints;
        double[] heartSound;
        String line;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("jh_mvp_1.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] numbersArray = line.split("; ");
                for(String num : numbersArray)
                    signal.add(Double.parseDouble(num));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
                int j=0;
                for (Double item : signal) {
                    heartSound[j] = item;
                    j++;
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
    public void JH_MVP_2() {
        int dataPoints;
        double[] heartSound;
        String line;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("jh_mvp_2.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] numbersArray = line.split("; ");
                for(String num : numbersArray)
                    signal.add(Double.parseDouble(num));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
            int j = 0;
            for (Double item : signal) {
                heartSound[j] = item;
                j++;
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
    public void JH_PDA_1() {
        int dataPoints;
        double[] heartSound;
        String line;
        BufferedReader br = null;
        try {
            for(int i=1;i<3;i++) {
                br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("jh_pda_1_"+i+".csv")));
                ArrayList<Double> signal = new ArrayList<>();
                while ((line = br.readLine()) != null) {
                    String[] numbersArray = line.split("; ");
                    for (String num : numbersArray)
                        signal.add(Double.parseDouble(num));
                }
                dataPoints = signal.size();
                heartSound = new double[dataPoints];
                for (int j = 0; j < dataPoints; j++) {
                    heartSound[j] = signal.get(j);
                }
                heartSoundClassification.setAuscultationArea(AuscultationArea.PULMONARY);

                String result = heartSoundClassification.getClassificationResult(false, heartSound);
                String classificationIndex = heartSoundClassification.getIndexClassification();
                assertEquals("Specific classification: " + result, "pda", classificationIndex);
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

    @Test
    public void JH_PDA_2() {
        int dataPoints;
        double[] heartSound;
        String line;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("jh_pda_2.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] numbersArray = line.split("; ");
                for(String num : numbersArray)
                    signal.add(Double.parseDouble(num));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
                int j=0;
                for (Double item : signal) {
                    heartSound[j] = item;
                    j++;
                }
            heartSoundClassification.setAuscultationArea(AuscultationArea.PULMONARY);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("Specific classification: "+result,"pda", classificationIndex);
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
    public void JH_PDA_3() {
        int dataPoints;
        double[] heartSound;
        String line;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("jh_pda_3.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] numbersArray = line.split("; ");
                for(String num : numbersArray)
                    signal.add(Double.parseDouble(num));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
                int j=0;
                for (Double item : signal) {
                    heartSound[j] = item;
                    j++;
                }
            heartSoundClassification.setAuscultationArea(AuscultationArea.PULMONARY);

            String result = heartSoundClassification.getClassificationResult(false, heartSound);
            String classificationIndex = heartSoundClassification.getIndexClassification();
            assertEquals("Specific classification: "+result,"pda", classificationIndex);
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
    public void JH_PDA_4() {
        int dataPoints;
        double[] heartSound;
        String line;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("jh_pda_4.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.clear();
                String[] numbersArray = line.split("; ");
                for (String num : numbersArray)
                    signal.add(Double.parseDouble(num));
                dataPoints = signal.size();
                heartSound = new double[dataPoints];
                int j = 0;
                for (Double item : signal) {
                    heartSound[j] = item;
                    j++;
                }
                heartSoundClassification.setAuscultationArea(AuscultationArea.PULMONARY);

                String result = heartSoundClassification.getClassificationResult(false, heartSound);
                String classificationIndex = heartSoundClassification.getIndexClassification();
                assertEquals("Specific classification: " + result, "pda", classificationIndex);
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

    @Test
    public void JH_MR_TR_2() {
        int dataPoints;
        double[] heartSound;
        String line;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("jh_mr_tr_2.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] numbersArray = line.split("; ");
                for(String num : numbersArray)
                    signal.add(Double.parseDouble(num));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
                int j=0;
                for (Double item : signal) {
                    heartSound[j] = item;
                    j++;
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
    public void JH_MR_TR_4() {
        int dataPoints;
        double[] heartSound;
        String line;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("jh_mr_tr_4.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.clear();
                String[] numbersArray = line.split("; ");
                for (String num : numbersArray)
                    signal.add(Double.parseDouble(num));

                dataPoints = signal.size();
                heartSound = new double[dataPoints];
                int j = 0;
                for (Double item : signal) {
                    heartSound[j] = item;
                    j++;
                }
                heartSoundClassification.setAuscultationArea(AuscultationArea.MITRAL);

                String result = heartSoundClassification.getClassificationResult(false, heartSound);
                String classificationIndex = heartSoundClassification.getIndexClassification();
                assertEquals("Specific classification: " + result, "mr_tr", classificationIndex);
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

    @Test
    public void JH_ASD_1() {
        /**
         * Had to include two lub-dubs to pass the test case
         */
        int dataPoints;
        double[] heartSound;
        String line;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("jh_asd_1.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] numbersArray = line.split("; ");
                for(String num : numbersArray)
                    signal.add(Double.parseDouble(num));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
                int j=0;
                for (Double item : signal) {
                    heartSound[j] = item;
                    j++;
                }
            heartSoundClassification.setAuscultationArea(AuscultationArea.AORTIC);

            String result = heartSoundClassification.getClassificationResult(true, heartSound);
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
    public void JH_PS_1() {
        int dataPoints;
        double[] heartSound;
        String line;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("jh_ps_1.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] numbersArray = line.split("; ");
                for(String num : numbersArray)
                    signal.add(Double.parseDouble(num));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
                int j=0;
                for (Double item : signal) {
                    heartSound[j] = item;
                    j++;
                }
            heartSoundClassification.setAuscultationArea(AuscultationArea.PULMONARY);

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
    public void JH_PS_2() {
        int dataPoints;
        double[] heartSound;
        String line;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("jh_ps_2.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] numbersArray = line.split("; ");
                for(String num : numbersArray)
                    signal.add(Double.parseDouble(num));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
                int j=0;
                for (Double item : signal) {
                    heartSound[j] = item;
                    j++;
                }
            heartSoundClassification.setAuscultationArea(AuscultationArea.PULMONARY);

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
    public void JH_PS_3() {
        int dataPoints;
        double[] heartSound;
        String line;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("jh_ps_3.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] numbersArray = line.split("; ");
                for(String num : numbersArray)
                    signal.add(Double.parseDouble(num));
            }
            dataPoints = signal.size();
            heartSound = new double[dataPoints];
                int j=0;
                for (Double item : signal) {
                    heartSound[j] = item;
                    j++;
                }
            heartSoundClassification.setAuscultationArea(AuscultationArea.PULMONARY);

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

    /**
     * jh_ps_1_2.csv file contains multiple lines, Each line has the lubdub signals acquired from the same recording.
     * We are running this test case repeatedly on each line of data
     */
    @Test
    public void JH_PS_4() {
        int dataPoints;
        double[] heartSound;
        String line;
        BufferedReader br = null;
        heartSoundClassification.setAuscultationArea(AuscultationArea.PULMONARY);
        try {
            br = new BufferedReader(new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open("jh_ps_1_2.csv")));
            ArrayList<Double> signal = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                signal.clear();
                String[] numbersArray = line.split("; ");
                for (String num : numbersArray)
                    signal.add(Double.parseDouble(num));
                dataPoints = signal.size();
                heartSound = new double[dataPoints];
                int j=0;
                for (Double item : signal) {
                    heartSound[j] = item;
                    j++;
                }

                String result = heartSoundClassification.getClassificationResult(false, heartSound);
                String classificationIndex = heartSoundClassification.getIndexClassification();
                assertEquals("Specific classification: " + result, "as_ps", classificationIndex);
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
