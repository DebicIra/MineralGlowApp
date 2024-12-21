package com.example.cosmeticsstoreapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    Button logoutBtn;
    TextView funnyText;
    String roleText;
    Button addProductBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        funnyText = findViewById(R.id.FunnyText);
        logoutBtn = findViewById(R.id.logoutBtn);
        addProductBtn = findViewById(R.id.addProductBtn);

        logoutBtn.setOnClickListener(view -> logout());

        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        int role = sharedPreferences.getInt("role", 2);
        if (role == 1) {
            roleText = "Администратор!";
            addProductBtn.setVisibility(View.VISIBLE); // Показываем кнопку только для админа
            addProductBtn.setOnClickListener(v -> {
                Intent intent = new Intent(this, AddProductActivity.class);
                startActivity(intent);
            });
        } else if (role == 2) {
            roleText = "Обычный пользователь!";
        } else {
            roleText = "Хороший человек";
        }
        String userName = sharedPreferences.getString("username", "Гость");
        funnyText.setText("Уважаемый " + userName + ", Вы - " + roleText);

        // Нижнее меню
        ImageButton buttonOne = findViewById(R.id.menu_button_one);
        ImageButton buttonTwo = findViewById(R.id.menu_button_two);
        ImageButton buttonThree = findViewById(R.id.menu_button_three);
        buttonOne.setEnabled(false);

        buttonTwo.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        buttonThree.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false); // Сбрасываем флаг авторизации
        editor.remove("username"); // Удаляем имя пользователя, если оно сохранено
        editor.apply();

        // Возвращаем пользователя на экран авторизации
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
