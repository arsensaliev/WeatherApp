package ru.android_basic.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.android_basic.Constants;
import ru.android_basic.MainActivity;
import ru.android_basic.R;
import ru.android_basic.fragments.location.RecyclerAdapter;
import ru.android_basic.fragments.location.LocationFragment;
import ru.android_basic.model.RequestApi;
import ru.android_basic.model.weather.WeatherRequest;

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
    private RequestApi requestApi;

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
            parcel = new Parcel(locationName, mainActivity.getCurrentCityId());
        }

        init(view);
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(Constants.CURRENT_PARCEL, parcel);
        super.onSaveInstanceState(outState);
    }

    private void init(View view) {
        textViewCountry = view.findViewById(R.id.textViewCountry);
        TextView textViewType = view.findViewById(R.id.textViewType);
        TextView textViewTemp = view.findViewById(R.id.textViewTemp);
        TextView textViewPressure = view.findViewById(R.id.textViewPress);
        TextView textViewWind = view.findViewById(R.id.textViewWind);

        ImageView imageViewEdit = view.findViewById(R.id.imageView);
        imageViewEdit.setOnClickListener(v -> mainActivity.pushFragments(new LocationFragment(), true, null));

        requestApi = new RequestApi.Builder(Constants.URL_CONNECTION, WeatherRequest.class)
                .requestType(RequestApi.Type.GET)
                .addPar(Constants.API_UNITS_NAME, Constants.API_UNITS_METRIC)
                .addPar(Constants.API_LANGUAGE_NAME, Constants.API_LANGUAGE_RU)
                .addPar(Constants.API_CITY_ID_NAME, parcel.getCityId())
                .addPar(Constants.API_KEY_NAME, Constants.API_KEY)
                .setOnRequestApiListener(new RequestApi.OnRequestApiListener() {
                    @Override
                    public void onSuccess(@Nullable Object object, int responseCode) {
                        WeatherRequest weatherRequest = (WeatherRequest) object;

                        parcel.setCityName(weatherRequest.getName());

                        textViewCountry.setText(weatherRequest.getName());

                        textViewType.setText(firstUpperCase(weatherRequest.getWeather()[0].getDescription()));

                        textViewTemp.setText(String.valueOf(weatherRequest.getMain().getTemp()));

                        textViewPressure.setText(String.valueOf(weatherRequest.getMain().getPressure()));

                        textViewWind.setText(String.valueOf(weatherRequest.getWind().getSpeed()));
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(getContext(), getString(R.string.toast_request_error), Toast.LENGTH_LONG).show();
                    }
                }).build();
    }

    public String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) return "";
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    public Parcel getCurrentParcel() {
        return parcel;
    }
}
