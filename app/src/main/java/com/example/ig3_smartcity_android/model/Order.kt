package com.example.ig3_smartcity_android.model

import java.util.*

data class Order(val order_date:Date?,
                 val user: User,
                 val meals: List<Meal>){

}
