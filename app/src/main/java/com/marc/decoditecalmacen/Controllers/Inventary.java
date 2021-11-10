package com.marc.decoditecalmacen.Controllers;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.marc.decoditecalmacen.Callbacks.InventaryCB;
import com.marc.decoditecalmacen.http.Rutes;
import com.marc.decoditecalmacen.http.VolleySingleton;
import com.marc.decoditecalmacen.models.Material;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Inventary {

    public void registerInventary(Context con, String material, int cant, String code, InventaryCB cb) throws JSONException {
        JSONObject body = new JSONObject();
        body.put("material", material);
        body.put("cantidad", cant);
        body.put("codigo", code);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Rutes.ADD_TO_INVENTARY, body, response -> {
            try {
                boolean register = response.getBoolean("registrado");
                cb.registerInventary(register);
            } catch (JSONException e) {
                try {
                    cb.errorRegisterInventary(response.getString("msg"));
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
                e.printStackTrace();
            }
        }, error -> Toast.makeText(con, error.getMessage(), Toast.LENGTH_SHORT).show());
        VolleySingleton.getInstanceVolley(con).addToRequestQueQue(request);
    }

    public void getAllInventary(Context con, InventaryCB cb){
        JsonArrayRequest json = new JsonArrayRequest(Request.Method.GET, Rutes.GET_ALL_INVENTARY , null, response -> {
            List<Material> inventary = new ArrayList<>();
            for (int i = 0; i < response.length(); i++){
                try {
                    JSONObject js = response.getJSONObject(i);
                    inventary.add(new Material(js.getString("material"),
                            js.getString("codigo"), js.getInt("cantidad"), js.getInt("idin"), js.getString("lastDateUpdate")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            Toast.makeText(con, response.toString(), Toast.LENGTH_SHORT).show();
            cb.getAllInventary(inventary);
        }, error -> {
            Toast.makeText(con, error.getMessage(), Toast.LENGTH_SHORT).show();
        });
        VolleySingleton.getInstanceVolley(con).addToRequestQueQue(json);
    }

}
