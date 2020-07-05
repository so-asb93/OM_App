package com.example.om_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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

    public Button switchToOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //switchToOther = (Button) findViewById(R.id.goToSecond);

        sharedPreferences = getSharedPreferences("OM_App", Context.MODE_PRIVATE);
        gson = new GsonBuilder()
                .setLenient()
                .create();


        makeAPIcall();

        //switchToOther.setOnClickListener(new View.OnClickListener() {
           // @Override
           // public void onClick(View v) {
                //Intent otherActivity = new Intent(getApplicationContext(),TeamBuilder.class);
              //  startActivity(otherActivity);
               // finish();
           // }
       // });

    }

    private List<Joueurs> getDataFromCache() {
        String jsonJoueurs = sharedPreferences.getString(Constants.KEY_JOUEURS_LIST, null);

        Toast.makeText(getApplicationContext(), "Data from cache", Toast.LENGTH_SHORT).show();

        if(jsonJoueurs == null){
            return null;
        } else {
            Type listType = new TypeToken<List<Joueurs>>(){}.getType();
            return gson.fromJson(jsonJoueurs, listType);
        }

    }

    private void showList(List<Joueurs> listJoueurs) {
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

        mAdapter = new ListAdapter(listJoueurs, MainActivity.this);
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
                    List<Joueurs> listJoueurs = response.body().getResults();
                    Toast.makeText(getApplicationContext(), "API SUCCESS", Toast.LENGTH_SHORT).show();
                    saveList(listJoueurs);
                    showList(listJoueurs);
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

    private void saveList(List<Joueurs> listJoueurs) {
        String jsonString = gson.toJson(listJoueurs);

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