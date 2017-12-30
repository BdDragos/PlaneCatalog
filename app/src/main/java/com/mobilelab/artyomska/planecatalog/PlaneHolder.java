package com.mobilelab.artyomska.planecatalog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.mobilelab.artyomska.planecatalog.model.Plane;
import com.mobilelab.artyomska.planecatalog.service.MainService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

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
                                ((Activity) context).startActivityForResult(intent1, 1);

                                break;

                            case R.id.deletePlane:

                                deletePlane(plane,v);
                                break;

                            case R.id.insertPlane:

                                Intent intent2 = new Intent(context, InsertActivity.class);
                                ((Activity) context).startActivityForResult(intent2, 1);

                                break;

                            case R.id.openWiki:

                                String query = null;
                                try {
                                    query = URLEncoder.encode(plane.getPlaneName(), "utf-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                String url = "http://www.google.com/search?q=" + query;
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(url));
                                context.startActivity(intent);

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

    public void deletePlane(final Plane plane,final View v)
    {
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();

        String tag_json_obj = "json_obj_req";
        String url = "http://DESKTOP-28CNHAN:8090//InventoryManagement/api/plane/DeletePlane";
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                if (response.compareTo("true") == 0)
                {
                    Snackbar snackbar2 = Snackbar.make(v, "Element was deleted", Snackbar.LENGTH_SHORT).setDuration(2000);
                    pDialog.dismiss();
                    adapter.onRemoveItem(getAdapterPosition());
                    snackbar2.show();
                }
                else
                {
                    pDialog.dismiss();
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Can't delete");
                    alertDialog.setMessage("Internet connection error");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e("ERROR", "Error occurred ", error);
                pDialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String,String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("ID",Integer.toString(plane.getID()));
                params.put("planeName", plane.getPlaneName());
                params.put("planeEngine", plane.getPlaneEngine());
                params.put("planeProducer", plane.getPlaneCountry());
                params.put("planeCountry", plane.getPlaneProducer());
                params.put("planeYear", Integer.toString(plane.getPlaneYear()));
                params.put("wikiLink", plane.getWikiLink());

                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }
}
