% This program will read the audio file and prompt the user for selection
% of cursor points. When user presses enter after the selection of cursor
% points, program will append the  cut signals to the respective
% classification file.
% addpath('D:\Shanti\Tom Sarma recordings');
clear
clc
trainingFileName = 'C:\Laptop data Jan 18\Desktop\Lab\new normal\new csv\normal.csv';
trainingFileID = fopen(trainingFileName, 'a');
logFileID = fopen('C:\Users\apt\Desktop\Thesis\New Recording Data\logtrans.csv','a');
audioFileName = 'C:\Laptop data Jan 18\Desktop\Lab\new normal\new csv\tarun-2- 2015.01.01- 22.59.37.csv';
if (exist(audioFileName, 'file'))
    Table = readtable(audioFileName, 'Delimiter', ',','ReadVariableNames',false);
else
    disp('File not found')
    return
end
[row, column] = size(Table);
audio = [];
for i=1:row
    % Compare each row's first coloumn with the String 'AUDIO'
    x = strcmp(Table{i,1}, 'AUDIO');
    %if it is AUDIO, then concatinate it to the existing audio
    if x == 1
        audio = cat(2, audio, Table{i, 4:2051});
    end
end
Fs = 8000;
fig = figure; hold on;
audio = normalize(audio);
audio = filtered(audio);
audiowrite('NewWavConverted.wav',audio,Fs);
plot(audio);
ylimits = get(gca,'YLim');
plotdata = [ylimits(1):0.01:ylimits(2)];
player = audioplayer(audio, Fs);
player.TimerFcn = {@plotMaker, player, gca, plotdata};
player.TimerPeriod = 0.005;
play(player);
play(player);
dcm_obj = datacursormode(fig);
disp('Click the points to cut the signal, then press Return.')
set(dcm_obj,'DisplayStyle','datatip','SnapToDataVertex','off','Enable','on');
% Wait while the user selects the cursor points
pause 
cursor_info = getCursorInfo(dcm_obj);

%write the log of operations performed by this program. It saves the
%timestamp, audio file name, diagonosis and cursor points information to a
%file.
timestamp = datestr(datetime('now','InputFormat','uuuu-MM-dd''T''HH:mmXXX','TimeZone','UTC'));
fprintf(logFileID,'%s,%s,%s' ,timestamp,audioFileName,trainingFileName);
%
indexTable = struct2table(cursor_info);
cursorRows = transpose(flipud(indexTable.DataIndex));
cursorLength = length(cursorRows);
for i = 1:cursorLength-1
    training = audio(cursorRows(i):cursorRows(i+1));
    fprintf(trainingFileID,'%f; ',training);
    fprintf(trainingFileID,'\n');
    fprintf(logFileID,',%d',cursorRows(i));
end
fprintf(logFileID,',%d\n',cursorRows(cursorLength));
fclose(trainingFileID);
fclose(logFileID);
close(fig);


