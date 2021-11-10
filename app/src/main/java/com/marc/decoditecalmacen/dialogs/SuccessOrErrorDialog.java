package com.marc.decoditecalmacen.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.marc.decoditecalmacen.R;

public class SuccessOrErrorDialog extends Dialog implements View.OnClickListener {

    private int setAnimation;
    View.OnClickListener listener;
    private String msg;

    public SuccessOrErrorDialog(Activity a, boolean succcesOrError, String msg) {
        super(a);
        this.setAnimation = succcesOrError ? R.raw.success_animation : R.raw.error_animation;
        this.msg = msg;
    }


    public SuccessOrErrorDialog(Activity a, boolean succcesOrError, View.OnClickListener listener) {
        super(a);
        this.listener = listener;
        this.setAnimation = succcesOrError ? R.raw.success_animation : R.raw.error_animation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_success_or_error);
        this.setCancelable(false);
        Button accept = findViewById(R.id.dialog_succes_accept);
        LottieAnimationView animationView = findViewById(R.id.dialog_success_animation);
        TextView tv = findViewById(R.id.dialog_succes_msg);
        tv.setText(msg);
        accept.setOnClickListener( ( listener == null) ? this : listener);
        animationView.setAnimation(setAnimation);
        animationView.playAnimation();
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
    }
}
