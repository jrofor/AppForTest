package com.example.roman.appfortest;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
//import android.view.MenuInflater;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.SearchView;
//import android.widget.SearchView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.roman.appfortest.forNetworkGifs.ui.State;
import com.example.roman.appfortest.forNetworkGifs.ui.adapter.PhotosAdapter;
import com.example.roman.appfortest.forNetworkGifs.ui.QueryValidator;
import com.example.roman.appfortest.forNetworkGifs.ux.DefaultResponse;
import com.example.roman.appfortest.forNetworkGifs.ux.RestApi;
import com.example.roman.appfortest.forNetworkGifs.ux.dto.GifDTO;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkGifs extends AppCompatActivity {

    public static final String DEFAULT_SEARCH_REQUEST = "News";
    private Toolbar toolbar;
    private RecyclerView rvPhotos;
    private View viewLoading;
    private View viewNoData;
    private View viewError;
    private TextView tvError;
    private Button btnTryAgain;
    private PhotosAdapter photosAdapter;
    private SearchView searchView;
    private static final String TAG = "myLogs";

    @Nullable
    private Call<DefaultResponse<List<GifDTO>>> searchRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_gifs);
        setupUi();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupUx();

    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindUx();
    }

    private void unbindUx() {
        searchView.setOnQueryTextListener(null);
        btnTryAgain.setOnClickListener(null);
        //cancelCurrentRequestIfNeeded();
    }

    private void setupUi() {
        findView();
        toolbar.setTitle(R.string.toolbar_search_tittle);
        setupRecyclerViews();
        setupSearchView();
    }

    private void setupUx() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                validateAndLoadGifs(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //this callback if text change
                return false;
            }
        });
        btnTryAgain.setOnClickListener(v -> onClickTryAgain());
    }

    private void onClickTryAgain() {
        final String query = searchView.getQuery().toString();
        if (query.isEmpty()) {
            loadGifs(DEFAULT_SEARCH_REQUEST);
        } else {
            validateAndLoadGifs(query);
        }
    }

    private void validateAndLoadGifs(@Nullable String query) {
        if (QueryValidator.isValid(query)) {
            loadGifs(query);
        }
    }

    private void loadGifs(@NonNull String search) {
        showState(State.Loading);
        cancelCurrentRequestIfNeeded();

        searchRequest = RestApi.getInstance()
                .gifs()
                .search(search);

        searchRequest.enqueue(new Callback<DefaultResponse<List<GifDTO>>>() {
            @Override
            public void onResponse(@NonNull Call<DefaultResponse<List<GifDTO>>> call,
                                   @NonNull Response<DefaultResponse<List<GifDTO>>> response) {
                checkResponseAndShowState(response);
            }

            @Override
            public void onFailure(@NonNull Call<DefaultResponse<List<GifDTO>>> call,
                                  @NonNull Throwable t) {
                handleError(t);
            }
        });
    }

    private void checkResponseAndShowState(@NonNull Response<DefaultResponse<List<GifDTO>>> response) {
        //Here I use Guard Clauses. You can find more here:
        //https://refactoring.com/catalog/replaceNestedConditionalWithGuardClauses.html

        //Here we have 4 clauses:

        if (!response.isSuccessful()) {
            showState(State.ServerError);
            return;
        }

        final DefaultResponse<List<GifDTO>> body = response.body();

        if (body == null) {
            showState(State.HasNoData);
            return;
        }

        final List<GifDTO> data = body.getData();

        if (data == null) {
            showState(State.HasNoData);
            return;
        }

        if (data.isEmpty()) {
            showState(State.HasNoData);
            return;
        }
        Log.d (TAG, " before State.HasData");
        photosAdapter.replaceItems(data);
        showState(State.HasData);
        Log.d (TAG, "State.HasData");


    }

    private void handleError(Throwable throwable) {
        if (throwable instanceof IOException) {
            showState(State.NetworkError);
            return;
        }
        showState(State.ServerError);
    }


    private void cancelCurrentRequestIfNeeded() {
        if (searchRequest == null) {
            return;
        }

        //check if request already cancelled
        if (searchRequest.isCanceled()) {
            searchRequest.cancel();
            return;
        }

        //check if request executed OR already in queue
        if (searchRequest.isExecuted()) {
            searchRequest.cancel();
            searchRequest = null;
        }
    }


    public void showState(@NonNull State state) {
        switch (state) {
            case Loading:
                rvPhotos.setVisibility(View.GONE);
                viewError.setVisibility(View.GONE);
                viewNoData.setVisibility(View.GONE);

                viewLoading.setVisibility(View.VISIBLE);
                break;

            case HasData:
                viewError.setVisibility(View.GONE);
                viewLoading.setVisibility(View.GONE);
                viewNoData.setVisibility(View.GONE);

                rvPhotos.setVisibility(View.VISIBLE);
                break;

            case HasNoData:
                rvPhotos.setVisibility(View.GONE);
                viewLoading.setVisibility(View.GONE);

                viewError.setVisibility(View.VISIBLE);
                viewNoData.setVisibility(View.VISIBLE);
                break;
            case NetworkError:
                rvPhotos.setVisibility(View.GONE);
                viewLoading.setVisibility(View.GONE);
                viewNoData.setVisibility(View.GONE);

                tvError.setText(getText(R.string.error_network));
                viewError.setVisibility(View.VISIBLE);
                break;

            case ServerError:
                rvPhotos.setVisibility(View.GONE);
                viewLoading.setVisibility(View.GONE);
                viewNoData.setVisibility(View.GONE);

                tvError.setText(getText(R.string.error_server));
                viewError.setVisibility(View.VISIBLE);
                break;

            default:
                throw new IllegalArgumentException("Unknown state: " + state);
        }

    }




    private void setupSearchView() {
        toolbar.inflateMenu(R.menu.network_gif_menu_list_search);
        final MenuItem item = toolbar.getMenu().findItem(R.id.network_gif_menu_list_search);
        searchView = (SearchView) item.getActionView();
    }

    private void setupRecyclerViews() {
        photosAdapter = new PhotosAdapter(Glide.with(this));
        rvPhotos.setLayoutManager(new LinearLayoutManager(this));
        rvPhotos.setAdapter(photosAdapter);
    }


    private void findView() {

        rvPhotos = findViewById(R.id.rv_photos);
        viewLoading = findViewById(R.id.lt_loading);
        viewNoData = findViewById(R.id.lt_no_date);
        viewError = findViewById(R.id.lt_error);
        tvError = findViewById(R.id.tv_error);
        btnTryAgain = findViewById(R.id.btn_try_again);
        toolbar = findViewById(R.id.toolbar);
    }

    public static void startActivity (Activity activity) {
        Intent activityIntent = new Intent(activity, NetworkGifs.class);
        activity.startActivity(activityIntent);
    }






}
