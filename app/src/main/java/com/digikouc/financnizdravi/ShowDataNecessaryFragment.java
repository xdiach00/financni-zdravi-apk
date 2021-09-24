package com.digikouc.financnizdravi;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowDataNecessaryFragment extends Fragment {

    int housing, services, fuel, itemsDailyConsumptions, food, loanPayment, healthCare, otherNecessaryExpenses;
    TextView showHousing, showServices, showFuel, showDaily, showFood, showLoan, showHealth, showOther, returnChart;
    String api_token;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = (View) inflater.inflate(R.layout.fragment_showdata_necessary, container, false);
        showHousing = (TextView) v.findViewById(R.id.showHousing);
        showServices = (TextView) v.findViewById(R.id.showServices);
        showFuel = (TextView) v.findViewById(R.id.showFuel);
        showDaily = (TextView) v.findViewById(R.id.showDaily);
        showFood = (TextView) v.findViewById(R.id.showFood);
        showLoan = (TextView) v.findViewById(R.id.showLoan);
        showHealth = (TextView) v.findViewById(R.id.showHealth);
        showOther = (TextView) v.findViewById(R.id.showOther);
        returnChart  = (TextView) v.findViewById(R.id.textReturnChart);
        DataRequest dataRequest = new DataRequest();

        Bundle bundle = this.getArguments();
        api_token = bundle.getString("TOKEN");
        Log.e("TOKEN2", "=====>" + api_token);

        returnChart.setPaintFlags(returnChart.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        dataRequest.setApi_token(api_token);
        enterData(dataRequest);

        returnChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });

        Log.e("ZADANI", "=====>" + api_token);
        Log.e("Housing", "=====>" + housing);



        return v;
    }


    public void enterData(DataRequest dataRequest){
        Call<DataResponse> dataResponseCall = ApiClient.getService().enterData2(dataRequest);
        dataResponseCall.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                if(response.isSuccessful()){
                    DataResponse dataResponse = response.body();
                    housing = (int) dataResponse.getData().getHousing();
                    services = (int) dataResponse.getData().getServices();
                    fuel = (int) dataResponse.getData().getFuel();
                    itemsDailyConsumptions = (int) dataResponse.getData().getItemsDailyConsumptions();
                    food = (int) dataResponse.getData().getFood();
                    loanPayment = (int) dataResponse.getData().getLoanPayment();
                    healthCare = (int) dataResponse.getData().getHealthCare();
                    otherNecessaryExpenses = (int) dataResponse.getData().getOtherNecessaryExpenses();

                    showHousing.setText(housing != 0 ? String.valueOf(housing) : "");
                    showServices.setText(services != 0 ? String.valueOf(services) : "");
                    showFuel.setText(fuel != 0 ? String.valueOf(fuel) : "");
                    showDaily.setText(itemsDailyConsumptions != 0 ? String.valueOf(itemsDailyConsumptions) : "");
                    showFood.setText(food != 0 ? String.valueOf(food) : "");
                    showLoan.setText(loanPayment != 0 ? String.valueOf(loanPayment) : "");
                    showHealth.setText(healthCare != 0 ? String.valueOf(healthCare) : "");
                    showOther.setText(otherNecessaryExpenses != 0 ? String.valueOf(otherNecessaryExpenses) : "");
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {

            }
        });
    }
}