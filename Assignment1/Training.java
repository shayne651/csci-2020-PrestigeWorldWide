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
	private static String path;
	protected static Map<String, Float> wordP;
	protected static Map<String, Double> probabilityFiles;
	private static double SpamFiles1;
	private static int HamFiles1;
	public static double precision;
	public static double accuracy;
	private static int wrong;
	private static File[] textFilesH;
	private static File[] textFilesS;

	public Training(String path){
		this.path = path;
	}

	public static void Train() 
	{
		HashMap<String,Integer> trainHamFreq = new HashMap<String,Integer>();
		HashMap<String,Integer> trainSpamFreq = new HashMap<String,Integer>();
		try
		{
			//used for counting the number of files (used for the probabilities later)
			hamFiles =0;
			spamFiles = 0;
			//used for reading every file in a folder
			File directory = new File(path + "/train/ham/");
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

			directory = new File(path + "/train/spam/");
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
			AccuracyAndPrecision();
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
		//uses the existing maps to make new ones with probabilites for the words being spam
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
		//holds the variable that is n
		Double n;
		//holds the end results for the File name with the Probability of if the file is 
		probabilityFiles = new TreeMap<String, Double>();
		try 
		{	
			File directory = new File(path + "/test/ham/");
			textFilesH = directory.listFiles();
			directory = new File(path + "/test/spam");
			textFilesS = directory.listFiles();

			for (File file : textFilesH)
			{
				n=0.0;
				//used for writing out the results to a file
				Scanner sc = new Scanner(file);
				PrintWriter writer = new PrintWriter("WordProbabilities","UTF-8");
				while (sc.hasNext())
				{
					//reccursivley adds the probability value of the word being used in spam to a variable called n
					String word = sc.next();
					Float val = wordP.get(new String (word));
					//makes sure that val is not null or 0 because they give wrong values
					if (val!=null && val !=0)
					{
						n+=(Math.log(1-val)-Math.log(val));
					}
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
	public static void AccuracyAndPrecision()
	{
		SpamFiles1=0;
		precision=0;

		try
		{
			//the following forloops and vairbales are turning a array of files into an array of Strings to make for easier comparing with other Strings
			File directory = new File(path + "/test/ham/");
			File[] textFilesH = directory.listFiles();
			String[] stringFileH = new String[textFilesH.length];
			directory = new File(path + "/test/spam");
			File[] textFilesS = directory.listFiles();
			String [] stringFileS= new String[textFilesS.length];
			int i=0;
			for (File file :textFilesS)
			{
				stringFileS[i] = file.getName();
				i++;
			}
			i=0;
			for (File file :textFilesH)
			{
				stringFileH[i] = file.getName();
				i++;
			}
			//scanning in the word document with all of the file names and probabilities so we can do the calculations
			Scanner sc = new Scanner(new File("WordProbabilities"));
			while (sc.hasNext())
			{
				String fName = sc.next();
				Double val;
				//not sure what the error was here but this was needed for it to run
				try 
				{
					val = Double.parseDouble(sc.next());
				}
				catch (Exception e)
				{
					val= 0.0;
				}
				//if anythign is above a 90% chance it is spam we consider it spam
				if (val > 0.90 )
				{
					if (contains(stringFileS,fName))
					{
						//counts our spam files
						SpamFiles1++;
					}
					else 
					{
						//counts our spam files and the files that we got wrong
						SpamFiles1++;
						wrong++;
					}
				}
				else 
				{
					if (contains(stringFileH,fName))
					{
						//counts ham files
						HamFiles1++;
					}
					else 
					{
						//counts ham files and incorrect files
						HamFiles1++;
						wrong++;
					}
				}
			}
			//finds the total files that we scanned
			double total = spamFiles+hamFiles;
			double correct = total - wrong;
			accuracy =correct/total;
			precision = SpamFiles1/spamFiles;
			PrintWriter writer = new PrintWriter("AnP","UTF-8");
			writer.println(accuracy +" "+precision);

			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static Boolean contains(String[] list , String val)
	{
		//searches a array for a input
		for (int i=0;i<=list.length-1;i++)
		{
			if (list[i].equals(val))
			{
				return true;
			}
		}
		return false;
	}
	public double getAcc()
	{
		//used in the UI
		return accuracy;
	}
	public double getPrec()
	{
		//used in the UI
		return precision;
	}
}