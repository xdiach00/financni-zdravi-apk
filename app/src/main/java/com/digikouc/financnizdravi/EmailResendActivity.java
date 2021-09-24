package com.digikouc.financnizdravi;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailResendActivity extends AppCompatActivity {

    static TextInputLayout layoutEmail;
    static TextInputEditText inputEmail;
    static String device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_resend);

        TextView goBack = (TextView) findViewById(R.id.textReturnLoginActivity);
        goBack.setPaintFlags(goBack.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        inputEmail = (TextInputEditText) findViewById(R.id.inputEmail);
        layoutEmail = (TextInputLayout) findViewById(R.id.editResetEmail);
        Button btnReset = (Button) findViewById(R.id.buttonResetPass);
        device = "m";

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutEmail.setErrorEnabled(false);
                String email = inputEmail.getText().toString();
                if (isEmailValid(email)){
                    EmailResendRequest emailResendRequest = new EmailResendRequest();
                    emailResendRequest.setEmail(email);
                    emailResendRequest.setDevice(device);
                    emailResend(emailResendRequest);
                }else{
                    layoutEmail.setErrorEnabled(true);
                    layoutEmail.setError("Zadejte E-mail");
                }
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    
    public void emailResend(EmailResendRequest emailResendRequest){
        Call<DataResponse> dataResponseCall = ApiClient.getService().resendActivation(emailResendRequest);
        dataResponseCall.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                if(response.isSuccessful()){
                    Intent code = new Intent(EmailResendActivity.this, EmailResendConfirmActivity.class);
                    startActivity(code);
                }else if(response.code() == 403){
                    Toast.makeText(EmailResendActivity.this, "Požadavek můžete odeslat jednou za 15 minut", Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        layoutEmail.setErrorEnabled(true);
                        layoutEmail.setError(jObjError.getString("message"));
                    } catch (JSONException e) {
                        Toast.makeText(EmailResendActivity.this, "Chyba při připojení k serveru. Zkontrolujte prosím připojení k internetu nebo zkuste později.", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(EmailResendActivity.this, "Chyba při připojení k serveru. Zkontrolujte prosím připojení k internetu nebo zkuste později.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                Toast.makeText(EmailResendActivity.this, "Chyba při připojení k serveru. Zkontrolujte prosím připojení k internetu nebo zkuste později.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}