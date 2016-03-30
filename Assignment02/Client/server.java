import java.net.*;
import java.io.*;

class server extends Thread 
{
	private static Socket cSocket = null;
	private static boolean upload;
	private static File file;
	private static int size;
	private static String ipA;

	public server(boolean upload , File file)throws Exception
	{
		super();
		this.upload=upload;
		this.file=file;
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
			downloadFClient();		}
	}
	public void sendFile()
	{
		try
		{
			ServerSocket sSocket = new ServerSocket(8080);

			cSocket = sSocket.accept();

			//receiving filename from client 
			InputStream is = cSocket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader in = new BufferedReader(isr);
			file = new File(in.readLine());

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
			//getting ip for the client
			ServerSocket sSocket = new ServerSocket(8080);
			Socket tempS = sSocket.accept();
			ipA = tempS.getLocalAddress().toString();
			tempS.close();

			int bytesRead;
			int currentTotal=0;
			Socket socket = new Socket(ipA,8080);
			System.out.println("connected");
			
			
			//receiving filename from client 
			InputStream is = cSocket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader in = new BufferedReader(isr);
			file = new File(in.readLine());

			//transfering the data from the client to the server (may work never actually tested)
			byte [] transferFile = new byte[2000000];
			is = socket.getInputStream();
			FileOutputStream fos = new FileOutputStream("ServerCopy-"+file.getName());
			BufferedOutputStream out = new BufferedOutputStream(fos);
			bytesRead = is.read(transferFile,0,transferFile.length);
			currentTotal = bytesRead;

			
			while (bytesRead > -1)
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
	//attemped the dir but i didnt understand it completly 
	// public void DIR()
	// {
	//  	try
	//  	{
	//  		ServerSocket sSocket = new ServerSocket(8080);
	//  		cSocket = sSocket.accept();
	//  		InputStream is = cSocket.getInputStream();
	//  		ObjectInputStream out = new ObjectInputStream(is);
	//  		ServerDir serverDir =(ServerDir)out.readObject();
	//  	}
	//  	catch(Exception e)
	//  	{

	//  	}
	// }
	//this was an attempt to make the server run on network instead of essentually being lan
	//i was trying to make the client tell the server what taks it wants it to exicute but i ran out of time
	// public void taks()
	// {
	// 	try
	// 	{
	// 		ServerSocket sSocket = new ServerSocket(8080);

	// 		Socket cSocket = sSocket.accept();
	// 		//receiving instructions from client 
	// 		InputStream is = cSocket.getInputStream();
	// 		InputStreamReader isr = new InputStreamReader(is);
	// 		BufferedReader in = new BufferedReader(isr);
	// 		int choice = Integer.parseInt(in.readLine());
	// 		switch(choice)
	// 		{
	// 			case 0:
	// 				sendFile();
	// 			case 1:
	// 				downloadFClient();
	// 			case 2:
	// 				DIR();
	// 			default: 
	// 				System.out.println("Invalid Option");
	// 		}

	// 	}
	// 	catch(Exception e)
	// 	{

	// 	}
	// }
}