package ru.android_basic.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.android_basic.MainActivity;
import ru.android_basic.R;

public class SettingsFragment extends Fragment {
    private MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        mainActivity = (MainActivity) getActivity();

        init(view);
        return view;
    }

    private void init(View view) {
        RadioGroup radioGroupTheme = view.findViewById(R.id.radioGroupTheme);
        RadioButton radioButtonDark = view.findViewById(R.id.radioButtonDark);
        RadioButton radioButtonLight = view.findViewById(R.id.radioButtonLight);

        radioButtonDark.setChecked(mainActivity.isDarkTheme());

        radioGroupTheme.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButtonDark:
                        mainActivity.setDarkTheme(true);
                        break;
                    case R.id.radioButtonLight:
                        mainActivity.setDarkTheme(false);
                        break;
                }
            }
        });
    }
}
