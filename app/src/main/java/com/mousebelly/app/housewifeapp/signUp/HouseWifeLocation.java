package com.mousebelly.app.housewifeapp.signUp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.test.mock.MockPackageManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mousebelly.app.housewifeapp.APIs;
import com.mousebelly.app.housewifeapp.CustomToast;
import com.mousebelly.app.housewifeapp.GPSTracker;
import com.mousebelly.app.housewifeapp.Login.LoginActivity;
import com.mousebelly.app.housewifeapp.Login.VerifyLogin;
import com.mousebelly.app.housewifeapp.MainActivity;
import com.mousebelly.app.housewifeapp.R;
import com.mousebelly.app.housewifeapp.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;


public class HouseWifeLocation extends FragmentActivity implements OnMapReadyCallback {

    private static final int REQUEST_CODE_PERMISSION = 2;
    static MarkerOptions markerOptions;

    String mPermission = android.Manifest.permission.ACCESS_FINE_LOCATION;
    GPSTracker gps;
    double latitude,longitude;

    Marker marker;

    EditText StreetName, ZipCode, StateName, CityName, Country;
    String AddStreetName, AddZipCode, AddStateName, AddCityName, AddCountry;
    TextView addph, addcnf, addpass, adderrmail, adderruser;
    Button ok;
    LatLng latLng;
    String StreetCheck, zipCheck, StateCheck, CityCheck, CountryCheck;
    private GoogleMap mMap;
    Dialog dialog;

    ProgressDialog pg;/*= new ProgressDialog(HouseWifeLocation.this.getParent());*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_housewife_location);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        pg = new ProgressDialog(HouseWifeLocation.this);

        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //Location();



        dialog = new Dialog(HouseWifeLocation.this);
        dialog.setContentView(R.layout.housewife_location_address_dialog_box);
        dialog.setTitle("Enter Address");


        StreetName = (EditText) dialog.findViewById(R.id.streetname);
        ZipCode = (EditText) dialog.findViewById(R.id.zipcode);
        StateName = (EditText) dialog.findViewById(R.id.statename);
        CityName = (EditText) dialog.findViewById(R.id.cityname);
        Country = (EditText) dialog.findViewById(R.id.country);
        /*cancle = (Button) dialog.findViewById(R.id.cancle);*/
        ok = (Button) dialog.findViewById(R.id.ok);


        addph = (TextView) dialog.findViewById(R.id.addphn);
        addcnf = (TextView) dialog.findViewById(R.id.addcnf);
        addpass = (TextView) dialog.findViewById(R.id.addpass);
        adderrmail = (TextView) dialog.findViewById(R.id.adderroremail);
        adderruser = (TextView) dialog.findViewById(R.id.adderrusername);

        locationDialogBox();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                locationDialogBox();


            }

        });


        Button signUp = (Button) findViewById(R.id.aboutme);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(AddStreetName)) {
                    StreetName.setError("This field is required");
                    return;
                }
                if (TextUtils.isEmpty(AddZipCode)) {
                    ZipCode.setError("This field is required");
                    return;
                }
                if (TextUtils.isEmpty(AddCityName)) {
                    CityName.setError("This field is required");
                    return;
                }
                if (TextUtils.isEmpty(AddStateName)) {
                    StateName.setError("This field is required");
                    return;
                }
                if (TextUtils.isEmpty(AddCountry)) {
                    Country.setError("This field is required");
                    return;
                }


                if (StreetName.getText().toString().equals("") || StreetName.getText().toString() == null || !StreetCheck.equals("Ok") ||
                        ZipCode.getText().toString().equals("") || ZipCode.getText().toString() == null || !zipCheck.equals("Ok") ||
                        StateName.getText().toString().equals("") || StateName.getText().toString() == null || !StateCheck.equals("Ok") ||
                        CityName.getText().toString().equals("") || CityName.getText().toString() == null || !CityCheck.equals("Ok") ||
                        Country.getText().toString().equals("") || Country.getText().toString() == null || !CountryCheck.equals("Ok")) {
                    System.out.println("not properly filled");
                    Toast.makeText(HouseWifeLocation.this, "One or more fields are not properly filled", Toast.LENGTH_SHORT).show();

                } else {

                  /*  Intent intent = new Intent(HouseWifeLocation.this,HouseWifeAboutUs.class);
                    startActivity(intent);*/

                    SignUpMainActivity.tabHost.setCurrentTab(2);

                   // new Usersend().execute();
                }
            }
        });

    }


    public void Location() {

        gps = new GPSTracker(HouseWifeLocation.this);

        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            HouseWifeDetails.signUpBean.setLatitude(String.valueOf(latitude));
            HouseWifeDetails.signUpBean.setLongitude(String.valueOf(longitude));
        } else {
            gps.showSettingsAlert();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Location();

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitude, longitude);
        marker = mMap.addMarker(new MarkerOptions().position(sydney).title("Drag me to your Home").draggable(true).snippet("MouseBelly"));
        marker.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14.0f));


        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                LatLng ll = marker.getPosition();
                HouseWifeDetails.signUpBean.setLatitude(String.valueOf(ll.latitude));
                HouseWifeDetails.signUpBean.setLongitude(String.valueOf(ll.longitude));

            }
        });

        // An AsyncTask class for accessing the GeoCoding Web Service


    }

    class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // mView.show(getSupportFragmentManager(), "");

        }

        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;

            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {
            //  mView.dismiss();

            if (addresses == null || addresses.size() == 0) {
                Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
            }

            // Clears all the existing markers on the map
            mMap.clear();

            try {
                for (int i = 0; i < addresses.size(); i++) {

                    Address address = addresses.get(i);

                    // Creating an instance of GeoPoint, to display in Google Map
                    latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    String addressText = String.format("%s, %s",
                            address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                            address.getCountryName());

                    markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.draggable(true);
                    markerOptions.title("Drag me to your Home");
                    markerOptions.snippet(addressText);
                    marker.showInfoWindow();

                    //set the value of lat and long if user does't move marker.
                    HouseWifeDetails.signUpBean.setLatitude(String.valueOf(latLng.latitude));
                    HouseWifeDetails.signUpBean.setLongitude(String.valueOf(latLng.longitude));

                    mMap.addMarker(markerOptions);


                    mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                        @Override
                        public void onMarkerDragStart(Marker marker) {

                        }

                        @Override
                        public void onMarkerDrag(Marker marker) {

                        }

                        @Override
                        public void onMarkerDragEnd(Marker marker) {
                            LatLng ll = marker.getPosition();
                            HouseWifeDetails.signUpBean.setLatitude(String.valueOf(ll.latitude));
                            HouseWifeDetails.signUpBean.setLongitude(String.valueOf(ll.longitude));

                        }
                    });

                    // Locate the first location
                    if (i == 0)
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                }
            } catch (Exception e) {
                e.printStackTrace();
                // Adding Markers on Google Map for each matching address

            }
        }
    }

    public class Usersend extends AsyncTask<Void, Void, Void> {

        int status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pg.setCancelable(false);
            pg.setMessage("Creating...");
           pg.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            System.out.println("This is do in background");

            try {
                status = Server.s.post(APIs.housewifesign_housewifesign, HouseWifeDetails.signUpBean.tojson());

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        if (status == 200) {
                            CustomToast.Toast(HouseWifeLocation.this,"Account Created");

                            String resp = Server.s.get(APIs.housewifesign_housewifesign + HouseWifeDetails.signUpBean.getEmail());

                            if (resp != null) {
                                try {
                                    JSONArray jaar = new JSONArray(resp);
                                    for (int i = 0; i < jaar.length(); i++) {
                                        JSONObject jsonObject = jaar.getJSONObject(i);
                                        String Email = jsonObject.getString("UserEmail_id");
                                        String Pwd = jsonObject.getString("Pwd");
                                        String UserName = jsonObject.getString("U_name");
                                        String Wallet = jsonObject.getString("wallet");
                                        LoginActivity.USERNAME = UserName;
                                        LoginActivity.user.setU_name(jsonObject.getString("U_name"));
                                        LoginActivity.user.setUserEmail_id(jsonObject.getString("UserEmail_id"));
                                        LoginActivity.USERID = jsonObject.getString("UserEmail_id");
                                        LoginActivity.user.setPhone_No(jsonObject.getString("Phone_No"));
                                        LoginActivity.user.setWallet(Wallet);

                                        JSONArray cordjaar = jsonObject.getJSONArray("Cordinates");
                                        for (int j = 0; j < cordjaar.length(); j++) {
                                            JSONObject jsoncord = cordjaar.getJSONObject(j);
                                            LoginActivity.user.setLongitude(jsoncord.getString("long"));
                                            LoginActivity.user.setLat(jsoncord.getString("lat"));
                                        }

                                        JSONArray addjaar = jsonObject.getJSONArray("Address");
                                        for (int j = 0; j < addjaar.length(); j++) {
                                            JSONObject jsonObject2 = addjaar.getJSONObject(j);
                                            LoginActivity.user.setStreet_name(jsonObject2.getString("street_name"));
                                            LoginActivity.user.setZip_code(jsonObject2.getString("zip_code"));
                                            LoginActivity.user.setState_name(jsonObject2.getString("state_name"));
                                            LoginActivity.user.setCity_name(jsonObject2.getString("city_name"));
                                            LoginActivity.user.setCountry(jsonObject2.getString("country"));
                                        }

                                        // System.out.println("hello1 "+mPassword);
                                        if (Email.equals(HouseWifeDetails.signUpBean.getEmail())) {
                                            String result = VerifyLogin.compare(HouseWifeDetails.signUpBean.getPassword(), Pwd) ? "Login Successful" : "Login Failed";
                                            System.out.println("Hash" + Pwd);

                                            if (result.equals("Login Successful")) {
                                                System.out.println("::::::::: Login Successfull :::::::::");
                                                Intent intent = new Intent(HouseWifeLocation.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                CustomToast.Toast(HouseWifeLocation.this,"Something went wrong");

                                            }
                                        }
//                        String result = VerifyLogin.compare(mPassword,Pwd)?"Login Successful":"Login Failed";
                                        // Toast.makeText(getApplicationContext(),  , Toast.LENGTH_LONG).show();

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }


                        } else {
                            CustomToast.Toast(HouseWifeLocation.this,"Something went wrong");
                        }
                    }
                });

                    /*Toast.makeText(Uploadphoto.this, "Account Created", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Uploadphoto.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }*/
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPreExecute();
            pg.dismiss();

        }
    }


    public void locationDialogBox() {
        StreetName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                AddStreetName = StreetName.getText().toString();
                StreetCheck = HouseWifeDetails.signUpBean.validateAddressStreet(StreetName.getText().toString());
                adderruser.setVisibility(View.VISIBLE);
                adderruser.setText(StreetCheck);
                if (StreetCheck.equals("Ok")) {
                    adderruser.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ZipCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                AddZipCode = ZipCode.getText().toString();
                zipCheck = HouseWifeDetails.signUpBean.validateAddressZipcode(ZipCode.getText().toString());
                adderrmail.setVisibility(View.VISIBLE);
                adderrmail.setText(zipCheck);
                if (zipCheck.equals("Ok")) {
                    adderrmail.setVisibility(View.GONE);
                }
            }
        });

        StateName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                AddStateName = StateName.getText().toString();
                StateCheck = HouseWifeDetails.signUpBean.validateAddressState(StateName.getText().toString());
                addpass.setVisibility(View.VISIBLE);
                addpass.setText(StateCheck);
                if (StateCheck.equals("Ok")) {
                    addpass.setVisibility(View.GONE);
                }
            }
        });

        CityName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                AddCityName = CityName.getText().toString();
                System.out.println(AddCityName);
                CityCheck = HouseWifeDetails.signUpBean.validateAddressCity(AddCityName);

                if (CityCheck.equals("Ok")) {
                    addcnf.setVisibility(View.GONE);
                } else {
                    addcnf.setVisibility(View.VISIBLE);
                    addcnf.setText(CityCheck);
                }
            }
        });

        Country.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                AddCountry = Country.getText().toString();
                CountryCheck = HouseWifeDetails.signUpBean.validateAddressCountry(Country.getText().toString());
                addph.setVisibility(View.VISIBLE);
                addph.setText(CountryCheck);
                if (CountryCheck.equals("Ok")) {
                    addph.setVisibility(View.GONE);
                }

            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (TextUtils.isEmpty(AddStreetName)) {
                    StreetName.setError("This field is required");
                    return;
                }
                if (TextUtils.isEmpty(AddZipCode)) {
                    ZipCode.setError("This field is required");
                    return;
                }
                if (TextUtils.isEmpty(AddCityName)) {
                    CityName.setError("This field is required");
                    return;
                }
                if (TextUtils.isEmpty(AddStateName)) {
                    StateName.setError("This field is required");
                    return;
                }
                if (TextUtils.isEmpty(AddCountry)) {
                    Country.setError("This field is required");
                    return;
                }

                if (StreetName.getText().toString().equals("") || StreetName.getText().toString() == null || !StreetCheck.equals("Ok") ||
                        ZipCode.getText().toString().equals("") || ZipCode.getText().toString() == null || !zipCheck.equals("Ok") ||
                        StateName.getText().toString().equals("") || StateName.getText().toString() == null || !StateCheck.equals("Ok") ||
                        CityName.getText().toString().equals("") || CityName.getText().toString() == null || !CityCheck.equals("Ok") ||
                        Country.getText().toString().equals("") || Country.getText().toString() == null || !CountryCheck.equals("Ok")) {
                    Toast.makeText(HouseWifeLocation.this, "One or more are fields are not properly filled", Toast.LENGTH_SHORT).show();

                } else {
                    String MainAddress = AddStreetName + " " + AddZipCode + " " + AddStateName + " " + AddCityName + " " + AddCountry;


                    if (MainAddress != null && !MainAddress.equals("")) {
                        new GeocoderTask().execute(MainAddress);
                    }

                }
                dialog.dismiss();
            }
        });


        dialog.show();
    }

}
