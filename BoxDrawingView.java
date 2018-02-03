package nl.ns.pathfinder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulvancappelle on 14-11-16.
 */

public class BoxDrawingView extends View {

    private static final String TAG = "BoxDrawingView";

    private Box mCurrentBox;
    private List<Box> mBoxes = new ArrayList<>();
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;


    // Used when creating the view in code
    public BoxDrawingView(Context context) {
        super(context, null);
        init();
    }


    // Used when inflating the view from XML
    public BoxDrawingView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        // Paint the boxes a nice semitransparent red (ARGB)
        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22777777);

        // Paint the background transparent
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0x00f8efe0);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF current = new PointF(event.getX(), event.getY());
        String action = "";

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";
                // Reset drawing state
                mCurrentBox = new Box(current);
                mBoxes.add(mCurrentBox);
                break;
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";
                if (mCurrentBox != null) {
                    mCurrentBox.setCurrent(current);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";
                mCurrentBox = null;
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";
                mCurrentBox = null;
                break;
        }

        Log.i(TAG, action + " at x = " + current.x + ", y = " + current.y);

        return true;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // Fill the background
        canvas.drawPaint(mBackgroundPaint);

        for (Box box : mBoxes) {
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);

            canvas.drawRect(left, top, right, bottom, mBoxPaint);
        }
    }


    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        int i = 0;
        for (Box box : mBoxes) {
            float[] positionsArray = {
                    box.getOrigin().x,
                    box.getOrigin().y,
                    box.getCurrent().x,
                    box.getCurrent().y
            };
            bundle.putFloatArray("box" + i, positionsArray);
            i++;
        }

        return bundle;
    }


    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) // implicit null check
        {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(bundle.getParcelable("superState"));
            int i = 0;
            while (bundle != null && bundle.containsKey("box" + i)) {
                float[] positionsArray = bundle.getFloatArray("box" + i);

                PointF origin = new PointF(positionsArray[0], positionsArray[1]);
                PointF current = new PointF(positionsArray[2], positionsArray[3]);
                Box box = new Box(origin);
                box.setCurrent(current);
                mBoxes.add(box);
                i++;
            }
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    public List<Box> getBoxes() {
        return mBoxes;
    }

    public void emptyBoxes() {
        mBoxes.clear();
    }
}
