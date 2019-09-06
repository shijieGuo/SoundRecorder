package saverecodingfragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dell.soundrecorder.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import checkpermissions.CheckPermissions;

public class SaveRecordingFragment extends Fragment {

    private View view=null;
    private ListView listView;
    private File sdcardfile;
    private File datafile;
    private File files[];
    private ArrayList arrayList;
    private MyAdapter myAdapter;
    private MediaPlayer playmusic;
    private static Handler handler;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(view==null) {
            view = inflater.inflate(R.layout.fragment_saverecording, container, false);
            setView();
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }

    public void onResume()
    {
        super.onResume();
    }


    private void setView()
    {
        listView = (ListView) view.findViewById(R.id.listview);
        setListview();
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==0X111){
                    new Thread(){
                        public void run()
                        {
                            initFile();
                            Message message=new Message();
                            message.what=0x110;
                            try { Thread.sleep(500);  } catch (InterruptedException e) {   e.printStackTrace();  }
                            handler.sendMessage(message);

                        }
                    }.start();
                }
                else if(msg.what==0x110)
                {
                    freshenlist();
                }
                super.handleMessage(msg);
            }
        };

    }

    public void setListview()
    {

        arrayList=new ArrayList();
        myAdapter=new MyAdapter(getActivity(),R.layout.item,arrayList);
        listView.setAdapter(myAdapter);
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
public String gettime(File file){
    playmusic=new MediaPlayer();
    try {
        //recordingTime = 0;
        //time1.setBase(SystemClock.elapsedRealtime());
        playmusic.setDataSource(file.getAbsolutePath());
        playmusic.prepare();
    } catch (IOException e) {
        e.printStackTrace();
    }
        //bar.setMax(playmusic.getDuration()/1000);
        int min=playmusic.getDuration()/1000/60;
        int s=playmusic.getDuration()/1000-(min*60);
        playmusic.reset();//清空MediaPlayer中的数据
        playmusic.stop();//停止播放
        playmusic.release();//释放资源
        String min1=null;
        String s1=null;
        if(min<10){
            min1="0"+min;
        }
        else min1=""+min;
        if(s<10){
            s1="0"+s;
        }
        else s1=""+s;
        //time2.setText(min1+":"+s1);//音频的总时长
        //play=1;
        //playmusic.start();

    return min1+":"+s1;
}
public static Handler gethandler(){
        return handler;
}
public void freshenlist() {
        arrayList.clear();
        for(int j=0;j<files.length;j++)
            arrayList.add(new Item(files[j],gettime(files[j])));
        myAdapter.notifyDataSetChanged();
        //Toast.makeText(getActivity(),"已刷新",Toast.LENGTH_SHORT).show();
    }
}
