package com.marc.decoditecalmacen.http;

public class Rutes {

    public static String host = "http://192.168.1.109:4000";
    public static final String LOGIN_USER = host + "/login";
    public static final String REGISTRAR_TRABAJADOR = host + "/trabajadores/register";
    public static final String GET_ALL_WORKERS = host + "/trabajadores/all";
    public static final String REGISTER_OUTSTORE = host + "/salidas/register";
    public static final String GET_OUTSSTORE_TODAY = host + "/salidas/salida/de";
    public static final String UPDATE_OUTSTORE = host + "/salidas/update";
    public static final String ADD_TO_OUTSTORE = host + "/salidas/material/add";
    public static final String ADD_TO_INVENTARY = host + "/inventario/register";
    public static final String GET_ALL_INVENTARY = host + "/inventario/all";


}
