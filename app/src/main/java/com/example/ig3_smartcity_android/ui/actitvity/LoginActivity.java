package com.example.ig3_smartcity_android.ui.actitvity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ig3_smartcity_android.R;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.connexion);
    }
}