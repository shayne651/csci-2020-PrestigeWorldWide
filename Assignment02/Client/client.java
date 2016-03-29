import java.net.*;
import java.io.*;

class client extends Thread
{
	private static boolean upload;
	private static File file;
	private static int size;
	private static Socket socket = null;

	public client(boolean upload , File file)
	{
		super();
		this.file=file;
		this.upload=upload;
		size=2022386;
		
			System.out.println(file.getName());
	}
	public void run()
	{
		if (upload == false)
		{
			download();
		}
		else 
		{
			uploadTServer();
		}
	}
	public void download()
	{
		try 
		{
			System.out.println("connecting to server...");
			int bytesRead=0;
			int currentTotal=0;
			socket = new Socket("10.120.39.35",8080);
			byte [] transferFile = new byte[size];
			InputStream is = socket.getInputStream();
			FileOutputStream fos = new FileOutputStream("copy-"+file.getName());
			BufferedOutputStream out = new BufferedOutputStream(fos);
			bytesRead = is.read(transferFile,0,transferFile.length);
			currentTotal = bytesRead;

			while (bytesRead > 0)
			{
				bytesRead=is.read(transferFile,currentTotal,(transferFile.length-currentTotal));
				if (bytesRead>=0)
				{
					currentTotal+=bytesRead;
				}
			}

			out.write(transferFile,0,currentTotal);
			out.flush();
			out.close();
			socket.close();
			System.out.println("transfer complete");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void uploadTServer()
	{
		try
		{
			ServerSocket sSocket = new ServerSocket(8080);

			Socket cSocket = sSocket.accept();
			System.out.println("Connected to Server");

			System.out.println(file.length());
			byte[] transferFile = new byte[(int)file.length()];
			FileInputStream fin = new FileInputStream(file);
			BufferedInputStream bin = new BufferedInputStream(fin);
			bin.read(transferFile,0,transferFile.length);
			OutputStream out = cSocket.getOutputStream();
			out.write(transferFile,0,transferFile.length);
			out.flush();
			System.out.println("File Sent to Server Successfully");
			cSocket.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}