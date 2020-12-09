package ru.android_basic.fragments.location;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import ru.android_basic.Constants;
import ru.android_basic.MainActivity;
import ru.android_basic.R;
import ru.android_basic.fragments.Parcel;
import ru.android_basic.model.city.CitiesModel;
import ru.android_basic.model.city.CityModel;

public class LocationFragment extends Fragment {
    private static final String TAG = "LOCATION_FRAGMENT";

    private List<CityItem> cityItems;
    private List<CityModel> cityModelList;
    private MainActivity mainActivity;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Parcel currentParcel;
    private RecyclerAdapter recyclerAdapter;


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

        EditText editTextCountry = view.findViewById(R.id.editTextCountry);

        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton);


        editTextCountry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                recyclerAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        floatingActionButton.setOnClickListener(v -> mainActivity.onBackPressed());
    }

    private RecyclerAdapter initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        Gson gson = new Gson();
        cityModelList = gson.fromJson(getCities(), CitiesModel.class).getList();

        cityItems = new ArrayList<>();
        for (int i = 0; i < cityModelList.size(); i++) {
            if (cityModelList.get(i).getCountry().equals("KZ")) {
                cityItems.add(new CityItem(cityModelList.get(i).getName(), cityModelList.get(i).getId()));
            }
        }

        recyclerAdapter = new RecyclerAdapter(cityItems);
        recyclerView.setAdapter(recyclerAdapter);

        return recyclerAdapter;
    }

    private String getCities() {
        StringBuilder buf = new StringBuilder();
        InputStream json = null;
        try {
            json = getContext().getAssets().open(Constants.FILE_CITIES_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(json, StandardCharsets.UTF_8));
        String str = null;

        while (true) {
            try {
                if ((str = in.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            buf.append(str);
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buf.toString();
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
//            currentParcel = new Parcel(location, );
        }

        init(view);
        return view;
    }
}