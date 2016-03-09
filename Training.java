import java.util.Scanner;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

class Training
{
	private static Map<String, Float> hamP;
	private static Map<String, Float> spamP;
	private static int hamFiles;
	private static int spamFiles;

	public static void main(String[] args) 
	{
		HashMap<String,Integer> trainHamFreq = new HashMap<String,Integer>();
		HashMap<String,Integer> trainSpamFreq = new HashMap<String,Integer>();
		try
		{
			hamFiles =0;
			spamFiles = 0;
			File directory = new File("data/train/ham/");
			File[] textFiles = directory.listFiles();

			for (File file :textFiles)
			{
				hamFiles++;
				Scanner sc = new Scanner(file);
				while (sc.hasNext())
				{
					String word = sc.next();
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
			}

			directory = new File("data/train/spam/");
			textFiles = directory.listFiles();

			for (File file :textFiles)
			{
				spamFiles++;
				Scanner sc = new Scanner(file);
				while (sc.hasNext())
				{
					String word = sc.next();
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
			}
			probabilitiesCalculations(trainHamFreq,trainSpamFreq);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void probabilitiesCalculations(HashMap<String,Integer> ham , HashMap<String,Integer> spam)
	{
		hamP = new TreeMap<String,Float>();
		spamP = new TreeMap<String,Float>();
		Iterator<Map.Entry<String,Integer>> it = ham.entrySet().iterator();
		while (it.hasNext())
		{
			Map.Entry<String,Integer> hamEntry =it.next();
			String hamWords = hamEntry.getKey();
			int hamWordsInt = hamEntry.getValue();
			System.out.println(hamWords);
			Float hamProbability = ((float)hamWordsInt)/hamFiles;
			hamP.put(hamWords,hamProbability);
		}
		// Iterator<Map.Entry<String,Integer>> it1 =spam.entrySet().iterator();
		// while (it1.hasNext())
		// {
		// 	Map.Entry<String,Integer> spamEntry =it.next();
		// 	String spamWords = spamEntry.getKey();
		// 	int spamWordsInt = spamEntry.getValue();
		// 	Float spamProbability = ((float)spamWordsInt)/spamFiles;
		// 	spamP.put(spamWords,spamProbability);
		// }
	}
}