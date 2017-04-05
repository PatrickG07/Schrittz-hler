package com.example.finngluecki.schrittzaehler;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by finn.gluecki on 23.03.2017.
 */
public class Background extends Service {

    static int AtReset;

    /**
     *
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        return START_NOT_STICKY;
    }

    /**
     *
     */
    @Override
    public void onDestroy() {
        Toast.makeText(this, "on Zerschtören call", Toast.LENGTH_SHORT).show();
    }
}
