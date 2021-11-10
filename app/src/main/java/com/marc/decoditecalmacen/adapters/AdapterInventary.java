package com.marc.decoditecalmacen.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.marc.decoditecalmacen.R;
import com.marc.decoditecalmacen.models.Material;

import java.util.List;

public class AdapterInventary extends RecyclerView.Adapter<AdapterInventary.InventaryViewHolder> {

    List<Material> inventaryList;

    @NonNull
    @Override
    public InventaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycler_item, parent, false);
        return new InventaryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull InventaryViewHolder holder, int position) {
        holder.title.setText(inventaryList.get(position).getMaterial());
        holder.cant.setText(String.valueOf(inventaryList.get(position).getCant()));
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setInventaryList(List<Material> inventary){
        this.inventaryList = inventary;
    }

    @Override
    public int getItemCount() {
        return inventaryList.size();
    }

    public static class InventaryViewHolder extends RecyclerView.ViewHolder{

        TextView title, cant;
        CheckBox box;

        public InventaryViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.recycler_item_material);
            cant = itemView.findViewById(R.id.recycler_item_cant);
            box = itemView.findViewById(R.id.recycler_item_checkbox);
        }
    }

}
