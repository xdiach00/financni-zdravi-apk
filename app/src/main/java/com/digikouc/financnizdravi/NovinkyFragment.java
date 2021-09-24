package com.digikouc.financnizdravi;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digikouc.financnizdravi.Model.FeedAdapter;
import com.digikouc.financnizdravi.Model.HTTPDataHandler;
import com.digikouc.financnizdravi.Model.RSSObject;
import com.google.gson.Gson;


public class NovinkyFragment extends Fragment{

    RecyclerView recyclerView;
    RSSObject rssObject;

    //RSS link
    private final String RSS_link = "https://www.mfcr.cz/cs/rss/vybrana-temata";
    private final String RSS_to_Json_API = "https://api.rss2json.com/v1/api.json?rss_url=";
    private final String API_with_Token = "/&api_key=pr63ktl3v4vj7eqtmqs0tibik7id9rplswqhul0e&count=20";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = (View) inflater.inflate(R.layout.fragment_news, container, false);
        ImageView goPrevious = (ImageView) v.findViewById(R.id.imageGoPrevious2);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);

        loadRSS();





        goPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });

        return v;
    }

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
