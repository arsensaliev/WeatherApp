package ru.android_basic.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.android_basic.R;

class RecyclerViewHolder extends RecyclerView.ViewHolder {
    TextView view;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView.findViewById(R.id.city);
    }

    public TextView getView() {
        return view;
    }
}