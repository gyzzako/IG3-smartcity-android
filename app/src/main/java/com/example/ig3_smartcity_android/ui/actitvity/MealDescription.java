package com.example.ig3_smartcity_android.ui.actitvity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.ig3_smartcity_android.R;
import com.example.ig3_smartcity_android.dataAccess.configuration.RetrofitConfigurationService;
import com.example.ig3_smartcity_android.model.Meal;
import com.example.ig3_smartcity_android.ui.viewModel.CartViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class MealDescription extends AppCompatActivity {

    private TextView mealName,description, portion_number;
    private Button addToCart;
    private ImageView mealImage;
    private Intent intent;
    private Meal meal;
    private CartViewModel cartViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_description);

        //affiche l'option retour vers la liste(recycleView)
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        this.intent = getIntent();

        mealImage = (ImageView)findViewById(R.id.mealImageId);
        mealName = (TextView) findViewById(R.id.mealName);
        description =(TextView) findViewById(R.id.description);
        portion_number =(TextView) findViewById(R.id.portion_number_id);
        addToCart = (Button)  findViewById(R.id.addToCartID);

        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            meal = (Meal) bundle.get("currentMeal");
            setText(mealName,meal.getName());
            setText(description,meal.getDescription());
            setText(portion_number,String.valueOf(meal.getPortion_number()));
            Glide.with(getApplicationContext()).load(RetrofitConfigurationService.BASE_URL + "mealimages/" + meal.getImage()).into(mealImage);
        }


        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Meal> meals = MainActivity.getMealsForCart();
                if(meals != null){
                    int i = 0;
                    boolean tabAlreadyContains = false;

                    while(i<meals.size() && !tabAlreadyContains){
                        if(Objects.equals(meals.get(i).getId(), meal.getId())){
                            tabAlreadyContains = true;
                        }
                        i++;
                    }
                    if(!tabAlreadyContains){
                        MainActivity.addMealToCart(meal);
                        goToMainActivity();
                    }else{
                        Toast.makeText(getApplicationContext(), R.string.mealAlreadyInCart, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    MainActivity.addMealToCart(meal);
                    goToMainActivity();
                }
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

    public void setText(TextView view,String text){
        view.setText(text);
    }

    private void goToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}