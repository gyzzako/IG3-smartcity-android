package com.example.ig3_smartcity_android.model

import java.io.Serializable

class Meal(val id:Int?, val name:String,
           val description:String, val image:String, val portion_number:Int): Serializable {
}