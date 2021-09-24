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

public class ShowDataSavingsFragment extends Fragment {

    int pension, emergency, children, capitalFunds, termDeposits, other, totalSaved;
    TextView showPension, showEmergency, showChildren, showFunds, showDeposits, showTotal, showOther, returnChart;
    String api_token;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = (View) inflater.inflate(R.layout.fragment_showdata_savings, container, false);
        showPension = (TextView) v.findViewById(R.id.showPension);
        showEmergency = (TextView) v.findViewById(R.id.showEmergency);
        showChildren = (TextView) v.findViewById(R.id.showChildren);
        showFunds = (TextView) v.findViewById(R.id.showFunds);
        showDeposits = (TextView) v.findViewById(R.id.showDeposits);
        showOther = (TextView) v.findViewById(R.id.showOther);
        showTotal = (TextView) v.findViewById(R.id.showTotal);
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
        Log.e("Pension", "=====>" + pension);



        return v;
    }


    public void enterData(DataRequest dataRequest){
        Call<DataResponse> dataResponseCall = ApiClient.getService().enterData4(dataRequest);
        dataResponseCall.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                if(response.isSuccessful()){
                    DataResponse dataResponse = response.body();
                    pension = (int) dataResponse.getData().getPension();
                    emergency = (int) dataResponse.getData().getEmergency();
                    children = (int) dataResponse.getData().getChildren();
                    capitalFunds = (int) dataResponse.getData().getCapitalFunds();
                    termDeposits = (int) dataResponse.getData().getTermDeposits();
                    other = (int) dataResponse.getData().getOther();
                    totalSaved = (int) dataResponse.getData().getTotalSaved();

                    showPension.setText(pension != 0 ? String.valueOf(pension) : "");
                    showEmergency.setText(emergency != 0 ? String.valueOf(emergency) : "");
                    showChildren.setText(children != 0 ? String.valueOf(children) : "");
                    showFunds.setText(capitalFunds != 0 ? String.valueOf(capitalFunds) : "");
                    showDeposits.setText(termDeposits != 0 ? String.valueOf(termDeposits) : "");
                    showOther.setText(other != 0 ? String.valueOf(other) : "");
                    showTotal.setText(totalSaved != 0 ? String.valueOf(totalSaved) : "");
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {

            }
        });
    }
}