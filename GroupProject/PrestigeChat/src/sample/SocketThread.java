package sample;

import java.io.*;
import java.net.*;
import java.util.Scanner;

class SocketThread extends SocketsThread
{
	public String message;
	public SocketThread(int port, String ip)
	{
		super(port,ip);
	}
	public void run()
	{
		try 
		{
			//makes the client listen for messages
			cSocket = new Socket(ip,port);
			System.out.println("Connected to server");
			cSocket.setTcpNoDelay(true);
			
			while (true)
			{
				Controller.messages.appendText(listen()+"\n");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}