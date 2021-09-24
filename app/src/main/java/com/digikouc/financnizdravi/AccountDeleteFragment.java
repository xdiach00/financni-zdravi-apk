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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountDeleteFragment extends Fragment {

    TextInputEditText confirmPass;
    static TextInputLayout passLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = (View) inflater.inflate(R.layout.fragment_delete_acc, container, false);
        TextView showEmail = (TextView) v.findViewById(R.id.showUserEmail);
        Button btnDelete = (Button) v.findViewById(R.id.btnDeleteAcc);
        confirmPass = (TextInputEditText) v.findViewById(R.id.inputPassDelete);
        passLayout = (TextInputLayout) v.findViewById(R.id.textChangePass);
        String email = this.getArguments().getString("EMAIL");
        String api_token = this.getArguments().getString("TOKEN");

        Log.e("PASS_CHANGE", "=====>" + api_token);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passLayout.setErrorEnabled(false);
                DeleteAccountRequest deleteAccountRequest = new DeleteAccountRequest();
                deleteAccountRequest.setApi_token(api_token);
                deleteAccountRequest.setPassword(confirmPass.getText().toString());

                deleteAcc(deleteAccountRequest);
            }
        });

        showEmail.setText(email);
        return v;
    }

    public void deleteAcc(DeleteAccountRequest deleteAccountRequest){
        Call<SettingsResponse> deleteAccountCall = ApiClient.getService().deleteAccount(deleteAccountRequest);
        deleteAccountCall.enqueue(new Callback<SettingsResponse>() {
            @Override
            public void onResponse(Call<SettingsResponse> call, Response<SettingsResponse> response) {

                if(response.isSuccessful()){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }else{
                    passLayout.setErrorEnabled(true);
                    passLayout.setError("Heslo není správné");
                }
            }
            @Override
            public void onFailure(Call<SettingsResponse> call, Throwable t) {

            }
        });
    }

}

