package jwt;

public class CWT {

	private double[] signal;
	private double[] val_WAV;
	private double[] xWAV;
	private double stepWAV;
	private double[][] coefs;
	private int threadSize = 5;
	
	public CWT (final double[] val_WAV_, final double[] xWAV_) {

        this.val_WAV = val_WAV_;
        this.xWAV = xWAV_;

        stepWAV = xWAV[1] - xWAV[0];
        for (double i:xWAV)
        	i -= xWAV[0];
    }

	public void setSignal(final double[] signal_) {
		this.signal = signal_;
	}
	
	public void ContinuousWaveletTransform(final int scale) {
		
		coefs = new double[scale][signal.length];
		
		final int scaleSize = scale/threadSize; //100
		final int[] row = new int[threadSize];
		for (int i = 0; i < threadSize; i++) {
			row[i] = i*scaleSize;
		}
		
		Thread[] t = new Thread[threadSize];
		for (int i = 0; i < threadSize; i++) {
            final int j = i;
            t[i] = new Thread(new Runnable() {
                public void run() {
                	CWTthread(row[j]+scaleSize, row[j], row[j]+1);
                }
            });
            t[i].start();
        }
		try {
            for (int i = 0; i < threadSize; i++) {
                t[i].join();
            }
            /*for (int i = 0; i < scale; i++) {
            	for (int j = 0; j < signal.length; j++) {
					System.out.print(coefs[i][j] + " ");
				}
            	System.out.println();
			}*/
        }catch (InterruptedException e) {
            System.out.println("Main thread Interrupted");
        }
		
		
		
		/*for(int k=1, index=0; k<=scale; k++,index++) {
			int[] j = new int[(int) (1+Math.floor(k*xWAV[xWAV.length-1]))];
			for(int x=0; x<j.length; x++){
				j[x] = (int) (Math.floor(x/(k*stepWAV)));
			}
			j =  (j.length==1) ? (new int[] {1, 1}) : j;
			
			double[] j_val = new double[j.length];
			for(int x=0; x<j.length; x++){
				j_val[x] = val_WAV[j[x]];
			}
			double[] f = flip(j_val);
			
			coefs[index] = complexConvolve(f, k);
		}*/
		
	}
	
	public void CWTthread (int scale, int index, int k) {
		
		for(; k<=scale; k++,index++) {
			int[] j = new int[(int) (1+Math.floor(k*xWAV[xWAV.length-1]))];
			for(int x=0; x<j.length; x++){
				j[x] = (int) (Math.floor(x/(k*stepWAV)));
			}
			j =  (j.length==1) ? (new int[] {1, 1}) : j;
			
			double[] j_val = new double[j.length];
			for(int x=0; x<j.length; x++){
				j_val[x] = val_WAV[j[x]];
			}
			double[] f = flip(j_val);
			
			coefs[index] = complexConvolve(f, k);
		}
		
	}
	
	public double[][] ContinuousWaveletTransformOri (final int scale) {
		double[][] coefsOri = new double[scale][signal.length];
		for(int k=1, index=0; k<=scale; k++,index++) {
			int[] j = new int[(int) (1+Math.floor(k*xWAV[xWAV.length-1]))];
			for(int x=0; x<j.length; x++){
				j[x] = (int) (Math.floor(x/(k*stepWAV)));
			}
			j =  (j.length==1) ? (new int[] {1, 1}) : j;
			
			double[] j_val = new double[j.length];
			for(int x=0; x<j.length; x++){
				j_val[x] = val_WAV[j[x]];
			}
			double[] f = flip(j_val);
			
			coefsOri[index] = complexConvolve(f, k);
		}
		/*System.out.println("Original is...");
		for (int i = 0; i < scale; i++) {
        	for (int j = 0; j < signal.length; j++) {
				System.out.print(coefsOri[i][j] + " ");
			}
        	System.out.println();
		}*/
		return coefsOri;
	}

	public double[][] scalogramdata () {
		double[][] data = new double[coefs.length][coefs[0].length];
		double sum = 0;
		
		for(int i=0; i<coefs.length; i++){
			for(int j=0; j<coefs[i].length; j++){
				data[i][j] = Math.abs(coefs[i][j]*coefs[i][j]);
				sum += data[i][j];
			}
		}
		for(int i=0; i<data.length; i++){
			for(int j=0; j<data[i].length; j++){
				data[i][j] = 100 * data[i][j] / sum;
			}
		}
		
		return data;
	}
	
	private double[] flip(final double[] array) {
        
		if (array == null) {
            throw new NullPointerException("array is null");
        }

        double[] res = new double[array.length];
        for (int i = 0; i < res.length; i++)
            res[res.length - 1 - i] = array[i];
        
        return res;
    }
	
	@SuppressWarnings("null")
	private double[] complexConvolve (final double[] f, int k) {
		
		double[] wconv = Utilities.convolve(signal, f);
		double[] diff = Utilities.differences(wconv);
		double[] vec = Utilities.vector(diff, signal.length);
		double[] coeff = new double[vec.length];
		
		for(int i=0; i<vec.length; i++) {
			coeff[i] = (-Math.sqrt(k)) * vec[i];
		}
		
		return coeff;
		
	}
}
