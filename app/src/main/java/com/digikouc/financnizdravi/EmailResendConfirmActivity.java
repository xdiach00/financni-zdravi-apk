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

public class EmailResendConfirmActivity extends AppCompatActivity {

    static TextInputEditText acceptCode;
    static TextInputLayout layoutCode;
    Button confirm;
    static String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_confirm);

        TextView returnBack = (TextView) findViewById(R.id.textReturnLoginActivity);
        returnBack.setPaintFlags(returnBack.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        acceptCode = (TextInputEditText) findViewById(R.id.inputCode);
        layoutCode = (TextInputLayout) findViewById(R.id.editConfirmEmail);
        confirm = (Button) findViewById(R.id.buttonConfirm);

        returnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent login = new Intent(EmailResendConfirmActivity.this, LoginActivity.class);
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
                    Toast.makeText(EmailResendConfirmActivity.this, "Váš účet byl aktivován. Můžete se přihlásit.", Toast.LENGTH_SHORT).show();

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

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}