 package com.digikouc.financnizdravi;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

 public class EnterDataFragment extends Fragment {

    private View layoutEnterMain, layoutInfoMain;
    private int mScreen;
    private static final int SCREEN1 = 0;
    private static final int SCREEN2 = 1;
    static TextInputEditText inputAverage, inputAnother;
    static TextView infoAverageMain, labelMain;
    float x1, x2, y1, y2; //Coordinates for detecting swiping
    String api_token;
    int averageSalary, otherRegularIncome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = (View) inflater.inflate(R.layout.fragment_enterdata, container, false);
        inputAverage = (TextInputEditText) v.findViewById(R.id.inputAverageMzda);
        inputAnother = (TextInputEditText) v.findViewById(R.id.inputAnotherMzda);
        infoAverageMain = (TextView) v.findViewById(R.id.textAverageMzda);
        labelMain = (TextView) v.findViewById(R.id.textAverageChange);
        TextView infoAnother = (TextView) v.findViewById(R.id.textAnotherMzda);
        ImageView change_second = (ImageView) v.findViewById(R.id.change_second);
        ImageView change_first = (ImageView) v.findViewById(R.id.change_first);
        ImageView goNext = (ImageView) v.findViewById(R.id.imageGoNext);
        layoutEnterMain = (RelativeLayout) v.findViewById(R.id.enterDataMain);
        layoutInfoMain = (RelativeLayout) v.findViewById(R.id.infoMainEnterData);
        api_token = this.getArguments().getString("TOKEN");
        DataRequest dataRequest = new DataRequest();
        SettingsRequest settingsRequest = new SettingsRequest();
        //averageSalary = this.getArguments().getInt("averageSalary");
        //otherRegularIncome = this.getArguments().getInt("otherRegularIncome");

        dataRequest.setApi_token(api_token);
        settingsRequest.setApi_token(api_token);
        enterData(dataRequest);
        setSettings(settingsRequest);

        Toast.makeText(getActivity(), "Posuňte vlevo nebo vpravo pro více informací.", Toast.LENGTH_SHORT).show();

        Log.e("ZADANI", "=====>" + api_token);
        Log.e("Average", "=====>" + averageSalary);



        inputAverage.addTextChangedListener(new NumberTextWatcherForThousand(inputAverage));
        inputAnother.addTextChangedListener(new NumberTextWatcherForThousand(inputAnother));
        //infoAverageMain.setText(Html.fromHtml("<b> Průměrná mesíční mzda / plat </b> - Uveďte svůj průměrný měsíční plat / mzdu včetně odměn, bonusů, přesčasů atd."));
        infoAnother.setText(Html.fromHtml("<b> Jiné pravidelné příjmy </b> - Jakékoliv jiné pravidelné příjmy - například pronájem, úroky, důchod atp."));
        //onTouchListener to detect swipes for changing between activities
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent touchEvent) {
                switch (touchEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = touchEvent.getX();
                        y1 = touchEvent.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = touchEvent.getX();
                        y2 = touchEvent.getY();
                        if ((x1 > x2) && (Math.abs(y1-y2) < 150)) {
                            mScreen = SCREEN2;
                            renderScreen();
                        } else if ((x1 < x2) && (Math.abs(y1-y2) < 150)) {
                            mScreen = SCREEN1;
                            renderScreen();
                        }
                }
                return true;
            }
        });

        //Change the fragment by clicking an arrow
        goNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle i = new Bundle();

                DataSetRequest dataSetRequest = new DataSetRequest();
                dataSetRequest.setApi_token(api_token);
                dataSetRequest.setAverage_salary(inputAverage.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputAverage.getText().toString())));
                dataSetRequest.setOther_regular_income(inputAnother.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputAnother.getText().toString())));
                setData(dataSetRequest);

                i.putString("TOKEN", api_token);
                Log.e("TOKEN", "====>" + api_token);
                EnterDataFragment2 frag = new EnterDataFragment2();
                frag.setArguments(i);
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, frag);
                fr.commit();

            }
        });

        //change layout buy clicking grey circles
        change_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScreen = SCREEN2;
                renderScreen();
            }
        });

        change_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScreen = SCREEN1;
                renderScreen();
            }
        });
        mScreen = SCREEN1; // default screen
        renderScreen();
        return v;
    }


    //function for showing new layout
    private void renderScreen(){
        layoutEnterMain.setVisibility(mScreen == SCREEN1 ? View.VISIBLE : View.GONE);
        layoutInfoMain.setVisibility(mScreen == SCREEN2 ? View.VISIBLE : View.GONE);
    }

     public void enterData(DataRequest dataRequest){
         Call<DataResponse> dataResponseCall = ApiClient.getService().enterData(dataRequest);
         dataResponseCall.enqueue(new Callback<DataResponse>() {
             @Override
             public void onResponse(Call<DataResponse> call, retrofit2.Response<DataResponse> response) {
                 if(response.isSuccessful()){
                     DataResponse dataResponse = response.body();
                     averageSalary = (int) dataResponse.getData().getAverageSalary();
                     otherRegularIncome = (int) dataResponse.getData().getOtherRegularIncome();

                     inputAverage.setText(averageSalary != 0 ? String.valueOf(averageSalary) : "");
                     inputAnother.setText(otherRegularIncome != 0 ? String.valueOf(otherRegularIncome) : "");

                     Log.e("AverageCall", "=====>" + averageSalary);
                 }
             }

             @Override
             public void onFailure(Call<DataResponse> call, Throwable t) {

             }
         });
     }

     public void setData(DataSetRequest dataSetRequest){
        Call<DataResponse> dataResponseCall = ApiClient.getService().setData(dataSetRequest);
        dataResponseCall.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                if(response.isSuccessful()){

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
                     if(dataResponse.getData().getType() == true){
                         infoAverageMain.setText(Html.fromHtml("<b> Průměrný měsíční příjem </b> - Uveďte svůj průměrný měsíční čistý zisk."));
                         labelMain.setText(Html.fromHtml("Průměrný měsíční příjem"));
                     }else{
                         infoAverageMain.setText(Html.fromHtml("<b> Průměrná měsíční mzda / plat </b> - Uveďte svůj průměrný měsíční plat / mzdu včetně odměn, bonusů, přesčasů atd."));
                         labelMain.setText(Html.fromHtml("Průměrná měsíční mzda / plat"));
                     }

                 }
             }

             @Override
             public void onFailure(Call<DataResponse> call, Throwable t) {

             }
         });
     }
}