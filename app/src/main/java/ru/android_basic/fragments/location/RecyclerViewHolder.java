package ru.android_basic.fragments.location;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.android_basic.R;

class RecyclerViewHolder extends RecyclerView.ViewHolder {
    TextView textView;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.city);
    }

    public TextView getTextView() {
        return textView;
    }
}