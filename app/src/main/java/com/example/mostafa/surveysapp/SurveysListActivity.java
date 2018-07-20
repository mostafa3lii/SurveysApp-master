package com.example.mostafa.surveysapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.mostafa.surveysapp.models.Question;
import com.example.mostafa.surveysapp.models.Survey;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SurveysListActivity extends AppCompatActivity implements SurveysAdapter.OnClickListener{


    @BindView(R.id.surveys_list)RecyclerView surveysRecyclerView;
    private SurveysAdapter mSurveysAdapter;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mSurveysReference;
    private ValueEventListener mValueEventListener ;
    private List<Survey> mSurveys;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surveys_list);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        mSurveysReference = FirebaseDatabase.getInstance().getReference().child(getString(R.string.surveys));
        surveysRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        // testing dummy survey
        Survey survey = getDummySurvey();
        List<Survey> surveys = new ArrayList<>();
        surveys.add(survey);

        mSurveysAdapter = new SurveysAdapter(surveys,this);
        surveysRecyclerView.setAdapter(mSurveysAdapter);


        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               /* mSurveys = new ArrayList<>();
                List<Survey> surveys = new ArrayList<>();
                for (DataSnapshot snapshot :dataSnapshot.getChildren()) {
                    String uId  = mAuth.getCurrentUser().getUid();
                    Survey survey = snapshot.getValue(Survey.class);
                    mSurveys.add(survey);
                    if(!snapshot.child(getString(R.string.results)).hasChild(uId) && !survey.getOwnerId().equals(uId))
                        surveys.add(survey);
                }
                mSurveysAdapter.addAll(surveys);*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    private Survey getDummySurvey() {
        Survey survey = new Survey();
        survey.setTitle("Survey title");
        survey.setOwnerId("dsad");
        survey.setOwnerPic("sasas");
        List<Question> questions = new ArrayList<>();
        Question question = new Question();
        question.setQuestion("What is essay now??");
        question.setAnswers(new ArrayList<String>());
        question.setType(1);
        Question question1 = new Question();
        question1.setType(2);
        question1.setAnswers(new ArrayList<String>(Arrays.asList("first", "second","third")));
        question1.setQuestion("who has answers??");
        Question question2 = new Question();
        question2.setType(2);
        question2.setAnswers(new ArrayList<String>(Arrays.asList("first", "second","third")));
        question2.setQuestion("who has answers??");
        questions.add(question);
        questions.add(question1);
        questions.add(question2);
        survey.setQuestions(questions);
        return survey;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSurveysReference.addValueEventListener(mValueEventListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSurveysReference.removeEventListener(mValueEventListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sign_out:
                AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(SurveysListActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                return true;
            case R.id.new_survey:
                startActivity(new Intent(this,NewSurveyActivity.class));
                return true;
            case R.id.my_surveys: {
                Intent intent = new Intent(this, MySurveysActivity.class);
                ArrayList<Survey> mySurveys = getMySurveys();
                intent.putParcelableArrayListExtra(getString(R.string.my_surveys),mySurveys);
                startActivity(intent);

            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ArrayList<Survey> getMySurveys ()
    {
        ArrayList<Survey> mySurveys = new ArrayList<>();
        for (Survey survey :mySurveys) {
            if (survey.getOwnerId().equals(mAuth.getCurrentUser().getUid()))
            {
                mySurveys.add(survey);
            }
        }
        return mySurveys;
    }

    @Override
    public void onClick(Survey survey) {
        Intent intent = new Intent(this,SurveyActivity.class);
        intent.putExtra(getString(R.string.survey),survey);
        startActivity(intent);
    }
}