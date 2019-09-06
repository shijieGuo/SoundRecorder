package connect;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import java.net.InetAddress;


public class InetThread extends Thread {

    private Handler mHandler;
    private Connect connect;

    private int connectPort[]={6000,6001,6002};//PC端
    private String IP="10.48.20.125";//PC端

    public static InetThread inetThread=new InetThread();

    private boolean ifSet=false;

    private InetThread()
    {

    }



    public void run()
    {
        connect.startConnect();

    }
    public void setConnect(Handler mHandler, Context context)
    {
        this.mHandler=mHandler;
        connect=new Connect(connectPort,IP,mHandler,context);
        ifSet=true;
    }

    public boolean isIfSet(){return this.ifSet;}

    public Connect getConnect()
    {
        return this.connect;
    }

    public void refreshInetThread()
    {
        this.inetThread=new InetThread();
    }


}
