package com.example.finngluecki.schrittzaehler;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends Activity implements SensorEventListener {

    DatabaseHelper mDatabaseHelper;
    private Button btnViewData, btnAddData, btn2ViewData, btnTarget;
    private FloatingActionButton FAB;
    TextView editText2, editTarget;

    public SensorManager sm;
    public Sensor sc;
    public TextView tv;

    boolean Timer = true;
    boolean walking = false;

    private int stepsInSensor = 0;
    private int stepAtReset;
    private String PREFS;
    int stepsSinceReset = 0;
    int c = 1;

    /**
     * Erstellt die einzelnen Layout Elemente
     * Wechselt die Szene durch das Klicken auf die Buttons btn2ViewData und btnViewData
     * Durch den Button btnAddData fügt man Daten des TextViews in die Datebank ein
     * Durch den Button btnTarget fügt man den Text des TextEdits als Target des Diagramms ein
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        stepAtReset = prefs.getInt("stepsAtReset", 0);
        setContentView(R.layout.activity_main);
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sc = sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        tv = (TextView) findViewById(R.id.schritte);
        editText2 = (TextView) findViewById(R.id.schritte);
        editTarget = (EditText) findViewById(R.id.editTarget);
        btnViewData = (Button) findViewById(R.id.btnView);
        btnAddData = (Button) findViewById(R.id.btnAdd);
        btn2ViewData = (Button) findViewById(R.id.btn2View);
        btnTarget = (Button) findViewById(R.id.btnTarget);
        FAB = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        mDatabaseHelper = new DatabaseHelper(this);

        this.registerReceiver(this.receiver,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        btnTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiagrammActivity.target = Integer.parseInt(editTarget.getText().toString());
                toastMessage("Ziel wurde Gesetzt");
            }
        });

        btn2ViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DiagrammActivity.class);
                startActivity(intent);
            }
        });

        btnViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListDataActiviti.class);
                startActivity(intent);
            }
        });

        btnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onInsert();
            }
        });

        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Fügt die Daten des TextViews in die Datenbank ein, mit dem aktuellen Datum
     */
    protected void onInsert(){
        String dateStr;
        Date d = new Date();
        long ldate = d.getTime();

        dateStr = Objects.toString(ldate);
        SimpleDateFormat curFormater = new SimpleDateFormat("dd MM yyyy");

        try {
            d = curFormater.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat postFormater = new SimpleDateFormat("MMM dd yyyy");
        String newDateStr = postFormater.format(d);

        String newEntry = String.valueOf(stepsSinceReset);
        String newEntry2 = newDateStr;
        if (editText2.length() != 0) {
            AddData(newEntry, newEntry2);
        } else {
            toastMessage("You must put something in the text field!");
        }

        Background.AtReset = stepsInSensor;
        stepAtReset = stepsInSensor;
        stepsSinceReset = 0;

        SharedPreferences.Editor editor = getSharedPreferences(PREFS, MODE_PRIVATE).edit();
        editor.putInt("sepsAtReset", stepAtReset);
        editor.commit();
        tv.setText(String.valueOf(stepsSinceReset));
    }

    /**
     * Fügt die Daten in die Datenbank ein
     * @param newEntry
     * @param newEntry2
     */
    public void AddData(String newEntry, String newEntry2) {
        boolean insertData = mDatabaseHelper.addData(newEntry, newEntry2);
        if (insertData) {
            toastMessage("Datenn Erfolgreich Eingefügt!");
        } else {
            toastMessage("Etwas ist schief gelaufen");
        }
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Wenn das Programm zum erstenmal gestartet wird, wird es auf 0 gesetzt
     * Nachdem es einmal resettet wurde, werden die alten insgesamten Steps(stepsInSensor) minus die alle steps die bevor dem neusten Eintrag gemacht wurden(stepsAtReset) gerechnet
     * und gibt diese aus
     * @param event
     */
    public void onSensorChanged(SensorEvent event) {
        if(c == 1){
            if(Background.AtReset == 0) {
                stepAtReset = Integer.valueOf((int) event.values[0]);
                c++;
            }else{
                stepAtReset = Background.AtReset;
                c++;
            }
        }
        if(walking){
            stepsInSensor = Integer.valueOf((int)event.values[0]);
            stepsSinceReset = stepsInSensor - stepAtReset;
            tv.setText(String.valueOf(stepsSinceReset));
            Background.AtReset = stepAtReset;
            makeNotification();
        }else{
            event.values[0] = 0;
        }
    }

    /**
     *
     * @param sensor
     * @param accuracy
     */
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * wenn der sensor einen (Schritt) realisiert setzt er walking auf true um einen Schritt zu zählen
     */
    protected void onResume() {
        super.onResume();
        walking = true;
        Sensor cSensor = sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (cSensor != null) {
            sm.registerListener((SensorEventListener) this, cSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Sensor nicht Gefunden", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * wenn nicht gelaufen wird, wird walking auf false gesetzt
     */
    protected void onPause(){
        super.onPause();
        walking = false;
    }

    /**
     * wenn es 00:00 ist fügt es automatisch die Daten des TextViews ein.
     */
    private BroadcastReceiver receiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context arg0, Intent intent) {
            int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            if(currentHour == 24 && Timer == true){
                onInsert();
                Timer = false;
            }else if(currentHour < 2 && Timer == false){
                Timer = true;
            }
        }
    };

    /**
     * Eine Notification Welche die Schritte anzeigt
     */
    private void makeNotification(){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle("Schritte")
                        .setContentText("" + stepsSinceReset)
                        .setOngoing(true);
        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT
                );
        int mNotificationId = 001;
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification n;
        n = mBuilder.build();
        n.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}
