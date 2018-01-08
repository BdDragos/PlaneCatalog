package com.mobilelab.artyomska.planecatalog;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mobilelab.artyomska.planecatalog.model.User;
import com.mobilelab.artyomska.planecatalog.service.LoginService;
import com.mobilelab.artyomska.planecatalog.utils.CheckNetwork;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText name;
    private EditText password;
    private LoginService service;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);

        if(hasLoggedIn)
        {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        name = findViewById(R.id.userInput);
        password = findViewById(R.id.passwordInput);
        Button loginButton = findViewById(R.id.loginButton);
        TextView attemptsText = findViewById(R.id.attemptsText);
        Button registerButton = findViewById(R.id.registerBut);

        attemptsText.setText("No of attempts remaining: 5");

        service = new LoginService(this);

        if (CheckNetwork.isNetworkConnected(this))
        {
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    validate(name.getText().toString(), password.getText().toString());
                }
            });
        }
        else
        {
            AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("No internet connection. The application will change to local database");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    validateOffline(name.getText().toString(), password.getText().toString());
                }
            });
        }
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    private void validateOffline(String userName, String userPassword)
    {
        if (service.authenticateUser(new User(userName,userPassword)))
        {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
            alertDialog.setTitle("Can't login");
            alertDialog.setMessage("Invalid username or password");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    private void validate(final String userName,final String userPassword)
    {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        String tag_json_obj = "json_obj_req";
        String url = "http:/DESKTOP-28CNHAN:8090/InventoryManagement/api/userdata/verify";
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                if (response.compareTo("false") != 0)
                {
                    pDialog.dismiss();
                    SharedPreferences settings = PreferenceManager
                            .getDefaultSharedPreferences(LoginActivity.this);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("token", response);
                    editor.putBoolean("hasLoggedIn", true);
                    editor.apply();
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    pDialog.dismiss();
                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog.setTitle("Can't login");
                    alertDialog.setMessage("Invalid username or password");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e("ERROR", "Error occurred ", error);
                Snackbar snackbar2 = Snackbar.make(getWindow().getDecorView().getRootView(), "Server error", Snackbar.LENGTH_SHORT).setDuration(2000);
                pDialog.dismiss();
                snackbar2.show();
            }
        })
        {
            @Override
            protected Map<String,String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("username", userName);
                params.put("password", userPassword);


                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }
}

