package com.marc.decoditecalmacen.Callbacks;

import com.marc.decoditecalmacen.models.Material;

import java.util.List;

public interface InventaryCB {

    void registerInventary(boolean isRegister);

    void errorRegisterInventary(String msg);

    void getAllInventary(List<Material> inventary);
}
