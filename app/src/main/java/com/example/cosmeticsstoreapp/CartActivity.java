package com.example.cosmeticsstoreapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CartActivity extends AppCompatActivity {

    private CartAdapter cartAdapter;
    private TextView totalPriceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartAdapter = new CartAdapter(this, CartManager.getInstance().getCartProducts());
        recyclerView.setAdapter(cartAdapter);

        totalPriceText = findViewById(R.id.total_amount);
        Button btnOrder = findViewById(R.id.btn_order);

        updateTotalPrice();

        btnOrder.setOnClickListener(v -> {
            Toast.makeText(this, "Заказ оформлен!", Toast.LENGTH_SHORT).show();
            CartManager.getInstance().clearCart();
            cartAdapter.notifyDataSetChanged();
            updateTotalPrice();
        });


        /* НИЖЕ МЕНЮШКА */
        ImageButton buttonOne = findViewById(R.id.menu_button_one);
        ImageButton buttonTwo = findViewById(R.id.menu_button_two);
        ImageButton buttonThree = findViewById(R.id.menu_button_three);
        buttonThree.setEnabled(false);
        // Установить обработчики кликов
        buttonOne.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });

        buttonTwo.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

//        buttonThree.setOnClickListener(v -> {
//            Intent intent = new Intent(this, CartActivity.class);
//            startActivity(intent);
//        });
    }

    private void updateTotalPrice() {
        int totalPrice = CartManager.getInstance().getTotalPrice();
        totalPriceText.setText(totalPrice + " руб.");
    }

}
