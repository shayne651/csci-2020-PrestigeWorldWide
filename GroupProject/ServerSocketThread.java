import java.io.*;
import java.net.*;

class ServerSocketThread extends SocketsThread
{

	public String message;

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
			while(true)
			{
				message = listen();
				if (message.toLowerCase().contains("/upload"))
				{
					sendMessage("/file waiting to be downloaded");
					sendFile(new File("./message.wav"));
				}
				else if (message.toLowerCase().contains("/file"))
				{
					downloadFile(("message.wav"));
				}
				else
				{
					serverStart1.messages.appendText(message+"\n");
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}