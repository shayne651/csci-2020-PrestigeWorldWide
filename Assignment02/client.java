import java.net.*;
import java.io.*;

class client extends Thread
{
	private static Socket socket = null;

	public client()
	{
		super();
		try
		{
			download();
		}
		catch(Exception e)
		{

		}
	}
	public static void download() throws Exception
	{
		System.out.println("connecting to server...");
		int bytesRead=0;
		int currentTotal=0;
		socket = new Socket("127.0.0.1",8080);
		byte [] transferFile = new byte[2022386];
		InputStream is = socket.getInputStream();
		FileOutputStream fos = new FileOutputStream("../../copy");
		BufferedOutputStream out = new BufferedOutputStream(fos);
		bytesRead = is.read(transferFile,0,transferFile.length);
		currentTotal = bytesRead;

		while (bytesRead > -1)
		{
			is.read(transferFile,currentTotal,(transferFile.length-currentTotal));
			if (bytesRead>=0)
			{
				currentTotal+=bytesRead;
			}
		}

		out.write(transferFile,0,currentTotal);
		out.flush();
		out.close();
		socket.close();
	}
}