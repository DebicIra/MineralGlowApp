package com.example.cosmeticsstoreapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProductDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        boolean isAdmin = false;
        ImageView productImage = findViewById(R.id.product_detail_image);
        TextView productName = findViewById(R.id.product_detail_name);
        TextView productPrice = findViewById(R.id.product_detail_price);
        TextView productDescription = findViewById(R.id.product_detail_description);
        TextView productComposition = findViewById(R.id.product_detail_composition);
        Button btnDeleteProduct = findViewById(R.id.btn_delete_product);
        Button btnAddToCart = findViewById(R.id.btn_add_to_cart);

        // Получение данных из Intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("product_name");
        String price = intent.getStringExtra("product_price");
        String description = intent.getStringExtra("product_description");
        String composition = intent.getStringExtra("product_composition");
        String imageUrl = intent.getStringExtra("product_image_url");
        int id = intent.getIntExtra("product_id", -1);
//        Toast.makeText(getApplicationContext(), String.valueOf(id), Toast.LENGTH_SHORT).show();
        productName.setText(name);
        productPrice.setText(price + " руб.");
        productDescription.setText(description);
        productComposition.setText(composition);
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        int role = sharedPreferences.getInt("role", 2);
        // Загрузка изображения через Glide
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.capy)
                .error(R.drawable.capy)
                .into(productImage);
        if (role == 1) {
            isAdmin = true;
        }
        // Логика отображения кнопок

        if (isAdmin) {
            btnDeleteProduct.setVisibility(View.VISIBLE);
        }

        // Логика для кнопок
        btnDeleteProduct.setOnClickListener(v -> {
            // Логика удаления товара
            deleteProduct(id); // Реализовать метод удаления
        });

        btnAddToCart.setOnClickListener(v -> {
            // Логика добавления товара в корзину
            CartManager.getInstance().addProductToCart(new Product(name, price, description, composition, imageUrl, id));
            Toast.makeText(this, "Товар добавлен в корзину", Toast.LENGTH_SHORT).show();
        });
    }

    private void deleteProduct(int productId) {
        // Создаем запрос на удаление
        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_DELETE_PRODUCT,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        String message = jsonObject.getString("message");

                        if (success) {
                            Toast.makeText(this, "Товар удален успешно!" + message, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "Ошибка: " + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.w("DELETE RESPONSE", response);
                        Toast.makeText(this, "Ошибка обработки ответа", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "Ошибка сети: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(productId));
                return params;
            }
        };

        // Добавляем запрос в очередь
        Volley.newRequestQueue(this).add(request);
    }

}
