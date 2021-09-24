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

public class ShowDataUnnecessaryFragment extends Fragment {

    int entertainment, restaurant, sport, shopping, hobby, travel, otherUnnecessaryExpenses;
    TextView showEntertainment, showRestaurant, showSport, showShopping, showHobby, showTravel, showOther, returnChart;
    String api_token;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = (View) inflater.inflate(R.layout.fragment_showdata_unnecessary, container, false);
        showEntertainment = (TextView) v.findViewById(R.id.showEntertainment);
        showRestaurant = (TextView) v.findViewById(R.id.showRestaurant);
        showSport = (TextView) v.findViewById(R.id.showSport);
        showShopping = (TextView) v.findViewById(R.id.showShopping);
        showHobby = (TextView) v.findViewById(R.id.showHobby);
        showTravel = (TextView) v.findViewById(R.id.showTravel);
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
        Log.e("Entertainment", "=====>" + entertainment);



        return v;
    }


    public void enterData(DataRequest dataRequest){
        Call<DataResponse> dataResponseCall = ApiClient.getService().enterData3(dataRequest);
        dataResponseCall.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                if(response.isSuccessful()){
                    DataResponse dataResponse = response.body();
                    entertainment = (int) dataResponse.getData().getEntertainment();
                    restaurant = (int) dataResponse.getData().getRestaurant();
                    sport = (int) dataResponse.getData().getSport();
                    shopping = (int) dataResponse.getData().getShopping();
                    hobby = (int) dataResponse.getData().getHobby();
                    travel = (int) dataResponse.getData().getTravel();
                    otherUnnecessaryExpenses = (int) dataResponse.getData().getOtherUnnecessaryExpenses();

                    showEntertainment.setText(entertainment != 0 ? String.valueOf(entertainment) : "");
                    showRestaurant.setText(restaurant != 0 ? String.valueOf(restaurant) : "");
                    showSport.setText(sport != 0 ? String.valueOf(sport) : "");
                    showShopping.setText(shopping != 0 ? String.valueOf(shopping) : "");
                    showHobby.setText(hobby != 0 ? String.valueOf(hobby) : "");
                    showTravel.setText(travel != 0 ? String.valueOf(travel) : "");
                    showOther.setText(otherUnnecessaryExpenses != 0 ? String.valueOf(otherUnnecessaryExpenses) : "");
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {

            }
        });
    }
}