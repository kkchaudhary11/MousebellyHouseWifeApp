package com.mousebelly.app.housewifeapp.mealplanner;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mousebelly.app.housewifeapp.APIs;
import com.mousebelly.app.housewifeapp.Server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Vasudev on 21/03/2017.
 */

class AddUI
{


    LinearLayout l;
    RelativeLayout r;

    public AddUI(LinearLayout lin , RelativeLayout rel) {
        this.l = lin;
        this.r = rel;

        new Handler(Looper.getMainLooper()).

                postDelayed(new Runnable() {
                    @Override
                    public void run () {
                        l.addView(r);
                        //selectedCheckBox.setChecked(true);
                    }
                },100);
    }

}

public class GetProductsData
{
    private String houseWifeId;

    Context c;

    LinearLayout parentLayout;

    public GetProductsData(String houseWifeId , Context context, LinearLayout parentLayout) {

        this.houseWifeId = houseWifeId;
        this.c = context;
        this.parentLayout = parentLayout;
    }

    public boolean status = false;

    private ArrayList<JSONObject> productsData = new ArrayList<>();

    public ArrayList<JSONObject> getProductsData() {
        return productsData;
    }


    protected void loadData(){

        try
        {
            status = false;

            String resp = Server.s.get(APIs.prod_approval_prod_approval3 + this.houseWifeId);

            System.out.println("Response");
            //System.out.println(resp);
            JSONArray jarr = new JSONArray(resp);

            for (int i = 0; i < jarr.length(); i++) {
                JSONObject jObj = jarr.getJSONObject(i);

                productsData.add(jObj);
            }

            int count = 0;
            for( JSONObject j : productsData )
            {
                System.out.println("Product : " +  j );

                //JSON into java Object
                ProductDetails productDetails = new ProductDetails(this.c);
                try
                {
                    productDetails.set_id(j.getString("_id"));
                    productDetails.setProd_name(j.getString("Prod_name"));
                    productDetails.setProd_category(j.getString("Prod_category"));
                    productDetails.setDescription(j.getString("Description"));
                    productDetails.setPrice(j.getInt("Price"));
                    productDetails.setProd_id(j.getString("Prod_id"));
                    productDetails.setFeedback(j.getString("feedback"));
                    productDetails.setIsApproved(j.getString("isApproved"));
                    productDetails.setIsRejected(j.getString("isRejected"));
                    productDetails.setImage(j.getString("Image"));
                    productDetails.setStarsize(j.getString("starsize"));

                    JSONObject jsonObject = j.getJSONObject("HWFEmail_id");
                    productDetails.setHWFEmail_id(jsonObject.getString("_id"));
                    productDetails.setHWF_Name(jsonObject.getString("HWF_Name"));
                    productDetails.setPhone_no(jsonObject.getString("Phone_no"));

                    productDetails.set__v(j.getString("__v"));
                    productDetails.setCountrate(j.getString("countrate"));

                    ProductsOfHousewife.productsOfHousewife.put( productDetails.getProd_id() , productDetails);

                  new  AddUI(parentLayout,productDetails.draw());
                }
                catch( Exception e )
                {
                    e.printStackTrace();
                }

            }

            status = true;
        }
        catch( Exception e )
        {
            e.printStackTrace();

        }
    }



}
