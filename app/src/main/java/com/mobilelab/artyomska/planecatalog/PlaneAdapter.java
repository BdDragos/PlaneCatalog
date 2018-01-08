package com.mobilelab.artyomska.planecatalog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobilelab.artyomska.planecatalog.model.Plane;
import com.mobilelab.artyomska.planecatalog.service.MainService;
import com.mobilelab.artyomska.planecatalog.utils.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artyomska on 11/21/2017.
 */

public class PlaneAdapter extends RecyclerView.Adapter<PlaneHolder> {

    private ArrayList<Plane> planes;
    private Context context;
    private int itemResource;
    private MainService service;
    private EndlessRecyclerViewScrollListener scrollListener;

    public PlaneAdapter(Context context, int itemResource, MainService service, EndlessRecyclerViewScrollListener scrollListener) {

        // 1. Initialize our adapter
        this.context = context;
        this.itemResource = itemResource;
        this.service = service;
        this.scrollListener = scrollListener;
        this.planes = new ArrayList<>();
    }

    // 2. Override the onCreateViewHolder method
    @Override
    public PlaneHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // 3. Inflate the view and return the new ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(this.itemResource, parent, false);
        return new PlaneHolder(this.context, view, this.service,this);
    }

    // 4. Override the onBindViewHolder method
    @Override
    public void onBindViewHolder(PlaneHolder holder, int position) {

        Plane bakery = this.planes.get(position);
        holder.bindPlane(bakery);
    }

    @Override
    public int getItemCount() {

        return this.planes.size();
    }


    public void onRemoveItem(int position)
    {
        Plane plane = this.planes.get(position);
        planes.remove(plane);
        notifyDataSetChanged();
    }

    public void onIorUItem()
    {
        notifyDataSetChanged();
        scrollListener.resetState();
    }

    public void addNewDataPage(ArrayList<Plane> data)
    {
        final int size = planes.size() + 1;
        planes.addAll(data);
        notifyItemRangeInserted(size, data.size());
    }

}
