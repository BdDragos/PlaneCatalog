package com.mobilelab.artyomska.planecatalog;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilelab.artyomska.planecatalog.model.Plane;
import com.mobilelab.artyomska.planecatalog.service.MainService;

import java.util.ArrayList;

/**
 * Created by Artyomska on 11/7/2017.
 */

public class ListViewAdapter extends ArrayAdapter<Plane> implements View.OnClickListener{

    private ArrayList<Plane> dataSet;
    private Context mContext;
    private MainService service;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtProducer;
        TextView txtCountry;
        ImageView info;
    }

    public ListViewAdapter(ArrayList<Plane> data, Context context,MainService service) {
        super(context, R.layout.listview_row, data);
        this.dataSet = data;
        this.mContext = context;
        this.service = service;

    }

    @Override
    public void onClick(final View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        final Plane dataModel=(Plane)object;


        switch (v.getId())
        {
            case R.id.item_info:
                Snackbar snack = Snackbar.make(v, "Plane Year " +dataModel.getPlaneYear(), Snackbar.LENGTH_LONG)
                        .setDuration(4000)
                        .setAction("DELETE", new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                Snackbar snackbar1 = Snackbar.make(v, "Element was deleted", Snackbar.LENGTH_SHORT);
                                service.deletePlane(dataModel.getPlaneName());
                                remove(dataModel);
                                notifyDataSetChanged();
                                snackbar1.show();
                            }
                        });
                snack.show();
                break;
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Plane dataModel = getItem(position);
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview_row, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.planeName);
            viewHolder.txtProducer = convertView.findViewById(R.id.planeProducer);
            viewHolder.txtCountry = convertView.findViewById(R.id.planeCountry);
            viewHolder.info = convertView.findViewById(R.id.item_info);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtName.setText(dataModel.getPlaneName());
        viewHolder.txtProducer.setText(dataModel.getPlaneProducer());
        viewHolder.txtCountry.setText(dataModel.getPlaneCountry());
        viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}
