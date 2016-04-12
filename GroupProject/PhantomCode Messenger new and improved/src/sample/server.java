package sample;

import java.net.*;
import java.io.*;

class server extends Thread 
{
	private static Socket cSocket = null;
	private static boolean upload;
	private static File file;
	private static int size;
	private static String fileName;
	private static String ipA;

	public server(File file)throws Exception
	{
		super();
		upload=true;
		this.file=file;
		//the upload was used to tell the server if it is downloading or uploading files, I would have changed it if i had ever gotten dir working but i couldnt so i left it 
	}
	
	public server(String ip)throws Exception
	{
		super();
		ip=ipA;
		upload=true;
		size = 200000;
		//the upload was used to tell the server if it is downloading or uploading files, I would have changed it if i had ever gotten dir working but i couldnt so i left it 
	}
	
	public void run()
	{
		if (upload == false)
		{
			sendFile();
		}
		else 
		{
			getFile();	
		}
	}
	public void sendFile()
	{
		try
		{
			ServerSocket sSocket = new ServerSocket(5555);

			cSocket = sSocket.accept();
			//sending the file name to the server to download it
			OutputStream os = cSocket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			BufferedWriter out1 = new BufferedWriter(osw);
			String fileName = file.getName();
			String message = fileName +"\n";
			out1.write(message);
			out1.flush();
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
	public void getFile()
	{
		try 
		{
			System.out.println("V");
			int read;
			int currentTotal=0;
			//my local ip used
			Socket cSocket = new Socket(ipA,4444);
			//receiving filename from client
			InputStream is = cSocket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader in = new BufferedReader(isr);
			fileName = in.readLine();
			//
			System.out.println("Downloading file...");
			//copying the file from the server to the client's computer 
			byte [] transferFile = new byte[2000000];
			is = cSocket.getInputStream();
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
			cSocket.close();
			System.out.println("transfer complete");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}