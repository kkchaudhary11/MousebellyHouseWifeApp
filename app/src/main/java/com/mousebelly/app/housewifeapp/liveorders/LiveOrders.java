package com.mousebelly.app.housewifeapp.liveorders;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mousebelly.app.housewifeapp.APIs;
import com.mousebelly.app.housewifeapp.R;
import com.mousebelly.app.housewifeapp.SocketAccess;

import org.json.JSONObject;

import java.util.HashMap;

import io.socket.emitter.Emitter;

import static com.mousebelly.app.housewifeapp.liveorders.GetOrders.orderUnits;


public class LiveOrders extends Fragment {

    View v;
    public static LinearLayout liveOrderLinerLayout;
    public ProgressBar pg;
    private CardView cardView;
    public static  RadioGroup OrderViewoptions;
    private RadioButton allOrders;
    private RadioButton pendingOrders,cookedOrders,rejectedOrders,acceptedOrders;


    public static HashMap<String, OrdersUnit> filteredOrderUnits = new HashMap<>();


    public LiveOrders() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_live_orders, container, false);

        liveOrderLinerLayout = (LinearLayout) v.findViewById(R.id.ProductsLayout);
        pg = (ProgressBar)v.findViewById(R.id.products_Progress_Bar);
        cardView = (CardView)v.findViewById(R.id.option_card);
        cardView.setVisibility(View.GONE);

        OrderViewoptions  = (RadioGroup)v.findViewById(R.id.radiogroup);
        allOrders  = (RadioButton)v.findViewById(R.id.all_orders);
        pendingOrders = (RadioButton)v.findViewById(R.id.pending_orders);
        cookedOrders = (RadioButton)v.findViewById(R.id.cooked_orders);
        rejectedOrders = (RadioButton)v.findViewById(R.id.rejected_orders);
        acceptedOrders = (RadioButton)v.findViewById(R.id.accepted_orders);

        OrderViewoptions.check(allOrders.getId());

        allOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Order Orders");
                GetOrders getOrders = new GetOrders();
                getOrders.showOrders();
            }
        });

        pendingOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetOrders getOrders = new GetOrders();
                getOrders.showFilteredOrders("ordered");
            }
        });

        acceptedOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetOrders getOrders = new GetOrders();
                getOrders.showFilteredOrders("accepted");
            }
        });

        cookedOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetOrders getOrders = new GetOrders();
                getOrders.showFilteredOrders("cooked");
            }
        });

        rejectedOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetOrders getOrders = new GetOrders();
                getOrders.showFilteredOrders("rejected");
            }
        });


        SocketAccess.socket.on("liveorderadded", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("listeing...");
                try {
                    JSONObject productjson = new JSONObject(args[0].toString());
                    System.out.println("live order :  " + productjson);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        new LoadLiveOrders().execute();


        return v;
    }

    public class LoadLiveOrders extends AsyncTask<Void, Void, Void> {

        GetOrders getOrders = new GetOrders();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pg.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... voids) {

            getOrders.loadFood(APIs.order_order);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            pg.setVisibility(View.GONE);
            cardView.setVisibility(View.VISIBLE);
            //show the options
            getOrders.showOrders();


        }

    }

}
