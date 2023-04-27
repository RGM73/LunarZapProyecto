package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Principal extends AppCompatActivity {
    TextView textView2,textView3;
    long diffSeconds;
    String horaFormateada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        diffSeconds=0;
        setContentView(R.layout.activity_principal);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.sunrise-sunset.org/json?lat=40.4165&lng=-3.70256&date=today";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject results = response.getJSONObject("results");
                            String sunset = results.getString("sunset");
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm:ss a");
                            LocalTime time = LocalTime.parse(sunset, formatter);
                            LocalTime horaMas2 = time.plusHours(2);
                             horaFormateada = horaMas2.format(DateTimeFormatter.ofPattern("HH:mm:ss a"));
                            textView3.setText(horaFormateada);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here
                        Log.e("Error", error.getMessage());
                    }
                });

        queue.add(jsonObjectRequest);

        new CountDownTimer( 1000*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                long secondsUntilFinished = millisUntilFinished / 1000;
                long minutesUntilFinished = secondsUntilFinished / 60;
                long hoursUntilFinished = minutesUntilFinished / 60;
                long minutesRemaining = minutesUntilFinished % 60;
                long secondsRemaining = secondsUntilFinished % 60;

                String countdown = String.format("%02d:%02d:%02d", hoursUntilFinished, minutesRemaining, secondsRemaining);
                textView2.setText(countdown);
            }

            public void onFinish() {
                textView2.setText("¡Ya ha anochecido!");
            }
        }.start();


    }
}