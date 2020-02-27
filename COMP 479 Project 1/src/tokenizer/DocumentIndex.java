/**
 * 
 */
package tokenizer;
import java.io.*;
/**
 * @author chris
 *
 *
 *Stores viable document information such as the document ID, title and terms contained in the document.
 */
public class DocumentIndex implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer docID;
	private String title;
	private String [] terms;
	
	
	
	
	public DocumentIndex()
	{
		
	}
	
	public DocumentIndex(int docID, String title, String[] terms) {
		super();
		this.docID = docID;
		this.title = title;
		this.terms = terms;
	}
	public int getDocID() {
		return docID;
	}
	public void setDocID(int docID) {
		this.docID = docID;
	}
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	public String[] getTerms() {
		return terms;
	}
	public void setTerms(String[] terms) {
		this.terms = terms;
	}


	
	

}
