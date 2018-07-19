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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SurveysListActivity extends AppCompatActivity {


    @BindView(R.id.surveys_list)RecyclerView surveysRecyclerView;
    private SurveysAdapter mSurveysAdapter;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mUsersReference;
    private ValueEventListener mValueEventListener ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surveys_list);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        mUsersReference = FirebaseDatabase.getInstance().getReference().child("surveys");
        surveysRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSurveysAdapter = new SurveysAdapter(new ArrayList<Survey>());
        surveysRecyclerView.setAdapter(mSurveysAdapter);
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Survey> surveys = new ArrayList<>();
                for (DataSnapshot snapshot :dataSnapshot.getChildren()) {
                    surveys.add(snapshot.getValue(Survey.class));
                }
                mSurveysAdapter.addAll(surveys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUsersReference.addValueEventListener(mValueEventListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mUsersReference.removeEventListener(mValueEventListener);
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
                ArrayList<Survey> mySurveys = mSurveysAdapter.getMySurveys(FirebaseAuth.getInstance().getUid());
                intent.putParcelableArrayListExtra(getString(R.string.my_surveys),mySurveys);
                startActivity(intent);

            }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
