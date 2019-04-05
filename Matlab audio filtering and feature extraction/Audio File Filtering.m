% This program will read the audio file and filters the signal with a cutoff frequency of 500 Hz
clear
clc
audioFileName = 'C:\Users\apt\Desktop\peterbently\normal\103_1305031931979_B.wav';
[y,Fs] = audioread(audioFileName);
figure
subplot(2,1,1)       
plot(y)
title('Initial Audio signal')
audio = [];
audio = normalize(y);
audio = (audio);
subplot(2,1,2)       % add second plot in 2 x 1 grid
plot(audio)       % plot using + markers
title('After Filtering the signal');
foldername = 'C:\Users\apt\Desktop\RecordedJhon\normal\';
listing = dir('C:\Users\apt\Desktop\RecordedJhon\normal\');
i=1;
for itr = 3:size(listing)
    filename = listing(itr).name;
    s = strcat(foldername,filename);
    disp(s);
    [y,Fs] = audioread(s);
    audio = [];
    audio = normalize(y);
    audio = filtered(audio);
    audiowrite(filename,audio,Fs);
end
