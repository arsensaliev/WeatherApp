package ru.android_basic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements Filterable {

    private static final String TAG = "RecyclerAdapter";
    private static final String SELECTED_CITY = "selectedCity";

    int count = 0;
    List<String> citiesList;
    List<String> citiesListAll;
    CityActivity cityActivityContext;

    public RecyclerAdapter(List<String> citiesList, Context context) {
        this.citiesList = citiesList;
        this.citiesListAll = new ArrayList<>(citiesList);
        this.cityActivityContext = (CityActivity) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: " + count++);
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.city.setText(citiesList.get(position));
    }

    @Override
    public int getItemCount() {
        return citiesList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<String> filteredList = new ArrayList<>();
            if (charSequence.toString().isEmpty()) {
                filteredList.addAll(citiesListAll);
            } else {
                for (String city : citiesListAll) {
                    if (city.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(city);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        ;

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            citiesList.clear();
            citiesList.addAll((Collection<? extends String>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView city;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            city = itemView.findViewById(R.id.city);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intentResult = new Intent();
            intentResult.putExtra(SELECTED_CITY, citiesList.get(getAdapterPosition()));
            cityActivityContext.setResult(Activity.RESULT_OK, intentResult);
            cityActivityContext.finish();
        }
    }


}
