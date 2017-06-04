package com.mousebelly.app.housewifeapp.mealplanner;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mousebelly.app.housewifeapp.IdManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Vasudev on 27/03/2017.
 */

public class MealPlanUnit {

    private String startTime;
    private String endTime;
    private Bitmap image;
    private String date;
    private int Id;
    private String prodId;

    static String startTimeSet;
    static String endTimeSet;

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    static public int MealPlanUnitCount = 0;

    public String getStartTime() {
        return startTime;
    }

    public boolean setStartTime(String startTime) {

        String temp = this.startTime;

        this.startTime = startTime;
        this.startTimeTextView.setText(this.startTime);

        if( !(ValidateTime(startTime , this.endTime)) )
        {
            Toast.makeText(this.c, "Invalid. Start Time and End Time must have a minimum gap of Half an Hour. This entry won't be saved till valid.", Toast.LENGTH_LONG).show();
            this.startTimeTextView.setText(temp);

            return false;
        }
        else
        {
            Toast.makeText(this.c, "Saving...", Toast.LENGTH_SHORT).show();

            DateProdLink.save();

            return true;
        }
    }

    public void setStartEndTime(String startTime,String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.startTimeTextView.setText(this.startTime);
        this.endTimeTextView.setText(this.endTime);
    }

    public boolean ValidateTime(String startTime,String endTime)
    {
        if( Integer.parseInt(endTime) - Integer.parseInt(startTime) >= 30 )
            return true;
        return false;
    }

    public String getEndTime() {
        return endTime;
    }

    public boolean setEndTime(String endTime) {

        String temp = this.endTime;

        this.endTime = endTime;
        this.endTimeTextView.setText(this.endTime);

        if( !(ValidateTime(this.startTime , endTime)) )
        {
            Toast.makeText(this.c, "Invalid. Start Time and End Time must have a minimum gap of Half an Hour. This entry won't be saved till valid.", Toast.LENGTH_LONG).show();
            this.endTimeTextView.setText(temp);

            return false;
        }
        else
        {
            Toast.makeText(this.c, "Saving...", Toast.LENGTH_SHORT).show();

            DateProdLink.save();

            return true;
        }

    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(String imageUrl) {

        try {
            URL url = new URL(imageUrl);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            this.image = bmp;

            System.out.println("Bitmap Loaded");

            this.itemImage.setImageBitmap(this.image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    //Context Set
    private Context c;

    RelativeLayout mealPlanUnitLayout;

    ImageView itemImage;
    TextView startTimeTextView;
    TextView endTimeTextView;

    Button itemDelete;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public MealPlanUnit(Context c) {
        this.c = c;

        MealPlanUnitCount++;

        this.Id = MealPlanUnitCount;

        this.mealPlanUnitLayout = new RelativeLayout(this.c);
        IdManager.addId("MealPlanUnit" + this.Id);
        this.mealPlanUnitLayout.setId( (int)IdManager.stringToIdMap.get("MealPlanUnit" + this.Id) );

        GradientDrawable shape =  new GradientDrawable();
        shape.setCornerRadius( 8 );
        shape.setColor(Color.WHITE);
        shape.setStroke(1,Color.BLACK);

        this.mealPlanUnitLayout.setMinimumHeight(100);

        this.mealPlanUnitLayout.setBackground(shape);

        // Item Image Init
        this.itemImage = new ImageView(this.c);
        IdManager.addId("MealPlanUnitImage" + this.Id);
        this.itemImage.setId( (int)IdManager.stringToIdMap.get("MealPlanUnitImage" + this.Id) );
        // Item Image Init

        // Item Start Time Init
        this.startTimeTextView = new TextView(this.c);
        IdManager.addId("MealPlanUnitStartTime" + this.Id);
        this.startTimeTextView.setId( (int)IdManager.stringToIdMap.get("MealPlanUnitStartTime" + this.Id) );

        shape =  new GradientDrawable();
        shape.setCornerRadius( 8 );
        shape.setColor(Color.RED);

        this.startTimeTextView.setBackground(shape);
        this.startTimeTextView.setPadding(0,0,0,0);
        this.startTimeTextView.setTextColor(Color.WHITE);
        // Item Start Time Init

        this.endTimeTextView = new TextView(this.c);
        IdManager.addId("MealPlanUnitEndTime" + this.Id);
        this.endTimeTextView.setId( (int)IdManager.stringToIdMap.get("MealPlanUnitEndTime" + this.Id) );

        shape =  new GradientDrawable();
        shape.setCornerRadius( 8 );
        shape.setColor(Color.RED);

        this.endTimeTextView.setBackground(shape);
        this.endTimeTextView.setPadding(0,0,0,0);
        this.endTimeTextView.setTextColor(Color.WHITE);

        this.itemDelete = new Button(this.c);
        this.itemDelete.setText("Delete");

        shape =  new GradientDrawable();
        shape.setCornerRadius( 8 );
        shape.setColor(Color.BLUE);

        this.itemDelete.setBackground(shape);
        this.itemDelete.setPadding(0,0,0,0);
        this.itemDelete.setTextColor(Color.WHITE);
    }
    //

    public RelativeLayout draw()
    {
        this.mealPlanUnitLayout.setTag(this.getProdId()+";;"+this.getStartTime()+";;"+this.getEndTime());

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.setMargins(10,10,10,10);
        this.itemImage.setLayoutParams(params);

        //

        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.RIGHT_OF , this.itemImage.getId() );
        params.addRule(RelativeLayout.ALIGN_TOP);
        params.setMargins(10,10,10,10);
        this.startTimeTextView.setPadding(10,10,10,10);
        this.startTimeTextView.setLayoutParams(params);

        //

        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.RIGHT_OF , this.itemImage.getId() );
        params.addRule(RelativeLayout.BELOW,this.startTimeTextView.getId());
        params.setMargins(10,10,10,10);
        this.itemDelete.setPadding(10,10,10,10);
        this.itemDelete.setLayoutParams(params);

        //

        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.RIGHT_OF , this.startTimeTextView.getId() );
        params.addRule(RelativeLayout.ALIGN_TOP);
        params.setMargins(10,10,10,10);
        this.endTimeTextView.setPadding(10,10,10,10);
        this.endTimeTextView.setLayoutParams(params);

        //Adding Elements
        this.mealPlanUnitLayout.addView(this.itemImage);
        this.mealPlanUnitLayout.addView(this.startTimeTextView);
        this.mealPlanUnitLayout.addView(this.itemDelete);
        this.mealPlanUnitLayout.addView(this.endTimeTextView);

        this.mealPlanUnitLayout.setPadding(10,10,10,10);

        this.mealPlanUnitLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
                alertDialogBuilder.setTitle("Choose Start and End Time.");

                RelativeLayout rl = new RelativeLayout(c);

                ImageView iv = new ImageView(c);

                iv.setImageBitmap(image);

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.setMargins(10,10,10,10);
                iv.setLayoutParams(params);

                IdManager.addId("MealPlanUnitImageAlert" + Id);
                iv.setId( (int)IdManager.stringToIdMap.get("MealPlanUnitImageAlert" + Id) );

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
                            startTimeSet = tl1[i];

                            if( !(ValidateTime( startTimeSet , endTimeSet)) )
                            {
                                Toast.makeText(c, "Invalid. Start Time and End Time must have a minimum gap of Half an Hour. This entry won't be saved till valid.", Toast.LENGTH_LONG).show();

                            }
                            else
                            {
                                Toast.makeText(c, "Saving...", Toast.LENGTH_SHORT).show();
                                setStartEndTime(startTimeSet,endTimeSet);
                                startTimeTextView.setText(startTimeSet);
                                endTimeTextView.setText(endTimeSet);

                                DateProdLink.save();
                            }

                        }
                        flag = true;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                int count = 0;

                for( String s:tl1 )
                {
                    if( startTime.equals(s) )
                        break;
                    count++;
                }

                if( count < tl1.length )
                {
                    spinTime1.setSelection(count);
                    startTimeSet = tl1[count];
                }


                IdManager.addId("MealPlanUnitStartTimeSpinner" + Id);
                spinTime1.setId( (int)IdManager.stringToIdMap.get("MealPlanUnitStartTimeSpinner" + Id) );

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
                            endTimeSet = tl2[i];
                            if( !(ValidateTime( startTimeSet , endTimeSet)) )
                            {
                                Toast.makeText(c, "Invalid. Start Time and End Time must have a minimum gap of Half an Hour. This entry won't be saved till valid.", Toast.LENGTH_LONG).show();

                            }
                            else
                            {
                                Toast.makeText(c, "Saving...", Toast.LENGTH_SHORT).show();
                                setStartEndTime(startTimeSet,endTimeSet);
                                startTimeTextView.setText(startTimeSet);
                                endTimeTextView.setText(endTimeSet);

                                DateProdLink.save();
                            }
                        }
                        flag = true;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                count = 0;

                for( String s:tl2 )
                {
                    if( endTime.equals(s) )
                        break;
                    count++;
                }

                if( count < tl2.length )
                {
                    spinTime2.setSelection(count);
                    endTimeSet = tl2[count];
                }

                rl.addView(spinTime2);

                alertDialogBuilder.setView(rl);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        return this.mealPlanUnitLayout;
    }

    @Override
    public String toString() {
        return "MealPlanUnit{" +
                "startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", date='" + date + '\'' +
                ", prodId='" + prodId + '\'' +
                '}';
    }
}
