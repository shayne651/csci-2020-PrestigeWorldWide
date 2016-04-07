package sample;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public class FileTransferThread extends Thread
{
	private Socket cSocket;
	private ServerSocket sSocket;
	
	public FileTransferThread()throws Exception
	{
		sSocket = new ServerSocket(4444);
		cSocket = sSocket.accept();
		
	}
	
	public FileTransferThread(String ip)throws Exception
	{
		cSocket = new Socket(ip,4444);
	}
	public void sendFile(File file)throws Exception
	{
		//sending the file
		byte[] transferFile = new byte[(int)file.length()];
		FileInputStream fin = new FileInputStream(file);
		BufferedInputStream bin = new BufferedInputStream(fin);
		bin.read(transferFile,0,transferFile.length);
		OutputStream out = cSocket.getOutputStream();
		out.write(transferFile,0,transferFile.length);
		out.flush();
		bin.close();
	}
	
	public void downloadFile(String name)throws Exception
	{
		//Receiving the fileSize
		InputStream is = cSocket.getInputStream();
		DataInputStream read = new DataInputStream(is);
		int length = read.readInt();
		//Receiving the file
		byte[] newFile = new byte[length];
		FileOutputStream fos = new FileOutputStream(name);
		BufferedOutputStream out  = new BufferedOutputStream(fos);
		int bytesRead = is.read(newFile,0,newFile.length);
		int currentTotal = bytesRead;

		while(bytesRead > -1)
		{
			bytesRead = is.read(newFile,currentTotal,(newFile.length-currentTotal));
			if (bytesRead >= 0)
			{
				currentTotal+=bytesRead;
			}
		}
		out.write(newFile,0,currentTotal);
		out.flush();
		out.close();
	}
	public void close()throws Exception
	{
		sSocket.close();
		cSocket.close();
	}
}