package de.mz.jk.bio.fasta;

import java.io.IOException;

/**
 * define common FastaReader methods 
 * @author J.Kuharev
 */
public interface iFastaReader 
{
	/**
	 * get next fasta record
	 * @return next FastaRecord
	 * @throws IOException 
	 */
	public FastaRecord getNextFastaRecord() throws IOException;
	
	/**
	 * get all fasta records
	 * @return array of fasta records
	 */
	public FastaRecord[] getAllFastaRecords();
	
	/**
	 * open a stream before
	 * @throws IOException
	 */
	public void open() throws IOException;
	
	/**
	 * close a stream
	 */
	public void close();
}
