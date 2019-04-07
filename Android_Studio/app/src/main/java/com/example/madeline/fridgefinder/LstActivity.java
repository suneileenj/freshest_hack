package com.example.madeline.fridgefinder;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;


import com.example.madeline.fridgefinder.db.ItemContract;
import com.example.madeline.fridgefinder.db.ItemDbHelper;

import java.util.ArrayList;



public class LstActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private ItemDbHelper mHelper;
    private ListView mItemListView;
    private ItemsAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lst);


        //helper for database
        mHelper = new ItemDbHelper(this);
        //initialize viewer for list
        mItemListView = findViewById(R.id.list_food);

        mHelper = new ItemDbHelper(this);
        updateUI();

//        mTextMessage = (TextView) findViewById(R.id.message);
//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_recents:
                        Toast.makeText(LstActivity.this, "Recents", Toast.LENGTH_SHORT).show();
                        Intent activity2Intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(activity2Intent);
                        break;
                    case R.id.action_favorites:
                        Intent activity3Intent = new Intent(getApplicationContext(), LstActivity.class);
                        startActivity(activity3Intent);
                        break;
                }
                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * handles when a new item is added to the list
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_add_item:
                final EditText itemEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add a new item")
                        .setMessage("What food would you like to add?")
                        .setView(itemEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.N)
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //get input, create new FoodEntry
                                String item = String.valueOf(itemEditText.getText());
                                //get current date
                                String date = FoodEntry.currentDate();
                                //get database
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                //put item into database
                                values.put(ItemContract.ItemEntry.COL_ITEM_TITLE, item);
                                //put date into database
                                values.put(ItemContract.ItemEntry.COL_ITEM_DATE, date);

                                db.insertWithOnConflict(ItemContract.ItemEntry.TABLE,
                                        null,
                                        values,
                                        SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();
                                updateUI();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * method that deletes an item when the delete button is
     * pressed
     * */
    public void deleteItem(View view){
        View parent = (View) view.getParent();
        TextView itemTextView = (TextView) parent.findViewById(R.id.food_name);
        String item = String.valueOf(itemTextView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        //gets the name of the item and deletes it from the database
        db.delete(ItemContract.ItemEntry.TABLE,
                ItemContract.ItemEntry.COL_ITEM_TITLE + " = ?",
                new String[]{item});
        db.close();
        //updates the UI
        updateUI();
    }
    /**
     * helper method to update UI
     * */
    private void updateUI(){
        /*create a new class that contains each food and use instead of string*/
        ArrayList<FoodEntry> itemList = new ArrayList();
        //log tasks into database
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(ItemContract.ItemEntry.TABLE,
                new String[]{ItemContract.ItemEntry._ID, ItemContract.ItemEntry.COL_ITEM_TITLE,
                        ItemContract.ItemEntry.COL_ITEM_DATE},
                null, null, null, null, null);
        while(cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(ItemContract.ItemEntry.COL_ITEM_TITLE);
            String nameOfEntry = cursor.getString(idx);
            idx = cursor.getColumnIndex(ItemContract.ItemEntry.COL_ITEM_DATE);
            String dateOfEntry = cursor.getString(idx);
            itemList.add(new FoodEntry(nameOfEntry, dateOfEntry));
        }
        //if mAdapter is null, create a new one
        if (mAdapter == null) {
            mAdapter = new ItemsAdapter(this, itemList);
            mItemListView.setAdapter(mAdapter); //set it as adapter

        } else {
            mAdapter.clear();
            mAdapter.addAll(itemList);
            mAdapter.notifyDataSetChanged();

        }
        cursor.close();
        db.close();
    }
}
