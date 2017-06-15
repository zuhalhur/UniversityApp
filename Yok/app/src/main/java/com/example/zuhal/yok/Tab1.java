package com.example.zuhal.yok;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
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
public class Tab1 extends Fragment {
    ProgressDialog mProgressDialog;
    ArrayList<String> announcements;
    ArrayAdapter<String>arrayAdapter;
    ListView announcementList;
    ArrayList<String>htmls;
    LinearLayout duyuru;

    public Tab1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_tab1, container, false);
        Log.i("oncreateView","calıstı");
        announcements =new ArrayList<String>();
        announcementList=(ListView)view.findViewById(R.id.announcementList);
        duyuru=(LinearLayout)view.findViewById(R.id.duyuru);
        htmls=new ArrayList<String>();
        new Announcement().execute();
        announcementList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(),OpenUrl.class);
                intent.putExtra("web","http://ybu.edu.tr/muhendislik/bilgisayar/"+htmls.get(position));
                startActivity(intent);
            }
        });

        duyuru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),OpenUrl.class);
                intent.putExtra("web","http://www.ybu.edu.tr/muhendislik/bilgisayar/content_list-257-duyurular.html");
                startActivity(intent);
            }
        });
        return view;
    }


    private class Announcement extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setTitle("Please wait !!");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                Document doc;
                Elements element;
                doc = Jsoup.connect("http://ybu.edu.tr/muhendislik/bilgisayar/").get();
                element = doc.select("div.caContent");
                element =element.select("div.cncItem");
                for( int i=0;i<element.size();i++){
                    announcements.add(element.get(i).text());
                    Elements element3 =  element.get(i).select("span > a");
                    String url = element3.attr("href");
                    htmls.add(url);

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
            arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.text_layout, announcements){
                @Override
                public View getView(int position, View convertView, ViewGroup parent){
                    ViewGroup.LayoutParams layoutparams;
                    View view = super.getView(position,convertView,parent);

                    layoutparams = view.getLayoutParams();

                    //Define your height here.
                    layoutparams.height = height/(announcements.size()+1);

                    view.setLayoutParams(layoutparams);

                    return view;
                }
            };

            announcementList.setAdapter(arrayAdapter);

            mProgressDialog.dismiss();
        }
    }
    public void onPause(){
        super.onPause();
        Log.i("onpause","çalıştı");
    }

}
