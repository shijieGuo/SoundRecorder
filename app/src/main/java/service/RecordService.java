package service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.dell.soundrecorder.R;

import java.io.File;
import java.io.IOException;

import recodfragment.CalendarDemo;

public class RecordService extends Service {

    private ImageButton record_imageButton;
    private PowerManager.WakeLock wakeLock=null;

    private RecordBinder recordBinder = new RecordBinder(this);

    private static MediaRecorder recorder = new MediaRecorder();

    private static int state=0;//0静止  1录音

    public class RecordBinder extends Binder {

        private File sdcardfile = null;
        private File datafile=null;
        private RecordService recordService;
        public RecordBinder(RecordService recordService){

            this.recordService=recordService;
        }

       public void recording(Chronometer chronometer) {

           switch(recordService.getState()) {

               case 0:

                   if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) //内存卡存在
                   {
                       sdcardfile = Environment.getExternalStorageDirectory();//获取目录文件
                       datafile=new File(sdcardfile.getAbsoluteFile()+"/SoundRecorder/data");
                       createDir(datafile.getAbsolutePath());
                   }
                   if(sdcardfile!=null) {


                       recorder.setAudioSource(MediaRecorder.AudioSource.MIC);

                       recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

                       recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);


                       try {
                           File file = new File(datafile + "/" + "new"+CalendarDemo.calendarDemo.getDate() + ".m4a");
                           recorder.setOutputFile(file.getAbsolutePath());//设置文件输出路径
                           //准备和启动录制音频
                           recorder.prepare();
                           recorder.start();

                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                       recordService.setState(1);
                       chronometer.setBase(SystemClock.elapsedRealtime());
                       chronometer.start();
                   }
                   break;
               case 1:
                   recorder.stop();
                   chronometer.stop();

                   recordService.setState(0);
                   break;
           }

       }

    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return  recordBinder;

    }

    public RecordService() {
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
        System.out.println("Service onDestroy");
        wakeLock.release();
        super.onDestroy();
    }

   private void setPower()
   {
       PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
       wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, RecordService.class.getName());
       wakeLock.acquire();

   }

   public int getState(){return this.state;}
   public void setState(int state){this.state=state;}


    private static boolean createDir(String destDirName) {
       File dir = new File(destDirName);
        if (dir.exists()) {
            return false;
         }
            if (!destDirName.endsWith(File.separator)) {//如果没有/ 加上/
                    destDirName = destDirName + File.separator;
              }
              //创建目录
             if (dir.mkdirs()) { return true;  }
             else { return false; }
    }




}
