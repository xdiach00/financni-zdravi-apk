package com.digikouc.financnizdravi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassSetActivity extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[!@#$%^&*-._])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 4 characters
                    "$");
    static TextInputLayout layoutPass, layoutConfirmPass, layoutEmail;
    static TextInputEditText  pass;
    static TextView textEmail;
    static String email_reg;
    TextView goBack, souhlas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_set);

        String email = getIntent().getStringExtra("EMAIL");
        String firstName = getIntent().getStringExtra("NAME");
        String lastName = getIntent().getStringExtra("SURNAME");
        String type = String.valueOf(getIntent().getIntExtra("TYPE", 0));
        Button passwordSet = (Button) findViewById(R.id.buttonEndRegister);
        Switch switchCheck = (Switch) findViewById(R.id.switchCheck);
        goBack = (TextView) findViewById(R.id.textGoBack);
        souhlas = (TextView) findViewById(R.id.textView3);
        pass = (TextInputEditText) findViewById(R.id.inputPass);
        TextInputEditText passConfirm = (TextInputEditText) findViewById(R.id.inputPassConfirm);
        textEmail = (TextView) findViewById(R.id.showUserEmail);
        layoutPass = (TextInputLayout) findViewById(R.id.textSetPass);
        layoutConfirmPass = (TextInputLayout) findViewById(R.id.textAcceptPass);
        layoutEmail = (TextInputLayout) findViewById(R.id.textUserEmail);
        String device = "m";

        goBack.setPaintFlags(goBack.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        textEmail.setText(email);

        if (souhlas != null) {
            souhlas.setMovementMethod(LinkMovementMethod.getInstance());
        }


        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailActivity = new Intent(PassSetActivity.this, RegisterActivity.class);
                startActivity(emailActivity);
                finish();
            }
        });

        passwordSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email_reg = textEmail.getText().toString();
                layoutPass.setErrorEnabled(false);
                layoutConfirmPass.setErrorEnabled(false);
                textEmail.setBackgroundResource(R.drawable.shape_no_edit);

                if (switchCheck.isChecked()){
                    if (!PASSWORD_PATTERN.matcher(pass.getText().toString()).matches()) {
                        layoutPass.setErrorEnabled(true);
                        layoutPass.setError("Heslo musí obsahovat minimálně 1 velké písmeno, 1 číslici a 1 speciální znak. Minimální délka hesla je 6 znaků.");
                    } else if (!pass.getText().toString().matches(passConfirm.getText().toString())) {
                        layoutConfirmPass.setErrorEnabled(true);
                        layoutConfirmPass.setError("Hesla se neshodují!");
                    } else {
                        RegisterRequest registerRequest = new RegisterRequest();
                        registerRequest.setEmail(email_reg);
                        registerRequest.setPassword(pass.getText().toString());
                        registerRequest.setFirst_name(firstName);
                        registerRequest.setLast_name(lastName);
                        registerRequest.setType(type);
                        registerRequest.setDevice(device);
                        registerUser(registerRequest);

                    }
                }else{
                    Toast.makeText(PassSetActivity.this, "Musíte dát souhlas se zpracováním Vašich osobních údajů!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void registerUser(RegisterRequest registerRequest){
        Call<RegisterResponse> registerResponseCall = ApiClient.getService().registerUser(registerRequest);
        registerResponseCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                if (response.isSuccessful()){

                    Intent confirmActivity = new Intent(PassSetActivity.this, EmailConfirmActivity.class);
                    confirmActivity.putExtra("EMAIL", email_reg);
                    confirmActivity.putExtra("PASS", pass.getText().toString());
                    startActivity(confirmActivity);
                    finish();

                }else{

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        //Toast.makeText(PassSetActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();

                        Log.e("ERROR", "=====>" + jObjError.getString("message"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {

                String message = t.getLocalizedMessage();
                Toast.makeText(PassSetActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

}