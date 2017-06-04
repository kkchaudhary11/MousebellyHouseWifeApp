package com.mousebelly.app.housewifeapp.signUp;

import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cropper.CropImage;
import com.example.cropper.CropImageView;
import com.mousebelly.app.housewifeapp.APIs;
import com.mousebelly.app.housewifeapp.CustomToast;
import com.mousebelly.app.housewifeapp.Login.LoginActivity;
import com.mousebelly.app.housewifeapp.Login.VerifyLogin;
import com.mousebelly.app.housewifeapp.MainActivity;
import com.mousebelly.app.housewifeapp.R;
import com.mousebelly.app.housewifeapp.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;

import static com.mousebelly.app.housewifeapp.signUp.HouseWifeDetails.signUpBean;

/**
 * Created by Kamal Kant on 02-06-2017.
 */

public class HouseWifeAboutUs extends AppCompatActivity {

    ProgressDialog pg;
    ImageView imageView;
    Button selectImage;
    Button signUp;
    String path,type;
    TextView ImageInfo,ErrorSelectImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_housewife_about_us);


        imageView = (ImageView)findViewById(R.id.imageView);
        selectImage  = (Button)findViewById(R.id.cancel_cheque);
        ImageInfo  = (TextView)findViewById(R.id.image_info_text);
        ErrorSelectImage = (TextView)findViewById(R.id.error_select_image);
        signUp = (Button)findViewById(R.id.signup);

        pg = new ProgressDialog(HouseWifeAboutUs.this);


        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startCropImageActivity(null);

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(path == null && path.equals("")){
                    ErrorSelectImage.setVisibility(View.VISIBLE);
                    return;
                }
                new Usersend().execute();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            System.out.println("This is Result :"+result);
            if (resultCode == RESULT_OK) {
                /*((ImageView) findViewById(R.id.quick_start_cropped_image)).setImageURI(result.getUri());*/

                if (data != null) {

                    ImageInfo.setVisibility(View.GONE);

                    try {
                        System.out.println(result.getUri());
                        path = result.getUri().getPath();
                        System.out.println("This is Image path" + path);
                        String[] getType = path.split("\\.");
                        type = getType[getType.length - 1];
                        ((ImageView) findViewById(R.id.imageView)).setImageURI(result.getUri());


                        String fileString = "";

                            File f = new File(path);
                            byte[] b = new byte[(int) f.length()];
                            if(f == null) throw new Exception();

                            FileInputStream fileInputStream = new FileInputStream(f);
                            fileInputStream.read(b);

                            fileString = Base64.encodeToString(b,Base64.DEFAULT);

                            fileString = fileString.replace("\\\\","");
                            fileString = fileString.replace("\n","");
                            fileString = fileString.replace("\t","");

                            fileString = "data:image/"+type.toLowerCase()+";base64," + fileString;
                            System.out.println("This is File Sting :" + fileString);

                            signUpBean.setImage(fileString.replaceAll("\\\\","") );


                    } catch (Exception ex) {
                        ex.printStackTrace();
                        CustomToast.Toast(this,"Something went wrong !");
                    }
                }

                System.out.println("This is result get URI:"+result.getUri());
               // Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        System.out.println("This is Image URI : "+imageUri);
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);

    }

    private String getPathFromURI(Uri contentUri) {

        String[] projection = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();

        int column_index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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
                status = Server.s.post(APIs.housewifesign_housewifesign, signUpBean.tojson());

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        if (status == 200) {
                            CustomToast.Toast(HouseWifeAboutUs.this,"Account Created");

                            String resp = Server.s.get(APIs.housewifesign_housewifesign + signUpBean.getEmail());

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
                                        if (Email.equals(signUpBean.getEmail())) {
                                            String result = VerifyLogin.compare(signUpBean.getPassword(), Pwd) ? "Login Successful" : "Login Failed";
                                            System.out.println("Hash" + Pwd);

                                            if (result.equals("Login Successful")) {
                                                System.out.println("::::::::: Login Successfull :::::::::");
                                                Intent intent = new Intent(HouseWifeAboutUs.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                CustomToast.Toast(HouseWifeAboutUs.this,"Something went wrong");

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
                            CustomToast.Toast(HouseWifeAboutUs.this,"Something went wrong");
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


}
