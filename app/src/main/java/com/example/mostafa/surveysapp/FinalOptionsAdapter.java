package com.example.mostafa.surveysapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FinalOptionsAdapter extends RecyclerView.Adapter<FinalOptionsAdapter.ViewHolder> {

    private List<String> options;

    public FinalOptionsAdapter(List<String> options) {
        this.options = options;
    }

    @NonNull
    @Override
    public FinalOptionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.final_option_list_item, parent, false);
        return new FinalOptionsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FinalOptionsAdapter.ViewHolder holder, int position) {

        holder.optionText.setText(options.get(position));

    }

    @Override
    public int getItemCount() {
        return options==null ? 0 : options.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.option)TextView optionText;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
