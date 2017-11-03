package com.ots.tdd.onthespectrum;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.SQLOutput;

import static android.support.constraint.R.id.parent;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "Settings Activity";
    int fontChange;
    boolean locked = false;
    boolean wantTo = false;
    String currentHint = "";
    TextView settingsText;
    TextView appearanceText;
    TextView profileLockText;
    TextView fontSizeText;
    TextView adjustColorText;

    SharedPreferences sharedPref;
  
    Switch lock = null;
    EditText passwordEditText = null;
    Button lockButton = null;
    static boolean isLocked = false;
    String password = "";


    /**
     * Loads the information for password/profile lock and sets up the lock, font size, and colors.
     * @param  savedInstanceState Bundle
     * @see android.app.Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = getApplicationContext().getSharedPreferences("OnTheSpectrum", Context.MODE_PRIVATE);
        int theme = sharedPref.getInt("colorTheme", R.style.AppTheme);
        setTheme(theme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
      
        settingsText = (TextView) findViewById(R.id.Settings);
        appearanceText = (TextView) findViewById(R.id.appearance);
        profileLockText = (TextView) findViewById(R.id.profileLockText);
        fontSizeText = (TextView) findViewById(R.id.FontSizeText);
        adjustColorText = (TextView) findViewById(R.id.AdjustColorText);

        //font size
        //default is fontChange of 0 unless specified in SharedPreferences
        RadioGroup fontRadios = (RadioGroup) findViewById(R.id.fontSizeChoice);

        int fontSharedPref = sharedPref.getInt("FontSizeChange", Integer.MAX_VALUE);
        if (fontSharedPref == Integer.MAX_VALUE) {
            fontChange = 0;
            ((RadioButton)fontRadios.getChildAt(1)).setChecked(true);
        } else {
            fontChange = fontSharedPref;
            if (fontChange < 0) {
                ((RadioButton)fontRadios.getChildAt(0)).setChecked(true);
            } else if (fontChange == 0) {
                ((RadioButton)fontRadios.getChildAt(1)).setChecked(true);
            } else {
                ((RadioButton)fontRadios.getChildAt(2)).setChecked(true);
            }
        }

        lock = (Switch) findViewById(R.id.lock);
        passwordEditText = (EditText) findViewById(R.id.password);
        lockButton = (Button) findViewById(R.id.lockButton);
        lock.setEnabled(false);
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("OnTheSpectrum", Context.MODE_PRIVATE);
        String prevPassword = sharedPref.getString("Lock", null);
        if (null == prevPassword) {
            updateSharedPreferences("");
            password = "";
            lockButton.setText("Lock");
            lock.setChecked(false);
            isLocked = false;
        } else {
            if (prevPassword.equals("")) {
                password = "";
                lockButton.setText("Lock");
                lock.setChecked(false);
                isLocked = false;
            } else {
                password = prevPassword;
                lockButton.setText("Unlock");
                lock.setChecked(true);
                isLocked = true;
            }
        }

        setTextSizes();

        //lock button
        lockButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (password.equals("")) { // meaning password not yet set
                    if (passwordEditText.getText().toString().equals("")) {
                        displayExceptionMessage("Provide a Password");
                    } else {
                        password = passwordEditText.getText().toString();
                        isLocked = true;
                        lockButton.setText("Unlock");
                        passwordEditText.setText("");
                        updateSharedPreferences(password);
                        displayExceptionMessage("Customization Locked");
                    }
                } else {
                    if (passwordEditText.getText().toString().equals(password)) {
                        isLocked = false;
                        lockButton.setText("Lock");
                        passwordEditText.setText("");
                        updateSharedPreferences("");
                        password = "";

                        displayExceptionMessage("Customization Unlocked");
                    } else { //wrong password don't set
                        passwordEditText.setText("");
                        isLocked = true;

                        displayExceptionMessage("Incorrect Password");
                    }

                }
                lock.setChecked(isLocked);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("FontSizeChange", fontChange);
        editor.commit();
    }
                                          
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?t
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        if (checked) {

            int theme = R.style.AppTheme;

            switch(view.getId()) {
                case R.id.smallRadio:
                    fontChange = -2;
                    theme = R.style.Theme4;
                    break;
                case R.id.mediumRadio:
                    fontChange = 0;
                    theme = R.style.Theme5;
                    break;
                case R.id.largeRadio:
                    fontChange = 4;
                    theme = R.style.Theme6;
                    break;
            }

            setTextSizes();

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("colorTheme", theme);
            editor.commit();
            this.recreate();
        }

    }

    private void setTextSizes() {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("OnTheSpectrum", Context.MODE_PRIVATE);
        int titleSize = sharedPref.getInt("TitleFontSize", 0);
        int subtitleSize = sharedPref.getInt("SubtitleFontSize", 0);
        int bodySize = sharedPref.getInt("BodyFontSize", 0);
        int fontChange = sharedPref.getInt("FontSizeChange", 0);
      
        settingsText.setTextSize(titleSize + fontChange);
        appearanceText.setTextSize(titleSize + fontChange);

        profileLockText.setTextSize(subtitleSize + fontChange);
        fontSizeText.setTextSize(subtitleSize + fontChange);
        adjustColorText.setTextSize(subtitleSize + fontChange);
      
        passwordEditText.setTextSize(bodySize + fontChange);
        lockButton.setTextSize(bodySize + fontChange);
    }

    /**
     * Updates the password saved on the device in SharedPreferences
     *
     * @param  password String of the newly saved password
     */
    public void updateSharedPreferences(String password) {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("OnTheSpectrum", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Lock", password);
        editor.commit();
    }


    /**
     * Displays the profile lock information change as a toast message ot the user.
     *
     * @param  msg String with information about the profile lock
     */
    public void displayExceptionMessage(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
