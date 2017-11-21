package com.mobilelab.artyomska.planecatalog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mobilelab.artyomska.planecatalog.model.Plane;
import com.mobilelab.artyomska.planecatalog.service.MainService;

public class PlaneHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private PlaneAdapter adapter;
    private final TextView txtName;
    private final TextView txtProducer;
    private final TextView txtCountry;
    private final ImageView info;
    private final MainService service;


    private Plane plane;
    private Context context;

    public PlaneHolder(Context context, View itemView, MainService service,PlaneAdapter adapter)
    {

        super(itemView);
        this.adapter = adapter;
        this.context = context;
        this.info = itemView.findViewById(R.id.item_info);
        this.txtName = itemView.findViewById(R.id.planeName);
        this.txtProducer = itemView.findViewById(R.id.planeProducer);
        this.txtCountry = itemView.findViewById(R.id.planeCountry);
        itemView.setOnClickListener(this);

        this.service = service;
    }

    public void bindPlane(Plane plane) {

        this.plane = plane;
        this.txtName.setText(plane.getPlaneName());
        this.txtProducer.setText(plane.getPlaneProducer());
        this.txtCountry.setText(plane.getPlaneCountry());
        this.info.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v)
    {
        final Plane plane = this.plane;
        switch (v.getId())
        {
            case R.id.item_info:

                PopupMenu popup = new PopupMenu(context, v);
                popup.getMenuInflater().inflate(R.menu.clipboard_popup,popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.updatePlane:

                                Intent intent1 = new Intent(context, UpdateActivity.class);

                                Gson gson = new Gson();
                                String planeAsString = gson.toJson(plane);
                                intent1.putExtra("PlaneString", planeAsString);
                                context.startActivity(intent1);

                                Snackbar snackbar1 = Snackbar.make(v, "Element was updated", Snackbar.LENGTH_SHORT).setDuration(2000);
                                adapter.onIorUItem();
                                snackbar1.show();

                                break;

                            case R.id.deletePlane:

                                Snackbar snackbar2 = Snackbar.make(v, "Element was deleted", Snackbar.LENGTH_SHORT).setDuration(2000);
                                service.deletePlane(plane.getPlaneName());
                                adapter.onRemoveItem(getAdapterPosition());
                                snackbar2.show();
                                break;

                            case R.id.insertPlane:

                                Intent intent2 = new Intent(context, InsertActivity.class);
                                context.startActivity(intent2);

                                Snackbar snackbar3 = Snackbar.make(v, "Element was inserted", Snackbar.LENGTH_SHORT).setDuration(2000);
                                adapter.onIorUItem();
                                snackbar3.show();

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
}
