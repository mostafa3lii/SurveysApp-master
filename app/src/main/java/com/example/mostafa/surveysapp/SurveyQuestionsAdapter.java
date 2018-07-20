package com.example.mostafa.surveysapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mostafa.surveysapp.models.Question;
import com.example.mostafa.surveysapp.models.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SurveyQuestionsAdapter extends RecyclerView.Adapter<SurveyQuestionsAdapter.ViewHolder> {

    private List<Question> questions;
    private Context context;
    private List<Question> answeredQuestions ;

    public SurveyQuestionsAdapter(List<Question> questions, Context context) {
        this.questions = questions;
        this.context = context;
        answeredQuestions = Arrays.asList(new Question[questions.size()]);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.survey_questions_list_item, parent, false);
        return new SurveyQuestionsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Question question = questions.get(position);
        holder.questionTextView.setText(question.getQuestion());
        final Question answeredQuestion = new Question();
        answeredQuestion.setType(question.getType());
        answeredQuestion.setQuestion(question.getQuestion());
        final List<String> answers = new ArrayList<>();
        answeredQuestion.setAnswers(answers);
        switch (question.getType()) {
            case 1: {
                holder.answerField.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence answer, int i, int i1, int i2) {
                        answers.clear();
                        if(!answer.toString().isEmpty()) answers.add(answer.toString());
                        answeredQuestion.setAnswers(answers);
                        answeredQuestions.set(holder.getAdapterPosition(),answeredQuestion);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                });
            }
            break;
            case 2: {
                holder.answerField.setVisibility(View.GONE);
                holder.optionsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                OnAnswerCheckListener onAnswerCheckListener = new OnAnswerCheckListener() {
                    @Override
                    public void onChecked(String answer) {
                        answers.clear();
                        answers.add(answer);
                        answeredQuestion.setAnswers(answers);
                        answeredQuestions.set(holder.getAdapterPosition(),answeredQuestion);
                    }
                };
                SingleAnswersAdapter singleAnswersAdapter = new SingleAnswersAdapter(question.getAnswers(),
                        onAnswerCheckListener);
                holder.optionsRecyclerView.setAdapter(singleAnswersAdapter);

            }
            break;
        }
    }


    @Override
    public int getItemCount() {
        return questions == null ? 0 : questions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.question)
        TextView questionTextView;
        @BindView(R.id.essay_answer)
        EditText answerField;
        @BindView(R.id.options_list)
        RecyclerView optionsRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnAnswerCheckListener {
         void onChecked(String answer);
    }


    public Result getResult()
    {
        Result result = new Result();
        result.setQuestions(answeredQuestions);
        return result;
    }
}
