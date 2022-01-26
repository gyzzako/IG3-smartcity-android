package com.example.ig3_smartcity_android.dataAccess.configuration;

import  android.content.Context;

import com.example.ig3_smartcity_android.dataAccess.webservice.ApiWebServices;
import com.example.ig3_smartcity_android.utils.ConnectivityCheckInterceptor;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class RetrofitConfigurationService {

    public static final String BASE_URL ="http://10.0.2.2:3001/";

    private Retrofit retrofitClient;
    private static ApiWebServices apiWebServices = null;


    private RetrofitConfigurationService(Context context){
        inittializeRetrofit(context);
    }

    private void inittializeRetrofit(Context context){
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityCheckInterceptor(context))
                .build();

        Moshi moshiConverter = new Moshi.Builder()
                .add(new KotlinJsonAdapterFactory())
                .build();

        this.retrofitClient = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshiConverter).asLenient())
                .baseUrl(BASE_URL)
                .build();
    }

    public static RetrofitConfigurationService getInstance(Context context){
        return new RetrofitConfigurationService(context);
    }


    public ApiWebServices apiWebServices(){
        if(apiWebServices == null){
            apiWebServices = retrofitClient.create((ApiWebServices.class));
        }
        return  apiWebServices;
    }
}
