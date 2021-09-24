package com.digikouc.financnizdravi;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    TextInputEditText inputEmail, inputPassword;
    TextInputLayout layoutEmail, layoutPassword;
    static String token, email_saved;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        layoutEmail = (TextInputLayout) findViewById(R.id.textLoginEmail);
        layoutPassword = (TextInputLayout) findViewById(R.id.textLoginPassword);
        inputEmail = (TextInputEditText) findViewById(R.id.inputLoginEmail);
        inputPassword = (TextInputEditText) findViewById(R.id.inputLoginPass);
        TextView textReset = (TextView) findViewById(R.id.textResetPass);
        TextView emailResend = (TextView) findViewById(R.id.textResendEmail);
        textReset.setPaintFlags(textReset.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        emailResend.setPaintFlags(emailResend.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        TextView textRegister = (TextView) findViewById(R.id.textRegister);
        textRegister.setPaintFlags(textRegister.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Button btnLogin = (Button) findViewById(R.id.buttonLogin);

        SharedPreferences preferences = getSharedPreferences("authorize", MODE_PRIVATE);
        token = preferences.getString("TOKEN", "");
        email_saved = preferences.getString("EMAIL", "");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);


        Log.e("HEIGHT", "====>" + displayMetrics);

        SettingsRequest settingsRequest = new SettingsRequest();
        settingsRequest.setApi_token(token);
        Log.e("SAVED TOKEN", "=====>" + token);
        Log.e("SAVED EMAIL", "=====>" + email_saved);

        checkToken(settingsRequest);

        textReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reset = new Intent(LoginActivity.this, PassResetActivity.class);
                startActivity(reset);
            }
        });

        emailResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resend = new Intent(LoginActivity.this, EmailResendActivity.class);
                startActivity(resend);
            }
        });

        textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(register);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();

                if (!isEmailValid(email) || password.matches("")) {
                    if (!isEmailValid(email)){ layoutEmail.setErrorEnabled(true); layoutEmail.setError("Zadejte E-mail"); } else {layoutEmail.setErrorEnabled(false);}
                    if (password.matches("")){ layoutPassword.setErrorEnabled(true); layoutPassword.setError("Zadejte heslo"); } else {layoutPassword.setErrorEnabled(false);}
                }else{
                    layoutEmail.setErrorEnabled(false);
                    layoutPassword.setErrorEnabled(false);
                    LoginRequest loginRequest = new LoginRequest();
                    loginRequest.setEmail(inputEmail.getText().toString());
                    loginRequest.setPassword(inputPassword.getText().toString());

                    loginUser(loginRequest);

                }

            }
        });
    }

    public void loginUser(LoginRequest loginRequest){
        Call<LoginResponse> loginResponseCall = ApiClient.getService().loginUser(loginRequest);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful()){
                    LoginResponse loginResponse = response.body();
                    Intent main = new Intent(LoginActivity.this, BottomNavigationActivity.class);
                    TextInputEditText inputEmail = (TextInputEditText) findViewById(R.id.inputLoginEmail); //Getting an Email address
                    main.putExtra("EMAIL", inputEmail.getText().toString());
                    main.putExtra("TOKEN", loginResponse.getData().getApi_token());

                    SharedPreferences preferences = getSharedPreferences("authorize", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("TOKEN", loginResponse.getData().getApi_token());
                    editor.putString("EMAIL", inputEmail.getText().toString());
                    editor.commit();
                    Log.e("TOKEN", loginResponse.getData().getApi_token());

                    startActivity(main);
                    finish();

                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        if (jObjError.getString("message").matches("Uživatel s tímto e-mailem nebyl nazelen")){
                            Log.e("TRUE", "TRUE");
                            layoutEmail.setErrorEnabled(true);
                            layoutEmail.setError(jObjError.getString("message"));
                        } else if (jObjError.getString("message").matches("Heslo není správné")){
                            layoutPassword.setErrorEnabled(true);
                            layoutPassword.setError(jObjError.getString("message"));
                        } else {
                            Toast.makeText(LoginActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                        }
                        Log.e("RESPONSE", "=====>" + jObjError.getString("message"));
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

                String message = t.getLocalizedMessage();
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void checkToken(SettingsRequest settingsRequest){
        Call<DataResponse> dataResponseCall = ApiClient.getService().showSettings(settingsRequest);
        dataResponseCall.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                if(response.code() == 200) {
                    Intent main = new Intent(LoginActivity.this, BottomNavigationActivity.class);
                    TextInputEditText inputEmail = (TextInputEditText) findViewById(R.id.inputLoginEmail); //Getting an Email address
                    main.putExtra("EMAIL", email_saved);
                    main.putExtra("TOKEN", token);

                    startActivity(main);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {

            }
        });
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}