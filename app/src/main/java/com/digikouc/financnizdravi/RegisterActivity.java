package com.digikouc.financnizdravi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    static String email, firstName, lastName;
    static int type;
    static TextInputEditText inputEmail, inputName, inputLastName;
    static TextInputLayout layoutEmail;
    static LabeledSwitch togglePerson;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        TextView textBackLogin = (TextView) findViewById(R.id.textBackLogin);
        inputEmail = (TextInputEditText) findViewById(R.id.inputRegisterEmail);
        layoutEmail = (TextInputLayout) findViewById(R.id.textRegisterEmail);
        inputName = (TextInputEditText) findViewById(R.id.inputRegisterName);
        inputLastName = (TextInputEditText) findViewById(R.id.inputRegisterSurname);
       togglePerson = findViewById(R.id.togglePerson);
        Button registerBtn = (Button) findViewById(R.id.buttonRegister);

        textBackLogin.setPaintFlags(textBackLogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        togglePerson.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if (isOn){
                    type = 1;
                }else{
                    type = 0;
                }
            }
        });


        textBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutEmail.setErrorEnabled(true);
                email = inputEmail.getEditableText().toString();
                if (inputName.getText().toString().matches("")){
                    Log.e("MATCHES", "true");
                    firstName = "";
                }else {
                    firstName = inputName.getEditableText().toString();
                }
                if (inputLastName.getText().toString().matches("")){
                    lastName = "";
                }else {
                    lastName = inputLastName.getEditableText().toString();
                }

                if (!isEmailValid(email)){
                    layoutEmail.setErrorEnabled(true);

                    layoutEmail.setError("Špatný formát E-mailu");
                    //inputEmail.setError("Špatný formát E-mailu");
                }else{
                    PassResetRequest passResetRequest = new PassResetRequest();
                    passResetRequest.setEmail(email);
                    checkEmail(passResetRequest);

                }

            }
        });
    }

    public void checkEmail(PassResetRequest checkEmailRequest){
        Call<EmailAvailableResponse> emailAvailableResponseCall = ApiClient.getService().checkEmail(checkEmailRequest);
        emailAvailableResponseCall.enqueue(new Callback<EmailAvailableResponse>() {
            @Override
            public void onResponse(Call<EmailAvailableResponse> call, Response<EmailAvailableResponse> response) {
                EmailAvailableResponse emailAvailableResponse = response.body();
                if(emailAvailableResponse.getStatus() == false){
                    layoutEmail.setErrorEnabled(true);
                    layoutEmail.setError(emailAvailableResponse.getMessage());
                }else{
                    Intent passSet = new Intent(RegisterActivity.this, PassSetActivity.class);
                    passSet.putExtra("EMAIL", email);
                    passSet.putExtra("TYPE", type);
                    passSet.putExtra("NAME", firstName);
                    passSet.putExtra("SURNAME", lastName);
                    startActivity(passSet);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<EmailAvailableResponse> call, Throwable t) {

            }
        });
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}