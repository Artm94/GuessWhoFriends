package com.example.arturo.guesswhofriends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by arturo on 26/11/17.
 */

public class HighScoresAdapter extends ArrayAdapter<HighScore> {
    private Context context;
    private ArrayList<HighScore> highScoreArrayList;


    public HighScoresAdapter(Context context, ArrayList data) {
        super(context, R.layout.score_item, data);
        this.context = context;
        this.highScoreArrayList = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.score_item, null);
        ImageView image = (ImageView) item.findViewById(R.id.imgScore);
        if(position <= 2){
            if(position == 0){
                image.setImageResource(R.drawable.gold_medal);
            }else if(position == 1){
                image.setImageResource(R.drawable.second);
            }else if(position == 2){
                image.setImageResource(R.drawable.third);
            }
        }else{
            image.setImageResource(R.drawable.medal);
        }
        TextView date = (TextView) item.findViewById(R.id.txtDate);
        date.setText(highScoreArrayList.get(position).getDate());
        TextView score = (TextView) item.findViewById(R.id.txtScore);
        score.setText(String.valueOf(highScoreArrayList.get(position).getScore()));
        return item;
    }
}
