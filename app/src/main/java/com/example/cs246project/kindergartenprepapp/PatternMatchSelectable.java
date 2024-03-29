package com.example.cs246project.kindergartenprepapp;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;


/**
 * Handles pattern recognition and completion
 * @author Trevor Adams
 * */
public class PatternMatchSelectable extends SelectableActivity implements View.OnTouchListener, MediaButtonHandler {

    // Create a new Array list that will hold the filenames to reference
    private PatternMatchSelectableModel _model;

    // Find the horizontal scroll view
    private LinearLayout layout_pattern;
    private LinearLayout layout_top_pattern;
    private ProgressBar _progBar;

    private int count = 0;
    private int position = 0;
    private Boolean _isCorrect;
    private boolean isFirstTime = true;
    private int _totalPatternCount = 0;
    private int _patternCount = 0;
    private int _indexNum = 0;
    private int numRounds = 9;
    private ArrayList<Integer> _patternIndexes;
    private Boolean _isResuming = false;


    private MediaPlayer _soundsOfPatternMediaPlayer;
    private MediaPlayer _answerMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pattern_match);
        layout_pattern = (LinearLayout) findViewById(R.id.layout_pattern);
        layout_top_pattern = (LinearLayout) findViewById(R.id.layout_top_pattern);
        _model = new PatternMatchSelectableModel(this, numRounds);
        _progBar = (ProgressBar) findViewById(R.id.patternProgressBar);
        _progBar.setMax(numRounds);
        _progBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(AppConstants.progressBarColor)));

        viewSetUp();

        // Disable the buttons
        enablePatternButtons(false);
        disableQuestionButtons(true);

        // During play instructions will show toast for the duration of the instructions
        //    and then stop when audio is over
        playInstructions(_model.getActivityInstructionsIndex());
    }

    /**
     * Handles triggering of audio and locking of buttons
     * */
    private void playPatternSound() {
        enablePatternButtons(false);
        disableQuestionButtons(true);
        _totalPatternCount = _model.getShownPatternLength();

        // Update _patternCount to the new total count
        _patternCount = _totalPatternCount;

        playPrePatternAudio();
    }

    /**
     * Creates/handles audio before pattern audio is played
     * */
    private void playPrePatternAudio() {
        if (_indexNum <= (_model.getShownPatternLength() - 1)) {
            int index = getResources().getIdentifier("instruct_the_pattern_is", "raw", getPackageName());
            _soundsOfPatternMediaPlayer = MediaPlayer.create(this, index);

            // Release pattern image audio after it is no longer playing
            _soundsOfPatternMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    mp = null;
                    _soundsOfPatternMediaPlayer = null;
                    patternSoundPlayer();
                }
            });

            // Play the audio
            _soundsOfPatternMediaPlayer.start();
        }
    }

    /**
     * Plays pattern audio file
     * */
    private void patternSoundPlayer() {
        if (_indexNum <= (_model.getShownPatternLength() - 1)) {
            _soundsOfPatternMediaPlayer = MediaPlayer.create(this, _patternIndexes.get(_indexNum));

            // Release pattern image audio after it is no longer playing
            _soundsOfPatternMediaPlayer.setOnCompletionListener(onPatternComplete);

            // Play the audio
            _soundsOfPatternMediaPlayer.start();
        }
    }

    /**
     * Completion listener creates loop for the number of items in the pattern
     * */
    MediaPlayer.OnCompletionListener onPatternComplete = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mp.release();
            mp = null;
            _soundsOfPatternMediaPlayer = null;
            if (_patternCount > 1) {
                _patternCount -= 1;
                _indexNum += 1;
                patternSoundPlayer();
            } else {
                _indexNum = 0;
                enablePatternButtons(true);
                disableQuestionButtons(false);
            }

        }
    };

    public void viewSetUp() {
        /**
         * This will loop through the generatedValueList based on the length of the array
         * It will then get the index of each file in the drawable directory,
         * then it will update the image for each button.
         * */
        for (MediaModel item : _model.generateValueList()) {
            final MediaButton btn = new MediaButton(this, item, this);
            btn.setScaleType(ImageView.ScaleType.FIT_XY);
            btn.setAdjustViewBounds(true);
            btn.setPadding(5, 5, 5, 5);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(5, 5, 30, 5);
            btn.setLayoutParams(lp);

            btn.setElevation(10);
            btn.setBackgroundColor(Color.TRANSPARENT);

            layout_pattern.addView(btn);
        }

        /**
         * This will loop through the patten length to set the pattern shown on top
         * */
        // Loop for setting up answers
        for (int item = 0; item < _model.getCurrentPatternLength(); item++) {
            // Add letter options on Top
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams lp = (new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
            lp.setMargins(5, 5, 10, 5);
            imageView.setLayoutParams(lp);
            imageView.setPadding(6, 0, 6, 0);
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setAdjustViewBounds(true);
            imageView.setImageResource(R.drawable.underline);
            imageView.setId(count);

            if (count == 0) {
                for (Integer patternIndex : _model.getPatternImageAnswerResourceIndex()) {
                    ImageView imagePatternView = new ImageView(this);
                    LinearLayout.LayoutParams lpBtn = (new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    lpBtn.setMargins(5, 5, 10, 5);
                    imagePatternView.setLayoutParams(lpBtn);

                    imagePatternView.setPadding(6, 0, 6, 0);
                    imagePatternView.setScaleType(ImageView.ScaleType.CENTER);
                    imagePatternView.setAdjustViewBounds(true);
                    imagePatternView.setBackgroundColor(Color.TRANSPARENT);
                    imagePatternView.setImageResource(patternIndex);

                    /**
                     * Setup event listener for pattern image
                     * */
                    imagePatternView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                playPatternSound();
                            }
                            return false;
                        }
                    });

                    layout_top_pattern.addView(imagePatternView);
                }

                imageView.setImageResource(R.drawable.underline);
                Drawable res = this.getResources().getDrawable(R.drawable.button_border, getTheme());
                imageView.setBackground(res);
            }
            count++;
            layout_top_pattern.addView(imageView);
        }

        _patternIndexes = (ArrayList) _model.getPatternAudioAnswerResourceIndex();

        if (!isFirstTime && !_isResuming) {
            playPatternSound();
        }
        _isResuming = false;
    }

    @Override
    public void onMediaButtonTouched(MediaButton mediaButton, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // Disable the buttons
            disableQuestionButtons(true);

            // checking for the correct value dynamically as button moves along
            _isCorrect = _model.isCorrectOrder((String) mediaButton.getValue());
            if(_isCorrect) {
                // Show answer toast
                displayToast(true);

                ImageView patternTopLine = (ImageView) findViewById(position);
                patternTopLine.setBackgroundResource(0);

                // Progress name position indicator
                if (position < (count - 1)) {
                    ImageView nextLetterSpot = (ImageView) findViewById(position + 1);
                    Drawable drawRes = getResources().getDrawable(R.drawable.button_border, getTheme());
                    nextLetterSpot.setImageDrawable(drawRes);

                    Animation fadeIn = AnimationUtils.loadAnimation(PatternMatchSelectable.this, R.anim.fade_in_animation);
                    nextLetterSpot.startAnimation(fadeIn);

                }
                // Get the letter index for the letter of the name selected
                int resourceIndex = getResources().getIdentifier(mediaButton.getValue().toString(), "drawable", getPackageName());
                patternTopLine.setImageResource(resourceIndex);

                position++;
            } else {
                // Show answer toast
                displayToast(false);
            }
        }
    }

    /**
     * Will disable or enable the pattern buttons
     * */
    private void enablePatternButtons(Boolean state){
        for (int i = 0; i < layout_top_pattern.getChildCount(); i++) {
            ImageView imagePatternView = (ImageView) layout_top_pattern.getChildAt(i);
            imagePatternView.setClickable(state);
            imagePatternView.setEnabled(state);
        }
    }

    /**
     * Will disable or enable the layout buttons
     * */
    private void disableQuestionButtons(Boolean state){
        for (int i = 0; i < layout_pattern.getChildCount(); i++) {
            MediaButton mediaButton = (MediaButton) layout_pattern.getChildAt(i);
            mediaButton.setIsDisabled(state);
        }
    }

    /**
     * Called when a round is over, to prepare views for next round
     * */
    private void resetActivity() {
        layout_top_pattern.removeAllViews();
        layout_pattern.removeAllViews();
        position = 0;
        count = 0;
        viewSetUp();

        // disable buttons
        enablePatternButtons(false);
        disableQuestionButtons(true);
    }

    private void playResultAudio() {
        // Enable the buttons when sound is complete
        int audioAnswerIndex = _model.getAnswerAudioIndex(_isCorrect);
        _answerMediaPlayer = MediaPlayer.create(this, audioAnswerIndex);
        _answerMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // cancel toast after audio
                if (_toast != null) {
                    _toast.cancel();
                }

                if (mp != null) {
                    mp.release();
                    mp = null;
                }

                if (!_model._isActivityDone && _model.isPatternQuestionsDone()){
                    _progBar.incrementProgressBy(1);
                    resetActivity();
                } else {
                    enablePatternButtons(true);
                    disableQuestionButtons(false);
                }

            }
        });
        _answerMediaPlayer.start();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    /**
     * Called when audio has completed (for media buttons only)
     * */
    @Override
    public void onAudioComplete() {

        if (_model._isActivityDone) {
            // Checking for if the activity is done
            this.finish();
        } else if (!_model._isActivityDone && _model.isPatternQuestionsDone()){
            playResultAudio();
        } else if (!_model._isActivityDone && !_model.isPatternQuestionsDone()){
            playResultAudio();
        }
    }

    /**
     * Called when audio has completed (for instructions)
     * */
    @Override
    public void onInstructionsAudioComplete() {

        // stop the instructions toast when done
        if (_toast != null) {
            _toast.cancel();
        }

        // Enable the buttons when sound is complete
        enablePatternButtons(true);
        disableQuestionButtons(false);
        playPatternSound();
        isFirstTime = false;
    }

    public void returnToMenu(View view) {
        if (_toast != null) {
            _toast.cancel();
        }

        this.finish();
    }

    /**
     * Overrides from skiptap activity and calls functions to handle all closing activities like
     * stopping audio for media players and existing toasts
     */
    @Override
    public void stopEverything() {

        // cancel any toasts that are still showing; done in override
        // leaving app
        if (_toast != null) {
            _toast.cancel();
        }
        // stop all audio that is playing
        stopAudio();
    }

    /**
     * Handles stopping audio the buttons might be playing when transitioning to other activities
     * or moving to another app.
     */
    @Override
    public void stopAudio() {

        // name button audio release
        for (int i = 0; i < layout_pattern.getChildCount(); i++) {
            MediaButton mediaButton = (MediaButton) layout_pattern.getChildAt(i);
            mediaButton.stopAudio();
        }

        // if leaving activity release audio for saying name back to you
        if(_soundsOfPatternMediaPlayer != null ) {
            _soundsOfPatternMediaPlayer.release();
            _soundsOfPatternMediaPlayer = null;
        }

        // release audio for answer
        if(_answerMediaPlayer != null ) {
            _answerMediaPlayer.release();
            _answerMediaPlayer = null;
        }

        super.stopAudio();
    }

    /**
     * When activity resumes make sure the the image button and question buttons are disabled.
     */
    @Override
    public void startAudio() {

        enablePatternButtons(false);
        disableQuestionButtons(true);

        playInstructions(_instructionsAudioResourceIndex);

        _backgroundAudioModel.startBackgroundAudio(this);
    }

    /**
     * Helps manage custom onResume actions to check if pattern is done
     * */
    @Override
    protected void onResume() {

        if(_model.isPatternQuestionsDone()) {
            _isResuming = true;
            resetActivity();
        }

        super.onResume();
    }

}

