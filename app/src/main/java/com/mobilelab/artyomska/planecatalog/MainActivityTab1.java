package com.mobilelab.artyomska.planecatalog;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.mobilelab.artyomska.planecatalog.controller.MainController;
import com.mobilelab.artyomska.planecatalog.model.Plane;
import com.mobilelab.artyomska.planecatalog.repository.PlaneRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artyomska on 11/6/2017.
 */

public class MainActivityTab1 extends Fragment {

    private List<Plane> dataModels;
    private ListView planeList;
    private ListViewAdapter adapter;
    private MainController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View RootView = inflater.inflate(R.layout.tab1, container, false);

        planeList = RootView.findViewById(R.id.planeList);
        controller = new MainController(getActivity());
        dataModels = controller.gettAllPlane();

        ArrayList<Plane> tmp = new ArrayList<>(dataModels);
        adapter= new ListViewAdapter(tmp,getActivity());

        planeList.setAdapter(adapter);

        return RootView;
    }
}
