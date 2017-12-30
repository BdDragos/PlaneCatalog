package com.mobilelab.artyomska.planecatalog.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Artyomska on 12/30/2017.
 */

public class CheckNetwork {
    Context context;

    private Context _context;

    public CheckNetwork(Context context){
        this._context = context;
    }

    public boolean isNetworkConnected(){
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }
}
