package com.mobilelab.artyomska.planecatalog;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mobilelab.artyomska.planecatalog.model.Plane;
import com.mobilelab.artyomska.planecatalog.service.MainService;
import com.mobilelab.artyomska.planecatalog.utils.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private PlaneAdapter adapter;
    private MainService service;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
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

        getPlanesFromServer();

        /*
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

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

    public PlaneAdapter getAdapter()
    {
        return this.adapter;
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
                    adapter.onIorUItem();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position)
            {
                case 0:
                    MainActivityTab1 tab1 = new MainActivityTab1();
                    return tab1;
                case 1:
                    MainActivityTab2 tab2 = new MainActivityTab2();
                    return tab2;
                case 2:
                    MainActivityTab3 tab3 = new MainActivityTab3();
                    return tab3;
                default:
                    return null;
            }
        }

        //IMPLEMENT PAGINATION FOR PAGES AND SERVER SIDED

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    private void getPlanesFromServer()
    {
        String tag_json_obj = "json_obj_req";

        String url = "http://DESKTOP-28CNHAN//InventoryManagement/api/plane/all";

        final ProgressDialog pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray >()
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

                        if (pl.getString("ID") != null)
                            ID = pl.getString("ID");
                        else
                            ID = "0";

                        if (pl.getString("planeName") != null)
                            planeName = pl.getString("planeName");
                        else
                            planeName = "";

                        if (pl.getString("planeEngine") != null)
                            planeEngine = pl.getString("planeEngine");
                        else
                            planeEngine = "";

                        if (pl.getString("planeProducer") != null)
                            planeProducer = pl.getString("planeProducer");
                        else
                            planeProducer = "";

                        if (pl.getString("planeCountry") != null)
                            planeCountry = pl.getString("planeCountry");
                        else
                            planeCountry = "";

                        if (pl.getString("planeYear") != null)
                            planeYear = pl.getString("planeYear");
                        else
                            planeYear = "0";

                        if (pl.getString("wikiLink") != null)
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
                pDialog.hide();

                Context context = MainActivity.this;
                service = new MainService(context);
                ArrayList<Plane> tmp = new ArrayList<>(newList);

                service.deleteAllFromPlane();
                for (Plane p : tmp)
                {
                    service.addNewPlane(p);
                }
                adapter = new PlaneAdapter(MainActivity.this, R.layout.listview_row, tmp, service);

            }

        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e("ERROR", "Error occurred ", error);
                pDialog.hide();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}
