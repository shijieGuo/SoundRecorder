package com.example.dell.soundrecorder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.IOException;
import java.util.ArrayList;

import checkpermissions.CheckPermissions;
import cloudfragment.CloudFragment;
import recodfragment.RecordFragment;
import saverecodingfragment.SaveRecordingFragment;

public class MainActivity extends AppCompatActivity {


    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE

    };
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;

    private ViewPager viewPager;
    private ArrayList<Fragment> mFragmentList = new ArrayList <Fragment>();
    private FragmentPagerAdapter fragmentPagerAdapter;

    private Button[] Buttons=new Button[3];
    private int choosen_fragment=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("afaf222",String.valueOf(a));

        setContentView(R.layout.activity_main);
        setpermission();
        initFragment();
        setView();
    }



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.v("afaf33333",String.valueOf(a));

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    private void initFragment()
    {

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        mFragmentList.add(new RecordFragment());
        mFragmentList.add(new SaveRecordingFragment());
        mFragmentList.add(new CloudFragment());

        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return mFragmentList != null ? mFragmentList.get(i) : null;
            }

            @Override
            public int getCount() {
                return mFragmentList != null ? mFragmentList.size() : 0;
            }
        };
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i){
                    case 0:
                        Buttons[0].setTextColor(Color.BLACK);
                        Buttons[1].setTextColor(Color.WHITE);
                        Buttons[2].setTextColor(Color.WHITE);
                        break;
                    case 1:
                        Buttons[1].setTextColor(Color.BLACK);
                        Buttons[0].setTextColor(Color.WHITE);
                        Buttons[2].setTextColor(Color.WHITE);
                        Message message=new Message();
                        message.what=0x111;
                        SaveRecordingFragment.gethandler().sendMessage(message);
                        break;
                    case 2:
                        Buttons[2].setTextColor(Color.BLACK);
                        Buttons[1].setTextColor(Color.WHITE);
                        Buttons[0].setTextColor(Color.WHITE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void setView()
    {
        Buttons[0]=findViewById(R.id.record_imagebutton);
        Buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0,true);
            }
        });
        Buttons[1]=findViewById(R.id.saverecording_imagebutton);
        Buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1,true);
            }
        });
        Buttons[2]=findViewById(R.id.cloud_imagebutton);
        Buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2,true);
            }
        });
        Buttons[0].setTextColor(Color.BLACK);
        Buttons[1].setTextColor(Color.WHITE);
        Buttons[2].setTextColor(Color.WHITE);
    }


    private void setpermission()
    {
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
//            }
//        }
        if (new CheckPermissions(this,this).checklacks()){
            return;
        }

    }
    private static int a=0;
    protected void onStart()
    {
        super.onStart();
        int a=0;
        a++;
    }

    protected  void onPause() {

        Log.v("afaf1",String.valueOf(a));
        super.onPause();
        a++;


    }
    protected void onDestroy() {

        super.onDestroy();
        Log.v("afaf6",String.valueOf(a));

        a++;
    }

    protected  void onStop() {

        Log.v("afaf2",String.valueOf(a));
        super.onStop();
        a++;


    }
    protected  void onResume() {

        Log.v("afaf3",String.valueOf(a));
        super.onResume();
        a++;


    }
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        Log.v("afaf4",String.valueOf(a));


        a++;

        outState.putInt("choosen_fragment",choosen_fragment);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null)
        {

            choosen_fragment= (int) savedInstanceState.getInt("choosen_fragment");
            Log.v("afaf22",String.valueOf(a));
        }
    }




}

