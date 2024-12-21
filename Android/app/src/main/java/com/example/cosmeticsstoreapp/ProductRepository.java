package com.example.cosmeticsstoreapp;

import static com.example.cosmeticsstoreapp.Constants.ROOT_URL;
import static com.example.cosmeticsstoreapp.Constants.URL_GET_PRODUCT;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    public interface ProductsCallback {
        void onSuccess(List<Product> Products);
        void onError(String errorMessage);
    }

    public static void fetchProducts(Context context, ProductsCallback callback) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL_GET_PRODUCT,
                null,
                response -> {
                    try {
                        List<Product> Products = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject productJson = response.getJSONObject(i);

                            String id = productJson.getString("id");

                            String name = productJson.getString("name");
                            String price = productJson.getString("price");
                            String desc = productJson.getString("description");
                            String composition = productJson.getString("composition");
                            String imageUrl = productJson.getString("image_src"); // URL картинки

                            Products.add(new Product(name, price, desc, composition, ROOT_URL + imageUrl, Integer.parseInt(id)));
                        }
                        callback.onSuccess(Products);
                    } catch (JSONException e) {
                        callback.onError("Ошибка парсинга данных: " + e.getMessage());
                        Log.e("AdminActivity", "Error JSON: " + e.getMessage());
                    }
                },
                error -> Log.e("AdminActivity", "Error JSON: " + error.getMessage())
        );
        Log.i("AdminActivity", "Error JSON: " + jsonArrayRequest);
        NetworkHandler.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }
}
