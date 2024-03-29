package com.example.cs246project.kindergartenprepapp;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * The model class associated with a letter tracing activity which handles all of the logic of
 * building the image resource files and tracking values.
 * <p>
 * @author  Dan Rix
 * @version 1.0
 * @since   2017-03-09
 */

public class LetterTraceableModel extends CharacterTraceableModel {

    public LetterTraceableModel(Context context) {
        super(context);
    }

    /**
     * {@inheritDoc}
     * Initializes _values with "a" - "z"
     */
    @Override
    protected void generateValueBank() {
        // Instantiate _values
        _valueBank = new ArrayList<String>();

        // Fill values with a - z using ascii values (97 = "a")
        for (int i = 97; i < (97 + 26); i++) {
            _valueBank.add((Character.valueOf((char) i)).toString());
        }

        super.generateValueBank();
    }

    /**
     * {@inheritDoc}
     * Get Current Values
     * Gets the current filename value(s) from _values in upper and lower case forms;
     * @return a list of values (1 or more).
     */
    @Override
    public List<Integer> getCurrentValues() {
        List<Integer> values = new ArrayList<>();

        int upperLetter = _context.getResources().getIdentifier("upper_" + _valueBank.get(_currentValueIndex), "drawable", _context.getPackageName());
        int lowerLetter = _context.getResources().getIdentifier("lower_" + _valueBank.get(_currentValueIndex), "drawable", _context.getPackageName());

        values.add(upperLetter);
        values.add(lowerLetter);

        return values;
    }

    /**
     * {@inheritDoc}
     * Gets the file name fo the instructions audio raw file
     * @return the file name of the instructions audio file
     */
    protected String getInstructionsFileName() {
        return "instruct_trace_letter_with_finger";
    }

    public int getCurrentValueAudioResourceIndex() {
        String audioFileName = _valueBank.get(_currentValueIndex);
        return _context.getResources().getIdentifier(audioFileName, "raw" , _context.getPackageName());
    }
}
