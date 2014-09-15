package com.bottinifuel.edi720to;

/**************************************************************************
*
*   File name: Verify997Ack
*   Goes through the 997 Acknowledgement to verify an EDI filing
* 	 
* @author carlonc
* 
*************************************************************************
* Change Log:
* 
*   Date         Description                                        Pgmr
*  ------------  ------------------------------------------------   -----
*  Mar 04,2013   Intial Dev...                                     carlonc 
*************************************************************************/

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.pb.x12.Segment;

import com.bottinifuel.edi720to.util.EDI997Messages;

public class Verify997Ack {
	
	private EDI720TO  MainApp; 
	private File      ack997File;    
	private EDIParser ack997          = null;
	private EDIParser filing          = null;
	private boolean   syntaxError     = false; 
	private boolean   functionalError = false;
	private String    verifyReport    = "";
			
	public Verify997Ack(EDI720TO app) {
		MainApp = app;
	}
	
	/**
	 * Sets the filing used for compare to 997 Acknowledgement 
	 * @param The filing File
	 */
	public int setFiling(File filingFile) {
		filing = new EDIParser(MainApp, filingFile); 
		return filing.getLastRC();
	}

	/**
	 * Sets the filing used for compare to 997 Acknowledgement 
	 * @param filing String
	 */
	public int setFiling(String filingString) {
		filing = new EDIParser(MainApp, filingString); 
		return filing.getLastRC();
	}
	
	/**
	 * Sets the 997 Acknowledgement used for compare  
	 * @param The 997 Ack File
	 */
	public int set997Ack(File ack997File) {
		this.ack997File = ack997File;
		ack997 = new EDIParser(MainApp, ack997File); 
		return ack997.getLastRC();
	}

	/**
	 * Sets the 997 Acknowledgement used for compare  
	 * @param The 997 Ack String
	 */
	public int set997Ack(String ack997String) {
		ack997 = new EDIParser(MainApp, ack997String);
		return ack997.getLastRC();
	}
	
	/**
	 * This starts the verify between the 997 Ack and the Filing 
	 * @return int 0 when all is good, else 1 
	 */
	public int startVerify() {
		if (ack997 == null) {
		    JOptionPane.showMessageDialog(MainApp.mainFrame,
	 				"<html>Verify incomplete, 997 Acknowlegement file has not been set", 
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
	   int rc = checkForValid997Ack();
	   if (rc==2) { 
		   verifyReport = "<html>Verify incomplete, The 997 Acknowlegement is not a valid 997 Acknowledgement.<br>Insure you have selected a valid 997 Ack.</html>";
		   JOptionPane.showMessageDialog(MainApp.mainFrame,
	 				verifyReport, 
	 				"Invalid 997 Acknowlegement",
	 				JOptionPane.ERROR_MESSAGE);
		    writeReport();
	    	return 1;
		   
	   }
	   rc = checkForST03Match();  // Filing ST03 must match 997 ST03
	   if (rc==2) { 
		   verifyReport = "<html>Verify incomplete, filing and 997 ST03 mismatch." +
			       "<br>The IRS implementation version is different from the filing." +
				   "<br>Contact Support to have this corrected.</html>"; 
		   JOptionPane.showMessageDialog(MainApp.mainFrame,
	 				verifyReport, 
	 				"ST03 mismatch",
	 				JOptionPane.ERROR_MESSAGE);
		   writeReport();    
	       return 1;
	   }	
	 
	   rc = check997AK101(); // must contain "TF"
	   if (rc==2) { 
		   verifyReport = "<html>Verify incomplete, the functional ID in the 997 Ack (AK101 element) is not TF.<br>Insure you have selected the correct 997 Ack.</html>"; 
		   JOptionPane.showMessageDialog(MainApp.mainFrame,
	 				verifyReport, 
	 				"Invalid 997 Functional ID",
	 				JOptionPane.ERROR_MESSAGE);
		    writeReport();
	    	return 1;
		   
	   }
	   
	   rc = checkFilingGS01(); // must contain "TF"
	   if (rc==2) {
		   verifyReport = "<html>Verify incomplete, the functional ID in the filing (GS01 element) is not TF</html>"; 
		   JOptionPane.showMessageDialog(MainApp.mainFrame,
	 				verifyReport, 
	 				"Invalid Filing Functional ID",
	 				JOptionPane.ERROR_MESSAGE);
		    writeReport();
	    	return 1;
		   
	   }
	   rc = checkForGS02Match(); // GS02 of the filing must match AK102 of the 997
	   if (rc==2) { 
		   verifyReport = "<html>Verify incomplete, filing GS02 and AK102 mismatch.<br>Insure you have selected the correct 997 Ack.</html>"; 
		   JOptionPane.showMessageDialog(MainApp.mainFrame,
	 				verifyReport, 
	 				"GS02 and AK102 mismatch",
	 				JOptionPane.ERROR_MESSAGE);
		    writeReport();
	    	return 1;
	   }
	   
	   rc = check997AK201(); // Must contain 813 
	   if (rc==2) { 
		   verifyReport = "<html>Verify incomplete, the functional ID in the 997 Ack (AK201 element) is not 813.<br>Insure you have selected the correct 997 Ack.</html>";
		   JOptionPane.showMessageDialog(MainApp.mainFrame,
				    verifyReport, 
	 				"Invalid Filing Functional ID",
	 				JOptionPane.ERROR_MESSAGE);
		    writeReport();
	    	return 1;
	   }
	   
	   rc = checkFilingST01(); // Must contain 813 
	   if (rc==2) { 
		   verifyReport = "<html>Verify incomplete, the functional ID in the filing (ST01 element) is not 813</html>";
		   JOptionPane.showMessageDialog(MainApp.mainFrame,
				    verifyReport,
	 				"Invalid Filing Functional ID",
	 				JOptionPane.ERROR_MESSAGE);
		    writeReport();
	    	return 1;
	   }
	   
	   rc = checkForST02Match(); 
	   if (rc==2) { 
		   verifyReport = "<html>Verify incomplete, filing ST02 and AK202 mismatch.<br>Insure you have selected the correct 997 Ack.</html>";
		   JOptionPane.showMessageDialog(MainApp.mainFrame,
	 				verifyReport, 
	 				"GS02 and AK102 mismatch",
	 				JOptionPane.ERROR_MESSAGE);
		    writeReport();
	    	return 1;
	   }	   
	   
	   // all preliminary checks are done, now check to see if 997 has been accepted
	   rc = checkAK5Acceptance();  // this checks for syntax errors
	   if (rc==2) { 
	       syntaxError = true;
	   }
	   else 
		   syntaxError = false; 
	   
	   // this checks for functional ID errors
	   rc = checkAK9Acceptance();
	   if (rc==2) { 
	       functionalError = true;
	   }
	   else 
		   functionalError = false; 
	   
	   if ((!syntaxError) && (!functionalError)) {  // all is well  
		   verifyReport = "<html><h2>997 Acknowledgement Report</h2><p>Verify complete." +
		   		" The 997 Ack has accepted the filing.<br>A 151 Acknowledgement will follow.</html>";  
		   JOptionPane.showMessageDialog(MainApp.mainFrame,
	 				verifyReport, 
	 				"997 Acceptance",
	 				JOptionPane.INFORMATION_MESSAGE);
		   writeReport();
		   return 0;
	   }
	   
	   // if we get here, the filing has been rejected 
	   // start of html error report
	   verifyReport = "<html><h2 align=\"center\">997 Acknowledgement Error Report</h2>";
	   verifyReport = verifyReport +"<h3 align=\"center\">Contact Support when the 977 Acknowledgement fails to verify</h3>";
	   rc = checkAK3();        // checks for Segment Syntax errors 
	   rc = checkAK4();        // checks for Element syntax errors 
	   rc = checkAK5Errors();  // checks for Transaction set errors
	   rc = checkAK9Errors();  // checks for Functional Group errors
	   verifyReport = verifyReport +"</html>";
	   
	   JOptionPane.showMessageDialog(MainApp.mainFrame,
				verifyReport, 
				"997 Error Report",
				JOptionPane.ERROR_MESSAGE);
	   writeReport();
	   return 0;
	}
	
	/**
	 * Validates if the 997 Acknowlegement is really a 997 Acknowlegement
	 * @return 0 for Valid Ack, 1 for Segment Error, 2 for invalid ack
	 * 
	 */
	private int checkForValid997Ack() {
		// 997 must be be in the Element 1 of the ST segment
		Segment s = ack997.getSegment("ST");
		if (ack997.getLastRC() != 0) {
			return 1; // error message already displayed
		}
		if (s.getElement(1).equals("997"))
			return 0;
		else 
			return 2;
	}
	
	/**
	 * This verifies ST03 in the filing matches ST03 in the 997
	 * This match verifies the Implementaion Convention used in the filing
	 * @return 0 for match, 1 for Segment Error, 2 for mismatch
	 */
	private int checkForST03Match() { 
		Segment fs = filing.getSegment("ST");
		if (filing.getLastRC() != 0) {
			return 1;  // error message already displayed
		}
		Segment as = ack997.getSegment("ST");
		if (ack997.getLastRC() != 0) {
			return 1;  // error message already displayed
		}
		if (fs.getElement(3).trim().equals(as.getElement(3).trim())) 
			return 0;
		else 
			return 2;
		
	}
	
	/**
	 * This verifies the Functional ID Identifier Code is TF in AK101 of the 997
	 * acknowledgement. TF identifies an 813 transaction set   
	 * @return 0 for Valid AK101, 1 for Segment Error, 2 for invalid AK101
	 */
	private int check997AK101() {
		// Functional ID must be TF element 1 of the AK1 segment
		Segment s = ack997.getSegment("AK1");
		if (ack997.getLastRC() != 0) {
			return 1;  // error message already displayed
		}
		if (s.getElement(1).equals("TF"))
			return 0;
		else 
			return 2;
	}
	
	/**
	 * This verifies the Functional ID Identifier Code is TF in the GS01 segment of the 
	 * filing being acknowledged. TF identifies an 813 transaction set   
	 * @return 0 for Valid GS01, 1 for Segment Error, 2 for invalid GS01
	 */
	private int checkFilingGS01() {
		// Functional ID must be TF element 1 in the GS segment
		Segment s = filing.getSegment("GS");
		if (filing.getLastRC() != 0) {
			return 1;  // error message already displayed
		}
		if (s.getElement(1).equals("TF"))
			return 0;
		else 
			return 2;
	}
	
	/**
	 * This verifies GS02 in the filing matches AK102 in the 997
	 * @return 0 for match, 1 for Segment Error, 2 for mismatch
	 */
	private int checkForGS02Match() {
		Segment fs = filing.getSegment("GS");
		if (filing.getLastRC() != 0) {
			return 1;  // error message already displayed
		}
		Segment as = ack997.getSegment("AK1");
		if (ack997.getLastRC() != 0) {
			return 1;  // error message already displayed
		}
		if (fs.getElement(2).trim().equals(as.getElement(2).trim())) 
			return 0;
		else 
			return 2;
	}
	
	/**
	 * This verifies the Functional ID Identifier Code is 813 in the AK201 segment of the 
	 * 997. 813 identifies an 813 transaction set   
	 * @return 0 for Valid GS01, 1 for Segment Error, 2 for invalid GS01
	 */
	private int check997AK201() {
		// Functional ID must be 813 in element 1 of the AK2 segment
		Segment s = ack997.getSegment("AK2");
		if (ack997.getLastRC() != 0) {
			return 1;  // error message already displayed
		}
		if (s.getElement(1).equals("813"))
			return 0;
		else 
			return 2;
	}
	
	/**
	 * This verifies the element one in the ST segment is ST
	 * @return 0 for Valid ST01, 1 for Segment Error, 2 for invalid ST01
	 */
	private int checkFilingST01() {
		// Functional ID must be 813 in element 1 of the ST segment
		Segment s = filing.getSegment("ST");
		if (filing.getLastRC() != 0) {
			return 1;  // error message already displayed
		}
		if (s.getElement(1).equals("813")) 
			return 0;
		else 
			return 2;
	}
	
	/**
	 * This verifies ST02 in the filing matches AK202 in the 997
	 * @return 0 for match, 1 for Segment Error, 2 for mismatch
	 */
	private int checkForST02Match() {
		Segment fs = filing.getSegment("ST");
		if (filing.getLastRC() != 0) {
			return 1;  // error message already displayed
		}
		Segment as = ack997.getSegment("AK2");
		if (ack997.getLastRC() != 0) {
			return 1;  // error message already displayed
		}
		if (fs.getElement(2).trim().equals(as.getElement(2).trim())) 
			return 0;
		else 
			return 2;
	}
	
	/**
	 * Checks AK501 for acceptance or rejection. AK501 verifies filing syntax 
	 * @return 0 for Acceptance, 1 for Segment Error, 2 for Rejection
	 */
	private int checkAK5Acceptance() {
		Segment s = ack997.getSegment("AK5");	
		if (ack997.getLastRC() != 0) {
			return 1;  // error message already displayed
		}
		if (s.getElement(1).equals("A")) 
			return 0;
		else 
		    return 2;
	}
	
	/**
	 * Checks AK901 for acceptance or rejection. AK901 verifies functional errors 
	 * @return 0 for Acceptance, 1 for Segment Error, 2 for Rejection
	 */
	private int checkAK9Acceptance() {
		Segment s = ack997.getSegment("AK9");	
		if (ack997.getLastRC() != 0) {
			return 1;  // error message already displayed
		}
		if (s.getElement(1).equals("A")) 
			return 0;
		else 
			return 2;
	}
	
	/**
	 * AK3 segment reports errors in a data segment and identifies the location of the data segment
	 * This builds the HTML to display any AK3 element errors
	 * @return 0 for no errors found, 1 for Segment Error, 2 for errors found
	 * 
	 */
	private int checkAK3() {
		Vector <Segment>segsV = ack997.getSegments("AK3");
		if (ack997.getLastRC() != 0) {
			return 1;  // error message already displayed
		}
		if (segsV.size() == 0) // no error here
			return 0;
		
		// set up the table header
		verifyReport = verifyReport +"<br><table border=\"1\" id=\"segment-errors\" class=\"tablesorter\">";
		verifyReport = verifyReport +"<caption>Segment Syntax Errors</caption>";
		verifyReport = verifyReport +"<thead>";
		verifyReport = verifyReport +"<tr><th class=\"header\">Segment ID</th>";
		verifyReport = verifyReport +"<th class=\"header\">Segment Position</th>";
		verifyReport = verifyReport +"<th class=\"header\">Loop Identifier</th>";
		verifyReport = verifyReport +"<th class=\"header\">Segment Error</th>";
		verifyReport = verifyReport +"</tr>";
		verifyReport = verifyReport +"</thead>";
		verifyReport = verifyReport +"<tbody>";
		
		for (Segment s: segsV) {
			verifyReport = verifyReport +"<tr>";
			verifyReport = verifyReport +"<td>"+s.getElement(1)+"</td>";
			verifyReport = verifyReport +"<td>"+s.getElement(2)+"</td>";
			verifyReport = verifyReport +"<td>"+s.getElement(3)+"</td>";
			try {
				verifyReport = verifyReport +"<td>"+EDI997Messages.getAK3Message(Integer.parseInt(s.getElement(4).trim()))+"</td>";
			} catch (NumberFormatException e) {
				verifyReport = verifyReport +"<td>"+s.getElement(4)+"</td>";
			}  
			verifyReport = verifyReport +"</tr>";
		}
		verifyReport = verifyReport +"</tbody>";
		verifyReport = verifyReport +"</table>";
		
		return 0;
	}
	
	/**
	 * AK4 segment reports errors in a data segment element 
	 * This builds the HTML to display any AK4 element errors
	 * @return 1 for Segment Error, else 0             
	 * 
	 */
	private int checkAK4() {
		Vector <Segment>segsV = ack997.getSegments("AK4");
		if (ack997.getLastRC() != 0) {
			return 1;  // error message already displayed
		}
		if (segsV.size() == 0) // no error here
			return 0;
		
		// set up the table header
		verifyReport = verifyReport +"<br><table border=\"1\" id=\"elment-errors\" class=\"tablesorter\">";
		verifyReport = verifyReport +"<caption>Element Syntax Errors</caption>";
		verifyReport = verifyReport +"<thead>";
		verifyReport = verifyReport +"<tr><th class=\"header\">Pos in Segment</th>";
		verifyReport = verifyReport +"<th class=\"header\">Element Ref Number</th>";
		verifyReport = verifyReport +"<th class=\"header\">Element Syntax Error</th>";
		verifyReport = verifyReport +"<th class=\"header\">Copy of Bad Element</th>";
		verifyReport = verifyReport +"</tr>";
		verifyReport = verifyReport +"</thead>";
		verifyReport = verifyReport +"<tbody>";
		
		for (Segment s: segsV) {
			verifyReport = verifyReport +"<tr>";
			verifyReport = verifyReport +"<td>"+s.getElement(1)+"</td>";
			verifyReport = verifyReport +"<td>"+s.getElement(2)+"</td>";
			try {
				verifyReport = verifyReport +"<td>"+EDI997Messages.getAK4Message(Integer.parseInt(s.getElement(3).trim()))+"</td>";
			} catch (NumberFormatException e) {
				verifyReport = verifyReport +"<td>"+s.getElement(3)+"</td>";
			}  
			verifyReport = verifyReport +"<td>"+s.getElement(4)+"</td>";
			verifyReport = verifyReport +"</tr>";
		}
		verifyReport = verifyReport +"</tbody>";
		verifyReport = verifyReport +"</table>";
		
		return 0;
	}
	
	/**
	 * AK5 segment reports errors in the transaction set
	 * This builds the HTML to display any AK5 errors
	 * @return 1 for Segment Error, else 0             
	 * 
	 */
	private int checkAK5Errors() {
		// one iteration of the AK5 exists
		Segment s = ack997.getSegment("AK5");	
		if (ack997.getLastRC() != 0) {
			return 1;  // error message already displayed
		}
		List<String> elements = s.getElements();  
		if (elements.size() <= 2)
			return 0;  // nothing to report 
		
		// set up the table header
		verifyReport = verifyReport +"<br><table border=\"1\" id=\"transaction-errors\" class=\"tablesorter\">";
		verifyReport = verifyReport +"<caption>Transaction Set Syntax Errors</caption>";
		verifyReport = verifyReport +"<thead><tr>";
		// loop to number of syntax errors reported (can be up to 5)
		for (int i=2; i<elements.size(); i++) { 
		   verifyReport = verifyReport +"<th class=\"header\">Transaction Syntax Error</th>";
		}
		verifyReport = verifyReport +"</tr>";
		verifyReport = verifyReport +"</thead>";
		
		// now add the data
		verifyReport = verifyReport +"<tbody><tr>";
		// loop adds the rest, if there
		for (int i=2; i<elements.size(); i++) { 
			try {
				verifyReport = verifyReport +"<td>"+EDI997Messages.getAK5Message(Integer.parseInt(elements.get(i)))+"</td>";
			} catch (NumberFormatException e) {
				verifyReport = verifyReport +"<td>"+elements.get(i)+"</td>";
			}  
		}
		// add ending tags for table row	
		verifyReport = verifyReport +"</tbody>";
		verifyReport = verifyReport +"</table>";
		return 0;
	}
	
	/**
	 * AK9 segment reports errors in the Functional Group  
	 * This builds the HTML to display any AK9 functional errors
	 * @return 1 for Segment Error, else 0             
	 * 
	 *//*
	private int checkAK9ErrorsTwoTables() {
		// one iteration of the AK5 exists
		Segment s = ack997.getSegment("AK9");	
		if (ack997.getLastRC() != 0) {
			return 1;  // error message already displayed
		}
		List<String> elements = s.getElements();  
		if (elements.size() <= 5)
			return 0;  // nothing to report 
		
		// set up the table header for the Transaction set numbers  
		errorReport = errorReport +"<br><table border=\"1\" id=\"transaction-errors\" class=\"tablesorter\">";
		errorReport = errorReport +"<caption>Functional Group Numbers</caption>";
		errorReport = errorReport +"<thead><tr>";
		errorReport = errorReport +"<th class=\"header\">Num of Included Tran Sets</th>";
		errorReport = errorReport +"<th class=\"header\">Num of Received Tran Sets</th>";
		errorReport = errorReport +"<th class=\"header\">Num of Accepted Tran Sets</th>";
		errorReport = errorReport +"</tr>";
		errorReport = errorReport +"</thead>";
		// now add the tranaction set number data
		errorReport = errorReport +"<tbody><tr>";
		errorReport = errorReport +"<td>"+s.getElement(2)+"</td>";
		errorReport = errorReport +"<td>"+s.getElement(3)+"</td>";
		errorReport = errorReport +"<td>"+s.getElement(4)+"</td>";
		errorReport = errorReport +"</tr></tbody>";
		errorReport = errorReport +"</table>";
		
		// next table is for the details of the errors
		// decided to do a second table here since the table can get too wide
		errorReport = errorReport +"<br><table border=\"1\" id=\"transaction-errors\" class=\"tablesorter\">";
		errorReport = errorReport +"<caption>Functional Group Syntax Error</caption>";
		errorReport = errorReport +"<thead><tr>";
		errorReport = errorReport +"<th class=\"header\">Functional Group Error</th>";
		errorReport = errorReport +"</tr>";
		errorReport = errorReport +"</thead>";
		
		// now add the tranaction set number data
		errorReport = errorReport +"<tbody>";
		// loop adds the rest, if there
		for (int i=5; i<elements.size(); i++) { 
			errorReport = errorReport +"<tr>";
			try {
				errorReport = errorReport +"<td>"+EDI997Messages.getAK9Message(Integer.parseInt(elements.get(i)))+"</td>";
			} catch (NumberFormatException e) {
				errorReport = errorReport +"<td>"+elements.get(i)+"</td>";
			} 
			errorReport = errorReport +"</tr>";
		}
		// add ending tags for table row	
		errorReport = errorReport +"</tbody>";
		errorReport = errorReport +"</table>";
		
		return 0;
	}*/
	
	/**
	 * AK9 segment reports errors in the Functional Group  
	 * This builds the HTML to display any AK9 functional errors
	 * @return 1 for Segment Error, else 0             
	 * 
	 */
	private int checkAK9Errors() {
		// one iteration of the AK5 exists
		Segment s = ack997.getSegment("AK9");	
		if (ack997.getLastRC() != 0) {
			return 1;  // error message already displayed
		}
		List<String> elements = s.getElements();  
		if (elements.size() <= 5)
			return 0;  // nothing to report 
		
		// set up the table header  
		verifyReport = verifyReport +"<br><table border=\"1\" id=\"transaction-errors\" class=\"tablesorter\">";
		verifyReport = verifyReport +"<caption>Functional Group Syntax Errors</caption>";
		verifyReport = verifyReport +"<thead><tr>";
		verifyReport = verifyReport +"<th class=\"header\">Included</th>";
		verifyReport = verifyReport +"<th class=\"header\">Received</th>";
		verifyReport = verifyReport +"<th class=\"header\">Accepted</th>";
		
		/*// loop for number of Syntax errors reported (can be up to 5)
		for (int i=5; i<elements.size(); i++) { 
		   errorReport = errorReport +"<th class=\"header\">Functional Group Syntax Error</th>";
		}*/
		verifyReport = verifyReport +"</tr>";
		verifyReport = verifyReport +"</thead>";
		
		// now add the tranaction set number data
		verifyReport = verifyReport +"<tbody><tr>";
		verifyReport = verifyReport +"<td>"+s.getElement(2)+"</td>";
		verifyReport = verifyReport +"<td>"+s.getElement(3)+"</td>";
		verifyReport = verifyReport +"<td>"+s.getElement(4)+"</td></tr>";
		// loop adds the rest, if there
		for (int i=5; i<elements.size(); i++) { 
			verifyReport = verifyReport +"<tr>";
			try {
				verifyReport = verifyReport +"<td colspan=\"3\">"+EDI997Messages.getAK9Message(Integer.parseInt(elements.get(i)))+"</td>";
			} catch (NumberFormatException e) {
				verifyReport = verifyReport +"<td colspan=\"3\">"+elements.get(i)+"</td>";
			}  
			verifyReport = verifyReport +"</tr>";
		}
		// add ending tags for table row	
		//errorReport = errorReport +"</tr></tbody>";
		verifyReport = verifyReport +"</tbody>";
		verifyReport = verifyReport +"</table>";
		
		return 0;
	}
	
	/**
	 * Write out the results of the 997 verify and add to currentFiling
	 */
	private void writeReport() {
		// TODO: Add 997 to CurrentFiling in Verify997Ack
		String fileName = ack997File.toString();
		String newFile  = "";
		int i           = fileName.lastIndexOf('.');
		try {
			String part1 = fileName.substring(0, i);
			//String part2 = fileName.substring(i);
			newFile = part1+"verify.html";
		} catch (IndexOutOfBoundsException e ) {
			 JOptionPane.showMessageDialog(MainApp.mainFrame,
		 				"<html>997 Verify Report write failed<br>"+e.getMessage(), 
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
			 				"<html>997 Verify Report saved as<br>"+newFile, 
			 				"997 Verify Report Save",
			 				JOptionPane.INFORMATION_MESSAGE);
		         }
		 
		      }
		      catch (IOException e) {
		    	  JOptionPane.showMessageDialog(MainApp.mainFrame,
			 				"<html>997 Verify Report write failed<br>"+e.getMessage(), 
			 				"Filing Report Error",
			 				JOptionPane.INFORMATION_MESSAGE);
		      }
	}
	
}  // end class
