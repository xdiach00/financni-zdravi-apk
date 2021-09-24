package com.digikouc.financnizdravi;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends Fragment {

    TextInputEditText inputName, inputSecondName;
    LabeledSwitch togglePerson;
    static TextView textSouhlas;
    String date;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = (View) inflater.inflate(R.layout.fragment_settings, container, false);
        Button changePass = (Button) v.findViewById(R.id.buttonChangePassword);
        Button saveChanges = (Button) v.findViewById(R.id.buttonSaveChanges);
        Button deleteAccount = (Button) v.findViewById(R.id.buttonDeleteAccount);
        togglePerson = v.findViewById(R.id.togglePersonChange);
        inputName = (TextInputEditText) v.findViewById(R.id.inputSettingsName);
        inputSecondName = (TextInputEditText) v.findViewById(R.id.inputSettingsSecondName);
        textSouhlas = (TextView)  v.findViewById(R.id.textSouhlas);
        TextView showEmail = (TextView) v.findViewById(R.id.showUserEmail);
        String email = this.getArguments().getString("EMAIL");
        String api_token = this.getArguments().getString("TOKEN");
        Bundle i = new Bundle();
        SettingsRequest settingsRequest = new SettingsRequest();

        settingsRequest.setApi_token(api_token);
        setSettings(settingsRequest);

        Log.e("SETTINGS", "=====>" + api_token);

        showEmail.setText(email);
        togglePerson.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {

            }
        });


        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SettingsRequest settingsRequest = new SettingsRequest();
                settingsRequest.setApi_token(api_token);
                settingsRequest.setFirst_name(inputName.getText().toString());
                settingsRequest.setLast_name(inputSecondName.getText().toString());
                settingsRequest.setType(togglePerson.isOn() ? "true" : "false");

                Log.e("TAG", settingsRequest.getType());

                changeSettings(settingsRequest);
            }
        });

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                i.putString("EMAIL", email);
                i.putString("TOKEN", api_token);
                PassChangeFragment frag = new PassChangeFragment();
                frag.setArguments(i);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, frag).addToBackStack(null).commit();

                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PassChangeFragment()).addToBackStack(null).commit();
            }
        });

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putString("EMAIL", email);
                i.putString("TOKEN", api_token);
                AccountDeleteFragment frag = new AccountDeleteFragment();
                frag.setArguments(i);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, frag).addToBackStack(null).commit();

            }
        });

        return v;
    }

    public void changeSettings(SettingsRequest settingsRequest){
        Call<DataResponse> dataResponseCall = ApiClient.getService().changeSettings(settingsRequest);
        dataResponseCall.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {

                if(response.isSuccessful()){

                    DataResponse dataResponse = response.body();
                    Toast.makeText(getActivity(), dataResponse.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {

            }
        });
    }
    public void setSettings(SettingsRequest settingsRequest){
        Call<DataResponse> dataResponseCall = ApiClient.getService().showSettings(settingsRequest);
        dataResponseCall.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                if(response.isSuccessful()){

                    DataResponse dataResponse = response.body();
                    inputName.setText(dataResponse.getData().getFirstName() == null ? "" : dataResponse.getData().getFirstName());
                    inputSecondName.setText(dataResponse.getData().getLastName() == null ? "" : dataResponse.getData().getLastName());
                    togglePerson.setOn(dataResponse.getData().getType());

                    date = dataResponse.getData().getCreated().getDate();
                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.000000");
                    SimpleDateFormat formatter6 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    try {
                        Date date1 = formatter.parse(date);
                        String destDate = formatter6.format(date1);
                        textSouhlas.setText("Souhlas se zpracováním osobních údajů udělen " + destDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {

            }
        });
    }

    public void onBackPressed() { getActivity().finish();}

}
