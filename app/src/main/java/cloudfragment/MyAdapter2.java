package cloudfragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.soundrecorder.R;

import java.io.File;
import java.util.ArrayList;

import connect.Connectport1;
import connect.Connectport2;
import connect.InetThread;
import saverecodingfragment.Item;

public class MyAdapter2 extends ArrayAdapter {
    private ArrayList<Item2> arrayList;
    private Context context;
    private InetThread inetThread;

    public MyAdapter2(Context context, int textViewResourceId, ArrayList<Item2> objects, InetThread inetThread) {
        super(context, textViewResourceId, objects);
        this.context=context;
        this.arrayList = objects;
        this.inetThread=inetThread;
    }
    @Override
    public int getCount() {
        return super.getCount();
    }
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        View v = convertView;
        final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.item, null);
        v.setLongClickable(true);
     /*   v.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View view) {

                return false;
            }
        });*/


        ListView.LayoutParams params = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT,300);//设置宽度和高度
        v.setLayoutParams(params);

        TextView filename_text = (TextView) v.findViewById(R.id.filename);
        TextView filedate_text= (TextView) v.findViewById(R.id.filedate);

        filename_text.setText(arrayList .get(position).getFilename());
        filedate_text.setText(arrayList .get(position).getFiledate());

        v.setClickable(true);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(getContext()).setTitle("Download the file?")
                        .setIcon(android.R.drawable.sym_def_app_icon)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Connectport2 connectport2=(Connectport2) inetThread.getConnect().getConnectThread(1);
                                connectport2.setChoosen(arrayList .get(position).getFilename());
                                connectport2.setStatus(1);

                            }
                        }).setNegativeButton("No",null)
                        .setCancelable(false).show();


            }
        });
        return v;
    }

    public ArrayList getArraryList()
    {
        return arrayList;
    }

}
