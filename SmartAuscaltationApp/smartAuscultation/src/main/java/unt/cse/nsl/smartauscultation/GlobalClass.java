package unt.cse.nsl.smartauscultation;

import android.app.Application;

import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.ObservationReal;

/**
 * load and share data between the activities
 * @author Shanti R Thiyagaraja
 */
public class GlobalClass extends Application {
    public double[] val_WAV;
    public double[] xWAV;
    public final int NUMBER_OF_HMM_MODEL = 8;
    public Hmm<ObservationReal>[] hmm = new Hmm[NUMBER_OF_HMM_MODEL];
    public String[] hmmName = new String [NUMBER_OF_HMM_MODEL];

//    public void readS2Files() {
//        try {
//            this.val_WAV = getValuesFromCSV(getAssets().open("db2_val_WAV.csv"));
//            this.xWAV = getValuesFromCSV(getAssets().open("db2_val_WAV.csv"));
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        readHMMFull();
//    }
//
//    private double[] getValuesFromCSV(InputStream inputStream) {
//        List<Double> values = new ArrayList<Double>();
//
//        BufferedReader br = null;
//        String line = "";
//        String cvsSplitBy = ",";
//        try {
//
//            br = new BufferedReader(new InputStreamReader(inputStream));//new FileReader(csvFile));
//            while ((line = br.readLine()) != null) {
//                String[] input = line.split(cvsSplitBy);
//                values.add(Double.parseDouble(input[0]));
//            }
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
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
//
//        double[] returnValues = new double[values.size()];
//
//        for (int i = 0; i < values.size(); i++) {
//            returnValues[i] = values.get(i);
//        }
//
//        return returnValues;
//    }
//
//    private void readHMMFull() {
//        hmmName[1] = "as_ps";
//        hmmName[2] = "mvp";
//        hmmName[3] = "mr_tr";
//        hmmName[4] = "ar_pr";
//        hmmName[5] = "pda";
//        hmmName[6] = "flow";
//        hmmName[7] = "ms_ts";
//        hmmName[0] = "normal";
//
//        File localFile = Environment.getExternalStorageDirectory();
//
//        for (int i = 0; i < NUMBER_OF_HMM_MODEL; i++) {
//            try {
//                Reader hmmReader = new InputStreamReader(getAssets().open(hmmName[i]));
////                System.out.println(i);
//                hmm[i] = HmmReader.read(hmmReader, new OpdfGaussianReader());
//                hmmReader.close();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (FileFormatException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
