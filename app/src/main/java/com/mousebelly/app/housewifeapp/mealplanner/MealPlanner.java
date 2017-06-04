package com.mousebelly.app.housewifeapp.mealplanner;


import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.mousebelly.app.housewifeapp.MainActivity;
import com.mousebelly.app.housewifeapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MealPlanner extends Fragment {

    View v;

    public ProgressBar pg;
    LinearLayout productsDataLayout;
    LinearLayout mealPlanLinearLayout;


    public MealPlanner() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_meal_planner, container, false);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        pg   = (ProgressBar)v.findViewById(R.id.pg);
        productsDataLayout = (LinearLayout)v.findViewById(R.id.ProductsDataLayout);

        mealPlanLinearLayout = (LinearLayout) v.findViewById(R.id.MealPlanLinearLayout);



        new LoadProducts().execute();
        // Inflate the layout for this fragment
        return v;
    }



    public class LoadProducts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pg.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            GetProductsData gpd = new GetProductsData("sunita@gmail.com",MainActivity.context,productsDataLayout);
            gpd.loadData();
            return null;

        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new LoadMeal().execute();
        }


    }


    public class LoadMeal extends AsyncTask<Void, Void, Void> {

        GetMealPlan gmp = new GetMealPlan("sunita@gmail.com",MainActivity.context,mealPlanLinearLayout);

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pg.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            gmp.getData();
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            gmp.loadData();
            pg.setVisibility(View.GONE);
        }


    }

}
