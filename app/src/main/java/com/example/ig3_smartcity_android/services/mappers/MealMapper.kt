package com.example.ig3_smartcity_android.services.mappers

import com.example.ig3_smartcity_android.model.Meal
import com.example.ig3_smartcity_android.dataAccess.dto.MealDTO
import java.util.*

object MealMapper {
    fun mapToMeal(mealdto: List<MealDTO>?): ArrayList<Meal>? {
        if (mealdto == null) {
            return  null
        } else {
            val meals = ArrayList<Meal>()
            for ((id, name, description,image,portion_number) in mealdto) {
                val meal =
                    Meal(id, name, description,image,portion_number)
                meals.add(meal)
            }
            return meals
        }
    }
}