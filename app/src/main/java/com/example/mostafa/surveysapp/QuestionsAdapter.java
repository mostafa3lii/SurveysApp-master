package com.example.mostafa.surveysapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mostafa.surveysapp.models.Question;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder>{

    private ArrayList<Question> questions;
    private Context context;

    public QuestionsAdapter(ArrayList<Question> questions, Context context) {
        this.questions = questions;
        this.context = context;
    }

    @NonNull
    @Override
    public QuestionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.questions_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionsAdapter.ViewHolder holder, int position) {
        Question question = questions.get(position);
        holder.questionTextView.setText(question.getQuestion());
        FinalOptionsAdapter finalOptionsAdapter = new FinalOptionsAdapter(question.getOptions());
        holder.optionsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.optionsRecyclerView.setAdapter(finalOptionsAdapter);
    }

    @Override
    public int getItemCount() {
        return questions==null ? 0 : questions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.question)TextView questionTextView;
        @BindView(R.id.options_list) RecyclerView optionsRecyclerView;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void add (Question question)
    {
        questions.add(question);
        notifyDataSetChanged();
    }

    public ArrayList<Question> getQuestions()
    {
        return questions;
    }

    public void add(ArrayList<Question> questions){
        this.questions.clear();
        this.questions.addAll(questions);
        notifyDataSetChanged();
    }
}
