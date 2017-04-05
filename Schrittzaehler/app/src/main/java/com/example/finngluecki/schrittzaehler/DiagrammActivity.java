package com.example.finngluecki.schrittzaehler;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

import static android.R.attr.data;
import static android.content.ContentValues.TAG;

public class DiagrammActivity extends Activity {

    DatabaseHelper mDatabaseHelper;

    private FloatingActionButton FAB;
    int a1 = 0, a2 = 0, a3 = 0, a4 = 0, a5 = 0, a6 = 0, a7 = 0;

    static int target;

    /**
     * gibt die Letzten 7 daten der datenbank aus in einem Diagramm
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagramm);
        mDatabaseHelper = new DatabaseHelper(this);

        Log.d(TAG, "populateListView: Displaying data in the ListView.");
        //get the data and append to a list
        Cursor data = mDatabaseHelper.getData();
        ArrayList<String> listData1 = new ArrayList<>();
        FAB = (FloatingActionButton) findViewById(R.id.floatingActionButton3);

        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiagrammActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        while(data.moveToNext()){
            //get the value from the database in column 1
            //then add it to the ArrayList
            listData1.add(data.getString(1));
        }

        int a = listData1.size();
        GraphView graph = (GraphView) findViewById(R.id.graph);
        if(a == 0){

        }else if(a == 1){
            a--;
            a1 = Integer.parseInt(listData1.get(a));
        }else if(a == 2){
            a--;
            a1 = Integer.parseInt(listData1.get(a));
            a--;
            a2 = Integer.parseInt(listData1.get(a));
        }else if(a == 3){
            a--;
            a1 = Integer.parseInt(listData1.get(a));
            a--;
            a2 = Integer.parseInt(listData1.get(a));
            a--;
            a3 = Integer.parseInt(listData1.get(a));
        }else if(a == 4){
            a--;
            a1 = Integer.parseInt(listData1.get(a));
            a--;
            a2 = Integer.parseInt(listData1.get(a));
            a--;
            a3 = Integer.parseInt(listData1.get(a));
            a--;
            a4 = Integer.parseInt(listData1.get(a));
        }else if(a == 5){
            a--;
            a1 = Integer.parseInt(listData1.get(a));
            a--;
            a2 = Integer.parseInt(listData1.get(a));
            a--;
            a3 = Integer.parseInt(listData1.get(a));
            a--;
            a4 = Integer.parseInt(listData1.get(a));
            a--;
            a5 = Integer.parseInt(listData1.get(a));
        }else if(a == 6){
            a--;
            a1 = Integer.parseInt(listData1.get(a));
            a--;
            a2 = Integer.parseInt(listData1.get(a));
            a--;
            a3 = Integer.parseInt(listData1.get(a));
            a--;
            a4 = Integer.parseInt(listData1.get(a));
            a--;
            a5 = Integer.parseInt(listData1.get(a));
            a--;
            a6 = Integer.parseInt(listData1.get(a));
        }else {
            a--;
            a1 = Integer.parseInt(listData1.get(a));
            a--;
            a2 = Integer.parseInt(listData1.get(a));
            a--;
            a3 = Integer.parseInt(listData1.get(a));
            a--;
            a4 = Integer.parseInt(listData1.get(a));
            a--;
            a5 = Integer.parseInt(listData1.get(a));
            a--;
            a6 = Integer.parseInt(listData1.get(a));
            a--;
            a7 = Integer.parseInt(listData1.get(a));
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, a7),
                new DataPoint(1, a6),
                new DataPoint(2, a5),
                new DataPoint(3, a4),
                new DataPoint(4, a3),
                new DataPoint(5, a2),
                new DataPoint(6, a1)
        });
        graph.addSeries(series);

        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, target),
                new DataPoint(1, target),
                new DataPoint(2, target),
                new DataPoint(3, target),
                new DataPoint(4, target),
                new DataPoint(5, target),
                new DataPoint(6, target)
        });
        graph.addSeries(series2);

        series2.setColor(Color.RED);
    }
}
