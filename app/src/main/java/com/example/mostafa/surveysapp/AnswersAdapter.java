package com.example.mostafa.surveysapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.ViewHolder>{


    private List<String> answers;
    private int type;
    private ArrayList<String> selectedAnswers;
    private ArrayList<Integer> selectedPositions;

    public AnswersAdapter(List<String> answers, int type, ArrayList<String> selectedAnswers, ArrayList<Integer> selectedPositions) {
        this.answers = answers;
        this.type = type;
        this.selectedAnswers = selectedAnswers;
        this.selectedPositions = selectedPositions;
    }

    @NonNull
    @Override
    public AnswersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.answer_list_item, parent, false);
        return new AnswersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AnswersAdapter.ViewHolder holder, int position) {

        switch (type)
        {
            case 2:
                updateUI(holder.radioButton,holder.checkBox,holder.getAdapterPosition());
                break;

            case 3:
                updateUI(holder.checkBox,holder.radioButton,holder.getAdapterPosition());
                break;
        }
    }


    private void updateUI (CompoundButton usedButton , CompoundButton unusedButton, final int position){
        final String answer = answers.get(position);
        unusedButton.setVisibility(View.GONE);
        usedButton.setVisibility(View.VISIBLE);
        usedButton.setText(answer);
        usedButton.setChecked(selectedPositions.contains(position));
        usedButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    if(type == 2) {
                        selectedPositions.clear();
                        selectedAnswers.clear();
                    }
                    selectedAnswers.add(answer);
                    selectedPositions.add(position);
                    notifyDataSetChanged();
                }else {
                    if(type==3)
                    {
                        selectedPositions.remove(Integer.valueOf(position));
                        selectedAnswers.remove(answer);
                    }
                }
            }
        });
    }

    public ArrayList<String> getSelectedAnswers ()
    {
        return selectedAnswers;
    }
    public ArrayList<Integer> getSelectedPositions (){return selectedPositions;}

    @Override
    public int getItemCount() {
        return answers==null ? 0 : answers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.radio)RadioButton radioButton;
        @BindView(R.id.checkbox)CheckBox checkBox;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


}