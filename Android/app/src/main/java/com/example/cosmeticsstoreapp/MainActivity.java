package com.example.cosmeticsstoreapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Проверяем, авторизован ли пользователь
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            // Если пользователь не авторизован, перенаправляем на экран авторизации
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_catalog);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        ProductAdapter adapter = new ProductAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Загрузка данных
        ProductRepository.fetchProducts(this, new ProductRepository.ProductsCallback() {
            @Override
            public void onSuccess(List<Product> products) {
                adapter.updateProducts(products); // Обновляем адаптер
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });


        /* НИЖЕ МЕНЮШКА */
        ImageButton buttonOne = findViewById(R.id.menu_button_one);
        ImageButton buttonTwo = findViewById(R.id.menu_button_two);
        ImageButton buttonThree = findViewById(R.id.menu_button_three);
        buttonTwo.setEnabled(false);
        // Установить обработчики кликов
        buttonOne.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            finish();
        });

//        buttonTwo.setOnClickListener(v -> {
//            // Ваш код для кнопки 2
//            System.out.println("Button Two clicked!");
//        });

        buttonThree.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });
    }
}
