package com.digikouc.financnizdravi;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadingFragment extends Fragment {

    int answer;
    String api_token;
    static  double total_saved;
    float sum_revenues, sum_necessary_expenses, sum_unnecessary_expenses, savingSum;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = (View) inflater.inflate(R.layout.fragment_loading, container, false);
        Bundle bundle = this.getArguments();
        DataRequest dataRequest = new DataRequest();

        api_token = bundle.getString("TOKEN");
        total_saved = bundle.getDouble("TOTALSAVED");

        Log.e("Total Saved", "=====>" + total_saved);

        dataRequest.setApi_token(api_token);
        getData(dataRequest);

        return v;
    }

    public void getData(DataRequest dataRequest) {
        Call<DataResponse> dataResponseCall = ApiClient.getService().getData(dataRequest);
        dataResponseCall.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                if (response.isSuccessful()) {
                    DataResponse dataResponse = response.body();
                    sum_revenues = (float) dataResponse.getData().getSum_revenues();
                    sum_necessary_expenses = (float) dataResponse.getData().getSum_necessary_expenses();
                    sum_unnecessary_expenses = (float) dataResponse.getData().getSum_unnecessary_expenses();
                    savingSum = (float) dataResponse.getData().getSavingsSum();

                    savingSum = savingSum + (float)total_saved;

                    if ((sum_necessary_expenses / sum_revenues > 0.5) && (savingSum < sum_unnecessary_expenses * 3)){
                        answer = 0; //Red Semafor
                    }else if((sum_necessary_expenses / sum_revenues <= 0.5) && (savingSum < sum_unnecessary_expenses * 3)){
                        answer = 1; //Orange(Yellow) Semafor
                    }else if((sum_necessary_expenses / sum_revenues <= 0.5) && (savingSum >= sum_unnecessary_expenses * 3)){
                        answer = 2; // Green Semafor
                    }

                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                    Bundle i = new Bundle();
                    i.putString("TOKEN", api_token);

                    switch (answer) {
                        case 0:
                            SemaforRedFragment frag_red = new SemaforRedFragment();
                            frag_red.setArguments(i);
                            fr.replace(R.id.fragment_container, frag_red);
                            fr.commit();
                            break;
                        case 1:
                            SemaforYellowFragment frag_yellow = new SemaforYellowFragment();
                            frag_yellow.setArguments(i);
                            fr.replace(R.id.fragment_container, frag_yellow);
                            fr.commit();
                            break;
                        case 2:
                            SemaforGreenFragment frag_green = new SemaforGreenFragment();
                            frag_green.setArguments(i);
                            fr.replace(R.id.fragment_container, frag_green);
                            fr.commit();
                            break;
                    }

                }

            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {

            }

        });
    }
}