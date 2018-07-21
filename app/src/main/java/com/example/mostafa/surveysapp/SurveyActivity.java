package com.example.mostafa.surveysapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mostafa.surveysapp.models.Question;
import com.example.mostafa.surveysapp.models.Result;
import com.example.mostafa.surveysapp.models.Survey;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SurveyActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.options_list)
    RecyclerView optionsRecyclerView;
    @BindView(R.id.submit)
    Button submitButton;
    @BindView(R.id.next)
    Button nextButton;
    @BindView(R.id.progress)
    TextView progress;
    @BindView(R.id.question)
    TextView questionTextView;
    @BindView(R.id.essay_answer)
    EditText answerField;


    private Survey mSurvey;
    private int mCurrentPosition = 0;
    private int mQuestionCount;
    private AnswersAdapter mAnswersAdapter;
    private List<Question> mAnsweredQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        ButterKnife.bind(this);
        mSurvey = getIntent().getParcelableExtra(getString(R.string.survey));
        mQuestionCount = mSurvey.getQuestions().size();
        optionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        previewQuestion();
        mAnsweredQuestions = new ArrayList<>();
        nextButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);
    }


    private void previewQuestion() {
        Question question = mSurvey.getQuestions().get(mCurrentPosition);
        answerField.getText().clear();
        answerField.clearFocus();
        questionTextView.setText(question.getQuestion());
        if (mCurrentPosition + 1 == mQuestionCount) {
            nextButton.setVisibility(View.GONE);
            submitButton.setVisibility(View.VISIBLE);
        } else {
            nextButton.setVisibility(View.VISIBLE);
            submitButton.setVisibility(View.GONE);
        }
        progress.setText(getString(R.string.progress, mCurrentPosition + 1, mQuestionCount));
        if (question.getType() != 1)
            answerField.setVisibility(View.GONE);
        switch (question.getType()) {
            case 2:
                mAnswersAdapter = new AnswersAdapter(question.getAnswers(), 2, new ArrayList<String>(), new ArrayList<Integer>());
                optionsRecyclerView.setAdapter(mAnswersAdapter);
                break;
            case 3:
                mAnswersAdapter = new AnswersAdapter(question.getAnswers(), 3, new ArrayList<String>(), new ArrayList<Integer>());
                optionsRecyclerView.setAdapter(mAnswersAdapter);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit) {

            if(isValidAnswer())
            {
                addAnsweredQuestion();
                submitResult();
            }
        } else if (v.getId() == R.id.next) {
            if (isValidAnswer()) {
                addAnsweredQuestion();
                mCurrentPosition++;
                previewQuestion();
            }
        }
    }

    private void submitResult() {
        Result result = new Result();
        result.setOwnerId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        result.setQuestions(mAnsweredQuestions);
        ////////////////

    }

    private void addAnsweredQuestion() {
        Question question = mSurvey.getQuestions().get(mCurrentPosition);
        Question answeredQuestion = new Question();
        answeredQuestion.setType(question.getType());
        answeredQuestion.setQuestion(question.getQuestion());
        if (question.getType()==1)
        {
            answeredQuestion.setAnswers(new ArrayList<>(Collections.singletonList(answerField.getText().toString().trim())));
        }
        else {
            answeredQuestion.setAnswers(mAnswersAdapter.getSelectedAnswers());
        }
        mAnsweredQuestions.add(answeredQuestion);
    }

    private boolean isValidAnswer() {
        Question question = mSurvey.getQuestions().get(mCurrentPosition);
        if(question.getType()==1)
        {
            return !answerField.getText().toString().trim().isEmpty();
        }
        else {
            return mAnswersAdapter.getSelectedAnswers().size() != 0;
        }
    }
}