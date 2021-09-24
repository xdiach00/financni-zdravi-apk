package com.digikouc.financnizdravi;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digikouc.financnizdravi.Model.FeedAdapter;
import com.digikouc.financnizdravi.Model.HTTPDataHandler;
import com.digikouc.financnizdravi.Model.RSSObject;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class DashboardFragment extends Fragment implements OnChartValueSelectedListener {

    float x1, x2, y1, y2; //Coordinates for detecting swiping
    float [] dataPie = new float[3]; //Chart input
    float sum_revenues, sum_necessary_expenses, sum_unnecessary_expenses, savingSum, totalSaved;
    String namePie [] = {"Nezbytné výdaje", "Zbytné výdaje", "Úspory"};
    int[] colors = {Color.rgb(46,49,146), Color.rgb(28,200,138), Color.rgb(41,171,226)}; //Colours of chart parts
    private View mLayout1, mLayout2;
    static RelativeLayout textAbout, table;
    private int mScreen;
    private static final int SCREEN1 = 0;
    private static final int SCREEN2 = 1;
    static TextView revenues, necessary, unnecessary, savingsuspory, totalsavings;
    static PieChart chart;
    List<PieEntry> pieEntries;
    PieDataSet dataSet;
    static String api_token;
    NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMAN);
    DecimalFormat decim = (DecimalFormat)nf;
    RecyclerView recyclerView;
    RSSObject rssObject;

    //RSS link
    private final String RSS_link = "https://www.mfcr.cz/cs/rss/vybrana-temata";
    private final String RSS_to_Json_API = "https://api.rss2json.com/v1/api.json?rss_url=";
    private final String API_with_Token = "/&api_key=pr63ktl3v4vj7eqtmqs0tibik7id9rplswqhul0e&count=20";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = (View) inflater.inflate(R.layout.fragment_dashboard, container, false);
        chart = v.findViewById(R.id.pie_chart);
        ImageView change_second = (ImageView) v.findViewById(R.id.change_second);
        ImageView change_first = (ImageView) v.findViewById(R.id.change_first);
        mLayout1 = (RelativeLayout) v.findViewById(R.id.layout_chart);
        mLayout2 = (RelativeLayout) v.findViewById(R.id.layout_news);
        textAbout = (RelativeLayout) v.findViewById(R.id.dashboardTextAbout);
        table = (RelativeLayout) v.findViewById(R.id.dashboardTable);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);

        loadRSS();

        revenues = (TextView) v.findViewById(R.id.tableRevenue);
        necessary = (TextView) v.findViewById(R.id.tableNecessery);
        unnecessary = (TextView) v.findViewById(R.id.tableUnnecessary);
        savingsuspory = (TextView) v.findViewById(R.id.tableUspory);
        totalsavings = (TextView) v.findViewById(R.id.tableSavings);
        api_token = this.getArguments().getString("TOKEN");
        DataRequest dataRequest = new DataRequest();

        chart.setVisibility(View.INVISIBLE);
        table.setVisibility(View.GONE);
        textAbout.setVisibility(View.VISIBLE);

        dataRequest.setApi_token(api_token);
        getData(dataRequest);

        Log.e("Data ", String.valueOf(dataPie[0]));

        chart.setDrawEntryLabels(false);
        chart.setDrawHoleEnabled(false);
        chart.animateX(500);
        chart.getDescription().setEnabled(false);
        chart.setOnChartValueSelectedListener(this);


        //Char legend params
        Legend legend = chart.getLegend();
        legend.setTextSize(16f);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        //onTouchListener to detect swipes for changing between activities
        //Сделать смену через переменные: 0 - первый экран, 1 - второй экран, 2 - третий экран
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
        mLayout1.setVisibility(mScreen == SCREEN1 ? View.VISIBLE : View.GONE);
        mLayout2.setVisibility(mScreen == SCREEN2 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onValueSelected(Entry entry, Highlight h) {
        Log.e("I clicked on", String.valueOf(h.getX()) );

        Bundle i = new Bundle();
        i.putString("TOKEN", api_token);

        if((int) h.getX() == 0){
            ShowDataNecessaryFragment frag = new ShowDataNecessaryFragment();
            frag.setArguments(i);
            FragmentTransaction fr = getFragmentManager().beginTransaction();
            fr.replace(R.id.fragment_container, frag);
            fr.addToBackStack(null);
            fr.commit();
        }
        else if((int) h.getX() == 1){
            ShowDataUnnecessaryFragment frag = new ShowDataUnnecessaryFragment();
            frag.setArguments(i);
            FragmentTransaction fr = getFragmentManager().beginTransaction();
            fr.replace(R.id.fragment_container, frag);
            fr.addToBackStack(null);
            fr.commit();
        }
        else if((int) h.getX() == 2){
            ShowDataSavingsFragment frag = new ShowDataSavingsFragment();
            frag.setArguments(i);
            FragmentTransaction fr = getFragmentManager().beginTransaction();
            fr.replace(R.id.fragment_container, frag);
            fr.addToBackStack(null);
            fr.commit();
        }
    }

    @Override
    public void onNothingSelected() {

    }

    public void getData(DataRequest dataRequest){
        Call<DataResponse> dataResponseCall = ApiClient.getService().getData(dataRequest);
        dataResponseCall.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, retrofit2.Response<DataResponse> response) {
                if(response.isSuccessful()){
                    DataResponse dataResponse = response.body();
                    sum_revenues = (float) dataResponse.getData().getSum_revenues();
                    sum_necessary_expenses = (float) dataResponse.getData().getSum_necessary_expenses();
                    sum_unnecessary_expenses = (float) dataResponse.getData().getSum_unnecessary_expenses();
                    savingSum = (float) dataResponse.getData().getSavingsSum();
                    totalSaved = (float) dataResponse.getData().getTotalSaved();

                    if((int)sum_necessary_expenses != 0 && (int)sum_unnecessary_expenses != 0) {
                        dataPie[0] = (float) sum_necessary_expenses;
                        dataPie[1] = (float) sum_unnecessary_expenses;
                        dataPie[2] = (float) savingSum;

                        textAbout.setVisibility(View.GONE);
                        table.setVisibility(View.VISIBLE);

                        revenues.setText(String.valueOf(decim.format((int) sum_revenues)) + " Kč");
                        necessary.setText(String.valueOf(decim.format((int) sum_necessary_expenses)) + " Kč");
                        unnecessary.setText(String.valueOf(decim.format((int) sum_unnecessary_expenses)) + " Kč");
                        savingsuspory.setText(String.valueOf(decim.format((int) savingSum)) + " Kč");
                        totalsavings.setText(String.valueOf(decim.format((int) totalSaved)) + " Kč");

                        Toast.makeText(getActivity(), "Kliknutím na graf zobrazíte další informace", Toast.LENGTH_SHORT).show();

                    } else{
                        dataPie[0] = 101f;
                        dataPie[1] = 101f;
                        dataPie[2] = 101f;
                    }

                    chart.setVisibility(View.VISIBLE);

                    pieEntries = new ArrayList<>();
                    for (int i = 0; i < dataPie.length; i++){
                        pieEntries.add(new PieEntry( dataPie[i], namePie[i]));
                    }

                    dataSet = new PieDataSet(pieEntries, " ");
                    dataSet.setColors(colors);
                    dataSet.setValueTextColor(Color.WHITE);
                    dataSet.setValueTextSize(18f);
                    PieData data = new PieData(dataSet);

                    //Get the chart:

                    chart.setData(data);
                    chart.getDescription().setEnabled(false);
                    dataSet.setValueFormatter(new DecimalRemover(chart));
                    //chart.setUsePercentValues(true);
                    chart.setUsePercentValues(true);
                    chart.animate();
                    chart.invalidate();




                    Log.e("Ness", "=====>" + sum_necessary_expenses);
                    Log.e("Unn", "=====>" + sum_unnecessary_expenses);
                    Log.e("SavingSum", "=====>" + savingSum);
                } else {
                    Toast.makeText(getActivity(), "Chyba při připojení k serveru. Zkuste se znovu přihlásit.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Chyba při připojení k serveru. Zkontrolujte prosím připojení k internetu nebo zkuste později.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBackPressed() { getActivity().finish();}

    private void loadRSS() {
        AsyncTask<String, String, String> loadRSSAsync = new AsyncTask<String, String, String>() {

            ProgressDialog mDialog = new ProgressDialog(getActivity());

            @Override
            protected void onPreExecute() {
                mDialog.setMessage("Počkejte, prosím...");
                mDialog.show();
            }

            @Override
            protected String doInBackground(String... params) {
                String result;
                HTTPDataHandler http = new HTTPDataHandler();
                result = http.GetHTTPData(params[0]);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                mDialog.dismiss();
                rssObject = new Gson().fromJson(s, RSSObject.class);
                FeedAdapter adapter = new FeedAdapter(rssObject, getActivity().getBaseContext());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        };

        StringBuilder url_get_data = new StringBuilder(RSS_to_Json_API);
        url_get_data.append(RSS_link);
        url_get_data.append(API_with_Token);
        loadRSSAsync.execute(url_get_data.toString());
    }

}
