package connect;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

public class Connectport3 extends ConnectThread {


    private int state=0;
    private String instruct;
    private Handler mHandler;


    private File sdcardfile;
    private File datafile;
    private File files[];





    public Connectport3(int connectPort, InetAddress computerInet, Handler mHandler) {
        super(connectPort,computerInet);
        this.mHandler=mHandler;

        initFile();


    }

    public void  management(OutputStream ou, InputStream in)
    {

        switch (state) {
            case 0:
                block();
                break;
            case 1://发送获取文件信息指令
                sendInstrcut(ou);
                break;
            case 2://接受文件信息
                getFileMessage(in);
                break;
            case 3:
                sendOut(ou);//发送断开连接请求
                break;
            default:

                break;
        }


    }

    private void sendInstrcut(OutputStream ou)
    {
        writeInfo(ou,instruct);
        state=2;
        setStop(false);

    }

    public void setState(int state)
    {
        this.state=state;
        setStop(false);
    }

    public void setInstruct(String instruct)
    {
        this.instruct=instruct;
    }




    private void block()
    {
        this.state=0;
        setStop(true);

    }




    private void getFileMessage(InputStream in)
    {
        this.state=0;

        byte b[]=readInfo(in);
        String message=new String(b,0,b.length);

        Message msg = Message.obtain();
        msg.what=0;
        msg.obj=message;
        mHandler.sendMessage(msg);



    }

    private void sendOut(OutputStream ou)
    {
        this.state=0;
        writeInfo(ou,instruct);
        InetThread.inetThread.getConnect().setOut();
    }




    private void initFile()
    {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) //内存卡存在
        {
            sdcardfile = Environment.getExternalStorageDirectory();//获取目录文件
            datafile=new File(sdcardfile.getAbsoluteFile()+"/SoundRecorder/data");
            createDir(datafile.getAbsolutePath());
            files=datafile.listFiles();

        }

    }


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