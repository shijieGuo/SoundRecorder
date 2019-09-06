package recodfragment;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.soundrecorder.R;

import checkpermissions.CheckPermissions;
import service.RecordService;

public class RecordFragment extends Fragment {

    private View view=null;
    private ImageButton record_imageButton;
    private TextView textView;
    private Chronometer chronometer;
    private int state=0;


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            RecordService.RecordBinder recordBinder = (RecordService.RecordBinder) service;

            recordBinder.recording(chronometer);

            getActivity().unbindService(connection);
            }


        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

    };






    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(view==null) {
            view = inflater.inflate(R.layout.fragment_record, container, false);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setView();

    }


    private void setView()
    {
        textView=view.findViewById(R.id.text);
        record_imageButton= view.findViewById(R.id.record_imagebutton);
        record_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new CheckPermissions(getContext(),getActivity()).checklacks()){
                    return;
                }
              if(state==0){
                  record_imageButton.setBackgroundResource(R.drawable.recording);state=1;
                  textView.setText("Recording..");
                  getActivity().startService(new Intent(getActivity(), RecordService.class));
                  getActivity().bindService(new Intent(getActivity(), RecordService.class),connection,Context.BIND_AUTO_CREATE);
              }
              else {
                  record_imageButton.setBackgroundResource(R.drawable.sound2);state=0;
                  textView.setText("Tap the button to start recording");
                  getActivity().startService(new Intent(getActivity(), RecordService.class));
                  getActivity().bindService(new Intent(getActivity(), RecordService.class),connection,Context.BIND_AUTO_CREATE);
                  //music_name name=new music_name(getContext(),0.3,0.8);
                  //name.setCancelable(true);
                  //name.show();
              }




            }
        });
        chronometer=view.findViewById(R.id.timer);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
