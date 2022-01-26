package com.example.ig3_smartcity_android.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.ig3_smartcity_android.R;
import com.example.ig3_smartcity_android.dataAccess.dto.JwtTokenPayloadDTO;
import com.example.ig3_smartcity_android.model.JwtTokenPayload;
import com.example.ig3_smartcity_android.model.NetworkError;
import com.example.ig3_smartcity_android.services.mappers.TokenMapper;
import com.example.ig3_smartcity_android.ui.actitvity.LoginActivity;
import com.example.ig3_smartcity_android.ui.error.ApiError;
import com.example.ig3_smartcity_android.ui.viewModel.DonnationViewModel;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class DonnationFragment extends Fragment {
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private EditText nameMealText;
    private EditText descriptionText;
    private EditText categorieText;
    private EditText nbPortionText;
    private Button takePictureButton;
    private Button addMealButton;
    private ImageView imageView;
    private DonnationViewModel donnationViewModel;

    private SharedPreferences sharedPreferences;
    private String token;

    public DonnationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        donnationViewModel = new ViewModelProvider(this).get(DonnationViewModel.class);
        View root  = inflater.inflate(R.layout.fragment_donnation,container,false);
        nameMealText = root.findViewById(R.id.nom);
        descriptionText = root.findViewById(R.id.descriptionID);
        categorieText = root.findViewById(R.id.categorie);
        nbPortionText = root.findViewById(R.id.nbPortionsID);
        imageView = root.findViewById(R.id.image_view_id);
        addMealButton = root.findViewById(R.id.donnationID);
        takePictureButton = root.findViewById(R.id.takePictureId);

        sharedPreferences = requireActivity().getSharedPreferences(getString(R.string.sharedPref),Context.MODE_PRIVATE);
        token = sharedPreferences.getString(getString(R.string.token),null);

        donnationViewModel.getError().observe(getViewLifecycleOwner(), error ->{
            ApiError.showError(error, getContext());
            if(error == NetworkError.NO_ERROR_DETECTED){
                goToMealsList();
            }
        });

        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{
                    Manifest.permission.CAMERA
            },REQUEST_IMAGE_CAPTURE);
        }

        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getActivity();
                PackageManager packageManager = context == null ? null : context.getPackageManager();
                if (packageManager == null || !packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                    Toast.makeText(getContext(),getResources().getText(R.string.camera_error),Toast.LENGTH_SHORT).show();
                } else {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE); //deprecated mais comme dans l'app example
                }
            }
        });

        addMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean areAllFiledsChecked = isFormValid();
                if(areAllFiledsChecked){
                    addMeal();
                }
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){//deprecated mais comme dans l'app example
        if(requestCode == REQUEST_IMAGE_CAPTURE ){
            if(data!=null){
                Bundle bundle = data.getExtras();
                Bitmap imageCaptured = (Bitmap) bundle.get("data");
                imageView.setImageBitmap(imageCaptured);
            }
        }
    }


    private boolean isFormValid(){
        if(nameMealText.getText().length() == 0){
            nameMealText.setError(getResources().getText(R.string.meal_name_error));
            return false;
        }
        if(descriptionText.getText().length() == 0){
            descriptionText.setError(getResources().getText(R.string.meal_description_error));
            return false;
        }
        try{
            Integer.parseInt(nbPortionText.getText().toString());
            if(nbPortionText.getText().length() == 0){
                nbPortionText.setError(getResources().getText(R.string.nbPortions_error));
                return false;
            }
        }catch (Exception e){
            nbPortionText.setError(getResources().getText(R.string.nbPortions_error));
            return false;
        }

        try{
            Integer.parseInt(categorieText.getText().toString());
            if(categorieText.getText().length() == 0){
                categorieText.setError(getResources().getText(R.string.meal_categorie_error));
                return false;
            }
        }catch (Exception e){
            categorieText.setError(getResources().getText(R.string.meal_categorie_error));
            return false;
        }

        return true;
    }

    public void addMeal(){
        if(token != null){
            DecodedJWT decodedJWT = JWT.decode(token);
            Claim jwtPayload = decodedJWT.getClaim("value");
            JwtTokenPayloadDTO JwtTokenPayloadDTO = jwtPayload.as(JwtTokenPayloadDTO.class);
            JwtTokenPayload jwtTokenPayload = TokenMapper.INSTANCE.mapToJwtTokenPayload(JwtTokenPayloadDTO);

            try{
                //meal data
                String name = nameMealText.getText().toString();
                String description = descriptionText.getText().toString();
                Integer portionNumber = Integer.parseInt(nbPortionText.getText().toString());
                Integer userId = jwtTokenPayload.getId();
                Integer categorieId = Integer.parseInt(categorieText.getText().toString());
                byte[] imageInByte = null;
                if(imageView.getDrawable() != null){
                    Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    imageInByte = baos.toByteArray();
                }

                //FormData
                MultipartBody.Part namePart = MultipartBody.Part.createFormData("name", null, RequestBody.create(MediaType.parse("text/plain"), name));
                MultipartBody.Part descriptionPart = MultipartBody.Part.createFormData("description", null, RequestBody.create(MediaType.parse("text/plain"), description));
                MultipartBody.Part portionNumberPart = MultipartBody.Part.createFormData("portion_number", null, RequestBody.create(MediaType.parse("text/plain"), String.valueOf(portionNumber)));
                MultipartBody.Part userFkPart = MultipartBody.Part.createFormData("user_fk", null, RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userId)));
                MultipartBody.Part categoryFkPart = MultipartBody.Part.createFormData("category_fk", null, RequestBody.create(MediaType.parse("text/plain"), String.valueOf(categorieId)));
                MultipartBody.Part filePart = null;
                if(imageInByte != null){
                    filePart = MultipartBody.Part.createFormData("image", "image", RequestBody.create(MediaType.parse("image/*"), imageInByte));
                }
                if(filePart != null){
                    donnationViewModel.addNewMeal(namePart,descriptionPart,portionNumberPart,userFkPart,categoryFkPart, filePart, token);
                }else{
                    Toast.makeText(getContext(),R.string.imageRequired,Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                Toast.makeText(getContext(),R.string.enterNumber,Toast.LENGTH_SHORT).show();
            }
        }else{
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

    private void goToMealsList(){
        try {
            TimeUnit.MILLISECONDS.sleep(20); //pour que ca ait le temps de charger le plat qu'on vient d'ajouter
        } catch (InterruptedException ignored) {
        }
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MealRecycleViewFragment fragment = new MealRecycleViewFragment();

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }
}