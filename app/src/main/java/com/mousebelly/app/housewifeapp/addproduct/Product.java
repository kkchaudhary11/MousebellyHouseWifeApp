package com.mousebelly.app.housewifeapp.addproduct;

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
    private String AddProductSystemID;
    private String Rating;
    private String FeedBack;
    private String isApproved;
    private String isRejected;
    private String HouseWifeEmail;
    private String HouseWifeName;
    private String HouseWifePhone;
    private String Star;
    private String SystemID;
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

    public String getAddProductSystemID() {
        return AddProductSystemID;
    }

    public void setAddProductSystemID(String addProductSystemID) {
        AddProductSystemID = addProductSystemID;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getFeedBack() {
        return FeedBack;
    }

    public void setFeedBack(String feedBack) {
        FeedBack = feedBack;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public String getIsRejected() {
        return isRejected;
    }

    public void setIsRejected(String isRejected) {
        this.isRejected = isRejected;
    }

    public String getHouseWifeEmail() {
        return HouseWifeEmail;
    }

    public void setHouseWifeEmail(String houseWifeEmail) {
        HouseWifeEmail = houseWifeEmail;
    }

    public String getHouseWifeName() {
        return HouseWifeName;
    }

    public void setHouseWifeName(String houseWifeName) {
        HouseWifeName = houseWifeName;
    }

    public String getHouseWifePhone() {
        return HouseWifePhone;
    }

    public void setHouseWifePhone(String houseWifePhone) {
        HouseWifePhone = houseWifePhone;
    }

    public String getStar() {
        return Star;
    }

    public void setStar(String star) {
        Star = star;
    }

    public String getSystemID() {
        return SystemID;
    }

    public void setSystemID(String systemID) {
        SystemID = systemID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    JSONObject tojson() throws JSONException {
        JSONObject jobj = new JSONObject();
        jobj.put("emailnaam2", this.getHouseWifeEmail());
        jobj.put("Prod_name", this.getProd_name() );
        jobj.put("Prod_category", this.getProd_cat());
        jobj.put("Description", this.getDescription());
        jobj.put("Price", this.getPrice());
        jobj.put("Type",this.getType()); //VEG/NONVEG

        jobj.put("prodimage_data_base64", this.getImage().replaceAll("\\\\","")  );

        return jobj;
    }

}
