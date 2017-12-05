package com.example.arturo.guesswhofriends;

import java.io.Serializable;

/**
 * Created by arturo on 26/11/17.
 */

public class HighScore implements Serializable {
    private int id;
    private String date;
    private int score;

    public HighScore(int id, String date, int score) {
        this.id = id;
        this.date = date;
        this.score = score;
    }

    public HighScore(String date, int score) {
        this.date = date;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
