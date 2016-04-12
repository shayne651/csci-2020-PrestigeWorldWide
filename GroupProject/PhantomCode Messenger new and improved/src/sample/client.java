package sample;

import java.net.*;
import java.io.*;

class client extends Thread
{
	private static boolean upload;
	private static File file;
	private static int size;
	private static String fileName;
	private static String ipA;

	public client(String filename, String ipA)
	{
		//calls the super class to start the thread
		super();
		//gives the file if it can
		this.fileName=filename;
		//upload or download
		upload=false;
		//random size (i couldnt figure out how to get the actual size)
		size=2000000;
		this.ipA = ipA;
	}
	public client(File file)
	{
		//calls the super class to start the thread
		super();
		//gives the file if it can
		this.file=file;
		//upload or download
		upload = true;
		//random size (i couldnt figure out how to get the actual size)
		size=2000000;
	}
	public void run()
	{
		//threads "main" function
		if (upload == false)
		{
			download();
		}
		else 
		{
			sendFile();
		}
	}
	public void download()
	{
		try 
		{
			System.out.println("connecting to server...");
			int read;
			int currentTotal=0;
			//my local ip used
			Socket socket = new Socket(ipA,5555);
			System.out.println("connected");
			//copying the file from the server to the client's computer 
			byte [] transferFile = new byte[2000000];
			InputStream is = socket.getInputStream();
			FileOutputStream fos = new FileOutputStream("copy-"+fileName);
			BufferedOutputStream out = new BufferedOutputStream(fos);
			read = is.read(transferFile,0,transferFile.length);
			currentTotal = read;

			do
			{
				read = is.read(transferFile,currentTotal,(transferFile.length-currentTotal));
				if (read>=0)
				{
					currentTotal+=read;
				}
			}while (read > -1);

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
	public void sendFile()
	{
		try
		{
			ServerSocket sSocket = new ServerSocket(4444);
			Socket cSocket = sSocket.accept();
			System.out.println("Connection Accepted");
			//sending the file from the server to the client
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
			sSocket.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}