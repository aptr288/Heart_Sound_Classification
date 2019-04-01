package unt.cse.nsl.smartauscultation;

/**
 * GenLloyd Library - Implementation of Generalized Lloyd (also known as
 * Linde-Buzo-Gray or LBG) algorithm
 *
 * @see http://www.data-compression.com/vq.html#lbg
 * @see http://books.google.de/books?id=a7IYh8ncb6AC&lpg=PA29&ots=WOOoPXuNKk&dq=java%20LBG%20implementation&pg=PA29#v=onepage&q&f=false
 *
 * @author Markus Konrad <post@mkonrad.net>
 */
public class GenLloyd {
    protected double[][] samplePoints;
    protected double[][] clusterPoints;
    protected double[] relClusterWeight;
    protected double[] absClusterWeight;

    int[] pointApproxIndices;
    int pointDimension = 0;
    protected double epsilon = 0.0005;
    protected double avgDistortion = 0.0;


    /**
     * Create Generalized Lloyd object with an array of sample points
     * @param samplePoints 2-dim. array[N][K] of N sample points where each
     * point has the same dimension K. You can also pass 'null' and then set
     * the sample points later using setSamplePoints()
     */
    public GenLloyd(double[][] samplePoints) {
        this.setSamplePoints(samplePoints);
    }

    /**
     * Return epsilon parameter (accuracy)
     * @return epsilon parameter (accuracy)
     */
    public double getEpsilon() {
        return epsilon;
    }

    /**
     * Set epsilon parameter (accuracy). Should be a small number 0.0 < epsilon < 0.1
     * @param epsilon parameter (accuracy)
     */
    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    /**
     * Set array of sample points
     * @param samplePoints 2-dim. array[N][K] of N sample points where each
     * point has the same dimension K
     */
    public void setSamplePoints(double[][] samplePoints) {
        if (samplePoints.length > 0) {
            this.samplePoints = samplePoints;
            this.pointDimension = samplePoints[0].length;
        }
    }

    /**
     * Get array of sample points
     * @return 2-dim. array[N][K] of N sample points where each
     * point has the same dimension K
     */
    public double[][] getSamplePoints() {
        return samplePoints;
    }

    /**
     * Get calculated cluster points AFTER they have been calculated using
     * calcClusters();
     * @return calculated cluster points. 2-dim array[N][K] with N clusters of
     * dimension K
     */
    public double[][] getClusterPoints() {
        return clusterPoints;
    }

    public double[] getRelClusterWeight() {
        return relClusterWeight;
    }

    public double[] getAbsClusterWeight() {
        return absClusterWeight;
    }

    public int getNumSamplePoints() {
        return samplePoints.length;
    }

    /**
     * Calculate <numClusters> cluster points. This methods needs to be called
     * BEFORE getClusterPoints() and getClusterWeight()
     * @param numClusters Number of calculated cluster points
     */
    public void calcClusters(int numClusters) {
        // initialize with first cluster
        clusterPoints = new double[1][pointDimension];
        relClusterWeight = new double[numClusters];
        absClusterWeight = new double[numClusters];
        relClusterWeight[0] = 1.0;
        absClusterWeight[0] = samplePoints.length;

        double[] newClusterPoint = initializeClusterPoint(samplePoints);
        clusterPoints[0] = newClusterPoint;


        if (numClusters > 1) {
            // calculate initial average distortion
            avgDistortion = 0.0;
            for (double[] samplePoint : samplePoints) {
                avgDistortion += Distances.euclid(samplePoint, newClusterPoint);
            }

            avgDistortion /= (double)(samplePoints.length * pointDimension);

            // set up array of point approximization indices
            pointApproxIndices = new int[samplePoints.length];

            // split the clusters
            int i = 1;
            do {
                i = splitClusters();
            } while (i < numClusters);
        }
    }

    protected int splitClusters() {
        int newClusterPointSize = 2;
        if (clusterPoints.length != 1) {
            newClusterPointSize = clusterPoints.length * 2;
        }

        // split clusters
        double[][] newClusterPoints = new double[newClusterPointSize][pointDimension];
        int newClusterPointIdx = 0;
        for (double[] clusterPoint : clusterPoints) {
            newClusterPoints[newClusterPointIdx] = createNewClusterPoint(clusterPoint, -1);
            newClusterPoints[newClusterPointIdx+1] = createNewClusterPoint(clusterPoint, +1);

            newClusterPointIdx += 2;
        }

        clusterPoints = newClusterPoints;

        // iterate to approximate cluster points
        //int iteration = 0;
        double curAvgDistortion = 0.0;
        do {
            curAvgDistortion = avgDistortion;

            // find the min values
            for (int pointIdx = 0; pointIdx < samplePoints.length; pointIdx++) {
                double minDist = Double.MAX_VALUE;
                for (int clusterPointIdx = 0; clusterPointIdx < clusterPoints.length; clusterPointIdx++) {
                    double newMinDist = Distances.euclid(samplePoints[pointIdx], clusterPoints[clusterPointIdx]);
                    if (newMinDist < minDist) {
                        minDist = newMinDist;
                        pointApproxIndices[pointIdx] = clusterPointIdx;
                    }
                }
            }

            // update codebook
            for (int clusterPointIdx = 0; clusterPointIdx < clusterPoints.length; clusterPointIdx++) {
                double[] newClusterPoint = new double[pointDimension];
                int num = 0;
                for (int pointIdx = 0; pointIdx < samplePoints.length; pointIdx++) {
                    if (pointApproxIndices[pointIdx] == clusterPointIdx) {
                        addPointValues(newClusterPoint, samplePoints[pointIdx]);
                        num++;
                    }
                }

                if (num > 0) {
                    multiplyPointValues(newClusterPoint, 1.0 / (double)num);
                    clusterPoints[clusterPointIdx] = newClusterPoint;
                    relClusterWeight[clusterPointIdx] = (double)num / (double)samplePoints.length;
                    absClusterWeight[clusterPointIdx] = (double)num;
                }
            }

            // increase iteration count
            //System.out.println("  > Iteration = " + iteration);
            //iteration++;

            // update average distortion
            avgDistortion = 0.0;
            for (int pointIdx = 0; pointIdx < samplePoints.length; pointIdx++) {
                avgDistortion += Distances.euclid(samplePoints[pointIdx], clusterPoints[pointApproxIndices[pointIdx]]);
            }

            avgDistortion /= (double)(samplePoints.length * pointDimension);

        } while (((curAvgDistortion - avgDistortion) / curAvgDistortion) > epsilon);

        return clusterPoints.length;
    }

    protected double[] initializeClusterPoint(double[][] pointsInCluster) {
        // calculate point sum
        double[] clusterPoint = new double[pointDimension];
        for (int numPoint = 0; numPoint < pointsInCluster.length; numPoint++) {
            addPointValues(clusterPoint, pointsInCluster[numPoint]);
        }

        // calculate average
        multiplyPointValues(clusterPoint, 1.0 / (double)pointsInCluster.length);

        return clusterPoint;
    }

    protected double[] createNewClusterPoint(double[] clusterPoint, int epsilonFactor) {
        double[] newClusterPoint = new double[pointDimension];
        addPointValues(newClusterPoint, clusterPoint);
        multiplyPointValues(newClusterPoint, 1.0 + (double)epsilonFactor * epsilon);

        return newClusterPoint;
    }

    protected void addPointValues(double[] v1, double[] v2) {
        for (int pointIdx = 0; pointIdx < v1.length; pointIdx++) {
            v1[pointIdx] += v2[pointIdx];
        }
    }

    protected void multiplyPointValues (double[] v1, double f) {
        for (int pointIdx = 0; pointIdx < v1.length; pointIdx++) {
            v1[pointIdx] *= f;
        }
    }
}