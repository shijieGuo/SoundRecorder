package saverecodingfragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dell.soundrecorder.R;

import java.io.IOException;
import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter {
    private ArrayList<Item> arrayList;
    private Context context;

    public MyAdapter(Context context, int textViewResourceId, ArrayList<Item> objects) {
        super(context, textViewResourceId, objects);
        this.context=context;
        this.arrayList = objects;
    }
    @Override
    public int getCount() {
        return super.getCount();
    }
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.item, null);
        v.setLongClickable(true);
        v.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View view) {
                CustomDialog2 customDialog2=new CustomDialog2(getContext(),arrayList.get(position),0.5,0.28);
                customDialog2.setCancelable(true);
                customDialog2.show();
                //Toast.makeText(getActivity(),"摇一摇",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        v.setClickable(true);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog customDialog=new CustomDialog(getContext(),arrayList.get(position),0.35,0.9);
                customDialog.setCancelable(false);
                customDialog.show();
            }
        });

        ListView.LayoutParams params = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT,170);//设置宽度和高度
        v.setLayoutParams(params);

        TextView filename_text = (TextView) v.findViewById(R.id.filename);
        TextView filedate_text= (TextView) v.findViewById(R.id.filedate);
        TextView filetime_text= (TextView) v.findViewById(R.id.filetime);
        filename_text.setText(arrayList .get(position).getFilename());
        filedate_text.setText(arrayList .get(position).getFiledate());
        filetime_text.setText(arrayList .get(position).getFiletime());//音频的总时长
        return v;
    }

    public ArrayList getArraryList()
    {
        return arrayList;
    }

}
