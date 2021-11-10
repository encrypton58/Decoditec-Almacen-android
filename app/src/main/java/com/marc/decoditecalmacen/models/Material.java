package com.marc.decoditecalmacen.models;

public class Material {

    String material;
    String code;
    int cant;
    int idin;
    String lastUpdate;

    public Material(String material, String code, int cant, int idin, String lastUpdate) {
        this.material = material;
        this.code = code;
        this.cant = cant;
        this.idin = idin;
        this.lastUpdate = lastUpdate;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCant() {
        return cant;
    }

    public void setCant(int cant) {
        this.cant = cant;
    }

    public int getIdin() {
        return idin;
    }

    public void setIdin(int idin) {
        this.idin = idin;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
