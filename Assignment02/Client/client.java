import java.net.*;
import java.io.*;

class client extends Thread
{
	private static boolean upload;
	private static File file;
	private static int size;

	public client(boolean upload , File file)
	{
		//calls the super class to start the thread
		super();
		//gives the file if it can
		this.file=file;
		//upload or download
		this.upload=upload;
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
			uploadTServer();
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
			Socket socket = new Socket("192.168.0.29",8080);
			System.out.println("connected");

			//sending the file name to the server to download it
			OutputStream os = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			BufferedWriter out1 = new BufferedWriter(osw);
			String fileName = file.getName();
			String message = fileName +"\n";
			out1.write(message);
			out1.flush();

			//copying the file from the server to the client's computer 
			byte [] transferFile = new byte[2000000];
			InputStream is = socket.getInputStream();
			FileOutputStream fos = new FileOutputStream("copy-"+file.getName());
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
	public void uploadTServer()
	{
		try
		{
			//giving the ip to the server for a file transfer
			Socket socket = new Socket("192.168.0.29",8080);
			socket.close();
			ServerSocket sSocket = new ServerSocket(8080);
			//starts the uploading process here 
			Socket cSocket = sSocket.accept();

			//sending the file name to the server to download it
			OutputStream os = cSocket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			BufferedWriter out1 = new BufferedWriter(osw);
			String fileName = file.getName();
			String message = fileName +"\n";
			out1.write(message);
			out1.flush();

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