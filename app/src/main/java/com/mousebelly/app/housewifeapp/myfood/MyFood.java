package com.mousebelly.app.housewifeapp.myfood;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.mousebelly.app.housewifeapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFood extends Fragment {

    public static LinearLayout productsLayout;
    View v;
    ProgressBar pg;

    public MyFood() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_my_food, container, false);

        MyFood.productsLayout = (LinearLayout) v.findViewById(R.id.ProductsLayout);
        pg = (ProgressBar)v.findViewById(R.id.products_Progress_Bar);

        new LoadProducts().execute();

        return v;
    }


    public class LoadProducts extends AsyncTask<Void, Void, Void> {

        GetMyFood getProductsData = new GetMyFood();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pg.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... voids) {

            getProductsData.loadFood();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            pg.setVisibility(View.GONE);
            //show the options
            getProductsData.showFood();


        }

    }

}
