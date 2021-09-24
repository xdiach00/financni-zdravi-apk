package com.digikouc.financnizdravi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailConfirmActivity extends AppCompatActivity {

    static TextInputEditText acceptCode;
    static TextInputLayout layoutCode;
    Button confirm;
    static String code, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_confirm);

        TextView returnBack = (TextView) findViewById(R.id.textReturnLoginActivity);
        returnBack.setPaintFlags(returnBack.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        acceptCode = (TextInputEditText) findViewById(R.id.inputCode);
        layoutCode = (TextInputLayout) findViewById(R.id.editConfirmEmail);
        confirm = (Button) findViewById(R.id.buttonConfirm);
        email = getIntent().getStringExtra("EMAIL");
        password = getIntent().getStringExtra("PASS");

        Log.e("EMAIL", "====>" + email);
        Log.e("PASS", "====>" + password);


        returnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent login = new Intent(EmailConfirmActivity.this, LoginActivity.class);
                startActivity(login);
                finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutCode.setErrorEnabled(false);

                code = acceptCode.getText().toString();
                ActivateUserRequest activateUserRequest = new ActivateUserRequest();
                activateUserRequest.setActivation_token(code);

                activateUser(activateUserRequest);
            }
        });
    }


    public void activateUser(ActivateUserRequest activateUserRequest){
        Call<DataResponse> dataResponseCall = ApiClient.getService().activateUser(activateUserRequest);
        dataResponseCall.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                if(response.isSuccessful()){
                    LoginRequest loginRequest = new LoginRequest();
                    loginRequest.setEmail(email);
                    loginRequest.setPassword(password);

                    loginUser(loginRequest);
                }else{
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        layoutCode.setErrorEnabled(true);
                        layoutCode.setError(jObjError.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {

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
                    Intent main = new Intent(EmailConfirmActivity.this, BottomNavigationActivity.class);
                    TextInputEditText inputEmail = (TextInputEditText) findViewById(R.id.inputLoginEmail); //Getting an Email address
                    main.putExtra("EMAIL", email);
                    main.putExtra("TOKEN", loginResponse.getData().getApi_token());

                    SharedPreferences preferences = getSharedPreferences("authorize", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("TOKEN", loginResponse.getData().getApi_token());
                    editor.putString("EMAIL", email);
                    editor.commit();
                    Log.e("TOKEN", loginResponse.getData().getApi_token());

                    startActivity(main);
                    finish();

                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}