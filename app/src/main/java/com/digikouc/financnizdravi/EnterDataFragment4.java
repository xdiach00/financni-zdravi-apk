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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterDataFragment4 extends Fragment {

    private View layoutEnterMain, layoutInfoMain;
    private int mScreen;
    private static final int SCREEN1 = 0;
    private static final int SCREEN2 = 1;
    float x1, x2, y1, y2; //Coordinates for detecting swiping
    int pension, emergency, children, capitalFunds, termDeposits, other, totalSaved;
    TextInputEditText inputPenzijni, inputNouze, inputDetem, inputFondy, inputVklady, inputJineSporeni, inputCelkem;
    String api_token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = (View) inflater.inflate(R.layout.fragment_enterdata_4, container, false);
        inputPenzijni = (TextInputEditText) v.findViewById(R.id.inputPenzijni);
        inputNouze = (TextInputEditText) v.findViewById(R.id.inputNouze);
        inputDetem = (TextInputEditText) v.findViewById(R.id.inputDetem);
        inputFondy = (TextInputEditText) v.findViewById(R.id.inputFondy);
        inputVklady = (TextInputEditText) v.findViewById(R.id.inputVklady);
        inputJineSporeni = (TextInputEditText) v.findViewById(R.id.inputJineSporeni);
        inputCelkem = (TextInputEditText) v.findViewById(R.id.inputCelkem);
        TextView infoPenzijni = (TextView) v.findViewById(R.id.textPenzijni);
        TextView infoNouze = (TextView) v.findViewById(R.id.textNouze);
        TextView infoDetem = (TextView) v.findViewById(R.id.textDetem);
        TextView infoFondy = (TextView) v.findViewById(R.id.textFondy);
        TextView infoVklady = (TextView) v.findViewById(R.id.textVklady);
        TextView infoJineSporeni = (TextView) v.findViewById(R.id.textJineSporeni);
        TextView infoCelkem = (TextView) v.findViewById(R.id.textCelkem);
        ImageView change_second = (ImageView) v.findViewById(R.id.change_second);
        ImageView change_first = (ImageView) v.findViewById(R.id.change_first);
        ImageView goNext = (ImageView) v.findViewById(R.id.imageGoNext);
        ImageView goPrevious = (ImageView) v.findViewById(R.id.imageGoPrevious);
        layoutEnterMain = (RelativeLayout) v.findViewById(R.id.enterDataFourth);
        layoutInfoMain = (RelativeLayout) v.findViewById(R.id.infoFourthEnterData);
        DataRequest dataRequest = new DataRequest();

        Bundle bundle = this.getArguments();
        api_token = bundle.getString("TOKEN");
        Log.e("TOKEN4", "=====>" + api_token);

        dataRequest.setApi_token(api_token);
        enterData(dataRequest);

        inputPenzijni.addTextChangedListener(new NumberTextWatcherForThousand(inputPenzijni));
        inputNouze.addTextChangedListener(new NumberTextWatcherForThousand(inputNouze));
        inputDetem.addTextChangedListener(new NumberTextWatcherForThousand(inputDetem));
        inputFondy.addTextChangedListener(new NumberTextWatcherForThousand(inputFondy));
        inputVklady.addTextChangedListener(new NumberTextWatcherForThousand(inputVklady));
        inputJineSporeni.addTextChangedListener(new NumberTextWatcherForThousand(inputJineSporeni));
        inputCelkem.addTextChangedListener(new NumberTextWatcherForThousand(inputCelkem));

        infoPenzijni.setText(Html.fromHtml("<b> Penzijní spoření  </b> - Kolik si měsíčně ukládáte stranou na penzi?"));
        infoNouze.setText(Html.fromHtml("<b> Spoření pro případ nouze </b> - Průměrná měsíční částka, kterou si ukládáte stranou pro případ nouze. Například nákup nové ledničky v případě poruchy."));
        infoDetem.setText(Html.fromHtml("<b> Spoření dětem </b> - Například stavební spoření či jiné peníze, které pravidelně odkládáte pro své děti."));
        infoFondy.setText(Html.fromHtml("<b> Kapitálové fondy </b> - Jakou průměrnou částku odkládáte do kapitálových fondů měsíčně?"));
        infoVklady.setText(Html.fromHtml("<b> Termínované vklady </b> - Průměrný měsíční termínovaný vklad."));
        infoJineSporeni.setText(Html.fromHtml("<b> Jiné spoření </b> - Pokud si pravidelně odkládáte část svých peněz jiným způsobem, uveďte prosím průměrnou měsíční částku do této kolonky."));
        infoCelkem.setText(Html.fromHtml("<b> Celkem mám naspořeno </b> - Jakou částku máte v tuto chvíli naspořenou?"));

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
                dataSetRequest.setPension(inputPenzijni.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputPenzijni.getText().toString())));
                dataSetRequest.setEmergency(inputNouze.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputNouze.getText().toString())));
                dataSetRequest.setChildren(inputDetem.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputDetem.getText().toString())));
                dataSetRequest.setCapital_funds(inputFondy.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputFondy.getText().toString())));
                dataSetRequest.setTerm_deposits(inputVklady.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputVklady.getText().toString())));
                dataSetRequest.setOther(inputJineSporeni.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputJineSporeni.getText().toString())));
                dataSetRequest.setTotal_saved(inputCelkem.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputCelkem.getText().toString())));
                setData(dataSetRequest);

                i.putString("TOKEN", api_token);
                i.putDouble("TOTALSAVED", inputCelkem.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputCelkem.getText().toString())));
                Log.e("TOKEN", "====>" + api_token);
                LoadingFragment frag = new LoadingFragment();
                frag.setArguments(i);
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, frag);
                fr.commit();

            }
        });

        goPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle i = new Bundle();

                DataSetRequest dataSetRequest = new DataSetRequest();
                dataSetRequest.setApi_token(api_token);
                dataSetRequest.setPension(inputPenzijni.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputPenzijni.getText().toString())));
                dataSetRequest.setEmergency(inputNouze.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputNouze.getText().toString())));
                dataSetRequest.setChildren(inputDetem.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputDetem.getText().toString())));
                dataSetRequest.setCapital_funds(inputFondy.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputFondy.getText().toString())));
                dataSetRequest.setTerm_deposits(inputVklady.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputVklady.getText().toString())));
                dataSetRequest.setOther(inputJineSporeni.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputJineSporeni.getText().toString())));
                dataSetRequest.setTotal_saved(inputCelkem.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputCelkem.getText().toString())));
                setData(dataSetRequest);

                i.putString("TOKEN", api_token);
                Log.e("TOKEN", "====>" + api_token);
                EnterDataFragment3 frag = new EnterDataFragment3();
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
        Call<DataResponse> dataResponseCall = ApiClient.getService().enterData4(dataRequest);
        dataResponseCall.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, retrofit2.Response<DataResponse> response) {
                if(response.isSuccessful()){
                    DataResponse dataResponse = response.body();
                    pension = (int) dataResponse.getData().getPension();
                    emergency = (int) dataResponse.getData().getEmergency();
                    children = (int) dataResponse.getData().getChildren();
                    capitalFunds = (int) dataResponse.getData().getCapitalFunds();
                    termDeposits = (int) dataResponse.getData().getTermDeposits();
                    other = (int) dataResponse.getData().getOther();
                    totalSaved = (int) dataResponse.getData().getTotalSaved();

                    inputPenzijni.setText(pension != 0 ? String.valueOf(pension) : "");
                    inputNouze.setText(emergency != 0 ? String.valueOf(emergency) : "");
                    inputDetem.setText(children != 0 ? String.valueOf(children) : "");
                    inputFondy.setText(capitalFunds != 0 ? String.valueOf(capitalFunds) : "");
                    inputVklady.setText(termDeposits != 0 ? String.valueOf(termDeposits) : "");
                    inputJineSporeni.setText(other != 0 ? String.valueOf(other) : "");
                    inputCelkem.setText(totalSaved != 0 ? String.valueOf(totalSaved) : "");
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {

            }
        });
    }

    public void setData(DataSetRequest dataSetRequest){
        Call<DataResponse> dataResponseCall = ApiClient.getService().setData4(dataSetRequest);
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
}