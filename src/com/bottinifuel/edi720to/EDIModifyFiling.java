/**
 * 
 */
package com.bottinifuel.edi720to;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.pb.x12.Context;
import org.pb.x12.Segment;
import org.pb.x12.X12Simple;

import com.bottinifuel.edi720to.util.FilingType;


/**
 * @author carlonc
 *
 *  Need to change this to parse the existing filing and modifiy only the 
 *  fields needed
 */
/* Change Log:
* 
*   Date         Description                                        Pgmr
*  ------------  ------------------------------------------------   -----
*  Feb 11,2013   Intial Dev...                                      carlonc 
*************************************************************************/
public class EDIModifyFiling {
 
	private final EDI720TO MainApp; 
	/**
	 * @param args
	 */
	public EDIModifyFiling(EDI720TO app, int filingType) {
		MainApp = app;
		
		Context irsContext = new Context('\\', '~', '^');
		X12Simple to_reporting = new X12Simple(irsContext);
		
		// get time for use when creating unique element numbers ??? don't use this if existing filing
		Date today = new Date();                                                
		Format formatter = new SimpleDateFormat("HHmmssSS");   
		
		// Add The ISA Segment
		// The Interchange Header. Has 16 elements and all are required. This segment
		// is paired with the Interchange Control Trailer (IEA).
		// Sample record:
		// ISA~03~I081415333~01~8452975580~32~141420673          ~01~040539587      ~130101~1430~|~00403~ISA143237480~0~T~^
		Segment isa_header = new Segment(irsContext);
		isa_header.addElement("ISA");              // segment ID
		isa_header.addElement("03");               // ISA01: Auth info qualifier
		isa_header.addElement(MainApp.preferences.getAuthCode());     // ISA02: Auth info
		isa_header.addElement("01");               // ISA03: Security info qualifier
		isa_header.addElement(MainApp.preferences.getSecurityCode()); // ISA04: Security info
		isa_header.addElement("32");               // ISA05: Interchange Sender ID qualifier
		// Interchange Sender is 15 characters in length, without any dashes
		String is = MainApp.preferences.getInterchageID();
		is = is.replace("-", "");                 // strip the dash
		String is2 = is + "               ".substring(0, is.length()+1); // pad to 15
		isa_header.addElement(is2);                // ISA06: add formatted Interchange Sender ID
		isa_header.addElement("01");               // ISA07: Interchange Receiver ID qualifier
		isa_header.addElement("040539587      ");  // ISA08: Interchange Receiver ID
		isa_header.addElement(MainApp.CurrentFiling.getYYMMDDStartDate()); // ISA09: Interchange date, YYMMDD
		isa_header.addElement(MainApp.CurrentFiling.getHHMMStartTime());   // ISA10: Interchange time, HHMM format 
		isa_header.addElement("|");                // ISA11: Repetition separator
		isa_header.addElement("00403");            // ISA12: Interchange control version
		isa_header.addElement("ISA"+formatter.format(today));  // ISA13: Unique Control number 
		isa_header.addElement("0");                // ISA14: Ack request
		isa_header.addElement(MainApp.preferences.getTestMode()); // ISA15: Usage indicator, Test or Production
		isa_header.addElement("^");                // ISA16: Sub-element separator
		to_reporting.addSegment(isa_header);
        
		//  Add GS Segment
		//  The Functional Group Header, 8 elements, all required. This segment is paired the
		//  Funtion Group Header (GE) segment .
		//  Sample record:
		//  GS~TF~Sender ID~Receiver ID~20100523~0931~1101~X~004030\
		Segment gs_header = new Segment(irsContext);
		gs_header.addElement("GS");              // segment ID
		gs_header.addElement("TF");              // GS01: Function identifier
		// To create a unique senders code, we will use the time
		gs_header.addElement("GS02"+formatter.format(today)); // GS02: Unique application senders code
		gs_header.addElement("040539587050");   // GS03: Application receivers code
		gs_header.addElement(MainApp.CurrentFiling.getYYYYMMDDStartDate()); // GS04: Use the same date as ISA09 but YYYYMMDD format
		gs_header.addElement(MainApp.CurrentFiling.getHHMMStartTime());     // GS05: Use same time as ISA10
		// To create a unique Group control number, we will use the time 
		gs_header.addElement("GS06"+formatter.format(today)); // GS06: Unique Group control number
		gs_header.addElement("X");               // GS07: Responsible agency code
		gs_header.addElement("004030");          // GS08: Version id code
		to_reporting.addSegment(gs_header);
		
		//   Add the ST Segment
		//   The Start of the Transaction set. It has three segments, first two are required.
		//   Sample record:
		//   ST~813~1234~0200\
		Segment st_header = new Segment(irsContext);
		st_header.addElement("ST");
		st_header.addElement("813");  // ST01: Flag for start of 813 Transaction set
		st_header.addElement("ST02"+formatter.format(today)); // ST02: Unique number to identify the data sent 
		st_header.addElement("0200"); // ST03: version number ??? 
		to_reporting.addSegment(st_header);
		
		// Add the BTI Segment
		// Beginning Tax Information, It has 14 elements, not all are required 
		// Sample record:
		// BTI~T6~050~47~040539587~20130101~Morg~24~141420673~49~xx~48~01-NY-2006-001976~00~
		Segment bti = new Segment(irsContext);
		bti.addElement("BTI");
		bti.addElement("T6");        // BTI01: Tax filing
		bti.addElement("050");       // BTI02: All Fuels Tax filing
		bti.addElement("47");        // BTI03: Tax authority
		bti.addElement("040539587"); // BTI04: IRS DUNS #
		bti.addElement(MainApp.CurrentFiling.getYYYYMMDDStartDate()); // BTI05: Use the same date as ISA09 but YYYYMMDD format
		// BTI06: Name control ID, first 4 chars of Businness name
		bti.addElement(MainApp.preferences.getBusinessName().substring(0,4));  
		bti.addElement("24");        // BTI07: ID code qualifier
		bti.addElement(MainApp.preferences.getEIN().replace("-", "")); // BTI08: EIN number, without dashes
		bti.addElement("49");        // BTI09: State ID number 
		bti.addElement("xx");        // BTI10: License/permit number ??? not sure where this is
		bti.addElement("48");        // BTI11: IRS EFID number for terminal operator
		bti.addElement(MainApp.preferences.getId637()); // BTI12: 637 registration number  
		if ((filingType==FilingType.ORIGINAL) || (filingType==FilingType.REJECTED)) {
		   bti.addElement("00");        // BTI13: Purpose: 00 = original filing or file rejected
		   bti.addElement("~");         // BTI14: If original, just add placeholder
		}
		else { 
			bti.addElement("~");       // BTI13: placeholder
			if (filingType==FilingType.REPLACEMENT)
				bti.addElement("6R");   // BTI14: 
			else if (filingType==FilingType.SUPPLEMENTAL)
				bti.addElement("6S");   // BTI14: 
			else if (filingType==FilingType.CORRECTED)
				bti.addElement("CO");   // BTI14: 
		}
        // BTI14 is coded when not an original filing, in this case BTI13 will be ~
		to_reporting.addSegment(bti);
		
		// Add the end date DTM Segment
		// The Date Time, It has 2 elements, all are required 
		// Sample record
		// DTM~194~20130131
		Segment dtm = new Segment(irsContext);
		dtm.addElement("DTM");
		dtm.addElement("194"); // DTM01: Identifies next element as the period end date   
		dtm.addElement(MainApp.CurrentFiling.getYYYYMMDDEndDate());  // DTM02: Filing End date
		to_reporting.addSegment(dtm);
		
		//   Add the TIA segment 
		//   To specify the tax information and/or amount and to be used for tax information 
		//   Required for original or supplemental, not for a correction.
		//   Sample record:
		//   TIA~5001~~~7940294~GA\
		Segment tia = new Segment(irsContext);
		tia.addElement("TIA");
		tia.addElement("5001");  // TIA01: Qualifies TIA004 as Total Net reported
		tia.addElement("~");     // TIA02: Not used
		tia.addElement("~");     // TIA03: Not Used
		tia.addElement(MainApp.CurrentFiling.TotalReceipts.toString());  // TIA04: Total Net Gallons ???
		tia.addElement("GA");    // TIA05: Qualifies TIA04 as gallons reported
		to_reporting.addSegment(tia);
		
		// Add REF segment if needed
		// Add add when submitting 151 rejects, replacement, or supplemental filings 
		if ((filingType==FilingType.REJECTED) || (filingType==FilingType.REPLACEMENT)|| (filingType==FilingType.SUPPLEMENTAL)) {
		    Segment ref = new Segment(irsContext);
		    ref.addElement("REF");
		    ref.addElement("FJ");  // REF01: Qualifies REF02 as the Original Transaction Set Control Number (ST02)
		    ref.addElement("ST02"+formatter.format(today)); // same as ST02
		    to_reporting.addSegment(ref);
		}
		
		///  
		
		
		// Add N1 Segment 
		// The Name Information Segment, 2 required elements
		Segment n1 = new Segment(irsContext);
		n1.addElement("N1");
		n1.addElement("L9"); // N101: Identifies N102 as the Information Provider
		//n1.addElement(MainApp.preferences.)
		
		
		Segment ge_trailer = new Segment(irsContext);
		ge_trailer.addElement("GE");
		ge_trailer.addElement("1");
		ge_trailer.addElement("1");
		
		to_reporting.addSegment(ge_trailer);
		
		Segment iea_trailer = new Segment(irsContext);
		iea_trailer.addElement("IEA");
		iea_trailer.addElement("1");
		iea_trailer.addElement("000000001");
		
		to_reporting.addSegment(iea_trailer);
		
		String testString = to_reporting.toString();
		System.out.println(testString);
	}

}
