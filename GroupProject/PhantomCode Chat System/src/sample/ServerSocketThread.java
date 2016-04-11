package sample;

import java.net.*;
import java.io.*;

class ServerSocketThread extends SocketsThread
{

	public String message;
	private FileTransferThread transferFiles;
	private serverStart1 Alpha;

	public ServerSocketThread(int port, serverStart1 Alpha)
	{
		super(port);
		this.Alpha = Alpha;
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
			while(true)
			{
				message = listen();
				if (message.toLowerCase().contains("/upload"))
				{
					transferFiles = new FileTransferThread();
					sendMessage("/file waiting to be downloaded");
					transferFiles.sendFile(new File("./message.wav"));
					transferFiles.close();
				}
				else if (message.toLowerCase().contains("/file"))
				{
					transferFiles.downloadFile("message.wav");
					//downloadFile(("message.wav"));
				}
				else if (message !="Client: ")
				{
					Alpha.messages.appendText(message+"\n");
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}