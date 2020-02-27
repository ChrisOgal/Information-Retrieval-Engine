/**
 * 
 */
package tokenizer;

import java.io.*;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.apache.commons.io.FileUtils;

import java.util.ArrayList;
import java.util.List;
/**
 * @author chris
 * 
 * Parser used to read in the document ID and terms. Stores relevant material in the DISK resource folder.
 *
 */
public class Parser{
	
	/**
	 * 
	 */

	private String input;
	private static Integer assignedID = 0;
	private ArrayList<DocumentIndex> documentList = new ArrayList<>();

	
	public Parser() {
		
	}
	
	public Parser(String input) {
		super();
		this.input = input;
	}

	

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}
	
	
	
	public static Integer getAssignedID() {
		return assignedID;
	}

	public static void setAssignedID(Integer assignedID) {
		Parser.assignedID = assignedID;
	}

	private String readFile(String file) throws IOException
	{
		
		BufferedReader reader = new BufferedReader (new FileReader(file));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		
		while ((line = reader.readLine()) != null)
		{
			stringBuilder.append(line);
		}
		
		return stringBuilder.toString();
	}
	
	public void testParser () throws IOException
	{
		
		File dir = new File ("C:\\Users\\chris\\Documents\\School\\Comp 479\\AITopics web crawl\\4hop");
		String [] extensions = new String [] {"html"};
		
		List <File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);
		
		
		for (File file: files)
		{
			input = file.getCanonicalPath();
			
			String readyFile = readFile(input);
			System.out.println("This file can be read.");
			
				
			Document doc = Jsoup.parse(readyFile);
			
			Elements title = doc.getElementsByTag("TITLE");
			Elements bodies = doc.getElementsByTag("BODY");
			
			
			
			assignedID += 1;
			
			
				/*System.out.println(bodies.size());
				System.out.println(dates.size());
				System.out.println(titles.size());
				System.out.println(ids.size()); */
				
					DocumentIndex sampleDoc = new DocumentIndex();
					sampleDoc.setTitle(title.text());
					if (bodies.size() != 0)
					{
						sampleDoc.setTerms(bodies.get(0).text().split(" "));
					}
					
					sampleDoc.setDocID(assignedID);
					documentList.add(sampleDoc);
					
				
				
				System.out.println(documentList.size());
			
				
			
	
				/*for (int i = 0; i < documentList.size(); i++)
				{
					
					System.out.println(documentList.get(i).getDocID() + "\n");
					for (int j = 0; j < documentList.get(i).getTerms().length; j++)
					{
						System.out.print(documentList.get(i).getTerms()[j]);
					}
					
					System.out.println("\n");
				} */
		}
				
		
	}

	public void writeCorpus () throws IOException
	{
		
		
		FileOutputStream fileOut = new FileOutputStream ("resources\\CRAWL DISK\\BLOCK2.txt");
		BufferedOutputStream buffOut = new BufferedOutputStream (fileOut);
		ObjectOutputStream objectOut = new ObjectOutputStream(buffOut);
		
		try 
		{
			
			for (int i = 0; i < documentList.size(); i++)
			{
				
				
				
				objectOut.writeObject(documentList.get(i));
				
				
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

	public ArrayList<DocumentIndex> getDocumentList() {
		return documentList;
	}

	public void setDocumentList(ArrayList<DocumentIndex> documentList) {
		this.documentList = documentList;
	}
	
	

}
