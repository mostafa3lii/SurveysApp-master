package com.example.mostafa.surveysapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mostafa.surveysapp.models.Survey;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SurveysAdapter extends RecyclerView.Adapter<SurveysAdapter.ViewHolder> {

    private List<Survey> surveys ;

    public SurveysAdapter(List<Survey> surveys) {
        this.surveys = surveys;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.surveys_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Survey survey = surveys.get(position);
        holder.titleTextView.setText(survey.getTitle());
        //holder.countTextView.setText(String.valueOf(survey.getQuestions().size()));
    }

    @Override
    public int getItemCount() {
        return surveys==null ? 0 : surveys.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.survey_owner_image)CircleImageView ownerImageView;
        @BindView(R.id.survey_title)TextView titleTextView;
        @BindView(R.id.questions_count)TextView countTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void addAll (List<Survey> surveys){
        this.surveys.addAll(surveys);
        notifyDataSetChanged();
    }

    public ArrayList<Survey> getMySurveys(String id) {
        ArrayList<Survey> mySurveys = new ArrayList<>();
        for (Survey survey :this.surveys) {
            if (survey.getOwnerId().equals(id))
            {
                mySurveys.add(survey);
            }
        }

        return mySurveys;
    }
}
