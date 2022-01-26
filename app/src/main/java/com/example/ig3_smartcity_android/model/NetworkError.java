package com.example.ig3_smartcity_android.model;

import com.example.ig3_smartcity_android.R;

public enum NetworkError {
    NO_CONNECTION_ERROR(R.string.connection_error),
    REQUEST_ERROR(R.string.request_error),
    TECHNICAL_ERROR(R.string.technical_error),
    NO_ERROR_DETECTED(R.string.no_error_detected),
    USER_ALREADY_EXIST(R.string.user_exists),
    BAD_CREDENTIALS_ERROR(R.string.credentials_problem),
    MEAL_ALREADY_CLAIMED(R.string.meal_already_claimed);

    private int errorMessage;

    NetworkError(int errorMessage){
        this.errorMessage = errorMessage;
    }

    public int getErrorMessage(){
        return this.errorMessage;
    }
}
