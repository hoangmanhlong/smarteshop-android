package com.example.loginapp.view.state;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginEmailButtonObserver implements TextWatcher {

    private Button button;

    private EditText[] editTexts;

    private TextView textView;

    public LoginEmailButtonObserver(Button button, TextView textView, EditText... editTexts) {
        this.button = button;
        this.textView = textView;
        this.editTexts = editTexts;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        textView.setVisibility(View.GONE);
        if (s.toString().trim().isEmpty()) {
            button.setEnabled(false);
        } else button.setEnabled(true);
    }

    @Override
    public void afterTextChanged(Editable s) {
        checkFields();
    }

    private void checkFields() {
        // Enable the button only if all fields are not empty
        boolean allFieldsNotEmpty = true;
        for (EditText editText : editTexts) {
            if (TextUtils.isEmpty(editText.getText().toString())) {
                allFieldsNotEmpty = false;
                break;
            }
        }
        button.setEnabled(allFieldsNotEmpty);
    }
}