package com.digikouc.financnizdravi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassChangeFragment extends Fragment {

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

    TextInputLayout layoutPass, layoutConfirmPass;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = (View) inflater.inflate(R.layout.fragment_passchange, container, false);
        TextView showEmail = (TextView) v.findViewById(R.id.showUserEmail);
        Button btnPassChange = (Button) v.findViewById(R.id.btnChangePass);
        TextInputEditText newPass = (TextInputEditText) v.findViewById(R.id.inputPassChange);
        TextInputEditText newPassConfirm = (TextInputEditText) v.findViewById(R.id.inputPassChangeConfirm);
        layoutPass = (TextInputLayout) v.findViewById(R.id.textChangePass);
        layoutConfirmPass = (TextInputLayout) v.findViewById(R.id.textChangeAcceptLayout);
        String email = this.getArguments().getString("EMAIL");
        String api_token = this.getArguments().getString("TOKEN");

        Log.e("PASS_CHANGE", "=====>" + api_token);

        btnPassChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutPass.setErrorEnabled(false);
                layoutConfirmPass.setErrorEnabled(false);
                if(!PASSWORD_PATTERN.matcher(newPass.getText().toString()).matches()){ layoutPass.setErrorEnabled(true); layoutPass.setError("Heslo musí obsahovat minimálně 1 velké písmeno, 1 číslici a 1 speciální znak. Minimální délka hesla je 6 znaků.");}
                else if(!newPass.getText().toString().matches(newPassConfirm.getText().toString()) ){layoutConfirmPass.setErrorEnabled(true); layoutConfirmPass.setError("Hesla se neshodují!");}
                else {
                    layoutPass.setErrorEnabled(false);
                    layoutConfirmPass.setErrorEnabled(false);
                    PassChangeRequest passChangeRequest = new PassChangeRequest();
                    passChangeRequest.setApi_token(api_token);
                    passChangeRequest.setPassword(newPass.getText().toString());

                    changePass(passChangeRequest);
                }
            }
        });

        showEmail.setText(email);
        return v;
    }

    public void changePass(PassChangeRequest passChangeRequest){
        Call<SettingsResponse> passChangeCall = ApiClient.getService().changePass(passChangeRequest);
        passChangeCall.enqueue(new Callback<SettingsResponse>() {
            @Override
            public void onResponse(Call<SettingsResponse> call, Response<SettingsResponse> response) {

                if(response.isSuccessful()){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
            @Override
            public void onFailure(Call<SettingsResponse> call, Throwable t) {

            }
        });
    }

}

