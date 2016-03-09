class Testing
{
	protected Map <String, Float> wordP;
	protected Map
	public static void main(String[] args) 
	{
		Float n;
		wordP = new Training().wordP;
		try 
		{	
			File directory = new File("data/test/ham/");
			File[] textFilesH = directory.listFiles();
			directory = new File("data/test/spam");
			File[] textFilesS = directory.listFiles();
			for (File file : textFilesH)
			{
				Scanner sc = new Scanner(file);
				while (sc.hasNext())
				{
					//reccursivley adds the probability value of the word being used in spam to a variable called n
					String word = sc.next();
					Float val = wordp.get(new String (word));
					n+=val;
					//after this we can plug into the formula to find the proability of the file being spam. Need a Map or list to hold the Name of the file as well as the probability that it is spam. I will decide what to keep it in once i review how the UI sets up tables with predetermined values 
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}