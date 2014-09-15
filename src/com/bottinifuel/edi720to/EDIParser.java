package com.bottinifuel.edi720to;

/**************************************************************************
*
*   File name: EDIParser
*   Parses an EDI report
* 	 
* @author carlonc
* 
*************************************************************************/
/* Change Log:
* 
*   Date         Description                                        Pgmr
*  ------------  ------------------------------------------------   -----
*  Feb 28,2013   Intial Dev...                                     carlonc 
*************************************************************************/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.pb.x12.Context;
import org.pb.x12.Parser;
import org.pb.x12.Segment;
import org.pb.x12.X12Simple;
import org.pb.x12.X12SimpleParser;

import com.bottinifuel.edi720to.util.ContextType;

public class EDIParser {
	
	private final boolean DEBUG = false;
	
	private Context   context = ContextType.IRSCONTENT;
	private String    x12Data = "";
	private X12Simple x12; // = new X12Simple(context);
	private Parser    parser  = new X12SimpleParser();
	
	private int rc = 0;
    
    private final EDI720TO MainApp; 
	
    /**
     * Constructor that takes an EDI filing for the parsing
     * @param The Main application 
     * @param The file to parser
     */
	public EDIParser(EDI720TO app, File file) {
		MainApp=app;
		
		try {
	        BufferedReader in = new BufferedReader(new FileReader(file));
	        x12Data=in.readLine();
	        in.close();
	    }  catch (IOException e) {
	    	rc = 1; 
	    	JOptionPane.showMessageDialog(MainApp.mainFrame,
	 				"<html>Verify incomplete, unable to open file "+file.toString()+"<br>"+e, 
	 				"File Error",
	 				JOptionPane.ERROR_MESSAGE);
	    }
		if (rc==0)
		   setSegments();
	}
	
	/**
	 * Constuctor that takes the EDI filing as a String
	 * @param The Main application 
	 * @param The EDI filing as a string
	 */
	public EDIParser(EDI720TO app, String fileName) {
		MainApp=app;
		x12Data = fileName;
		setSegments();
	}	
	
	private void setSegments() {
		// In order to get the parser to work, I had to change the 
	    // Segment separator since it errors with
	    // java.util.regex.PatternSyntaxException
	    try {
	       x12Data = x12Data.replace(context.getSegmentSeparator(), '#');
	       x12 = (X12Simple) parser.parse(x12Data);
	       if (DEBUG) {
	    	   System.out.println("Starting parse of EDI string: "+x12Data);
		       for (Segment s : x12) {
		    	    System.out.println(s);
		       }
	       }    
	    } catch (Exception e1) {
	    	rc = 1;
	    	JOptionPane.showMessageDialog(MainApp.mainFrame,
	 				"<html>Verify incomplete, unable to parse the filing segments<br>"+e1, 
	 				"Error Parseing report",
	 				JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	/**
	 * Returns a segment or a null if not found
	 * @param The segment name to search
	 * @return The found segment
	 */
	public Segment getSegment(String segmentName) {
		Segment segment = null;
		 try {
		       for (Segment s : x12) {
		    	    if (s.getElement(0).equals(segmentName))
		      	       segment = s;
		       }
		 } catch (Exception e1) {
		    	rc = 1;
		    	JOptionPane.showMessageDialog(MainApp.mainFrame,
		 				"<html>Verify incomplete, error obtaining segment "+segmentName+"<br>"+e1, 
		 				"Segment Error",
		 				JOptionPane.ERROR_MESSAGE);
		    	return null;
		}
		return segment;
	}
	
	/**
	 * Returns a Vector of segments or a null if not found
	 * @param The segment name to search
	 * @return The found segments in a Vector
	 */
	public Vector<Segment> getSegments(String segmentName) {
		Vector<Segment> segV = new Vector<Segment>();
		 try {
		       for (Segment s : x12) {
		    	    if (s.getElement(0).equals(segmentName))
		      	       segV.add(s);
		       }
		 } catch (Exception e1) {
		    	rc = 1;
		    	JOptionPane.showMessageDialog(MainApp.mainFrame,
		 				"<html>Verify incomplete, error obtaining segment "+segmentName+"<br>"+e1, 
		 				"Segment Error",
		 				JOptionPane.ERROR_MESSAGE);
		}
		return segV;
	}
	
	/**
	 * Get the RC of the last request
	 * @return
	 */
	public int getLastRC() {
		return rc;
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		//String fileName = "C:\\Users\\carlonc\\mystuff\\ediFilings\\MorgA_997.edi"; 
		String fileName = "C:\\Users\\carlonc\\mystuff\\ediFilings\\Morg_T-14-NY-1423_813_012013_T_00.edi";
		new EDIParser(new EDI720TO(), new File(fileName));
	}
}
