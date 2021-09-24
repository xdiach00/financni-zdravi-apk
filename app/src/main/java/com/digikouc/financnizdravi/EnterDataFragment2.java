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

public class EnterDataFragment2 extends Fragment {

    private View layoutEnterMain, layoutInfoMain;
    private int mScreen;
    private static final int SCREEN1 = 0;
    private static final int SCREEN2 = 1;
    float x1, x2, y1, y2; //Coordinates for detecting swiping
    int housing, services, fuel, itemsDailyConsumptions, food, loanPayment, healthCare, otherNecessaryExpenses;
    TextInputEditText inputBydleni, inputSluzby, inputHmoty, inputDenni, inputPotraviny, inputSplatky, inputZdravotny, inputJineNezbytne;
    String api_token;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = (View) inflater.inflate(R.layout.fragment_enterdata_2, container, false);
        inputBydleni = (TextInputEditText) v.findViewById(R.id.inputBydleni);
        inputSluzby = (TextInputEditText) v.findViewById(R.id.inputSluzby);
        inputHmoty = (TextInputEditText) v.findViewById(R.id.inputHmoty);
        inputDenni = (TextInputEditText) v.findViewById(R.id.inputDenniPotreby);
        inputPotraviny = (TextInputEditText) v.findViewById(R.id.inputPotraviny);
        inputSplatky = (TextInputEditText) v.findViewById(R.id.inputSplatky);
        inputZdravotny = (TextInputEditText) v.findViewById(R.id.inputZdravotniPece);
        inputJineNezbytne = (TextInputEditText) v.findViewById(R.id.inputJineNezbytne);
        TextView infoBydleni = (TextView) v.findViewById(R.id.textBydleni);
        TextView infoSluzby = (TextView) v.findViewById(R.id.textSluzby);
        TextView infoHmoty = (TextView) v.findViewById(R.id.textHmoty);
        TextView infoDenni = (TextView) v.findViewById(R.id.textDenniPotreby);
        TextView infoPotraviny = (TextView) v.findViewById(R.id.textPotraviny);
        TextView infoSplatky = (TextView) v.findViewById(R.id.textSplatky);
        TextView infoZdravotny = (TextView) v.findViewById(R.id.textZdravotniPece);
        TextView infoJineNezbytne = (TextView) v.findViewById(R.id.textJineNezbytne);
        ImageView change_second = (ImageView) v.findViewById(R.id.change_second);
        ImageView change_first = (ImageView) v.findViewById(R.id.change_first);
        ImageView goNext = (ImageView) v.findViewById(R.id.imageGoNext);
        ImageView goPrevious = (ImageView) v.findViewById(R.id.imageGoPrevious);
        layoutEnterMain = (RelativeLayout) v.findViewById(R.id.enterDataSecond);
        layoutInfoMain = (RelativeLayout) v.findViewById(R.id.infoSecondEnterData);
        DataRequest dataRequest = new DataRequest();

        Bundle bundle = this.getArguments();
        api_token = bundle.getString("TOKEN");
        Log.e("TOKEN2", "=====>" + api_token);

        dataRequest.setApi_token(api_token);
        enterData(dataRequest);




        Log.e("ZADANI", "=====>" + api_token);
        Log.e("Housing", "=====>" + housing);

        inputBydleni.addTextChangedListener(new NumberTextWatcherForThousand(inputBydleni));
        inputSluzby.addTextChangedListener(new NumberTextWatcherForThousand(inputSluzby));
        inputHmoty.addTextChangedListener(new NumberTextWatcherForThousand(inputHmoty));
        inputDenni.addTextChangedListener(new NumberTextWatcherForThousand(inputDenni));
        inputPotraviny.addTextChangedListener(new NumberTextWatcherForThousand(inputPotraviny));
        inputSplatky.addTextChangedListener(new NumberTextWatcherForThousand(inputSplatky));
        inputZdravotny.addTextChangedListener(new NumberTextWatcherForThousand(inputZdravotny));
        inputJineNezbytne.addTextChangedListener(new NumberTextWatcherForThousand(inputJineNezbytne));

        infoBydleni.setText(Html.fromHtml("<b> Bydlení  </b> - Měsíční náklady vydané na bydlení - například nájem nebo hypotéka, elektřina, plyn, voda, odpad, poplatky za."));
        infoSluzby.setText(Html.fromHtml("<b> Služby </b> - Náklady na telefon, internet, kadeřníka, instalatéra atd."));
        infoHmoty.setText(Html.fromHtml("<b> Pohonné hmoty </b> - Měsíční náklady za pohonné hmoty či MHD kupón, autobus, vlak..."));
        infoDenni.setText(Html.fromHtml("<b> Předměty denní potřeby </b> - Měsíční náklady za běžné předměty jako je například toaletní papír, zubní pasta, kartáček, povlečení, žárovka, obuv..."));
        infoPotraviny.setText(Html.fromHtml("<b> Potraviny </b> - Částka, kterou v průměru měsíčně utratíte za nákup potravin. Do této položky nepočítejte útraty v restauraci."));
        infoSplatky.setText(Html.fromHtml("<b> Splátky úvěrů </b> - Měsíční částka, kterou vydáte na splácení vašich dluhů/úvěrů."));
        infoZdravotny.setText(Html.fromHtml("<b> Zdravotní péče </b> - Průměrné měsíční náklady za zubaře, lékaře, medikamenty..."));
        infoJineNezbytne.setText(Html.fromHtml("<b> Jiné nezbytné výdaje </b> - Nevešly se Vám nezbytné náklady do žádné skupiny? Použijte prosím tuto kolonku."));

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
                dataSetRequest.setHousing(inputBydleni.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputBydleni.getText().toString())));
                dataSetRequest.setServices(inputSluzby.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputSluzby.getText().toString())));
                dataSetRequest.setFuel(inputHmoty.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputHmoty.getText().toString())));
                dataSetRequest.setItems_daily_consumptions(inputDenni.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputDenni.getText().toString())));
                dataSetRequest.setFood(inputPotraviny.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputPotraviny.getText().toString())));
                dataSetRequest.setLoan_payment(inputSplatky.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputSplatky.getText().toString())));
                dataSetRequest.setHealth_care(inputZdravotny.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputZdravotny.getText().toString())));
                dataSetRequest.setOther_necessary_expenses(inputJineNezbytne.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputJineNezbytne.getText().toString())));
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

        goPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle i = new Bundle();

                DataSetRequest dataSetRequest = new DataSetRequest();
                dataSetRequest.setApi_token(api_token);
                dataSetRequest.setHousing(inputBydleni.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputBydleni.getText().toString())));
                dataSetRequest.setServices(inputSluzby.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputSluzby.getText().toString())));
                dataSetRequest.setFuel(inputHmoty.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputHmoty.getText().toString())));
                dataSetRequest.setItems_daily_consumptions(inputDenni.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputDenni.getText().toString())));
                dataSetRequest.setFood(inputPotraviny.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputPotraviny.getText().toString())));
                dataSetRequest.setLoan_payment(inputSplatky.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputSplatky.getText().toString())));
                dataSetRequest.setHealth_care(inputZdravotny.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputZdravotny.getText().toString())));
                dataSetRequest.setOther_necessary_expenses(inputJineNezbytne.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputJineNezbytne.getText().toString())));
                setData(dataSetRequest);

                i.putString("TOKEN", api_token);
                Log.e("TOKEN", "====>" + api_token);
                EnterDataFragment frag = new EnterDataFragment();
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
        Call<DataResponse> dataResponseCall = ApiClient.getService().enterData2(dataRequest);
        dataResponseCall.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, retrofit2.Response<DataResponse> response) {
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

                    inputBydleni.setText(housing != 0 ? String.valueOf(housing) : "");
                    inputSluzby.setText(services != 0 ? String.valueOf(services) : "");
                    inputHmoty.setText(fuel != 0 ? String.valueOf(fuel) : "");
                    inputDenni.setText(itemsDailyConsumptions != 0 ? String.valueOf(itemsDailyConsumptions) : "");
                    inputPotraviny.setText(food != 0 ? String.valueOf(food) : "");
                    inputSplatky.setText(loanPayment != 0 ? String.valueOf(loanPayment) : "");
                    inputZdravotny.setText(healthCare != 0 ? String.valueOf(healthCare) : "");
                    inputJineNezbytne.setText(otherNecessaryExpenses != 0 ? String.valueOf(otherNecessaryExpenses) : "");
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {

            }
        });
    }

    public void setData(DataSetRequest dataSetRequest){
        Call<DataResponse> dataResponseCall = ApiClient.getService().setData2(dataSetRequest);
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