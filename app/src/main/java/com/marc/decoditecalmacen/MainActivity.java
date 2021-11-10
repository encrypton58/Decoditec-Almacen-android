package com.marc.decoditecalmacen;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.marc.decoditecalmacen.Views.Dashboard;
import com.marc.decoditecalmacen.dialogs.LoginDialog;
import com.marc.decoditecalmacen.login.Login;
import com.marc.decoditecalmacen.models.User;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText eEmail, ePass;
    private boolean correctEmailFormat = false, correctPassFormat = false;
    private LoginDialog dialogCharge;
    private TextView pe, ee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eEmail = findViewById(R.id.inputCorreo);
        ePass = findViewById(R.id.inputPass);
        eEmail = findViewById(R.id.inputCorreo);
        ePass = findViewById(R.id.inputPass);
        ee = findViewById(R.id.emailError);
        pe = findViewById(R.id.passError);
        Button bLogin = findViewById(R.id.loginButton);
        dialogCharge = new LoginDialog(MainActivity.this, "", false, false);
        eEmail.addTextChangedListener(watcherCorreo);
        ePass.addTextChangedListener(watcherPass);
        bLogin.setOnClickListener(v -> loguear(correctEmailFormat, correctPassFormat));
    }

    private void loguear(boolean correctEmailFormat, boolean correctPassFormat){
        Login login = new Login();
        String correo = eEmail.getText().toString();
        String pass = ePass.getText().toString();
        if(correctEmailFormat && correctPassFormat){
            dialogCharge.show();
            login.loginUser(correo,pass, this, msg -> {
                dialogCharge.dismiss();
                LoginDialog dialog;
                if(msg.equals("")){
                    User user = new Gson().fromJson(getSharedPreferences("user", MODE_PRIVATE).getString("userJson", ""), User.class);
                    if(user != null) {
                        if (!user.getToken().isEmpty()) {
                            startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        }else{
                            dialog = new LoginDialog(MainActivity.this, "No se Encontro El Token", true, true);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.show();
                        }
                    }else{
                        dialog = new LoginDialog(MainActivity.this, "Inicia Sesion de Nuevo", true, true);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }
                }else{
                    dialog = new LoginDialog(MainActivity.this, msg, true, true);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            });
        }
    }

    TextWatcher watcherCorreo = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            final Pattern VALID_EMAIL_ADDRESS_REGEX =
                    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(s);

            if(!s.toString().isEmpty() && !matcher.find()){
                ee.setText("No Es Una Direccion de Correo");
                correctEmailFormat = false;
            }else{
                ee.setText("");
                correctEmailFormat = true;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    TextWatcher watcherPass = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length() < 8){
                correctPassFormat = false;
                pe.setText("La contraseña debe ser minimo de 8 caracteres");
            }else{
                pe.setText("");
                correctPassFormat = true;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


}