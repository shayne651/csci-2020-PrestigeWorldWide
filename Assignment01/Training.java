import java.util.Scanner;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;

class Training
{
	private static Map<String, Float> hamP;
	private static Map<String, Float> spamP;
	private static int hamFiles;
	private static int spamFiles;
	protected static Map<String, Float> wordP;
	protected static Map<String, Double> probabilityFiles;

	public static void main(String[] args) 
	{
		HashMap<String,Integer> trainHamFreq = new HashMap<String,Integer>();
		HashMap<String,Integer> trainSpamFreq = new HashMap<String,Integer>();
		try
		{
			//used for counting the number of files (used for the probabilities later)
			hamFiles =0;
			spamFiles = 0;
			//used for reading every file in a folder
			File directory = new File("data/train/ham/");
			File[] textFiles = directory.listFiles();
			//this will hold all the words in a file so it will only count a word once per file
			ArrayList<String> words = new ArrayList<String>();
			//used for checking if the word has been used before 
			Boolean found =false;

			for (File file :textFiles)
			{
				//clears words so it will count each word once per file only 
				words.clear();
				hamFiles++;
				//selects a new file every itteration 
				Scanner sc = new Scanner(file);
				while (sc.hasNext())
				{
					found = false;
					String word = sc.next();
					//checks if the word has been used once in the document already
					for (String check : words)
					{
						if (check == word)
						{
							found = true;
							break;
						}
					}
					//counts the word only once per document 
					if (found == true)
					{
						
					}
					else 
					{
						Integer val = trainHamFreq.get(new String(word));
						if (val != null)
						{
							trainHamFreq.put(word,new Integer(val+1));
						}
						else 
						{
							trainHamFreq.put(word,1);
						}
					}
					words.add(word);
				}
			}

			directory = new File("data/train/spam/");
			textFiles = directory.listFiles();

			for (File file :textFiles)
			{
				words.clear();
				spamFiles++;
				Scanner sc = new Scanner(file);
				while (sc.hasNext())
				{
					found= false;
					String word = sc.next();
					for (String check : words)
					{
						if (check == word)
						{
							found = true;
							break;
						}
					}
					if (found == true)
					{
						System.out.println("already done");
					}
					else
					{
						Integer val = trainSpamFreq.get(new String(word));
						if (val != null)
						{
							trainSpamFreq.put(word,new Integer(val+1));
						}
						else 
						{
							trainSpamFreq.put(word,1);
						}
					}
					words.add(word);
				}
			}
			probabilitiesCalculations(trainHamFreq,trainSpamFreq);
			Testing();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void probabilitiesCalculations(HashMap<String,Integer> ham , HashMap<String,Integer> spam)
	{
		wordP = new TreeMap<String,Float>();
		hamP = new TreeMap<String,Float>();
		spamP = new TreeMap<String,Float>();
		Iterator<Map.Entry<String,Integer>> it = ham.entrySet().iterator();
		while (it.hasNext())
		{
			Map.Entry<String,Integer> hamEntry =it.next();
			String hamWords = hamEntry.getKey();
			int hamWordsInt = hamEntry.getValue();
			Float hamProbability = ((float)hamWordsInt)/hamFiles;
			hamP.put(hamWords,hamProbability);
		}
		Iterator<Map.Entry<String,Integer>> it1 =spam.entrySet().iterator();
		while (it1.hasNext())
		{
			Map.Entry<String,Integer> spamEntry =it1.next();
			String spamWords = spamEntry.getKey();
			int spamWordsInt = spamEntry.getValue();
			Float spamProbability = ((float)spamWordsInt)/spamFiles;
			spamP.put(spamWords,spamProbability);
		}
		Iterator<Map.Entry<String,Float>> it2 = hamP.entrySet().iterator();
		while(it2.hasNext())
		{
			Map.Entry<String,Float> wordsP = it2.next();
			String wordHold = wordsP.getKey();
			Float hamProb = wordsP.getValue();
			Float spamProb = spamP.get(new String(wordHold));
			if(spamProb == null)
			{
				wordP.put(wordHold,0f);
			}
			else
			{
				Float wordProb = (spamProb)/(spamProb+hamProb);
				wordP.put(wordHold,wordProb);
			}
		}
	}
	public static void Testing()
	{
		Float n;
		probabilityFiles = new TreeMap<String, Double>();
		try 
		{	
			File directory = new File("data/test/ham/");
			File[] textFilesH = directory.listFiles();
			directory = new File("data/test/spam");
			File[] textFilesS = directory.listFiles();

			for (File file : textFilesH)
			{
				n=0f;
				Scanner sc = new Scanner(file);
				PrintWriter writer = new PrintWriter("WordProbabilities","UTF-8");
				while (sc.hasNext())
				{
					//reccursivley adds the probability value of the word being used in spam to a variable called n
					String word = sc.next();
					Float val = wordP.get(new String (word));
					System.out.println(val);
					if (val!=null)
					{
						n+=val;
					}
					//after this we can plug into the formula to find the proability of the file being spam. Need a Map or list to hold the Name of the file as well as the probability that it is spam. I will decide what to keep it in once i review how the UI sets up tables with predetermined values 
				}
				Double fileProbability =1/(1+(Math.pow(Math.E,n)));
				String fileName = file.getName();
				probabilityFiles.put(fileName,fileProbability);
				Iterator<Map.Entry<String,Double>> it = probabilityFiles.entrySet().iterator();
				while (it.hasNext())
				{
					Map.Entry<String,Double> wordsP = it.next();
					String wordHold = wordsP.getKey();
					Double prob = wordsP.getValue();
					writer.println(wordHold + " " + prob);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}