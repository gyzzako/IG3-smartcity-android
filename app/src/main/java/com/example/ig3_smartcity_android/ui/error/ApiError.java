package com.example.ig3_smartcity_android.ui.error;

import android.content.Context;
import android.widget.Toast;

import com.example.ig3_smartcity_android.R;
import com.example.ig3_smartcity_android.model.NetworkError;

public class ApiError {
    public static void showError(NetworkError error, Context context) {
        switch (error) {
            case TECHNICAL_ERROR:
                Toast.makeText(context, R.string.technical_error, Toast.LENGTH_SHORT).show();
                break;
            case NO_CONNECTION_ERROR:
                Toast.makeText(context, R.string.connection_error, Toast.LENGTH_SHORT).show();
                break;
            case REQUEST_ERROR:
                Toast.makeText(context, R.string.request_error, Toast.LENGTH_SHORT).show();
                break;
            case USER_ALREADY_EXIST:
                Toast.makeText(context, R.string.user_exists, Toast.LENGTH_SHORT).show();
                break;
            case BAD_CREDENTIALS_ERROR:
                Toast.makeText(context, R.string.credentials_problem, Toast.LENGTH_SHORT).show();
                break;
            case MEAL_ALREADY_CLAIMED:
                Toast.makeText(context, R.string.meal_already_claimed, Toast.LENGTH_SHORT).show();
        }
    }
}
