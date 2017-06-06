package com.mousebelly.app.housewifeapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mousebelly.app.housewifeapp.addproduct.AddProduct;
import com.mousebelly.app.housewifeapp.mealplanner.MealPlanner;
import com.mousebelly.app.housewifeapp.myfood.MyFood;

import static com.mousebelly.app.housewifeapp.Login.LoginActivity.USERID;
import static com.mousebelly.app.housewifeapp.Login.LoginActivity.USERNAME;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static Context context;
    public static NavigationView navigationView;
    public static FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        TextView nav_user = (TextView) hView.findViewById(R.id.user_email_id);
        TextView nav_username = (TextView) hView.findViewById(R.id.user_name);

        navigationView.getMenu().getItem(0).setChecked(true);

        nav_user.setText(USERID);
        nav_username.setText(USERNAME);

        customActionBar();

        //load myfood items
        MyFood myFood = new MyFood();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.relative_layout_fragment,myFood,myFood.getTag()).commit();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("parent called");

        AddProduct.croppedImage( requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Exit Application?");
            alertDialogBuilder
                    .setMessage("Do You Want to Exit")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    moveTaskToBack(true);
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                    System.exit(1);
                                }
                            })

                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.live_order) {
            // Handle the camera action
        }else if(id == R.id.my_food){
            MyFood myFood = new MyFood();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relative_layout_fragment,myFood,myFood.getTag()).commit();
        }
        else if (id == R.id.meal_planner) {
            MealPlanner mealPlan= new MealPlanner();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relative_layout_fragment,mealPlan,mealPlan.getTag()).commit();

        } else if (id == R.id.add_product) {
            AddProduct addProduct = new AddProduct();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relative_layout_fragment,addProduct,addProduct.getTag()).commit();

        } else if (id == R.id.feedback) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void customActionBar(){
        // Update the action bar title with the TypefaceSpan instance
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayOptions(actionBar.getDisplayOptions()
                | ActionBar.DISPLAY_SHOW_CUSTOM);


        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_texture));

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        ActionBar.LayoutParams titleLayout = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL);

        Typeface trumpitFaceHeader = Typeface.createFromAsset(getAssets(), this.getResources().getString(R.string.font_face));

        LinearLayout mainTitleLayout = new LinearLayout(MainActivity.this);
        mainTitleLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        mainTitleLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout maintitle = new LinearLayout(MainActivity.this);
        maintitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            maintitle.setClipToOutline(true);
        }
        maintitle.setBackgroundResource(R.drawable.customborder);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            maintitle.setElevation(2.0f);
        }
        mainTitleLayout.addView(maintitle);


        TextView actionBarText = new TextView(MainActivity.this);
        actionBarText.setTextColor(ContextCompat.getColor(MainActivity.context,R.color.Tamarillo));
        actionBarText.setPadding(30, 2, 0, 2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            actionBarText.setElevation(4.0f);
        }
        actionBarText.setText("Mouse");
        //actionBarText.setBackgroundColor(Color.parseColor("#ffffff"));
        actionBarText.setTextSize(25);
        actionBarText.setTypeface(trumpitFaceHeader);


        actionBarText.setLayoutParams(titleLayout);
        maintitle.addView(actionBarText);

        TextView actionBarText1 = new TextView(MainActivity.this);
        actionBarText1.setTextColor(ContextCompat.getColor(MainActivity.context,R.color.Yellow_Orange));
        actionBarText1.setPadding(0, 2, 30, 2);
        actionBarText1.setText(" - Belly");
        actionBarText1.setTypeface(trumpitFaceHeader);
        //actionBarText1.setBackgroundColor(Color.parseColor("#ffffff"));
        actionBarText1.setTextSize(25);
        maintitle.addView(actionBarText1);
        actionBar.setTitle("");
        actionBar.setCustomView(mainTitleLayout);

        //END OF CUSTOM ACTION BAR
        /////////////////////////


        //TODO uncomment for return to main activity on click
       /* maintitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Products products = new Products();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relative_layout_fragment,products, products.getTag()).commit();
            }
        });*/

    }




}
