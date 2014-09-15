package com.bottinifuel.edi720to;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.pb.x12.Segment;

import com.bottinifuel.edi720to.util.EDI151Messages;

/**************************************************************************
*
*   File name: Verify151Ack
*   Goes through the 151 Acknowledgement to verify an EDI filing
* 	 
* @author carlonc
* 
*************************************************************************
* Change Log:
* 
*   Date         Description                                        Pgmr
*  ------------  ------------------------------------------------   -----
*  Mar 05,2013   Intial Dev...                                     carlonc 
*************************************************************************/

public class Verify151Ack {
	
	private final boolean DEBUG = true; 
	
	private EDI720TO  MainApp; 
	private File      ack151File;    
	private EDIParser ack151          = null;
	private EDIParser filing          = null;
	//private boolean   syntaxError     = false; 
	//private boolean   functionalError = false;
	private String    btaReject       = "";  // BTA reject or acceptance message
	private String    filingType      = "";  // BTI filing type, 00, 6R, 6S, CO 
	private String    errorQty        = "";  // used to hold the error count 
	private String    verifyReport    = "";
	
	private Vector<String> correctV   = new Vector<String>();
	private Vector<String> infoV      = new Vector<String>();
	
	public Verify151Ack(EDI720TO app) {
		MainApp = app;
	}
	
	/**
	 * Sets the filing used for compare to 151 Acknowledgement 
	 * @param The filing File
	 */
	public int setFiling(File filingFile) {
		filing = new EDIParser(MainApp, filingFile); 
		return filing.getLastRC();
	}

	/**
	 * Sets the filing used for compare to 151 Acknowledgement 
	 * @param filing String
	 */
	public int setFiling(String filingString) {
		filing = new EDIParser(MainApp, filingString); 
		return filing.getLastRC();
	}
	
	/**
	 * Sets the 151 Acknowledgement used for compare  
	 * @param The 151 Ack File
	 */
	public int set151Ack(File ack151File) {
		this.ack151File = ack151File;
		ack151 = new EDIParser(MainApp, ack151File); 
		return ack151.getLastRC();
	}

	/**
	 * Sets the 151 Acknowledgement used for compare  
	 * @param The 151 Ack String
	 */
	public int set151Ack(String ack151String) {
		ack151 = new EDIParser(MainApp, ack151String);
		return ack151.getLastRC();
	}
	
	/**
	 * This starts the verify between the 151 Ack and the Filing 
	 * @return int 0 when all is good, else 1 
	 */
	public int startVerify() {
		if (ack151 == null) {
		    JOptionPane.showMessageDialog(MainApp.mainFrame,
	 				"<html>Verify incomplete, 151 Acknowlegement file has not been set", 
	 				"File not set Error",
	 				JOptionPane.ERROR_MESSAGE);
	    	return 1;
	   }
		   
	   if (filing == null) {
		   JOptionPane.showMessageDialog(MainApp.mainFrame,
	 				"<html>Verify incomplete, Filing Report file has not been set", 
	 				"File not set Error",
	 				JOptionPane.ERROR_MESSAGE);
	    	return 1;
		   
	   }
	   
	   int rc = checkForValid151Ack();
	   if (rc==2) { 
		   verifyReport = "<html>Verify incomplete, The 151 Acknowlegement is not a valid 151 Acknowledgement.<br>" +
		   		"Insure you have selected a valid 151 Ack.</html>";
		   JOptionPane.showMessageDialog(MainApp.mainFrame,
	 				verifyReport, 
	 				"Invalid 151 Acknowlegement",
	 				JOptionPane.ERROR_MESSAGE);
		    writeReport();
	    	return 1;
	   }
	   
	   rc = checkForSTMatches();  // Filing ST02 and ST03 must match 151 
	   if (rc==2) { 
		   // verifyReport already set  
		   JOptionPane.showMessageDialog(MainApp.mainFrame,
	 				verifyReport, 
	 				"ST mismatch",
	 				JOptionPane.ERROR_MESSAGE);
		   writeReport();    
	       return 1;
	   }	
	   
	   rc = checkForBTISegmentMatch();
	   if (rc==2) { 
		   verifyReport = "<html>Verify incomplete, filing and 151 BTI segment mismatch." +
				   "<br>Insure you have opened the correct 151 filing.";
		   JOptionPane.showMessageDialog(MainApp.mainFrame,
	 				verifyReport, 
	 				"Invalid 151 Acknowlegement",
	 				JOptionPane.ERROR_MESSAGE);
		    writeReport();
	    	return 1;
	   }
	   
	   rc = checkDTMEndDate();
	   if (rc==2) { 
		   verifyReport = "<html>Verify incomplete, filing and 151 End Dates mismatch." +
				   "<br>Insure you have opened the correct 151 filing.";
		   JOptionPane.showMessageDialog(MainApp.mainFrame,
	 				verifyReport, 
	 				"Invalid 151 Acknowlegement",
	 				JOptionPane.ERROR_MESSAGE);
		    writeReport();
	    	return 1;
	   }
	   
	   // if filing is not Original, then 151 REF~FJ control number must match ST02 in the original filing
	   if (!filingType.equals("00")) { // no need to check REF FJ in Original filings 
		   rc = checkRefFJ();  // TODO: get original filing ST02 
		   if (rc==2) { 
			   verifyReport = "<html>Verify incomplete, filing ST02 Control number and 151 ack REF FJ must match" +
					   "<br>when the filing is not an Original File." +
					   "<br>Insure you have opened the correct 151 filing." +
					   "<br>If the 151 is correct then contact Support to have this fixed.</html>"; 
			   JOptionPane.showMessageDialog(MainApp.mainFrame,
		 				verifyReport, 
		 				"Invalid 151 Acknowlegement",
		 				JOptionPane.ERROR_MESSAGE);
			    writeReport();
		    	return 1;
		   }	
	   }
	   
	  // all preliminary checks are done, now check to see if 151 has been accepted
	   rc = checkBTAAcceptance(); 
	   if (rc==0) { 
		   verifyReport = "<html><h2>151 Acknowledgement Report</h2><p>Verify complete." +
			   		 " The 151 Ack has accepted the filing with no errors.</html>";  
			   JOptionPane.showMessageDialog(MainApp.mainFrame,
		 				verifyReport, 
		 				"151 Acceptance",
		 				JOptionPane.INFORMATION_MESSAGE);
			   writeReport();
			   return 0;  // return with good rc
	   }
	   
	   // if we get here, the filing has been rejected 
	   rc = getErrorQty();  
	   
	   // start of html error report
	   verifyReport = "<html><h2 align=\"center\">151 Acknowledgement Error Report</h2>";
	   
	  
		   
	   
	   
	   rc = checkPBI();
	   //writeReport();
	   return 0;
	} // end startVerify  
	
	/**
	 * Validates if the 151 Acknowlegement is really a 151 Acknowlegement
	 * @return 0 for Valid Ack, 1 for Segment Error, 2 for invalid ack
	 * 
	 */
	private int checkForValid151Ack() {
		// 151 must be be in the Element 1 of the ST segment
		Segment s = ack151.getSegment("ST");
		if (ack151.getLastRC() != 0) {
			return 1; // error message already displayed
		}
		if (s.getElement(1).equals("151"))
			return 0;
		else 
			return 2;
	}
	
	/**
	 * This verifies ST02 and ST03 in the filing matches the 151
	 * ST02 verifies we are comparing the correct 151 for the filing
	 * ST03 verifies the IRS is using the same implementation version 
	 * @return 0 for match, 1 for Segment Error, 2 for mismatch
	 */
	private int checkForSTMatches() { 
		Segment fs = filing.getSegment("ST");
		if (filing.getLastRC() != 0) {
			return 1;  // error message already displayed
		}
		Segment as = ack151.getSegment("ST");
		if (ack151.getLastRC() != 0) {
			return 1;  // error message already displayed
		}
		// verify st02
		if (!fs.getElement(2).trim().equals(as.getElement(2).trim()))  {
			verifyReport = "<html>Verify incomplete, filing and 151 ST02 mismatch." +
				    "<br>Insure you have opened the correct 151 filing.";
			return 2;
		}
		// verify st03
	    if (!fs.getElement(3).trim().equals(as.getElement(3).trim())) {
	    	verifyReport = "<html>Verify incomplete, filing and 151 ST03 mismatch." +
		             "<br>The IRS implementation version is different from the filing." +
			         "<br>Contact Support to have this corrected.</html>"; 
		   return 2;
	    }   
	    else 
		   return 0;
	}
	
	/**
	 * This verifies filing BTI matches the 151
	 * It is a further check to see if we are working withe the correct 151 filing
	 * @return 0 for match, 1 for Segment Error, 2 for mismatch
	 */
	private int checkForBTISegmentMatch() {
		Segment fs = filing.getSegment("BTI");
		if (filing.getLastRC() != 0) {
			return 1;  // error message already displayed
		}
		Segment as = ack151.getSegment("BTI");
		if (ack151.getLastRC() != 0) {
			return 1;  // error message already displayed
		}
		
		// get the filing type
		if (fs.getElement(13).equals("00")) {
			filingType = "00";
		}
		else 
			filingType = fs.getElement(14);
		
		// entire BTI segment must match
		if (fs.toString().equals(as.toString()))  
			return 0;
		else 
			return 2;
		
	}
	
	/**
	 * This verifies filing end date and 151 match
	 * It is a further check to see if we are working withe the correct 151 filing
	 * @return 0 for match, 1 for Segment Error, 2 for mismatch
	 */
	private int checkDTMEndDate() {
		String filingEndDate = "";
		String ack151EndDate = "";
		Vector <Segment>segsV = filing.getSegments("DTM");
		if (filing.getLastRC() != 0) {
			return 1;  // error message already displayed
		}
		for (Segment s: segsV) {
			if (s.getElement(1).equals("194")) {
			   filingEndDate = s.getElement(2);
			   break;
		    }   
		}
		
		segsV.setSize(0);
		segsV = ack151.getSegments("DTM");
		if (ack151.getLastRC() != 0) {
			return 1;  // error message already displayed
		}
		for (Segment s: segsV) {
			if (s.getElement(1).equals("194")) {
				ack151EndDate = s.getElement(2);
			   break;
		    }   
		}
		
		// ending dates must match
		if (filingEndDate.equals(ack151EndDate))  
			return 0;
		else 
			return 2;	
	}
	
	/**
	 * Checks BTA01 for acceptance or rejection. 
	 * @return 0 for Acceptance, 1 for Segment Error, 2 for Rejection
	 */
	private int checkBTAAcceptance() {
		Segment s = ack151.getSegment("BTA");	
		if (ack151.getLastRC() != 0) {
			return 1;  // error message already displayed
		}
		if (s.getElement(1).equals("AT")) 
			return 0;
		else {
			btaReject = EDI151Messages.getBTA01Message(s.getElement(1));
		    return 2;
		}    
	}

    /**
     * if filing is not Original, then 151 REF~FJ control number must match ST02 in the original filing
      * @return 0 for Acceptance, 1 for Segment Error, 2 for Rejection
     */
	private int checkRefFJ() {
		String ack151Ref02 = "";
				
		Vector <Segment>segsV = ack151.getSegments("REF");
		if (ack151.getLastRC() != 0) {
			return 1;  // error message already displayed
		}
		for (Segment s: segsV) {
			if (s.getElement(1).equals("FJ")) {
				ack151Ref02 = s.getElement(2);
			   break;
		    }   
		}
		
		Segment s = filing.getSegment("ST");
		if (filing.getLastRC() != 0) {
			return 1;  // error message already displayed
		}
		
		if (!s.getElement(2).equals(ack151Ref02)) {
		   return 2;
		}
		else 
			return 0;
	}
	
	/**
	 * This reads the QTY segment to get the 151 ack error count 
	  * @return 0 for Acceptance, 1 for Segment Error
	 */
	private int getErrorQty() {
		Segment s = ack151.getSegment("QTY");	
		if (ack151.getLastRC() != 0) {
			return 1;  // error message already displayed
		}
		errorQty = s.getElement(2);
		return 0;
	}
		
	private int checkPBI() {
		Vector <Segment>segsV = ack151.getSegments("PBI");
		if (ack151.getLastRC() != 0) {
			return 1;  // error message already displayed
		}
		if (segsV.size() == 0) // no error here
			return 0;
		
		String element;
		String primary;
		String secondary;
		String message; 
		
		for(Segment s: segsV) {
			// reset for this round
			element   = "";
			primary   = "";
			secondary = "";
			message  =  "";
			try {
				// parse segment 1
				element = s.getElement(1); // consists of primary and secondary codes 
				primary   = element.substring(0,4);
				secondary = element.substring(4);
				message = message+"<h3>"+EDI151Messages.getPBI01PrimaryDescription(Integer.parseInt(primary))+" ";
				message = message+EDI151Messages.getPBI01PrimaryElementID(Integer.parseInt(primary))+": ";
				message = message+EDI151Messages.getPBI01SecondaryDescription(Integer.parseInt(secondary))+"</h3>";
				
				// parse segment 3 next 
				element = s.getElement(3); // consists of primary and secondary codes 
				primary   = element.substring(0,3);
				secondary = element.substring(3);
				message = message+EDI151Messages.getPBI03PrimaryDescription(Integer.parseInt(primary))+", ";
				message = message+EDI151Messages.getPBI03SecondaryDescription(Integer.parseInt(secondary))+". ";
			} catch (Exception e) {
				JOptionPane.showMessageDialog(MainApp.mainFrame,
		 				"<html>Contact Support, 151 PBI Verify fail:<br>"+e+"</html>", 
		 				"151 Verify Failure",
		 				JOptionPane.ERROR_MESSAGE);
				return 1;
			}
			
			// just add segment 4 to message
			message = message+"<br>"+EDI151Messages.getPBI04Description(s.getElement(4).trim())+". ";
			
			// parse segment 5
			element = s.getElement(5).trim();
			if (!element.equals("")) 
				message = message+"<br>"+message+element+". ";
			
			// parse segment 6 
			element = s.getElement(6).trim();
			if (!element.equals("")) 
				message = message+"<br>"+element;
			
			// parse segment 2, Contains CO (Correct Errors) or NA (No Action)
			element = s.getElement(2).trim();
			if (!element.equals("CO")) 
				correctV.addElement(message);
			else 
				infoV.addElement(message); 
			
			if (DEBUG) {
				JOptionPane.showMessageDialog(MainApp.mainFrame,
		 				"<html>"+message+"</html>", 
		 				"DEBUG Mode Message",
		 				JOptionPane.INFORMATION_MESSAGE);
			}
		} // end for
		return 0; 
	}  
	
	/**
	 * Write out the results of the 151 verify and add to currentFiling
	 */
	private void writeReport() {
		// TODO: Add 151 to CurrentFiling in Verify151Ack
		String fileName = ack151File.toString();
		String newFile  = "";
		int i           = fileName.lastIndexOf('.');
		try {
			String part1 = fileName.substring(0, i);
			//String part2 = fileName.substring(i);
			newFile = part1+"verify.html";
		} catch (IndexOutOfBoundsException e ) {
			 JOptionPane.showMessageDialog(MainApp.mainFrame,
		 				"<html>151 Verify Report write failed<br>"+e.getMessage(), 
		 				"Filing Report Error",
		 				JOptionPane.INFORMATION_MESSAGE);
		}
		File file = new File(newFile);
		   boolean fileExists = file.exists();
		   if (fileExists) {
			   int answer = JOptionPane.showConfirmDialog(MainApp.mainFrame,
					    "<html>File "+newFile+"<br>already exists, over write file?", 
					    "Duplicate File",                  
			            JOptionPane.YES_NO_OPTION,   
			            JOptionPane.QUESTION_MESSAGE);
			  if (answer == JOptionPane.NO_OPTION)                                 
			           return;  /// user does not wish to overwrite                                      
			   
		   }
			   
		   try {
		         BufferedWriter out = new 
		         BufferedWriter(new FileWriter(newFile));
		         out.write(verifyReport);
		         out.close();
		         if (!fileExists) {  // don't show another dialog 
			         JOptionPane.showMessageDialog(MainApp.mainFrame,
			 				"<html>151 Verify Report saved as<br>"+newFile, 
			 				"151 Verify Report Save",
			 				JOptionPane.INFORMATION_MESSAGE);
		         }
		 
		      }
		      catch (IOException e) {
		    	  JOptionPane.showMessageDialog(MainApp.mainFrame,
			 				"<html>151 Verify Report write failed<br>"+e.getMessage(), 
			 				"Filing Report Error",
			 				JOptionPane.INFORMATION_MESSAGE);
		      }
	}

}
