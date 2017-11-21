package com.mobilelab.artyomska.planecatalog;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.mobilelab.artyomska.planecatalog.service.MainService;
import com.mobilelab.artyomska.planecatalog.model.Plane;
import com.mobilelab.artyomska.planecatalog.utils.MyParcelable;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class MainActivityTab1 extends Fragment {

    private MainService service;
    private RecyclerView planeList;
    private PlaneAdapter adapter;
    private static ArrayList<Plane> tmp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View RootView = inflater.inflate(R.layout.tab1, container, false);

        service = new MainService(getActivity());
        List<Plane> dataModels = service.gettAllPlane();

        tmp = new ArrayList<>(dataModels);

        adapter = new PlaneAdapter(getActivity(), R.layout.listview_row, tmp, service);
        planeList = RootView.findViewById(R.id.planeList);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        planeList.setLayoutManager(llm);
        planeList.setAdapter(adapter);


        return RootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

    }

}
