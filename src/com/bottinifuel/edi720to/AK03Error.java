package com.bottinifuel.edi720to;

import static com.bottinifuel.edi720to.util.EDI997Messages.getAK3Message;

/**************************************************************************
*
*   File name: Segment Error
*   Defines the elements in a segment error as reported in a 997 or 151
*   acknowledgement report 
* 	 
* @author carlonc
* 
*************************************************************************/
/* Change Log:
* 
*   Date         Description                                        Pgmr
*  ------------  ------------------------------------------------   -----
*  Mar 04,2013   Intial Dev...                                     carlonc 
*************************************************************************/

public class AK03Error {
	
	public String SegmentID;
	public String SegmentPosition;
	public String LoopID;
	public String SegmentError;
	
	public AK03Error(String SegmentID, String SegmentPosition, String LoopID, String SegmentError) {
		this.SegmentID        = SegmentID;
		this.SegmentPosition  = SegmentPosition;
		this.LoopID           = LoopID;
		try {
		   this.SegmentError    = getAK3Message(Integer.parseInt(SegmentError.trim()));
		} catch (NumberFormatException e) {
			this.SegmentError = SegmentError;
		}  
	}
}
