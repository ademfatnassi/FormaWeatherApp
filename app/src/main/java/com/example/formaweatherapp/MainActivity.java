package com.example.formaweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    EditText searchBar;
    ImageButton searchBtn;
    ImageView weatherIcon;
    TextView temp, description, city, humidity, pressure;
    String YOUR_API_KEY = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchBar = findViewById(R.id.search_bar);
        searchBtn = findViewById(R.id.search_btn);

        weatherIcon = findViewById(R.id.ic_weather);
        temp = findViewById(R.id.temp);
        description = findViewById(R.id.description);
        city = findViewById(R.id.city);
        humidity = findViewById(R.id.humidity);
        pressure = findViewById(R.id.pressure);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityName = searchBar.getText().toString();
                cityName = cityName.replace(" ","%20");
                Ion.with(MainActivity.this)
                        .load("http://api.openweathermap.org/data/2.5/weather?q="+cityName+"&APPID="+YOUR_API_KEY)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                if(e == null){
                                    String str_cod = result.get("cod").getAsString();
                                    if (str_cod.equals("200")){
                                        String iconStr = result.get("weather").getAsJsonArray().get(0).getAsJsonObject().get("icon").getAsString();
                                        String descriptionStr = result.get("weather").getAsJsonArray().get(0).getAsJsonObject().get("description").toString();
                                        String tempStr = result.get("main").getAsJsonObject().get("temp").toString();
                                        String pressureStr = result.get("main").getAsJsonObject().get("pressure").toString();
                                        String humidityStr = result.get("main").getAsJsonObject().get("humidity").toString();

                                        String countryStr = result.get("sys").getAsJsonObject().get("country").toString();
                                        String cityStr = result.get("name").getAsString();
                                        Toast.makeText(MainActivity.this, "WORK WORK WORK ...", Toast.LENGTH_SHORT).show();

                                        double tempCelsus = Double.parseDouble(tempStr) - 273.15;
                                        String tempp = new DecimalFormat("#.##").format(tempCelsus);

                                        description.setText(descriptionStr);
                                        temp.setText(tempp);
                                        pressure.setText(pressureStr);
                                        humidity.setText(humidityStr);
                                        city.setText(String.format("%s, %s", cityStr, countryStr));
                                        Picasso.get().load("http://openweathermap.org/img/wn/"+iconStr+"@2x.png").into(weatherIcon);

                                    }else{
                                        Toast.makeText(MainActivity.this, "Error : Can't resolve these error: code 6ix9ine (type miss touch)", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    Toast.makeText(MainActivity.this, "Error : 404 NOT FOUND "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
}