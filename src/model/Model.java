package model;

import java.util.Vector;
import java.util.Enumeration;

import view.*;
 
public abstract class Model {
    private Vector<View> views;
    private boolean changed;
 
    public Model() {
        views = new Vector<View>();
    }
 
    /** Add a View to this Model's set of views, if it is not already.
     *  @param v View to add
     *  @throws NullPointerException if v is null
     */
    public synchronized void addView(View v) {
        if (v == null) {
            throw new NullPointerException();
        }
        if (!views.contains(v)) {
            views.addElement(v);
        }
    }
 
    /** Delete a View from this Model's set of views. */
    public synchronized void deleteView(View v) {
        views.removeElement(v);
    }
 
    /** Remove all Views from this Model. */
    public synchronized void deleteViews() {
        views.removeAllElements();
    }
 
    /** Notify this Model's Views if it has changed, and clear the changed status.  Views are notified in no specific order. */
    public synchronized void notifyViews(Object arg) {
        if (hasChanged()) {
            for (Enumeration<View> en = views.elements(); en.hasMoreElements();) {
                ((View) en.nextElement()).update(this, arg);
            }
            clearChanged();
        }
    }
 
    /** Equivalent to notifyViews(null). */
    public void notifyViews() {
        notifyViews(null);
    }
 
    /** @return true if this Model has been changed. */
    protected synchronized boolean hasChanged() {
        return changed;
    }
 
    /** Set this Model into the "changed" state. */
    public synchronized void setChanged() {
        changed = true;
    }
 
    /** Clear this Model's "changed" state. */
    protected synchronized void clearChanged() {
        changed = false;
    }
}
