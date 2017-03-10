package com.example.cs246project.kindergartenprepapp;

import java.util.ArrayList;
import java.util.List;

/**
 * The model class associated with a name tracing activity which handles all of the logic of
 * building the image resource files and tracking values.
 * <p>
 * @author  Dan Rix
 * @version 1.0
 * @since   2017-03-09
 */

public class NameTraceModel {

    protected List<String> _valueBank;

    /**
     * Non-Default Constructor
     */
    public NameTraceModel(String name) {
        _valueBank = new ArrayList<String>();

        for(int i = 0; i < name.length(); i++){
            Character character = name.charAt(i);
            _valueBank.add(character.toString());
        }
    }

    public List<String> getValues() {
        List<String> values = new ArrayList<String>();

        for (int i = 0; i < _valueBank.size(); i++) {
            if (i == 0) {
                values.add("upper_" + _valueBank.get(i).toLowerCase());
            } else {
                values.add("lower_" + _valueBank.get(i));
            }
        }

        return values;
    }

    public int getNumberOfCharacters() {
        return _valueBank.size();
    }

}