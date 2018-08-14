package de.mz.jk.bio.fasta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class FastaStringReader implements iFastaReader
{
	private BufferedReader reader = null;
	private String nextTitle = "";
	private String fastaText;
	
	public FastaStringReader(){}
	public FastaStringReader(String fastaString)
	{
		fastaText = fastaString;
	}
	
	public FastaRecord getNextFastaRecord() throws IOException
	{
		String title = nextTitle;
		String sequence = "";
		String line;
		boolean hasNext = false;

		while( (line = reader.readLine()) != null ) 
		{
			if( line.length() > 0							// if line size > 0
				&& (line.charAt(0)=='>' || title.length()>0)// if next title line found or title already set	
				&& line.charAt(0)!=';')						// if line is not a comment
			{
				if( line.charAt(0)=='>' )					// if it is a title line
				{
					if(title.length()==0)					// if title is not set
					{
						title = line.trim();				// set title
					}
					else									// otherwise next title was found
					{	
						hasNext = true;
						nextTitle = line.trim();			// set nextTitle
						break;								// leave reading loop
					}
				}
				else
				{
					sequence += line.trim();				// append sequence parts
				}
			}
			else{} 											// otherwise just skip line	
		}
		if(!hasNext) nextTitle="";		
		// return new FastaRecord or null if no sequence and no title found
		return (title.length()>0 || sequence.length()>0) ? new FastaRecord(title, sequence) : null;
	}

	/**
	 * get all fasta records from the opened fasta string
	 * @return array of fasta records
	 */
	public FastaRecord[] getAllFastaRecords()
	{
		ArrayList<FastaRecord> set = new ArrayList<FastaRecord>();
		
		FastaRecord rec = null;
		do
		{
			try
			{
				rec = getNextFastaRecord();
				if(rec!=null) set.add(rec);
			}
			catch (IOException e){e.printStackTrace();}
		}
		while(rec!=null);
		
		FastaRecord[] result = new FastaRecord[set.size()];
		
		for(int i=0; i<set.size(); i++)
		{
			result[i] = set.get(i);
		}
		
		return result;
	}
	
	/**
	 * @param fastaString the fastaText to set
	 */
	public void setFastaString(String fastaString) 
	{
		this.fastaText = fastaString;
	}

	/**
	 * @return the fastaText
	 */
	public String getFastaString() 
	{
		return fastaText;
	}

	/**
	 * open given fasta string
	 * @param fastaString
	 * @throws IOException
	 */
	public void openFastaString(String fastaString) throws IOException
	{
		setFastaString(fastaString);
		open();
	}
	
	/**
	 * open stream from fasta string defined before
	 * @throws IOException
	 */
	public void open() throws IOException 
	{
		if(fastaText.length()<1) throw new IOException("no content in fasta string found.");

		close();
		nextTitle = "";
		reader = new BufferedReader( new StringReader(fastaText) );
	}
	
	/**
	 * close string stream
	 */
	public void close()
	{
		if(reader != null)
		try{
			reader.close();
			reader = null;
		} catch (IOException e) { e.printStackTrace(); }
	}
}
