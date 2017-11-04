package com.example.arturo.guesswhofriends;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbDiscover;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.Discover;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.people.Person;

//This class creates loads game questions
public class LoadingActivity extends AppCompatActivity {
    //Variable declaration
    public static String API_KEY = "e01a182bdeed3afc056d41564eba1bdd";
    private static final int MOST_POPULAR = 0;
    public static final int YEAR_RANGE = 1;
    public static final int NOW_PLAYING = 2;
    public static final int MOVIE_CAST = 3;
    public static final int MOVIE_GENRE = 4;
    private int questionsNumber = 10;
    private InternetInformationTask informationTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        //Creates a new task to get the questions
        informationTask = new InternetInformationTask();
        informationTask.execute();
    }

    //Gets a list of movies to create the questions
    public List<MovieDb> getMovieList(){
        List<MovieDb> movieDbList = null;
        Random random = new Random();
        int currentCriteria = random.nextInt(5);
        TmdbApi tmdbApi = new TmdbApi(API_KEY);
        TmdbDiscover tmdbDiscover = tmdbApi.getDiscover();
        Discover discover;
        //Selects a random criteria
        switch(currentCriteria){
            case MOST_POPULAR:
                movieDbList = tmdbApi.getMovies().getPopularMovies("es", 1).getResults();
                break;
            case YEAR_RANGE:
                int primary_year = random.nextInt((Calendar.getInstance().get(Calendar.YEAR) - 1980) - 5) + 1980;
                discover = new Discover()
                        .page(1)
                        .language("es")
                        .sortBy("popularity.desc");
                discover.getParams().put("primary_release_date.gte", String.valueOf(primary_year));
                movieDbList = tmdbDiscover.getDiscover(discover).getResults();
                break;
            case MOVIE_CAST:
                List<Person> persons = tmdbApi.getPeople().getPersonPopular(1).getResults();
                Person selectedPerson = persons.get(random.nextInt(persons.size()));
                discover = new Discover()
                        .page(1)
                        .language("es")
                        .sortBy("popularity.desc");
                discover.getParams().put("with_cast", String.valueOf(selectedPerson.getId()));
                movieDbList = tmdbDiscover.getDiscover(discover).getResults();
                break;
            case NOW_PLAYING:
                movieDbList = tmdbApi.getMovies().getNowPlayingMovies("es", 1).getResults();
                break;
            case MOVIE_GENRE:
                List<Genre> genres = tmdbApi.getGenre().getGenreList("es");
                Genre selectedGenre = genres.get(random.nextInt(genres.size()));
                discover = new Discover()
                        .page(1)
                        .language("es")
                        .sortBy("popularity.desc");
                discover.getParams().put("with_genres", String.valueOf(selectedGenre.getId()));
                movieDbList = tmdbDiscover.getDiscover(discover).getResults();
                break;
        }

        List<MovieDb> movieDbs = new ArrayList<>();
        for(MovieDb movie : movieDbList){
            movieDbs.add(tmdbApi.getMovies().getMovie(movie.getId(), "es", TmdbMovies.MovieMethod.credits));
        }
        return movieDbs;
    }

    //This class gets the information from Internet and creates the questions
    private class InternetInformationTask extends AsyncTask<Object, Object, ArrayList<Question>> {

        @Override
        protected ArrayList<Question> doInBackground(Object... params) {
            try{
                List<MovieDb> movieList = getMovieList();
                QuestionBuilder builder = new QuestionBuilder(movieList);
                ArrayList<Question> questions = new ArrayList<>();
                for(int i = 0; i < questionsNumber; i++){
                    questions.add(builder.nextQuestion());
                }
                return questions;
            }catch(Exception e){
                e.printStackTrace();
                this.cancel(true);
                return null;
            }
        }

        //Creates the GameActivity and sends it the questions
        @Override
        public void onPostExecute(ArrayList<Question> result){
            Intent gameActivity = new Intent(getApplicationContext(), GameActivity.class);
            gameActivity.putExtra("questions", result);
            startActivity(gameActivity);
            finish();
        }

        //If the task is cancelled, goes back to the MainActivity
        @Override
        public void onCancelled(){
            Toast.makeText(getApplicationContext(), "Se cancelo la partida", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
