package com.example.ig3_smartcity_android.utils.errors;

import java.io.IOException;

public class NoConnectivityException extends IOException {

    @Override
    public String getMessage(){
        return "Pas de connexion internet";
    }
}
