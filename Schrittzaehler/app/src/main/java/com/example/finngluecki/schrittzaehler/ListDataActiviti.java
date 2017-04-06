package com.example.finngluecki.schrittzaehler;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

public class ListDataActiviti extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";

    private FloatingActionButton FAB;

    DatabaseHelper mDatabaseHelper;

    private ListView lListView, rListView;

    /**
     * erstellt die einzelnen elemente
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data_activiti);
        lListView = (ListView) findViewById(R.id.listView1);
        rListView = (ListView) findViewById(R.id.listView2);
        mDatabaseHelper = new DatabaseHelper(this);
        FAB = (FloatingActionButton) findViewById(R.id.floatingActionButton4);

        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListDataActiviti.this, MainActivity.class);
                startActivity(intent);
            }
        });

        populateListView();
    }

    /**
     * gibt die daten der Datenbank in einem listView an
     */
    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the ListView.");

        //get the data and append to a list
        Cursor data = mDatabaseHelper.getData();
        ArrayList<String> listData1 = new ArrayList<>();
        ArrayList<String> listData2 = new ArrayList<>();
        while(data.moveToNext()){
            //get the value from the database in column 1
            //then add it to the ArrayList
            listData1.add(data.getString(1));
            listData2.add(data.getString(2));

        }
        //create the list adapter and set the adapter
        ListAdapter adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData1);
        ListAdapter adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData2);
        lListView.setAdapter(adapter1);
        rListView.setAdapter(adapter2);
    }
}
