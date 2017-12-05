package com.example.arturo.guesswhofriends;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
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
    private boolean userAnswers[];
    private TimerTask gameTimeTask;
    private ProgressBar pgbMaxTime;
    private int gameTime;
    private int currentTime;
    private int questionsQuantity;
    private int gameDifficulty;
    private boolean animationsStatus;
    private final int interval = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Variable initialization
        txtQuestion = (TextView) findViewById(R.id.txtQuestion);
        txtQuestion.setMovementMethod(new ScrollingMovementMethod());
        btnOne = (Button) findViewById(R.id.btnOne);
        btnTwo = (Button) findViewById(R.id.btnTwo);
        btnThree = (Button) findViewById(R.id.btnThree);
        btnFour = (Button) findViewById(R.id.btnFour);
        pgbMaxTime = (ProgressBar) findViewById(R.id.pgbMaxTime);
        questions = (ArrayList<Question>) getIntent().getExtras().get("questions");
        questionsQuantity = questions.size();
        userAnswers = new boolean[questionsQuantity];
        Arrays.fill(userAnswers, Boolean.FALSE);
        gameTime = (int) getIntent().getExtras().get("game_time");
        gameDifficulty = (int) getIntent().getExtras().get("game_difficulty");
        animationsStatus = (boolean) getIntent().getExtras().get("animations_status");
        currentQuestionIndex = 0;

        //Add the first question to the view
        Question question = questions.get(currentQuestionIndex);
        txtQuestion.setText(question.getQuestion());
        txtQuestion.scrollTo(0,0);
        btnOne.setText(question.getAnswer1());
        btnTwo.setText(question.getAnswer2());
        btnThree.setText(question.getAnswer3());
        btnFour.setText(question.getAnswer4());
        if(animationsStatus){
            changeBackground(question.getBackground());
        }

        //Create the task to control the time
        gameTimeTask = new TimerTask() {
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
                            Intent scoreStart = new Intent(getApplicationContext(), ScoreActivity.class);
                            scoreStart.putExtra("score", getTotalScore());
                            startActivity(scoreStart);
                            Toast.makeText(getApplicationContext(), "Se acabo el tiempo ", Toast.LENGTH_LONG).show();
                            finish();
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
                if(currentQuestionIndex < questionsQuantity - 1){
                    currentQuestionIndex++;
                    Question question = questions.get(currentQuestionIndex);
                    if(animationsStatus){
                        changeBackground(question.getBackground());
                    }
                    txtQuestion.setText(question.getQuestion());
                    txtQuestion.scrollTo(0,0);
                    btnOne.setText(question.getAnswer1());
                    btnTwo.setText(question.getAnswer2());
                    btnThree.setText(question.getAnswer3());
                    btnFour.setText(question.getAnswer4());
                }else{
                    //Cancel the task above if there's no more questions
                    gameTimeTask.cancel();
                    //If user answers the last question, the score must be showed here
                    Intent scoreStart = new Intent(getApplicationContext(), ScoreActivity.class);
                    scoreStart.putExtra("score", getTotalScore());
                    startActivity(scoreStart);
                    Toast.makeText(getApplicationContext(), "Juego terminado ", Toast.LENGTH_LONG).show();
                    finish();
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
        int questionScoreValue = 0;
        switch(gameDifficulty){
            case Settings.NOVEL_DIFFICULTY:
               questionScoreValue =  20;
               break;
            case Settings.ENTHUSIAST_DIFFICULTY:
                questionScoreValue = 40;
                break;
            case Settings.PROFESSIONAL_DIFFICULTY:
                questionScoreValue = 60;
                break;
            case Settings.EXPERT_DIFFICULTY:
                questionScoreValue = 80;
                break;
        }
        int totalScore = 0;
        boolean isPerfectScore = true;
        for(boolean answer : userAnswers){
            isPerfectScore = isPerfectScore && answer;
            totalScore += answer ? questionScoreValue : 0;
        }
        totalScore += isPerfectScore ? totalScore * 0.20 : 0;
        if(currentTime < gameTime){
            int progress = currentTime * 100 / gameTime;
            totalScore += ((100 - progress)/100) * questionScoreValue;
        }
        return totalScore;
    }
}
