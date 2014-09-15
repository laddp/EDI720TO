package com.bottinifuel.edi720to.util;


/**************************************************************************
*
*   File name: ContextType
*   The class represents an X12 context for TOR filings.
*   A X12 context consists of a segment separator, element separator 
*   and a composite element separator. 
* 	 
* @author carlonc
* 
*************************************************************************/
/* Change Log:
* 
*   Date         Description                                        Pgmr
*  ------------  ------------------------------------------------   -----
*  Feb 20,2013    Intial Dev...                                     carlonc 
*************************************************************************/
import org.pb.x12.Context;

public class ContextType {
	
	public static final Context IRSCONTENT = new Context('\\', '~', '^');
  
}	
