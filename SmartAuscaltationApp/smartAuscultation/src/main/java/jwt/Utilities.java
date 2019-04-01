package jwt;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author $ain't
 */
class Utilities {
    
    private Utilities() {};

    static double[] convolve(final double[] a, final double[] b) {
        if (a == null) {
            throw new NullPointerException("a is null");
        }
        if (b == null) {
            throw new NullPointerException("b is null");
        }
        
        double[] res = new double[a.length + b.length - 1];

        for(int i = 0; i < a.length; i++) {
        	for(int j = 0; j < b.length; j++) {
                    res[i + j] += a[i] * b[j];
        	}
        }

        return res;
    }

    static double[] downsample(final double[] a) {
        if (a == null) {
            throw new NullPointerException("a is null");
        }

        double[] res = new double[a.length / 2];

        for (int i = 0; i < res.length; i++) {
               res[i] = a[i * 2 + 1];
        }
        return res;
    }

    static double[] upsample(final double[] a) {
        if (a == null) {
            throw new NullPointerException("a is null");
        }

        double[] res = new double[a.length * 2];

        for (int i = 0; i < a.length; i++) {
               res[i * 2 + 1] = a[i];
        }

        return res;
    }

    static double[][] deepCopyOf(final double[][] array) {
        if (array == null) {
            throw new NullPointerException("array is null");
        }

        double[][] copy = new double[array.length][];

        for(int i = 0 ; i < array.length ; i++){
            copy[i] = new double[array[i].length];
            System.arraycopy(array[i], 0, copy[i], 0, array[i].length);
        }

        return copy;
    }
    
    static double[] differences(final double[] a) {
    	if (a == null) {
            throw new NullPointerException("array is null");
        }
    	
    	double[] res = new double[a.length-1];
    	
    	for (int i = 0; i < res.length; i++) {
    		res[i] = a[i+1] - a[i];
    	}
		return res;
    }
    
    static double[] vector(final double[] signal, int slength) {
    	if (signal == null) 
            throw new NullPointerException("array is null");  	
    	if (slength > signal.length)
    		throw new NullPointerException("signal.length is larger then length");
    	
    	int rem;
    	double[] res = new double[slength] ;
    	
    	if(signal.length % 2 == 0) {
    		if(slength % 2 == 0){
    			rem = (signal.length - slength)/2;
    			for(int i=0, j=rem; j<=rem-1+slength ; i++, j++){
    				res[i] = signal[j];
    			}
    		}
    		else{
    			rem = (signal.length - slength + 1)/2;
    			for(int i=0, j=rem - 1; j<=rem-2+slength ; i++, j++){
    				res[i] = signal[j];
    			}
    		}
    	}
    	else {
    		if(slength % 2 == 0){
    			rem = (signal.length - slength - 1)/2;
    			for(int i=0, j=rem; j<=rem-1+slength ; i++, j++){
    				res[i] = signal[j];
    			}
    		}
    		else{
    			rem = (signal.length - slength)/2;
    			for(int i=0, j=rem; j<=rem-1+slength ; i++, j++){
    				res[i] = signal[j];
    			}
    		}
    	}
    	
		return res;
    }
}
