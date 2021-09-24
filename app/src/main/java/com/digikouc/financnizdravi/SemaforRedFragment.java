package com.digikouc.financnizdravi;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class SemaforRedFragment extends Fragment {

    String api_token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = (View) inflater.inflate(R.layout.fragment_semafor_red, container, false);
        ImageView goNext = (ImageView) v.findViewById(R.id.imageGoNext);

        Bundle bundle = this.getArguments();
        api_token = bundle.getString("TOKEN");

        goNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle i = new Bundle();

                i.putString("TOKEN", api_token);
                Log.e("TOKEN", "====>" + api_token);
                PieChartFragment frag = new PieChartFragment();
                frag.setArguments(i);
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, frag);
                fr.addToBackStack(null);
                fr.commit();
            }
        });

        return v;
    }

}