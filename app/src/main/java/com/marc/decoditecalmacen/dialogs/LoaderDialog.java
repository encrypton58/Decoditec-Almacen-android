package com.marc.decoditecalmacen.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.marc.decoditecalmacen.R;

public class LoaderDialog extends Dialog {

    public LoaderDialog(Activity a) {
        super(a);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_loader);
        this.setCancelable(false);
        LottieAnimationView animationView = findViewById(R.id.dialog_loader_animation_view);
        animationView.setAnimation(R.raw.loader_animation);
        animationView.setRepeatCount(100);
        animationView.setRepeatMode(LottieDrawable.REVERSE);
        animationView.setRepeatMode(LottieDrawable.RESTART);
        animationView.playAnimation();

    }
}
