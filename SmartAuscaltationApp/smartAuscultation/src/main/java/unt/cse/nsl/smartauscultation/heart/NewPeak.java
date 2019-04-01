package unt.cse.nsl.smartauscultation.heart;

/**
 * Created by Anurag Chitnis on 11/23/2016.
 */

public class NewPeak {


    /**The PCM16 value at the peak */
    private double value;
    /**The timestamp in milliseconds of the peak. */
    private double timestamp_ms;

    /**Create a new AudioPeak.
     * @param value The PCM16 audio chunk value
     * @param timestamp_ms The timestamp in milliseconds of the peak.
     */
    public NewPeak( double value, double timestamp_ms )
    {
        this.value = value;
        this.timestamp_ms = timestamp_ms;
    }

    public double getTimestampMillis() {
        return timestamp_ms;
    }

    public double getValue() {
        return value;
    }
}
