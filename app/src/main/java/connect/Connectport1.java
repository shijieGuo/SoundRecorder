package connect;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.InetAddress;

import cloudfragment.Notification_FileTran;


public class Connectport1 extends ConnectThread {

	private static int state=0;

	private String choosen_File;

	private File sdcardfile;
	private File datafile;
	private File files[];

	private Notification_FileTran notification_fileTran;
	private Context context;


	public Connectport1(int connectPort, InetAddress computerInet, Context context) {
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
			case 1:
                sendFile(ou);
                break;
			default:

				break;
		}


	}


	private void block()
	{
		setStop(true);

	}

	private int sendFile(OutputStream ou)
	{

		File f = new File(datafile+"/"+choosen_File);
		if (!f.exists()) {  return -1; }

		ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(f));
			int buf_size = 1024;
			byte[] buffer = new byte[buf_size];
			int len = 0;
			int num=0;
			float length=(float)f.length();

			notification_fileTran=new Notification_FileTran(choosen_File,context,choosen_File);
			notification_fileTran.build();


			while (-1 != (len = in.read(buffer, 0, buf_size))) {
				bos.write(buffer, 0, len);
				num=num+len;
				sendHnadlerMessage((int)((num)/length*100));

			}
			in.close();
			byte b1[]= bos.toByteArray();


			byte b0[]=(choosen_File+" "+b1.length+"#").getBytes();
			byte b[]=new byte[b0.length+b1.length];

			System.arraycopy(b0, 0, b, 0, b0.length);
			System.arraycopy(b1, 0, b, b0.length, b1.length);
			writeInfo2(ou,b);
		}
		catch (Exception e){}

		state=0;
		return 1;

	}
	private void sendHnadlerMessage(int progress)
	{
		notification_fileTran.setProgress(progress);
	}

	public void setChoosen(String message)
	{
		choosen_File=message;
		state=1;
		setStop(false);
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
