package saverecodingfragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.InputType;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.soundrecorder.MainActivity;
import com.example.dell.soundrecorder.R;
import java.io.File;
import java.io.IOException;

import java.util.Timer;
import java.util.TimerTask;

import share.ShareDialog;

public class CustomDialog extends AlertDialog{
    private MediaPlayer playmusic=new MediaPlayer();;
    private int play=0;//0表示暂停  1表示播放
    private int current=0;
    private double dialog_width;
    private double dialog_height;
    private File file;
    private String time;
    private EditText title;
    private TextView time1;
    private TextView time2;
    private SeekBar bar;
    private ImageButton playButton;
    private ImageButton close;
    private ImageButton name;
    private boolean finish=false;
    private boolean start=false;
    private Timer timer;
    private Activity activity;
    private Item item;
    private boolean isname=true;
    public CustomDialog(@NonNull Context context,Item item,double dialog_height, double dialog_width) {
        super(context);
        //  setContentView(R.layout.custom_dialog);
        this.activity = (Activity) context;
        this.dialog_height = dialog_height;
        this.dialog_width = dialog_width;
        this.item=item;
        this.file=item.getFile();
        this.time=item.getFiletime();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //为了锁定app界面的东西是来自哪个xml文件
        setContentView(R.layout.windowlayout);


        //设置弹窗的宽度
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        Point size = new Point();
        d.getSize(size);
        p.width = (int) (size.x * dialog_width);//size.x*0.8是dialog的宽度为app界面的80%
        p.height = (int) (size.y * dialog_height);
        getWindow().setAttributes(p);
        findview();
        setviews();
    }
    public void findview() {
        title = (EditText)findViewById(R.id.title);
        time1 = (TextView)findViewById(R.id.time1);
        time2 = (TextView)findViewById(R.id.time2);
        name = (ImageButton)findViewById(R.id.name);
        bar=(SeekBar)findViewById(R.id.bar);
        playButton=(ImageButton)findViewById(R.id.imageButton);
        close=(ImageButton)findViewById(R.id.close);
        timer=new Timer();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0X111){
                bar.setProgress(current);
                time1.setText(formatterTime(current));
                if(current==playmusic.getDuration())timer.cancel();
            }
            super.handleMessage(msg);
        }
    };
    private TimerTask timerTask;
    private void refreshTimer()
    {
        timerTask=new TimerTask() {
            @Override
            public void run() {
                if(playmusic != null ){
                    current = playmusic.getCurrentPosition();
                } // 100 / 1000  //0.1% * 100
                if(finish)current=playmusic.getDuration();
                Message message=new Message();
                message.what=0x111;
                handler.sendMessage(message);

            }
        };
        timer=new Timer();
        timer.schedule(timerTask,0,100);
    }


    public void setviews(){

        prepareMusic();

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isname){
                    title.setCursorVisible(true);
                    title.setFocusable(true);
                    title.setFocusableInTouchMode(true);
                    title.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(title,0);
                    isname=false;
                    return;
                }
                else {
                    InputMethodManager imm = (InputMethodManager) getContext().
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(title.getWindowToken(), 0);
                    title.setCursorVisible(false);
                    title.setFocusable(false);
                    title.setFocusableInTouchMode(false);
                    isname=true;
                    String rootPath = file.getParent();
                    File newFile = new File(rootPath + File.separator + title.getText()+".amr");
                    if (file.renameTo(newFile))
                    {
                        file=new File(rootPath + File.separator + title.getText()+".amr");
                        Toast.makeText(activity,"更换成功",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(activity,"更换失败",Toast.LENGTH_SHORT).show();
                    }
                    return;
                }



            }
        });
        title.setText(item.getFilename());
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.setCursorVisible(true);
            }
        });
        time2.setText(time);//音频的总时
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b) {
                    if(play==0) {

                        playMusic(1);
                        seekToMusic(i);

                    }
                    else if(play==1)
                    {
                        seekToMusic(i);
                    }
                    current=i;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //关闭弹窗
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(play!=0){
                    playmusic.reset();
                    playmusic.stop();
                    play=0;
                }
                refreshlist();
                dismiss();//关闭

            }
        });
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(play==0){//如果暂停
                    playMusic(0);

                }
                else if(play==1){//如果播放中

                    stopMusic();
                }
            }
        });
    }
    public String formatterTime(int currentPosition) {
        int min=currentPosition/1000/60;
        int s=currentPosition/1000-(min*60);
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
        return min1+":"+s1;
    }
    private void prepareMusic()
    {


        try {
            playmusic.setDataSource(file.getAbsolutePath());
            playmusic.prepare();
            bar.setMax(playmusic.getDuration());
            play=0;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void playMusic(int index)//0 按钮触发 1 进度条触发
    {
        playButton.setBackgroundResource(R.drawable.play);
        playmusic.start();
        if(finish)
        {
            finish=false;
            if(index==0) //播放完成后点了重新开始播放
                bar.setProgress(0);
        }
        if(start==false) {
            playmusic.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    play = 0;
                    start=false;
                    finish=true;
                    playButton.setBackgroundResource(R.drawable.stop);
                }
            });
        }
        if(start==false)start=true;
        refreshTimer();

        play=1;
    }

    private void stopMusic()
    {
        playmusic.pause();
        playButton.setBackgroundResource(R.drawable.stop);
        timer.cancel();
        play=0;

    }

    private void seekToMusic(int i)
    {
        playmusic.seekTo(i);
    }
//刷新listview列表
private void refreshlist(){
    Message message=new Message();
    message.what=0x111;
    SaveRecordingFragment.gethandler().sendMessage(message);
}
}
