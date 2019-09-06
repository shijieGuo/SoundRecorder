package saverecodingfragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.example.dell.soundrecorder.R;
import java.io.File;

import connect.Connectport1;
import connect.InetThread;
import share.ShareDialog;

public class CustomDialog2 extends AlertDialog {

    private double dialog_width;
    private double dialog_height;
    private File file;
    private Activity activity;
    private ImageButton delete;
    private ImageButton share;
    private ImageButton upload;
    public CustomDialog2(@NonNull Context context, Item item, double dialog_height, double dialog_width) {
        super(context);
        //  setContentView(R.layout.custom_dialog);
        this.activity = (Activity) context;
        this.dialog_height = dialog_height;
        this.dialog_width = dialog_width;
        this.file=item.getFile();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //为了锁定app界面的东西是来自哪个xml文件
        setContentView(R.layout.windowlayout2);


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
    @Override
    public void show() {
        super.show();
        getWindow().setWindowAnimations(R.style.dialogWindowAnim1);
        // 设置显示位置为右部
        getWindow().setGravity(Gravity.RIGHT);
    }
    public void findview() {
        delete=(ImageButton)findViewById(R.id.delete);
        share=(ImageButton)findViewById(R.id.share);
        upload=(ImageButton)findViewById(R.id.upload);
    }
    public void setviews(){
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                //ShareDialog shareDialog=new ShareDialog(getContext(),0.25,1);
                //shareDialog.setCancelable(true);
                //shareDialog.show();
                //在这里实现分享
                Intent intent=new Intent(Intent.ACTION_SEND);
                Uri uri= FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider",file );//Uri.fromFile(file);
                //intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                intent.putExtra(Intent.EXTRA_STREAM,uri);
                intent.setType("application/octet-stream");
                //intent.setType("image/*");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(Intent.createChooser(intent,"share"));
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                file.delete();
                refreshlist();
                dismiss();//关闭
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //上传
                if(InetThread.inetThread.isIfSet()) {
                    if(InetThread.inetThread.getConnect().getIfConnect()) {
                        ((Connectport1) (InetThread.inetThread.getConnect().getConnectThread(0))).setChoosen(file.getName());
                    }
                }
                dismiss();


            }
        });
    }
    private void refreshlist(){
        Message message=new Message();
        message.what=0x111;
        SaveRecordingFragment.gethandler().sendMessage(message);
    }
}
