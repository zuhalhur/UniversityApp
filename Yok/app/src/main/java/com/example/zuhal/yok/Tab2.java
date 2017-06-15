package com.example.zuhal.yok;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Tab2 extends Fragment {

    ListView mealList;
    TextView date;
    ArrayList<String>lists;
    ArrayAdapter<String>adapter;
    ArrayList<String>meals;
    ImageView image;
    LinearLayout yemekler;
    public Tab2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_tab2, container, false);
        mealList=(ListView)view.findViewById(R.id.mealList);
        image=(ImageView)view.findViewById(R.id.liste);
        yemekler=(LinearLayout)view.findViewById(R.id.yemek);
        meals=new ArrayList<>();
        lists=new ArrayList<>();
        date=(TextView)view.findViewById(R.id.date);
        new Meal().execute();
        yemekler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent();
                i.setAction(Intent.ACTION_VIEW);
                i.addCategory(Intent.CATEGORY_BROWSABLE);
                i.setData(Uri.parse("http://ybu.edu.tr/sks/contents/files/MAYIS%20MEN%C3%9C%20YEN%C4%B0%20(1).pdf"));
                startActivity(i);

            }
        });
        return view;
    }
    private class Meal extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document document = Jsoup.connect("http://ybu.edu.tr/sks/").get();
                Elements td = document.select("table table td h5");
                Elements tr=document.select("table table td h3");
                lists.add(tr.text());
                for(int i=0;i<td.size();i++){
                    TextNode e2 = (TextNode) td.get(i).childNode(0);
                    meals.add(e2.text());

                }

            }catch (IOException e){
                e.printStackTrace();
            }

            return  null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            final int height = metrics.heightPixels;
            adapter = new ArrayAdapter<String>(getActivity(),R.layout.text_layout, meals){
                @Override
                public View getView(int position, View convertView, ViewGroup parent){
                    ViewGroup.LayoutParams layoutparams;
                    View view = super.getView(position,convertView,parent);

                    layoutparams = view.getLayoutParams();

                    //Define your height here.
                    layoutparams.height = height/(meals.size()+1);

                    view.setLayoutParams(layoutparams);

                    return view;
                }
            };
            if (mealList != null) {
                mealList.setAdapter(adapter);
                date.setText(lists.get(0));
            }
        }

    }

}
