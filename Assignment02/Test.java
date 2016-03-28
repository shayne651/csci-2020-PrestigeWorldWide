class Test
{
	public static void main(String[] args) {
		server testServer = new server();
		client testClient = new client();
		testServer.start();
		testClient.start();
	}
}