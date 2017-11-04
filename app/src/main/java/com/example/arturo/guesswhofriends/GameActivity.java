package com.example.arturo.guesswhofriends;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

//This class controls the game process
public class GameActivity extends AppCompatActivity {

    //Variable declaration
    private Button btnOne, btnTwo, btnThree, btnFour;
    private TextView txtQuestion;
    private ArrayList<Question> questions;
    private int currentQuestionIndex;
    private View.OnClickListener onClickListener;
    private Boolean userAnswers[];
    private TimerTask gameTimeTask;
    private ProgressBar pgbMaxTime;
    private int gameTime = 45000;
    private int interval = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Variable initialization
        txtQuestion = (TextView) findViewById(R.id.txtQuestion);
        btnOne = (Button) findViewById(R.id.btnOne);
        btnTwo = (Button) findViewById(R.id.btnTwo);
        btnThree = (Button) findViewById(R.id.btnThree);
        btnFour = (Button) findViewById(R.id.btnFour);
        pgbMaxTime = (ProgressBar) findViewById(R.id.pgbMaxTime);
        questions = (ArrayList<Question>) getIntent().getExtras().get("questions");
        userAnswers = new Boolean[questions.size()];
        currentQuestionIndex = 0;

        //Add the first question to the view
        Question question = questions.get(currentQuestionIndex);
        txtQuestion.setText(question.getQuestion());
        btnOne.setText(question.getAnswer1());
        btnTwo.setText(question.getAnswer2());
        btnThree.setText(question.getAnswer3());
        btnFour.setText(question.getAnswer4());
        changeBackground(question.getBackground());

        //Create the task to control the time
        gameTimeTask = new TimerTask() {
            private int currentTime = 0;
            private int progress = 0;
            @Override
            public void run() {
                //Update the progress bar status
                if(currentTime <= gameTime){
                    progress = currentTime * 100 / gameTime;
                    pgbMaxTime.setProgress(progress);
                    currentTime += interval;
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //If the time of execution ends, the score must be showed here
                            Toast.makeText(getApplicationContext(), "Se acabo el tiempo: ", Toast.LENGTH_LONG).show();
                        }
                    });
                    this.cancel();
                }
            }
        };
        //Start and update the task above every second
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(gameTimeTask, 0, interval);

        //Create the button listener
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validate the user answer
                String userAnswer = String.valueOf(((Button) v).getTag());
                validateAnswer(Integer.parseInt(userAnswer));
                //Insert the next question into the view
                if(currentQuestionIndex < questions.size() - 1){
                    currentQuestionIndex++;
                    Question question = questions.get(currentQuestionIndex);
                    String background = question.getBackground();
                    changeBackground(background);
                    txtQuestion.setText(question.getQuestion());
                    btnOne.setText(question.getAnswer1());
                    btnTwo.setText(question.getAnswer2());
                    btnThree.setText(question.getAnswer3());
                    btnFour.setText(question.getAnswer4());
                }else{
                    //Cancel the task above if there's no more questions
                    gameTimeTask.cancel();
                    //If user answers the last question, the score must be showed here
                    Toast.makeText(getApplicationContext(), "Juego terminado: ", Toast.LENGTH_LONG).show();
                }
            }
        };

        //Add the listener above to the answer buttons
        btnOne.setOnClickListener(onClickListener);
        btnTwo.setOnClickListener(onClickListener);
        btnThree.setOnClickListener(onClickListener);
        btnFour.setOnClickListener(onClickListener);
    }

    //Change the background
    public boolean changeBackground(String background){
        if(background.isEmpty()){
            return false;
        }else{
            //Every time this method is called creates a new task to download and process the new background, could be dangerous...
            new DownloadImageTask(findViewById(R.id.backgroundLayout), getApplicationContext()).execute(background);
            return true;
        }
    }

    //Process the user answer
    public void validateAnswer(int userAnswer){
        userAnswers[currentQuestionIndex] = questions.get(currentQuestionIndex).checkAnswer(userAnswer);
    }

    //Ge the user total Score
    public int getTotalScore(){
        int totalScore = 0;
        for(boolean answer : userAnswers){
            totalScore += answer ? 100 : 0;
        }
        return totalScore;
    }
}
