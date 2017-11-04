package com.example.arturo.guesswhofriends;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbDiscover;
import info.movito.themoviedbapi.model.Discover;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.people.Person;

public class MainActivity extends AppCompatActivity {

    private Button btnPlay, btnAchievments, btnConfig;
    private AccessTokenTracker tokenTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnAchievments = (Button) findViewById(R.id.btnAchievments);
        btnConfig = (Button) findViewById(R.id.btnConfig);

        updateToken(AccessToken.getCurrentAccessToken());

        setTokenTracker(new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                updateToken(currentAccessToken);
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameStart = new Intent(getApplicationContext(), LoadingActivity.class);
                startActivity(gameStart);
            }
        });

    }

    public void updateToken(AccessToken token){
        if(token == null){
            Intent LoginActivity = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(LoginActivity);
        }
    }

    public AccessTokenTracker getTokenTracker() {
        return tokenTracker;
    }

    public void setTokenTracker(AccessTokenTracker tokenTracker) {
        this.tokenTracker = tokenTracker;
    }

}
