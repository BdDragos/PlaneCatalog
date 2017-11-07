package com.mobilelab.artyomska.planecatalog;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.mobilelab.artyomska.planecatalog.model.Plane;

import java.util.ArrayList;

/**
 * Created by Artyomska on 11/6/2017.
 */

public class MainActivityTab1 extends Fragment {

    ArrayList<Plane> dataModels;
    private ListView planeList;
    private static ListViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View RootView = inflater.inflate(R.layout.tab1, container, false);

        planeList = (ListView)RootView.findViewById(R.id.planeList);

        dataModels = new ArrayList<>();
        dataModels.add(new Plane("FW-190","BMW","Focke-Wulf","Germany",1941));
        dataModels.add(new Plane("BF-109","Daimler","Messerschmit","Germany",1939));
        dataModels.add(new Plane("Spitfire","Rolls-Royce","Supermarine","UK",1941));
        dataModels.add(new Plane("P-51","Packard","North-American","USA",1941));

        adapter= new ListViewAdapter(dataModels,getActivity());

        planeList.setAdapter(adapter);

        return RootView;
    }
}
