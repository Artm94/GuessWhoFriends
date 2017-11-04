package com.example.arturo.guesswhofriends;

import java.io.Serializable;

/**
 * Created by arturo on 22/10/17.
 */

//This model class represents a question
public class Question implements Serializable{
    //Variable declaration
    private String question;
    private String answer1, answer2, answer3, answer4;
    private int correctAnswer;
    private String background;

    //Initialize a new Question Object
    public Question(String question, String answer1, String answer2, String answer3, String answer4, int correctAnswer){
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        if(correctAnswer < 0 || correctAnswer > 4){
            this.correctAnswer = 1;
        }else {
            this.correctAnswer = correctAnswer;
        }
    }

    //Checks if the answers given by the user is correct
    public boolean checkAnswer(int userAnswer){
        return (correctAnswer == userAnswer);
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer1() {
        return answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    //Sets the correct answer
    public void setCorrectAnswer(int correctAnswer){
        if(correctAnswer < 0 || correctAnswer > 4){
            this.correctAnswer = 1;
        }else {
            this.correctAnswer = correctAnswer;
        }
    }

    //Returns the correct (String) answer
    public String getCorrectAnswer() {
        String correctAnswer = null;
        switch (this.correctAnswer){
            case 0:
                correctAnswer = getAnswer1();
                break;
            case 1:
                correctAnswer = getAnswer2();
                break;
            case 2:
                correctAnswer = getAnswer3();
                break;
            case 3:
                correctAnswer = getAnswer4();
                break;
        }
        return correctAnswer;
    }

    public int getCorrectAnswerIndex(){
        return correctAnswer;
    }

}
