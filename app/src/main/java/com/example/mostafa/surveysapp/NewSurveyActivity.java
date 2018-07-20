package com.example.mostafa.surveysapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mostafa.surveysapp.models.Question;
import com.example.mostafa.surveysapp.models.Survey;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewSurveyActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_CODE = 88;

    @BindView (R.id.questions_list) RecyclerView questionsRecyclerView;
    @BindView(R.id.essay)FloatingActionButton essayButton;
    @BindView(R.id.single_choice)FloatingActionButton singleChoiceButton;
    @BindView(R.id.add_question)FloatingActionMenu newQuestionMenu;
    @BindView(R.id.multi_choice)FloatingActionButton multiChoiceButton;
    @BindView(R.id.survey_title)EditText titleField;
    @BindView(R.id.publish)Button publishButton;

    private QuestionsAdapter mQuestionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_survey);
        ButterKnife.bind(this);
        mQuestionsAdapter = new QuestionsAdapter(new ArrayList<Question>(),this);
        questionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        questionsRecyclerView.setAdapter(mQuestionsAdapter);
        essayButton.setOnClickListener(this);
        singleChoiceButton.setOnClickListener(this);
        multiChoiceButton.setOnClickListener(this);
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishSurvey();
            }
        });
    }

    private void publishSurvey()
    {
        if (titleField.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.add_survey_title), Toast.LENGTH_SHORT).show();
            return;
        }
        else if (mQuestionsAdapter.getQuestions().size()==0)
        {
            Toast.makeText(this, getString(R.string.no_questions_added), Toast.LENGTH_SHORT).show();
            return;
        }
        Survey survey = new Survey();
        survey.setOwnerPic(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
        survey.setQuestions(mQuestionsAdapter.getQuestions());
        survey.setOwnerId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        survey.setTitle(titleField.getText().toString());
        DatabaseReference surveysReference = FirebaseDatabase.getInstance().getReference().child("surveys");
        surveysReference.push().setValue(survey);
        Toast.makeText(this, getString(R.string.survey_published_successfully), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getString(R.string.questions),mQuestionsAdapter.getQuestions());
        outState.putString(getString(R.string.survey_title),titleField.getText().toString());
        outState.putParcelable(getString(R.string.position),questionsRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<Question> questions = savedInstanceState.getParcelableArrayList(getString(R.string.questions));
        mQuestionsAdapter.add(questions);
        titleField.setText(savedInstanceState.getString(getString(R.string.survey_title)));
        questionsRecyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(getString(R.string.position)));
    }

    @Override
    public void onClick(View v) {
        int type ;
        if(v.getId()==R.id.essay) type = 1;
        else if (v.getId()==R.id.single_choice) type = 2;
        else type = 3;
        newQuestionMenu.close(true);
        addNewQuestion(type);
    }

    private void addNewQuestion(int type) {
        Intent intent = new Intent(this,NewQuestionActivity.class);
        intent.putExtra(getString(R.string.type),type);
        startActivityForResult(intent,REQUEST_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK){
                mQuestionsAdapter.add((Question)data.getParcelableExtra(getString(R.string.question)));
            }

        }
    }
}