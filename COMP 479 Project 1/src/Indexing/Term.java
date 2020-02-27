/**
 * 
 */
package Indexing;

import java.io.Serializable;
import java.util.Map;

/**
 * @author chris
 * Term is a combination of a token and its postingsList.
 */
public class Term implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String term;
	private Map <Integer, Integer> postingsList;
	//private Integer[] postingsList;
	
	public Term() {
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public Map<Integer, Integer> getPostingList() {
		return postingsList;
	}


	public void setPostingsList(Map<Integer, Integer> postingsList) {
		this.postingsList = postingsList;
	}


	
}
