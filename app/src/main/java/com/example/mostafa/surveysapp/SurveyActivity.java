package com.example.mostafa.surveysapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mostafa.surveysapp.models.Question;
import com.example.mostafa.surveysapp.models.Result;
import com.example.mostafa.surveysapp.models.Survey;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SurveyActivity extends AppCompatActivity implements View.OnClickListener{


    @BindView(R.id.options_list)RecyclerView optionsRecyclerView;
    @BindView(R.id.submit_result)Button submitButton;
    @BindView(R.id.next_question)Button nextButton;
    @BindView(R.id.progress_text)TextView progressText;
    @BindView(R.id.question)TextView questionTextView;
    @BindView(R.id.essay_answer)EditText answerField;

    private List<Question> mAnsweredQuestion;
    private int mCurrentPosition = 1;
    private int mQuestionsCount;
    private Survey mSurvey;
    private AnswersAdapter answersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        ButterKnife.bind(this);
        mSurvey = getIntent().getParcelableExtra(getString(R.string.survey));
        mQuestionsCount = mSurvey.getQuestions().size();
        previewQuestion();
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.submit_result)
        {

        }
        else if(view.getId()==R.id.next_question){

        }
    }



    private void previewQuestion()
    {

        Question question = mSurvey.getQuestions().get(mCurrentPosition);
        questionTextView.setText(question.getQuestion());
        switch (question.getType()){
            case 1: {
                answerField.setVisibility(View.VISIBLE);
                optionsRecyclerView.setVisibility(View.GONE);

                break;
            }
            case 2:
                break;
            case 3:
                break;
        }
        progressText.setText(getString(R.string.progress,mCurrentPosition,mQuestionsCount));
        if(mCurrentPosition==mQuestionsCount) {
            nextButton.setVisibility(View.GONE);
            submitButton.setVisibility(View.VISIBLE);
        }
    }
}