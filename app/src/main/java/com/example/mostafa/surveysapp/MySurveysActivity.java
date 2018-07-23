package com.example.mostafa.surveysapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mostafa.surveysapp.models.Survey;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MySurveysActivity extends AppCompatActivity{


    @BindView(R.id.profile_pic)CircleImageView profileImage;
    @BindView(R.id.username)TextView usernameTextView;
    @BindView(R.id.search)SearchView searchView;
    @BindView(R.id.my_surveys_list)RecyclerView mySurveysRecyclerView;
    @BindView(R.id.message)TextView messageTextView;
    private MySurveysAdapter mySurveysAdapter;
    private ArrayList<Survey> mMySurveys;
    private boolean isSearching ;
    private String mSearchWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_surveys);
        ButterKnife.bind(this);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        Picasso.get().load(currentUser.getPhotoUrl()).into(profileImage);
//        usernameTextView.setText(currentUser.getDisplayName());
        mMySurveys = getIntent().getParcelableArrayListExtra(getString(R.string.my_surveys));
        mySurveysAdapter = new MySurveysAdapter(mMySurveys);
        mySurveysRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mySurveysRecyclerView.setAdapter(mySurveysAdapter);
        updateSearchView();
    }

    private void updateSearchView() {
        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                isSearching = true;
                mSearchWord = query;
                searchOnSurvey();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                isSearching = false;
                mySurveysAdapter.addAll(mMySurveys);
                searchView.clearFocus();
                closeKeyboard();
                return true;
            }
        });
    }

    private void closeKeyboard()
    {
        InputMethodManager inputManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(getString(R.string.is_searching),isSearching);
        outState.putString(getString(R.string.search_word),mSearchWord);
        outState.putString(getString(R.string.input_text),searchView.getQuery().toString());
        outState.putParcelable(getString(R.string.position),mySurveysRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isSearching = savedInstanceState.getBoolean(getString(R.string.is_searching));
        mSearchWord = savedInstanceState.getString(getString(R.string.search_word));
        searchView.setQuery(savedInstanceState.getString(getString(R.string.input_text)),false);
        if(isSearching)searchOnSurvey();
        mySurveysRecyclerView.getLayoutManager().onRestoreInstanceState(
                savedInstanceState.getParcelable(getString(R.string.position)));
    }

    private void searchOnSurvey() {

        SurveySearchTask surveySearchTask = new SurveySearchTask(mMySurveys, new OnSearchFinished() {
            @Override
            public void onFinished(List<Survey> surveys) {
                if(surveys!=null && !surveys.isEmpty())
                    mySurveysAdapter.addAll(surveys);
                else
                    if(mMySurveys.size()!=0)
                    messageTextView.setText(getString(R.string.no_search_results));
            }
        });
        surveySearchTask.execute(mSearchWord);
    }

    private static class SurveySearchTask extends AsyncTask<String, Void, ArrayList<Survey>> {

        private List<Survey> surveys;
        private OnSearchFinished onSearchFinished;

        SurveySearchTask(List<Survey> surveys,OnSearchFinished onSearchFinished) {
            this.surveys = surveys;
            this.onSearchFinished = onSearchFinished;
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
            onSearchFinished.onFinished(surveys);
        }
    }

    private interface OnSearchFinished{
        void onFinished(List<Survey> surveys);
    }
}
