package de.mz.jk.bio.fasta;
import java.io.Serializable;


/**
 * rudimentary representation of a fasta entry
 * @author J.Kuharev
 */
public class FastaRecord implements Serializable
{
	private static final long serialVersionUID = 20170118;

	private String title="";
	private String sequence="";

	/**
	 * create a new empty fasta record
	 */
	public FastaRecord(){}
	
	/**
	 * create a new user defined fasta record
	 * @param title
	 * @param sequence
	 */
	public FastaRecord(String title, String sequence)
	{
		setTitle(title);
		setSequence(sequence);
	}
	
	/**
	 * @return whole title line without leading ">"
	 */
	public String getTitle()
	{
		return title;
	}
	
	/**
	 * set the title line
	 * @param title
	 */
	public void setTitle(String title)
	{
		this.title = ((title.startsWith(">")) ? title.substring(1) : title).trim();
	}
	
	/**
	 * @return whole sequence as a single string
	 */
	public String getSequence()
	{
		return sequence;
	}
	
	/**
	 * set the sequence from a string
	 * @param sequence
	 */
	public void setSequence(String sequence)
	{
		this.sequence = sequence;
	}

	/**
	 * split the sequence into an array of lines with maximum given line size
	 * @param maxLength maximum line size
	 * @return array of strings (lines)
	 */
	public String[] getSequenceAsArray(int maxLength)
	{
		int size = sequence.length() / maxLength + ((sequence.length() % maxLength == 0) ? 0 : 1);
		String[] result = new String[size];
		for(int i=0; i<size; i++)
		{
			int start = i*maxLength;
			int end = start + maxLength - 1;
			if(end>=sequence.length()) end = sequence.length()-1;
			result[i] = sequence.substring(start, end);
		}
		return result;
	}
}
