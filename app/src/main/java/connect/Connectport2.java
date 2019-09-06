package connect;



import android.content.Context;
import android.os.Environment;
import android.os.Handler;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.InetAddress;

import cloudfragment.Notification_FileTran;


public class Connectport2 extends ConnectThread {


    private static int state=0;

    private Context context;
    private String choosen_File;
    private Handler handler;

    private File sdcardfile;
    private File datafile;
    private File files[];

    private Notification_FileTran notification_fileTran;


    public Connectport2(int connectPort, InetAddress computerInet, Context context) {
        super(connectPort,computerInet);
        this.context=context;

        initFile();


    }

    public void  management(OutputStream ou, InputStream in)
    {

        switch (state) {
            case 0:
                block();
                break;
            case 1://发送选择的文件名
                sendChoosen(ou);
                break;
            case 2://接受文件内容
                getFile(in);
                break;

            default:

                break;
        }


    }

    public void setStatus(int status)
    {
        this.state=status;
        setStop(false);
    }


    private void block()
    {
        this.state=0;
        setStop(true);

    }






    private void sendChoosen(OutputStream ou)
    {
        this.state=2;
        writeInfo(ou,choosen_File);
    }

    private void getFile(InputStream in)
    {
        this.state=0;

        byte b1[]=readInfo(in);
        int i=0;
        while(b1[i]!='#')i++;
        String s=new String(b1,0,i);
        String ss[]=s.split(" ");
        String filename=ss[0];
        int num=Integer.valueOf(ss[1]);
        float length=num;

        notification_fileTran=new Notification_FileTran(filename,context,filename);
        notification_fileTran.build();

        byte b[]=new byte[b1.length-s.length()-1];
        System.arraycopy(b1, s.length()+1, b, 0, b.length);


        try {
            String strFilePath = datafile.getPath()+"/"+filename;


            File file = new File(strFilePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.write(b);
            num=num-b.length;

            sendHnadlerMessage((int)((length-num)/length*100));
            while(num>0)
            {
                b=readInfo(in);
                raf.write(b);
                num=num-b.length;
                sendHnadlerMessage((int)((length-num)/length*100));
            }


            raf.close();
        } catch (Exception e) { }
    }

    private void sendHnadlerMessage(int progress)
    {
        notification_fileTran.setProgress(progress);
    }





    public void setChoosen(String message)
    {
        choosen_File=message;
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