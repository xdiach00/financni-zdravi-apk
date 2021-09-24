package com.digikouc.financnizdravi;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class PieChartFragment extends Fragment implements OnChartValueSelectedListener {

    float [] dataPie = new float[3]; //Chart input
    float sum_revenues, sum_necessary_expenses, sum_unnecessary_expences, savingSum, totalSaved;
    String namePie [] = {"Nezbytné výdaje", "Zbytné výdaje", "Úspory"};
    int[] colors = {Color.rgb(46,49,146), Color.rgb(28,200,138), Color.rgb(41,171,226)}; //Colours of chart parts
    static TextView revenues, necessary, unnecessary, savingsuspory, totalsavings;
    static PieChart chart;
    List<PieEntry> pieEntries;
    PieDataSet dataSet;
    String api_token;
    NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMAN);
    DecimalFormat decim = (DecimalFormat)nf;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = (View) inflater.inflate(R.layout.fragment_pie_chart, container, false);
        chart = (PieChart) v.findViewById(R.id.pie_chart);
        revenues = (TextView) v.findViewById(R.id.tableRevenue);
        necessary = (TextView) v.findViewById(R.id.tableNecessery);
        unnecessary = (TextView) v.findViewById(R.id.tableUnnecessary);
        savingsuspory = (TextView) v.findViewById(R.id.tableUspory);
        totalsavings = (TextView) v.findViewById(R.id.tableSavings);
        ImageView goPrevious = (ImageView) v.findViewById(R.id.imageGoPrevious);
        ImageView goNext = (ImageView) v.findViewById(R.id.imageGoNext);
        DataRequest dataRequest = new DataRequest();
        Bundle bundle = this.getArguments();
        api_token = bundle.getString("TOKEN");

        Toast.makeText(getActivity(), "Při kliknutí na část grafu dojdete k zobrazení rozpadu", Toast.LENGTH_SHORT).show();

        dataRequest.setApi_token(api_token);
        getData(dataRequest);

        chart.setDrawEntryLabels(false);
        chart.setDrawHoleEnabled(false);
        chart.animateX(500);
        chart.getDescription().setEnabled(false);
        chart.setOnChartValueSelectedListener(this);

        //Char legend params
        Legend legend = chart.getLegend();
        legend.setTextSize(16f);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        goPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });

        goNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new NovinkyFragment());
                fr.addToBackStack(null).commit();
            }
        });

        return v;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("I clicked on", String.valueOf(h.getX()) );

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
                    sum_unnecessary_expences = (float) dataResponse.getData().getSum_unnecessary_expenses();
                    savingSum = (float) dataResponse.getData().getSavingsSum();
                    totalSaved = (float) dataResponse.getData().getTotalSaved();

                    if((int)sum_necessary_expenses != 0 && (int)sum_unnecessary_expences != 0) {
                        dataPie[0] = (float) sum_necessary_expenses;
                        dataPie[1] = (float) sum_unnecessary_expences;
                        dataPie[2] = (float) savingSum;

                        revenues.setText(String.valueOf(decim.format((int) sum_revenues)) + " Kč");
                        necessary.setText(String.valueOf(decim.format((int) sum_necessary_expenses)) + " Kč");
                        unnecessary.setText(String.valueOf(decim.format((int) sum_unnecessary_expences)) + " Kč");
                        savingsuspory.setText(String.valueOf(decim.format((int) savingSum)) + " Kč");
                        totalsavings.setText(String.valueOf(decim.format((int) totalSaved)) + " Kč");

                    } else{
                        dataPie[0] = 101f;
                        dataPie[1] = 101f;
                        dataPie[2] = 101f;
                    }

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
                    chart.setUsePercentValues(true);
                    chart.animate();
                    chart.invalidate();




                    Log.e("Ness", "=====>" + sum_necessary_expenses);
                    Log.e("Unn", "=====>" + sum_unnecessary_expences);
                    Log.e("SavingSum", "=====>" + savingSum);
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {

            }
        });
    }
}
