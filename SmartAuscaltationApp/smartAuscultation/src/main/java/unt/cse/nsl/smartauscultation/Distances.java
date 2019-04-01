package unt.cse.nsl.smartauscultation;
/**
 * Class with static methods for differnt implementations of distance metrics
 * that can be calculated between two vectors.
 *
 * @author Markus Konrad <post@mkonrad.net>
 */
public class Distances {
    /**
     * Squared euclidian distance between vectors <v1> and <v2> for doubles
     * @param v1 vector 1
     * @param v2 vector 2
     * @return squared euclidian distance
     */
    static public double euclid(double[] v1, double[] v2) {
        double dist = 0.0;

        for (int f = 0; f < v1.length; f++) {
            double d = v1[f] - v2[f];
            dist += d * d;
        }

        return dist;
    }

    /**
     * Squared euclidian distance between vectors <v1> and <v2> for floats
     * @param v1 vector 1
     * @param v2 vector 2
     * @return squared euclidian distance
     */
    static public float euclid(float[] v1, float[] v2) {
        float dist = 0.0f;

        for (int f = 0; f < v1.length; f++) {
            float d = v1[f] - v2[f];
            dist += d * d;
        }

        return dist;
    }

    /**
     * Calculate classic Hausdorff distance between vectors <f1> and <f2>.
     *
     * @param f1 Feature vector 1
     * @param f2 Feature vector 2
     * @return classic Hausdorff distance
     */
    static public float hausdorff(float[][] f1, float[][] f2) {
        // calculate maximum of all minima between f1 and f2
        float max1 = maxOfDistMinima(f1, f2);

        // calculate maximum of all minima between f2 and f1
        float max2 = maxOfDistMinima(f2, f1);

        return Math.max(max1, max2);
    }

    /**
     * Calculate perceptually modified Hausdorff distance between vectors <f1>
     * and <f2>, weighted by <w1> and <w2>
     *
     * @param f1    Feature vector 1
     * @param w1    Weight values of feature vector 1
     * @param f2    Feature vector 2
     * @param w2    Weight values of feature vector 2
     * @return perceptually modified Hausdorff distance
     */
    static public double pmHausdorff(double[][] f1, double[] w1, double[][] f2, double[] w2) {
        // calculate maximum of all minima between f1 and f2
        double max1 = maxOfDistMinima(f1, w1, f2, w2);

        // calculate maximum of all minima between f2 and f1
        double max2 = maxOfDistMinima(f2, w2, f1, w1);

        return Math.max(max1, max2);
    }

    /**
     * Calculate the histogram intersection between histograms <h1> and <h2>
     * @param h1 histogram 1
     * @param h2 histogram 2
     * @return intersection value
     */
    static public float histIntersection(float[] h1, float[] h2) {
        float minSum = 0.0f;

        for (int i = 0; i < h1.length; i++) {
            minSum += Math.min(h1[i], h2[i]);
        }

        float minOverallSum = Math.min(histSum(h1), histSum(h2));

        return 1.0f - minSum / minOverallSum;
    }

    static private float histSum(float [] h) {
        float s = 0.0f;

        for (int i = 0; i < h.length; i++) {
            s += h[i];
        }

        return s;
    }

    /**
     * Calculate weighted maximum of distance minima between 2 feature vectors
     * <f1> and <f2> weighted by <w1> and <w2>.
     *
     * @param f1    Feature vector 1
     * @param w1    Weight values of feature vector 1
     * @param f2    Feature vector 2
     * @param w2    Weight values of feature vector 2
     * @return maximum of distance minima.
     */
    static private double maxOfDistMinima(double[][] f1, double[] w1, double[][] f2, double[] w2) {
        // calculate minima between f1 and f2
        double[] minima = new double[f1.length];
        double sumW = 0.0;
        for (int i1 = 0; i1 < f1.length; i1++) {
            minima[i1] = Double.MAX_VALUE;
            for (int i2 = 0; i2 < f2.length; i2++) {
                double dist = euclid(f1[i1], f2[i2]);

                dist = dist / Math.min(w1[i1], w2[i2]);  // mod. hausdorff

                if (dist < minima[i1]) {
                    minima[i1] = dist;

                }
            }

            sumW += w1[i1];
        }

        // calculate mod. hausdorff
        double sumD = 0.0;
        for (int i = 0; i < minima.length; i++) {
            sumD += w1[i] * minima[i];
        }

        return sumD / sumW;
    }

    static private float maxOfDistMinima(float[][] f1, float[][] f2) {
        // calculate minima between f1 and f2
        float[] minima = new float[f1.length];
        for (int i1 = 0; i1 < f1.length; i1++) {
            minima[i1] = Float.MAX_VALUE;
            for (int i2 = 0; i2 < f2.length; i2++) {
                float dist = euclid(f1[i1], f2[i2]);

                if (dist < minima[i1]) {
                    minima[i1] = dist;

                }
            }
        }


        // calculate classic hausdorff
        // get the maximum of the minima for f1
        float max = -Float.MIN_VALUE;
        for (int i = 0; i < minima.length; i++) {
            if (minima[i] > max) {
                max = minima[i];
            }
        }

        return max;
    }
}