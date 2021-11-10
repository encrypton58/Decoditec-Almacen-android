package com.marc.decoditecalmacen.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.marc.decoditecalmacen.Callbacks.LoginCallback.LoginCallBack;
import com.marc.decoditecalmacen.http.Rutes;
import com.marc.decoditecalmacen.http.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Login {

    public void loginUser(String correo, String pass, Context con, LoginCallBack callBack){

            HashMap<String, String> params = new HashMap<>();
            params.put("correo", correo);
            params.put("pass", pass);
            params.put("plataform", "android");

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                    Rutes.LOGIN_USER, new JSONObject(params), response -> {
                try {
                    JSONObject res = response.getJSONObject("response");
                    if(res.has("login")){
                        callBack.logged(res.getString("login"));
                    }else if(res.has("msg")){
                        callBack.logged(res.getString("msg"));
                    }else if(res.has("user")){
                        SharedPreferences spu = con.getSharedPreferences("user", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = spu.edit();
                        editor.putString("userJson", res.getString("user"));
                        editor.apply();
                        callBack.logged("");
                    }

                }catch (JSONException e){
                    callBack.logged(e.getMessage());
                    System.out.println(e.getMessage());

                }

            }, error -> {
                callBack.logged(error.getMessage());
                Toast.makeText(con.getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            });

            VolleySingleton.getInstanceVolley(con).addToRequestQueQue(jsonRequest);
    }

}
