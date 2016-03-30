import java.io.*;

class Test
{
	public static void main(String[] args) throws Exception {
		//starts the sever thread
		//the parameters for the server are as follows (Boolean) if you want to upload a file use true if you want to download use false
		//(File) you give it the File so it can use the name to download/upload the file
		server testServer = new server(false , new File ("string"));
		//starts the thread
		testServer.start();
	}
}