package com.example.cs246project.kindergartenprepapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * A view for free drawing on a canvas.
 * <p>
 * @author  Dan Rix
 * @version 1.0
 * @since   2017-02-20
 */

public class DrawView extends View {

    // I don't know just yet what this is used for...
    private Bitmap _bitmap;

    // The canvas for drawing on (like a paint canvas)
    private Canvas _canvas;

    // The series of lines ("path") the user draws on the canvas
    private Path _path;

    // Variables for tracking the previous coordinates
    private float _previousX;
    private float _previousY;

    // The paint handles the color, style, and width of the stroke
    private Paint _paint;

    // Constructs a DrawView
    public DrawView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        _path = new Path();

        _paint = new Paint();
        _paint.setAntiAlias(true);
        _paint.setColor(Color.RED);
        _paint.setStyle(Paint.Style.STROKE);
        _paint.setStrokeWidth(10f);
    }

    // Constructs a DrawView
    public DrawView(Context context) {
        super(context);

        _path = new Path();

        _paint = new Paint();
        _paint.setAntiAlias(true);
        _paint.setColor(Color.RED);
        _paint.setStyle(Paint.Style.STROKE);
        _paint.setStrokeWidth(10f);

        setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // When the view is drawn on the screen, draw the path
        // (or lines) on to the canvase that the user has drawn
        // already
        canvas.drawPath(_path, _paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        _bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        _canvas = new Canvas(_bitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // Which touch event action are we currently doing?
        switch(MotionEventCompat.getActionMasked(event)) {
            case (MotionEvent.ACTION_DOWN) :
                // We have just started our touch
                onTouchStart(event.getX(), event.getY());
                break;
            case (MotionEvent.ACTION_MOVE) :
                // We are moving our touch
                onTouchMove(event.getX(), event.getY());
                break;
        }

        // Invalidate means to set the view's state as invalid,
        // forcing a redraw/refresh of the view (we don't want
        // it lagging so we invalidate/refresh with every touch)
        invalidate();

        // true = we've processed the event and are done with it
        // (otherwise it would propagate to other views that
        // might be on top or underneath this view - i.e. false).
        return true;
    }

    /**
     * OnTouchStart
     * Sets up for drawing (once the user moves their touch position) by resetting the
     * path's starting point and saving the (x, y) coordinates.
     *
     * @param x is the horizontal position of where the user is touching on the view
     * @param y is the vertical position of where the user is touching on the view
     */
    private void onTouchStart(float x, float y) {
        // Move the path to where you are currently touching (otherwise a
        // line will be drawn from your last touch to this touch).
        _path.moveTo(x, y);

        // Set the previous coordinates to the current ones (track where the touch is)
        _previousX = x;
        _previousY = y;
    }

    /**
     * OnTouchMove
     * Draws a line from (_previousX, _previousY) to (x, y) and saves the (x, y) coordinates.
     *
     * @param x is the horizontal position of where the user is touching on the view
     * @param y is the vertical position of where the user is touching on the view
     */
    private void onTouchMove(float x, float y) {
        // Draw the line
        _path.quadTo(_previousX, _previousY, (x + _previousX) /2, (y + _previousY) /2);

        // Set the previous coordinates to the current ones (track where the touch is)
        _previousX = x;
        _previousY = y;
    }

    /**
     * ClearView
     * Clears the view of any drawings
     */
    public void clearView() {
        _path.reset();

        // Invalidate so that the view will refresh with the changes
        invalidate();
    }
}