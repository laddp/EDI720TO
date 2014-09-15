/**
 * 
 */
package com.bottinifuel.edi720to.util;

import java.io.Serializable;

/**
 * @author laddp
 *
 */
public class Abbreviation implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6818179995521434027L;
	public final String Term;
	public final String Abbreviation;
	
	public Abbreviation(String term, String abrrev)
	{
		Term = term;
		Abbreviation = abrrev;
	}
	
	public String toString()
	{
		return Abbreviation + " - " + Term;
	}
}
