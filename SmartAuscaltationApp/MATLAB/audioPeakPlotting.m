%{
    AudioPeakPlotting - Plots the peaks found in a recording from the
    HeartSounds application.

    audioFileName must be a csv file with the following format:
    C1      C2          C3          C4-C2050
    AUDIO   TimeStamp   ChunkSize   DATA

    audioPeakFile must be a csv file with the following format:
    C1          C2
                TimeStamp
                TimeStamp
    FirstS1     TimeStamp
    FirstS2     TimeStamp
    SecondS2    TimeStamp
    Diagnosis
%}

% Get audio and peak file paths - if folders are in $PATH only filenames
% are needed
clearvars;
clc;
audioFileName = input('What is the audio file path? ','s');
audioPeakFile = input('What is the peak file path? ','s');
data=csvread(audioFileName,0,1);
p_data=csvread(audioPeakFile,0,1);
start_time=data(1,1);
[row,col]=size(data);
[p_row,p_col]=size(p_data);
% Read in audio file to array
audio=reshape(data(:,3:col)',1,[]);
% Read in peak file to array
p_row = p_row - 1;
peak=p_data(1:p_row);
% Generate normalization factors
    % used for different sampling rates, i.e. 2048 vs 4096
s=randi([2,row]);
diff=data(s,1)-data(s-1,1);
diff=cast(diff,'int64');
range=data(1,2)/diff;
range=cast(range,'double');
% Normalize peak times
peak(:,2)=peak(:,1)-start_time;
% In case first recorded peak is before recording start_time
if(peak(1,2) < 0)
    peaks=peak(2:p_row,:);
    clear peak;
    peak=peaks;
    clear peaks;
    p_row=p_row-1;
end
% Scale peak timeStamps
peak(:,3)=peak(:,2)*range;
peak(:,4)=audio(peak(:,3));
% Generate figure and plot
h = figure('Visible','off','Name','Audio Plot');
plot(audio);
hold on;
% Plot peaks
plot(peak(1:p_row-3,3),peak(1:p_row-3,4),'r*');
plot(peak(p_row-2:p_row,3),peak(p_row-2:p_row,4),'g*');
% Show peak segment
hold off;
l = axis;
axis([peak(1,3)-5000 peak(p_row-3,3)+5000 l(3) l(4)]);
h.Visible = 'on';