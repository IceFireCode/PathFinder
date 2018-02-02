package nl.ns.pathfinder;

import android.graphics.PointF;

/**
 * Created by paulvancappelle on 14-11-16.
 */

public class Box {

    private PointF mOrigin;
    private PointF mCurrent;


    public Box(PointF origin) {
        mOrigin = origin;
        mCurrent = origin;
    }

    public PointF getOrigin() {
        return mOrigin;
    }


    public PointF getCurrent() {
        return mCurrent;
    }

    public void setCurrent(PointF current) {
        mCurrent = current;
    }
}
