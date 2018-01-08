package com.mobilelab.artyomska.planecatalog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mobilelab.artyomska.planecatalog.service.MainService;
import com.mobilelab.artyomska.planecatalog.model.Plane;
import com.mobilelab.artyomska.planecatalog.utils.CheckNetwork;
import com.mobilelab.artyomska.planecatalog.utils.EndlessRecyclerViewScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class MainActivityTab1 extends Fragment {

    private MainService service;
    private PlaneAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        MainActivity activity = (MainActivity) getActivity();

        this.service = activity.getService();

        View RootView = inflater.inflate(R.layout.tab1, container, false);

        RecyclerView planeList = RootView.findViewById(R.id.planeList);

        LinearLayoutManager llm = new LinearLayoutManager(activity);
        planeList.setLayoutManager(llm);

        EndlessRecyclerViewScrollListener scrollListener;
        if (CheckNetwork.isNetworkConnected(getActivity()))
            scrollListener = new EndlessRecyclerViewScrollListener(llm) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    getPlanesFromServer(page);
                }
            };
        else
            scrollListener = new EndlessRecyclerViewScrollListener(llm) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    getPlanesFromLocalDatabase(page);
                }
            };

        adapter = new PlaneAdapter(activity, R.layout.listview_row, service,scrollListener);
        planeList.setAdapter(adapter);
        planeList.addOnScrollListener(scrollListener);
        scrollListener.resetState();
        if (CheckNetwork.isNetworkConnected(getActivity()))
            getPlanesFromServer(0);
        else
            getPlanesFromLocalDatabase(0);


        return RootView;

    }

    private void getPlanesFromLocalDatabase(int page)
    {
        List<Plane> planes = service.gettAllPlane();
        ArrayList<Plane> fin = new ArrayList<>();
        List<Object> result = planes.stream().skip(page  * 5).limit(5).collect(Collectors.toList());
        for (Object x : result)
        {
            fin.add((Plane)x);
        }
        adapter.addNewDataPage(fin);

    }

    private void getPlanesFromServer(int page)
    {
        String tag_json_obj = "json_obj_req";
        String uri = String.format("http://DESKTOP-28CNHAN:8090//InventoryManagement/api/plane/allPagined?pageNumber=%1$s&_pageSize=%2$s&pageSize=%3$s",
                page,
                5,
                5);

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET, uri, null, new Response.Listener<JSONArray>()
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

                adapter.addNewDataPage(newList);

            }

        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e("ERROR", "Error occurred ", error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                SharedPreferences settings = PreferenceManager
                        .getDefaultSharedPreferences(getActivity());
                String auth_token_string = settings.getString("token", "");

                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=UTF-8");
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

    public void onIorU()
    {
        adapter.onIorUItem();
    }
}
