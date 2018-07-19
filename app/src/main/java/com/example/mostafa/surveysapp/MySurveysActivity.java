package com.example.mostafa.surveysapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mostafa.surveysapp.models.Survey;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MySurveysActivity extends AppCompatActivity {

    private FirebaseUser mCurrentUser;
    @BindView(R.id.profile_pic)CircleImageView profileImage;
    @BindView(R.id.username)TextView usernameTextView;
    @BindView(R.id.search_field)EditText searchField;
    @BindView(R.id.search_button)ImageButton searchButton;
    @BindView(R.id.my_surveys_list)RecyclerView mySurveysRecyclerView;
    private MySurveysAdapter mySurveysAdapter;
    private ArrayList<Survey> mMySurveys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_surveys);
        ButterKnife.bind(this);
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mCurrentUser.getPhotoUrl()!=null)
            Picasso.get().load(mCurrentUser.getPhotoUrl()).into(profileImage);
        usernameTextView.setText(mCurrentUser.getDisplayName());
        mMySurveys = getIntent().getParcelableArrayListExtra(getString(R.string.my_surveys));
        mySurveysAdapter = new MySurveysAdapter(mMySurveys);
        mySurveysRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mySurveysRecyclerView.setAdapter(mySurveysAdapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!searchField.getText().toString().isEmpty())
                {
                    searchOnSurvey(searchField.getText().toString());
                }
            }
        });
    }

    private void searchOnSurvey(String surveyTitle) {
        SurveySearchTask surveySearchTask = new SurveySearchTask(mMySurveys,mySurveysAdapter);
        surveySearchTask.execute(surveyTitle);
    }


    private static class SurveySearchTask extends AsyncTask<String, Void, ArrayList<Survey>> {

        private List<Survey> surveys;
        private MySurveysAdapter surveysAdapter;

        SurveySearchTask(List<Survey> surveys, MySurveysAdapter surveysAdapter) {
            this.surveys = surveys;
            this.surveysAdapter = surveysAdapter;
        }

        @Override
        protected ArrayList<Survey> doInBackground(String... strings) {
            ArrayList<Survey> resultSurveys = new ArrayList<>();
            for (Survey survey : surveys)
                 {
                     if(survey.getTitle().contains(strings[0]))
                         resultSurveys.add(survey);
                 }
            return resultSurveys;
        }


        @Override
        protected void onPostExecute(ArrayList<Survey> surveys) {
            if(surveys!=null && surveys.size()!=0)
            {
                surveysAdapter.clear();
                surveysAdapter.add(surveys);
            }
        }
    }


}
