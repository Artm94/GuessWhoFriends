package com.example.arturo.guesswhofriends;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.share.ShareBuilder;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.widget.ShareDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ScoreActivity extends AppCompatActivity {

    private ImageView imgHighScore;
    private TextView userScore, txtHighScore;
    private Button playAgain, compartir;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        final ShareDialog shareDialog = new ShareDialog(this);

        userScore = (TextView) findViewById(R.id.textScore);
        txtHighScore = (TextView) findViewById(R.id.txtHighScore);
        playAgain = (Button) findViewById(R.id.btnPlayAgain);
        compartir = (Button) findViewById(R.id.btnCompartir);
        imgHighScore = (ImageView) findViewById(R.id.imgHighScore);
        databaseHelper = new DatabaseHelper(getApplicationContext());

        final int score = getIntent().getExtras().getInt("score");
        userScore.setText(String.valueOf(score));

        int scorePosition = getScorePosition(score);
        if(scorePosition > -1){
            if(scorePosition == 0){
                imgHighScore.setImageResource(R.drawable.gold_medal);
            }else if (scorePosition == 1){
                imgHighScore.setImageResource(R.drawable.second);
            }else if(scorePosition == 2){
                imgHighScore.setImageResource(R.drawable.third);
            }else{
                imgHighScore.setImageResource(R.drawable.medal);
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date scoreDate = Calendar.getInstance().getTime();
            if(databaseHelper.insertHighScore(new HighScore(dateFormat.format(scoreDate), score))){
                imgHighScore.setVisibility(View.VISIBLE);
                txtHighScore.setVisibility(View.VISIBLE);
            }
        }

        compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareAppLinkViaFacebook("http://theitalianrestaurant.esy.es/tmdbt/score.php?score=" + score);
                /*Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Share Something");
                startActivity(Intent.createChooser(intent, "Share with"));*/
                /*// Create an object
                ShareOpenGraphObject object = new ShareOpenGraphObject.Builder()
                        .putString("og:type", "books.book")
                        .putString("og:title", "The Movie DB Trivia")
                        .putString("og:description", "Tu puntaje obtenido es: "+score)
                        .putString("books:isbn", "0-553-57340-3")
                        .build();
                // Create an action
                ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
                        .setActionType("books.reads")
                        .putObject("book", object)
                        .build();
                // Create the content
                ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
                        .setPreviewPropertyName("book")
                        .setAction(action)
                        .build();
                shareDialog.show(content);*/
            }
        });

        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent game = new Intent(getApplicationContext(), LoadingActivity.class);
                startActivity(game);
                finish();
            }
        });

    }

    private int getScorePosition(int score){
        ArrayList<HighScore> highScores = databaseHelper.getHighScores(10);
        int place = -1;
        for(HighScore highScore: highScores){
            place++;
            if(score == highScore.getScore()){
                return -1;
            }else if(score > highScore.getScore()){
                return place;
            }
        }
        return highScores.size() < 10 ? ++place : -1;
    }

    private void shareAppLinkViaFacebook(String urlToShare) {
        try {
            Intent intent1 = new Intent();
            intent1.setPackage("com.facebook.katana");
            intent1.setAction("android.intent.action.SEND");
            intent1.setType("text/plain");
            intent1.putExtra("android.intent.extra.TEXT", urlToShare);
            startActivity(intent1);
        } catch (Exception e) {
            // If we failed (not native FB app installed), try share through SEND
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + urlToShare;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
            startActivity(intent);
        }
    }
}
