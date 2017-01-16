package com.example.jessemitchell.popularmovies.app.POJOs;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by jesse.mitchell on 1/16/2017.
 */

public interface StudentInterface
{
    @GET("api/RetrofitAndroidObjectResponse")
    Call<StudentTest> getStudent();
}
