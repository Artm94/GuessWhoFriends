package com.example.arturo.guesswhofriends;

import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.people.PersonCast;

/**
 * Created by arturo on 22/10/17.
 */

//This class creates a question builder from a list of movies
public class QuestionBuilder {

    //Variable declaration
    private List<MovieDb> movieDbList;
    public static final int RELEASE_YEAR = 0;
    public static final int OVERVIEW = 1;
    public static final int CAST = 2;
    public static final int ORIGINAL_TITLE = 3;
    public static final int MOVIE_CHARACTER = 4;
    public static final int COMPANY_NAME = 5;

    public QuestionBuilder(List<MovieDb> movieDbList){
        this.movieDbList = movieDbList;
    }

    public List<MovieDb> getMovieDbList() {
        return movieDbList;
    }

    public void setMovieDbList(List<MovieDb> movieDbList) {
        this.movieDbList = movieDbList;
    }

    //Creates a new question
    public Question nextQuestion(){
        MovieDb movieDbs[] = new MovieDb[4];
        Random random = new Random();
        //Takes 4 movies from the given list
        for (int i = 0; i < 4; i++){
            int index = random.nextInt(movieDbList.size());
            movieDbs[i] = movieDbList.get(index);
            movieDbList.remove(index);
        }
        int currentCriteria = random.nextInt(5);
        //int currentCriteria = 4;
        String answers[] = new String[4];
        String question = "";
        int correctAnswer = 0;
        //Selects a random question type
        switch (currentCriteria){
            case RELEASE_YEAR:
                correctAnswer = random.nextInt(movieDbs.length);
                answers[correctAnswer] = movieDbs[correctAnswer].getReleaseDate().split("-")[0];
                for(int i = 0; i < answers.length;i++){
                    if(i != correctAnswer){
                        answers[i] = String.valueOf(random.nextInt((Calendar.getInstance().get(Calendar.YEAR) - 1980) - 5) + 1980);
                    }
                }
                question = "¿En que año se lanza la pelicula " + movieDbs[correctAnswer].getTitle() + "?";
                break;
            case COMPANY_NAME:
                for(int i = 0; i < answers.length; i++){
                    int index = random.nextInt(movieDbs[i].getProductionCompanies().size());
                    answers[i] = movieDbs[i].getProductionCompanies().get(index).getName();
                }
                correctAnswer = random.nextInt(answers.length);
                question = "¿Cual de las siguientes compañias produjo la pelicula "
                        + movieDbs[correctAnswer].getTitle() + "?";
                break;
            case OVERVIEW:
                for(int i = 0; i < answers.length; i++){
                    answers[i] = movieDbs[i].getTitle();
                }
                correctAnswer = random.nextInt(answers.length);
                question = movieDbs[correctAnswer].getOverview();
                break;
            case CAST:
                System.out.println("actores: " + movieDbs[0].getCredits().getCast().size());
                for(int i = 0; i < answers.length; i++){
                    int index = random.nextInt(movieDbs[i].getCredits().getCast().size());
                    answers[i] = movieDbs[i].getCredits().getCast().get(index).getName();
                }
                correctAnswer = random.nextInt(answers.length);
                question = "¿Cual de los siguientes actores aparece en la pelicula "
                        + movieDbs[correctAnswer].getTitle() + "?";
                break;
            case ORIGINAL_TITLE:
                for(int i = 0; i < answers.length; i++){
                    answers[i] = movieDbs[i].getOriginalTitle();
                }
                correctAnswer = random.nextInt(answers.length);
                question = "Pelicula mejor conocida como " + movieDbs[correctAnswer].getTitle();
                break;
            case MOVIE_CHARACTER:
                int movieIndex = random.nextInt(movieDbs.length);
                PersonCast personCasts[] = new PersonCast[4];
                for(int i = 0; i < personCasts.length && movieDbs[movieIndex].getCredits().getCast().size() > 0;){
                    int currentCast = random.nextInt(movieDbs[movieIndex].getCredits().getCast().size());
                    if(!movieDbs[movieIndex].getCredits().getCast().get(currentCast).getCharacter().isEmpty()){
                        personCasts[i] = movieDbs[movieIndex].getCredits().getCast().get(currentCast);
                        i++;
                    }
                    movieDbs[movieIndex].getCast().remove(currentCast);
                }
                correctAnswer = random.nextInt(answers.length);
                for (int i = 0; i < answers.length; i++){
                    answers[i] = personCasts[i].getName();
                }
                question = "¿Que actor interpreta a " + personCasts[correctAnswer].getCharacter()
                        + " en la pelicula " + movieDbs[movieIndex].getTitle() + "?";
                break;
        }
        //Returns the selected movies to the main list
        for(MovieDb movie : movieDbs){
            movieDbList.add(movie);
        }
        //Creates a question object
        String urlBackground = "https://image.tmdb.org/t/p/w500" + movieDbs[correctAnswer].getBackdropPath();
        Question questionResult = new Question(question, answers[0], answers[1], answers[2], answers[3], correctAnswer);
        questionResult.setBackground(urlBackground);
        //System.out.println("criterio: " + currentCriteria);
        return questionResult;
    }

}
