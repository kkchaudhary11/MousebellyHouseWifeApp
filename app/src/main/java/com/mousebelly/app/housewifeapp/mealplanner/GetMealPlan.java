package com.mousebelly.app.housewifeapp.mealplanner;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.support.v7.app.AlertDialog;
import android.view.DragEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mousebelly.app.housewifeapp.APIs;
import com.mousebelly.app.housewifeapp.IdManager;
import com.mousebelly.app.housewifeapp.Server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Vasudev on 21/03/2017.
 */

public class GetMealPlan{

    private String houseWifeId;

    private ArrayList<JSONObject> mealPlan = new ArrayList<>();

    public ArrayList<JSONObject> getMealPlan() {
        return mealPlan;
    }

    public boolean status = false;

    private static String newItemStartTime = "1000";
    private static String newItemEndTime = "1000";

    static int textViewId = -1;

    Context c;

    private LinearLayout mealPlanLayout;

    static public LinearLayout mealPlanLayoutAdd;

    static MealPlanUnit mpuAdd;
    static RelativeLayout xAdd;

    private void removeViewFromLayout( RelativeLayout x , MealPlanUnit mpu)
    {
        boolean flag = false;

        l: for(int i = 0; i < this.mealPlanLayout.getChildCount() ; i++)
        {
            RelativeLayout rl = (RelativeLayout)this.mealPlanLayout.getChildAt(i);

            for(int j = 0; j < rl.getChildCount() ; j++)
            {
                View v = (View)rl.getChildAt(j);

                if( v.getId() == x.getId() )
                {
                    rl.removeView(v);
                    flag = true;
                    break l;
                }
            }
        }

        System.out.println("Element Found");

        int index = 0;

        if( flag )
        {
            Toast.makeText(c,"Saving...",Toast.LENGTH_SHORT);

            ArrayList al = DateProdLink.dataProd.get(mpu.getDate());

            for( Object o : al )
            {
                MealPlanUnit m = (MealPlanUnit) o;

                if( m.getProdId() == mpu.getProdId() && m.getDate().equals(mpu.getDate()) )
                    break;

                index++;
            }

            if( index < al.size() )
            {
                al.remove(index);
            }

            DateProdLink.save();
        }
    }

    private void addViewToLayout( RelativeLayout x , RelativeLayout y , boolean alreadyThere)
    {
        xAdd = x;

        if(x.getChildAt(x.getChildCount()-1) instanceof TextView)
        {

            if(alreadyThere)
            {
                TextView t = (TextView) x.getChildAt(x.getChildCount()-1);
                System.out.println(t.getText());

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                //params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.addRule(RelativeLayout.RIGHT_OF , t.getId() );
                params.setMargins(10,10,10,10);
                y.setPadding(10,10,10,10);
                y.setLayoutParams(params);

                x.addView(y);
            }
            else
            {
                ///////////////////////////////////////////////////////
                boolean flag = true;

                String sourceProdId = y.getTag().toString().split(";;")[0];

                ProductDetails p = (ProductDetails) ProductsOfHousewife.productsOfHousewife.get(sourceProdId);

                Bitmap sourceImage = p.getBmpImage();

                for( int i = 0 ; i < x.getChildCount() ; i++ )
                {
                    if( x.getChildAt(i) instanceof TextView )
                        textViewId = x.getChildAt(i).getId();

                    if( x.getChildAt(i) instanceof RelativeLayout )
                    {
                        String targetProdId = x.getChildAt(i).getTag().toString().split(";;")[0];

                        if( sourceProdId.equals(targetProdId) )
                        {
                            flag = false;
                            break;
                        }
                    }
                }

                if (flag)
                {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);

                    // set title
                    alertDialogBuilder.setTitle("Choose Start and End Time");

                    newItemStartTime = "1000";
                    newItemEndTime = "1000";

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Click Ok when done.")
                            .setCancelable(false)
                            .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, close
                                    // current activity

                                }
                            })
                            .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            });

                    /////////////////////////////////
                    RelativeLayout rl = new RelativeLayout(c);

                    ImageView iv = new ImageView(c);

                    iv.setImageBitmap(sourceImage);

                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    params.setMargins(10,10,10,10);
                    iv.setLayoutParams(params);

                    IdManager.addId("NewItemImageAlert");
                    iv.setId( (int)IdManager.stringToIdMap.get("NewItemImageAlert") );

                    rl.addView(iv);

                    final String[] tl1 = {"1000", "1130", "1200", "1330", "1400", "1530", "1600", "1730", "1800", "1930", "2000"};
                    ArrayList<String> timeList1 = new ArrayList<String>(Arrays.asList(tl1));

                    Spinner spinTime1 = new Spinner(c);
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, timeList1);
                    spinTime1.setAdapter(dataAdapter);

                    params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    //params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    params.addRule(RelativeLayout.RIGHT_OF , iv.getId() );
                    params.addRule(RelativeLayout.CENTER_VERTICAL);
                    params.setMargins(10,10,10,10);
                    spinTime1.setPadding(10,10,10,10);
                    spinTime1.setLayoutParams(params);


                    spinTime1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        boolean flag = false;
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if( flag )
                            {
                                newItemStartTime = (tl1[i]);
                            }
                            flag = true;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    IdManager.addId("NewItemStartTimeSpinner");
                    spinTime1.setId( (int)IdManager.stringToIdMap.get("NewItemStartTimeSpinner") );

                    rl.addView(spinTime1);

                    final String[] tl2 = {"1000", "1130", "1200", "1330", "1400", "1530", "1600", "1730", "1800", "1930", "2000"};
                    ArrayList<String> timeList2 = new ArrayList<String>(Arrays.asList(tl2));

                    Spinner spinTime2 = new Spinner(c);
                    dataAdapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, timeList2);
                    spinTime2.setAdapter(dataAdapter);

                    params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    //params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    params.addRule(RelativeLayout.RIGHT_OF , spinTime1.getId() );
                    params.addRule(RelativeLayout.CENTER_VERTICAL);
                    params.setMargins(10,10,10,10);
                    spinTime2.setPadding(10,10,10,10);
                    spinTime2.setLayoutParams(params);

                    spinTime2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        boolean flag = false;

                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if( flag )
                            {
                                newItemEndTime = (tl2[i]);
                            }
                            flag = true;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    rl.addView(spinTime2);

                    alertDialogBuilder.setView(rl);
                    /////////////////////////////////

                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Boolean wantToCloseDialog = false;
                            //Do stuff, possibly set wantToCloseDialog to true then...

                            if( Integer.parseInt(newItemEndTime) - Integer.parseInt(newItemStartTime) >= 30 )
                            {
                                wantToCloseDialog = true;

                                mpuAdd.setStartEndTime(newItemStartTime,newItemEndTime);

                                RelativeLayout rl = mpuAdd.mealPlanUnitLayout;

                                TextView t = (TextView) xAdd.getChildAt(xAdd.getChildCount()-1);

                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                //params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                params.addRule(RelativeLayout.RIGHT_OF , t.getId() );
                                params.setMargins(10,10,10,10);
                                rl.setPadding(10,10,10,10);
                                rl.setLayoutParams(params);

                                xAdd.addView(rl);

                                Toast.makeText(c, "Saving...", Toast.LENGTH_SHORT).show();

                                System.out.println("Date Tag: " + xAdd.getTag());

                                mpuAdd.setDate(xAdd.getTag().toString());

                                ArrayList arr = DateProdLink.dataProd.get(xAdd.getTag().toString());

                                if( arr == null )
                                    arr = new ArrayList();

                                arr.add(mpuAdd);

                                DateProdLink.dataProd.put(xAdd.getTag().toString(), arr);

                                DateProdLink.save();
                            }
                            else
                            {
                                Toast.makeText(c, "Invalid. Start Time and End Time must have a minimum gap of Half an Hour. This entry can't be saved till valid.", Toast.LENGTH_LONG).show();
                            }

                            if(wantToCloseDialog)
                                alertDialog.dismiss();
                            //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                        }
                    });
                }
                else
                {
                    Toast.makeText(this.c, "Item already Exists.", Toast.LENGTH_LONG).show();
                }
                ///////////////////////////////////////////////////////
            }
        }
        else
        {
            if(alreadyThere)
            {
                TextView textView = (TextView) x.getChildAt(0);

                RelativeLayout t = (RelativeLayout) x.getChildAt(x.getChildCount()-1);

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                //params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.addRule(RelativeLayout.ALIGN_LEFT , t.getId() );
                params.addRule(RelativeLayout.RIGHT_OF , textView.getId() );
                params.addRule(RelativeLayout.BELOW , t.getId() );
                params.setMargins(10,10,10,10);
                y.setPadding(10,10,10,10);
                y.setLayoutParams(params);

                x.addView(y);
            }
            else
            {
                ///////////////////////////////////////////////////////
                boolean flag = true;

                String sourceProdId = y.getTag().toString().split(";;")[0];

                ProductDetails p = (ProductDetails) ProductsOfHousewife.productsOfHousewife.get(sourceProdId);

                Bitmap sourceImage = p.getBmpImage();

                for( int i = 0 ; i < x.getChildCount() ; i++ )
                {
                    if( x.getChildAt(i) instanceof TextView )
                        textViewId = x.getChildAt(i).getId();

                    if( x.getChildAt(i) instanceof RelativeLayout )
                    {
                        String targetProdId = x.getChildAt(i).getTag().toString().split(";;")[0];

                        if( sourceProdId.equals(targetProdId) )
                        {
                            flag = false;
                            break;
                        }
                    }
                }

                if (flag)
                {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);

                    // set title
                    alertDialogBuilder.setTitle("Choose Start and End Time");

                    newItemStartTime = "1000";
                    newItemEndTime = "1000";

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Click Ok when done.")
                            .setCancelable(false)
                            .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, close
                                    // current activity

                                }
                            })
                            .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            });

                    /////////////////////////////////
                    RelativeLayout rl = new RelativeLayout(c);

                    ImageView iv = new ImageView(c);

                    iv.setImageBitmap(sourceImage);

                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    params.setMargins(10,10,10,10);
                    iv.setLayoutParams(params);

                    IdManager.addId("NewItemImageAlert");
                    iv.setId( (int)IdManager.stringToIdMap.get("NewItemImageAlert") );

                    rl.addView(iv);

                    final String[] tl1 = {"1000", "1130", "1200", "1330", "1400", "1530", "1600", "1730", "1800", "1930", "2000"};
                    ArrayList<String> timeList1 = new ArrayList<String>(Arrays.asList(tl1));

                    Spinner spinTime1 = new Spinner(c);
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, timeList1);
                    spinTime1.setAdapter(dataAdapter);

                    params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    //params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    params.addRule(RelativeLayout.RIGHT_OF , iv.getId() );
                    params.addRule(RelativeLayout.CENTER_VERTICAL);
                    params.setMargins(10,10,10,10);
                    spinTime1.setPadding(10,10,10,10);
                    spinTime1.setLayoutParams(params);


                    spinTime1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        boolean flag = false;
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if( flag )
                            {
                                newItemStartTime = (tl1[i]);
                            }
                            flag = true;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    IdManager.addId("NewItemStartTimeSpinner");
                    spinTime1.setId( (int)IdManager.stringToIdMap.get("NewItemStartTimeSpinner") );

                    rl.addView(spinTime1);

                    final String[] tl2 = {"1000", "1130", "1200", "1330", "1400", "1530", "1600", "1730", "1800", "1930", "2000"};
                    ArrayList<String> timeList2 = new ArrayList<String>(Arrays.asList(tl2));

                    Spinner spinTime2 = new Spinner(c);
                    dataAdapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, timeList2);
                    spinTime2.setAdapter(dataAdapter);

                    params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    //params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    params.addRule(RelativeLayout.RIGHT_OF , spinTime1.getId() );
                    params.addRule(RelativeLayout.CENTER_VERTICAL);
                    params.setMargins(10,10,10,10);
                    spinTime2.setPadding(10,10,10,10);
                    spinTime2.setLayoutParams(params);

                    spinTime2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        boolean flag = false;

                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if( flag )
                            {
                                newItemEndTime = (tl2[i]);
                            }
                            flag = true;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    rl.addView(spinTime2);

                    alertDialogBuilder.setView(rl);
                    /////////////////////////////////

                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Boolean wantToCloseDialog = false;
                            //Do stuff, possibly set wantToCloseDialog to true then...

                            if( Integer.parseInt(newItemEndTime) - Integer.parseInt(newItemStartTime) >= 30 )
                            {
                                wantToCloseDialog = true;

                                mpuAdd.setStartEndTime(newItemStartTime,newItemEndTime);

                                RelativeLayout rl = mpuAdd.mealPlanUnitLayout;

                                RelativeLayout t = (RelativeLayout) xAdd.getChildAt(xAdd.getChildCount()-1);

                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                //params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                params.addRule(RelativeLayout.ALIGN_LEFT , t.getId() );
                                params.addRule(RelativeLayout.RIGHT_OF , textViewId );
                                params.addRule(RelativeLayout.BELOW , t.getId() );
                                params.setMargins(10,10,10,10);
                                rl.setPadding(10,10,10,10);
                                rl.setLayoutParams(params);

                                xAdd.addView(rl);

                                Toast.makeText(c, "Saving...", Toast.LENGTH_SHORT).show();

                                System.out.println("Date Tag: " + xAdd.getTag());

                                mpuAdd.setDate(xAdd.getTag().toString());

                                ArrayList arr = DateProdLink.dataProd.get(xAdd.getTag().toString());

                                if( arr == null )
                                    arr = new ArrayList();

                                arr.add(mpuAdd);

                                DateProdLink.dataProd.put(xAdd.getTag().toString(), arr);

                                DateProdLink.save();
                            }
                            else
                            {
                                Toast.makeText(c, "Invalid. Start Time and End Time must have a minimum gap of Half an Hour. This entry can't be saved till valid.", Toast.LENGTH_LONG).show();
                            }

                            if(wantToCloseDialog)
                                alertDialog.dismiss();
                            //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                        }
                    });
                }
                else
                {
                    Toast.makeText(this.c, "Item already Exists.", Toast.LENGTH_LONG).show();
                }
                ///////////////////////////////////////////////////////
            }
        }
    }

    public GetMealPlan(String houseWifeId, Context context, LinearLayout linearLayout) {
        this.houseWifeId = houseWifeId;
        this.c = context;
        this.mealPlanLayout = linearLayout;

        GetMealPlan.mealPlanLayoutAdd = this.mealPlanLayout;

        Date dt = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMMM yyyy");

        for( int k = 0 ; k < 7 ; k++ ) {
            RelativeLayout dateRelativeLayout = new RelativeLayout(this.c);


            ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class

            // get paint
            Paint paint = rectShapeDrawable.getPaint();

            // set border color, stroke and stroke width
            paint.setColor(Color.GRAY);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5); // you can change the value of 5
            paint.setShadowLayer(10, 10, 10, Color.BLACK);

            dateRelativeLayout.setBackgroundDrawable(rectShapeDrawable);

            TextView dateValue = new TextView(this.c);

            IdManager.addId("DateTextView" + k);
            dateValue.setId( (int)IdManager.stringToIdMap.get("DateTextView" + k) );
            //cal.add(Calendar.DATE,k);

            if (k == 0)
                cal.add(Calendar.DATE, 0);
            else
                cal.add(Calendar.DATE, 1);

            dt = cal.getTime();

            String dateVal = formatter.format(dt).toString();

            for( int i = 1 ; i<=9 ; i++ )
            {
                dateVal = dateVal.replaceAll(" 0"+i+" "," "+i+" ");
            }

            dateRelativeLayout.setTag( dateVal );

            dateValue.setText(formatter.format(dt).toString());
            dateValue.setPadding(10, 10, 10, 10);
            dateValue.setWidth(100);
            dateValue.setTextColor(Color.BLACK);
            dateValue.setBackgroundColor(Color.WHITE);

            dateRelativeLayout.addView(dateValue);

//            MealPlanUnit mpu = new MealPlanUnit(this.c);
//            mpu.setProdId("product21401");
//            mpu.setStartEndTime("1000","1100");
//            mpu.setImage( "http://res.cloudinary.com/mousebelly/image/upload/v1489592220/ztvhakzotfdlujkq4inx.png" );
//            RelativeLayout rl = mpu.draw();
//            addViewToLayout(dateRelativeLayout,rl,true);
//
//            mpu = new MealPlanUnit(this.c);
//            mpu.setProdId("product91941");
//            mpu.setStartEndTime("1200","1300");
//            mpu.setImage( "http://res.cloudinary.com/mousebelly/image/upload/v1489240594/xmi7hs7g6bugk4arawzc.png" );
//            rl = mpu.draw();
//            addViewToLayout(dateRelativeLayout,rl,true);
//
//            mpu = new MealPlanUnit(this.c);
//            mpu.setProdId("product83032");
//            mpu.setStartEndTime("1400","1600");
//            mpu.setImage( "http://res.cloudinary.com/mousebelly/image/upload/v1489592368/xduzggqnhovrj4bogdwl.png" );
//            rl = mpu.draw();
//            addViewToLayout(dateRelativeLayout,rl,true);

            dateRelativeLayout.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View view, DragEvent dragEvent) {

                    View v = (View)dragEvent.getLocalState();

                    switch (dragEvent.getAction()) {
                        case DragEvent.ACTION_DROP:
                            System.out.println("Dropped");
                            System.out.println("Tag : " + v.getTag());

                            String[] data = v.getTag().toString().split(";;");

                            if( data.length == 4 )
                            {
                                final MealPlanUnit mpu = new MealPlanUnit(c);

                                mpuAdd = mpu;

                                mpu.setProdId(data[0]);

                                if( data[1].equals("-") || data[2].equals("-") )
                                    mpu.setStartEndTime("1000","1000");
                                else
                                    mpu.setStartEndTime( data[1] ,data[2]);

                                mpu.setImage( data[3] );

                                RelativeLayout rl = mpu.draw();

                                mpu.itemDelete.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        removeViewFromLayout(mpu.mealPlanUnitLayout,mpu);
                                    }
                                });
                                addViewToLayout((RelativeLayout) view,rl,false);
                            }


                        default:
                            break;
                    }
                    return true;
                }
            });

            this.mealPlanLayout.addView(dateRelativeLayout);
        }
    }


    protected void getData() {

        try
        {
            status = false;

            String resp = Server.s.get(APIs.mealplan_mealplan + this.houseWifeId);

            System.out.println("Response");
            //System.out.println(resp);
            JSONArray jarr = new JSONArray(resp);

            JSONObject mealPlan = jarr.getJSONObject(0);

            if( mealPlan != null )
            {
                JSONObject productPlanMacro = (JSONObject)mealPlan.get("product_plan");
                JSONObject productPlanMicro = (JSONObject)productPlanMacro.get("f");

                //System.out.println(productPlanMicro);

                Iterator i = productPlanMicro.keys();

                while( i.hasNext() )
                {
                    String key = i.next().toString();
                    //System.out.println( key + " :" );

                    JSONArray itemSchedule = new JSONArray(productPlanMicro.get(key).toString());

                    for( int k = 0 ; k < itemSchedule.length() ; k++ )
                    {
                        //System.out.println( itemSchedule.get(k) );

                        this.mealPlan.add( (JSONObject) itemSchedule.get(k));
                    }
                }
            }

            status = true;

            new DateProdLink(this.mealPlan,this.c);

        }
        catch( Exception e )
        {
            e.printStackTrace();

        }

    }

    public void loadData()
    {
        System.out.println("Load Data:");
        System.out.println(DateProdLink.dataProd);

        for( int i = 0 ; i < GetMealPlan.mealPlanLayoutAdd.getChildCount() ; i++ )
        {
            System.out.println( GetMealPlan.mealPlanLayoutAdd.getChildAt(i).getTag() );
            System.out.println( DateProdLink.dataProd.get( GetMealPlan.mealPlanLayoutAdd.getChildAt(i).getTag() ) );

            if( DateProdLink.dataProd.get( GetMealPlan.mealPlanLayoutAdd.getChildAt(i).getTag() ) != null )
            for( Object o : DateProdLink.dataProd.get( GetMealPlan.mealPlanLayoutAdd.getChildAt(i).getTag() ) )
            {
                final MealPlanUnit mpu = (MealPlanUnit)o;

                RelativeLayout rl = (RelativeLayout)GetMealPlan.mealPlanLayoutAdd.getChildAt(i);

                ProductDetails p = (ProductDetails)ProductsOfHousewife.productsOfHousewife.get(mpu.getProdId());

                mpu.setDate( GetMealPlan.mealPlanLayoutAdd.getChildAt(i).getTag().toString() );

                mpu.itemDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        System.out.println("Prod_Id : " + mpu.getProdId());
//                        System.out.println("Date : " + mpu.getDate());

                        removeViewFromLayout(mpu.mealPlanUnitLayout,mpu);
                    }
                });

                mpu.setImage(p.getImage());

                this.addViewToLayout(rl,mpu.draw(),true);
            }
        }
    }


}
