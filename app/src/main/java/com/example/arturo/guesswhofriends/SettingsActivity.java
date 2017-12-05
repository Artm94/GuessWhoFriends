package com.example.arturo.guesswhofriends;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {
    private Spinner spnQuantity;
    private Spinner spnDifficulty;
    private Switch swtAnimations;
    private Button btnAbout;
    private Settings settings;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        spnQuantity = (Spinner) findViewById(R.id.spnQuantity);
        spnDifficulty = (Spinner) findViewById(R.id.spnDifficulty);
        swtAnimations = (Switch) findViewById(R.id.swtAnimations);
        btnAbout = (Button) findViewById(R.id.btnAbout);

        ArrayAdapter<CharSequence> difficultyAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.difficulty_labels, android.R.layout.simple_spinner_item);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter <CharSequence> quantityAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.quantity_labels, android.R.layout.simple_spinner_item);
        quantityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDifficulty.setAdapter(difficultyAdapter);
        spnQuantity.setAdapter(quantityAdapter);

        settings = new Settings(getApplicationContext());
        loadGameDifficulty(settings.getGameDifficulty());
        loadGameQuestionsQuanity(settings.getQuestionsQuantity());
        swtAnimations.setChecked(settings.getAnimationsStatus());

        spnDifficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case Settings.NOVEL_DIFFICULTY:
                        settings.setGameDifficulty(Settings.NOVEL_DIFFICULTY);
                        break;
                    case Settings.ENTHUSIAST_DIFFICULTY:
                        settings.setGameDifficulty(Settings.ENTHUSIAST_DIFFICULTY);
                        break;
                    case Settings.PROFESSIONAL_DIFFICULTY:
                        settings.setGameDifficulty(Settings.PROFESSIONAL_DIFFICULTY);
                        break;
                    case Settings.EXPERT_DIFFICULTY:
                        settings.setGameDifficulty(Settings.EXPERT_DIFFICULTY);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnQuantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case Settings.FIVE_QUESTIONS:
                        settings.setQuestionsQuantity(Settings.FIVE_QUESTIONS);
                        break;
                    case Settings.TEN_QUESTIONS:
                        settings.setQuestionsQuantity(Settings.TEN_QUESTIONS);
                        break;
                    case Settings.FIFTEEN_QUESTIONS:
                        settings.setQuestionsQuantity(Settings.FIFTEEN_QUESTIONS);
                        break;
                    case Settings.TWENTY_QUESTIONS:
                        settings.setQuestionsQuantity(Settings.TWENTY_QUESTIONS);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        swtAnimations.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setAnimationsStatus(isChecked);
            }
        });
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent about = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(about);
            }
        });
    }

    public void loadGameDifficulty(int difficultyValue){
        switch(difficultyValue){
            case Settings.NOVEL_DIFFICULTY:
                spnDifficulty.setSelection(0);
                break;
            case Settings.ENTHUSIAST_DIFFICULTY:
                spnDifficulty.setSelection(1);
                break;
            case Settings.PROFESSIONAL_DIFFICULTY:
                spnDifficulty.setSelection(2);
                break;
            case Settings.EXPERT_DIFFICULTY:
                spnDifficulty.setSelection(3);
                break;
        }
    }

    public void loadGameQuestionsQuanity(int questionsQuantity){
        switch (questionsQuantity){
            case Settings.FIVE_QUESTIONS:
                spnQuantity.setSelection(0);
                break;
            case Settings.TEN_QUESTIONS:
                spnQuantity.setSelection(1);
                break;
            case Settings.FIFTEEN_QUESTIONS:
                spnQuantity.setSelection(2);
                break;
            case Settings.TWENTY_QUESTIONS:
                spnQuantity.setSelection(3);
                break;
        }
    }
}
