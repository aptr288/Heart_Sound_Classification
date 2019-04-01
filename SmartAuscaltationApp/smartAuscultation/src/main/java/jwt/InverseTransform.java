package jwt;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author $ain't
 */
public abstract class InverseTransform implements Transform {
    /**
     * Returns the reconsturcted signal.
     * @return the reconstructed signal
     */
    public abstract double[] getReconstruction();
}
