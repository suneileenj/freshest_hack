package com.example.madeline.fridgefinder.helperClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.listprototype.R;

import java.util.ArrayList;

public class ItemsAdapter extends ArrayAdapter<FoodEntry> {

    public ItemsAdapter(Context context, ArrayList<FoodEntry> foods){
        super(context, 0 , foods);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        FoodEntry food = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_food, parent, false);
        }
        // Lookup view for data population
        TextView dateAdded = (TextView) convertView.findViewById(R.id.date_added);
        TextView foodName = (TextView) convertView.findViewById(R.id.food_name);
        // Populate the data into the template view using the data object
        dateAdded.setText(food.dateAdded);
        foodName.setText(food.foodName);
        // Return the completed view to render on screen
        return convertView;
    }
}
