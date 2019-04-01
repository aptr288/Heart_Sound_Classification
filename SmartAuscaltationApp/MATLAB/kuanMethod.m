clearvars;
clc;
audioFileName = input('What is the audio file path? ','s');
csv='.csv';
audioFileName=strcat(audioFileName,csv);
data = readtable(audioFileName, 'Delimiter', ',','ReadVariableNames',false);
% data=csvread(audioFileName,0,1);
[row,col]=size(data);
audio = [];
for i=1:row
    x = strcmp(data{i,1}, 'AUDIO');
    if x == 1
        audio = cat(2, audio, data{i, 4:2051});
    end
end
% audio=reshape(data(:,3:col)',1,[]);
% Downsample to 500 hz by taking every 16th sample
d_audio=downsample(audio,16);
% Normalize by subtracting mean then dividing by std
nor=d_audio-mean(d_audio);
d_audio=nor/std(nor);
% Filter audio
Fs=500;
wn2=200*2/Fs;
wn1=20*2/Fs;
[b,a]=butter(5,[wn1 wn2],'bandpass');
fil_audio=filter(b,a,d_audio);
% cut first and last 500 samples off for clearer signal
cut_audio=fil_audio(500:end-500);
% get new length
n=length(cut_audio);
% differentiate signal
d(1)=cut_audio(2)-cut_audio(1);
d(n)=cut_audio(n)-cut_audio(n-1);
for j = 2:n-1
    d(j)=(cut_audio(j+1)-cut_audio(j-1))./2;
end
% square signal
sq=(n);
for j = 1:n
    sq(j)=d(j)*d(j);
end
% plot
f = figure('Visible','off');
plot(cut_audio);
hold on;
findpeaks(sq,'minpeakdistance',125,'minpeakheight',.75);
f.Visible='on';