package ru.android_basic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import ru.android_basic.fragments.AboutFragment;
import ru.android_basic.fragments.HomeFragment;
import ru.android_basic.fragments.LocationFragment;
import ru.android_basic.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    // Time
    private long mLastClickTime;

    // Fragments
    private FragmentManager fragmentManager;
    private LocationFragment locationFragment;

    // Tab
    private String currentTab = TAB_MAIN;
    private static final String TAB_MAIN = "tab_home";
    private static final String TAB_ABOUT = "tab_about";
    private static final String TAB_SETTINGS = "tab_settings";

    // Navigation
    private BottomNavigationView navView;

    // SharedPreferences
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Parcel
    private MainParcel mainParcel;

    private Map<String, Stack<Fragment>> mStacks;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        // SharedPreferences
        sharedPreferences = getSharedPreferences(Constants.MAIN_SHARED_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Views
        navView = findViewById(R.id.nav_view);

        if (savedInstanceState != null) {
            mainParcel = savedInstanceState.getParcelable(Constants.CURRENT_PARCEL);
            setCurrentTab(mainParcel.getCurrentTab());
        } else {
            mainParcel = new MainParcel(false, TAB_MAIN);
        }

        if (sharedPreferences.getBoolean(Constants.SHARED_THEME_IS_DARK, false) &&
                AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES) {
            setDarkTheme(true);
        } else if (!sharedPreferences.getBoolean(Constants.SHARED_THEME_IS_DARK, false) &&
                AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_NO) {
            setDarkTheme(false);
        }

        init();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putParcelable(Constants.CURRENT_PARCEL, mainParcel);

        super.onSaveInstanceState(outState);
    }

    private void init() {
        if (mStacks == null) {
            mStacks = new HashMap<String, Stack<Fragment>>();
            mStacks.put(TAB_MAIN, new Stack<Fragment>());
            mStacks.put(TAB_ABOUT, new Stack<Fragment>());
            mStacks.put(TAB_SETTINGS, new Stack<Fragment>());
        }

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                    return false;
                }

                mLastClickTime = SystemClock.elapsedRealtime();

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        setCurrentTab(TAB_MAIN);
                        break;
                    case R.id.navigation_about:
                        setCurrentTab(TAB_ABOUT);
                        break;
                    case R.id.navigation_settings:
                        setCurrentTab(TAB_SETTINGS);
                        break;
                }

                mainParcel.setCurrentTab(getCurrentTab());
                selectedTab();

                return true;
            }
        });

        int id = 0;
        switch (getCurrentTab()) {
            case TAB_MAIN:
                id = R.id.navigation_home;
                break;
            case TAB_ABOUT:
                id = R.id.navigation_about;
                break;
            case TAB_SETTINGS:
                id = R.id.navigation_settings;
                break;
        }

        navView.setSelectedItemId(id);

        navView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if (mStacks.get(getCurrentTab()).size() > 1) {
                    mStacks.get(getCurrentTab()).subList(1, mStacks.get(getCurrentTab()).size()).clear();
                    selectedTab();
                }
            }
        });
    }

    private void selectedTab() {
        if (mStacks.get(getCurrentTab()).empty()) {

            Fragment fragment = null;

            switch (getCurrentTab()) {
                case TAB_MAIN:
                    fragment = new HomeFragment();
                    break;
                case TAB_ABOUT:
                    fragment = new AboutFragment();
                    break;
                case TAB_SETTINGS:
                    fragment = new SettingsFragment();
                    break;
            }
            pushFragments(fragment, true, null);
        } else {
            pushFragments(mStacks.get(getCurrentTab()).lastElement(), false, null);
        }
    }

    public void pushFragments(Fragment fragment, boolean addToBS, Bundle bundle) {
        if (addToBS) {
            mStacks.get(getCurrentTab()).push(fragment);
        }

        if (bundle != null) {
            fragment.setArguments(bundle);
        }

        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        ft.replace(R.id.container, fragment);
        ft.commit();
    }

    public String getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(String tab) {
        currentTab = tab;
    }

    public void setDarkTheme(boolean isDark) {
        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        }
        editor.putBoolean(Constants.SHARED_THEME_IS_DARK, isDark);
        editor.commit();
        //recreate();
    }

    public boolean isDarkTheme() {
        return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
    }

}