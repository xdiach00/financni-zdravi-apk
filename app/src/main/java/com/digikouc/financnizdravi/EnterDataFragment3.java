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

public class EnterDataFragment3 extends Fragment {

    private View layoutEnterMain, layoutInfoMain;
    private int mScreen;
    private static final int SCREEN1 = 0;
    private static final int SCREEN2 = 1;
    float x1, x2, y1, y2; //Coordinates for detecting swiping
    int entertainment, restaurant, sport, shopping, hobby, travel, otherUnnecessaryExpenses;
    TextInputEditText inputZabava, inputRestaurace, inputSport, inputNakupovani, inputKonicky, inputCestovani, inputJineZbytne;
    String api_token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = (View) inflater.inflate(R.layout.fragment_enterdata_3, container, false);
        inputZabava = (TextInputEditText) v.findViewById(R.id.inputZabava);
        inputRestaurace = (TextInputEditText) v.findViewById(R.id.inputRestaurace);
        inputSport = (TextInputEditText) v.findViewById(R.id.inputSport);
        inputNakupovani = (TextInputEditText) v.findViewById(R.id.inputNakupovani);
        inputKonicky = (TextInputEditText) v.findViewById(R.id.inputKonicky);
        inputCestovani = (TextInputEditText) v.findViewById(R.id.inputCestovani);
        inputJineZbytne = (TextInputEditText) v.findViewById(R.id.inputJineZbytne);
        TextView infoZabava = (TextView) v.findViewById(R.id.textZabava);
        TextView infoRestaurace = (TextView) v.findViewById(R.id.textRestaurace);
        TextView infoSport = (TextView) v.findViewById(R.id.textSport);
        TextView infoNakupovani = (TextView) v.findViewById(R.id.textNakupovani);
        TextView infoKonicky = (TextView) v.findViewById(R.id.textKonicky);
        TextView infoCestovani = (TextView) v.findViewById(R.id.textCestovani);
        TextView infoJineZbytne = (TextView) v.findViewById(R.id.textJineZbytne);
        ImageView change_second = (ImageView) v.findViewById(R.id.change_second);
        ImageView change_first = (ImageView) v.findViewById(R.id.change_first);
        ImageView goNext = (ImageView) v.findViewById(R.id.imageGoNext);
        ImageView goPrevious = (ImageView) v.findViewById(R.id.imageGoPrevious);
        layoutEnterMain = (RelativeLayout) v.findViewById(R.id.enterDataThird);
        layoutInfoMain = (RelativeLayout) v.findViewById(R.id.infoThirdEnterData);
        DataRequest dataRequest = new DataRequest();

        Bundle bundle = this.getArguments();
        api_token = bundle.getString("TOKEN");
        Log.e("TOKEN3", "=====>" + api_token);

        dataRequest.setApi_token(api_token);
        enterData(dataRequest);

        inputZabava.addTextChangedListener(new NumberTextWatcherForThousand(inputZabava));
        inputRestaurace.addTextChangedListener(new NumberTextWatcherForThousand(inputRestaurace));
        inputSport.addTextChangedListener(new NumberTextWatcherForThousand(inputSport));
        inputNakupovani.addTextChangedListener(new NumberTextWatcherForThousand(inputNakupovani));
        inputKonicky.addTextChangedListener(new NumberTextWatcherForThousand(inputKonicky));
        inputCestovani.addTextChangedListener(new NumberTextWatcherForThousand(inputCestovani));
        inputJineZbytne.addTextChangedListener(new NumberTextWatcherForThousand(inputJineZbytne));

        infoZabava.setText(Html.fromHtml("<b> Zábava  </b> - Měsíční náklady na zábavu, např. kino, streamovací služby, hry, sázky atp."));
        infoRestaurace.setText(Html.fromHtml("<b> Restaurace </b> - Částka kterou průměrně za měsíc utratíte v restauraci, kavárnách, občerstveních."));
        infoSport.setText(Html.fromHtml("<b> Sport a wellness </b> - Průměrné měsíční výdaje za sport a wellness. Patří sem například permanentka do posilovny, masáže, kurzy atd."));
        infoNakupovani.setText(Html.fromHtml("<b> Nakupování </b> - Kolik průměrně utratíte za nákup zbytných věcí měsíčně? Spadá sem elektronika, knihy, nákupy oblečení, šperků atd."));
        infoKonicky.setText(Html.fromHtml("<b> Koníčky </b> - Průměrný měsíční výdaj za vaše koníčky"));
        infoCestovani.setText(Html.fromHtml("<b> Cestování </b> - Průměrné měsíční náklady na cestování. Nejedná se o cesty do práce nebo na nákupy. Patří sem výlety a dovolené."));
        infoJineZbytne.setText(Html.fromHtml("<b> Jiné zbytné výdaje </b> - Jakékoliv jiné zbytné výdaje, které se nevešly do některé ze skupin výše."));

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
                dataSetRequest.setEntertainment(inputZabava.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputZabava.getText().toString())));
                dataSetRequest.setRestaurant(inputRestaurace.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputRestaurace.getText().toString())));
                dataSetRequest.setSport(inputSport.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputSport.getText().toString())));
                dataSetRequest.setShopping(inputNakupovani.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputNakupovani.getText().toString())));
                dataSetRequest.setHobby(inputKonicky.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputKonicky.getText().toString())));
                dataSetRequest.setTravel(inputCestovani.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputCestovani.getText().toString())));
                dataSetRequest.setOther_unnecessary_expenses(inputJineZbytne.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputJineZbytne.getText().toString())));
                setData(dataSetRequest);

                i.putString("TOKEN", api_token);
                Log.e("TOKEN", "====>" + api_token);
                EnterDataFragment4 frag = new EnterDataFragment4();
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
                dataSetRequest.setEntertainment(inputZabava.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputZabava.getText().toString())));
                dataSetRequest.setRestaurant(inputRestaurace.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputRestaurace.getText().toString())));
                dataSetRequest.setSport(inputSport.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputSport.getText().toString())));
                dataSetRequest.setShopping(inputNakupovani.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputNakupovani.getText().toString())));
                dataSetRequest.setHobby(inputKonicky.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputKonicky.getText().toString())));
                dataSetRequest.setTravel(inputCestovani.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputCestovani.getText().toString())));
                dataSetRequest.setOther_unnecessary_expenses(inputJineZbytne.getText().toString().matches("") ? 0.00d : Double.valueOf(NumberTextWatcherForThousand.trimSpaceOfString(inputJineZbytne.getText().toString())));
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
        Call<DataResponse> dataResponseCall = ApiClient.getService().enterData3(dataRequest);
        dataResponseCall.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, retrofit2.Response<DataResponse> response) {
                if(response.isSuccessful()){
                    DataResponse dataResponse = response.body();
                    entertainment = (int) dataResponse.getData().getEntertainment();
                    restaurant = (int) dataResponse.getData().getRestaurant();
                    sport = (int) dataResponse.getData().getSport();
                    shopping = (int) dataResponse.getData().getShopping();
                    hobby = (int) dataResponse.getData().getHobby();
                    travel = (int) dataResponse.getData().getTravel();
                    otherUnnecessaryExpenses = (int) dataResponse.getData().getOtherUnnecessaryExpenses();

                    inputZabava.setText(entertainment != 0 ? String.valueOf(entertainment) : "");
                    inputRestaurace.setText(restaurant != 0 ? String.valueOf(restaurant) : "");
                    inputSport.setText(sport != 0 ? String.valueOf(sport) : "");
                    inputNakupovani.setText(shopping != 0 ? String.valueOf(shopping) : "");
                    inputKonicky.setText(hobby != 0 ? String.valueOf(hobby) : "");
                    inputCestovani.setText(travel != 0 ? String.valueOf(travel) : "");
                    inputJineZbytne.setText(otherUnnecessaryExpenses != 0 ? String.valueOf(otherUnnecessaryExpenses) : "");
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {

            }
        });
    }

    public void setData(DataSetRequest dataSetRequest){
        Call<DataResponse> dataResponseCall = ApiClient.getService().setData3(dataSetRequest);
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