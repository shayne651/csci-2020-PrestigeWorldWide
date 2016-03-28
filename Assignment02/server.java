import java.net.*;
import java.io.*;

class server extends Thread 
{
	private static Socket cSocket = null;

	public server()throws Exception
	{
		super();

		ServerSocket sSocket = new ServerSocket(8080);

		cSocket = sSocket.accept();
		System.out.println("Connection Accepted");

		download(new File("test.txt"));	
	}
	public static void upload(File file) throws Exception
	{
		byte[] transferFile = new byte[(int)file.length()];
		FileInputStream fin = new FileInputStream(file);
		BufferedInputStream bin = new BufferedInputStream(fin);
		bin.read(transferFile,0,transferFile.length);
		FileOutputStream out = new FileOutputStream(file.getName());
		out.write(transferFile,0,transferFile.length);
		out.flush();
		System.out.println("File uploaded to server Successfully");
	}
	public static void download(File file)
	{
		try
		{
			byte[] transferFile = new byte[(int)file.length()];
			FileInputStream fin = new FileInputStream(file);
			BufferedInputStream bin = new BufferedInputStream(fin);
			bin.read(transferFile,0,transferFile.length);
			OutputStream out = cSocket.getOutputStream();
			out.write(transferFile,0,transferFile.length);
			out.flush();
			System.out.println("File uploaded to server Successfully");
			cSocket.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}