package sample;

import java.net.*;
import javafx.scene.control.TextArea;

class SocketThread extends SocketsThread
{
	private String message;
	private TextArea messages;
	private clientStart Alpha;
	
	public SocketThread(int port, String ip, clientStart Alpha)
	{
		super(port,ip);
		this.Alpha = Alpha;
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
				message = listen();
				if (message.toLowerCase().contains("/upload"))
				{
					sendMessage("/file waiting to be downloaded");
					//sendFile(new File("./message.wav"),ip);
				}
				else if (message.toLowerCase().contains("/file"))
				{
					//downloadFile(("message.wav"),ip);
				}
				else if (message != "")
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