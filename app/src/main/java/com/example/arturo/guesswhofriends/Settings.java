package com.example.arturo.guesswhofriends;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by arturo on 21/11/17.
 */

public class Settings {
    public static final String GAME_DIFFICULTY = "game_difficulty";
    public static final String QUESTIONS_QUANTITY = "questions_quantity";
    public static final String ANIMATIONS_STATUS = "animations_status";
    public static final int NOVEL_DIFFICULTY = 0;
    public static final int ENTHUSIAST_DIFFICULTY = 1;
    public static final int PROFESSIONAL_DIFFICULTY = 2;
    public static final int EXPERT_DIFFICULTY = 3;
    public static final int FIVE_QUESTIONS = 0;
    public static final int TEN_QUESTIONS = 1;
    public static final int FIFTEEN_QUESTIONS = 2;
    public static final int TWENTY_QUESTIONS = 3;
    private final int defaultDifficulty = 1;
    private final int defaultQuantity = 1;
    private final boolean defaultAnimationsStatus = true;
    private final String preferencesId = "app_settings";
    private int gameDifficulty;
    private int questionsQuantity;
    private boolean animationsStatus;
    private Context appContext;
    private SharedPreferences preferences;

    public Settings(Context context){
        this.appContext = context;
        preferences = appContext.getSharedPreferences(preferencesId, Context.MODE_PRIVATE);
        gameDifficulty = preferences.getInt(GAME_DIFFICULTY, defaultDifficulty);
        questionsQuantity = preferences.getInt(QUESTIONS_QUANTITY, defaultQuantity);
        animationsStatus = preferences.getBoolean(ANIMATIONS_STATUS, defaultAnimationsStatus);
    }

    public void setGameDifficulty(int gameDifficulty){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(GAME_DIFFICULTY, gameDifficulty);
        if(editor.commit()){
            this.gameDifficulty = gameDifficulty;
        }
    }

    public void setQuestionsQuantity(int questionsQuantity){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(QUESTIONS_QUANTITY, questionsQuantity);
        if(editor.commit()){
            this.questionsQuantity = questionsQuantity;
        }
    }

    public void setAnimationsStatus(boolean animationsStatus){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(ANIMATIONS_STATUS, animationsStatus);
        if(editor.commit()){
            this.animationsStatus = animationsStatus;
        }
    }

    public int getGameDifficulty() {
        return gameDifficulty;
    }

    public int getQuestionsQuantity() {
        return questionsQuantity;
    }

    public boolean getAnimationsStatus() {
        return animationsStatus;
    }
}
