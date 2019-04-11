package src;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author a-haydar
 */
public interface ArrowStateListener {

    /**
     * Called when an arrow hits a target.
     *
     * @param target which was hit
     */
    public void arrowHitTarget(Targetable target);


    public void arrowHitLand(int xPosition);
}
