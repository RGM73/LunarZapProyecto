package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Home extends Fragment {
    TextView textView2, textView3;
    long diffSeconds;
    String horaFormateada;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_principal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        diffSeconds = 0;
        textView2 = getView().findViewById(R.id.textView2);
        textView3 = getView().findViewById(R.id.textView3);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
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
                            horaFormateada = horaMas2.format(formatter);
                            textView3.setText(horaFormateada);

                            LocalTime now = LocalTime.now();
                            formatter = DateTimeFormatter.ofPattern("h:mm:ss a");
                            LocalTime anochecer = null;
                            if (horaFormateada != null && !horaFormateada.isEmpty()) {
                                anochecer = LocalTime.parse(horaFormateada, formatter);
                            } else {
                                LocalTime hora = LocalTime.of(22, 30);
                                anochecer = hora;
                            }
                            long diffSeconds = ChronoUnit.SECONDS.between(now, anochecer);
                            new CountDownTimer(diffSeconds * 1000, 1000) {

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

    }

}