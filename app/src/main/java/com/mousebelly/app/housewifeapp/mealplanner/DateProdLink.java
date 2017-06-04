package com.mousebelly.app.housewifeapp.mealplanner;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.mousebelly.app.housewifeapp.APIs;
import com.mousebelly.app.housewifeapp.Login.LoginActivity;
import com.mousebelly.app.housewifeapp.Server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Kamal Kant on 22-03-2017.
 */

public class DateProdLink {

    public static HashMap<String , ArrayList> dataProd = new HashMap<>();

    static Context c;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public DateProdLink(ArrayList<JSONObject> mealData, Context c) {
        String pDate;
        this.c = c;

        System.out.println("Meal Data:");
        System.out.println(mealData);

        for( JSONObject j : mealData ) {
            //System.out.println( j );

            try {
                pDate = j.getString("Date");
                //System.out.println("Date1 : "+pDate);
                ArrayList productArray = new ArrayList();
                for( JSONObject i : mealData ){
                   // System.out.println("Date2 : "+ i.getString("Date"));
                    if(pDate.equals(i.getString("Date"))){

                        MealPlanUnit mpu = new MealPlanUnit(this.c);
                        mpu.setProdId(i.getString("Prod_id"));
                        mpu.setStartEndTime(j.getString("Start_Time"),j.getString("End_Time"));

                        //if( p != null )
                        //mpu.setImage(p.getImage());

                        //mpu.setImage();

                        //gson.fromJson(jsonObject.toString(), DateProdArrayObj.class);

                        productArray.add(mpu);
                        //System.out.println(productIdArray);
                    }
                }
                dataProd.put(pDate,productArray);
            }catch (Exception e){
                e.printStackTrace();
        }
        }

        System.out.println("Data Product Array : "+dataProd);

    }

    public static void save()
    {
        System.out.println(dataProd);

        HashMap hm = new HashMap();

        Set keys = dataProd.keySet();

        for( Object key : keys )
        {
            if( key!=null && !key.toString().equals("") )
            {
                key = key.toString();

                ArrayList al = dataProd.get(key);

                for( Object m : al )
                {
                    MealPlanUnit mpu = (MealPlanUnit)m;

                    if( mpu.getDate() != null )
                    if(hm.get(mpu.getProdId()) == null)
                    {
                        ArrayList list = new ArrayList();

                        list.add(mpu);

                        hm.put(mpu.getProdId(),list);
                    }
                    else
                    {
                        ArrayList list = (ArrayList)hm.get(mpu.getProdId());

                        list.add(mpu);

                        hm.put(mpu.getProdId(),list);
                    }
                }
            }

        }

        System.out.println(hm);

        JSONObject jsonObject = new JSONObject();

        keys = hm.keySet();

        for( Object key : keys )
        {
            ArrayList al = (ArrayList)hm.get(key);

            JSONArray jarr = new JSONArray();

            for(Object o:al)
            {
                MealPlanUnit mpu = (MealPlanUnit) o;

                try
                {
                    JSONObject jobj = new JSONObject();

                    jobj.put("Prod_id",mpu.getProdId());
                    jobj.put("Date",mpu.getDate());
                    jobj.put("Start_Time",mpu.getStartTime());
                    jobj.put("End_Time",mpu.getEndTime());

                    ProductDetails p = (ProductDetails)ProductsOfHousewife.productsOfHousewife.get(mpu.getProdId());

                    jobj.put("Food_Object", p.toJSON().get("Food_Object") );

                    jarr.put(jobj);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }

            try
            {
                jsonObject.put(key.toString(),jarr);
            }
            catch( Exception e )
            {
                e.printStackTrace();
            }

        }

        System.out.println("JSON Data:");
        System.out.println(jsonObject);

        int resp = Server.s.putwithdata(APIs.mealplan_mealplan + LoginActivity.USERID,jsonObject);

        if( resp == 200 )
        {
            Toast.makeText(c, "Saved.", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(c, "Unable to Save. Please try again.", Toast.LENGTH_LONG).show();
        }
    }

}
