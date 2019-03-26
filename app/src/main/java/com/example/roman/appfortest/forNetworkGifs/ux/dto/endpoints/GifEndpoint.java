package com.example.roman.appfortest.forNetworkGifs.ux.dto.endpoints;

import android.support.annotation.NonNull;

import com.example.roman.appfortest.forNetworkGifs.ux.DefaultResponse;
import com.example.roman.appfortest.forNetworkGifs.ux.dto.GifDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GifEndpoint {
    @NonNull
    @GET("gifs/search")
    Call<DefaultResponse<List<GifDTO>>> search(@Query("q") @NonNull String search);


}
