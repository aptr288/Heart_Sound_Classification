% This code takes a signal sampled at 8000 Hz and downsamples it to 500 Hz.
% It then finds the length of the original recording by comparing the last
% timestamp to the first timestamp. The downsampled signal is then
% normalized. The normalized signal is then differentiated and squared to
% find the peaks. Ultimately, the downsampled signal, the differentiated
% and squared signal, and the peaks are graphed. If needed, the squared
% signal and be upsampled back to 8kHz and graphed on to the original
% signal.

% maintenance
clearvars;
clc;
% read in audio file. Must be in MATLAB path.
audioFileName = input('What is the audio file path? ','s');
csv='.csv';
audioFileName=strcat(audioFileName,csv);
data = readtable(audioFileName, 'Delimiter', ',','ReadVariableNames',false);
% data=csvread(audioFileName,0,1);
[row,~]=size(data);
audio = [];
timeStamps=[];
% Get audio samples and timestamps
for i=1:row
    x = strcmp(data{i,1}, 'AUDIO');
    if x == 1
        audio = cat(2, audio, data{i, 4:2051});
        timeStamps=[timeStamps;data{i,2}];
    end
end
% determine length of recording
x=length(timeStamps);
l=timeStamps(x)+256-.125;
l=l-timeStamps(1);
l=l/10^3;
% downsample audio
d_audio=downsample(audio,16);
% normalize signal
nor=d_audio-mean(d_audio);
d_audio=nor/std(d_audio);
n=length(d_audio);
% differentiate signal
d(1)=d_audio(2)-d_audio(1);
d(n)=d_audio(n)-d_audio(n-1);
for j=2:n-1
    d(j)=(d_audio(j+1)-d_audio(j-1))./2;
end
% square signal
sq =(n);
for j=1:n
    sq(j)=d(j)*d(j);
end
f=figure('Visible','off');
t_d=0:l/(n-1):l;
% plot downsampled signal
plot(t_d,d_audio);
hold on;
% plot squared signal
plot(t_d,sq);
[pks,loc]=findpeaks(sq,'minpeakdistance',75,'minpeakheight',3);
% mark peaks
text(t_d(loc)+.02,pks,'*','Color','g');
title([audioFileName,' Downsampled: 500Hz -Norm']);
f.Visible='on';
% n=length(audio)-1;
% t_o=0:l/n:l;
% u_sq=upsample(sq,16);
% hold off
% ax1=subplot(2,1,1);
% ax2=subplot(2,1,2);
% subplot(ax1);
% plot(t_o,audio);
% hold on
% plot(t_o,u_sq);
% subplot(ax2);
% plot(t_d,d_audio);
% hold on
% plot(t_d,sq);