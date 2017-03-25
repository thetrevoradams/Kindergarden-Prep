package com.example.cs246project.kindergartenprepapp;

import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

/**
 * An Activity that allows the user to trace letters in the alphabet, numbers, or words (including
 * their name). This is meant to help them improve their fine motor, writing, and recognition
 * skills.
 * <p>
 * @author  Dan Rix
 * @version 1.0
 * @since   2017-02-20
 */

public class NameTraceableActivity extends SkipTapActivity {

    // Model object for managing the name character values.
    private NameTraceableModel _model;

    // The view control that allows the user to trace on a transparent canvas.
    private DrawView _drawView;

    // Width of the characters in pixels.
    private static int _characterWidth = 300;

    // Total width of the traceable area (length of the character * 300dp).
    private int _totalWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _model = new NameTraceableModel(this);

        // Set the content view layout
        setContentView(R.layout.name_traceable_activity);

        // Set the _drawView
        _drawView = (DrawView) findViewById(R.id.drawView);

        // Build the background images from the traceCharacters
        setTraceBackgroundFromValues(_model.getValues());

        playInstructions(getResources().getIdentifier(_model.getInstructionsFileName(), "raw", getPackageName()));

        // Hide the previous button since the activity is at the start
        FloatingActionButton previousButton = (FloatingActionButton) findViewById(R.id.btnPrevious);
        previousButton.setVisibility(View.INVISIBLE);

        // Hide button if the activity isn't already complete
        FloatingActionButton doneButton = (FloatingActionButton) findViewById(R.id.btnDone);
        doneButton.setVisibility(View.INVISIBLE);

        TextView myTextView=(TextView)findViewById(R.id.textView);
        Typeface typeFace=Typeface.createFromAsset(getAssets(),"penmanship_print.ttf");
        myTextView.setTypeface(typeFace);
    }

    /**
     * Set Trace Background From Values
     * Sets the background trace images using a list of string values (file names).
     */
    private void setTraceBackgroundFromValues(String values) {
        _totalWidth = _model.getNumberOfCharacters() * _characterWidth;

        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.text_and_draw_view_container);
        layout.getLayoutParams().width = _totalWidth;

        DrawView drawView = (DrawView) findViewById(R.id.drawView);
        drawView.getLayoutParams().width = _totalWidth;

        TextView myTextView=(TextView)findViewById(R.id.textView);
        myTextView.getLayoutParams().width = _totalWidth;
        myTextView.setText(values);
    }

    /**
     * Scrolls/Skips to the next value in the name.
     * @param view The view that activated the function (e.g. button)
     */
    public void goToNextValue(View view) {
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.text_and_draw_view_container);
        int x = layout.getScrollX() + 300;
        if (x < (_totalWidth - 300)) {
            layout.scrollTo(x, 0);
        }

        FloatingActionButton previousButton = (FloatingActionButton) findViewById(R.id.btnPrevious);
        if (previousButton.getVisibility() != View.VISIBLE) {
            previousButton.setVisibility(View.VISIBLE);
            Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation);
            previousButton.startAnimation(fadeIn);
        }

        if (x >= (_totalWidth - (_characterWidth * 3))) {
            FloatingActionButton nextButton = (FloatingActionButton) findViewById(R.id.btnNext);
            nextButton.setVisibility(View.INVISIBLE);

            FloatingActionButton doneButton = (FloatingActionButton) findViewById(R.id.btnDone);
            doneButton.setVisibility(View.VISIBLE);
            Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation);
            doneButton.startAnimation(fadeIn);
        }
    }

    /**
     * Scrolls/Skips to the previous value in the name.
     * @param view The view that activated the function (e.g. button)
     */
    public void goToPreviousValue(View view) {
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.text_and_draw_view_container);
        int x = layout.getScrollX() - _characterWidth;
        if (x >= 0) {
            layout.scrollTo(x, 0);
        }

        FloatingActionButton nextButton = (FloatingActionButton) findViewById(R.id.btnNext);
        if (nextButton.getVisibility() != View.VISIBLE) {
            nextButton.setVisibility(View.VISIBLE);
            Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation);
            nextButton.startAnimation(fadeIn);
        }

        if (x <= 0) {
            FloatingActionButton previousButton = (FloatingActionButton) findViewById(R.id.btnPrevious);
            previousButton.setVisibility(View.INVISIBLE);
        }

        FloatingActionButton doneButton = (FloatingActionButton) findViewById(R.id.btnDone);
        doneButton.setVisibility(View.INVISIBLE);
    }

    /**
     * Clear Draw View
     * Clears all the tracing from the _drawView.
     * @param view the button that caused this action to be called
     */
    public void clearDrawView(View view) {
        _drawView.clearView();
    }

    /**
     * On Done Button Click
     * Runs the the actionst that should take place when a user is done with tracing their name.
     * @param view that activated this action.
     */
    public void onDoneButtonClick(View view) {
        // TODO: Display the snapshot popup

        // Play a completion sound
        MediaPlayer mediaPlayer = MediaPlayer.create(this, _model.getCompletionAudioIndex());
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();

                // Go back to the main menu
                finish();
            }
        });
        mediaPlayer.start();
    }

    /**
     * Return To Menu
     * Retuns the user to the main menu activity
     * @param view that activated this action.
     */
    public void returnToMenu(View view) {
        finish();
    }
}
