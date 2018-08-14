package de.mz.jk.bio.fasta;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * read sequentially a fasta formatted file 
 * @author J.Kuharev
 */
public class FastaFileReader implements iFastaReader
{
	private File fastaFile = null;
	private BufferedReader reader = null;
	private String nextTitle = "";
	
	/**
	 * create new reader, 
	 * the current fasta file has to be defined before using
	 */
	public FastaFileReader(){}
	
	/**
	 * create new reader and define the current fasta file 
	 * @param fastaFile
	 */
	public FastaFileReader(File fastaFile)
	{
		this.fastaFile = fastaFile;
	}	
	
	/**
	 * open the given fasta file
	 * @param fastaFile the fastaFile to open
	 * @throws IOException 
	 */
	public void openFastaFile(File fastaFile) throws IOException 
	{
		this.fastaFile = fastaFile;
		open();
	}

	/**
	 * open the fasta file defined before
	 * @throws IOException
	 */
	public void open() throws IOException 
	{
		if(fastaFile==null) 
			throw new IOException("no file to open.");
		
		if(!fastaFile.canRead()) 
			throw new IOException(fastaFile.getAbsolutePath() + " is not readable.");

		close();
		nextTitle = "";
		reader = new BufferedReader( new FileReader(fastaFile) );
	}
	
	/**
	 * close file stream
	 */
	public void close()
	{
		if(reader != null)
		try{
			reader.close();
			reader = null;
		} catch (IOException e) { e.printStackTrace(); }
	}

	/**
	 * @return the current fastaFile
	 */
	public File getFastaFile() 
	{
		return fastaFile;
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
	 * get all fasta records from the opened fasta file
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
			catch(IOException e){e.printStackTrace();}
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
	 * read all fasta entries from a file
	 * @param fastaFile the file to read
	 * @return array of fasta records
	 * @throws IOException if something went wrong
	 */
	public FastaRecord[] getAllFastaRecords(File fastaFile) throws IOException
	{
		openFastaFile(fastaFile);
		FastaRecord[] result = getAllFastaRecords();
		close();
		return result;
	}
}
