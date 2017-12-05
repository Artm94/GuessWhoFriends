package com.example.arturo.guesswhofriends;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HighScoresActivity extends AppCompatActivity {

    private ListView scoreList;
    private ArrayAdapter<HighScore> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        adapter = new HighScoresAdapter(getApplicationContext(), databaseHelper.getHighScores(10));
        scoreList = (ListView) findViewById(R.id.scoreList);
        scoreList.setAdapter(adapter);
    }
}
