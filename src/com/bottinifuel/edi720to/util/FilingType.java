package com.bottinifuel.edi720to.util;
/**************************************************************************
*
*   File name: FilingType
*   Defines all of the types of EDI filings
* 	 
* @author carlonc
* 
*************************************************************************/
/* Change Log:
* 
*   Date         Description                                        Pgmr
*  ------------  ------------------------------------------------   -----
*  Feb 12,2013    Intial Dev...                                     carlonc 
*************************************************************************/
public class FilingType {
	
	public static final int ORIGINAL     = 1; // Original Filing
    public static final int REPLACEMENT  = 2; // Original filing resulted in "must fix"
    public static final int SUPPLEMENTAL = 3; // New or additional data to Original or modified return 
    public static final int CORRECTED    = 4; // Adjusting or correcting an original or modified filing
    public static final int REJECTED     = 5; // Original file identified as FILE REJECTED - Out of balance
}	
