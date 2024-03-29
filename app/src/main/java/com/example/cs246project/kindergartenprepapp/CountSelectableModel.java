package com.example.cs246project.kindergartenprepapp;

import android.content.Context;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * <p>
 * @author  Michael Lucero
 * @version 1.0
 * @since   2017-03-08
 */

/**
 * COUNTSELECTABLEMODEL Class will build a list of random values used with the count selectable
 * activities. Subclass of Selectable Model
 */
public class CountSelectableModel extends SelectableModel {

    private Boolean _isFirstTime;

    // static resource for word activity instruction
    private static final String _activityInstructions =
            "instruct_count_pictures_and_find_number_that_matches";

    // for changing up the motivational messages
    static final List<String> correct = new ArrayList<String>(){{
        add("motivate_great_job");
        add("motivate_you_did_it");
        add("motivate_way_to_go");
        add("motivate_you_are_awesome");
        add("motivate_stupendous");
        add("motivate_you_found_the_number");
    }};

    // for changing up the motivational messages
    static final List<String> incorrect = new ArrayList<String>(){{
        add("motivate_try_again");
        add("motivate_dont_give_up");
        add("motivate_keep_practicing");
        add("motivate_give_it_another_try");
        add("motivate_not_quite");
    }};

    /************ Constructors *************/

    /**
     * @param optionCount how many values are need to be generated
     */
    public CountSelectableModel(Context context, int optionCount) {
        super(context);
        if((optionCount > 0) && (optionCount <= 10)) {
            _optionCount = optionCount;
            _isActivityDone = false;
            _isFirstTime = true;
        }
        else {
        }

        // initialize question bank
        buildInitialQuestionAnswerBanks();
    }

    /************** METHODS ***************/

    /**
     *  Gets the instruction audio resource index for the word selectable activity.
     *  @return The audio resource index for the activity instruction.
     */
    public int getActivityInstructionsIndex() {

        return _context.getResources().getIdentifier(_activityInstructions, "raw", _context.getPackageName());
    }

    /**
     *  BUILDINITIALQUESTIONBANK method to build values that can be randomly pulled from
     */
    protected void buildInitialQuestionAnswerBanks() {
        _answerBank  = new ArrayList<>();
        _questionBank = new ArrayList<>();

        for (int i = 0 ; i <= 10; i++) {
            _questionBank.add(i);
            _answerBank.add(i);
        }
    }

    /**
     * GETANSWERRESOURCEINDEX will get the answer for the activity as a resource index
     */
    public int getAnswerResourceIndex() {
        int resourceIndex = _context.getResources().getIdentifier("count_" + _answer, "drawable", _context.getPackageName());
        return resourceIndex;
    }

    /**
     * GENERATEVALUELIST will build a set of indexes required for retrieving audio and image files
     */
    @Override
    public List<MediaModel> generateValueList() {

        List<Integer> randomValues;
        List<MediaModel> results = new ArrayList<>();

        // make sure the activity is not over because all values have been selected correctly
        //    otherwise would result in endless loop in random activity
        if (!_isActivityDone) {
            // get random values with is parameters
            randomValues = randomValuesGenerator();
        }
        else {
            return null;
        }

        // shuffle list to make is random
        Collections.shuffle(randomValues);

        // Using the random values now associate the images and sounds to buttons to be used
        //    by the calling activity
        for (Integer value : randomValues) {

            // set the picture to match the number
            int imageFileResourceIndex = _context.getResources().getIdentifier("number_wide_" + String.valueOf(value), "drawable", _context.getPackageName());
            List<Integer> audioFileResourceIndexes = new ArrayList<>();

            // number name
            int audioFileResourceIndex3 = _context.getResources().getIdentifier("number_" + String.valueOf(value), "raw", _context.getPackageName());
            audioFileResourceIndexes.add(audioFileResourceIndex3);

            // used to make the motivations different each time
            Collections.shuffle(correct);

            // used to make the motivations different each time
            Collections.shuffle(incorrect);

            // correct answer
            if (value == _answer) {
                int audioFileResourceIndex2 = _context.getResources().getIdentifier(correct.get(0), "raw", _context.getPackageName());
                audioFileResourceIndexes.add(audioFileResourceIndex2);
            } else { // incorrect
                int audioFileResourceIndex2 = _context.getResources().getIdentifier(incorrect.get(0), "raw", _context.getPackageName());
                audioFileResourceIndexes.add(audioFileResourceIndex2);
            }

            // retrieve and associate buttons with image and audio
            MediaModel<Integer> mediaModel = new MediaModel<>(imageFileResourceIndex, audioFileResourceIndexes, value);
            results.add(mediaModel);
        }

        return results;
    }

    /**
     * Generate an array of random values to be used for the buttons.
     * Values are unique that are not used again if already answered.
     * Also ensures that 0 is never the first value to be shown.
     * @return set random random values to be used for media association and buttons.
     */
    @Override
    protected List<Integer> randomValuesGenerator() {

        List<Integer> valueList = new ArrayList<>();
        Random randomValueRetriever = new Random();

        // ensure that 0 is not the first number to appear
        do {
            // get random first value with conditions based on available answer bank questions
            _answer = _answerBank.get(randomValueRetriever.nextInt(_answerBank.size()));
        } while ((_answer.equals(0)) && (_isFirstTime));

        _isFirstTime = false;

        // add the initial random value to list to start
        valueList.add((Integer) _answer);

        // generate the rest of the buttons with random values that don't match button 1st
        //    button made
        while (valueList.size() < _optionCount) {

            // get random value
            Integer randomNum = (Integer) _questionBank.get(randomValueRetriever.nextInt(_questionBank.size()));

            // check if number already added, not added if -1 is returned
            if (valueList.indexOf(randomNum) == -1)
                valueList.add(randomNum);
        }

        return valueList;
    }
}