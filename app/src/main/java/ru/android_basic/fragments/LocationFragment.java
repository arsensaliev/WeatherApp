package ru.android_basic.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.android_basic.Constants;
import ru.android_basic.MainActivity;
import ru.android_basic.R;
import ru.android_basic.adapters.RecyclerAdapter;

public class LocationFragment extends Fragment {
    private static final String SELECTED_LOCATION = "location";

    private List<String> data;
    private MainActivity mainActivity;
    private SharedPreferences sharedPreferences;
    private String mParam1;
    private SharedPreferences.Editor editor;
    private Parcel currentParcel;
    private RecyclerAdapter recyclerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(SELECTED_LOCATION);
        }

        data = new ArrayList<>();
        data.addAll(Arrays.asList(
                getResources().getStringArray(R.array.locations)
        ));
    }

    public Parcel getCurrentParcel() {
        return currentParcel;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(Constants.CURRENT_PARCEL, currentParcel);
        super.onSaveInstanceState(outState);
    }

    private void init(View view) {
        initRecyclerView(view);
    }

    private RecyclerAdapter initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new RecyclerAdapter(data));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerAdapter = new RecyclerAdapter(data);
        return recyclerAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);

        mainActivity = (MainActivity) getActivity();
        sharedPreferences = getContext().getSharedPreferences(Constants.MAIN_SHARED_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (savedInstanceState != null) {
            currentParcel = (Parcel) savedInstanceState.getParcelable(Constants.CURRENT_PARCEL);
        } else {
            String location = sharedPreferences.getString(Constants.SHARED_COUNTRY_NAME, getResources().getStringArray(R.array.locations)[0]);
            currentParcel = new Parcel(location);
        }

        init(view);
        return view;
    }
}