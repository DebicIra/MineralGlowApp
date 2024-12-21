package com.example.cosmeticsstoreapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class AddProductActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText productName, productPrice, productDescription;
    private Button btnSubmitProduct, selectImageBtn;
    private ImageView productImagePreview;

    private Bitmap selectedImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productDescription = findViewById(R.id.productDescription);
        btnSubmitProduct = findViewById(R.id.btnSubmitProduct);
        selectImageBtn = findViewById(R.id.selectImageBtn);
        productImagePreview = findViewById(R.id.productImagePreview);

        selectImageBtn.setOnClickListener(v -> openImageChooser());
        btnSubmitProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendProductToServer();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Выберите изображение"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            try {
                selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                productImagePreview.setVisibility(View.VISIBLE);
                productImagePreview.setImageBitmap(selectedImageBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendProductToServer() {
        if (selectedImageBitmap == null) {
            Toast.makeText(this, "Пожалуйста, выберите изображение", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
            String LINE_FEED = "\r\n";

            try {
                URL url = new URL(Constants.URL_ADD_IMAGES);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

                // Отправка параметров
                writeFormField(outputStream, boundary, "name", productName.getText().toString());
                writeFormField(outputStream, boundary, "price", productPrice.getText().toString());
                writeFormField(outputStream, boundary, "desc", productDescription.getText().toString());

                // Отправка файла
                writeFileField(outputStream, boundary, "image", "product_image.png", "image/png", getImageByteArray(selectedImageBitmap));

                // Завершающий boundary
                outputStream.writeBytes("--" + boundary + "--" + LINE_FEED);
                outputStream.flush();
                outputStream.close();

                // Чтение ответа сервера
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    Log.d("Response", response.toString());
                    runOnUiThread(() -> Toast.makeText(this, "Товар успешно добавлен!", Toast.LENGTH_SHORT).show());
                } else {
                    Log.e("HTTP_ERROR", "Response Code: " + responseCode);
                    runOnUiThread(() -> Toast.makeText(this, "Ошибка: " + responseCode, Toast.LENGTH_SHORT).show());
                }

                connection.disconnect();
            } catch (Exception e) {
                Log.e("UploadError", "Error: ", e);
                runOnUiThread(() -> Toast.makeText(this, "Ошибка отправки данных", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void writeFormField(DataOutputStream outputStream, String boundary, String name, String value) throws Exception {
        String LINE_FEED = "\r\n";
        outputStream.writeBytes("--" + boundary + LINE_FEED);
        outputStream.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + LINE_FEED);
        outputStream.writeBytes(LINE_FEED);
//        outputStream.writeBytes(value + LINE_FEED);
        outputStream.writeBytes(URLEncoder.encode(value, "UTF-8") + LINE_FEED);
    }

    private void writeFileField(DataOutputStream outputStream, String boundary, String name, String fileName, String mimeType, byte[] fileData) throws Exception {
        String LINE_FEED = "\r\n";

        // Генерация уникального имени файла
        String uniqueFileName = System.currentTimeMillis() + "_" + fileName;

        outputStream.writeBytes("--" + boundary + LINE_FEED);
        outputStream.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + uniqueFileName + "\"" + LINE_FEED);
        outputStream.writeBytes("Content-Type: " + mimeType + LINE_FEED);
        outputStream.writeBytes(LINE_FEED);
        outputStream.write(fileData);
        outputStream.writeBytes(LINE_FEED);
    }


    private byte[] getImageByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
