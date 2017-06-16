package com.mousebelly.app.housewifeapp.liveorders;

import android.widget.RelativeLayout;

import com.mousebelly.app.housewifeapp.EmptyMessage;
import com.mousebelly.app.housewifeapp.Login.LoginActivity;
import com.mousebelly.app.housewifeapp.MainActivity;
import com.mousebelly.app.housewifeapp.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import static com.mousebelly.app.housewifeapp.liveorders.LiveOrders.filteredOrderUnits;

/**
 * Created by Kamal Kant on 03-06-2017.
 */

public class GetOrders {

    public static HashMap<String, OrdersUnit> orderUnits = new HashMap<>();

    public void loadFood(String url){

        String resp = Server.s.get(url);

        try {
            JSONArray jsonArray = new JSONArray(resp);

            for(int i = 0 ; i<jsonArray.length(); i++) {

                try {
                    JSONObject foodJson = jsonArray.getJSONObject(i);
                    OrdersUnit myFoodUnit = new OrdersUnit();

                    String _id = (foodJson.getString("_id"));
                    String email = (foodJson.getString("UserEmail_id"));
                    String Total_bill = (foodJson.getString("Total_bill"));
                    String Payment_status = (foodJson.getString("Payment_status"));
                    String order_date = (foodJson.getString("order_date"));
                    String order_Id = (foodJson.getString("order_Id"));

                    JSONObject productDetails = foodJson.getJSONObject("Prod_details");

                    Iterator iterator = productDetails.keys();

                    while (iterator.hasNext()) {
                        String key = iterator.next().toString();

                        JSONObject product = productDetails.getJSONObject(key);

                        String HwfId = product.getString("HWID");

                        if (HwfId.equals(LoginActivity.USERID) && Payment_status.equals("Success")) {

                            myFoodUnit.set_id(_id);
                            myFoodUnit.setUserEmail_id(email);
                            myFoodUnit.setTotal_bill(Total_bill);
                            myFoodUnit.setPayment_status(Payment_status);
                            myFoodUnit.setOrder_date(order_date);
                            myFoodUnit.setOrder_Id(order_Id);

                            myFoodUnit.setHWID(HwfId);
                            myFoodUnit.setPID(product.getString("PID"));
                            myFoodUnit.setProduct_Name(product.getString("Product_Name"));
                            myFoodUnit.setPrice_item(product.getString("Price_item"));
                            myFoodUnit.setStatus(product.getString("status"));
                            myFoodUnit.setQty(product.getString("Qty"));
                            myFoodUnit.setStars(product.getString("stars"));
                            myFoodUnit.setImage(product.getString("Image"));

                            orderUnits.put(myFoodUnit.get_id(), myFoodUnit);

                        }


                    }


                }catch (Exception e){
                    e.printStackTrace();
                    continue;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("Order Hash Map : " + orderUnits);


    }

    public void showOrders(){

        LiveOrders.liveOrderLinerLayout.removeAllViews();

        if (orderUnits.isEmpty()) {
            RelativeLayout rl = EmptyMessage.show(MainActivity.context, "No Item");
            LiveOrders.liveOrderLinerLayout.addView(rl);
            return;
        }

        Iterator it = orderUnits.keySet().iterator();

        while (it.hasNext()) {
            String key = it.next().toString();
             OrdersUnit myFoodUnit= new OrdersUnit();
             OrdersUnit f = orderUnits.get(key);

            //RelativeLayout rl = favItem.draw(f);

            LiveOrders.liveOrderLinerLayout.addView(myFoodUnit.draw(f));
        }



    }

    public void showFilteredOrders(String status){


        filteredOrderUnits.clear();
        System.out.println(status);
        for (OrdersUnit o : orderUnits.values()) {

            if(o.getStatus().equals(status)){
                filteredOrderUnits.put(o.get_id(),o);
            }
        }

        System.out.println("filteredOrderUnits : "+filteredOrderUnits);

        LiveOrders.liveOrderLinerLayout.removeAllViews();

        if (filteredOrderUnits.isEmpty()) {
            RelativeLayout rl = EmptyMessage.show(MainActivity.context, "No Item");
            LiveOrders.liveOrderLinerLayout.addView(rl);
            return;
        }

        Iterator it = filteredOrderUnits.keySet().iterator();

        while (it.hasNext()) {
            String key = it.next().toString();
            OrdersUnit myFoodUnit= new OrdersUnit();
            OrdersUnit f = filteredOrderUnits.get(key);

            //RelativeLayout rl = favItem.draw(f);

            LiveOrders.liveOrderLinerLayout.addView(myFoodUnit.draw(f));
        }

    }



}
