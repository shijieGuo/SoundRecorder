package cloudfragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.dell.soundrecorder.R;

import java.util.ArrayList;

import checkpermissions.CheckPermissions;
import connect.Connect;
import connect.Connectport2;
import connect.Connectport3;
import connect.InetThread;
import saverecodingfragment.Item;
import saverecodingfragment.MyAdapter;
import service.CloudService;
import service.RecordService;

public class CloudFragment extends Fragment {

    private View view=null;
    private ListView listView;

    private Button log_button;
    private Button fresh_button;


    private boolean logState=false;



    public CloudFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(view==null) {
            view = inflater.inflate(R.layout.fragment_cloud, container, false);
            setView();
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }

    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 0:
                   getFileMessage(msg.obj.toString());
                   break;
                case 1:
                    break;
                    default:
                        break;
            }
        }

        private void getFileMessage(String message){
            String files[]=message.split(",");
            int num=files.length;
            Item2 item2s[]=new Item2[num];
            for(int i=0;i<num;i++)
            {
                String fileMessage[]=files[i].split(" ");
                String name=fileMessage[0];
                String date=fileMessage[1];
                item2s[i]=new Item2(name,date);
            }

            setListview(item2s);

        }
    };



    private void setView()
    {
        listView=view.findViewById(R.id.listview);


        log_button=(Button)view.findViewById(R.id.logbutton);
        log_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (new CheckPermissions(getContext(),getActivity()).checklacks()){
                        return;
                    }
               // getActivity().startService(new Intent(getActivity(), CloudService.class));  报错 主线程干太多事了
                if(((Button)v).getText().equals("登录")) {


                        InetThread.inetThread.setConnect(mHandler, getActivity());
                        InetThread.inetThread.start();

                        new Thread() {

                            public void run() {
                                while (!InetThread.inetThread.getConnect().getIfConnect()) {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (Exception e) {
                                    }
                                }
                                ((Connectport3) InetThread.inetThread.getConnect().getConnectThread(2)).setInstruct("#sendFileMessage#");
                                ((Connectport3) InetThread.inetThread.getConnect().getConnectThread(2)).setState(1);
                            }
                        }.start();
                        ((Button) v).setText("断开");


                }
                else
                {
                    ((Button) v).setText("登录");
                    ((Connectport3) InetThread.inetThread.getConnect().getConnectThread(2)).setInstruct("#out#");
                    ((Connectport3) InetThread.inetThread.getConnect().getConnectThread(2)).setState(3);
                    listView.setAdapter(null);

                }


            }
        });

        fresh_button=(Button)view.findViewById(R.id.freshbutton);
        fresh_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new CheckPermissions(getContext(),getActivity()).checklacks()){
                    return;
                }
                if(InetThread.inetThread.isIfSet()) {
                    if (InetThread.inetThread.getConnect().getIfConnect()) {
                        ((Connectport3) InetThread.inetThread.getConnect().getConnectThread(2)).setInstruct("#sendFileMessage#");
                        ((Connectport3) InetThread.inetThread.getConnect().getConnectThread(2)).setState(1);
                    }
                }
            }
        });



    }

    public void setListview(Item2 item2s[])
    {

        ArrayList arrayList=new ArrayList();
        for(int i=0;i<item2s.length;i++)
            arrayList.add(item2s[i]);
        MyAdapter2 myAdapter2=new MyAdapter2(getActivity(),R.layout.item2,arrayList,InetThread.inetThread);
        listView.setAdapter(myAdapter2);


    }
}
