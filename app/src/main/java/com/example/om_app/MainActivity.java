package com.example.om_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static final String BASE_URL = "https://raw.githubusercontent.com/so-asb93/OM_App/master/app/src/main/java/com/example/om_app/";
    private SharedPreferences sharedPreferences;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("application_esiea", Context.MODE_PRIVATE);
        gson = new GsonBuilder()
                .setLenient()
                .create();

        List<Joueurs> pokemonList = getDataFromCache();

        if(pokemonList != null){
            showList(pokemonList);
        } else {
            makeAPIcall();
        }
    }

    private List<Joueurs> getDataFromCache() {
        String jsonPokemon = sharedPreferences.getString(Constants.KEY_JOUEURS_LIST, null);

        if(jsonPokemon == null){
            return null;
        } else {
            Type listType = new TypeToken<List<Joueurs>>(){}.getType();
            return gson.fromJson(jsonPokemon, listType);
        }

    }

    private void showList(List<Joueurs> pokemonList) {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        final SwipeRefreshLayout swiperefresh = findViewById(R.id.swiperefresh);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swiperefresh.setRefreshing(false);
            }
        });
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new ListAdapter(pokemonList);
        recyclerView.setAdapter(mAdapter);
    }


    private void makeAPIcall() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        OM_API om_api = retrofit.create(OM_API.class);

        Call<RestJoueursResponse> call = om_api.getJoueursResponse();
        call.enqueue(new Callback<RestJoueursResponse>() {
            @Override
            public void onResponse(Call<RestJoueursResponse> call, Response<RestJoueursResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    List<Joueurs> JoueursList = response.body().getResults();
                    Toast.makeText(getApplicationContext(), "API SUCCESS", Toast.LENGTH_SHORT).show();
                    saveList(JoueursList);
                    showList(JoueursList);
                } else {
                    showError();
                }
            }

            @Override
            public void onFailure(Call<RestJoueursResponse> call, Throwable t) {
                showError();
            }
        });
    }

    private void saveList(List<Joueurs> pokemonList) {
        String jsonString = gson.toJson(pokemonList);

        sharedPreferences
                .edit()
                .putString(Constants.KEY_JOUEURS_LIST, jsonString)
                .apply();

        Toast.makeText(getApplicationContext(), "List Saved", Toast.LENGTH_SHORT).show();

    }

    private void showError() {
        Toast.makeText(getApplicationContext(), "API ERROR", Toast.LENGTH_SHORT).show();
    }
}

