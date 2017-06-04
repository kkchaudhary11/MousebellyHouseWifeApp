package com.mousebelly.app.housewifeapp.myfood;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mousebelly.app.housewifeapp.IdManager;
import com.mousebelly.app.housewifeapp.MainActivity;
import com.mousebelly.app.housewifeapp.R;

import java.net.URL;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by Kamal Kant on 03-06-2017.
 */

public class MyFoodUnit {

    private String _id;
    private String Prod_name;
    private String Prod_category;
    private String Description;
    private String Price;
    private String Prod_id;
    private String feedback;
    private String isApproved;
    private String isRejected;
    private String Image;
    private String starsize;
    private String salePrice;
    private Bitmap bmpImage;


    //getters and setters

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getProd_name() {
        return Prod_name;
    }

    public void setProd_name(String prod_name) {
        Prod_name = prod_name;
    }

    public String getProd_category() {
        return Prod_category;
    }

    public void setProd_category(String prod_category) {
        Prod_category = prod_category;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getProd_id() {
        return Prod_id;
    }

    public void setProd_id(String prod_id) {
        Prod_id = prod_id;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
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

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;

        try {
            URL url = new URL(image);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            this.bmpImage = bmp;

            System.out.println("Bitmap Loaded");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getStarsize() {
        return starsize;
    }

    public void setStarsize(String starsize) {
        this.starsize = starsize;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    static int Id = 0;
    int myId;

    RelativeLayout rl;
    CardView cards;
    RelativeLayout cardrl;
    ImageView productImage;
    TextView productNameTextView;
    TextView productCategoryView;
    TextView priceTextView;
    RatingBar ratingBar;

    public MyFoodUnit(){
        Id++;
        this.myId = Id;

        this.rl = new RelativeLayout(MainActivity.context);
        IdManager.addId("relativeLayout" + this.myId);
        this.rl.setId((int) IdManager.stringToIdMap.get("relativeLayout" + this.myId));

        this.cards = new CardView(MainActivity.context);
        IdManager.addId("Productlistcards" + this.Id);
        this.cards.setId((Integer) IdManager.stringToIdMap.get("Productlistcards" + this.Id));

        this.productImage = new ImageView(MainActivity.context);
        IdManager.addId("productImage" + this.Id);
        this.productImage.setId((Integer) IdManager.stringToIdMap.get("productImage" + this.Id));

        this.productNameTextView = new TextView(MainActivity.context);
        IdManager.addId("productNameTextView" + this.myId);
        this.productNameTextView.setId((int) IdManager.stringToIdMap.get("productNameTextView" + this.myId));

        this.productCategoryView = new TextView(MainActivity.context);
        IdManager.addId("productCategoryView" + this.myId);
        this.productCategoryView.setId((int) IdManager.stringToIdMap.get("productCategoryView" + this.myId));

        this.cardrl = new RelativeLayout(MainActivity.context);
        IdManager.addId("cardrl" + this.myId);
        this.cardrl.setId((int) IdManager.stringToIdMap.get("cardrl" + this.myId));

        this.priceTextView = new TextView(MainActivity.context);
        IdManager.addId("priceTextView" + this.myId);
        this.priceTextView.setId((int) IdManager.stringToIdMap.get("priceTextView" + this.myId));

        this.ratingBar = new RatingBar(MainActivity.context);
        IdManager.addId("ratingBar" + this.myId);
        this.ratingBar.setId((int) IdManager.stringToIdMap.get("ratingBar" + this.myId));

    }

    public RelativeLayout draw(final MyFoodUnit f){

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT); ;

        //cart view

        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.cards.setLayoutParams(params);
        this.cards.setContentPadding(0, 0, 0, 0);
        this.cards.setCardElevation(15);
        this.cards.setMaxCardElevation(20);
        this.cards.setUseCompatPadding(true);
        this.cards.setCardBackgroundColor(Color.parseColor("#EEEEEE"));

        //image view
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 500);
        this.productImage.setLayoutParams(params);
        this.productImage.setAdjustViewBounds(true);
        this.productImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        this.productImage.setPadding(10, 10, 10, 10);
        this.productImage.setImageBitmap(f.bmpImage);
        this.cardrl.addView(productImage);

        //product name
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW,productImage.getId());
        this.productNameTextView.setLayoutParams(params);
        this.productNameTextView.setPadding(20, 10, 10, 0);
        this.productNameTextView.setTextSize(21);
        this.productNameTextView.setText(f.getProd_name());
        this.cardrl.addView(productNameTextView);

        //product name
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW,productNameTextView.getId());
        this.productCategoryView.setLayoutParams(params);
        this.productCategoryView.setPadding(20, 5, 10, 10);
        this.productCategoryView.setTextSize(18);
        this.productCategoryView.setTypeface(null, Typeface.BOLD);
        this.productCategoryView.setTextColor(ContextCompat.getColor(MainActivity.context, R.color.Affair));
        this.productCategoryView.setText(f.getProd_category());
        this.cardrl.addView(productCategoryView);

        //price view
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW,productCategoryView.getId());
        this.priceTextView.setLayoutParams(params);
        this.priceTextView.setPadding(20, 10, 10, 10);
        this.priceTextView.setTextSize(18);
        this.priceTextView.setTypeface(null, Typeface.BOLD);
        this.priceTextView.setTextColor(ContextCompat.getColor(MainActivity.context, R.color.Roman));
        this.priceTextView.setText("₹"+f.getPrice());
        this.cardrl.addView(priceTextView);

        //rating
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW,productNameTextView.getId());
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        this.ratingBar.setLayoutParams(params);
        this.ratingBar.setStepSize((float) 0.1);
        this.ratingBar.setNumStars(5);
        this.ratingBar.setScaleX(0.5f);
        this.ratingBar.setScaleY(0.5f);
        this.ratingBar.setIsIndicator(true);
        this.ratingBar.setRating(Float.parseFloat(f.starsize));
        this.cardrl.addView(ratingBar);
        //this.prodRatingbar.setBackgroundColor(Color.parseColor("#FFFF00"));


        this.cards.addView(this.cardrl);

        rl.addView(this.cards);

        return rl;
    }


}
