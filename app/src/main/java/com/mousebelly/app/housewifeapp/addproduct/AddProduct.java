package com.mousebelly.app.housewifeapp.addproduct;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cropper.CropImage;
import com.example.cropper.CropImageView;
import com.mousebelly.app.housewifeapp.APIs;
import com.mousebelly.app.housewifeapp.CustomProgressDialog;
import com.mousebelly.app.housewifeapp.CustomToast;
import com.mousebelly.app.housewifeapp.MainActivity;
import com.mousebelly.app.housewifeapp.R;
import com.mousebelly.app.housewifeapp.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class AddProduct extends Fragment{

    View v;
    List<String> CategoryNameArray;
    Spinner prod_category;
    static Product product;
    RelativeLayout rl;
    ProgressBar pg;
    Button addImage, addFood;
    static ImageView imageView;
    EditText ProdName,ProdDescription, ProdPrice;
    ProgressDialog progressDialog;


    public AddProduct() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_add_product, container, false);

        product = new Product();

        rl = (RelativeLayout)v.findViewById(R.id.rl);
        rl.setVisibility(View.INVISIBLE);
        pg = (ProgressBar)v.findViewById(R.id.pg);
        prod_category = (Spinner) v.findViewById(R.id.product_category);
        addImage = (Button)v.findViewById(R.id.add_image);
        addFood = (Button)v.findViewById(R.id.add_food);
        ProdName = (EditText)v.findViewById(R.id.prod_name);
        ProdDescription = (EditText)v.findViewById(R.id.prod_description);
        ProdPrice =(EditText)v.findViewById(R.id.prod_price);
        imageView = ((ImageView)v.findViewById(R.id.food_imageView));

        progressDialog = CustomProgressDialog.getDialog(MainActivity.context,"Saving");

        MainActivity.fab.setVisibility(View.INVISIBLE);

        new fetchCategories().execute();

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startCropImageActivity(null);

            }
        });

        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Product_name = ProdName.getText().toString();
                String Prod_description = ProdDescription.getText().toString();
                String Prod_price = ProdPrice.getText().toString();

                if(TextUtils.isEmpty(Product_name)){
                    ProdName.setError("This field is required");
                    return;
                }if ( TextUtils.isEmpty(Prod_description)){
                    ProdDescription.setError("This filed is required");
                    return;}


                if (TextUtils.isEmpty(Prod_price) ){
                    ProdPrice.setError("This filed is required");
                    return;
                }
                int prodPriceTemp = Integer.parseInt(Prod_price);
                if(prodPriceTemp<10 && prodPriceTemp>1000){
                    CustomToast.Toast(MainActivity.context,"Enter Valid Price");
                    return;
                }
                if(product.getImage()==null && product.getImage().equals("")){
                    CustomToast.Toast(MainActivity.context,"Image is required");
                    return;
                }

                product.setProd_name(Product_name);
                product.setDescription(Prod_description);
                product.setPrice(Prod_price);

                 new SaveFood().execute();
            }
        });

        return v;
    }


    public class fetchCategories extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pg.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... voids) {

            String jsonstr = Server.s.get(APIs.prod_category_prod_category);
            if (jsonstr != null) {
                try {
                    JSONArray jaar = new JSONArray(jsonstr);
                    CategoryNameArray = new ArrayList<>();
                    for (int i = 0; i < jaar.length(); i++) {
                        JSONObject jsonObject = jaar.getJSONObject(i);
                        String Categorylist;
                        try {
                            Categorylist = jsonObject.getString("category_name");

                        }catch (Exception e){
                            Categorylist = "No category Define";
                        }
                        CategoryNameArray.add(Categorylist);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {

            pg.setVisibility(View.GONE);
            rl.setVisibility(View.VISIBLE);

            prod_category.setAdapter(new ArrayAdapter<String>(MainActivity.context,
                            android.R.layout.simple_spinner_dropdown_item,
                            CategoryNameArray));

            prod_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    String Prod_category =(CategoryNameArray.get(i));
                    product.setProd_cat(Prod_category);
                    System.out.println("This is Category   : "+Prod_category);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }
    }


    public static void croppedImage(int requestCode, int resultCode, Intent data){

        String path,type;
        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            System.out.println("This is Result :"+result);
            if (resultCode == RESULT_OK) {

                if (data != null) {

                    //ImageInfo.setVisibility(View.GONE);

                    try {
                        System.out.println(result.getUri());
                        path = result.getUri().getPath();
                        System.out.println("This is Image path" + path);
                        String[] getType = path.split("\\.");
                        type = getType[getType.length - 1];
                        imageView.setImageURI(result.getUri());


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

                        product.setImage(fileString.replaceAll("\\\\","") );


                    } catch (Exception ex) {
                        ex.printStackTrace();
                        CustomToast.Toast(MainActivity.context,"Something went wrong !");
                    }
                }

                System.out.println("This is result get URI:"+result.getUri());
                // Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(MainActivity.context, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        System.out.println("This is Image URI : "+imageUri);
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(getActivity());
    }

    public class SaveFood extends AsyncTask<Void,Void,Void>{

        public void onPreExecute(){
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Server.s.post(APIs.prod_approval_prod_approval, product.tojson());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void res) {
            super.onPostExecute(res);
            progressDialog.dismiss();
        }

    }



}
