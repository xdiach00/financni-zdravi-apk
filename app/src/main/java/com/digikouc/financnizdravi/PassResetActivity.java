package com.digikouc.financnizdravi;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassResetActivity extends AppCompatActivity {

    static TextInputLayout layoutEmail;
    static TextInputEditText inputEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_reset);

        TextView goBack = (TextView) findViewById(R.id.textReturnLoginActivity);
        goBack.setPaintFlags(goBack.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        inputEmail = (TextInputEditText) findViewById(R.id.inputEmail);
        layoutEmail = (TextInputLayout) findViewById(R.id.editResetEmail);
        Button btnReset = (Button) findViewById(R.id.buttonResetPass);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutEmail.setErrorEnabled(false);
                String email = inputEmail.getText().toString();
                if (isEmailValid(email)){
                    PassResetRequest passResetRequest = new PassResetRequest();
                    passResetRequest.setEmail(email);
                    passReset(passResetRequest);
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
    
    public void passReset(PassResetRequest passResetRequest){
        Call<DataResponse> dataResponseCall = ApiClient.getService().resetPass(passResetRequest);
        dataResponseCall.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                Toast.makeText(PassResetActivity.this, "Instrukce pro změnu hesla odeslána do vašeho E-mailu", Toast.LENGTH_SHORT).show();
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