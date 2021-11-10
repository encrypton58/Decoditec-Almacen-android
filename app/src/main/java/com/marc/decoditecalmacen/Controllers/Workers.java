package com.marc.decoditecalmacen.Controllers;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.marc.decoditecalmacen.Callbacks.trabajadores.CallbacksTrabajadores;
import com.marc.decoditecalmacen.http.Rutes;
import com.marc.decoditecalmacen.http.VolleySingleton;
import com.marc.decoditecalmacen.models.User;
import com.marc.decoditecalmacen.models.Worker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Workers {

    public void registerWorker(Context con, String nom, String num, String area, String imagenStr, CallbacksTrabajadores cbt){
        try {
            JSONObject body = new JSONObject();
            body.put("nombre", nom);
            body.put("numero", num);
            body.put("tvl", area);
            body.put("imagen", imagenStr);
            User user = new Gson().fromJson(con.getSharedPreferences("user", Context.MODE_PRIVATE).getString("userJson", ""), User.class);
            body.put("Authorization", user.getToken());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Rutes.REGISTRAR_TRABAJADOR, body,
                    response -> {
                        try {
                            boolean registro = response.has("registrado");
                            if(registro){
                                boolean registrado = (boolean) response.get("registrado");
                                cbt.registrarTrabajador(registrado, "");
                            }else{
                                String msg = response.get("msg").toString();
                                cbt.registrarTrabajador(false, msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            cbt.registrarTrabajador(false, "Error Al Parsear");
                            Toast.makeText(con, "Error en El parseo", Toast.LENGTH_SHORT).show();
                        }
                    }, error -> {
                cbt.registrarTrabajador(false, error.getMessage());
            });

            VolleySingleton.getInstanceVolley(con).addToRequestQueQue(request);
        }catch(JSONException e){
            Toast.makeText(con, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void getAllWorkers(Context con, CallbacksTrabajadores cbt){
        try {
            JSONObject some = new JSONObject();
            User user = new Gson().fromJson(con.getSharedPreferences("user", Context.MODE_PRIVATE).getString("userJson", ""), User.class);
            some.put("plataform", "android");
            some.put("token", user.getToken());
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                    Rutes.GET_ALL_WORKERS+"?plataform=android&token="+user.token, null,
                    response -> {
                        ArrayList<Worker> workers = new ArrayList<>();
                        for(int i = 0; i < response.length(); i++){
                            try {
                                JSONObject wj = (JSONObject) response.get(i);
                                Worker worker = new Worker(
                                        wj.getString("id_trabajador"), wj.getString("nombre"), wj.getString("area"));
                                workers.add(worker);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        cbt.getAllWorkers(workers);

                        Toast.makeText(con, workers.size() + "", Toast.LENGTH_SHORT).show();
                    }, error -> Toast.makeText(con, error.getMessage(), Toast.LENGTH_SHORT).show());
            VolleySingleton.getInstanceVolley(con).addToRequestQueQue(request);
        }catch(JSONException e){
            Toast.makeText(con, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
