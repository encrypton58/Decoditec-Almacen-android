package com.marc.decoditecalmacen.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.marc.decoditecalmacen.Callbacks.InventaryCB;
import com.marc.decoditecalmacen.Controllers.Inventary;
import com.marc.decoditecalmacen.R;
import com.marc.decoditecalmacen.adapters.AdapterInventary;
import com.marc.decoditecalmacen.models.Material;

import java.util.List;

public class EditInventary extends AppCompatActivity implements InventaryCB {

    RecyclerView view;
    AdapterInventary some = new AdapterInventary();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_inventary);
        view = findViewById(R.id.ei_r_v);

        new Inventary().getAllInventary(getApplicationContext(), this);

    }

    @Override
    public void registerInventary(boolean isRegister) {

    }

    @Override
    public void errorRegisterInventary(String msg) {

    }

    @Override
    public void getAllInventary(List<Material> inventary) {
        some.setInventaryList(inventary);
        view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        view.setAdapter(some);

    }
}