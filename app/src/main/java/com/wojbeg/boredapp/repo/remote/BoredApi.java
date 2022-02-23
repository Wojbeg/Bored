package com.wojbeg.boredapp.repo.remote;

import static com.wojbeg.boredapp.utils.Constants.ACTIVITY;

import com.wojbeg.boredapp.model.Idea;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BoredApi {

    @GET(ACTIVITY)
    Observable<Idea> getIdea();

    @GET(ACTIVITY)
    Observable<Idea> getCustomIdea(
            @Query("type") String type,
            @Query("participants") int participants,
            @Query("minaccessibility") double accessibilityMin,
            @Query("maxaccessibility") double accessibilityMax,
            @Query("minprice") double priceMin,
            @Query("maxprice") double priceMax
    );

    //String type, int participants, double accessibilityMin, double accessibilityMax,  double priceMin, double priceMax

    //GET("CoTutajMaByć/{key}")
    //Flowable<Idea> getIdeaByKey(
    // @Path("key") int key
    // );

}
