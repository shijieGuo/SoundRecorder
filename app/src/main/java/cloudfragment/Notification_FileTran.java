package cloudfragment;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

import com.example.dell.soundrecorder.R;

public class Notification_FileTran extends Notification {

    private static int idNum=0;
    private int idnum;

    private String id;
    private String filename;
    private String text="0%";

    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder=null;
    private Notification notification=null;
    private Context context;



    public Notification_FileTran(String id, Context context, String filename)
    {
        this.filename = filename;
        this.id=id;
        this.context=context;
        notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);






    }

    public void build()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel mChannel = new NotificationChannel(id,filename,NotificationManager.IMPORTANCE_LOW);

            notificationManager.createNotificationChannel(mChannel);
            builder = new NotificationCompat.Builder(context,id)
                    .setChannelId(id)
                    .setContentTitle(filename)
                    .setContentText(text)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setProgress(100,0,false)
                    .setAutoCancel(true);


        } else {
          /*  NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setContentTitle(filename)
                    .setContentText(text)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setOngoing(true)
                    .setChannelId(id);//无效
            notification = notificationBuilder.build();*/
        }
        idnum=idNum;
        notificationManager.notify(idnum, builder.build());
        idNum++;
    }

    public void setProgress(int set){

                builder.setProgress(100,set,false);
                builder.setContentText(set+"%");
                notificationManager.notify(idnum, builder.build());

    }




}
