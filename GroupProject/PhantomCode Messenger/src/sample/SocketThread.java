package sample;

import java.net.*;
import javafx.scene.control.TextArea;

class SocketThread extends SocketsThread
{
	private String message;
	private TextArea messages; 
	
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
				else if (message != null)
				{
					clientStart.messages.appendText(message+"\n");
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}