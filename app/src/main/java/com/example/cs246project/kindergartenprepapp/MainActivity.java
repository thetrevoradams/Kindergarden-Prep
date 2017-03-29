package com.example.cs246project.kindergartenprepapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * The Login Activity where the user will enter their name to be used in the app's "activities".
 * <p>
 * @author  Micheal Lucero, Trevor Adams, & Dan Rix
 * @version 1.0
 * @since   2017-02-20
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // For hiding status the bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        SharedPreferences settings = this.getSharedPreferences(AppConstants.sharePreferenceSettings, MODE_PRIVATE);
        String firstname = settings.getString(AppConstants.sharePreferenceFirstName, "");
        String lastname = settings.getString(AppConstants.sharePreferenceLastName, "");

        // Retrieve name
        EditText firstNameField = (EditText) findViewById(R.id.playerFirstName);
        firstNameField.setText(firstname);
        EditText lastNameField = (EditText) findViewById(R.id.playerLastName);
        lastNameField.setText(lastname);


    }

    public void goToMainMenu(View view) {
        SharedPreferences settings = this.getSharedPreferences(AppConstants.sharePreferenceSettings, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        // get value from the text field
        EditText firstNameField = (EditText) findViewById(R.id.playerFirstName);
        EditText lastNameField = (EditText) findViewById(R.id.playerLastName);

        // Check if a first name value was entered
        if (firstNameField.getText().length() == 0) {
            editor.putString(AppConstants.sharePreferenceFirstName, "SkipTap");
        } else {
            // will save the name value
            editor.putString(AppConstants.sharePreferenceFirstName, firstNameField.getText().toString());
        }

        editor.putString(AppConstants.sharePreferenceLastName, lastNameField.getText().toString());
        editor.commit();

        // create intent to start menu activity
        Intent intent = new Intent(getBaseContext(), MenuActivity.class);
        startActivity(intent);
    }

    @Override
    public void onUserInteraction() {

        // For hiding status the bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        super.onUserInteraction();
    }

    @Override
    protected void onResume() {

        // For hiding status the bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        super.onResume();
    }

    @Override
    protected void onRestart() {

        // For hiding status the bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        super.onRestart();
    }
}
