package com.example.mostafa.surveysapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mostafa.surveysapp.models.Survey;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SurveysAdapter extends RecyclerView.Adapter<SurveysAdapter.ViewHolder> {

    private List<Survey> surveys ;
    private OnClickListener onClickListener;

    public SurveysAdapter(List<Survey> surveys, OnClickListener onClickListener) {
        this.surveys = surveys;
        this.onClickListener = onClickListener;
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
        final Survey survey = surveys.get(position);
        holder.titleTextView.setText(survey.getTitle());
        holder.countTextView.setText(String.valueOf(survey.getQuestions().size()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(survey);
            }
        });
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

    public interface OnClickListener{
        public void onClick (Survey survey);
    }
}