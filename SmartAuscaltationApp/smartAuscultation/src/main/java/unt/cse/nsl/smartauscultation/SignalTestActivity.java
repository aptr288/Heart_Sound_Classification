//package unt.cse.nsl.mybloodpressure;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Color;
//import android.media.AudioTrack;
//import android.os.Bundle;
//import android.os.Environment;
//import android.widget.RelativeLayout;
//
//import org.achartengine.ChartFactory;
//import org.achartengine.GraphicalView;
//import org.achartengine.model.XYMultipleSeriesDataset;
//import org.achartengine.model.XYSeries;
//import org.achartengine.renderer.XYMultipleSeriesRenderer;
//import org.achartengine.renderer.XYSeriesRenderer;
//import org.unt.cse.nsl.audio.AudioData;
//import org.unt.cse.nsl.audio.AudioInput;
//import org.unt.cse.nsl.audio.AudioPeak;
//import org.unt.cse.nsl.video.ARGBFrame;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.StringTokenizer;
//
///**
// * Created by Anurag Chitnis on 11/7/2016.
// */
//
//public class SignalTestActivity extends Activity implements AudioInput{
//
//    Context context;
//    /**
//     * Base layout of the graph container
//     */
//    private RelativeLayout analysisBase;
//    /**
//     * This variable will hold the series of data-points
//     */
//    private XYMultipleSeriesDataset dataSet;
//    /**
//     * This variable will hold the view related configuration
//     */
//    private XYMultipleSeriesRenderer seriesRenderer;
//    private XYSeriesRenderer audioRendererS1;
//    private XYSeries audioSeriesS1;
//
//    private BufferedReader reader;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        context = getApplicationContext();
//        setContentView(R.layout.fragment_final_result);
//
//        analysisBase = (RelativeLayout) findViewById(R.id.analysis_base);
//
//        dataSet = new XYMultipleSeriesDataset();
//        seriesRenderer = new XYMultipleSeriesRenderer();
//        audioSeriesS1 = new XYSeries("Audio S1");
//        audioRendererS1 = new XYSeriesRenderer();
//
//        audioRendererS1.setColor(Color.BLUE);
//        audioRendererS1.setFillBelowLine(false);
//
//        dataSet.addSeries(audioSeriesS1);
//
//        seriesRenderer.addSeriesRenderer(audioRendererS1);
//
//
//
//
//        // Make a view
//        GraphicalView graphicalView = ChartFactory
//                .getLineChartView(context, dataSet, seriesRenderer);
//
//        analysisBase.addView(graphicalView, 0);
//    }
//
//    private void readAudioFile() {
//
//        // Get all of the available files
//        String rootPath = Environment.getExternalStorageDirectory()
//                .getAbsolutePath()
//                + File.separator
//                + getString(R.string.app_name)
//                + File.separator
//                + Messages.getString("BloodPressureActivity.recordings_text"); //$NON-NLS-1$
//
//        // Go to the heart sound directory
//        rootPath = rootPath + File.separator + "Heart"+ File.separator +"anurag- 2016.11.07- 12.57.41.csv";
//
//        File audioFile = new File(rootPath);
//
//        try
//        {
//            reader = new BufferedReader( new FileReader( audioFile ), 8096 );
//        }
//        catch ( FileNotFoundException e )
//        {
//            e.printStackTrace();
//        }
//
//        boolean running = true;
//        String line = null;
//        try
//        {
//            while ( running && ( line = reader.readLine() ) != null )
//            {
//                // Parse the line
//                StringTokenizer st = new StringTokenizer( line, "," );
//                String s = st.nextToken();
//                if ( s.compareTo( "AUDIO" ) == 0 )
//                {
//                    final long timestamp = Long.parseLong( st.nextToken() );
//                    int length = Integer.parseInt( st.nextToken() );
//                    short[] chunkData = new short[length];
//                    for ( int i = 0; i < length; i++ )
//                    {
//                        chunkData[i] = Short.parseShort( st.nextToken() );
//                    }
//                    final AudioData audioData = new AudioData(
//                            chunkData, length, timestamp, 8000 );
////							( (Activity) activity )
////								.runOnUiThread( new Runnable()
////								{
////									public void run()
////									{
////										logi( this.getClass(), "REPLAY AUDIO: " + timestamp + "ms" );
//                    audioSeriesS1.add(audioData.getTimestamp_ms(),audioData.ge);
//                    performAudioCallback( audioData );
////									}
////								} );
//                }
//            }
//        }
//        catch ( IOException e )
//        {
//            e.printStackTrace();
//        }
//        running = false;
//
//    }
//
//    @Override
//    public void inputAudio(final AudioData data) {
//        audioDataList.add(data);
//
//        if(smartAuscultationActivity != null) {
//            smartAuscultationActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    double timeStampModified = data.getTimestamp_ms();
//
//                    // Begin drawing routine
//                    seriesRenderer.setRange(new double[] { timeStampModified - CHART_LENGTH_MS,
//                            timeStampModified, -10000, 10000 });
//                    short chunkData[] = data.getChunkData();
//                    for (int i = 0; i < data.getLength(); i++) {
//                        audioTimeSeries.add(timeStampModified, chunkData[i]);
//                        timeStampModified = timeStampModified + data.getSingleShortLength_ms();
//                    }
//                    graphicalView.repaint();
//                }
//            });
//        }
//    }
//}
