package com.example.mostafa.surveysapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mostafa.surveysapp.models.Survey;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SurveysAdapter extends RecyclerView.Adapter<SurveysAdapter.ViewHolder> {

    private List<Survey> surveys ;
    private OnClickListener onClickListener;
    private Context context;

    public SurveysAdapter(List<Survey> surveys, OnClickListener onClickListener, Context context) {
        this.surveys = surveys;
        this.onClickListener = onClickListener;
        this.context = context;
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
        holder.countTextView.setText(context.getString(R.string.question_count,survey.getQuestions().size()));
        Picasso.get().load(survey.getOwnerPic()).into(holder.ownerImageView);
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
        this.surveys.clear();
        this.surveys.addAll(surveys);
        notifyDataSetChanged();
    }

    public interface OnClickListener{
         void onClick (Survey survey);
    }
}