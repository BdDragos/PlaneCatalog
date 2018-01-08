package com.mobilelab.artyomska.planecatalog;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.mobilelab.artyomska.planecatalog.model.Plane;
import com.mobilelab.artyomska.planecatalog.service.MainService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private MainActivityTab1 tab1;
    private MainService service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                1);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                1);

        service = new MainService(MainActivity.this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public MainService getService()
    {
        return this.service;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            SharedPreferences settings = PreferenceManager
                    .getDefaultSharedPreferences(MainActivity.this);
            SharedPreferences.Editor editor = settings.edit();
            editor.clear();
            editor.putBoolean("hasLoggedIn", false);
            editor.apply();
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.sync)
        {
            syncDatabases();
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String stredittext = data.getStringExtra("rez");
                if (stredittext.equals("1"))
                {
                    tab1.onIorU();
                }
            }
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position)
            {
                case 0:
                    tab1 = new MainActivityTab1();
                    return tab1;
                case 1:
                    return new MainActivityTab2();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }




    private void syncDatabases()
    {
        String tag_json_obj = "json_obj_req";
        String uri = "http://DESKTOP-28CNHAN:8090//InventoryManagement/api/plane/syncDatabases";
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait while syncing");
        pDialog.show();

        List<Plane> allPlane = service.gettAllPlane();
        Gson gson = new Gson();
        JSONArray jsArray = new JSONArray();

        for (Plane p : allPlane)
        {
            String jsonString = gson.toJson(p);
            try {
                JSONObject obj = new JSONObject(jsonString);
                obj.remove("ID");
                jsArray.put(obj);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST, uri, jsArray, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                ArrayList<Plane> newList = new ArrayList<>();
                try
                {
                    for(int i=0;i<response.length();i++)
                    {
                        String planeName,planeEngine,planeProducer,planeCountry,planeYear,wikiLink,ID;
                        JSONObject pl = response.getJSONObject(i);

                        String p1 = pl.optString("ID");
                        if (pl != null && !p1.isEmpty())
                            ID = pl.getString("ID");
                        else
                            ID = "0";

                        String p2 = pl.optString("planeName");
                        if (p2 != null && !p2.isEmpty())
                            planeName = pl.getString("planeName");
                        else
                            planeName = "";

                        String p3 = pl.optString("planeEngine");
                        if (p3 != null && !p3.isEmpty())
                            planeEngine = pl.getString("planeEngine");
                        else
                            planeEngine = "";

                        String p4 = pl.optString("planeProducer");
                        if (p4 != null && !p4.isEmpty())
                            planeProducer = pl.getString("planeProducer");
                        else
                            planeProducer = "";

                        String p5 = pl.optString("planeCountry");
                        if (p5 != null && !p5.isEmpty())
                            planeCountry = pl.getString("planeCountry");
                        else
                            planeCountry = "";

                        String p6 = pl.optString("planeYear");
                        if (p6 != null && !p6.isEmpty())
                            planeYear = pl.getString("planeYear");
                        else
                            planeYear = "0";

                        String p7 = pl.optString("wikiLink");
                        if (p7 != null && !p7.isEmpty())
                            wikiLink = pl.getString("wikiLink");
                        else
                            wikiLink = "";

                        Plane plf = new Plane(Integer.parseInt(ID),planeName,planeEngine,planeProducer,planeCountry,Integer.parseInt(planeYear),wikiLink);
                        newList.add(plf);
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                service.deleteAllFromPlane();
                pDialog.dismiss();
                tab1.onIorU();

            }

        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e("ERROR", "Error occurred ", error);
                pDialog.dismiss();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                SharedPreferences settings = PreferenceManager
                        .getDefaultSharedPreferences(MainActivity.this);
                String auth_token_string = settings.getString("token", "");

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Basic " + auth_token_string);
                return params;
            }
        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}
