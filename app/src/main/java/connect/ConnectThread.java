package connect;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

public abstract class ConnectThread extends Thread
{
	private int connectPort;
	private InetAddress computerInet;
    private Socket socket;
    private OutputStream ou;
    private InputStream in;
    private boolean ifconnect=false;
    private boolean run=true;
    protected boolean stop=false;

	
	public ConnectThread(int connectPort, InetAddress computerInet)
	{
		this.connectPort=connectPort;
		this.computerInet=computerInet;

	}

	public boolean getIfConnect(){return this.ifconnect;}
	
	abstract public void management(OutputStream ou, InputStream in);
	
 	public void run()
    {
    	try {
		Log.v("连接目标端口:",connectPort+"...");
		this.socket=new Socket(computerInet,connectPort);
		this.ou=this.socket.getOutputStream();
		this.in=this.socket.getInputStream();
		ifconnect=true;
		Log.v("连接目标端口:",connectPort+"成功");

	   }
	    catch (SocketException e) {e.printStackTrace();}
	    catch (IOException e) {e.printStackTrace();}

	    if(ifconnect) {
			Log.v("端口" + connectPort + "接受线程运行成功。。。", "d");
			try {

				byte[] buf = new byte[1024];

				while (run) {
					while (stop) {
						Thread.sleep(1000);
					}
					management(ou, in);

				}


			} catch (Exception e) {
				e.printStackTrace();
			}
		}
     }

     protected byte[] readInfo(InputStream in)
	 {
	 	try{
		 byte b[]=new byte[1024*1024];
		 int len=in.read(b);

		 byte bs[]=new byte[len];
		 for(int i=0;i<len;i++)
		 	bs[i]=b[i];

		 return bs;

	     }catch(IOException e){e.printStackTrace();}//接收信息
		 return null;
	 }

	 protected void writeInfo(OutputStream ou, String message)
	 {
		 try {
			 ou.write(message.getBytes());
		 } catch (IOException e) { e.printStackTrace();}
	 }

	protected void writeInfo2(OutputStream ou,byte b[])
	{
		try {
			ou.write(b);
		} catch (IOException e) { e.printStackTrace();}
	}

	 protected  void setStop(boolean stop)
	 {
	 	this.stop=stop;
	 }


	 protected  void setRun(boolean run){
 		setStop(false);
 		this.run=run;
 		Log.v("断开连接：",connectPort+"");
 	}





}
	
	
	
