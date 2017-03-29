package com.example.cs246project.kindergartenprepapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * An Activity that allows the user to trace letters in the alphabet, numbers, or words (including
 * their name). This is meant to help them improve their fine motor, writing, and recognition
 * skills.
 * <p>
 * @author  Dan Rix
 * @version 1.0
 * @since   2017-02-20
 */

public class SightWordTraceableActivity extends SkipTapActivity {

    // FrameLayout that holds the drawView and trace background.
    FrameLayout _frameLayout;

    // Model object for managing the name character values.
    private SightWordTraceableModel _model;

    // The view control that allows the user to trace on a transparent canvas.
    private DrawView _drawView;

    // Width of the characters in pixels.
    private static int _characterWidth = AppConstants.characterTracingImageWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _model = new SightWordTraceableModel(this);

        // Set the content view layout
        setContentView(R.layout.sight_word_traceable_activity);

        _frameLayout = (FrameLayout) findViewById(R.id.frameLayout);

        // Set the _drawView
        _drawView = (DrawView) findViewById(R.id.drawView);

        // Build the background images from the traceCharacters
        setTraceBackgroundFromValues();

        playInstructions(getResources().getIdentifier(_model.getInstructionsFileName(), "raw", getPackageName()));

        // Hide the previous button since the activity is at the start
        FloatingActionButton previousButton = (FloatingActionButton) findViewById(R.id.btnPrevious);
        previousButton.setVisibility(View.INVISIBLE);

        // Hide button if the activity isn't already complete
        FloatingActionButton doneButton = (FloatingActionButton) findViewById(R.id.btnDone);
        doneButton.setVisibility(View.INVISIBLE);
    }

    /**
     * Set Trace Background From Values
     * Sets the background trace images using a list of string values (file names).
     */
    private void setTraceBackgroundFromValues() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.letterLayout);
        layout.removeAllViews();

        for (int i = 0; i < _model.getCurrentValues().size(); i++) {
            AppCompatImageView imageView = new AppCompatImageView(this);
            imageView.setImageResource(_model.getCurrentValues().get(i));
            LinearLayout.LayoutParams layoutParams = new AppBarLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(layoutParams);
            imageView.setAlpha(0.5f);
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setAdjustViewBounds(true);

            layout.addView(imageView);
        }

        //layout.getLayoutParams().width = _model.getNumberOfCharacters() * _characterWidth;
    }

    /**
     * Go Back To Next Value
     * Displays the next value (un-traced), if any.
     */
    public void goToNextValue(View view) {
        if (!_model.isComplete()) {
            _model.goToNextValue();
            clearAllTracings(view);
            setTraceBackgroundFromValues();
            //playCurrentValueAudio();
            FloatingActionButton previousButton = (FloatingActionButton) findViewById(R.id.btnPrevious);
            if (previousButton.getVisibility() != View.VISIBLE) {
                previousButton.setVisibility(View.VISIBLE);
                Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation);
                previousButton.startAnimation(fadeIn);
            }
        }

        if (_model.isComplete()) {
            // Hide the next button since its at the end
            FloatingActionButton nextButton = (FloatingActionButton) findViewById(R.id.btnNext);
            nextButton.setVisibility(View.INVISIBLE);

            // Show the done button
            FloatingActionButton doneButton = (FloatingActionButton) findViewById(R.id.btnDone);
            doneButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Go Back To Previous Value
     * Displays the previous value (un-traced), if any.
     */
    public void goToPreviousValue(View view) {
        if (!_model.isAtBeginning()) {
            _model.goToPreviousValue();
            clearAllTracings(view);
            setTraceBackgroundFromValues();
            //playCurrentValueAudio();
            FloatingActionButton nextButton = (FloatingActionButton) findViewById(R.id.btnNext);
            if (nextButton.getVisibility() != View.VISIBLE) {
                nextButton.setVisibility(View.VISIBLE);
                Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation);
                nextButton.startAnimation(fadeIn);
            }
        }

        if (_model.isAtBeginning()){
            FloatingActionButton previousButton = (FloatingActionButton) findViewById(R.id.btnPrevious);
            previousButton.setVisibility(View.INVISIBLE);
        }

        // Hide the done button if not already hidden
        FloatingActionButton doneButton = (FloatingActionButton) findViewById(R.id.btnDone);
        if (doneButton.getVisibility() == View.VISIBLE && !_model.isComplete()) {
            doneButton.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * Clear Last Tracing
     * Clears the last tracing by the user from the _drawView.
     * @param view the button that caused this action to be called
     */
    public void clearLastTracing(View view) {
        _drawView.clearPreviousPath();
    }

    /**
     * Clear All Tracings
     * Clears all the tracings by the user from the _drawView.
     * @param view the button that caused this action to be called
     */
    public void clearAllTracings(View view) {
        _drawView.clearAllPaths();
    }

    /**
     * On Done Button Click
     * Runs the the actionst that should take place when a user is done with tracing their name.
     * @param view that activated this action.
     */
    public void onDoneButtonClick(View view) {
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

