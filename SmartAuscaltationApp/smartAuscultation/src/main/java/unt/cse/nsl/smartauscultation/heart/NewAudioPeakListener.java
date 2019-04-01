package unt.cse.nsl.smartauscultation.heart;

/**
 * Created by Anurag Chitnis on 11/23/2016.
 */

public interface NewAudioPeakListener {

    /**Handle the specified peak.  This is called by AudioPeakFinder on all
     * registered listeners.
     * @param peak The peak to be handled.
     */
     void onPeak( NewPeak peak );

}
