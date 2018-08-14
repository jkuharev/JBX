package de.mz.jk.bio.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * static library containing usual protein calculation methods:<br>
 * - molecular weight calculation<br>
 * - isoelectric point calculation<br>
 * @author J.Kuharev
 */
public class PeptideWorker 
{
	public static void main(String[] args) 
	{
		String seq = "IYEVEGMR";
		System.out.println(PeptideWorker.getMolecularWeight(seq));
		
//		String seq = TestLib.getTestSequences()[0].getSequence();
//		System.out.println(
//			seq + "\nweight=" + getMolecularWeight(seq) + 
//			"\nisoelectric point=" + getIsoelectricPoint(seq)
//		);
	}
	
	public final static double finalWaterPlusProtonWeight = 19.01842;
	
	public static double hydrophobicity(char aa)
	{
		aa = ( "" + aa ).toLowerCase().charAt( 0 );
		// kite and doolitle 1982
		switch(aa)
		{
			case 'R': return -4.5;
			case 'K': return -3.9;
			case 'N': return -3.5;
			case 'D': return -3.5;
			case 'Q': return -3.5;
			case 'E': return -3.5;
			case 'H': return -3.2;
			case 'P': return -1.6;
			case 'Y': return -1.3;
			case 'W': return -0.9;
			case 'S': return -0.8;
			case 'T': return -0.7;
			case 'G': return -0.4;
			case 'A': return 1.8;
			case 'M': return 1.9;
			case 'C': return 2.5;
			case 'F': return 2.8;
			case 'L': return 3.8;
			case 'V': return 4.2;
			case 'I': return 4.5;
			default:
				return 0;
		}
	}
	/**
	 * Reference:  
	 * http://www.matrixscience.com/help/aa_help.html
	 */
	private static String AAData =
		//"1-Letter Symbol=3-Letter Symbol=Name=Average Mass=Monoisotopic Mass;"+
		"A=Ala=Alanine=71.0779=71.037114;"+
		"R=Arg=Arginine=156.1857=156.101111;"+
		"N=Asn=Asparagine=114.1026=114.042927;"+
		"D=Asp=Aspartic acid=115.0874=115.026943;"+
		"B=Asx=Asn or Asp=114.595=114.534935;"+
		"C=Cys=Cysteine=103.1429=103.009185;"+
		"E=Glu=Glutamic acid=129.114=129.042593;"+
		"Q=Gln=Glutamine=128.1292=128.058578;"+
		"Z=Glx=Glu or Gln=128.6216=128.5505855;"+
		"G=Gly=Glycine=57.0513=57.021464;"+
		"H=His=Histidine=137.1393=137.058912;"+
		"I=Ile=Isoleucine=113.1576=113.084064;"+
		"L=Leu=Leucine=113.1576=113.084064;"+
		"K=Lys=Lysine=128.1723=128.094963;"+
		"M=Met=Methionine=131.1961=131.040485;"+
		"F=Phe=Phenylalanine=147.1739=147.068414;"+
		"P=Pro=Proline=97.1152=97.052764;"+
		"S=Ser=Serine=87.0773=87.032028;"+
		"T=Thr=Threonine=101.1039=101.047679;"+
		"U=SeC=Selenocysteine=150.0379=150.95363;"+
		"W=Trp=Tryptophan=186.2099=186.079313;"+
		"Y=Tyr=Tyrosine=163.1733=163.06332;"+
		"X=Xaa=Unknown=0=0;"+
		"V=Val=Valine=99.1311=99.068414";
	/*
		"A=Ala=Alanine=89;"+
		"R=Arg=Arginine=174;"+
		"N=Asn=Asparagine=132;"+
		"D=Asp=Aspartic Acid=133;"+
		"B=Asx=Asparagine or aspartic acid=133;"+
		"C=Cys=Cysteine=121;"+
		"Q=Gln=Glutamine=146;"+
		"E=Glu=Glutamic acid=147;"+
		"Z=Glx=Glutamine or glutamic acid=147;"+
		"G=Gly=Glycine=75;"+
		"H=His=Histidine=155;"+
		"I=Ile=Isoleucine=131;"+
		"L=Leu=Leucine=131;"+
		"K=Lys=Lysine=146;"+
		"M=Met=Methionine=149;"+
		"F=Phe=Phenylalanine=165;"+
		"P=Pro=Proline=115;"+
		"S=Ser=Serine=105;"+
		"T=Thr=Threonine=119;"+
		"W=Trp=Tryptophan=204;"+
		"Y=Tyr=Tyrosine=181;"+
		"V=Val=Valine=117;";
	*/
	
	static Map<String, Double> one2av_mass = new HashMap<String, Double>();
	static Map<String, Double> one2mi_mass = new HashMap<String, Double>();
	static Map<String, String> one2three = new HashMap<String, String>();
	static Map<String, String> one2name = new HashMap<String, String>();
	static Map<String, String> three2one = new HashMap<String, String>();
	static Map<String, String> name2one = new HashMap<String, String>();
	
	static 
	{
		String[] AADataStrings = AAData.split(";");
		for(String aa : AADataStrings)
		{
			String[] tmp = aa.split("=");
			one2av_mass.put(tmp[0], Double.parseDouble(tmp[3]));
			one2mi_mass.put(tmp[0], Double.parseDouble(tmp[4]));
			one2three.put(tmp[0], tmp[1]);
			one2name.put(tmp[0], tmp[2]);
			three2one.put(tmp[1], tmp[0]);
			name2one.put(tmp[2], tmp[0]);
		}
	}
	
	/**
	 * calculate the molecular weight of a protein sequence
	 * @param sequence protein sequence
	 * @return molecular weight
	 */
	public static double getMolecularWeight(String sequence)
	{
		char[] aa = sequence.toUpperCase().toCharArray();
		double sum = 0.0;
		for(char a : aa)
		{
			try{
				double w = one2mi_mass.get(a+"");
				sum += w;
			}
			catch(Exception e)
			{
				System.err.println("'"+a+"' is not a known amino acid");
			}
		}
		if(sum>0) sum += finalWaterPlusProtonWeight; 
		return sum;
	}
	
	/**
	 * calculate the isoelectric point of a protein sequence
	 * using bisction algorithm from 
	 * http://isoelectric.ovh.org/files/practise-isoelectric-point.html#mozTocId763352
	 * @param sequence protein sequence
	 * @return pH the isoelectric point
	 */
	public static double getIsoelectricPoint(String sequence)
	{
		char[] protein = sequence.toCharArray();
		int ProtLength;                       //now we are getting protein length
		ProtLength = protein.length;

		final char Asp = 'D';
		final char Glu = 'E';
		final char Cys = 'C';
		final char Tyr = 'Y';
		final char His = 'H';
		final char Lys = 'K';
		final char Arg = 'R';

		int AspNumber = 0;
		int GluNumber = 0;
		int CysNumber = 0;
		int TyrNumber = 0;
		int HisNumber = 0;
		int LysNumber = 0;
		int ArgNumber = 0;
		
		//  we are looking for charged amino acids
		for(int i = 0; i <= protein.length - 1; ++i)             
		{
			switch(protein[i])
			{
				case Asp: ++AspNumber; break;
				case Glu: ++GluNumber; break;
				case Cys: ++CysNumber; break;
				case Tyr: ++TyrNumber; break;
				case His: ++HisNumber; break;
				case Lys: ++LysNumber; break;
				case Arg: ++ArgNumber; break;
				default:
			}
		}

		double NQ = 0.0; //net charge in given pH

		double QN1=0;  //C-terminal charge
		double QN2=0;  //D charge
		double QN3=0;  //E charge
		double QN4=0;  //C charge
		double QN5=0;  //Y charge
		double QP1=0;  //H charge
		double QP2=0;  //NH2 charge
		double QP3=0;  //K charge
		double QP4=0;  //R charge

		double pH = 6.5;             //starting point pI = 6.5 - theoretically it should be 7, but
		//average protein pI is 6.5 so we increase the probability
		double pHprev = 0.0;         //of finding the solution
		double pHnext = 14.0;        //0-14 is possible pH range
		double X = 0.0;
		double E = 0.01;             //epsilon means precision [pI = pH � E]
		double temp = 0.0;

		do
		{
			// we are using pK values form Wikipedia as they give quite good approximation
			// if you want you can change it
			QN1=-1/(1+Math.pow(10,(3.65-pH)));                                       
			QN2=-AspNumber/(1+Math.pow(10,(3.9-pH)));          
			QN3=-GluNumber/(1+Math.pow(10,(4.07-pH)));          
			QN4=-CysNumber/(1+Math.pow(10,(8.18-pH)));          
			QN5=-TyrNumber/(1+Math.pow(10,(10.46-pH)));       
			QP1=HisNumber/(1+Math.pow(10,(pH-6.04)));           
			QP2=1/(1+Math.pow(10,(pH-8.2)));               
			QP3=LysNumber/(1+Math.pow(10,(pH-10.54)));          
			QP4=ArgNumber/(1+Math.pow(10,(pH-12.48)));           

			NQ=QN1+QN2+QN3+QN4+QN5+QP1+QP2+QP3+QP4;


			if (pH>=14.0)
			{   
				//you should never see this message
				System.err.println("Something is wrong - pH is higher than 14");  //
				break;                                           
			}    

			//%%%%%%%%%%%%%%%%%%%%%%%%%   BISECTION   %%%%%%%%%%%%%%%%%%%%%%%%

			if(NQ<0) //we are out of range, thus the new pH value must be smaller   
			{                  
				temp = pH;
				pH = pH-((pH-pHprev)/2);
				pHnext = temp;
			}
			else //we used to small pH value, so we have to increase it
			{                    
				temp = pH;
				pH = pH + ((pHnext-pH)/2);
				pHprev = temp;
			}

			//conclusions: due the usage of bisection method we could shorten the calculation to 10-12 iterations!!!
		}//terminal condition, finding isoelectric point with given precision
		while(!((pH-pHprev<E)&&(pHnext-pH<E)));

		return pH;
	}
}
