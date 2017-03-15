/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bowman;

/**
 *
 * @author a-haydar
 */
public interface Targetable {
    /**
     * you cal this method when you try 
     * @param x
     * @param y
     * @return true if point (x,y) is on target, false otherwise 
     */
    public boolean hitTarget(int x, int y);
}
