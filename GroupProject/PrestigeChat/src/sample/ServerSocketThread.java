package sample;

import java.io.*;
import java.net.*;

class ServerSocketThread extends SocketsThread
{
	public ServerSocketThread(int port)
	{
		super(port);
	}

	public void run()
	{
		try
		{
			//makes the server listen for messages
			sSocket = new ServerSocket(port);
			cSocket = sSocket.accept();
			System.out.println("connected to the client");
			cSocket.setTcpNoDelay(true);

			listen();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}