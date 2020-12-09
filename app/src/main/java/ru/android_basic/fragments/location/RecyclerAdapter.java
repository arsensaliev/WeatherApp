package ru.android_basic.fragments.location;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.android_basic.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> implements Filterable {
    private static final String TAG = "RecyclerAdapter";

    int count = 0;
    List<CityItem> cityItemList;
    List<CityItem> cityItemListAll;

    public RecyclerAdapter(List<CityItem> cityItemList) {
        this.cityItemList = cityItemList;
        this.cityItemListAll = cityItemList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: " + count++);

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.row_item;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        CityItem cityItem = cityItemList.get(position);
        holder.getTextView().setText(cityItem.getCityName());
    }

    @Override
    public int getItemCount() {
        return cityItemList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<CityItem> filteredList = new ArrayList<>();

            if (charSequence.toString().isEmpty()) {
                filteredList.addAll(cityItemListAll);
            } else {
                for (CityItem item : cityItemListAll
                ) {
                    if (item.getCityName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            Log.d(TAG, String.valueOf(filteredList));
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            cityItemList.clear();
            cityItemList.addAll((Collection<? extends CityItem>) results.values);
            notifyDataSetChanged();
        }
    };
}
