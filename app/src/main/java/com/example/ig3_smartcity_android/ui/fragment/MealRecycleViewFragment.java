package com.example.ig3_smartcity_android.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ig3_smartcity_android.R;
import com.example.ig3_smartcity_android.dataAccess.configuration.RetrofitConfigurationService;
import com.example.ig3_smartcity_android.model.Meal;
import com.example.ig3_smartcity_android.ui.actitvity.LoginActivity;
import com.example.ig3_smartcity_android.ui.actitvity.MealDescription;
import com.example.ig3_smartcity_android.ui.error.ApiError;
import com.example.ig3_smartcity_android.ui.viewModel.MealViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class MealRecycleViewFragment extends Fragment {

    private static Fragment fragment;
    private MealAdapter mealAdapter;
    private MealViewModel mealViewModel;
    private SharedPreferences sharedPreferences;


    public MealRecycleViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_meal_recycle_view,container,false);
        RecyclerView mealRecycleView = root.findViewById(R.id.mealRecyclerView);

        //récupère la valeur du token dans le sharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences(getString(R.string.sharedPref),Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(getString(R.string.token),null);

        mealRecycleView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mealViewModel = new ViewModelProvider(this).get(MealViewModel.class);

        if(token != null){
            mealViewModel.getAllMeals(token);

        }else{
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }
        mealAdapter = new MealAdapter(getActivity());
        mealViewModel.getMeal().observe(getViewLifecycleOwner(),mealAdapter::setMeals);
        mealViewModel.getError().observe(getViewLifecycleOwner(), error ->{
            ApiError.showError(error, getContext());
        });

        mealRecycleView.setAdapter(mealAdapter);

        fragment = this;
        return root;
    }

    // interface qui fournit la méthode(void) de récuperation de l'indice de l'élément de la liste cliqué.
    private interface OnItemSelectedListener{
        void onItemSelected(int position);
    }

    //View  Holder.
    private static class MealViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView mealName;
        TextView description;
        TextView portion_number;

        public MealViewHolder(@NonNull View itemView, OnItemSelectedListener listener) {
            super(itemView);
            image = itemView.findViewById(R.id.mealImageId);
            mealName = itemView.findViewById(R.id.mealNameID);
            description = itemView.findViewById(R.id.mealDescription);
            portion_number = itemView.findViewById(R.id.portionNumber);

            itemView.setOnClickListener(e->{
                int currentMealPosition = getAbsoluteAdapterPosition();
                listener.onItemSelected(currentMealPosition);
            });
        }
    }


    //Adapter.
    private static class MealAdapter extends RecyclerView.Adapter<MealViewHolder>{

        private List<Meal> meals;
        private Context context;

        public MealAdapter(Context context){
            this.context = context;
        }

        @NonNull
        @Override
        public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_item_layout,parent,false);
            return new MealViewHolder(view, position->{
                Meal touchedMeal = meals.get(position);
                Intent intent = new Intent(fragment.getActivity(), MealDescription.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("currentMeal", touchedMeal);
                intent.putExtras(bundle);
                fragment.requireActivity().startActivity(intent);
            });
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
            Meal meal = meals.get(position);
            String name  = meal.getName();
            String description = meal.getDescription();
            String mealImage = meal.getImage();
            Uri mealUri = Uri.parse(mealImage);
            Integer portionNumber = meal.getPortion_number();

            holder.mealName.setText(name);
            holder.description.setText(description);
            holder.portion_number.setText(context.getResources().getString(R.string.quantity) + " " + portionNumber.toString());


            Glide.with(fragment)
                    .load(RetrofitConfigurationService.BASE_URL + "mealimages/" + mealUri)
                    .placeholder(R.drawable.bicky)// -> image par defaut si jamais les images de l'api ne veulent pas charger.
                    .into(holder.image);
        }

        @Override
        public int getItemCount() {
            return meals == null ? 0: meals.size();
        }

        public void setMeals(List<Meal> meals){
            this.meals = meals;
            notifyDataSetChanged();
        }
    }
}