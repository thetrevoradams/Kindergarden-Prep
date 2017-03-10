package com.example.cs246project.kindergartenprepapp;

import android.content.Context;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * <p>
 * @author  Michael Lucero
 * @version 1.0
 * @since   2017-03-08
 */

/**
 * WORDSELECTABLEMODEL Class will build a list of random values used with the word selectable
 * activity. Subclass of Selectable Model
 */
public class WordSelectableModel extends SelectableModel {

    /************ Constructors *************/

    /**
     * @param optionCount how many values are need to be generated
     */
    public WordSelectableModel(Context context, int optionCount) {
        super(context);
        if((optionCount > 0) && (optionCount <= 26)) {
            _optionCount = optionCount;
            _isActivityDone = false;
        } else {
            Log.i(TAG, "CountSelectableModel: 0 < option count <= 26; out of possible range");
        }

        // initialize question bank
        buildInitialQuestionAnswerBanks();
    }

    /************** METHODS ***************/

    /**
     *  BUILDINITIALQUESTIONBANK method to build values that can be randomly pulled from
     */
    protected void buildInitialQuestionAnswerBanks() {
        _answerBank  = new ArrayList<>();
        _questionBank = new ArrayList<>();

        for (char i = 'a' ; i <= 'z'; i++) {
            _questionBank.add(Character.toString(i));
            _answerBank.add(Character.toString(i));
        }
    }


    /**
     * GENERATEVALUELIST will build a set of indexes required for retrieving audio and image files
     */
    public List<MediaModel> generateValueList() {

        List<String> randomValues = new ArrayList<>();
        List<MediaModel> results = new ArrayList<>();

        // make sure the activity is not over because all values have been selected correctly
        //    otherwise would result in endless loop in random activity
        if (!_isActivityDone) {
            // get random values with is parameters
            randomValues = randomValuesGenerator();
        }
        else {
            Log.w(TAG, "generateButtonList: able to generate random values " +
                    _answerBank.size() + " > 0");
            return null;
        }

        // shuffle list to make is random
        Collections.shuffle(randomValues);

        /////////////////////// Convert values to Filenames and associate files
        //List<MBMODEL> genValues (int Count)
        //MB Model <T>
        // -imgresource int
        // -audioresource List <int>
        // -value T

        for (String value : randomValues) {
            int imageFileResourceIndex = _context.getResources().getIdentifier("upper_" + value, "drawable", _context.getPackageName());
            List<Integer> audioFileResourceIndexes = new ArrayList<>();
            int audioFileResourceIndex1 = _context.getResources().getIdentifier(value, "raw", _context.getPackageName());
            audioFileResourceIndexes.add(audioFileResourceIndex1);

            // letter sound
            int audioFileResourceIndex3 = _context.getResources().getIdentifier("letter_sound_" + value, "raw", _context.getPackageName());
            audioFileResourceIndexes.add(audioFileResourceIndex3);

            // correct answer
            if (value == _answer) {
                int audioFileResourceIndex2 = _context.getResources().getIdentifier("motivate_great_job", "raw", _context.getPackageName());
                audioFileResourceIndexes.add(audioFileResourceIndex2);
            } else { // incorrect
                int audioFileResourceIndex2 = _context.getResources().getIdentifier("upper", "raw", _context.getPackageName());
                audioFileResourceIndexes.add(audioFileResourceIndex2);
            }
            MediaModel<String> mediaModel = new MediaModel<>(imageFileResourceIndex, audioFileResourceIndexes, value);
            results.add(mediaModel);
        }

        return results;
    }
}