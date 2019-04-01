package unt.cse.nsl.smartauscultation;

import Jama.Matrix;

public class Settings {

	/* THESE ARE DECENT DEFAULT SETTINGS BASED ON ME */
	public static final Matrix DEFAULT_REGRESSION = new Matrix(new double[][] {
			{ 1.7480e-01, 8.8740e-02 }, { -1.0876e+02, -1.5693e+02 },
			{ 8.8067e+01, 1.0513e+02 }, { -1.0441e+01, 6.0641e+00 } });

	public static final Matrix DEFAULT_P = new Matrix(new double[][] {
			{ 4.1294e-03, -6.6992e-01, 2.2381e+00, -7.6384e-01 },
			{ -6.6992e-01, 4.4820e+02, -6.4064e+02, 1.0398e+02 },
			{ 2.2381e+00, -6.4064e+02, 1.4907e+03, -4.0974e+02 },
			{ -7.6384e-01, 1.0398e+02, -4.0974e+02, 1.4536e+02 } });

	public static final float DEFAULT_SYS_OFFSET = 110.9f;
	public static final float DEFAULT_DIAS_OFFSET = 71.621f;

	/* THESE ARE GARBAGE DEFAULT SETTINGS FOR TESTING */
	// public static final Matrix DEFAULT_REGRESSION = new Matrix(new double[][]
	// {
	// { -0.35864, -0.46458 }, { 57.95089, 83.96187 },
	// { -31.89252, -11.71422 }, { 23.81434, 19.48746 } });
	//
	// public static final Matrix DEFAULT_P = new Matrix(new double[][] {
	// { 1.0546e-04, 6.9141e-04, 7.1156e-04, -9.7937e-03 },
	// { 6.9141e-04, 1.1140e+01, -3.6818e+00, -2.3323e+00 },
	// { 7.1156e-04, -3.6818e+00, 1.1515e+01, -2.3347e+00 },
	// { -9.7937e-03, -2.3323e+00, -2.3347e+00, 2.2909e+00 } });
	//
	// public static final float DEFAULT_SYS_OFFSET = 106.102f;
	// public static final float DEFAULT_DIAS_OFFSET = 84.496f;

	// Video peak finder preferred settings
	public static final int VPF_WINDOW_SIZE = 15;
	public static final double VPF_STDEV_MULT_H = 0.85;
	public static final int VPF_MIN_PEAK_DIFF_ms = 250;
	public static final char VPF_CHANNEL = 'g';

	// FFT peak finder preferred settings
	public static final int FFTPF_SMOOTH_LENGTH = 25;
	public static final float FFTPF_SHIFT = 0.15f;
	public static final int FFTPF_MIN_PEAK_DIFF_ms = 300;

	// Audio to FFT preferred settings
	public static final int A2FFT_LENGTH = 128;
	public static final int A2FFT_INC_SIZE = 128;

	// Blood pressure peak chooser preferred settings
	public static final long BPPC_SHIFT_ms = 100;

	// Median blood pressure accumulator preferred settings
	public static final float MBPA_PERC_TO_CUT = 0.25f;

	// Requirements to complete a blood pressure reading
	public static final int COMP_MIN_READINGS = 20;
	public static final float COMP_STDEV_RANGE = 15.0f;
	public static final float COMP_STDEV_MAX = 3.0f;

	// Acceptable signal definitions
	public static final float PULSE_MAX_STDEV_ms = 300.0f;

	// Default values
	public static final int DEF_LOWER_FREQ = 10;
	public static final int DEF_UPPER_FREQ = 30;

	// Microphone settings
	public static final int MIC_RATE_Hz = 8000;
	public static final int MIC_BUFFER_SIZE = 2048;
}
