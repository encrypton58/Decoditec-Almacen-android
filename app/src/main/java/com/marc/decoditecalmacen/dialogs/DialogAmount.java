package com.marc.decoditecalmacen.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.marc.decoditecalmacen.R;

public class DialogAmount {

    private final AmountCallback ac;
    Dialog dialog;

    public DialogAmount(Context context, AmountCallback callback) {
        ac = callback;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_amount);
        final EditText cant = dialog.findViewById(R.id.dialog_amount_input);
        final Button accept = dialog.findViewById(R.id.dialog_amount_accept);
        cant.setInputType(InputType.TYPE_CLASS_NUMBER);
        accept.setOnClickListener(v -> {
            int amount = Integer.parseInt(cant.getText().toString());
            ac.setAmount(amount);
        });

    }

    public void show() {
        this.dialog.show();
    }

    public void dissmiss(){
        this.dialog.dismiss();
    }

    public interface AmountCallback {
        void setAmount(int amount);
    }

}
