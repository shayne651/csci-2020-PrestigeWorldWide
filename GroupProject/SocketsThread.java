import java.io.*;
import java.net.*;
import java.util.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

//I made this abstract Socket class to make sure everytime i make a Socket that it will be its own thread and it also prevented me to have to 
//re write my code for every time i want to start either the client or server 

public abstract class SocketsThread extends Thread
{
	//needed variables
	protected Socket cSocket;
	protected int port;
	protected String ip;
	protected ServerSocket sSocket;
	protected String message1;

	public SocketsThread(int port)
	{
		//this will start the client end of the chat is only a port is given
		super();
		this.port=port;
	}

	public SocketsThread(int port, String ip)
	{
		//this will start the server end of the chat if the ip and port are given 
		super();
		this.port=port;
		this.ip=ip;
	}

	public abstract void run();//this needs to be made differently for server and client so i left it to be made in the class

	public String byteTString(byte[] bytes) throws Exception
	{
		//converts a byte array to a String
		String words = new String(bytes,"UTF-8");
		return words;
	}

	public byte[] stringTByte(String words) throws Exception
	{
		//changes a string to a byte array
		byte[] bytes = words.getBytes();
		return bytes;
	} 

	public void sendMessage(String message)throws Exception
	{
		//will convert the message string into a byte array and then send it
		byte[] messageBytes = stringTByte(message);
		OutputStream out = cSocket.getOutputStream();
		DataOutputStream writer = new DataOutputStream(out);
		int length = messageBytes.length;
		writer.writeInt(length);
		if (length>0)
		{
			writer.write(messageBytes,0,length);
		}
	}

	public String getMessage()throws Exception
	{
		//recives the incoming chat messages
		InputStream is = cSocket.getInputStream();
		DataInputStream read = new DataInputStream(is);
		int length = read.readInt();
		byte[] messageBytes = new byte[length];
		if (length>0)
		{
			read.readFully(messageBytes);
		}
		String message = byteTString(messageBytes);
		return message;
	}

	public void listen()throws Exception
	{
		//the listen function will just wait to receive the messages
		while(true)
		{
			message1 = getMessage();
			playSound();
			System.out.println(message1);
		}
	}

	public void sendFile(File file)throws Exception
	{
		//sending the fileSize
		String length = String.valueOf(file.length());
		sendMessage(length);
		//sending the file
		byte[] transferFile = new byte[(int)file.length()];
		FileInputStream fin = new FileInputStream(file);
		BufferedInputStream bin = new BufferedInputStream(fin);
		bin.read(transferFile,0,transferFile.length);
		OutputStream out = cSocket.getOutputStream();
		out.write(transferFile,0,transferFile.length);
		out.flush();
	}

	public void receiveFile(String name)throws Exception
	{
		//receving the fileSize
		InputStream is = cSocket.getInputStream();
		DataInputStream read = new DataInputStream(is);
		int length = read.readInt();
		//receving the file
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

	public void playSound() throws Exception
	{
		//plays a sound
		AudioInputStream audioIn = AudioSystem.getAudioInputStream(SocketsThread.class.getResource("message.wav"));
		Clip clip = AudioSystem.getClip();
		clip.open(audioIn);
		clip.start();
	}
}