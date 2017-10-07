package com.georeminder.src.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.georeminder.src.R;
import com.georeminder.src.beans.GeoNamesBean;
import com.georeminder.src.fragments.ListGeoFencingFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Javed.Salat on 9/13/2016.
 */
public class GeoFenceListAdapter extends RecyclerView.Adapter<GeoFenceListAdapter.MyViewHolder> {
    private List<GeoNamesBean> profileBeanList;
    public static final int iD = 0;
    public static ListGeoFencingFragment fragment;
    static OnCLickItemButtonListener onCLickItemButtonListener;
    private Map<Integer, Boolean> mFoldStates = new HashMap<>();
    Context mContext;


    public GeoFenceListAdapter(Context context, List<GeoNamesBean> profileBeans, ListGeoFencingFragment profileFragment) {
        this.profileBeanList = profileBeans;
        onCLickItemButtonListener = (OnCLickItemButtonListener) profileFragment;
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.listitem_geofence, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        GeoNamesBean contactInfo = profileBeanList.get(position);
        holder.listitemGeofenceName.setText(contactInfo.getGeoFencingName());
        holder.listitemGeofenceAddress.setText(contactInfo.getGeoAddress() + "");
        String radius = (contactInfo.getGeoFencingRadius() / 1000) +" "+ mContext.getString(R.string.Lable_Away);
        holder.listitemGeofenceRadius.setText(radius);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                setPosition(position);
                return false;
            }
        });

    }


    @Override
    public int getItemCount() {
        return profileBeanList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        @BindView(R.id.listitem_geofenceName)
        TextView listitemGeofenceName;
        @BindView(R.id.listitem_geofenceAddress)
        TextView listitemGeofenceAddress;
        @BindView(R.id.listitem_geofenceRadius)
        TextView listitemGeofenceRadius;
        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnCreateContextMenuListener(this);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickButton(getLayoutPosition());
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select The Action");
            contextMenu.add(0, iD,
                    0, R.string.remove_item);
        }
    }

    protected static void clickButton(int position) {
        onCLickItemButtonListener.onItemButtonClick(position);

    }

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public interface OnCLickItemButtonListener {
        public void onItemButtonClick(int position);
    }
}
