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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.mobilelab.artyomska.planecatalog.model.Plane;
import com.mobilelab.artyomska.planecatalog.service.MainService;
import com.mobilelab.artyomska.planecatalog.utils.CheckInteger;
import com.mobilelab.artyomska.planecatalog.utils.CheckNetwork;

import java.util.HashMap;
import java.util.Map;

public class UpdateActivity extends AppCompatActivity {

    private Button updateButton;
    private Button clearButton;
    private EditText textName;
    private EditText textEngine;
    private EditText textProducer;
    private EditText textCountry;
    private EditText textYear;
    private EditText textWiki;
    private MainService service;
    private Plane oldPlane;
    private Plane newPlane;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        Gson gson = new Gson();
        String planeAsString = getIntent().getStringExtra("PlaneString");
        oldPlane = gson.fromJson(planeAsString, Plane.class);

        textName = findViewById(R.id.textName);
        textEngine = findViewById(R.id.textEngine);
        textProducer = findViewById(R.id.textProd);
        textCountry = findViewById(R.id.textCountry);
        textYear = findViewById(R.id.textYear);
        textWiki = findViewById(R.id.textWiki);

        updateButton = findViewById(R.id.buttonUpdatePlane);
        clearButton = findViewById(R.id.buttonClearPlane);

        textName.setText(oldPlane.getPlaneName());
        textEngine.setText(oldPlane.getPlaneEngine());
        textProducer.setText(oldPlane.getPlaneProducer());
        textCountry.setText(oldPlane.getPlaneCountry());
        textYear.setText(Integer.toString(oldPlane.getPlaneYear()));
        textWiki.setText(oldPlane.getWikiLink());


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                update(textName.getText().toString(), textEngine.getText().toString(),textProducer.getText().toString(),
                        textCountry.getText().toString(),textYear.getText().toString(),textWiki.getText().toString());
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                textName.setText("");
                textEngine.setText("");
                textProducer.setText("");
                textCountry.setText("");
                textYear.setText(0);
                textWiki.setText("");
            }
        });

        service = new MainService(getApplicationContext());
    }

    private void updateOffline(Plane newPlane)
    {
        if (service.updatePlane(newPlane))
        {
            AlertDialog alertDialog = new AlertDialog.Builder(UpdateActivity.this).create();
            alertDialog.setTitle("Success");
            alertDialog.setMessage("The plane was updated");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.putExtra("rez", "1");
                            setResult(RESULT_OK, intent);
                            finishActivity(1);
                            dialog.dismiss();
                            finish();
                        }
                    });
            alertDialog.show();
        }
        else
        {
            AlertDialog alertDialog = new AlertDialog.Builder(UpdateActivity.this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Plane couldn't be updated. Plane name already exists.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }


    private void update(String name, String engine, String producer, String country, String year, String wiki)
    {
        if (name.equals("") || engine.equals("") || producer.equals("") || country.equals("") || year.equals(""))
        {
            AlertDialog alertDialog = new AlertDialog.Builder(UpdateActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Please complete all the fields(wiki is optional)");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        else {
            if (!CheckInteger.isInteger(year, 10)) {
                AlertDialog alertDialog = new AlertDialog.Builder(UpdateActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Year must be a number");
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
                this.newPlane = new Plane(oldPlane.getID(),name, engine, producer, country, Integer.parseInt(year), wiki);
                if (CheckNetwork.isNetworkConnected(this))
                    deletePlane(newPlane);
                else
                    updateOffline(newPlane);
            }
        }
    }

    public void deletePlane(final Plane plane)
    {
        final ProgressDialog pDialog = new ProgressDialog(UpdateActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        String tag_json_obj = "json_obj_req";
        String url = "http:/DESKTOP-28CNHAN:8090//InventoryManagement/api/plane/UpdatePlane";
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                if (response.compareTo("true") != 0)
                {
                    pDialog.dismiss();
                    AlertDialog alertDialog = new AlertDialog.Builder(UpdateActivity.this).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Plane couldn't be updated. Plane name already exists.");
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
                    pDialog.dismiss();
                    AlertDialog alertDialog = new AlertDialog.Builder(UpdateActivity.this).create();
                    alertDialog.setTitle("Success");
                    alertDialog.setMessage("The plane was updated");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.putExtra("rez", "1");
                                    setResult(RESULT_OK, intent);
                                    dialog.dismiss();
                                    finishActivity(1);
                                    finish();
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
                Snackbar snackbar2 = Snackbar.make(getWindow().getDecorView().getRootView(), "Element couldn't be updated. Server error", Snackbar.LENGTH_SHORT).setDuration(2000);
                pDialog.dismiss();
                snackbar2.show();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                SharedPreferences settings = PreferenceManager
                        .getDefaultSharedPreferences(UpdateActivity.this);
                String auth_token_string = settings.getString("token", "");

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Basic " + auth_token_string);
                return params;
            }

            @Override
            protected Map<String,String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("ID",Integer.toString(plane.getID()));
                params.put("planeName", plane.getPlaneName());
                params.put("planeEngine", plane.getPlaneEngine());
                params.put("planeProducer", plane.getPlaneCountry());
                params.put("planeCountry", plane.getPlaneProducer());
                params.put("planeYear", Integer.toString(plane.getPlaneYear()));
                params.put("wikiLink", plane.getWikiLink());

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
