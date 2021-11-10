package com.marc.decoditecalmacen.Views;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.marc.decoditecalmacen.Callbacks.InventaryCB;
import com.marc.decoditecalmacen.Controllers.Inventary;
import com.marc.decoditecalmacen.R;
import com.marc.decoditecalmacen.models.Material;
import com.tapadoo.alerter.Alerter;
import org.json.JSONException;

import java.util.List;

public class RegisterInventary extends AppCompatActivity implements InventaryCB{

    private EditText material, code, can;
    private String codeStr = "-1";
    private CodeScannerView scanner;
    private  CodeScanner sc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_inventary);
        material = findViewById(R.id.ri_in_material);
        code = findViewById(R.id.ri_in_code);
        can = findViewById(R.id.ri_in_cant);
        Button register = findViewById(R.id.ri_register_in_inventary);
        scanner = findViewById(R.id.ri_scanner_code);
        sc = new CodeScanner(this, scanner);

        code.setOnClickListener(codeListener);
        register.setOnClickListener(registerListener);

        sc.startPreview();
        sc.setDecodeCallback(cb);
    }

    DecodeCallback cb = result -> {
        codeStr = result.getText();
        code.setText(result.getText());
    };

    View.OnClickListener registerListener = v -> {
        try {
            if(!material.getText().toString().isEmpty() && !can.getText().toString().isEmpty() && !codeStr.equals("-1")) {
                new Inventary().registerInventary(getApplicationContext(),
                        material.getText().toString(), Integer.parseInt(can.getText().toString()),
                        codeStr, this);
            }else{
                Toast.makeText(this, "Llena Todos los Datos", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    };

    View.OnClickListener codeListener = v -> Alerter.create(RegisterInventary.this)
            .setTitle("Escanea el codigo con la camara")
            .setDuration(3500)
            .setIcon(R.drawable.icon_scanner).show();

    @Override
    public void registerInventary(boolean isRegister) {
        if(isRegister) {
            Alerter.create(RegisterInventary.this)
                    .setTitle("Registrado Correctamente")
                    .setBackgroundColorRes(R.color.success)
                    .setText("Se Registro Correctamente En inventario")
                    .setDuration(4000).show();

            material.setText("");
            code.setText("");
            can.setText("");
            scanner.setFocusableInTouchMode(true);
            scanner.requestFocus();
            sc.startPreview();
        }else{
            Alerter.create(RegisterInventary.this)
                    .setTitle("No se pudo registrar")
                    .setBackgroundColorRes(R.color.design_default_color_error)
                    .setText("No se pudo registrar en inventario")
                    .setDuration(4000).show();
        }
    }

    @Override
    public void errorRegisterInventary(String msg) {
        Alerter.create(RegisterInventary.this)
                .setTitle("Ocurrio un error")
                .setBackgroundColorRes(R.color.design_default_color_error)
                .setText(msg)
                .setDuration(4000).show();
    }

    @Override
    public void getAllInventary(List<Material> inventary) {

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left, R.anim.slide_rigth_out);
    }
}