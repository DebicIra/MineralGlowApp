package com.example.cosmeticsstoreapp;

import static com.example.cosmeticsstoreapp.Constants.URL_LOGIN;
import static com.example.cosmeticsstoreapp.Constants.URL_REGISTER;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    private EditText loginInputReg, passInputReg;
    private Button regBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        loginInputReg = findViewById(R.id.loginInputReg);
        passInputReg = findViewById(R.id.passInputReg);
        regBtn = findViewById(R.id.regBtn);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = loginInputReg.getText().toString().trim();
                String password = passInputReg.getText().toString().trim();

                if (!username.isEmpty() && !password.isEmpty()) {
                    registerUser(username, password);
                } else {
                    Toast.makeText(RegistrationActivity.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerUser(final String username, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("success")) {
                                Toast.makeText(RegistrationActivity.this, "Регистрация прошла успешно", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                finish();
                                // Переход к авторизации или главной странице
                            } else {
                                String message = jsonObject.getString("message");
                                Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_SHORT).show();
                                Log.e("RegError", response);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("RegError", response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(RegistrationActivity.this, "Ошибка подключения", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }
}
