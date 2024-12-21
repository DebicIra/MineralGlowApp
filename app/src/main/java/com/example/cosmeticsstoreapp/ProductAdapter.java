package com.example.cosmeticsstoreapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final List<Product> productList;
    private final Context context;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText(product.getPrice());

        // Загрузка изображения через Glide
        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.capy)
                .error(R.drawable.capy)
                .into(holder.productImage);

        // Добавление товара в корзину
        holder.btnAddToCart.setOnClickListener(v -> {
            CartManager.getInstance().addProductToCart(product);
            Toast.makeText(context, "Товар добавлен в корзину", Toast.LENGTH_SHORT).show();
        });

        // Открытие детальной карточки
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("product_name", product.getName());
            intent.putExtra("product_price", product.getPrice());
            intent.putExtra("product_image_url", product.getImageUrl());
            intent.putExtra("product_description", product.getDesc());
            intent.putExtra("product_composition", product.getComposition());
            intent.putExtra("product_id", product.getId());
            context.startActivity(intent);
        });
    }



    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice;
        Button btnAddToCart; // Добавляем кнопку

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            btnAddToCart = itemView.findViewById(R.id.btn_add_to_cart); // Инициализируем кнопку
        }
    }

    public void updateProducts(List<Product> newProducts) {
        productList.clear();
        productList.addAll(newProducts);
        notifyDataSetChanged();
    }

}
