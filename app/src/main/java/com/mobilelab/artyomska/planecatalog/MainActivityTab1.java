package com.mobilelab.artyomska.planecatalog;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.mobilelab.artyomska.planecatalog.service.MainService;
import com.mobilelab.artyomska.planecatalog.model.Plane;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Artyomska on 11/6/2017.
 */

public class MainActivityTab1 extends Fragment {

    private List<Plane> dataModels;
    private ListView planeList;
    private static ListViewAdapter adapter;
    private static MainService service;
    private static ArrayList<Plane> tmp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View RootView = inflater.inflate(R.layout.tab1, container, false);

        planeList = RootView.findViewById(R.id.planeList);
        service = new MainService(getActivity());
        dataModels = service.gettAllPlane();

        tmp = new ArrayList<>(dataModels);
        adapter= new ListViewAdapter(tmp,getActivity(),service);

        planeList.setAdapter(adapter);

        return RootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String stredittext = data.getStringExtra("id");
                if (stredittext.equals("value"))
                {
                    dataModels = service.gettAllPlane();

                    ArrayList<Plane> tmp = new ArrayList<>(dataModels);
                    adapter= new ListViewAdapter(tmp,getActivity(),service);

                    planeList.setAdapter(adapter);
                }
            }
        }
    }

    public static void updateFragment1ListView(Plane plane){
        if(adapter != null)
        {
            tmp.add(plane);
            adapter.notifyDataSetChanged();
        }
    }

    public static void updateListViewAfterUpdate(Plane oldPlane,Plane newPlane)
    {
        if(adapter != null)
        {
            tmp.remove(oldPlane);
            tmp.add(newPlane);
            adapter.notifyDataSetChanged();
        }
    }
}
