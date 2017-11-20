package com.mobilelab.artyomska.planecatalog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mobilelab.artyomska.planecatalog.model.Plane;
import com.mobilelab.artyomska.planecatalog.service.MainService;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<Plane> implements View.OnClickListener{

    private Context mContext;
    private MainService service;

    private static class ViewHolder {
        TextView txtName;
        TextView txtProducer;
        TextView txtCountry;
        ImageView info;
    }

    public ListViewAdapter(ArrayList<Plane> data, Context context,MainService service)
    {
        super(context, R.layout.listview_row, data);
        this.mContext = context;
        this.service = service;

    }

    @Override
    public void onClick(final View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        final Plane plane=(Plane)object;

        switch (v.getId()) {
            case R.id.item_info:

                PopupMenu popup = new PopupMenu(mContext, v);
                popup.getMenuInflater().inflate(R.menu.clipboard_popup,popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.updatePlane:

                                Intent intent = new Intent(mContext, UpdateActivity.class);

                                Gson gson = new Gson();
                                String planeAsString = gson.toJson(plane);
                                intent.putExtra("PlaneString", planeAsString);
                                ((Activity) mContext).startActivityForResult(intent, 1);
                                break;

                            case R.id.deletePlane:

                                Snackbar snackbar1 = Snackbar.make(v, "Element was deleted", Snackbar.LENGTH_SHORT).setDuration(2000);
                                service.deletePlane(plane.getPlaneName());
                                remove(plane);
                                notifyDataSetChanged();
                                snackbar1.show();
                                break;

                            default:

                                break;
                        }

                        return true;
                    }
                });

                break;

            default:
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
