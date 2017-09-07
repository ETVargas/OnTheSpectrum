package com.ots.tdd.onthespectrum;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import java.sql.SQLOutput;

import static android.support.constraint.R.id.parent;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "Settings Activity";
    int fontSize = 12;
    boolean locked = false;
    boolean wantTo = false;
    String password = "";
    String currentHint = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //spinner stuff for font size
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.fontSizeAttempt, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View selectedItemView, int pos, long id){
                String fontString = parent.getItemAtPosition(pos).toString();
                int fontSize = Integer.parseInt(fontString);
                System.out.println(fontSize);
                TextView a = (TextView) findViewById(R.id.AdjustColorText);
                a.setTextSize(fontSize);
                a =(TextView) findViewById(R.id.FontSizeText);
                a.setTextSize(fontSize);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }
        });

        //switch stuff for profile lock
        final Switch switched = (Switch) findViewById(R.id.lock);
        switched.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                switched.toggle();
                if (isChecked) {

                    Log.i(TAG, "checked");
                    wantTo = true;
                    EditText passwordEditText = (EditText) findViewById(R.id.password);
                    if (password.equals("")) {
                        passwordEditText.setHint("Enter new password and press OK");
                    } else {
                        passwordEditText.setHint("Enter password to change");
                    }

                } else {
                    Log.i(TAG, "unchecked");
                    wantTo = false;
//                    locked = false;
                    EditText passwordEditText = (EditText) findViewById(R.id.password);
                    passwordEditText.setHint("Enter password to change");
                }

                switched.setChecked(locked); //added to prevent it from doing it unnecessary
            }
        });

        //okay button
        Button button = (Button) findViewById(R.id.okButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                EditText passwordEditText = (EditText) findViewById(R.id.password);
                if (password.equals("")) { // meaning password not yet set
//                    System.out.println("setting password");
                    Log.i(TAG, "setting password");
                    password = passwordEditText.getText().toString();
                    locked = true;

                } else {
                    if (passwordEditText.getText().toString().equals(password)) {
                        Log.i(TAG, "password correct");
                        locked = wantTo;

                    } else { //wrong password don't set
                        Log.i(TAG, "password incorrect");
                        locked = !wantTo;
                    }

                    //then
//                    if (locked) { //= true
//                    }
                }
                //then
                System.out.println("wantTo: " + wantTo + " switch: " + switched.isChecked());
                switched.setChecked(locked);
                System.out.println("locked: " + locked + " switch: " + switched.isChecked());
                passwordEditText.setText("");
                passwordEditText.setHint("");
            }
        });
    }

    public void onOkayPress() {
        System.out.println("okay pressed");

    }
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        String fontString = parent.getItemAtPosition(pos).toString();

        int fontSize = Integer.parseInt(fontString);
        System.out.println(fontSize);
        TextView a = (TextView) findViewById(R.id.AdjustColorText);
        a.setTextSize(fontSize);
        a = (TextView) findViewById(R.id.FontSizeText);
        a.setTextSize(fontSize);
    }
}
