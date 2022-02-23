package com.wojbeg.boredapp.di;


import static com.wojbeg.boredapp.utils.Constants.BASE_URL;

import com.wojbeg.boredapp.di.scopes.AppScope;
import com.wojbeg.boredapp.repo.remote.BoredApi;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiModule {

    @AppScope
    @Provides
    static BoredApi boredApi(Retrofit retrofit){
        return retrofit.create(BoredApi.class);
    }

    @AppScope
    @Provides
    static Retrofit retrofit(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


}
