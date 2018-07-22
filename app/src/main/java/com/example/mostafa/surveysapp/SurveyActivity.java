package com.example.mostafa.surveysapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private ArrayList<Question> mAnsweredQuestions;
    private ArrayList<Integer> mSelectedPositions;
    private ArrayList<String> mSelectedAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        ButterKnife.bind(this);
        mSurvey = getIntent().getParcelableExtra(getString(R.string.survey));
        mQuestionCount = mSurvey.getQuestions().size();
        optionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAnsweredQuestions = new ArrayList<>();
        mSelectedAnswers = new ArrayList<>();
        mSelectedPositions = new ArrayList<>();
        nextButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(getString(R.string.position), mCurrentPosition);
        outState.putIntegerArrayList(getString(R.string.positions), mAnswersAdapter.getSelectedPositions());
        outState.putStringArrayList(getString(R.string.answers), mAnswersAdapter.getSelectedAnswers());
        outState.putParcelableArrayList(getString(R.string.questions), mAnsweredQuestions);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentPosition = savedInstanceState.getInt(getString(R.string.position));
        mSelectedPositions = savedInstanceState.getIntegerArrayList(getString(R.string.positions));
        mSelectedAnswers = savedInstanceState.getStringArrayList(getString(R.string.answers));
        mAnsweredQuestions = savedInstanceState.getParcelableArrayList(getString(R.string.questions));
    }

    @Override
    protected void onResume() {
        super.onResume();
        previewQuestion();
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
            case 3:
                mAnswersAdapter = new AnswersAdapter(question.getAnswers(), question.getType(), mSelectedAnswers, mSelectedPositions);
                optionsRecyclerView.setAdapter(mAnswersAdapter);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        if (isValidAnswer()) {
            addAnsweredQuestion();
            switch (v.getId()) {
                case R.id.submit:
                    submitResult();
                    break;
                case R.id.next:
                    mCurrentPosition++;
                    previewQuestion();
                    break;
            }
        }
        else Toast.makeText(this,getString(R.string.answer_the_question),Toast.LENGTH_SHORT).show();
    }

    private void submitResult() {
        Result result = new Result();
        result.setOwnerId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        result.setQuestions(mAnsweredQuestions);
        DatabaseReference resultReference = FirebaseDatabase.getInstance().getReference()
                .child(mSurvey.getId()).child(getString(R.string.results))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        resultReference.setValue(result).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(SurveyActivity.this, getString(R.string.poll_submitted), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void addAnsweredQuestion() {
        Question question = mSurvey.getQuestions().get(mCurrentPosition);
        Question answeredQuestion = new Question();
        answeredQuestion.setType(question.getType());
        answeredQuestion.setQuestion(question.getQuestion());
        if (question.getType() == 1) {
            answeredQuestion.setAnswers(new ArrayList<>(Collections.singletonList(answerField.getText().toString())));
        } else {
            answeredQuestion.setAnswers(mAnswersAdapter.getSelectedAnswers());
        }
        mAnsweredQuestions.add(answeredQuestion);
    }

    private boolean isValidAnswer() {
        Question question = mSurvey.getQuestions().get(mCurrentPosition);
        if (question.getType() == 1) {
            return !answerField.getText().toString().trim().isEmpty();
        } else {
            return mAnswersAdapter.getSelectedAnswers().size() != 0;
        }
    }
}