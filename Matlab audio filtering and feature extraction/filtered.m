function result=filtered(matrix)
% Filters the signal with a cutoff frequency of 500 Hz. A 10th order
% butterworth low pass filter filters the signal. For signals sampled with
% an 8000 Hz sampling frequency.ftype — Filter type 'low'
%10th-order lowpass Butterworth filter with a cutoff frequency of 450 Hz,
%which, for data sampled at 8000 Hz
    Fc = 450;
    Fs = 8000;
    wn = Fc*2/Fs;
    [b, a] = butter(10, wn, 'low');
    result = filter(b, a, matrix);
end