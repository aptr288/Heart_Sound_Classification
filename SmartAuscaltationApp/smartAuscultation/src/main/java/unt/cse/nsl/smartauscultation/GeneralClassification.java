package unt.cse.nsl.smartauscultation;

/**
 * Created by Anurag Chitnis on 10/14/2016.
 */

public abstract class GeneralClassification {

    protected String logFileName;

    protected double[][] transpose (double[][] array) {
        if (array == null || array.length == 0)//empty or unset array, nothing do to here
            return array;

        int width = array.length;
        int height = array[0].length;

        double[][] array_new = new double[height][width];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                array_new[y][x] = array[x][y];
            }
        }
        return array_new;
    }

    protected double[] normalize (double selectedSound[]) {
        double min = selectedSound[0];
        int dataPoints = selectedSound.length;
        for (int i = 1; i < dataPoints; i++) {
            if (selectedSound[i] < min) {
                min = selectedSound[i];
            }
        }
        for (int i = 0; i < dataPoints; i++) {
            selectedSound[i] = selectedSound[i] - min;
        }
        double max = selectedSound[0];
        for (int i = 1; i < dataPoints; i++) {
            if (selectedSound[i] > max) {
                max = selectedSound[i];
            }
        }
        for (int i = 0; i < dataPoints; i++) {
            selectedSound[i] = selectedSound[i]/max;
        }

        return selectedSound;
    }
}
