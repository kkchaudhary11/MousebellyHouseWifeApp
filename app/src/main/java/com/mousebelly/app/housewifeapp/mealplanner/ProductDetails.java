package com.mousebelly.app.housewifeapp.mealplanner;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mousebelly.app.housewifeapp.IdManager;
import com.mousebelly.app.housewifeapp.MainActivity;
import com.mousebelly.app.housewifeapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by Kamal Kant on 23-03-2017.
 */

public class ProductDetails {

    private String Prod_name;
    private String Prod_category;
    private String Description;
    private int Price;
    private String Prod_id;
    private String feedback;
    private String isApproved;
    private String isRejected;
    private String Image;
    private String starsize;

    private String _id;
    private String HWFEmail_id;
    private String HWF_Name;
    private String Phone_no;

    private String __v;
    private String countrate;

    static public int productDetailsCount = 0;

    private int Id;

    //getters and setters

    public String getProd_name() {
        return Prod_name;
    }

    public void setProd_name(String prod_name) {
        Prod_name = prod_name;
        this.productNameTextView.setText(prod_name);
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

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
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
        this.setBmpImage(image);
    }

    public String getStarsize() {
        return starsize;
    }

    public void setStarsize(String starsize) {
        this.starsize = starsize;
    }

    ////////////////////////////////////////

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getHWFEmail_id() {
        return HWFEmail_id;
    }

    public void setHWFEmail_id(String HWFEmail_id) {
        this.HWFEmail_id = HWFEmail_id;
    }

    public String get__v() {
        return __v;
    }

    public void set__v(String __v) {
        this.__v = __v;
    }

    public String getCountrate() {
        return countrate;
    }

    public void setCountrate(String countrate) {
        this.countrate = countrate;
    }

    public String getHWF_Name() {
        return HWF_Name;
    }

    public void setHWF_Name(String HWF_Name) {
        this.HWF_Name = HWF_Name;
    }

    public String getPhone_no() {
        return Phone_no;
    }

    public void setPhone_no(String phone_no) {
        Phone_no = phone_no;
    }

    private Bitmap bmpImage;

    public Bitmap getBmpImage() {
        return bmpImage;
    }

    Context c;
    ImageView itemImage;
    TextView productNameTextView;
    RelativeLayout productDetailsLayout;

    public void setBmpImage(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            this.bmpImage = bmp;

            System.out.println("Bitmap Loaded");

            this.itemImage.setImageBitmap(this.bmpImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public ProductDetails(Context context)
    {
        this.c = context;
        productDetailsCount++;

        this.Id = productDetailsCount;

        this.productDetailsLayout = new RelativeLayout(this.c);
        IdManager.addId("ProductDetails" + this.Id);
        this.productDetailsLayout.setId( (int)IdManager.stringToIdMap.get("ProductDetails" + this.Id) );

        GradientDrawable shape =  new GradientDrawable();
        shape.setCornerRadius( 8 );
        shape.setColor(Color.WHITE);
        shape.setStroke(1,Color.BLACK);

        this.productDetailsLayout.setMinimumHeight(40);

        this.productDetailsLayout.setBackground(shape);

        // Item Image Init
        this.itemImage = new ImageView(this.c);
        IdManager.addId("ProductDetailsImage" + this.Id);
        this.itemImage.setId( (int)IdManager.stringToIdMap.get("ProductDetailsImage" + this.Id) );
        // Item Image Init

        // Item Start Time Init
        this.productNameTextView = new TextView(this.c);
        IdManager.addId("ProductDetailsProductName" + this.Id);
        this.productNameTextView.setId( (int)IdManager.stringToIdMap.get("ProductDetailsProductName" + this.Id) );

        /*shape =  new GradientDrawable();
        shape.setCornerRadius( 8 );
        shape.setColor(Color.RED);*/

        this.productNameTextView.setPadding(0,0,0,0);
        this.productNameTextView.setTextColor(ContextCompat.getColor(MainActivity.context, R.color.Amulet));
        // Item Start Time Init
    }

    public RelativeLayout draw()
    {

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.setMargins(10,10,10,10);
        this.itemImage.setLayoutParams(params);

        //

        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.BELOW , this.itemImage.getId() );
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.setMargins(10,10,10,10);
        this.productNameTextView.setLayoutParams(params);
        this.productNameTextView.setPadding(10,10,10,10);
        //

        //Adding Elements
        this.productDetailsLayout.addView(this.itemImage);
        this.productDetailsLayout.addView(this.productNameTextView);

        this.productDetailsLayout.setTag( this.getProd_id() + ";;-;;-;;" + this.getImage()  );

        this.productDetailsLayout.setOnTouchListener(new View.OnTouchListener() {

            static final int MIN_DISTANCE_Y = 40;

            private float downY, upY;

            @Override
            public boolean onTouch(View view, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        downY = event.getY();
                        //System.out.println("ACTION DOWN");
                        return true;
                    }
                    case MotionEvent.ACTION_MOVE: {

                        //System.out.println("ACTION MOVE");

                        upY = event.getY();

                        float deltaY = downY - upY;

                        System.out.println("SOURCE ID:" + productDetailsLayout.getId());

                        // swipe vertical?
                        if (Math.abs(deltaY) > MIN_DISTANCE_Y) {
                            if (deltaY < 0) {
                                ClipData data = ClipData.newPlainText("", "");
                                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                                        view);
                                view.startDrag(data, shadowBuilder, view, 0);

                                System.out.println("Tag 1:" + view.getTag());

                                return true;
                            }
                            if (deltaY > 0) {
                                ClipData data = ClipData.newPlainText("", "");
                                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                                        view);
                                view.startDrag(data, shadowBuilder, view, 0);

                                System.out.println("Tag 2:" + view.getTag());

                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        });


        return this.productDetailsLayout;
    }

    @Override
    public String toString() {
        return "" +
                "Prod_name='" + Prod_name + '\'' +
                ", Prod_category='" + Prod_category + '\'' +
                ", Description='" + Description + '\'' +
                ", Price='" + Price + '\'' +
                ", Prod_id='" + Prod_id + '\'' +
                ", feedback='" + feedback + '\'' +
                ", isApproved='" + isApproved + '\'' +
                ", isRejected='" + isRejected + '\'' +
                ", Image='" + Image + '\'' +
                ", starsize='" + starsize + '\'' +
                "";
    }


    public JSONObject toJSON() throws JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("_id",_id);
        jsonObject.put("Prod_name",Prod_name);
        jsonObject.put("Prod_category",Prod_category);
        jsonObject.put("Description",Description);
        jsonObject.put("Price",Price);
        jsonObject.put("Prod_id",Prod_id);
        jsonObject.put("Prod_name",Prod_name);
        jsonObject.put("feedback",feedback);
        jsonObject.put("isApproved",isApproved);
        jsonObject.put("isRejected",isRejected);
        jsonObject.put("Image",Image);
        jsonObject.put("starsize",starsize);

        JSONObject hwfjson = new JSONObject();
        hwfjson.put("_id",HWFEmail_id);
        hwfjson.put("HWF_Name",HWF_Name);
        hwfjson.put("Phone_no",Phone_no);

        jsonObject.put("HWFEmail_id",hwfjson);
        jsonObject.put("__v",__v);
        jsonObject.put("countrate",countrate);

        JSONObject foodObject = new JSONObject();
        foodObject.put("Food_Object",jsonObject);

       /* Gson gson = new Gson();
        String jsonString = gson.toJson(this);
        JSONObject jsonObject = new JSONObject(jsonString);*/
        return foodObject;
    }
}