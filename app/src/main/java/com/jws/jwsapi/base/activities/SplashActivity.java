package com.jws.jwsapi.base.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jws.jwsapi.R;

public class SplashActivity extends AppCompatActivity {

    public String Version="FRM 1.02";
    Intent hidenav;
    ImageView imageView;
    TextView tvVersion;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getApplication().setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.activity_splashscreen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hidenav = new Intent("android.intent.action.HIDE_NAVIGATION_BAR");
        this.getApplicationContext().sendBroadcast(hidenav);
        imageView=findViewById(R.id.imageView);
        tvVersion=findViewById(R.id.tvVersion);
        tvVersion.setText(Version);
       Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);

       // Asignar la animación al TextView
       imageView.startAnimation(fadeIn);

       // Hacer que el TextView sea visible cuando la animación termine
       fadeIn.setAnimationListener(new Animation.AnimationListener() {
           @Override
           public void onAnimationStart(Animation animation) { }

           @Override
           public void onAnimationEnd(Animation animation) {
               imageView.setVisibility(View.VISIBLE);
           }

           @Override
           public void onAnimationRepeat(Animation animation) { }
       });

       Animation slide = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);

       // Asignar la animación al TextView
       tvVersion.startAnimation(slide);

       // Hacer que el TextView sea visible cuando la animación termine
       slide.setAnimationListener(new Animation.AnimationListener() {
           @Override
           public void onAnimationStart(Animation animation) { }

           @Override
           public void onAnimationEnd(Animation animation) {
               tvVersion.setVisibility(View.VISIBLE);
           }

           @Override
           public void onAnimationRepeat(Animation animation) { }
       });

       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               // on below line we are
               // creating a new intent
               Intent i = new Intent(SplashActivity.this, MainActivity.class);

               // on below line we are
               // starting a new activity.
               startActivity(i);

               // on the below line we are finishing
               // our current activity.
               finish();
           }
       }, 3000);


   }

}


