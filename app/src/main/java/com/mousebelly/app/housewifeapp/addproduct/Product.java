package com.mousebelly.app.housewifeapp.addproduct;

import com.mousebelly.app.housewifeapp.Login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by adity on 20-Feb-17.
 */

public class Product {

    private String Prod_name;
    private String Prod_cat;
    private String price;
    private String Description;
    private String Image;
    private String Type;

    public String getProd_name() {
        return Prod_name;
    }

    public void setProd_name(String prod_name) {
        Prod_name = prod_name;
    }

    public String getProd_cat() {
        return Prod_cat;
    }

    public void setProd_cat(String prod_cat) {
        Prod_cat = prod_cat;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }


    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    JSONObject tojson() throws JSONException {
        JSONObject jobj = new JSONObject();
        jobj.put("emailnaam2", LoginActivity.USERID);
        jobj.put("Prod_name", this.getProd_name() );
        jobj.put("Prod_category", this.getProd_cat());
        jobj.put("Description", this.getDescription());
        jobj.put("Price", this.getPrice());
        jobj.put("Type",this.getType()); //VEG/NONVEG

        jobj.put("prodimage_data_base64", this.getImage().replaceAll("\\\\","")  );

        return jobj;
    }

}
