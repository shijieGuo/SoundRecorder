package service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.widget.Chronometer;

import java.io.File;
import java.io.IOException;

import connect.Connect;

public class CloudService extends Service {

    private PowerManager.WakeLock wakeLock=null;

    private Connect connect;
    private int connectPort[]={5000,5001};//PC端
    private String IP="10.40.9.48";//PC端



    public class RecordBinder extends Binder {

        public RecordBinder(RecordService recordService){

        }


    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return  null;

    }

    public CloudService() {
        super();


    }
    public void onCreate()
    {
        super.onCreate();

        setPower();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        return super.onStartCommand(intent,flags,startId);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setPower()
    {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, RecordService.class.getName());
        wakeLock.acquire();

    }


}
