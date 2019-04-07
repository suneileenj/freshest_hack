package com.example.listprototype;

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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.util.Log;

import android.widget.TextView;


import com.example.listprototype.db.*;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    //instance variables
    private ItemDbHelper mHelper;
    private ListView mItemListView;
    private ArrayAdapter<String> mAdapter;

    /**
     * Creates a new state and updates the UI
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //helper for database
        mHelper = new ItemDbHelper(this);
        //initialize viewer for list
        mItemListView = findViewById(R.id.list_food);

        mHelper = new ItemDbHelper(this);
        updateUI();

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
                final EditText taskEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add a new item")
                        .setMessage("What food would you like to add?")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //get string input
                                String item = String.valueOf(taskEditText.getText());
                                //get database
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                //put item into database
                                values.put(ItemContract.ItemEntry.COL_ITEM_TITLE, item);
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
        ArrayList<String> itemList = new ArrayList();
        //log tasks into database
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(ItemContract.ItemEntry.TABLE,
                new String[]{ItemContract.ItemEntry._ID, ItemContract.ItemEntry.COL_ITEM_TITLE},
                null, null, null, null, null);
        while(cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(ItemContract.ItemEntry.COL_ITEM_TITLE);
            itemList.add(cursor.getString(idx));
        }
        //if mAdapter is null, create a new one
        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.item_food, //the view to use for the food
                    R.id.food_name, //where to put the string of data
                    itemList); //where to get the data
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
