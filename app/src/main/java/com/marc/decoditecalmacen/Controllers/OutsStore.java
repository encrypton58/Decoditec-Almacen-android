package com.marc.decoditecalmacen.Controllers;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.marc.decoditecalmacen.Callbacks.OutsStoreCB;
import com.marc.decoditecalmacen.http.Rutes;
import com.marc.decoditecalmacen.http.VolleySingleton;
import com.marc.decoditecalmacen.models.User;

import org.json.JSONException;
import org.json.JSONObject;

public class OutsStore {

    public void registerOutStore(Context con, String id, String code, String fecha, int cant, OutsStoreCB cb) throws JSONException {

        JSONObject body = new JSONObject();
        body.put("worker", id);
        body.put("codeM", code);
        body.put("fecha", fecha);
        body.put("cant", cant);
        User user = new Gson().fromJson(con.getSharedPreferences("user", Context.MODE_PRIVATE).getString("userJson", ""), User.class);
        body.put("registro", user.getCorreo());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Rutes.REGISTER_OUTSTORE, body, response -> {
            Toast.makeText(con, response.toString(), Toast.LENGTH_SHORT).show();
            cb.isRegister(true);
        }, error -> Toast.makeText(con, error.getMessage(), Toast.LENGTH_SHORT).show());

        VolleySingleton.getInstanceVolley(con).addToRequestQueQue(request);
    }

    public void updateMaterial(Context con, String idWorker, String codeMaterial, String dateOut, int cant, OutsStoreCB oscb) throws JSONException {
        JSONObject body = new JSONObject();
        body.put("worker", idWorker);
        body.put("fecha", dateOut);
        body.put("code", codeMaterial);
        body.put("cant", cant);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, Rutes.UPDATE_OUTSTORE, body, response -> {
            Toast.makeText(con, response.toString(), Toast.LENGTH_SHORT).show();
        }, error -> {

        });

        VolleySingleton.getInstanceVolley(con).addToRequestQueQue(request);

    }

    public void getOutsStoreToday(Context con, String id, String fecha, String code, OutsStoreCB oscb) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, Rutes.GET_OUTSSTORE_TODAY + "?worker=" + id + "&fecha=" + fecha + "&codigo=" + code, null, response -> {
            boolean lastOutIsText;
            boolean outStoreIsText;
            try {
                if (response.toString().contains("No existe")) {
                    oscb.codes("", "", -1);
                } else {
                    lastOutIsText = response.get("lastOut").toString().equals("vacias"); // true or false
                    outStoreIsText = response.get("today").toString().equals("vacias"); // true or false

                    if (lastOutIsText && outStoreIsText) { //No hay Salidas
                        oscb.codes(null, null, 3); //no hay salidas
                    } else if (!outStoreIsText) {
                        OutsJson today = new Gson().fromJson(response.get("today").toString(), OutsJson.class);
                        Materials[] todayJson = new Gson().fromJson(today.getMaterial(), Materials[].class);
                        int index = -1;
                        boolean flag = false;
                        for (int i = 0; i < todayJson.length; i++) {
                            if (todayJson[i].getCodigo().equals(code)) {
                                flag = true;
                                index = i;
                                break;
                            }
                        }
                        if (flag) {
                            oscb.codes(today.getFecha().split("T")[0], todayJson[index].getCodigo(), 1);//estas registrado hoy
                        } else {
                            oscb.codes(today.getFecha().split("T")[0], null, 2);
                        }
                    } else {
                        OutsJson last = new Gson().fromJson(response.get("lastOut").toString(), OutsJson.class);
                        oscb.codes(last.getFecha().split("T")[0], code, 4);
                    }


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(con, error.getMessage(), Toast.LENGTH_SHORT).show());
        VolleySingleton.getInstanceVolley(con).addToRequestQueQue(request);
    }

    public void addMaterialOutstore(Context con, String idWorker, String date, String code, int cant, OutsStoreCB cb) throws JSONException {
        JSONObject body = new JSONObject();
        body.put("worker", idWorker);
        body.put("fecha", date);
        body.put("codigo", code);
        body.put("cant", cant);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, Rutes.ADD_TO_OUTSTORE, body, response -> {
            Toast.makeText(con, response.toString(), Toast.LENGTH_LONG).show();
        }, error -> {
            Toast.makeText(con, error.getMessage(), Toast.LENGTH_SHORT).show();
        });
        VolleySingleton.getInstanceVolley(con).addToRequestQueQue(request);
    }

    private static class OutsJson {

        String outsStore;
        int id_salida;
        String id_trabajador;
        String material;
        String fecha;
        String lastOut;

        public String getOutsStore() {
            return outsStore;
        }

        public void setOutsStore(String outsStore) {
            this.outsStore = outsStore;
        }

        public int getId_salida() {
            return id_salida;
        }

        public void setId_salida(int id_salida) {
            this.id_salida = id_salida;
        }

        public String getId_trabajador() {
            return id_trabajador;
        }

        public void setId_trabajador(String id_trabajador) {
            this.id_trabajador = id_trabajador;
        }

        public String getMaterial() {
            return material;
        }

        public void setMaterial(String material) {
            this.material = material;
        }

        public String getFecha() {
            return fecha;
        }

        public void setFecha(String fecha) {
            this.fecha = fecha;
        }

        public String getLastOut() {
            return lastOut;
        }

        public void setLastOut(String lastOut) {
            this.lastOut = lastOut;
        }
    }

    private static class Materials {

        String codigo;
        int cantidad;

        public Materials(String codigo, int cantidad) {
            this.codigo = codigo;
            this.cantidad = cantidad;
        }

        public String getCodigo() {
            return codigo;
        }

        public void setCodigo(String codigo) {
            this.codigo = codigo;
        }

        public int getCantidad() {
            return cantidad;
        }

        public void setCantidad(int cantidad) {
            this.cantidad = cantidad;
        }
    }

}
