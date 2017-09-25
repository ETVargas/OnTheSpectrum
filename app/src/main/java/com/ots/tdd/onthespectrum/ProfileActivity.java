package com.ots.tdd.onthespectrum;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class ProfileActivity extends AppCompatActivity {
    ArrayAdapter<ProfileElement> adapter;
    EditText editInfoType;
    EditText editUserInfo;
    TextView alertTextView;
    int profileElementCounter;

    ArrayList<ProfileElement> itemList;
    ProfileElement[] test = {
            new ProfileElement("Name", "Testing Name", 0),
            new ProfileElement("Birth Date", "September 7, 2017", 1),
            new ProfileElement("Gender", "Female", 2),
    };

    protected void onCreate(Bundle savedInstanceState) {
        profileElementCounter = 3;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        itemList = new ArrayList<ProfileElement>(Arrays.asList(test));
        adapter=new ArrayAdapter<ProfileElement>(this, R.layout.profile_item, itemList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ProfileElement current = itemList.get(position);

                // Inflate only once
                if(convertView == null) {
                    convertView = getLayoutInflater()
                            .inflate(R.layout.profile_item, null, false);
                }

                int currProfileNumber = current.profileNumber;
                ProfileElementViewContainer currPEVC = null;
                currPEVC = ProfileElementViewContainer.findContainerUsingNumber(currProfileNumber);
                if (currPEVC == null) {
                    TextView infoType = (TextView) convertView.findViewById(R.id.infoType);
                    EditText userInfo = (EditText) convertView.findViewById(R.id.userInfo);
                    ImageView editInfo = (ImageView) convertView.findViewById(R.id.editInfo);
                    ImageView saveInfo = (ImageView) convertView.findViewById(R.id.saveInfo);
                    ImageView cancelInfo = (ImageView) convertView.findViewById(R.id.cancelInfo);
                    ProfileElementViewContainer newProfileElement = new ProfileElementViewContainer(
                            infoType, userInfo, editInfo, saveInfo, cancelInfo, currProfileNumber);
                    ProfileElementViewContainer.addPEVCToArray(newProfileElement);
                    editInfo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editInfo(v);
                        }
                    });
                    saveInfo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            saveInfo(v);
                        }
                    });
                    cancelInfo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancelInfo(v);
                        }
                    });
                    //userInfo.setFocusable(false);
                    //userInfo.setClickable(false);
                    userInfo.setEnabled(false);
                    //userInfo.setBackgroundColor(0x0106000d); //R.color.transparent
                    userInfo.getBackground().clearColorFilter();

                    editInfo.setVisibility(View.VISIBLE);
                    saveInfo.setVisibility(View.GONE);
                    cancelInfo.setVisibility(View.GONE);

                    // if this remains unused, may delete the ProfileTextWatcher class
                    //userInfo.addTextChangedListener(new ProfileTextWatcher(userInfo));

                    infoType.setText(current.infoType);
                    userInfo.setText(current.userInfo);
                } else {
                    TextView infoType = (TextView) convertView.findViewById(R.id.infoType);
                    EditText userInfo = (EditText) convertView.findViewById(R.id.userInfo);
                    ImageView editInfo = (ImageView) convertView.findViewById(R.id.editInfo);
                    ImageView saveInfo = (ImageView) convertView.findViewById(R.id.saveInfo);
                    ImageView cancelInfo = (ImageView) convertView.findViewById(R.id.cancelInfo);

                    infoType.setText(ProfileElementViewContainer.getTextView(currPEVC).getText());
                    userInfo.setText(ProfileElementViewContainer.getEditText(currPEVC).getText());
                    editInfo.setVisibility(ProfileElementViewContainer.getEdit(currPEVC).getVisibility());
                    saveInfo.setVisibility(ProfileElementViewContainer.getSave(currPEVC).getVisibility());
                    cancelInfo.setVisibility(ProfileElementViewContainer.getCancel(currPEVC).getVisibility());
                    if (ProfileElementViewContainer.getEdit(currPEVC).getVisibility() != View.GONE) {
                        userInfo.getBackground().clearColorFilter();
                        //set focusability here
                        //userInfo.setFocusable(true);
                        //userInfo.setClickable(true);
                        userInfo.setEnabled(false);
                    } else {
                        //userInfo.setFocusable(false);
                        //userInfo.setClickable(false);
                        userInfo.setEnabled(true);
                    }


                }



                return convertView;
            }
        };
        ListView listV=(ListView)findViewById(R.id.list);
        listV.setAdapter(adapter);
        //this is added in attempt to prevent the edittext from being refreshed
        listV.setItemsCanFocus(true);
        editInfoType=(EditText)findViewById(R.id.infoType);
        editUserInfo = (EditText)findViewById(R.id.userInfo);

        alertTextView = (TextView)findViewById(R.id.alert);
        Button btAdd=(Button)findViewById(R.id.btAdd);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newType = editInfoType.getText().toString();
                String newInfo = editUserInfo.getText().toString();

                if (!newType.isEmpty() && !newInfo.isEmpty()) {
                    alertTextView.setTextColor(Color.argb(0, 255, 64, 129));
                    // add new item to arraylist
                    itemList.add(new ProfileElement(newType, newInfo, profileElementCounter));
                    profileElementCounter++;
                    // notify listview of data changed
                    adapter.notifyDataSetChanged();

                    // resets the text boxes
                    editInfoType.setText("");
                    editUserInfo.setText("");
                } else {
                    alertTextView.setTextColor(Color.argb(255, 255, 64, 129));
                }
            }

        });

    }


    public void editInfo(View v) {
        ImageView currEditInfo = (ImageView) v;
        ProfileElementViewContainer pevc = ProfileElementViewContainer.
                findContainerUsingEdit(currEditInfo);
        ImageView currSaveInfo = ProfileElementViewContainer.getSave(pevc);
        ImageView currCancelInfo = ProfileElementViewContainer.getCancel(pevc);
        EditText currEditText = ProfileElementViewContainer.getEditText(pevc);

        //currEditText.setFocusableInTouchMode(true);
        //currEditText.setClickable(true);
        currEditText.setEnabled(true);
        ProfileElementViewContainer.setPreviousText(pevc, currEditText.getText().toString());
        currEditText.setBackgroundColor(0x01060000); //R.color.darker_grey

        currEditInfo.setVisibility(View.GONE);
        currSaveInfo.setVisibility(View.VISIBLE);
        currCancelInfo.setVisibility(View.VISIBLE);
    }

    public void saveInfo(View v) {
        ImageView currSaveInfo = (ImageView) v;
        ProfileElementViewContainer pevc = ProfileElementViewContainer.
                findContainerUsingSave(currSaveInfo);
        ImageView currEditInfo = ProfileElementViewContainer.getEdit(pevc);
        ImageView currCancelInfo = ProfileElementViewContainer.getCancel(pevc);
        EditText currEditText = ProfileElementViewContainer.getEditText(pevc);

        //currEditText.setFocusable(false);
        //currEditText.setClickable(false);
        currEditText.setEnabled(false);
        currEditText.setBackgroundColor(0x0106000d); //R.color.transparent

        currEditInfo.setVisibility(View.VISIBLE);
        currSaveInfo.setVisibility(View.GONE);
        currCancelInfo.setVisibility(View.GONE);
    }

    public void cancelInfo(View v) {
        ImageView currCancelInfo = (ImageView) v;
        ProfileElementViewContainer pevc = ProfileElementViewContainer.
                findContainerUsingCancel(currCancelInfo);
        ImageView currSaveInfo = ProfileElementViewContainer.getSave(pevc);
        ImageView currEditInfo = ProfileElementViewContainer.getEdit(pevc);
        EditText currEditText = ProfileElementViewContainer.getEditText(pevc);

        //currEditText.setFocusable(false);
        //currEditText.setClickable(false);
        currEditText.setEnabled(false);
        currEditText.setText(ProfileElementViewContainer.getPreviousText(pevc));
        currEditText.setBackgroundColor(0x0106000d); //R.color.transparent

        currEditInfo.setVisibility(View.VISIBLE);
        currSaveInfo.setVisibility(View.GONE);
        currCancelInfo.setVisibility(View.GONE);
    }
}

