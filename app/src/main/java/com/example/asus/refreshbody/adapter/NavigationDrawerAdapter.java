package com.example.asus.refreshbody.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.refreshbody.R;
import com.example.asus.refreshbody.model.NavigationDrawerItem;

import java.util.Collections;
import java.util.List;

/**
 * Created by Asus on 7/3/2016.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    List<NavigationDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public NavigationDrawerAdapter(Context context, List<NavigationDrawerItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row,parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavigationDrawerItem current = data.get(position);
        holder.title.setText(current.getTitle());
        holder.imageViewItem.setImageResource(current.getImage());
        if(position==2)
            holder.bottomView.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imageViewItem;
        View bottomView;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.textview_nav_drawer_item);
            imageViewItem=(ImageView)itemView.findViewById(R.id.img_nav_drawer_item);
            bottomView=(View) itemView.findViewById(R.id.bottom_line_nav_item);
        }
    }
}

