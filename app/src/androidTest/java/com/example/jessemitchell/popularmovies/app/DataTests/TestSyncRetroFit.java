package com.example.jessemitchell.popularmovies.app.DataTests;

import android.test.AndroidTestCase;
import android.util.Log;

import com.example.jessemitchell.popularmovies.app.POJOs.StudentInterface;
import com.example.jessemitchell.popularmovies.app.POJOs.StudentTest;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jesse.mitchell on 1/16/2017.
 */

public class TestSyncRetroFit extends AndroidTestCase
{

    public void testRetroFit()
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://androidtutorialpoint.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        StudentInterface studentInter = retrofit.create(StudentInterface.class);

        String studentUrl = studentInter.getStudent().request().url().toString();

        Call<StudentTest> test = studentInter.getStudent();
        try {
            StudentTest body = test.execute().body();
        }
        catch (IOException e)
        {
            Log.v("", e.getMessage());
        }

        test.enqueue(new Callback<StudentTest>() {
            @Override
            public void onResponse(Call<StudentTest> call, Response<StudentTest> response) {

                StudentTest body = response.body();
                Log.v("Student Id:", body.getStudentId());
                body.getStudentId();
            }

            @Override
            public void onFailure(Call<StudentTest> call, Throwable t) {

            }
        });


    }
}
