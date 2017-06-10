package com.mousebelly.app.housewifeapp.myfood;

import android.widget.RelativeLayout;

import com.mousebelly.app.housewifeapp.APIs;
import com.mousebelly.app.housewifeapp.EmptyMessage;
import com.mousebelly.app.housewifeapp.Login.LoginActivity;
import com.mousebelly.app.housewifeapp.MainActivity;
import com.mousebelly.app.housewifeapp.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Kamal Kant on 03-06-2017.
 */

public class GetMyFood {

    public static HashMap<String, MyFoodUnit> foodUnits = new HashMap<>();

    public void loadFood(){

        String resp = Server.s.get(APIs.prod_approval_prod_hwf+ LoginActivity.USERID);

        try {
            JSONArray jsonArray = new JSONArray(resp);

            for(int i = 0 ; i<jsonArray.length(); i++) {
                JSONObject foodJson = jsonArray.getJSONObject(i);
                MyFoodUnit myFoodUnit = new MyFoodUnit();

                myFoodUnit.set_id(foodJson.getString("_id"));
                myFoodUnit.setProd_name(foodJson.getString("Prod_name"));
                myFoodUnit.setProd_category(foodJson.getString("Prod_category"));
                myFoodUnit.setDescription(foodJson.getString("Description"));
                myFoodUnit.setPrice(foodJson.getString("Price"));
                myFoodUnit.setProd_id(foodJson.getString("Prod_id"));
                myFoodUnit.setFeedback(foodJson.getString("feedback"));
                myFoodUnit.setIsApproved(foodJson.getString("isApproved"));
                myFoodUnit.setIsRejected(foodJson.getString("isRejected"));
                myFoodUnit.setImage(foodJson.getString("Image"));
                myFoodUnit.setStarsize(foodJson.getString("starsize"));
                myFoodUnit.setSalePrice(foodJson.getString("salePrice"));

                foodUnits.put(myFoodUnit.getProd_id(),myFoodUnit);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(resp);


    }

    public void showFood(){

        if (foodUnits.isEmpty()) {
            RelativeLayout rl = EmptyMessage.show(MainActivity.context, "No Item");
            MyFood.productsLayout.addView(rl);
            return;
        }

        Iterator it = foodUnits.keySet().iterator();

        while (it.hasNext()) {
            String key = it.next().toString();
             MyFoodUnit myFoodUnit= new MyFoodUnit();
             MyFoodUnit f = foodUnits.get(key);

            //RelativeLayout rl = favItem.draw(f);

            MyFood.productsLayout.addView(myFoodUnit.draw(f));
        }

    }

}
