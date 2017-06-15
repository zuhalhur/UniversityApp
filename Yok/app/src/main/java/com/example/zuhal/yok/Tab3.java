package com.example.zuhal.yok;



import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Tab3 extends Fragment {

    ListView newsList;
    ArrayAdapter<String> adapter1;
    ArrayList<String> news;
    ArrayList<String>html;
    LinearLayout haber;


    public Tab3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_tab3, container, false);
        news =new ArrayList<String>();
        newsList=(ListView)view.findViewById(R.id.newsList);
        haber=(LinearLayout)view.findViewById(R.id.haber);
        html=new ArrayList<String>();
        new News().execute();
        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(),OpenUrl.class);
                intent.putExtra("web","http://ybu.edu.tr/muhendislik/bilgisayar/"+html.get(position));
                startActivity(intent);


            }
        });
        haber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),OpenUrl.class);
                intent.putExtra("web","http://www.ybu.edu.tr/muhendislik/bilgisayar/content_list-314-haberler.html");
                startActivity(intent);

            }
        });
        return view;
    }

    private class News extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to the web site
                Document doc;
                Elements element;
                doc = Jsoup.connect("http://ybu.edu.tr/muhendislik/bilgisayar/").get();
                element = doc.select("div.cnContent");
                element =element.select("div.cncItem");
                for( int i=0;i<element.size();i++){
                    news.add(element.get(i).text());
                    Elements element3 =  element.get(i).select("span > a");
                    String url = element3.attr("href");
                    html.add(url);
                }

            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            final int height = metrics.heightPixels;
            adapter1 = new ArrayAdapter<String>(getActivity(),R.layout.text_layout, news){
                @Override
                public View getView(int position, View convertView, ViewGroup parent){
                    ViewGroup.LayoutParams layoutparams;
                    View view = super.getView(position,convertView,parent);

                    layoutparams = view.getLayoutParams();

                    //Define your height here.
                    layoutparams.height = height/(news.size()+1);

                    view.setLayoutParams(layoutparams);

                    return view;
                }
            };

            newsList.setAdapter(adapter1);

        }
    }

}
