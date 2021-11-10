package com.marc.decoditecalmacen.Views;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.marc.decoditecalmacen.Callbacks.trabajadores.CallbacksTrabajadores;
import com.marc.decoditecalmacen.Controllers.Workers;
import com.marc.decoditecalmacen.R;
import com.marc.decoditecalmacen.dialogs.LoaderDialog;
import com.marc.decoditecalmacen.dialogs.SuccessOrErrorDialog;
import com.marc.decoditecalmacen.models.Worker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

public class RegisterWorkers extends AppCompatActivity {

    private ImageView imageShow;
    private String imgStr;
    private Uri uriImage;
    private String currentPathImage;
    private LoaderDialog ld;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_workers);

        //set hooks with ui
        Spinner areas = findViewById(R.id.rw_areas);
        imageShow = findViewById(R.id.rw_show_taked_image);
        final EditText name = findViewById(R.id.rw_in_name);
        final EditText number = findViewById(R.id.rw_in_number);
        final Button takePhoto = findViewById(R.id.rw_take_photo);
        final Button register = findViewById(R.id.rw_register_worker);
        areas.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                R.layout.spinner_item, getResources().getStringArray(R.array.areas)));

        takePhoto.setOnClickListener(listenerPickPhoto);
        register.setOnClickListener(v -> {
            ld = new LoaderDialog(RegisterWorkers.this);
            ld.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            ld.show();
            String nombre = name.getText().toString();
            String numero = number.getText().toString();
            String areaStr = areas.getSelectedItem().toString();
            new Workers().registerWorker(getApplicationContext(), nombre, numero, areaStr, imgStr, cbt);
        });
    }

    @SuppressLint("QueryPermissionsNeeded")
    View.OnClickListener listenerPickPhoto = v -> {
        Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(in.resolveActivity(getApplicationContext().getPackageManager()) != null){
            File foto = null;
            try{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    foto = createImageFile();
                }else{
                    Toast.makeText(getApplicationContext(), "No se puede ejecutar la función", Toast.LENGTH_SHORT).show();
                }
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
            if(foto != null){
                Uri uriFoto = FileProvider.getUriForFile(getApplicationContext(),
                        "com.marc.decoditecalmacen.file", foto);
                uriImage = uriFoto;
                in.putExtra(MediaStore.EXTRA_OUTPUT, uriFoto);
                startActivityForResult(in, 1);
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Bitmap img = BitmapFactory.decodeFile(currentPathImage);
            imageShow.setImageBitmap(img);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                try {
                    Bitmap convertStr = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uriImage);
                    imgStr = imageToString(convertStr);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "No Soporta La aplicacion el dispositivo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String imageToString(Bitmap map){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        map.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] imgBytes = baos.toByteArray();
        return Base64.getEncoder().encodeToString(imgBytes);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SimpleDateFormat")
    File createImageFile()throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;
        File storageDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        currentPathImage = image.getAbsolutePath();
        return image;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left, R.anim.slide_rigth_out);
    }

    CallbacksTrabajadores cbt = new CallbacksTrabajadores() {
        @Override
        public void registrarTrabajador(boolean registrado, String msg) {
            ld.dismiss();
            SuccessOrErrorDialog sd = new SuccessOrErrorDialog(RegisterWorkers.this, registrado, msg);
            sd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            sd.show();
        }

        @Override
        public void getAllWorkers(ArrayList<Worker> workers) {

        }
    };

}