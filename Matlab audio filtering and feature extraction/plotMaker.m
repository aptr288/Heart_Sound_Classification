%%
%% timer callback function definition
function plotMaker(...
    obj,...
    eventdata,...
    player,...
    figHandle,...
    plotdata)

if strcmp(player.Running, 'on')
    hMarker = findobj(figHandle, 'Color', 'r');
    delete(hMarker);
    
    x = player.CurrentSample;
    
    plot(repmat(x, size(plotdata)), plotdata, 'r');
end