package com.majateam.mewzik.network;

import com.majateam.mewzik.config.MewzikConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Generic Retrofit Service generator
 * Created by Nicolas Martino on 20/08/2016.
 */
public class ServiceGenerator {
    public static final String API_BASE_URL = MewzikConfig.SOUNDCLOUD_BASE_URL;

    private static Retrofit mRetrofit;

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static Retrofit retrofit(){
        return mRetrofit;
    }

    public static <S> S createService(Class<S> serviceClass) {
        OkHttpClient httpClient;
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient = httpClientBuilder
                .addInterceptor(logging)
                .build();
        mRetrofit = builder.client(httpClient).build();
        return mRetrofit.create(serviceClass);
    }

}