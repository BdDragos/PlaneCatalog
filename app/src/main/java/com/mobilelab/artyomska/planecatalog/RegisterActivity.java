package com.mobilelab.artyomska.planecatalog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mobilelab.artyomska.planecatalog.model.User;
import com.mobilelab.artyomska.planecatalog.service.LoginService;
import com.mobilelab.artyomska.planecatalog.utils.CheckNetwork;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText userText;
    private EditText passText;
    private EditText repeatPassText;
    private Button registerBut;
    private Button clearBut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userText = findViewById(R.id.userText);
        passText = findViewById(R.id.passText);
        repeatPassText = findViewById(R.id.repeatPassText);
        registerBut = findViewById(R.id.registerBut);
        clearBut = findViewById(R.id.clearBut);

        CheckNetwork network = new CheckNetwork(this);

        if (network.isNetworkConnected()) {
            registerBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    register(userText.getText().toString(), passText.getText().toString(), repeatPassText.getText().toString());
                }
            });
        }
        else {
            registerBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    registerOffline(userText.getText().toString(), passText.getText().toString(), repeatPassText.getText().toString());
                }
            });
        }
        clearBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userText.setText("");
                passText.setText("");
                repeatPassText.setText("");

            }
        });
    }

    private void registerOffline(String userName, String userPassword, String repeatUserPassword)
    {
        if (userName == null || userPassword == null || userName.length() < 4 || userPassword.length() < 4)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Username or password are empty/length is less than 4");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        else if (userPassword.compareTo(repeatUserPassword) != 0)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Passwords aren't the same.Please retype them");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        else
        {
            LoginService service = new LoginService(this);
            if (service.registerUser(new User(userName,userPassword)))
            {
                AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
                alertDialog.setTitle("Success");
                alertDialog.setMessage("User was registered");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                alertDialog.show();
            }
            else
            {
                AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
                alertDialog.setTitle("Can't register");
                alertDialog.setMessage("User already exists");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        }
    }


    private void register(final String userName, final String userPassword, String repeatUserPassword)
    {
        if (userName == null || userPassword == null || userName.length() < 4 || userPassword.length() < 4)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Username or password are empty/length is less than 4");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        else if (userPassword.compareTo(repeatUserPassword) != 0)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Passwords aren't the same.Please retype them");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        else {

            final ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            String url = "http://DESKTOP-28CNHAN:8090/InventoryManagement/api/userdata/AddUser";
            String tag_json_obj = "json_obj_req";

            StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
                    if (response.compareTo("true") == 0)
                    {
                        pDialog.dismiss();
                        AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
                        alertDialog.setTitle("Success");
                        alertDialog.setMessage("User was registered");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                });
                        alertDialog.show();
                    }
                    else
                    {
                        pDialog.dismiss();
                        AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
                        alertDialog.setTitle("Can't register");
                        alertDialog.setMessage("User already exists");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                    pDialog.dismiss();
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", userName);
                    params.put("password", userPassword);

                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
        }
    }
}
