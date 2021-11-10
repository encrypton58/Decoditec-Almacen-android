package com.marc.decoditecalmacen.Callbacks.trabajadores;

import com.marc.decoditecalmacen.models.Worker;

import java.util.ArrayList;

public interface CallbacksTrabajadores {

    void registrarTrabajador(boolean registrado, String msg);

    void getAllWorkers(ArrayList<Worker> workers);

}
