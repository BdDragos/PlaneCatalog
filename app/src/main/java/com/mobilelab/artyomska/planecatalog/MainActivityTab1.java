package com.mobilelab.artyomska.planecatalog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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



public class MainActivityTab1 extends Fragment {

    private MainService service;
    private RecyclerView planeList;
    private PlaneAdapter adapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private CheckNetwork network;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        MainActivity activity = (MainActivity) getActivity();

        this.adapter = activity.getAdapter();
        this.service = activity.getService();
        this.network = new CheckNetwork(activity);

        View RootView = inflater.inflate(R.layout.tab1, container, false);

        planeList = RootView.findViewById(R.id.planeList);
        LinearLayoutManager llm = new LinearLayoutManager(activity);
        planeList.setLayoutManager(llm);

        scrollListener = new EndlessRecyclerViewScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getPlanesFromServer(page);
            }
        };

        planeList.addOnScrollListener(scrollListener);
        planeList.setAdapter(adapter);

        getPlanesFromServer(1);


        return RootView;
    }

    private void getPlanesFromLocalDatabase()
    {

    }

    private void getPlanesFromServer(int page)
    {
        String tag_json_obj = "json_obj_req";
        String uri = String.format("http://DESKTOP-28CNHAN:8090//InventoryManagement/api/plane/allPagined?pageNumber=%1$s&_pageSize=%2$s&pageSize=%3$s",
                page,
                5,
                5);
        String url = "http://DESKTOP-28CNHAN:8090//InventoryManagement/api/plane/all";

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
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}
