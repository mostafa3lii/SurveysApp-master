package com.example.mostafa.surveysapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SingleAnswersAdapter extends RecyclerView.Adapter<SingleAnswersAdapter.ViewHolder> {

    private List<String> answers;
    private int checkedPosition = -1;
    private SurveyQuestionsAdapter.OnAnswerCheckListener onAnswerCheckListener;

    public SingleAnswersAdapter(List<String> answers, SurveyQuestionsAdapter.OnAnswerCheckListener onAnswerCheckListener) {
        this.answers = answers;
        this.onAnswerCheckListener = onAnswerCheckListener;
    }

    @NonNull
    @Override
    public SingleAnswersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_answer_list_item, parent, false);
        return new SingleAnswersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SingleAnswersAdapter.ViewHolder holder, int position) {
        final String answer = answers.get(position);
        holder.answerButton.setChecked(position == checkedPosition);
        holder.answerButton.setText(answer);
    }

    @Override
    public int getItemCount() {
        return answers == null ? 0 : answers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.answer)RadioButton answerButton;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            answerButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    if(checked) {
                        checkedPosition = getAdapterPosition();
                        onAnswerCheckListener.onChecked(answers.get(getAdapterPosition()));
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

}
