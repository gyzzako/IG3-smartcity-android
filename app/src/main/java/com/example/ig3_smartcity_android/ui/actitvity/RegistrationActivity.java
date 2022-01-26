package com.example.ig3_smartcity_android.ui.actitvity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ig3_smartcity_android.R;
import com.example.ig3_smartcity_android.model.NetworkError;
import com.example.ig3_smartcity_android.model.User;
import com.example.ig3_smartcity_android.ui.error.ApiError;
import com.example.ig3_smartcity_android.ui.viewModel.RegistrationViewModel;

import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {

    private EditText firstnameEditText,
            nameEditText,
            pseudoEditText,
            passwordEditText,
            provinceEditText,
            cityEditText,
            addressEditText,
            telephoneText;

    private Button buttonRegister;
    private RegistrationViewModel registrationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); //pour afficher le bouton retour vers l'activité login.
        getSupportActionBar().setTitle(R.string.inscription);
        registrationViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);

        firstnameEditText = findViewById(R.id.prenom);
        nameEditText = findViewById(R.id.nom);
        pseudoEditText = findViewById(R.id.pseudo);
        passwordEditText = findViewById(R.id.password);
        provinceEditText = findViewById(R.id.province);
        cityEditText = findViewById(R.id.city);
        addressEditText = findViewById(R.id.adresse);
        buttonRegister = findViewById(R.id.registrationBtnID);
        telephoneText = findViewById(R.id.telephone);

        //l'utilisateur n'est enregistré que si tous les champs du formulaire sont remplis.
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean areAllFieldsChecked = isFormValid();
                if(areAllFieldsChecked){
                    registerUser();
                    resetFormAfterRegister();
                }
            }
        });

        registrationViewModel.getError().observe(this,error -> {
            if(error == NetworkError.NO_ERROR_DETECTED){
                Toast.makeText(this,R.string.registration_message,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }else{
                ApiError.showError(error, this);
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu){
        return true;
    }


    public boolean isFormValid(){
        if(firstnameEditText.length()==0){
            firstnameEditText.setError(getResources().getText(R.string.firstname_empty_error));
            return false;
        }
        if(nameEditText.length()==0){
            nameEditText.setError(getResources().getText(R.string.lastname_empty_error));
            return false;
        }
        if(pseudoEditText.length()==0){
            pseudoEditText.setError(getResources().getText(R.string.username_empty_message));
            return true;
        }
        if(passwordEditText.length()==0){
            passwordEditText.setError(getResources().getText(R.string.password_empty_message));
            return false;
        }
        if(provinceEditText.length() == 0){
            provinceEditText.setError(getResources().getText(R.string.province_empty_error));
            return false;
        }
        if(cityEditText.length()==0){
            cityEditText.setError(getResources().getText(R.string.city_empty_error));
            return false;
        }
        if(addressEditText.length()==0){
            addressEditText.setError(getResources().getText(R.string.address_empty_error));
            return false;
        }

        try{
            Integer.parseInt(telephoneText.getText().toString());
            if(telephoneText.length() == 0){
                telephoneText.setError(getResources().getText(R.string.phone_error));
                return false;
            }
        }catch (Exception e){
            telephoneText.setError(getResources().getText(R.string.phone_character));
            return false;
        }

        return true;
    }

    /**
     * this method allows to reset the form after a new user has been registered.
     */
    public void resetFormAfterRegister(){
        firstnameEditText.getText().clear();
        nameEditText.getText().clear();
        pseudoEditText.getText().clear();
        passwordEditText.getText().clear();
        provinceEditText.getText().clear();
        cityEditText.getText().clear();
        cityEditText.getText().clear();
        telephoneText.getText().clear();
        addressEditText.getText().clear();
    }

    /**
     * this method allows to register a new user to the database.
     */
    public void registerUser(){
        User user = new User(null,
                firstnameEditText.getText().toString(),
                nameEditText.getText().toString(),
                telephoneText.getText().toString(),
                pseudoEditText.getText().toString(),
                passwordEditText.getText().toString(),
                provinceEditText.getText().toString(),
                cityEditText.getText().toString(),
                addressEditText.getText().toString()

        );
        registrationViewModel.registerUser(user);
    }

}