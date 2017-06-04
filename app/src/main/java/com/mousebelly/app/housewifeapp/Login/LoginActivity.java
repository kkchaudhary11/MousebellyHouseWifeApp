package com.mousebelly.app.housewifeapp.Login;

import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.mousebelly.app.housewifeapp.APIs;
import com.mousebelly.app.housewifeapp.CustomProgressDialog;
import com.mousebelly.app.housewifeapp.LocalSaveModule;
import com.mousebelly.app.housewifeapp.MainActivity;
import com.mousebelly.app.housewifeapp.R;
import com.mousebelly.app.housewifeapp.Server;
import com.mousebelly.app.housewifeapp.SocketAccess;
import com.mousebelly.app.housewifeapp.signUp.SignUpMainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.socket.emitter.Emitter;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    public static Context context;

    private static final int REQUEST_READ_CONTACTS = 0;

    //public static Context currentActivity;
    public static String url;
    public static User user = new User();
    public static String USERID, USERNAME;
    private UserLoginTask mAuthTask = null;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    // private View mProgressView;
    private ProgressDialog loginProgress;
    private TextView Signup;

    TextView forgetPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginActivity.context = LoginActivity.this;

        loginProgress = CustomProgressDialog.getDialog(LoginActivity.this,"Verifying...");

        Typeface trumpitFace = Typeface.createFromAsset(getAssets(), this.getResources().getString(R.string.font_face));
        TextView mouse = (TextView) findViewById(R.id.mouse);
        mouse.setTypeface(trumpitFace);
        TextView belly = (TextView) findViewById(R.id.belly);
        belly.setTypeface(trumpitFace);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();


        mPasswordView = (EditText) findViewById(R.id.password);

        SharedPreferences cred = this.getSharedPreferences(LocalSaveModule.Credential, Context.MODE_PRIVATE);
        String savedEmail = cred.getString("UserName", "").toString();
        String savedPass = cred.getString("Password", "").toString();

        mEmailView.setText(savedEmail);
        mPasswordView.setText(savedPass);


        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });


        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = mEmailView.getText().toString();

                url = APIs.housewifesign_housewifesign + Email;
                mPasswordView.clearFocus();
                mEmailView.clearFocus();
                attemptLogin();
            }
        });

        Signup = (TextView) findViewById(R.id.email_sign_up_button);



        Signup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent = new Intent(LoginActivity.this, SignUpMainActivity.class);
                startActivity(signupIntent);
            }
        });


        forgetPass = (TextView) findViewById(R.id.forget_password);


        forgetPass.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                LinearLayout popupLayout = new LinearLayout(getApplicationContext());
                popupLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                popupLayout.setOrientation(LinearLayout.VERTICAL);




                final EditText emailOTP = new EditText(getApplicationContext());
                emailOTP.setPadding(10, 50, 10, 30);
                emailOTP.setHint("Your Email");
                emailOTP.setImeActionLabel("Done", EditorInfo.IME_ACTION_DONE);
                emailOTP.setImeOptions(EditorInfo.IME_ACTION_DONE);
                emailOTP.setInputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                popupLayout.addView(emailOTP);

                Button sendOTP = new Button(getApplicationContext());
                sendOTP.setText("Send OTP");
                sendOTP.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String emailAddress = emailOTP.getText().toString();
                        //url = APIs.mail_sendotp + emailAddress;
                        emailOTP.clearFocus();
                        attemptLogin();
                    }
                });
                popupLayout.addView(sendOTP);


                builder.setCustomTitle(popupLayout);
                builder.show();
            }
        });

    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        USERID = email;
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = this.getCurrentFocus();


        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            if (focusView != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            }
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            // mProgressView.setVisibility(View.VISIBLE);
           // loginProgress.setMessage("Verifying...");
            loginProgress.setCancelable(false);
            loginProgress.show();
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {


            String jsonstr = Server.s.get(url);
            if (jsonstr != null) {
                System.out.println("Value of " + jsonstr);


                try {
                    JSONArray jaar = new JSONArray(jsonstr);

                    JSONObject jsonObject = jaar.getJSONObject(0);
                    String Email = jsonObject.getString("HWFEmail_id");
                    String Pwd = jsonObject.getString("Pwd");
                    String UserName = jsonObject.getString("HWF_Name");
                    USERNAME = UserName;

                    user.setU_name(UserName);
                    user.setUserEmail_id(Email);
                    user.setPhone_No(jsonObject.getString("Phone_no"));


                    JSONArray cordjaar = jsonObject.getJSONArray("Cordinates");
                    for (int j = 0; j < cordjaar.length(); j++) {
                        JSONObject jsoncord = cordjaar.getJSONObject(j);
                        user.setLongitude(jsoncord.getString("long"));
                        user.setLat(jsoncord.getString("lat"));
                    }


                    JSONArray addjaar = jsonObject.getJSONArray("Address");
                    for (int j = 0; j < addjaar.length(); j++) {
                        JSONObject jsonObject2 = addjaar.getJSONObject(j);
                        user.setStreet_name(jsonObject2.getString("street_name"));
                        user.setZip_code(jsonObject2.getString("zip_code"));
                        user.setState_name(jsonObject2.getString("state_name"));
                        user.setCity_name(jsonObject2.getString("city_name"));
                        user.setCountry(jsonObject2.getString("country"));
                    }


                    System.out.println("Entered Password : " + mPassword);
                    if (Email.equals(mEmail)) {
                        String result = VerifyLogin.compare(mPassword, Pwd) ? "Login Successful" : "Login Failed";
                        System.out.println("Hash" + Pwd);

                        boolean status = VerifyLogin.compare(mPassword, Pwd);
                        if (status) {
                            LocalSaveModule.storePreferences(LoginActivity.this, mPassword);
                            System.out.println("saved");


                            System.out.println("Login Successful");

                            SocketAccess.socket.emit("logging", "");

                            System.out.println("listening");
                            SocketAccess.socket.on("loggedin", new Emitter.Listener() {
                                @Override
                                public void call(Object... args) {

                                    String socketId;

                                    try {
                                        JSONObject socketIdjson = new JSONObject(args[0].toString());
                                        socketId = socketIdjson.getString("socket_id");
                                        System.out.println("SOCKET ID : " + socketId);
                                        // System.out.println("http://mousebelly.herokuapp.com/sign/sockSessionid/"+user.getEmail()+"/"+socketId);

                                        //TODO uncomment to

                                        /* Server.s.put(APIs.sign_sockSessionid + USERID + "/" + socketId);

                                        Server.s.put(APIs.sign_LoggedinCheck + USERID + "/true");
                                        Server.s.put(APIs.sign_isConnectedtrue + USERID + "/true");*/

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }

                        return status;

                    }
//                        String result = VerifyLogin.compare(mPassword,Pwd)?"Login Successful":"Login Failed";
                    // Toast.makeText(getApplicationContext(),  , Toast.LENGTH_LONG).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {

                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();

                Intent logintomainactivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(logintomainactivity);

            } else {
                forgetPass.setVisibility(View.VISIBLE);
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                Toast.makeText(getApplicationContext(), "Login Failure", Toast.LENGTH_LONG).show();
            }

            loginProgress.dismiss();
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            loginProgress.dismiss();

            // mProgressView.setVisibility(View.GONE);
        }
    }


}

