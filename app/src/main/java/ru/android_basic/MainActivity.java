package ru.android_basic;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ru.android_basic.fragments.LocationFragment;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private LocationFragment locationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationFragment = new LocationFragment();
        fragmentManager = getSupportFragmentManager();

        pushFragment(locationFragment, false, null);
    }

    public void pushFragment(Fragment fragment, boolean addToBS, Bundle bundle) {
        if (bundle != null) {
            fragment.setArguments(bundle);
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(R.id.container, fragment);

        if (addToBS) {
            fragmentTransaction.addToBackStack(Constants.EMPTY_STRING);
        }

        fragmentTransaction.commit();
    }

    public void changeLocation(String location) {
    }
}