package com.example.lab5starter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;

public class CityArrayAdapter extends ArrayAdapter<City> {
    private ArrayList<City> cities;
    private Context context;
    private CollectionReference citiesRef;

    public CityArrayAdapter(Context context, ArrayList<City> cities, CollectionReference citiesRef){
        super(context, 0, cities);
        this.cities = cities;
        this.context = context;
        this.citiesRef = citiesRef;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.layout_city, parent, false);
        }

        City city = cities.get(position);
        TextView cityName = view.findViewById(R.id.textCityName);
        TextView cityProvince = view.findViewById(R.id.textCityProvince);
        Button deleteButton = view.findViewById(R.id.buttonDeleteCity);

        cityName.setText(city.getName());
        cityProvince.setText(city.getProvince());

        // Deletes a city
        deleteButton.setOnClickListener(v -> {
            // Removes that deleted city from a local list of cities
            cities.remove(position);
            notifyDataSetChanged();

            // Removes the city from Firestore
            citiesRef.document(city.getName())
                    .delete()
                    .addOnSuccessListener(aVoid -> Log.d("Firestore", "City successfully deleted!"))
                    .addOnFailureListener(e -> Log.e("Firestore", "Error deleting city", e));
        });

        return view;
    }
}
