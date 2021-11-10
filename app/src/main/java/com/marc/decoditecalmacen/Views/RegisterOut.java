package com.marc.decoditecalmacen.Views;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.marc.decoditecalmacen.Callbacks.OutsStoreCB;
import com.marc.decoditecalmacen.Callbacks.trabajadores.CallbacksTrabajadores;
import com.marc.decoditecalmacen.Controllers.OutsStore;
import com.marc.decoditecalmacen.Controllers.Workers;
import com.marc.decoditecalmacen.R;
import com.marc.decoditecalmacen.dialogs.DialogAmount;
import com.marc.decoditecalmacen.models.Worker;
import com.tapadoo.alerter.Alerter;
import com.tapadoo.alerter.OnHideAlertListener;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;

public class RegisterOut extends AppCompatActivity implements OutsStoreCB, DialogAmount.AmountCallback {

    CodeScanner scanner;
    EditText date, material;
    AutoCompleteTextView numberWorker;
    ArrayList<Worker> workers;
    String dateOut, idWorker, codeMaterial;
    int cant = 0;
    boolean isRegister, isAddMaterial;
    DialogAmount dialogAmount;

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_out);
        Button register = findViewById(R.id.ro_rg);
        date = findViewById(R.id.ro_in_dates);
        CodeScannerView csw = findViewById(R.id.ro_scanner);
        csw.setOnClickListener(v -> {
            if (idWorker == null) {
                Toast.makeText(this, "Selecciona Primero Un Trabajador", Toast.LENGTH_SHORT).show();
            } else {
                scanner.startPreview();
            }
        });
        numberWorker = findViewById(R.id.ro_number);
        numberWorker.setOnTouchListener((v, event) -> {
            final int DRAWABLE_3 = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (numberWorker.getRight() - numberWorker.getCompoundDrawables()[DRAWABLE_3].getBounds().width())) {
                    numberWorker.setText("");
                    return true;
                }
            }
            return false;
        });
        register.setOnClickListener(registerListener);
        material = findViewById(R.id.ro_in_material);
        material.setOnClickListener(v -> {
            if (idWorker == null) {
                Toast.makeText(this, "Selecciona Primero Un Trabajador", Toast.LENGTH_SHORT).show();
            } else {
                scanner.startPreview();
            }
        });
        numberWorker.setOnItemClickListener((parent, view, position, id) -> {
            Worker worker = (Worker) parent.getAdapter().getItem(position);
            this.idWorker = worker.getId();
            numberWorker.setText(worker.getId() + "-" + worker.getName());
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(numberWorker.getWindowToken(), 0);
            scanner.startPreview();
        });

        Workers http = new Workers();
        http.getAllWorkers(getApplicationContext(), cbt);
        scanner = new CodeScanner(getApplicationContext(), csw);
        scanner.setDecodeCallback(callback);
        date.setOnClickListener(v -> setDate());
        Calendar c = Calendar.getInstance();
        String day = c.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + c.get(Calendar.DAY_OF_MONTH) : String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        date.setText(day + "/" + month + "/" + year);
        dateOut = year + "-" + month + "-" + day;
    }

    public void setDate() {
        final Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(this, dateSetListener, y, m, d);
        dialog.getDatePicker().setBackgroundColor(getResources().getColor(R.color.darker_gray));
        dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
        month++;
        dateOut = year + "-" + month + "-" + dayOfMonth;
        date.setText(dayOfMonth + "/" + month + "/" + year);
    };

    DecodeCallback callback = result -> {
        if (result.getText().length() > 5) {
            codeMaterial = result.getText();
            dialogAmount = new DialogAmount(RegisterOut.this, this);
            new OutsStore().getOutsStoreToday(getApplicationContext(), idWorker, dateOut, codeMaterial, this);
            runOnUiThread(() -> material.setText(result.getText()));
        } else {
            runOnUiThread(() -> numberWorker.setText(result.getText()));
        }
    };

    View.OnClickListener registerListener = v -> {
        try {
            OutsStore outsStore = new OutsStore();
            if(cant < 0){
                Toast.makeText(this, "No Existe el producto", Toast.LENGTH_SHORT).show();
            } else if (!isRegister && !isAddMaterial) {
                outsStore.registerOutStore(getApplicationContext(), idWorker, codeMaterial, dateOut, cant, this);
            } else if (isRegister && !isAddMaterial) {
                outsStore.updateMaterial(getApplicationContext(), idWorker, codeMaterial, dateOut, cant, this);
            } else {
                outsStore.addMaterialOutstore(getApplicationContext(), idWorker, dateOut, codeMaterial, cant, this);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    };

    CallbacksTrabajadores cbt = new CallbacksTrabajadores() {
        @Override
        public void registrarTrabajador(boolean registrado, String msg) {

        }

        @Override
        public void getAllWorkers(ArrayList<Worker> workers1) {
            workers = workers1;
            ArrayAdapter<Worker> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, workers);
            numberWorker.setAdapter(adapter);
        }
    };

    @Override
    public void isRegister(boolean register) {

    }

    @Override
    public void codes(String fecha, String code, int isRegister) {

        switch (isRegister) {
            case -1:
                Alerter.create(RegisterOut.this)
                        .setTitle("No existe el material")
                        .setDuration(4000)
                        .show();
                cant = isRegister;
                break;
            case 1:
                Alerter.create(RegisterOut.this)
                        .setTitle("Información Importante")
                        .setText("El material con codigo: " + code + " se dio Hoy Por Ultima vez")
                        .enableSwipeToDismiss()
                        .setOnHideListener(listener)
                        .enableInfiniteDuration(true)
                        .show();
                this.isRegister = true;
                this.isAddMaterial = false;
                break;
            case 2:
                Alerter.create(RegisterOut.this)
                        .setTitle("El usuario Ya saco Hoy material")
                        .setText("Pero este material no uwu")
                        .setDuration(4000)
                        .setOnHideListener(listener)
                        .show();
                this.isRegister = true;
                this.isAddMaterial = true;
                break;
            case 3:
                Alerter.create(RegisterOut.this)
                        .setTitle("No ha pedido nada el dia de hoy ni antes este material")
                        .setDuration(2000)
                        .setOnHideListener(listener)
                        .show();
                this.isRegister = false;
                this.isAddMaterial = false;
                break;
            case 4:
                Alerter.create(RegisterOut.this)
                        .setTitle("Información Importante")
                        .setText("El material con codigo: " + code + " se dio por ultima vez a este trabajador el dia " + fecha)
                        .enableSwipeToDismiss()
                        .setOnHideListener(listener)
                        .enableInfiniteDuration(true)
                        .show();
                this.isRegister = false;
                this.isAddMaterial = false;
                break;
        }
    }

    OnHideAlertListener listener = () -> dialogAmount.show();

    @Override
    public void setAmount(int amount) {
        cant = amount;
        dialogAmount.dissmiss();
    }
}