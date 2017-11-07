package com.mobilelab.artyomska.planecatalog;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mobilelab.artyomska.planecatalog.controller.LoginController;
import com.mobilelab.artyomska.planecatalog.controller.MainController;

/**
 * Created by Artyomska on 11/6/2017.
 */

public class MainActivityTab2 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View RootView = inflater.inflate(R.layout.tab2, container, false);

        Button insertButton = RootView.findViewById(R.id.insertNewPlane);

        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View RootView)
            {
                Intent intent = new Intent(getActivity(), InsertActivity.class);
                startActivityForResult(intent,1);
            }
        });

        return RootView;
    }

}
