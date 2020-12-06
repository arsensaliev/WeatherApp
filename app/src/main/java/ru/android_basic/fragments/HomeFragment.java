package ru.android_basic.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import ru.android_basic.Constants;
import ru.android_basic.MainActivity;
import ru.android_basic.R;
import ru.android_basic.adapters.RecyclerAdapter;

public class HomeFragment extends Fragment {

    private MainActivity mainActivity;

    // Parcel
    private Parcel parcel;

    // SharedPreferences
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // View
    private TextView textViewCountry;

    private RecyclerAdapter recyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mainActivity = (MainActivity) getActivity();
        sharedPreferences = getContext().getSharedPreferences(Constants.MAIN_SHARED_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (savedInstanceState != null) {
            parcel = savedInstanceState.getParcelable(Constants.CURRENT_PARCEL);
        } else {
            String locationName = sharedPreferences.getString(Constants.SHARED_COUNTRY_NAME, getResources().getStringArray(R.array.locations)[0]);
            parcel = new Parcel(locationName);
        }

        return view;
    }
}
