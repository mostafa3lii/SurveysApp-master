package com.example.mostafa.surveysapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mostafa.surveysapp.models.Question;
import com.example.mostafa.surveysapp.models.Result;
import com.example.mostafa.surveysapp.models.Survey;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SurveyActivity extends AppCompatActivity implements View.OnClickListener{


    @BindView(R.id.questions_list)RecyclerView questionsRecyclerView;
    @BindView(R.id.submit_result)Button submitButton;


    private SurveyQuestionsAdapter surveyQuestionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        ButterKnife.bind(this);
        Survey survey = getIntent().getParcelableExtra(getString(R.string.survey));
        questionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        surveyQuestionsAdapter = new SurveyQuestionsAdapter(survey.getQuestions(),this);
        questionsRecyclerView.setAdapter(surveyQuestionsAdapter);
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.submit_result)
        {
            Result result = surveyQuestionsAdapter.getResult();
            for (Question question :result.getQuestions()) {
                if(question.getAnswers().size()==0)
                    Toast.makeText(this, getString(R.string.answer_all_questions), Toast.LENGTH_SHORT).show();
                    return;
            }
            for (Question question :result.getQuestions()) {
                Log.i("questionnn",question.getQuestion());
                for (String answer :question.getAnswers()) {
                    Log.i("answer", answer);
                }

            }

        }
    }
}