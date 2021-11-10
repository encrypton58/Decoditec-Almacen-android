package com.marc.decoditecalmacen.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.marc.decoditecalmacen.R;

import soup.neumorphism.NeumorphCardView;

public class Dashboard extends AppCompatActivity {

    NeumorphCardView registerWorker, registerOut, registerInventary, editInventary;
    private final int CAMERA_REQUEST_CODE = 101;
    private final int READ_EXTERNAL_CODE = 102;
    private final int WRITER_EXTERNAL_CODE = 103;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        registerWorker = findViewById(R.id.ds_r_worker);
        registerOut = findViewById(R.id.ds_out);
        registerInventary = findViewById(R.id.ds_r_regin);
        editInventary = findViewById(R.id.ds_r_ediin);
        registerWorker.setOnClickListener(v -> openActivity(new Intent(this, RegisterWorkers.class)));
        registerOut.setOnClickListener(v -> openActivity(new Intent(this, RegisterOut.class)));
        registerInventary.setOnClickListener( v -> openActivity(new Intent(this, RegisterInventary.class)));
        editInventary.setOnClickListener(v -> openActivity(new Intent(this, EditInventary.class)));
        setUpPermissions();
    }

    private void openActivity(Intent intent){
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_left_out);
    }

    private void setUpPermissions() {
        int permission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        int storagePer = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int storage = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED &&
                storage != PackageManager.PERMISSION_GRANTED && storagePer != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = new String[3];
            permissions[0] = Manifest.permission.CAMERA;
            permissions[1] = Manifest.permission.READ_EXTERNAL_STORAGE;
            permissions[2] = Manifest.permission.WRITE_EXTERNAL_STORAGE;


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, CAMERA_REQUEST_CODE);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_REQUEST_CODE){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(), "Se Requiere La Camara", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == READ_EXTERNAL_CODE){
            if(grantResults[1] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(), "Se Requiere La Lectura", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == WRITER_EXTERNAL_CODE){
            if(grantResults[2] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(), "Se Requiere La Escritura", Toast.LENGTH_SHORT).show();
            }
        }
    }

}