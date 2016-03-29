import java.net.*;
import java.io.*;

class server extends Thread 
{
	private static Socket cSocket = null;
	private static boolean upload;
	private static File file;
	private static int size;

	public server(boolean upload , File file)throws Exception
	{
		super();
		this.upload=upload;
		this.file=file;
	}
	public void run()
	{
		if (upload == false)
		{
			sendFile();
		}
		else 
		{
			downloadFClient();		}
	}
	public void sendFile()
	{
		try
		{
			ServerSocket sSocket = new ServerSocket(8080);

			cSocket = sSocket.accept();
			System.out.println("Connection Accepted");

			File file = new File("test.txt");
			System.out.println(file.length());
			byte[] transferFile = new byte[(int)file.length()];
			FileInputStream fin = new FileInputStream(file);
			BufferedInputStream bin = new BufferedInputStream(fin);
			bin.read(transferFile,0,transferFile.length);
			OutputStream out = cSocket.getOutputStream();
			out.write(transferFile,0,transferFile.length);
			out.flush();
			System.out.println("File Sent to client Successfully");
			cSocket.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void downloadFClient()
	{
		try 
		{
			System.out.println("Client Sending File..." + size);
			int bytesRead=0;
			int currentTotal=0;
			Socket socket = new Socket("127.168.0.15",8080);
			byte [] transferFile = new byte[size];
			InputStream is = socket.getInputStream();
			FileOutputStream fos = new FileOutputStream(file.getName());
			BufferedOutputStream out = new BufferedOutputStream(fos);
			bytesRead = is.read(transferFile,0,transferFile.length);
			currentTotal = bytesRead;

			while (bytesRead > 0)
			{
				bytesRead = is.read(transferFile,currentTotal,(transferFile.length-currentTotal));
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
	// public File DIR()
	// {
	// 	try
	// 	{
	// 		ServerSocket sSocket = new ServerSocket(8080);
	// 		cSocket = sSocket.accept();
	// 		InputStream is = cSocket.getInputStream();
	// 		ObjectInputStream out = new ObjectInputStream(is);
	// 		ServerDir serverDir = new ServerDir(file);
	// 	}
	// 	catch(Exception e)
	// 	{

	// 	}
	// }
}