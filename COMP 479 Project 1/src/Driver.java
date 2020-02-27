/**
 * 
 */
import java.io.IOException;

import Indexing.*;
import tokenizer.*;
/**
 * @author chris
 * 
 */
public class Driver {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * 
	 */
	static Indexer testIndexer = new Indexer();
	static Parser testParser = new Parser();
	
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		
	
	//	testParsing();
		
		
//	testCorpus();
		
	// testInversion();
		
		//testMerge();
	   
	testIndexer.generateWorkingSet("resources\\CRAWL DISK\\BLOCK1.txt");
	testIndexer.generateAiWorkingSet();
//	
	testIndexer.loadAiDictionary();
	
		
		
		
			System.out.println(testIndexer.getAiWorkingSet().size());
		
	
	
	
		
	
	
	// testSingleTermQuery();
	   
	// testANDQuery();
	   
    // testORQuery();
	 
	}
	
	
	private static void testORQuery() throws ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		
		
		//Use 1 for tf-idf ranking and 2 for bm25 ranking
		//Use 3 for tf-idf ranking and 4 for bm25 ranking with AI vocabulary
		//Second number specifies the topk return
		testIndexer.orSearch("professor, artificial, intelligence research", 1, 100);
		//System.out.println(testIndexer.getWorkingSet().size());
		
		
	}


	private static void testANDQuery() throws ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		
		
		//Use 1 for tf-idf ranking and 2 for bm25 ranking
		//Use 3 for tf-idf ranking and 4 for bm25 ranking with AI vocabulary
		//Second number specifies the topk return
		testIndexer.andSearch("professor artificial intelligence research", 4, 10);
	}


	private static void testSingleTermQuery() throws ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		
		
		testIndexer.singleSearch("intelligence");
	}


	public static void testParsing()
	{
		
		try 
		{
		
			
		testParser.testParser();
		testParser.writeCorpus(); 
	}  
	
	catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
	}

	
	public static void testIndexing()
	{
		
		try {
			
			testIndexer.generateWorkingSet("resources\\CRAWL DISK\\BLOCK2.txt");
			//testIndexer.writeDictionaries("resources\\DICTIONARIES\\BLOCK" + (i + 1) + ".txt");
			
		}  
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testCorpus ()
	{
		
		
		try {
			testIndexer.indexCorpus();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public static void testInversion()
	{
		
		
		try {
			testIndexer.createBlocks();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testMerge() throws ClassNotFoundException, IOException
	{
		testIndexer.mergeBlocks();
	}
}
