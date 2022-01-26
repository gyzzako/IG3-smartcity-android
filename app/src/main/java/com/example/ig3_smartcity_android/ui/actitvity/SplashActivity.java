package com.example.ig3_smartcity_android.ui.actitvity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.ig3_smartcity_android.R;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIMEOUT = 2000; //millisecondes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); //tout l'écran
        setContentView(R.layout.activity_splash);

        Animation fadeOut = new AlphaAnimation(1,0);
        fadeOut.setStartOffset(500);
        fadeOut.setDuration(1800);
        ImageView imageView = findViewById(R.id.imageSplash);
        imageView.setAnimation(fadeOut);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //JWT
                try{
                    SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedPref), Context.MODE_PRIVATE);
                    String token = sharedPreferences.getString(getString(R.string.token),null);
                    DecodedJWT decodedJWT = JWT.decode(token);
                    Claim jwtExp = decodedJWT.getClaim("exp");
                    long jwtExpirationTime = Long.parseLong(jwtExp.as(String.class));

                    Intent intent;
                    if(System.currentTimeMillis() < jwtExpirationTime*1000){
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                    }else{
                        intent = new Intent(getApplicationContext(), LoginActivity.class);
                    }
                    startActivity(intent);
                    finish();

                }catch (Exception e){
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        },SPLASH_SCREEN_TIMEOUT);
    }
}