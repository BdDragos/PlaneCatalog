package com.mobilelab.artyomska.planecatalog;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mobilelab.artyomska.planecatalog.controller.MainController;
import com.mobilelab.artyomska.planecatalog.model.Plane;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String stredittext = data.getStringExtra("id");
                if (stredittext.equals("value"))
                {
                    dataModels = controller.gettAllPlane();

                    ArrayList<Plane> tmp = new ArrayList<>(dataModels);
                    adapter= new ListViewAdapter(tmp,getActivity());

                    planeList.setAdapter(adapter);
                }
            }
        }
    }

    public void updateFragment1ListView(){
        if(adapter != null){
            adapter.notifyDataSetChanged();
        }
    }
}
