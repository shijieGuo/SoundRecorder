package connect;


import android.content.Context;
import android.os.Handler;

import java.net.InetAddress;
import java.net.UnknownHostException;



public class Connect {
	
		private int connectPort[];
		private String computerIP;
		private InetAddress computerInet;
		private boolean ifconnect=false;

		private ConnectThread[] connectthread;

		private Handler mHandler;
			
		public Connect(int [] connectPort, String computerIP, Handler mHandler, Context context)
		{
			try {
				this.connectPort=connectPort;
				this.computerIP=computerIP;
				this.computerInet=InetAddress.getByName(this.computerIP);
				this.mHandler=mHandler;

			} catch (UnknownHostException e) {e.printStackTrace();}

			connectthread=new ConnectThread[connectPort.length];
			connectthread[0]=(ConnectThread)(new Connectport1(connectPort[0],computerInet,context));//发送文件
			connectthread[1]=(ConnectThread)(new Connectport2(connectPort[1],computerInet,context));//接受文件
			connectthread[2]=(ConnectThread)(new Connectport3(connectPort[2],computerInet,mHandler));//发送指令
		}
		
		public void startConnect()
		{


		     	connectthread[0].start();
			    connectthread[1].start();
			    connectthread[2].start();
			
		}
		
		public boolean getIfConnect()
		{
			ifconnect=true;
			for(int i=0;i<connectthread.length;i++)
				if(connectthread[i].getIfConnect()==false)
				{
					ifconnect=false;break;
				}
			return ifconnect;
		}

		public ConnectThread getConnectThread(int index)
		{
			return connectthread[index];
		}

		public void setOut()
		{
			for(int i=0;i<connectthread.length;i++)
				connectthread[i].setRun(false);
			InetThread.inetThread.refreshInetThread();
		}



		
		
		
			
			
			
			
			
		

}
