package ru.android_basic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class CityActivity extends AppCompatActivity {
    private static final String SELECTED_CITY = "selectedCity";
    private static String city;

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;

    private List<String> citiesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        toolbar = findViewById(R.id.cityToolbar);
        setSupportActionBar(toolbar);

        citiesList = new ArrayList<>();
        addCitiesInArrayList(citiesList);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerAdapter = new RecyclerAdapter(citiesList);
        recyclerView.setAdapter(recyclerAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.city_menu, menu);
        MenuItem item = menu.findItem(R.id.citySettings);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void finishActivityWithResult(String city) {
        Intent intentResult = new Intent();
        intentResult.putExtra(SELECTED_CITY, city);
        setResult(RESULT_OK, intentResult);
        finish();
    }

    public static void addCitiesInArrayList(List<String> arrayList) {
        arrayList.add("Тюмень");
        arrayList.add("Грозный");
        arrayList.add("Казань");
        arrayList.add("Санкт-Петербург");
        arrayList.add("Краснодар");
        arrayList.add("Уфа");
        arrayList.add("Новосибирск");
        arrayList.add("Москва");
        arrayList.add("Красноярск");
        arrayList.add("Кемерово");
        arrayList.add("Екатеринбург");
        arrayList.add("Нижний Новгород");
        arrayList.add("Самара");
        arrayList.add("Челябинск");
        arrayList.add("Омск");
        arrayList.add("Ростов-на-Дону");
        arrayList.add("Уфа");
        arrayList.add("Пермь");
        arrayList.add("Волгоград");
        arrayList.add("Воронеж");
        arrayList.add("Саратов");
        arrayList.add("Тольятти");
        arrayList.add("Ижевск");
        arrayList.add("Иркутск");
        arrayList.add("Оренбург");
        arrayList.add("Сочи");
    }
}