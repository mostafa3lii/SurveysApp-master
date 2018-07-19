package com.example.mostafa.surveysapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.ViewHolder>{

    private ArrayList<String> options;

    public OptionsAdapter(ArrayList<String> options) {
        this.options = options;
    }

    @NonNull
    @Override
    public OptionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.option_list_item, parent, false);
        return new OptionsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OptionsAdapter.ViewHolder holder, int position) {

        holder.optionField.setText(options.get(position));
        holder.optionField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence option, int start, int before, int count) {
                options.set(holder.getAdapterPosition(), option.toString());
            }

            @Override
            public void afterTextChanged(Editable answer) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return options==null ? 0 : options.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.option)EditText optionField;
         public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public ArrayList<String> getOptions ()
    {
        return this.options;
    }
    public void add(String option){
        options.add(option);
        notifyDataSetChanged();
    }

    public void removeOption()
    {
        if(options.size()>2) {
            options.remove(options.size() - 1);
            notifyDataSetChanged();
        }
    }

    public void add(ArrayList<String> options)
    {
        this.options.clear();
        this.options.addAll(options);
        notifyDataSetChanged();
    }
}
