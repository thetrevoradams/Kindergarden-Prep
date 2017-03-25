package com.example.cs246project.kindergartenprepapp;

import android.widget.TextView;

import java.util.List;

/**
 * An Activity that allows the user to trace letters in the alphabet, numbers, or words (including
 * their name). This is meant to help them improve their fine motor, writing, and recognition
 * skills.
 * <p>
 * @author  Dan Rix
 * @version 1.0
 * @since   2017-02-20
 */

public class LetterTraceActivity extends CharacterTraceActivity {

    protected void initializeModel() {
        _model = new LetterTraceModel(this);
    }

    protected void initializeLayoutIndex() {
        setContentView(R.layout.character_traceable_activity);
    }

    protected void initializeDrawView() {
        _drawView = (DrawView) findViewById(R.id.drawView);
    }

    protected void setTraceBackgroundFromValues(List<String> values) {
        TextView myTextView=(TextView)findViewById(R.id.textView);
        myTextView.setText(values.get(0) + values.get(1));
    }
}
