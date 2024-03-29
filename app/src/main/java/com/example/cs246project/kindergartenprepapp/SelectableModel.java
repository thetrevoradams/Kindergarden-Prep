package com.example.cs246project.kindergartenprepapp;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * <p>
 * @author  Michael Lucero
 * @version 1.0
 * @since   2017-03-09
 */

/**
 * Class will build a list of random values used with the selectable activities.
 * The class will create the specified number of values of which will have the following
 * properties: they are unique with no duplicates, options are reduced as the user gets a
 * question right, The first generated value is assigned to be the answer. The class will
 * have a function that will return a list of value back.
 * @param <T> will support additional types per activity called.
 */
abstract class SelectableModel<T> extends Application {

    /* MEMBER VARIABLES */

    public Context _context;
    protected List<T> _questionBank;
    protected List<T> _answerBank;
    protected T _answer;
    //protected T _answerIndex;
    protected Boolean _isActivityDone;
    protected int _optionCount;

    protected Toast _toast;
    private static final String TAG = "SelectableModel";
    private ArrayList myColors = new ArrayList(Arrays.asList("#B1D5E5", "#B6D9D6", "#FAE0B8", "#CFE9E5", "#F3D6C8", "#D1C8BE", "#EEC9D7", "#FDF9CD", "#F3E0C4", "#CCD7E4"));
    private Random r;
    private int randomNum;

    /* CONSTRUCTOR */

    public SelectableModel (Context context){
        _context = context;
        _isActivityDone = false;
        Collections.shuffle(myColors);
        // Set up random number for color of buttons
        r = new Random();
        randomNum = r.nextInt((10 - 1) + 1) + 1;
    }

    /* METHODS */

    /**
     * Tells if the value is the correct answer and update the question bank if
     * already correct
     * @param value what has been selected by the user
     * @return Compares the answer with value selected to see if correct
     */
    public Boolean isCorrect(T value) {

        if(!_isActivityDone) {
            if (value == _answer) {
                // find value in list and then remove it from the possibilities
                _answerBank.remove(value);
            } else {
            }
        }

        // check if all questions have now been answered
        if (_answerBank.size() == 0) {
            _isActivityDone = true;
        }
        return (value == _answer);
    }

    /**
     * Tells if the activity is over and all values in the question bank have been used
     */
    public Boolean isActivityDone() { return _isActivityDone; }

    /**
     * Tells if the activity is over
     * @return question bank current size
     */
    public int getProgress() {
        return _answerBank.size();
    }


    public String getBtnColor() {
        if (randomNum % 10 == 0) {
            randomNum = 1;
        } else {
            randomNum++;
        }
        return (String) myColors.get(randomNum-1);
    }


    /**
     * abstract requires building of a question bank
     */
    abstract protected void buildInitialQuestionAnswerBanks();

    /**
     * abstract Build a set of indexes required for retrieving audio and image files
     * Functions as follows:
     */
    abstract public List<T> generateValueList();

    /**
     * Generate an array of random values to be used for the buttons.
     * Values are unique that are not used again if already answered.
     * @return set random random values to be used for media association and buttons
     */
    protected List<T> randomValuesGenerator() {

        List<T> valueList = new ArrayList<>();
        Random randomValueRetriever = new Random();

        // get random first value with conditions based on available answer bank questions
        _answer = _answerBank.get(randomValueRetriever.nextInt(_answerBank.size()));

        // add the initial random value to list to start
        valueList.add(_answer);

        // generate the rest of the buttons with random values that don't match button 1st
        //    button made
        while (valueList.size() < _optionCount) {

            // get random value
            T randomNum = _questionBank.get(randomValueRetriever.nextInt(_questionBank.size()));

            // check if number already added, not added if -1 is returned
            if (valueList.indexOf(randomNum) == -1)
                valueList.add(randomNum);
        }

        return valueList;
    }

    public String getAnswer() {
        return  String.valueOf(_answer);
    }

    public int getNameLength() {
        return  _optionCount;
    }
}
