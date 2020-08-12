package com.cds_jo.pharmacyGI.assist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;


import com.cds_jo.pharmacyGI.Pop_Po_Select_Items;
import com.cds_jo.pharmacyGI.R;

import java.util.ArrayList;

public class Po_ListItemAdapter extends RecyclerView.Adapter<PoListViewHolder>  {
    Context c;
    ArrayList<Cls_Invf> spacecrafts;

    public Po_ListItemAdapter(Context c, ArrayList<Cls_Invf> spacecrafts  ) {
        this.c = c;
        this.spacecrafts = spacecrafts;
    }

    //INITIALIZE VH
    @Override
    public PoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.po_select_item_row,parent,false);
        return new PoListViewHolder(v,c);
    }
    @Override
    public void onBindViewHolder(PoListViewHolder holder, int position) {
        //BIND DATA
        holder.ItemNo.setText(spacecrafts.get(position).getItem_No());
        holder.ItemNm.setText(spacecrafts.get(position).getItem_Name());
        holder.tv_Qty.setText(spacecrafts.get(position).getQty());
        holder.tv_BoQty.setText(spacecrafts.get(position).getBounce());
        holder.ItemPrice.setText(spacecrafts.get(position).getPrice());
        holder.CustQty.setText(spacecrafts.get(position).getCustQty());
    }

    @Override
    public int getItemCount() {
        return spacecrafts.size();
    }



}