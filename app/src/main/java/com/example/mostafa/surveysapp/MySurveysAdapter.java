package com.example.mostafa.surveysapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mostafa.surveysapp.models.Survey;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MySurveysAdapter extends RecyclerView.Adapter<MySurveysAdapter.ViewHolder> {

    private List<Survey> mySurveys;

    public MySurveysAdapter(List<Survey> mySurveys) {
        this.mySurveys = mySurveys;
    }

    @NonNull
    @Override
    public MySurveysAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_surveys_list_item, parent, false);
        return new MySurveysAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MySurveysAdapter.ViewHolder holder, int position) {
        Survey survey = mySurveys.get(position);
        holder.titleTextView.setText(survey.getTitle());
        holder.questionsCountTextView.setText(String.valueOf(survey.getQuestions().size()));
    }

    @Override
    public int getItemCount() {
        return mySurveys==null ? 0 : mySurveys.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.survey_title)TextView titleTextView;
        @BindView(R.id.pin)ImageButton pinButton;
        @BindView(R.id.questions_count)TextView questionsCountTextView;
        @BindView(R.id.results_count)TextView resultsCountTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


    public void clear ()
    {
        mySurveys.clear();
        notifyDataSetChanged();
    }
    public void add(List<Survey> surveys)
    {
        mySurveys.addAll(surveys);
        notifyDataSetChanged();
    }
}
