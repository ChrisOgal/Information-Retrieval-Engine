/**
 * 
 */
package Indexing;

import java.io.*;
import tokenizer.*;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author chris
 * Class used for the purposes of reading in the parsed documents, tokenizing, creating postingslists for each block and writing them
 * to the dictionary directory. After this, the accumulated dictionaries are merged into the final inverted index
 */
public class Indexer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Term [] dictionary;
	private String filePath = "";
	private ArrayList<DocumentIndex> workingSet = new ArrayList<>();
	private ArrayList<DocumentIndex> aiWorkingSet = new ArrayList<>();
	int directorySize;
	private ArrayList <Term> firstTerm = new ArrayList<>();
	private ArrayList <Term> dictionaryBlock = new ArrayList<>();
	private ArrayList <Term> aiDictionaryBlock = new ArrayList<>();
	
	private String[] stopwords = { "a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any",
			"are", "aren't", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both",
			"but", "by", "can't", "cannot", "could", "couldn't", "did", "didn't", "do", "does", "doesn't", "doing",
			"don't", "down", "during", "each", "few", "for", "from", "further", "had", "hadn't", "has", "hasn't",
			"have", "haven't", "having", "he", "he'd", "he'll", "he's", "her", "here", "here's", "hers", "herself",
			"him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is",
			"isn't", "it", "it's", "its", "itself", "let's", "me", "more", "most", "mustn't", "my", "myself", "no",
			"nor", "not", "of", "off", "on", "once", "only", "or", "other", "ought", "our", "ours", "ourselves",
			"out", "over", "own", "same", "shan't", "she", "she'd", "she'll", "she's", "should", "shouldn't", "so",
			"some", "such", "than", "that", "that's", "the", "their", "theirs", "them", "themselves", "then",
			"there", "there's", "these", "they", "they'd", "they'll", "they're", "they've", "this", "those",
			"through", "to", "too", "under", "until", "up", "very", "was", "wasn't", "we", "we'd", "we'll", "we're",
			"we've", "were", "weren't", "what", "what's", "when", "when's", "where", "where's", "which", "while",
			"who", "who's", "whom", "why", "why's", "with", "won't", "would", "wouldn't", "you", "you'd", "you'll",
			"you're", "you've", "your", "yours", "yourself", "yourselves" };
	
	private String [] aiRawVocabulary = {"algorithm", "artificial", "intelligence", "general" ,"narrow", "neural", "network", "backpropagation",
			
										"bayesian", "bayes", "model", "belief", "decision", "big", "data", "chatbots", "classification", "clustering", 
										
										"cognitive", "computing", "computer", "vision", "convolutional", "mining", "deep", "learning", "digital", "ecosystem", 
										
										"general", "adversarial", "genetic", "heuristic", "image", "recognition", "limited", "memory", "machine", "translation", "natural", 
										
										"language", "processing", "process", "optical", "character", "recognition", "pattern", "reactive", "recurrent", "reinforcement", "robotics",
										
										"automation", "strong", "ai", "structured", "supervised", "transfer", "turing", "test", "unstructured", "unsupervised", "weak"};
	private Set <String> aiVocabulary = new HashSet<>();
	
	public Indexer()
	{
		directorySize = initialDirectorySize();
	}
	
	
	public ArrayList<DocumentIndex> getWorkingSet() {
		return workingSet;
	}

	public void setWorkingSet(ArrayList<DocumentIndex> workingSet) {
		this.workingSet = workingSet;
	}

	

	public ArrayList<Term> getAiDictionaryBlock() {
		return aiDictionaryBlock;
	}


	public void setAiDictionaryBlock(ArrayList<Term> aiDictionaryBlock) {
		this.aiDictionaryBlock = aiDictionaryBlock;
	}


	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public int getDirectorySize() {
		return directorySize;
	}

	public void setDirectorySize(int directorySize) {
		this.directorySize = directorySize;
	}
	
	
	public Term[] getdictionary() {
		return dictionary;
	}


	public void setdictionary(Term[] dictionary) {
		this.dictionary = dictionary;
	}


	public ArrayList<Term> getFirstTerm() {
		return firstTerm;	
	}


	public void setFirstTerm(ArrayList<Term> firstTerm) {
		this.firstTerm = firstTerm;
	}


	public ArrayList<Term> getDictionaryBlock() {
		return dictionaryBlock;
	}


	public void setDictionaryBlock(ArrayList<Term> dictionaryBlock) {
		this.dictionaryBlock = dictionaryBlock;
	}


	public String[] getAiRawVocabulary() {
		return aiRawVocabulary;
	}


	public void setAiRawVocabulary(String[] aiRawVocabulary) {
		this.aiRawVocabulary = aiRawVocabulary;
	}

	public Set<String> getAiVocabulary() {
		return aiVocabulary;
	}


	public void setAiVocabulary(Set<String> aiVocabulary) {
		this.aiVocabulary = aiVocabulary;
	}


	public void generateAiVocab ()
	{
		for (String word: aiRawVocabulary)
		{
			aiVocabulary.add(word);
		}
	}

	
	public ArrayList<DocumentIndex> getAiWorkingSet() {
		return aiWorkingSet;
	}


	public void setAiWorkingSet(ArrayList<DocumentIndex> aiWorkingSet) {
		this.aiWorkingSet = aiWorkingSet;
	}


	public int initialDirectorySize()
	{
		int directoryLength = 0;
		
		directoryLength = new File ("resources\\CRAWL DISK").listFiles().length;
		return directoryLength;
	}
	
	public int termExists (Term [] dictionary, String newTerm)
	{
		for (int i = 0; i < dictionary.length; i++)
		{
			
			if ("".equals(newTerm))
			{
				return -2;
			}
			
			if (dictionary[i] == null)
			{
				return -1;
			}
			
			
			if (dictionary[i].getTerm().equals(newTerm))
			{
				return i;
			}
		}
		
		return -2;
	}
	
	public void generateWorkingSet (String file) throws IOException, ClassNotFoundException
	{
		try 
		{
			workingSet = new ArrayList<>();
			
			this.setFilePath(file);
			FileInputStream fileIn = new FileInputStream (this.getFilePath());
			BufferedInputStream buffIn = new BufferedInputStream(fileIn);
			ObjectInputStream objectIn = new ObjectInputStream (buffIn);
			boolean nextDocument = true;
			
			//thirtyStopWords();
			//allStopWords();
			
			while (nextDocument)
				{
					Object obj = null;
					
					obj =  objectIn.readObject();
					
					if (obj != null)
						
						
					{
						DocumentIndex newEntry = new DocumentIndex();
						newEntry = (DocumentIndex)obj;
						
						if (newEntry.getTerms() != null)
							
						{
							String [] noReuter = new String [newEntry.getTerms().length];
							
							for (int i = 0; i < newEntry.getTerms().length; i++)
							{
								
								
								noReuter[i] = newEntry.getTerms()[i].replaceAll("[^a-zA-Z]", "");
								
								noReuter[i] = noReuter[i].toLowerCase();
								
								for (int j = 0; j < stopwords.length; j++)
								{
									
									if (stopwords[j].equals(noReuter[i]))
									{
										noReuter[i] = "";
									}
									
								} 
								
								if (file.equals("resources\\CRAWL DISK\\BLOCK2.txt"))
								{
									if (!aiVocabulary.contains(noReuter[i]))
									{
										noReuter[i] = noReuter[i].replaceAll("[^a-zA-Z]", "");
									}
								}
								
							}
							
							//noReuter = thirtyStopWords(noReuter);
							//String [] resizedReuter = resizeTermArray(noReuter);
							newEntry.setTerms(noReuter);
							workingSet.add(newEntry);
							//System.out.println("Doc added");
						}
					}
					
					else 
					{
						nextDocument = false;
					}
				}
			
			
			objectIn.close();
		}
		
		catch (EOFException e)
		{
			//System.out.println(workingSet.size());
		} 
		finally 
		{
			
		}
	}
	
	public void generateAiWorkingSet () throws IOException, ClassNotFoundException
	{
		
			try 
			{
				generateAiVocab();
				aiWorkingSet = new ArrayList<>();
				
				this.setFilePath("resources\\CRAWL DISK\\BLOCK2.txt");
				FileInputStream fileIn = new FileInputStream (this.getFilePath());
				BufferedInputStream buffIn = new BufferedInputStream(fileIn);
				ObjectInputStream objectIn = new ObjectInputStream (buffIn);
				boolean nextDocument = true;
				
				//thirtyStopWords();
				//allStopWords();
				
				while (nextDocument)
					{
						Object obj = null;
						
						obj =  objectIn.readObject();
						
						if (obj != null)
							
							
						{
							DocumentIndex newEntry = new DocumentIndex();
							newEntry = (DocumentIndex)obj;
							
							if (newEntry.getTerms() != null)
								
							{
								String [] noReuter = new String [newEntry.getTerms().length];
								
								for (int i = 0; i < newEntry.getTerms().length; i++)
								{
									
									
									noReuter[i] = newEntry.getTerms()[i].replaceAll("[^a-zA-Z]", "");
									
									noReuter[i] = noReuter[i].toLowerCase();
									
									for (int j = 0; j < stopwords.length; j++)
									{
										
										if (stopwords[j].equals(noReuter[i]))
										{
											noReuter[i] = "";
										}
										
									} 
									
									
										if (!aiVocabulary.contains(noReuter[i]))
										{
											noReuter[i] = "";
										}
									 
									
								}
								
								//noReuter = thirtyStopWords(noReuter);
								//String [] resizedReuter = resizeTermArray(noReuter);
								newEntry.setTerms(noReuter);
								aiWorkingSet.add(newEntry);
								//System.out.println("Doc added");
							}
						}
						
						else 
						{
							nextDocument = false;
						}
					}
				
				
				objectIn.close();
			}
			
			catch (EOFException e)
			{
				//System.out.println(workingSet.size());
			} 
			finally 
			{
				
			}
		}
	
	public String [] generateCaseFoldedWorkingSet(String [] bloatedTerms)
	{
		String [] smallTerms = new String [bloatedTerms.length];
		
		
		for (int i = 0; i < bloatedTerms.length; i++)
		{
			smallTerms[i] = bloatedTerms[i].toLowerCase();
		}
		
		return smallTerms;
	}

	public void thirtyStopWords ()
	{
		
		for (int i = 0; i < 30; i++)
		{
			stopwords[i] = stopwords[i].replaceAll("[^a-zA-Z]", "");
			
			stopwords[i] = stopwords[i].toLowerCase();
		}
		
	}
	
	public void  allStopWords ()
	{
		
		for (int i = 0; i < stopwords.length; i++)
		{
			stopwords[i] = stopwords[i].replaceAll("[^a-zA-Z]", "");
			
			stopwords[i] = stopwords[i].toLowerCase();
		}
		}
	
	public int numberOfTerms (ArrayList<DocumentIndex> workingSet)
	{
		int totalTerms = 0;
		
		for (int i = 0; i < workingSet.size(); i++)
		{
			for (int j = 0; j < workingSet.get(i).getTerms().length; j++)
			{
				if ("".equals(workingSet.get(i).getTerms()[j]) == false)
				{
					totalTerms += 1;
				}
				
				
			}
		}
		return totalTerms;
	}

	public void generateDictionary()
	{
		
		
		dictionary = new Term [numberOfTerms(workingSet)];
		
		
		
		for (int i = 0; i < workingSet.size(); i++)
		{
			
			
			for (int j = 0; j < workingSet.get(i).getTerms().length; j++)
			{
			
				int det = termExists (dictionary, workingSet.get(i).getTerms()[j]);
			
			switch (det)
			{
				case -1:
				{
					for (int k = 0; k < dictionary.length; k++)
					{
						if (dictionary[k] == null)
						{
							Map <Integer, Integer> postingsList = new TreeMap <> ();
							Term acceptedTerm = new Term(); 
							
							
								postingsList.put(workingSet.get(i).getDocID(), 1);
							
							
							acceptedTerm.setTerm(workingSet.get(i).getTerms()[j]);
							acceptedTerm.setPostingsList(postingsList);
							dictionary[k] = acceptedTerm;
							System.out.println("Added");
							break;
						}
					}
					
					break;
				}
				
				case -2:
				{
					break;
				}
				
				default:
				{
					
					
					
					Map<Integer, Integer> updatedPostingsList = dictionary[det].getPostingList();
					
					
						
						if (updatedPostingsList.containsKey(workingSet.get(i).getDocID()))
						{
							updatedPostingsList.replace(workingSet.get(i).getDocID(), updatedPostingsList.get(workingSet.get(i).getDocID()).intValue()+1);
							System.out.println("Frequency Updated");
						}
						
						else 
						{
							updatedPostingsList.put(workingSet.get(i).getDocID(), 1);
							System.out.println("Found in new doc");
							
							
						}
				}
			}
			}
			
			}
		
	
		//Add titles to the dictionary
		for (int i = 0; i < workingSet.size(); i++)
		{
			
			if (workingSet.get(i).getTitle() != null)
			{
				
				String [] workingTitle = workingSet.get(i).getTitle().split(" ");
				for (int j = 0; j < workingTitle.length; j++)
				{
				
					int det = termExists (dictionary, workingTitle[j]);
				
				switch (det)
				{
					case -1:
					{
						for (int k = 0; k < dictionary.length; k++)
						{
							if (dictionary[k] == null)
							{
								Map <Integer, Integer> postingsList = new TreeMap <> ();
								Term acceptedTerm = new Term(); 
								
								
									postingsList.put(workingSet.get(i).getDocID(), 1);
								
								
								acceptedTerm.setTerm(workingTitle[j]);
								acceptedTerm.setPostingsList(postingsList);
								dictionary[k] = acceptedTerm;
								System.out.println("Added");
								break;
							}
						}
						
						break;
					}
					
					case -2:
					{
						break;
					}
					
					default:
					{
						
						
						
						Map<Integer, Integer> updatedPostingsList = dictionary[det].getPostingList();
						
						
							
							if (updatedPostingsList.containsKey(workingSet.get(i).getDocID()))
							{
								updatedPostingsList.replace(workingSet.get(i).getDocID(), updatedPostingsList.get(workingSet.get(i).getDocID()).intValue()+1);
								System.out.println("Frequency Updated");
							}
							
							else 
							{
								updatedPostingsList.put(workingSet.get(i).getDocID(), 1);
								System.out.println("Found in new doc");
								
								
							}
					}
				}
				}
			}
			
			
			}
		
		sortDictionary(dictionary);
		System.out.println(dictionary[0].getTerm());
		firstTerm.add(dictionary[0]);
		System.out.println(dictionary.length);
		
	}
	
	public void sortDictionary (Term [] dictionary)
	{
		
		Term temp;
		
		for (int i = 0; i < dictionary.length - 1; i++)
		{
			if (dictionary[i] == null)
			{
				break;
			}
			
			else
			{
				for (int j = 1; j < dictionary.length; j++)
				{
					if (dictionary[j] == null)
					{
						break;
					}
					
					for (int k = 0; k < dictionary[i].getTerm().length(); k++)
					{
						if (k < dictionary[j].getTerm().length())
						{
							int diff = dictionary[i].getTerm().charAt(k) - dictionary[j].getTerm().charAt(k);
							
							if ( diff < 0)
							{
								break;
							}
							
							if (diff > 0)
							{
								temp = dictionary[i];
								dictionary[i] = dictionary[j];
								dictionary[j] = temp;
								break;
							}
						}
					}
				}
			}
			
		}
	}

	public void writeDictionary (String filePath) throws IOException
	{
		try 
		{
			
			FileOutputStream fileOut = new FileOutputStream (filePath);
			BufferedOutputStream buffOut = new BufferedOutputStream (fileOut);
			ObjectOutputStream objectOut = new ObjectOutputStream(buffOut);
				
				for (int i = 0; i < dictionary.length; i++)
				{
					if (dictionary[i] == null)
					{
						break;
					}
					
					else
					{
						objectOut.writeObject(dictionary[i]);
					}
					
				}
				
				
				//objectOut.flush();
				//buffOut.flush();
				
				objectOut.close();
				buffOut.close();
			fileOut.close();
		}
		
		finally
		{
			
		}
	}
	
	public void indexCorpus() throws ClassNotFoundException, IOException
	{
		
			generateWorkingSet("resources\\CRAWL DISK\\BLOCK2.txt");
			generateDictionary();
			System.out.println("Dictionary block generated");
			writeDictionary("resources\\CRAWL DICTIONARY\\AIdictionary.txt");
			System.out.println("Dictionary block complete");
			
		
	}

	public void invertIndex () throws IOException, ClassNotFoundException
	{
		
		
		
		int indexBlockSize = 25000;
		int termCounter = 0;
		int invertedBlocks = 0;
		Term [] invertedIndexBlock = new Term[25000];
		int dictionaryDirectoryLength = new File ("resources\\DICTIONARIES").listFiles().length;
		
		
		/*
		
		int fileCounter = 0;
		 
		
		
		
		
		while (fileCounter < dictionaryDirectoryLength) //Start by reading terms into the inverted Index Block
		{
			try 
			{
				
				
				this.setFilePath("resources\\DICTIONARIES\\dictionary" + (fileCounter + 1) + ".txt");
				FileInputStream fileIn = new FileInputStream (this.getFilePath());
				BufferedInputStream buffIn = new BufferedInputStream(fileIn);
				ObjectInputStream objectIn = new ObjectInputStream (buffIn);
				boolean nextDocument = true;
				
				
				while (nextDocument)
					{
					
						boolean exists = false;
						Object obj = null;
						
						obj =  objectIn.readObject();
						
						if (obj != null)		
						{
							Term newEntry = new Term();
							newEntry = (Term)obj;
							
							for (int k = 0; k < termCounter; k++)
							{
								if (invertedIndexBlock[k] != null && invertedIndexBlock[k].getTerm().equals(newEntry.getTerm()))
								{
									exists = true;
									mergeTerms(invertedIndexBlock[k], newEntry);
									System.out.println("Term Updated");
								}
							}
							
							
							if (exists == false)
							{
								invertedIndexBlock [termCounter] = newEntry;
								termCounter += 1;
								System.out.println("Term added");
							}
							
							
							if (termCounter == indexBlockSize) // When the index Block is full, open up the dictionaries and sort one by one
							{
								
								sortDictionary(invertedIndexBlock);
								int sampleDictionaryCounter = 0;
								
								while (sampleDictionaryCounter < dictionaryDirectoryLength)
								{
									ArrayList<Term> sampleDictionary = new ArrayList<>();
									try
									{
										
										FileInputStream dictionaryIn = new FileInputStream ("resources\\DICTIONARIES\\dictionary" + (sampleDictionaryCounter + 1) + ".txt");
										BufferedInputStream buffDictionaryIn = new BufferedInputStream(dictionaryIn);
										ObjectInputStream objectDictionaryIn = new ObjectInputStream (buffDictionaryIn);
										boolean nextTerm = true;
										
										while (nextTerm)
										{
											Object samp = null;
											
											samp =  objectDictionaryIn.readObject();
											
											if (samp != null)	
												
											{
												Term newSamp = new Term();
												newSamp = (Term)samp;
												
												sampleDictionary.add(newSamp);
												System.out.println("Samp added");
											}
											
											else 
											{
												nextTerm = false;
												sampleDictionaryCounter += 1;
												
											}
									   }
										
									}
									
									catch (EOFException e)
									{
										System.out.println("Dictionary added");
										sortToIndex(invertedIndexBlock, sampleDictionary); //sorts per the dictionary added
										
										try 
										{
											
											FileOutputStream fileOut = new FileOutputStream ("resources\\DICTIONARIES\\dictionary" + (sampleDictionaryCounter + 1) + ".txt");
											BufferedOutputStream buffOut = new BufferedOutputStream (fileOut);
											ObjectOutputStream objectOut = new ObjectOutputStream(buffOut);
												
												for (int i = 0; i < sampleDictionary.size(); i++)
												{
													if (sampleDictionary.get(i) == null)
													{
														break;
													}
													
													else
													{
														objectOut.writeObject(sampleDictionary.get(i));
													}
													
												}
												
												
												//objectOut.flush();
												//buffOut.flush();
												objectOut.close();
												buffOut.close();
												
											fileOut.close();
											System.out.println("Dictionary rewritten");
										}
										
										finally
										{
											
										}
										
										
										sampleDictionaryCounter += 1;
										
									} 
									
									finally 
									{
										
									}
								}
								
								writeInvertedIndex("resources\\INVERTED INDEX\\index block " + (invertedBlocks + 1) + ".txt",invertedIndexBlock );
								invertedBlocks+=1;
								termCounter = 0;
								invertedIndexBlock = new Term [25000];	
							}
						}
						
						else 
						{
							nextDocument = false;
							fileCounter +=1;
							
						}
					}
				
				
				objectIn.close();
			}
			
			catch (EOFException e)
			{
				
				
				fileCounter +=1;
				System.out.println(termCounter);
			} 
			
			
			finally  
			{
				
			}
			
		}
		
		
		sortDictionary(invertedIndexBlock);
		int sampleDictionaryCounter = 0;
		
		while (sampleDictionaryCounter < dictionaryDirectoryLength) // Final sort incase a block is less than 25000 terms. 
		{
			ArrayList<Term> sampleDictionary = new ArrayList<>();
			try
			{
				
				FileInputStream dictionaryIn = new FileInputStream ("resources\\DICTIONARIES\\dictionary" + (sampleDictionaryCounter + 1) + ".txt");
				BufferedInputStream buffDictionaryIn = new BufferedInputStream(dictionaryIn);
				ObjectInputStream objectDictionaryIn = new ObjectInputStream (buffDictionaryIn);
				boolean nextTerm = true;
				
				while (nextTerm)
				{
					Object samp = null;
					
					samp =  objectDictionaryIn.readObject();
					
					if (samp != null)	
						
					{
						Term newSamp = new Term();
						newSamp = (Term)samp;
						
						sampleDictionary.add(newSamp);
						System.out.println("Samp added");
					}
					
					else 
					{
						nextTerm = false;
						sampleDictionaryCounter += 1;
						
					}
			   }
				
			}
			
			catch (EOFException e)
			{
				System.out.println("Dictionary added");
				sortToIndex(invertedIndexBlock, sampleDictionary); //sorts per the dictionary added
				System.out.println("Dictionary sorted");
				
				try  //Issue reading and rewritting
				{
					
					FileOutputStream fileOut = new FileOutputStream ("resources\\DICTIONARIES\\dictionary" + (sampleDictionaryCounter + 1) + ".txt");
					BufferedOutputStream buffOut = new BufferedOutputStream (fileOut);
					ObjectOutputStream objectOut = new ObjectOutputStream(buffOut);
						
						for (int i = 0; i < sampleDictionary.size(); i++)
						{
							if (sampleDictionary.get(i) == null)
							{
								break;
							}
							
							else
							{
								objectOut.writeObject(sampleDictionary.get(i));
							}
							
						}
						
						
						//objectOut.flush();
						//buffOut.flush();
						objectOut.close();
						buffOut.close();
						
					fileOut.close();
					System.out.println("Dictionary rewritten");
				}
				
				finally
				{
					
				}
				sampleDictionaryCounter += 1;
				
			} 
			
			finally 
			{
				
			}
		}
		
		writeInvertedIndex("resources\\INVERTED INDEX\\index block " + (invertedBlocks + 1) + ".txt",invertedIndexBlock ); //writing the index to disk
		
		System.out.println("Indexing complete"); */
		
		
	}

	public void  sortToIndex (Term [] invIndex, ArrayList<Term> dict)
	{
		/*Term temp;
		
		
		for (int i = 0; i < invIndex.length - 1; i++)
		{
			if (invIndex[i] == null)
			{
				break;
			}
			
			else
			{
				for (int j = 1; j < dict.size(); j++)
				{
					if (dict.get(j) == null)
					{
						break;
					}
					
					
					if (invIndex[i].getTerm().equals(dict.get(j).getTerm()) && invIndex[i].getPostingList()[0] != dict.get(j).getPostingList()[0])
					{
						mergeTerms(invIndex[i], dict.get(j));
					}
					
					for (int k = 0; k < invIndex[i].getTerm().length(); k++)
					{
						if (k < dict.get(j).getTerm().length())
						{
							int diff = invIndex[i].getTerm().charAt(k) - dict.get(j).getTerm().charAt(k);
							
							if ( diff > 0)
							{
								break;
							}
							
							if (diff < 0)
							{
								temp = invIndex[i];
								invIndex[i] = dict.get(j);
								dict.set(j, temp);
								break;
							}
						}
					}
				}
			}
			
		} */
		
	}

	public String [] resizeTermArray(String [] terms)
	{
		int i = 0;
		
		
		for (int j = 0; j < terms.length; j++)
		{
			if ("".equals(terms[j]) || terms[j] == null)
			{
				i -=1;
			}
		}
		
		String [] resizedTerms = new String [terms.length - i];
		
		for (int j = 0; j < terms.length-i; j++)
		{
			if ("".equals(terms[j]) == false && terms[j] != null)
			{
				resizedTerms[j] =  terms[j];
			}
			
		}
		
		return resizedTerms;
	}
	
	public void writeInvertedIndex (String filePath, Term [] invIndex) throws IOException
	{
	/*	try 
		{
			
			FileOutputStream fileOut = new FileOutputStream (filePath);
			//BufferedOutputStream buffOut = new BufferedOutputStream (fileOut);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
				
				for (int i = 0; i < invIndex.length; i++)
				{
					if (invIndex[i] == null)
					{
						break;
					}
					
					else
					{
						objectOut.writeObject(invIndex[i]);
					}
					
				}
				
				
				//objectOut.flush();
				//buffOut.flush();
				//buffOut.close();
				objectOut.close();
				
			fileOut.close();
		}
		
		finally
		{
			
		}  */
	}

	public void mergeTerms (Term term1, Term term2)
	{
		
		/*int notNull = 0;
		int legitTerms = 0;
		
		for (int i = 0; i < term1.getPostingList().length; i++)
		{
			if (term1.getPostingList()[i] != null)
			{
				notNull += 1;
			}
		}
		
		for (int i = 0; i < term2.getPostingList().length; i++)
		{
			if (term2.getPostingList()[i] != null)
			{
				notNull += 1;
			}
		}
		
		
		Integer[] mergedList = new Integer [notNull];
		
		
			
			for (int j = 0; j < term1.getPostingList().length; j++)
			{
				if (term1.getPostingList()[j] != null)
				{
					mergedList[j] = term1.getPostingList()[j];
					legitTerms += 1;
				}
				
			}
			
			for (int j = 0; j < term2.getPostingList().length; j++)
			{
				if (term2.getPostingList()[j] != null)
				{
					mergedList[j + legitTerms] = term2.getPostingList()[j];
				}
			}
			
			
			
			term1.setPostingList(mergedList);
			
			*/
		
	}
	
	public void createBlocks () throws IOException, ClassNotFoundException
	{
		/*int dictionaryDirectoryLength = new File ("resources\\DICTIONARIES").listFiles().length;
		int indexBlockSize = 25000;
		int termCounter = 0;
		int fileCounter = 0;
		int invertedBlocks = 0;
		ArrayList <Term> invBlock = new ArrayList<>();
		
		
		
		while (fileCounter < dictionaryDirectoryLength)
		{
			try 
			{
				
				FileInputStream fileIn = new FileInputStream ("resources\\150 STOP DICTIONARIES\\dictionary" + (fileCounter + 1) + ".txt");
				BufferedInputStream buffIn = new BufferedInputStream(fileIn);
				ObjectInputStream objectIn = new ObjectInputStream (buffIn);
				boolean nextTerm = true;
				
				
				while (nextTerm)
					{
					
						boolean exists = false;
						Object obj = null;
						
						obj =  objectIn.readObject();
						
						if (obj != null)
								
						{
							Term newEntry = new Term();
							newEntry = (Term)obj;
							
							
							for (int i = 0; i < invBlock.size(); i++)
							{
								if (invBlock.get(i) != null)
								{
									if (invBlock.get(i).getTerm().equals(newEntry.getTerm()) && invBlock.get(i).getPostingList()[0] != newEntry.getPostingList()[0])
									{
										mergeTerms(invBlock.get(i), newEntry);
										exists = true;
									}
								}
								
							}
							
						
							if (!exists)
							{
								invBlock.add(newEntry);
								termCounter += 1;
							}
							
							if (termCounter == indexBlockSize)
							{
								sortBlock(invBlock);
								firstTerm.add(invBlock.get(0));
								try 
								{
									
									FileOutputStream fileOut = new FileOutputStream ("resources\\BLOCKS 150\\Block " + (invertedBlocks + 1) + ".txt");
									BufferedOutputStream buffOut = new BufferedOutputStream (fileOut);
									ObjectOutputStream objectOut = new ObjectOutputStream(buffOut);
										
										for (int i = 0; i < invBlock.size(); i++)
										{
											if (invBlock.get(i) == null)
											{
												break;
											}
											
											else
											{
												objectOut.writeObject(invBlock.get(i));
											}
											
										}
										
										
										//objectOut.flush();
										//buffOut.flush();
										
										objectOut.close();
										buffOut.close();
										
									fileOut.close();
									
									invertedBlocks +=1;
									
									invBlock = new ArrayList<>();
									termCounter = 0;
								}
								
								finally
								{
									
								}
							}
							
						}
						
						else 
						{
							nextTerm = false;
						}
					}
				
				
				objectIn.close();
			}
			
			catch (EOFException e)
			{
					
				if (fileCounter == dictionaryDirectoryLength-1)
				{
					sortBlock(invBlock);
					firstTerm.add(invBlock.get(0));
					try 
					{
						
						FileOutputStream fileOut = new FileOutputStream ("resources\\BLOCKS 150\\Block " + (invertedBlocks + 1) + ".txt");
						BufferedOutputStream buffOut = new BufferedOutputStream (fileOut);
						ObjectOutputStream objectOut = new ObjectOutputStream(buffOut);
							
							for (int i = 0; i < invBlock.size(); i++)
							{
								if (invBlock.get(i) == null)
								{
									break;
								}
								
								else
								{
									objectOut.writeObject(invBlock.get(i));
								}
								
							}
							
							
							//objectOut.flush();
							//buffOut.flush();
							
							objectOut.close();
							buffOut.close();
							
						fileOut.close();
						
						invertedBlocks +=1;
					}
					
					finally
					{
						
					}
				}
				System.out.println("File " + (fileCounter + 1) + "complete");
				fileCounter += 1;
				
			} 
			finally 
			{
				try 
				{
					
					FileOutputStream fileOut = new FileOutputStream ("resources\\BLOCKS 150\\FirstTerms.txt");
					BufferedOutputStream buffOut = new BufferedOutputStream (fileOut);
					ObjectOutputStream objectOut = new ObjectOutputStream(buffOut);
						
						for (int i = 0; i < firstTerm.size(); i++)
						{
							if (firstTerm.get(i) == null)
							{
								break;
							}
							
							else
							{
								objectOut.writeObject(firstTerm.get(i));
							}
							
						}
						
						
						//objectOut.flush();
						//buffOut.flush();
						
						objectOut.close();
						buffOut.close();
						
					fileOut.close();
					
					
				}
				
				finally 
				{
					
				}
			}
		} */
			
	}
	
	public void sortBlock (ArrayList <Term> invBlock)
	{
		/*	Term temp;
		
		for (int i = 0; i < invBlock.size() - 1; i++)
		{
			if (invBlock.get(i) == null)
			{
				break;
			}
			
			else
			{
				for (int j = 1; j < invBlock.size(); j++)
				{
					if (invBlock.get(j) == null)
					{
						break;
					}
					
					for (int k = 0; k < invBlock.get(i).getTerm().length(); k++)
					{
						if (k < invBlock.get(j).getTerm().length())
						{
							int diff = invBlock.get(i).getTerm().charAt(k) - invBlock.get(j).getTerm().charAt(k);
							
							if ( diff < 0)
							{
								break;
							}
							
							if (diff > 0)
							{
								temp = invBlock.get(i);
								invBlock.set(i, invBlock.get(j));
								invBlock.set(j, temp);
								break;
							}
						}
					}
				}
			}
			
		} */
	}
	
	public void mergeBlocks () throws IOException, ClassNotFoundException
	{
		ArrayList<Term> blockA = null;
		ArrayList<Term> blockB = null;
		int blockCounter = 0;
		int blockDirectorySize = new File ("resources\\BLOCKS 150").listFiles().length - 1;
		int comparisonCounter = 1;
		
		while (blockCounter < blockDirectorySize)
			
		try
		{
			
			FileInputStream fileIn = new FileInputStream ("resources\\BLOCKS 150\\Block " + (blockCounter + 1) + ".txt");
			BufferedInputStream buffIn = new BufferedInputStream(fileIn);
			ObjectInputStream objectIn = new ObjectInputStream (buffIn);
			boolean nextTerm = true;
			comparisonCounter = 1;
			blockA = new ArrayList<>();
		
		while (nextTerm)
			{
				Object obj = null;
				
				obj =  objectIn.readObject();
				
				if (obj != null)
					
					
				{
					Term newEntry = new Term();
					newEntry = (Term)obj;
					
					blockA.add(newEntry);
					System.out.println("Term added");
				}
				
				else 
				{
					nextTerm = false;
				}
			}
		
		
		objectIn.close();
	}
	
	catch (EOFException e)
	{
		System.out.println(blockA.size());
		
		while (comparisonCounter < blockDirectorySize)
		{
			try
			{
				
				FileInputStream fileIn = new FileInputStream ("resources\\BLOCKS 150\\Block " + (comparisonCounter + 1) + ".txt");
				BufferedInputStream buffIn = new BufferedInputStream(fileIn);
				ObjectInputStream objectIn = new ObjectInputStream (buffIn);
				boolean nextTerm = true;
				blockB = new ArrayList<>();
			
			while (nextTerm)
				{
					Object obj = null;
					
					obj =  objectIn.readObject();
					
					if (obj != null)
						
						
					{
						Term newEntry = new Term();
						newEntry = (Term)obj;
						
						blockA.add(newEntry);
						System.out.println("Term added");
					}
					
					else 
					{
						nextTerm = false;
					}
				}
			
			
			objectIn.close();
		}
		
		catch (EOFException e1)
		{
			System.out.println(blockB.size());
			
			for (int i = 0; i < blockA.size(); i++)
			{
				for (int j = 0; j < blockB.size(); j++)
				{
					
					if (blockB.get(j) == null || blockA.get(i) == null && blockB.get(j) == null )
					{
						
					}
					
					
					if (blockA.get(i) == null && blockB.get(j) != null)
					{
						blockA.set(i, blockB.get(j));
						blockB.remove(j);
					}
					
					if (blockA.get(i) != null && blockB.get(j) != null)
					{
						sortTerms(blockA.get(i), blockB.get(j));
					}
					
				}
			}
			
			sortBlock(blockB);
			
			try 
			{
				
				FileOutputStream fileOut = new FileOutputStream ("resources\\BLOCKS 150\\Block " + (comparisonCounter + 1) + ".txt");
				BufferedOutputStream buffOut = new BufferedOutputStream (fileOut);
				ObjectOutputStream objectOut = new ObjectOutputStream(buffOut);
					
					for (int i = 0; i < blockB.size(); i++)
					{
						if (blockB.get(i) == null)
						{
							break;
						}
						
						else
						{
							objectOut.writeObject(blockB.get(i));
						}
						
					}
					
					
					//objectOut.flush();
					//buffOut.flush();
					
					objectOut.close();
					buffOut.close();
					
				fileOut.close();
				
				
				comparisonCounter +=1;
				blockB = new ArrayList<>();
			}
			
			finally
			{
				
			}
			
			
			
		} 
			finally 
			{
			
			}
		}
			
		
		
		sortBlock(blockA);
		
		try 
		{
			
			FileOutputStream fileOut = new FileOutputStream ("resources\\BLOCKS 150\\Block " + (blockCounter + 1) + ".txt");
			BufferedOutputStream buffOut = new BufferedOutputStream (fileOut);
			ObjectOutputStream objectOut = new ObjectOutputStream(buffOut);
				
				for (int i = 0; i < blockA.size(); i++)
				{
					if (blockA.get(i) == null)
					{
						break;
					}
					
					else
					{
						objectOut.writeObject(blockA.get(i));
					}
					
				}
				
				
				//objectOut.flush();
				//buffOut.flush();
				
				objectOut.close();
				buffOut.close();
				
			fileOut.close();
			
			
			blockCounter +=1;
			blockA = new ArrayList<>();
		}
		
		finally
		{
			
		} 
	}
		finally 
		{
		
		}
		
		
	} 
	
	public void loadDictionary () throws IOException, ClassNotFoundException
	{
		
		//Map <Integer, Integer> term1Postings = new TreeMap <>();
		//ArrayList <Term> currentBlock = new ArrayList<>();
		
		
			try 
			{
				
				dictionaryBlock = new ArrayList<>();
				
				FileInputStream fileIn = new FileInputStream ("resources\\CRAWL DICTIONARY\\dictionary.txt");
				BufferedInputStream buffIn = new BufferedInputStream(fileIn);
				ObjectInputStream objectIn = new ObjectInputStream (buffIn);
				boolean nextTerm = true;
				
				
				
				while (nextTerm)
					{
						Object obj = null;
						
						obj =  objectIn.readObject();
						
						if (obj != null)
							
							
						{
							Term newEntry = new Term();
							newEntry = (Term)obj;
							
							dictionaryBlock.add(newEntry);
							//System.out.println("Term added");
						}
						
						else 
						{
							nextTerm = false;
						}
					}
				
				
				objectIn.close();
				buffIn.close();
			}
			
			catch (EOFException e)
			{
				
				
				
			} 
			finally 
			{
				
			}
		
		
	
	}
	
	public void loadAiDictionary () throws IOException, ClassNotFoundException
	{
		
		//Map <Integer, Integer> term1Postings = new TreeMap <>();
		//ArrayList <Term> currentBlock = new ArrayList<>();
		
		
			try 
			{
				
				aiDictionaryBlock = new ArrayList<>();
				
				FileInputStream fileIn = new FileInputStream ("resources\\CRAWL DICTIONARY\\AIdictionary.txt");
				BufferedInputStream buffIn = new BufferedInputStream(fileIn);
				ObjectInputStream objectIn = new ObjectInputStream (buffIn);
				boolean nextTerm = true;
				
				
				
				while (nextTerm)
					{
						Object obj = null;
						
						obj =  objectIn.readObject();
						
						if (obj != null)
							
							
						{
							Term newEntry = new Term();
							newEntry = (Term)obj;
							
							aiDictionaryBlock.add(newEntry);
							//System.out.println("Term added");
						}
						
						else 
						{
							nextTerm = false;
						}
					}
				
				
				objectIn.close();
				buffIn.close();
			}
			
			catch (EOFException e)
			{
				
				
				
			} 
			finally 
			{
				
			}
		
		
	
	}

	public Map <Integer, Integer> fetchQueryToken (String term1, ArrayList <Term> dictionaryBlock)
	{
		
		Map <Integer, Integer> term1Postings = new TreeMap<>();
		
		for (int j = 0; j < dictionaryBlock.size(); j++)
		{
			if (dictionaryBlock.get(j).getTerm().equals(term1))
			{
				term1Postings = dictionaryBlock.get(j).getPostingList();
				
				break;
			}
		}
		
		return term1Postings;
	}
	
	public void singleSearch (String term1) throws ClassNotFoundException, IOException
	{
		
		//generateFirstTerms();
		loadDictionary();
		Map<Integer, Integer> singlePosting = fetchQueryToken(term1, dictionaryBlock);
		
		if (singlePosting.isEmpty())
		{
			System.out.println(term1 + " not in corpus");
		}
		
		else
		{
			System.out.println("The term " + term1 + " appears in " + singlePosting.size() + " documents ");
			
		
				//System.out.println(singlePosting.keySet());
				
				for (Map.Entry<Integer, Integer> entry: singlePosting.entrySet())
				{
					System.out.println("DocId: " + entry.getKey()
										+ " Title: " + workingSet.get(entry.getKey() - 1).getTitle());
				}	
		}
		
	
	}
	
	public void andSearch (String term, int ranker, int topK) throws ClassNotFoundException, IOException
	{
		
		
		loadDictionary();
		loadAiDictionary();
		Map <Integer, Integer> term1Postings = new HashMap<>();
		Map <Integer, Integer> term2Postings = new HashMap<>();
		String [] splitTerms = term.split(" ");
		
		
		term1Postings = fetchQueryToken(splitTerms[0], dictionaryBlock);
		term2Postings = fetchQueryToken(splitTerms[1], dictionaryBlock);
		
		
		Set <Integer> combinedPostings = new HashSet<>(term1Postings.keySet());
		combinedPostings.retainAll(term2Postings.keySet());
		
		
		if (splitTerms.length > 2)
		{
			for (int i = 2; i < splitTerms.length; i++)
			{
				Map <Integer, Integer> additionalPostings = fetchQueryToken(splitTerms[i], dictionaryBlock);
				
				combinedPostings.retainAll(additionalPostings.keySet());
					
			}
		}
		
		
		HashMap <Integer, Double> unweightedPostings = new HashMap<>();

		switch (ranker)
		{
		case 1:
			
			for (Integer docID: combinedPostings)
			{
				double weight = 0;
				
				for (String anchor: splitTerms)
				{
					Map <Integer, Integer> anchorPost = new HashMap <>();
					
							anchorPost =  fetchQueryToken(anchor, dictionaryBlock);
							
							
							if (anchorPost.containsKey(docID))
							{
								weight += tfIdfRank(anchorPost, anchorPost, docID, docID,  workingSet);
								
							}
							
				}
				
				unweightedPostings.put(docID, weight);
				
			}
			
			break;
			
		case 2:
			
			for (Integer docID: combinedPostings)
			{
				double weight = 0;
				
				for (String anchor: splitTerms)
				{
					Map <Integer, Integer> anchorPost = new HashMap <>();
					
							anchorPost =  fetchQueryToken(anchor, dictionaryBlock);
							
							
							if (anchorPost.containsKey(docID))
							{
								weight += bm25Rank(anchorPost, anchorPost, docID, docID,  workingSet, workingSet);
								 
								
							}
							
				}
				
				unweightedPostings.put(docID, weight);
				
			}
			break;
			
		case 3:
			
			for (Integer docID: combinedPostings)
			{
				double weight = 0;
				
				for (String anchor: splitTerms)
				{
					Map <Integer, Integer> anchorPost = new HashMap <>();
					Map <Integer, Integer> anchorRegular = new HashMap <>();
					
							anchorPost =  fetchQueryToken(anchor, aiDictionaryBlock);
							anchorRegular = fetchQueryToken(anchor, dictionaryBlock);
							
							if (anchorPost.isEmpty() == false)
							{
								for (Integer aiDocID: anchorPost.keySet())
								{
									
										weight += tfIdfRank(anchorPost, anchorRegular, aiDocID, docID, aiWorkingSet);
										
										    
								}
							}	
				}
				
				unweightedPostings.put(docID, weight);
				
			}
			break;
			
		case 4:
			
			for (Integer docID: combinedPostings)
			{
				double weight = 0;
				
				for (String anchor: splitTerms)
				{
					Map <Integer, Integer> anchorPost = new HashMap <>();
					Map <Integer, Integer> anchorRegular = new HashMap <>();
					
							anchorPost =  fetchQueryToken(anchor, aiDictionaryBlock);
							anchorRegular = fetchQueryToken(anchor, dictionaryBlock);
							if (!anchorPost.isEmpty())
							{
								for (Integer aiDocID: anchorPost.keySet())
								{
									
										weight += bm25Rank(anchorPost, anchorRegular, aiDocID, docID, aiWorkingSet, workingSet);	
									
								}
								
							}
				}
				
				unweightedPostings.put(docID, weight);
				
			}
			
			break;
		}
		
		
		HashMap <Integer, Double> weightedPostings = sortByValue(unweightedPostings);
		
		System.out.print("The terms " );
		
		for (String word: splitTerms)
		{
			System.out.print( "\"" + word + "\" ");
		}
		
		System.out.print("all appear in " +  combinedPostings.size() + " documents. The top " + topK + " are");
		
		int resultCounter = topK;
		
		for (Map.Entry<Integer, Double> weightedPost: weightedPostings.entrySet())
		{
			System.out.print("\nDocId: " + weightedPost.getKey()
			+ " Title: " + workingSet.get(weightedPost.getKey() - 1).getTitle());
			resultCounter -= 1;
			
			if (resultCounter == 0)
			{
				break;
			}
		}
		
		
	}
	
	public void orSearch (String term, int ranker, int topK) throws ClassNotFoundException, IOException
	{
		
		
		 loadDictionary();
		 loadAiDictionary();
		Map <Integer, Integer> term1Postings = new TreeMap<>();
		Map <Integer, Integer> nextTermPostings = new TreeMap<>();
		String [] splitTerms = term.split(" ");
		
		term1Postings = fetchQueryToken(splitTerms[0], dictionaryBlock);
		Set <Integer> combinedPostings = new TreeSet<>(term1Postings.keySet());
		
		for (int x = 1; x < splitTerms.length; x++)
		{
			
			
		nextTermPostings = fetchQueryToken(splitTerms[x], dictionaryBlock);
		combinedPostings.addAll(nextTermPostings.keySet());
		
		}
		
		HashMap <Integer, Double> unweightedPostings = new HashMap<>();

		switch (ranker)
		{
		case 1:
			
			for (Integer docID: combinedPostings)
			{
				double weight = 0;
				
				for (String anchor: splitTerms)
				{
					Map <Integer, Integer> anchorPost = new HashMap <>();
					
							anchorPost =  fetchQueryToken(anchor, dictionaryBlock);
							
							
							if (anchorPost.containsKey(docID))
							{
								weight += tfIdfRank(anchorPost, anchorPost, docID, docID,  workingSet);
								
							}
							
				}
				
				unweightedPostings.put(docID, weight);
				
			}
			
			break;
			
		case 2:
			
			for (Integer docID: combinedPostings)
			{
				double weight = 0;
				
				for (String anchor: splitTerms)
				{
					Map <Integer, Integer> anchorPost = new HashMap <>();
					
							anchorPost =  fetchQueryToken(anchor, dictionaryBlock);
							
							
							if (anchorPost.containsKey(docID))
							{
								weight += bm25Rank(anchorPost, anchorPost, docID, docID,  workingSet, workingSet);
								 
								
							}
							
				}
				
				unweightedPostings.put(docID, weight);
				
			}
			break;
			
		case 3:
			
			for (Integer docID: combinedPostings)
			{
				double weight = 0;
				
				for (String anchor: splitTerms)
				{
					Map <Integer, Integer> anchorPost = new HashMap <>();
					Map <Integer, Integer> anchorRegular = new HashMap <>();
					
							anchorPost =  fetchQueryToken(anchor, aiDictionaryBlock);
							anchorRegular = fetchQueryToken(anchor, dictionaryBlock);
							
							if (anchorPost.isEmpty() == false)
							{
								for (Integer aiDocID: anchorPost.keySet())
								{
									
										weight += tfIdfRank(anchorPost, anchorRegular, aiDocID, docID, aiWorkingSet);
										
										    
								}
							}	
				}
				
				unweightedPostings.put(docID, weight);
				
			}
			break;
			
		case 4:
			
			for (Integer docID: combinedPostings)
			{
				double weight = 0;
				
				for (String anchor: splitTerms)
				{
					Map <Integer, Integer> anchorPost = new HashMap <>();
					Map <Integer, Integer> anchorRegular = new HashMap <>();
					
							anchorPost =  fetchQueryToken(anchor, aiDictionaryBlock);
							anchorRegular = fetchQueryToken(anchor, dictionaryBlock);
							if (!anchorPost.isEmpty())
							{
								for (Integer aiDocID: anchorPost.keySet())
								{
									
										weight += bm25Rank(anchorPost, anchorRegular, aiDocID, docID, aiWorkingSet, workingSet);
									
								}
								
							}
				}
				
				unweightedPostings.put(docID, weight);
				
			}
			
			break;
		}
		
		
		HashMap <Integer, Double> weightedPostings = sortByValue(unweightedPostings);
				
					System.out.print("The terms " );
					
					for (String word: splitTerms)
					{
						System.out.print("\"" + word + "\" ");
					}
					
					System.out.print("collectively appear in " +  combinedPostings.size() + " documents. The top " + topK + " are");
					
					int resultCounter = topK;
					
					for (Map.Entry<Integer, Double> weightedPost: weightedPostings.entrySet())
					{
						System.out.print("\nDocId: " + weightedPost.getKey()
						+ " Title: " + workingSet.get(weightedPost.getKey() - 1).getTitle());
						resultCounter -= 1;
						
						if (resultCounter == 0)
						{
							break;
						}
					}
					
				
		
	}
	
	public void sortTerms (Term termA, Term termB)
	{
		Term temp;
		
		if (termA.getTerm().equals(termB.getTerm()) && termA.getPostingList() != termB.getPostingList())
		{
			mergeTerms(termA, termB);
		}
		
		for (int k = 0; k < termA.getTerm().length(); k++)
		{
			if (k < termB.getTerm().length())
			{
				int diff = termA.getTerm().charAt(k) - termB.getTerm().charAt(k);
				
				if ( diff < 0)
				{
					break;
				}
				
				if (diff > 0)
				{
					temp = termA;
					termA = termB;
					termB = temp;;
					break;
				}
			}
		}
	}

	public double tfIdfRank (Map<Integer, Integer> AIpostings, Map<Integer, Integer> regularPostings, Integer AIdocID, Integer regularDocID ,  ArrayList <DocumentIndex> workingSet)
	{
		double score = 0;
		
		if (AIpostings.get(AIdocID) == null)
		{
			return score;
		}
		double top = (1 + Math.log(regularPostings.get(regularDocID)));
		double middle1 = workingSet.size() ;
		double middle2 = AIpostings.size();
		double middle = middle1/middle2;
	   double bottom = (Math.log(middle));
		
		score = top * bottom;
		
		return score;
	}
	
	public double bm25Rank (Map<Integer, Integer> AIpostings, Map<Integer, Integer> regularPostings, Integer AIdocID, Integer regularDocID ,  ArrayList <DocumentIndex> AIworkingSet, ArrayList <DocumentIndex> regularWorkingSet)
	{
		double score = 0;
		if (AIpostings.get(AIdocID) == null)
		{
			return score;
		}
		double termFreq = regularPostings.get(regularDocID);
		double docLength = 1;
		try 
		{
			docLength= regularWorkingSet.get(regularDocID).getTerms().length;
		}
		
		catch (IndexOutOfBoundsException e)
		{
			return score;
		}
		
		
		double avgDl = averageDocLength(regularWorkingSet);
		double k = 1.5;
		double b = 0.75;
		double idf = Math.log((AIworkingSet.size() - AIpostings.size() + 0.5)/(AIpostings.size() + 0.5)) + 1;
		
		score = idf * ((termFreq * (k + 1))/(termFreq + k * (1 -b + b * (docLength / avgDl))));
		
		
		return score;
	}
	
	public int averageDocLength (ArrayList<DocumentIndex> workingSet)
	{
		int average = 0;
		
		
		int totalLength = 0;
		int corpusSize = workingSet.size();
		
		for (DocumentIndex doc: workingSet)
		{
			totalLength += doc.getTerms().length;
		}
		
		average = totalLength / corpusSize;
		
		return average;
	}
	
	public void generateFirstTerms () throws IOException, ClassNotFoundException

	{
		
		try
		{
		FileInputStream fileIn = new FileInputStream ("resources\\BLOCKS 150\\FirstTerms.txt");
		BufferedInputStream buffIn = new BufferedInputStream(fileIn);
		ObjectInputStream objectIn = new ObjectInputStream (buffIn);
		boolean nextDocument = true;
		
		while (nextDocument)
			{
				Object obj = null;
				
				obj =  objectIn.readObject();
				
				if (obj != null)
					
					
				{
					Term newEntry = new Term();
					newEntry = (Term)obj;
					
					firstTerm.add(newEntry);
					System.out.println("First Term added");
				}
				
				else 
				{
					nextDocument = false;
				}
			}
		
		
		objectIn.close();
	}
	
	catch (EOFException e)
	{
		System.out.println(workingSet.size());
	} 
		finally 
		{
		
		}
	}
	
	public HashMap<Integer, Double> sortByValue(HashMap<Integer, Double> unweightedPostings) 
    { 
        
        List<Map.Entry<Integer, Double> > list = 
               new LinkedList< >(unweightedPostings.entrySet()); 
  
       
        Collections.sort(list, new Comparator<Map.Entry<Integer, Double> >() { 
            public int compare(Map.Entry<Integer, Double> o1,  
                               Map.Entry<Integer, Double> o2) 
            { 
                return (o2.getValue()).compareTo(o1.getValue()); 
            } 
        }); 
          
          
        HashMap<Integer, Double> temp = new LinkedHashMap<>(); 
        for (Map.Entry<Integer, Double> aa : list) { 
            temp.put(aa.getKey(), aa.getValue()); 
        } 
        return temp; 
    }
}

