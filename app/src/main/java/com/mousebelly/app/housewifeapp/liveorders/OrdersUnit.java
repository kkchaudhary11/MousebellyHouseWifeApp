package com.mousebelly.app.housewifeapp.liveorders;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mousebelly.app.housewifeapp.APIs;
import com.mousebelly.app.housewifeapp.IdManager;
import com.mousebelly.app.housewifeapp.MainActivity;
import com.mousebelly.app.housewifeapp.R;
import com.mousebelly.app.housewifeapp.Server;

import java.net.URL;

/**
 * Created by Kamal Kant on 03-06-2017.
 */

public class OrdersUnit {

    private String _id;
    private String UserEmail_id;
    private String PID;
    private String Product_Name;
    private String status;
    private String HWID;
    private String Qty;
    private String stars;
    private String Price_item;
    private String Image;
    private String Total_bill;
    private String Payment_status;
    private String order_date;
    private String order_Id;
    private Bitmap bmpImage;


    //getters and setters


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserEmail_id() {
        return UserEmail_id;
    }

    public void setUserEmail_id(String userEmail_id) {
        UserEmail_id = userEmail_id;
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public String getProduct_Name() {
        return Product_Name;
    }

    public void setProduct_Name(String product_Name) {
        Product_Name = product_Name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHWID() {
        return HWID;
    }

    public void setHWID(String HWID) {
        this.HWID = HWID;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getPrice_item() {
        return Price_item;
    }

    public void setPrice_item(String price_item) {
        Price_item = price_item;
    }

    public String getTotal_bill() {
        return Total_bill;
    }

    public void setTotal_bill(String total_bill) {
        Total_bill = total_bill;
    }

    public String getPayment_status() {
        return Payment_status;
    }

    public void setPayment_status(String payment_status) {
        Payment_status = payment_status;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_Id() {
        return order_Id;
    }

    public void setOrder_Id(String order_Id) {
        this.order_Id = order_Id;
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

    @Override
    public String toString() {
        return "OrdersUnit{" +
                "_id='" + _id + '\'' +
                ", UserEmail_id='" + UserEmail_id + '\'' +
                ", PID='" + PID + '\'' +
                ", Product_Name='" + Product_Name + '\'' +
                ", status='" + status + '\'' +
                ", HWID='" + HWID + '\'' +
                ", Qty='" + Qty + '\'' +
                ", stars='" + stars + '\'' +
                ", Price_item='" + Price_item + '\'' +
                ", Image='" + Image + '\'' +
                ", Total_bill='" + Total_bill + '\'' +
                ", Payment_status='" + Payment_status + '\'' +
                ", order_date='" + order_date + '\'' +
                ", order_Id='" + order_Id + '\'' +
                '}';
    }

    static int Id = 0;
    int myId;

    RelativeLayout rl;
    CardView cards;
    RelativeLayout cardrl;
    ImageView productImage;
    TextView productNameTextView;
    TextView orderId;
    TextView productQuantityView;
    TextView priceTextView;
    TextView statusTextView;

    Button acceptButton, rejectButton, cookedButton;

    public OrdersUnit(){
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

        this.productQuantityView = new TextView(MainActivity.context);
        IdManager.addId("productCategoryView" + this.myId);
        this.productQuantityView.setId((int) IdManager.stringToIdMap.get("productCategoryView" + this.myId));

        this.cardrl = new RelativeLayout(MainActivity.context);
        IdManager.addId("cardrl" + this.myId);
        this.cardrl.setId((int) IdManager.stringToIdMap.get("cardrl" + this.myId));

        this.priceTextView = new TextView(MainActivity.context);
        IdManager.addId("priceTextView" + this.myId);
        this.priceTextView.setId((int) IdManager.stringToIdMap.get("priceTextView" + this.myId));

        this.acceptButton = new Button(MainActivity.context);
        IdManager.addId("acceptButton" + this.myId);
        this.acceptButton.setId((int) IdManager.stringToIdMap.get("acceptButton" + this.myId));

        this.rejectButton= new Button(MainActivity.context);
        IdManager.addId("rejectButton" + this.myId);
        this.rejectButton.setId((int) IdManager.stringToIdMap.get("rejectButton" + this.myId));

        this.statusTextView= new TextView(MainActivity.context);
        IdManager.addId("statusTextView" + this.myId);
        this.statusTextView.setId((int) IdManager.stringToIdMap.get("statusTextView" + this.myId));

        this.cookedButton = new Button(MainActivity.context);
        IdManager.addId("cookedButton" + this.myId);
        this.cookedButton.setId((int) IdManager.stringToIdMap.get("cookedButton" + this.myId));

        this.orderId = new TextView(MainActivity.context);
        IdManager.addId("orderId" + this.myId);
        this.orderId.setId((int) IdManager.stringToIdMap.get("orderId" + this.myId));

    }

    public RelativeLayout draw(final OrdersUnit f){

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT); ;

        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.cards.setLayoutParams(params);
        this.cards.setContentPadding(0, 0, 0, 0);
        this.cards.setCardElevation(15);
        this.cards.setMaxCardElevation(20);
        this.cards.setUseCompatPadding(true);
        this.cards.setCardBackgroundColor(Color.parseColor("#EEEEEE"));

        //image view
        params = new RelativeLayout.LayoutParams(350, 350);
        this.productImage.setLayoutParams(params);
        this.productImage.setAdjustViewBounds(true);
        this.productImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        this.productImage.setPadding(10, 10, 10, 10);
        this.productImage.setImageBitmap(f.bmpImage);
        this.cardrl.addView(productImage);

        //product name
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.RIGHT_OF,productImage.getId());
        this.productNameTextView.setLayoutParams(params);
        this.productNameTextView.setPadding(20, 10, 10, 0);
        this.productNameTextView.setTextSize(21);
        this.productNameTextView.setText(f.getProduct_Name());
        this.cardrl.addView(productNameTextView);


        //orderId
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.RIGHT_OF,productImage.getId());
        params.addRule(RelativeLayout.BELOW,productNameTextView.getId());
        this.orderId.setLayoutParams(params);
        this.orderId.setPadding(20, 10, 10, 0);
        this.orderId.setText(f.getOrder_Id());
        this.cardrl.addView(orderId);


        //quantity name
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.RIGHT_OF,orderId.getId());
        params.addRule(RelativeLayout.BELOW,productNameTextView.getId());
        this.productQuantityView.setLayoutParams(params);
        this.productQuantityView.setPadding(20, 5, 10, 10);
        this.productQuantityView.setTypeface(null, Typeface.BOLD);
        this.productQuantityView.setText("QTY : " + f.getQty());
        this.cardrl.addView(productQuantityView);

        //price view
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.RIGHT_OF,productQuantityView.getId());
        params.addRule(RelativeLayout.BELOW,productNameTextView.getId());
        this.priceTextView.setLayoutParams(params);
        this.priceTextView.setPadding(20, 10, 10, 10);
        this.priceTextView.setTypeface(null, Typeface.BOLD);
        this.priceTextView.setTextColor(ContextCompat.getColor(MainActivity.context, R.color.colorPrimary));
        this.priceTextView.setText("₹"+f.getPrice_item());
        this.cardrl.addView(priceTextView);

        //status view
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.RIGHT_OF,productImage.getId());
        params.addRule(RelativeLayout.BELOW,productQuantityView.getId());
        this.statusTextView.setLayoutParams(params);
        this.statusTextView.setPadding(20, 10, 10, 10);
        this.statusTextView.setText(f.getStatus());
        this.statusTextView.setTypeface(null, Typeface.BOLD);
        this.statusTextView.setTextColor(ContextCompat.getColor(MainActivity.context, R.color.Roman));
        this.cardrl.addView(statusTextView);

        if(f.getStatus().equals("ordered")) {
            //accept button
            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.RIGHT_OF, productImage.getId());
            params.addRule(RelativeLayout.BELOW, statusTextView.getId());
            this.acceptButton.setLayoutParams(params);
            this.acceptButton.setTypeface(null, Typeface.BOLD);
            this.acceptButton.setTextColor(ContextCompat.getColor(MainActivity.context, R.color.Amulet));
            this.acceptButton.setText("Accept");
            this.cardrl.addView(acceptButton);

            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Server.s.put(APIs.order_order+"/"+f.getPID()+"/"+f.getOrder_Id());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();


                    if(GetOrders.orderUnits.containsKey(f.get_id())){
                        f.setStatus("accepted");
                        GetOrders.orderUnits.put(f.get_id(),f);
                        GetOrders getOrders = new GetOrders();
                       // System.out.println("Checked Id : " + LiveOrders.OrderViewoptions.getCheckedRadioButtonId());
                        if(LiveOrders.OrderViewoptions.getCheckedRadioButtonId() == R.id.all_orders) {
                            getOrders.showOrders();
                        }else{
                            getOrders.showFilteredOrders("ordered");
                        }
                    }
                }
            });

            //reject button
            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.RIGHT_OF, acceptButton.getId());
            params.addRule(RelativeLayout.BELOW, statusTextView.getId());
            this.rejectButton.setLayoutParams(params);
            this.rejectButton.setTypeface(null, Typeface.BOLD);
            this.rejectButton.setTextColor(ContextCompat.getColor(MainActivity.context, R.color.Roman));
            this.rejectButton.setText("Reject");
            this.cardrl.addView(rejectButton);

            rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Server.s.put(APIs.order_order2+"/"+f.getPID()+"/"+f.getOrder_Id());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();


                    f.setStatus("rejected");
                    GetOrders.orderUnits.put(f.get_id(),f);
                    GetOrders getOrders = new GetOrders();
                    // System.out.println("Checked Id : " + LiveOrders.OrderViewoptions.getCheckedRadioButtonId());
                    if(LiveOrders.OrderViewoptions.getCheckedRadioButtonId() == R.id.all_orders) {
                        getOrders.showOrders();
                    }else{
                        getOrders.showFilteredOrders("ordered");
                    }
                }
            });


        }

        if(f.getStatus().equals("accepted")) {
            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.RIGHT_OF, productImage.getId());
            params.addRule(RelativeLayout.BELOW, statusTextView.getId());
            this.cookedButton.setLayoutParams(params);
            this.cookedButton.setTypeface(null, Typeface.BOLD);
            this.cookedButton.setTextColor(ContextCompat.getColor(MainActivity.context, R.color.Affair));
            this.cookedButton.setText("Cooked");
            this.cardrl.addView(cookedButton);

        }

        cookedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Server.s.put(APIs.order_order1+"/"+f.getPID()+"/"+f.getOrder_Id());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                f.setStatus("cooked");
                GetOrders.orderUnits.put(f.get_id(),f);
                GetOrders getOrders = new GetOrders();
                // System.out.println("Checked Id : " + LiveOrders.OrderViewoptions.getCheckedRadioButtonId());
                if(LiveOrders.OrderViewoptions.getCheckedRadioButtonId() == R.id.all_orders) {
                    getOrders.showOrders();
                }else{
                    getOrders.showFilteredOrders("accepted");
                }
            }
        });

        this.cards.addView(this.cardrl);

        rl.addView(this.cards);

        return rl;
    }


}
