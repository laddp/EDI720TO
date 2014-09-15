
package com.bottinifuel.edi720to;
/**
 * @author carlonc
 *
 * Creates an Original EDI Filing
 */
/* Change Log:
* 
*   Date         Description                                        Pgmr
*  ------------  ------------------------------------------------   -----
*  Feb 11,2013   Intial Dev...                                     carlonc 
*************************************************************************/

import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.pb.x12.Context;
import org.pb.x12.Segment;
import org.pb.x12.X12Simple;

import com.bottinifuel.edi720to.util.ContextType;

public class EDICreateOriginalFiling {
 
	private String bti13           = "";
	private String bti14           = "";
	private String ediReport       = "";
	private String businessfirst4  = "";
	private int stCount            = 0; // keeps track of the number of transaction set segments (ST) 
	private int uniqueChar         = 1;
	private Context irsContext     = ContextType.IRSCONTENT;
	private X12Simple to_reporting = new X12Simple(irsContext);
	
	// get time for use when creating unique element numbers
	private Date today = new Date();                                                
	private Format hhmmss            = new SimpleDateFormat("HHmmss");   
	private Format yyyymmddFormatter =  new SimpleDateFormat("yyyyMMdd");
	private Format yymmddFormatter   =  new SimpleDateFormat("yyMMdd");
	private Format hhmmFormatter     = new SimpleDateFormat("HHmm");
	
	private boolean bulkRecipts     = false; // bulk receipts to report flag
	private boolean nonBulkReceipts = false; // non bulk receipts to report flag 
	private boolean disburements    = false; // disbursements to report flag
		
	private Vector<String> carriersDone = new Vector<String>();
	
	private final EDI720TO MainApp; 
	/**
	 * @param args
	 */
	public EDICreateOriginalFiling(EDI720TO app) {
		MainApp = app;
				
		boolean activity = true;  
		if (MainApp.CurrentFiling.TotalReceipts.equals(BigDecimal.valueOf(0)) && 
			MainApp.CurrentFiling.TotalDisbursements.equals(BigDecimal.valueOf(0))) {
				activity = false;
		}	
		
	    try {
		   businessfirst4 = MainApp.preferences.getBusinessName().substring(0,4);
	    }
	    catch (IndexOutOfBoundsException e) {
	    	JOptionPane.showMessageDialog(MainApp.mainFrame,
					"<html>Unable to obtain the business name for the filing:<br>" + e.toString() + "<br>", 
					"Verify the business name in Preferences",
					JOptionPane.ERROR_MESSAGE);	
	    	return;
	    }
	    
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
		is = is.replace("-", "");                 // strip the dashes
		String is2 = is + "               ".substring(0, 15-is.length()); // pad to 15
		isa_header.addElement(is2);                // ISA06: add formatted Interchange Sender ID
		isa_header.addElement("01");               // ISA07: Interchange Receiver ID qualifier
		isa_header.addElement("040539587      ");  // ISA08: Interchange Receiver ID
		isa_header.addElement(yymmddFormatter.format(today));// ISA09: Interchange date, YYMMDD
		isa_header.addElement(hhmmFormatter.format(today));  // ISA10: Interchange time, HHMM format 
		isa_header.addElement("|");                // ISA11: Repetition separator
		isa_header.addElement("00403");            // ISA12: Interchange control version
		String isa13 = uniqueChar+hhmmss.format(today); 
		uniqueChar++;
		String isaIndentifier = isa13 + "000000000".substring(0, 9-isa13.length()); // pad to 9
		isa_header.addElement(isaIndentifier);     // ISA13: Unique Control number (NO, 9 in length)
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
		gs_header.addElement(MainApp.preferences.getApplicationID().replace("-", "")); // GS02: Application Sender ID without the dash  
		gs_header.addElement("040539587050");    // GS03: Application receivers code
		gs_header.addElement(yyyymmddFormatter.format(today)); // GS04: GS Create Date
		gs_header.addElement(hhmmss.format(today));  // GS05: GS Create Time
		// To create a unique Group control number, we will use the time 
		String gsIndentifier = uniqueChar+hhmmss.format(today); 
		uniqueChar++;
		gs_header.addElement(gsIndentifier);     // GS06: Unique Group control number (1-9 NO length)
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
		String stIdentifier = uniqueChar+hhmmss.format(today);
		uniqueChar++;
		st_header.addElement(stIdentifier); // ST02: Unique number to identify the data sent (4-9 chars, same value as SE02)
		st_header.addElement("0200"); // ST03: The implementation convention reference
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
		bti.addElement(yyyymmddFormatter.format(today)); // BTI05: Transaction set Create date
		// BTI06: Name control ID, first 4 chars of Businness name
		bti.addElement(businessfirst4);  
		bti.addElement("24");        // BTI07: ID code qualifier
		bti.addElement(MainApp.preferences.getEIN().replace("-", "")); // BTI08: EIN number, without dashes
		bti.addElement("49");        // BTI09: State ID number 
		bti.addElement("~");        // BTI10: License/permit number ??? not sure where this is
		bti.addElement("48");        // BTI11: IRS EFID number for terminal operator
		bti.addElement(MainApp.preferences.getId637()); // BTI12: 637 registration number  
		bti13 = "00";
		bti14 = "";
		bti.addElement(bti13);        // BTI13: Purpose: 00 = original filing 
		//bti.addElement("~");        // BTI14: Original, just add placeholder
		to_reporting.addSegment(bti);
		stCount++;
		
		// Add the end date DTM Segment
		// The Date Time, It has 2 elements, all are required 
		// Sample record
		// DTM~194~20130131
		Segment dtm = new Segment(irsContext);
		dtm.addElement("DTM");
		dtm.addElement("194"); // DTM01: Identifies next element as the period end date   
		dtm.addElement(MainApp.CurrentFiling.getYYYYMMDDEndDate());  // DTM02: Filing End date
		to_reporting.addSegment(dtm);
		stCount++;
		
		//   Add the TIA segment 
		//   To specify the tax information and/or amount and to be used for tax information 
		//   Required for original or supplemental, not for a correction.
		//   Sample record:
		//   TIA~5001~~~7940294~GA\
		Segment tia = new Segment(irsContext);
		tia.addElement("TIA");
		tia.addElement("5001");  // TIA01: Qualifies TIA004 as Total Net reported
		tia.addElement("");      // TIA02: Not used
		tia.addElement("");      // TIA03: Not Used
		tia.addElement(MainApp.getNetGallons().toString()); // TIA04: Total Net Gallons ???
		tia.addElement("GA");    // TIA05: Qualifies TIA04 as gallons reported
		to_reporting.addSegment(tia);
		stCount++;
		
		// Add N1 Segment 
		// The Name Information Segment, 2 required elements
		// Sample Record:
		// N1~L9~Morgan Fuel and Heating Co Inc
		Segment n1 = new Segment(irsContext);
		n1.addElement("N1");
		n1.addElement("L9"); // N101: Identifies N102 as the Business Name
		n1.addElement(MainApp.preferences.getBusinessName());  // N102: Business Name
		//n1.addElement("31"); // N101: Identifies N102 as the Mailing Address
		//n1.addElement(MainApp.preferences.getMailingAddr1());  // N102: Mailing address
		to_reporting.addSegment(n1);
		stCount++;
		
		// Add the N3 Segment
		// The Address Information
		// Sample Record:
		// N3~2785 W Main St
		Segment n3 = new Segment(irsContext);
		n3.addElement("N3");
		n3.addElement(MainApp.preferences.getMailingAddr1());  // N301: Mailing Address
		to_reporting.addSegment(n3);
		stCount++;
		
		// Add the N4 Segment
		// The City, State, Zip Code, and Country
		// Sample Record
		// N4~Wappingers Falls~NY~12590~USA
		Segment n4 = new Segment(irsContext);
		n4.addElement("N4");
		n4.addElement(MainApp.preferences.getCity());    // N401: The City
		n4.addElement(MainApp.preferences.getState());   // N402: The State
		n4.addElement(MainApp.preferences.getZip());     // N403: The zip code
		n4.addElement(MainApp.preferences.getCountry()); // N404: The Country Abbreviation
		to_reporting.addSegment(n4);
		stCount++;
		
		// Add the PER Segment
		// The Information Provider Contact Person
		// Sample record
		// PER~CN~Mark Caprara~TE~845-297-5580~FX~845-297-3026~EM~mcaprara@bottinifuel.com
		Segment per1 = new Segment(irsContext);
		per1.addElement("PER");
		per1.addElement("CN");                                 // PER01: Qualifies next element is General Contact Personnel
		per1.addElement(MainApp.preferences.getGeneralName()); // PER02: General Contact
		per1.addElement("TE");                                 // PER03: Qualifies next element is telphone number
		per1.addElement(MainApp.preferences.getGeneralTelephone().replace("-", "")); // PER04: Telephone
		per1.addElement("FX");                                 // PER05: Qualifies next element as a FAX
		per1.addElement(MainApp.preferences.getGeneralFax().replace("-", ""));       // PER06: The FAX
		per1.addElement("EM");                                 // PER07: Qualifies next element as an e-mail
		per1.addElement(MainApp.preferences.getGeneralEmail());// PER08: The e-mail
		to_reporting.addSegment(per1);
		stCount++;
		// repeat for EDI Coordinator
		Segment per2 = new Segment(irsContext);
		per2.addElement("PER");
		per2.addElement("EA");                                 // PER01: Qualifies next element is EDI Coordinator
		per2.addElement(MainApp.preferences.getEDIname());     // PER02: EDI Coordinator
		per2.addElement("TE");                                 // PER03: Qualifies next element is telphone number
		per2.addElement(MainApp.preferences.getEDItelephone().replace("-", "")); // PER04: Telephone
		per2.addElement("FX");                                 // PER05: Qualifies next element as a FAX
		per2.addElement(MainApp.preferences.getEDIfax().replace("-", ""));       // PER06: The FAX
		per2.addElement("EM");                                 // PER07: Qualifies next element as an e-mail
		per2.addElement(MainApp.preferences.getEDIemail());    // PER08: The e-mail
		to_reporting.addSegment(per2);
		stCount++;
		
		//********************************************
		// Beginning of the Terminal Operator Report
		//********************************************
		// Add the TFS Segment
		// The Tax Form Segment. Begins a new section of the TS-813, 
		// known as Table2 or the body
		// Record Format
		// TFS~T2~TOR~~~TC~T-14-NY-1423
		Segment tfs = new Segment(irsContext);
		tfs.addElement("TFS");
		tfs.addElement("T2");   // TFS01: Qualifies this TFS loop as a Tax Form
		tfs.addElement("TOR");  // TFS02: Qualifies this TFS loop as a Terminal Operator Report
		tfs.addElement("");     // TFS03: Not used
		tfs.addElement("");     // TFS04: Not Used
		tfs.addElement("TC");   // TFS05: Qualifies next element as a Terminal Control Number
		tfs.addElement(MainApp.preferences.getFCNcontrolNum()); // TFS06: The IRS Terminal Control Number
		to_reporting.addSegment(tfs);
		stCount++;
		
		// Add the first REF Segment
		// The Reference Segment. Two iterations of this REF are required. This first occurrence
		// notifies the IRS which taxing authorities have the right to receive the info. 
		// Sample record
		// REF~SU~IRS~~~SO^NY
		Segment tfsRef1 = new Segment(irsContext);
		tfsRef1.addElement("REF");
		tfsRef1.addElement("SU");      // REF01: Qualifies REF02 as special processing code
		// ??? can be IRS, N/A(IRS should not receive copy, or 1(no terminal activity)
		// ask Patrick if I will ever need N/A
		if (!activity)
			tfsRef1.addElement("1");   // REF02: Flags no terminal activity for this filing
		else 
			tfsRef1.addElement("IRS"); // REF02: The IRS is to receive a copy of this filing
		tfsRef1.addElement("");        // REF03: Not used 
		tfsRef1.addCompositeElement("SO", MainApp.preferences.getState()); // REF04: SO, followed by State
		to_reporting.addSegment(tfsRef1);
		stCount++;
		
		Segment tfsRef2 = new Segment(irsContext);
		// Add the second  REF Segment
		// This iteration provides a unique sequence number for each occurrence of the TFS loop
		// Sample record
		// REF~55~000000000000001
		tfsRef2.addElement("REF");
		tfsRef2.addElement("55");   // REF01:  Qualifies REF02 as a sequence number
		tfsRef2.addElement(uniqueChar+hhmmss.format(today));  // REF02: Unique Information Providers Sequence for TFS loop 
		uniqueChar++;
		to_reporting.addSegment(tfsRef2);
		stCount++;
		
		if (!activity) {
			// Add the REF for no activity, No disbursements or receipts
			Segment naRef = new Segment(irsContext);
			naRef.addElement("REF");
			naRef.addElement("BE"); // REF01: Qualifies next element as type of activity
			naRef.addElement("1");  // REF02: Activity code to indicate no activity
			to_reporting.addSegment(naRef);
			stCount++;
		}
		else {  
			// Add the DTM, when there is activity 
			// The Date Time
			// Sample record
			// DTM~184~20130131
			Segment tfsDtm = new Segment(irsContext);
			tfsDtm.addElement("DTM");
			tfsDtm.addElement("184");   // DTM01: identifies date as ending inventory
			tfsDtm.addElement(MainApp.CurrentFiling.getYYYYMMDDEndDate()); // STM02: Ending Inventory date
			to_reporting.addSegment(tfsDtm);
			stCount++;
			
			// Add the FGS, when there is activity
			// The Form Group Segment
			// Identifies the product code being reported
			// Sample Record
			// FGS~EI~PG~227\
			Segment fgs = new Segment(irsContext);
			fgs.addElement("FGS");
			fgs.addElement("EI");  // FGS01: Identifies ending Inventory
			fgs.addElement("PG");  // FGS02: Qualifies next element as a product group
			if (MainApp.CurrentFiling.selected2Product.equals("low")) // FGS03: Product code being reported
			   fgs.addElement("227"); // not sure about this yet, Appendix A only lists 227 ??? product code
			else 
			   fgs.addElement("227");	
			to_reporting.addSegment(fgs);
	        stCount++;
		}
	   
		// Add next REF Segment
		// Provides a unique sequence number for each occurrence of the FGS segment
		// Sample Record
		// REF~55~5124857382\
		Segment seqRef = new Segment(irsContext);
		seqRef.addElement("REF");  
		seqRef.addElement("55");  // REF01: Qualifies next element as a Sequence number
		seqRef.addElement(uniqueChar+hhmmss.format(today)); // REF02: Information Provider's sequence number
		uniqueChar++;
		to_reporting.addSegment(seqRef);
		stCount++;
		
		if (activity) {
			// Add the TIA segment when there is activity 
			// This TIA carries the Net Gallons of ending Inventory
			Segment tfTia = new Segment(irsContext);
			tfTia.addElement("TIA");
			tfTia.addElement("5002");  // TIA01: Net Physical inventory gallons reported
			tfTia.addElement("");      // TIA02: Not used    
			tfTia.addElement("");      // TIA03: Not used
			tfTia.addElement(MainApp.CurrentFiling.ClosingInventory.toString()); // TIA04: ending gallons
			tfTia.addElement("GA");    // TIA05: Qualifies TIA04 as gallons
			to_reporting.addSegment(tfTia);
			stCount++;
		}
		
		// Begin Schedule Details
		if (activity) {
			checkActivities(); // verifies what activity there is to report
			if (nonBulkReceipts)
			   doNonBulkReceiptsHdr();
			if (bulkRecipts)
			   doBulkReceiptsHdr();
			if (disburements)
			   doDisbursements();
		}
		
		//*******************************************************************
		// We are done with the TOR Report, now add all the closing segments
		//********************************************************************
		// Add SE segment, closes the ST segment
		// Sample record
		// SE~14~3124857382\
		Segment se = new Segment(irsContext);
		se.addElement("SE");
		se.addElement(Integer.toString(stCount)); // SE01: Number of segments between ST/SE pair
		se.addElement(stIdentifier);              // SE02: ST02 Control Number
		to_reporting.addSegment(se);
				
		// Add the GE Segment, closes GS Segment
		// Sample Record
		// GE~1~2124857382\
		List<Segment> sList = to_reporting.findSegment("ST");  // gets the ST count for next segment
		Segment ge_trailer = new Segment(irsContext);
		ge_trailer.addElement("GE");
		ge_trailer.addElement(Integer.toString(sList.size())); // GE01: Number of ST/SE pairs    
		ge_trailer.addElement(gsIndentifier);                  // GE02: same as GS06               
		to_reporting.addSegment(ge_trailer);
		
		// Add the IEA Segment, closes the ISA segment
		// Sample Record
		// IEA~1~1124857382\
		sList = to_reporting.findSegment("GS"); // gets the GS count for next segment
		Segment iea_trailer = new Segment(irsContext);
		iea_trailer.addElement("IEA");
		iea_trailer.addElement(Integer.toString(sList.size()));  // IEA01: Number GS/GE pair)
		iea_trailer.addElement(isaIndentifier);                  // IEA02: same as ISA13
		to_reporting.addSegment(iea_trailer);
		
		ediReport  = to_reporting.toString();
	}
		
	private void doBulkReceiptsHdr() {
		Segment tfs15A = new Segment(irsContext);
		tfs15A.addElement("TFS");
		tfs15A.addElement("T3");  // TFS01: Qualifies this loop as a tax schedule
		tfs15A.addElement("15A"); // TFS02: 15A=Terminal Receipts
		tfs15A.addElement("PG");  // TFS03: Qualifies next element as product group
		if (MainApp.CurrentFiling.selected2Product.equals("low")) // FGS03: Product code being reported
			   tfs15A.addElement("227"); // TFS04 product code ??? 
			else 
			   tfs15A.addElement("227");	
		tfs15A.addElement("94");  // TFS05: Qualifies next element as mode code
		tfs15A.addElement("B ");  // TFS06: B=Barge(Bulk) 
		to_reporting.addSegment(tfs15A);
		stCount++;
		
		// Add The next REF
		// Used to notify the IRS which taxing Authorities have the right to receive
		// the information
		Segment ref1 = new Segment(irsContext);
		ref1.addElement("REF");
		ref1.addElement("SU");  // REF01: Qualifies next element as special processing code
		ref1.addElement("IRS"); // REF02: IRS can receive copy of info
		ref1.addElement("");    // REF03: Not used
		// REF04: Only need to report terminal State for Bulk receipts
		ref1.addCompositeElement("S0", MainApp.preferences.getState());  
		to_reporting.addSegment(ref1);
		stCount++;
		
		// Add the next ref
		// Provides a unique number sequence number for each schedule detail TFS loop
		Segment ref2 = new Segment(irsContext);
		ref2.addElement("REF");
		ref2.addElement("55");   // REF01: Qualifies next element as sequence
		ref2.addElement(uniqueChar+hhmmss.format(today)); // REF02: Sequence number
		uniqueChar++;
		to_reporting.addSegment(ref2);
		stCount++;
		
		Vector<Carrier> carriers = MainApp.preferences.getCarrierTableV();
		for (int i=0; i<carriers.size(); i++) {
			doBulkCarrierDetails(carriers.elementAt(i));
		}
	}	
	
	private void doBulkCarrierDetails(Carrier c) {
		if (c.vessel.equalsIgnoreCase("Truck"))
			return;   // never do Truck receipts under Bulk
		else if (carriersDone.contains(c.carrier)) 
			return;   // carrier already done
		else 
			carriersDone.addElement(c.carrier);
		
		// Next N1 identifies the Carrier 
		Segment n11 = new Segment(irsContext);
		n11.addElement("N1");
		n11.addElement("CA");         // N101: Identifies Carrier
		n11.addElement(c.carrier);    // N102: Carrier Name 
		n11.addElement("24");         // N103: Qualifies next element as EIN
		n11.addElement(c.carrierEin); // N104: Carrier's EIN 
		to_reporting.addSegment(n11);
		stCount++;

		// Add the N1
		// For Bulk Receipts, Identifies the destination terminal
		Segment n12 = new Segment(irsContext);
		n12.addElement("N1");
		n12.addElement("DT");  // N101: Destination Terminal
		n12.addElement("");    // N102: Not Used
		n12.addElement("TC");  // N103: Qualifies next element as FCN
		n12.addElement(MainApp.preferences.getFCNcontrolNum());
		to_reporting.addSegment(n12);
		stCount++;
		
		Vector<Carrier> vessels = getCarrierVessels(c.carrier);
		for (int i=0; i<vessels.size(); i++) {   
		    doBulkCarrierReceiptDetails(vessels.elementAt(i));
	    }
	}
	
	private void doNonBulkReceiptsHdr() {
		// assumption, Bottini is Carrier for all Trucked NonBulk receipts
		Segment tfs = new Segment(irsContext);
		tfs.addElement("TFS");
		tfs.addElement("T3");  // TFS01: Qualifies this loop as a tax schedule
		tfs.addElement("15A"); // TFS02: 15A=Terminal Receipts
		tfs.addElement("PG");  // TFS03: Qualifies next element as product group
		if (MainApp.CurrentFiling.selected2Product.equals("low")) // FGS03: Product code being reported
			   tfs.addElement("227"); // TFS04 product code ??? 
			else 
			   tfs.addElement("227");	
		tfs.addElement("94");  // TFS05: Qualifies next element as mode code
		tfs.addElement("J ");  // TFS06: J=Truck, Non Bulk  
		to_reporting.addSegment(tfs);
		stCount++;
		
		// Add The next REF
		// Used to notify the IRS which taxing Authorities have the right to receive
		// the information
		Segment ref1 = new Segment(irsContext);
		ref1.addElement("REF");
		ref1.addElement("SU");  // REF01: Qualifies next element as special processing code
		ref1.addElement("IRS"); // REF02: IRS can receive copy of info
		ref1.addElement("");    // REF03: Not used
		// REF04: Only need to report terminal State for Bulk receipts
		ref1.addCompositeElement("S0", MainApp.preferences.getState());  
		to_reporting.addSegment(ref1);
		stCount++;
		
		// Add the next ref
		// Provides a unique number sequence number for each schedule detail TFS loop
		Segment ref2 = new Segment(irsContext);
		ref2.addElement("REF");
		ref2.addElement("55");   // REF01: Qualifies next element as sequence
		ref2.addElement(uniqueChar+hhmmss.format(today)); // REF02: Sequence number
		uniqueChar++;
		to_reporting.addSegment(ref2);
		stCount++;
		
		// Add the N1
		// For Bulk Receipts, Identifies the Destination Terminal
		Segment n11 = new Segment(irsContext);
		n11.addElement("N1");
		n11.addElement("DT");            // N101: Destination Terminal
		n11.addElement("");              // N102: Not Used
		n11.addElement("TC");            // N103: Qualifies next element as FCN
		n11.addElement(MainApp.preferences.getFCNcontrolNum());
		to_reporting.addSegment(n11);
		stCount++;
		
		// Next N1 identifies the Carrier 
		// Assumption, Bottini is the carrier for all non bulk receipts
		Segment n12 = new Segment(irsContext);
		n12.addElement("N1");
		n12.addElement("CA");               // N101: Identifies Carrier
		n12.addElement(businessfirst4);     // N102: Carrier Name 
		n12.addElement("24");               // N103: Qualifies next element as EIN
		n12.addElement(MainApp.preferences.getEIN()); // N104: Carrier's EIN  
		to_reporting.addSegment(n12);
		stCount++;
		
		// This FGS loop begins individual shipments within the TFS loop. It is repeated 
		// when one of the following changes:
		// Bill of Lading (Document) Number
		// Bill of Lading Date (Date Shipped)
		// Vessel Official Number
		// Gallons 
		doNonBulkReceiptDetails("Truck"); 
	}
	
	private void doDisbursements() {
		Segment tfs = new Segment(irsContext);
		tfs.addElement("TFS");
		tfs.addElement("T3");  // TFS01: Qualifies this loop as a tax schedule
		tfs.addElement("15B"); // TFS02: 15B=Terminal Disbursements
		tfs.addElement("PG");  // TFS03: Qualifies next element as product group
		if (MainApp.CurrentFiling.selected2Product.equals("low")) // FGS03: Product code being reported
			   tfs.addElement("227"); // TFS04 product code ??? 
			else 
			   tfs.addElement("227");	
		tfs.addElement("94");  // TFS05: Qualifies next element as mode code
		tfs.addElement("RS");  // TFS06: RS=Dispensed at FCN for end user 
		to_reporting.addSegment(tfs);
		stCount++;
		
		// Add The next REF
		// Used to notify the IRS which taxing Authorities have the right to receive
		// the information
		Segment ref1 = new Segment(irsContext);
		ref1.addElement("REF");
		ref1.addElement("SU");  // REF01: Qualifies next element as special processing code
		ref1.addElement("IRS"); // REF02: IRS can receive copy of info
		ref1.addElement("");    // REF03: Not used
		// REF04: Only need to report terminal State for Bulk receipts
		ref1.addCompositeElement("S0", MainApp.preferences.getState());  
		to_reporting.addSegment(ref1);
		stCount++;
		
		// Add the next ref
		// Provides a unique number sequence number for each schedule detail TFS loop
		Segment ref2 = new Segment(irsContext);
		ref2.addElement("REF");
		ref2.addElement("55");   // REF01: Qualifies next element as sequence
		ref2.addElement(uniqueChar+hhmmss.format(today)); // REF02: Sequence number
		uniqueChar++;
		to_reporting.addSegment(ref2);
		stCount++;
		
		// Add the N1
		// For Bulk Receipts, Identifies the Origin Terminal
		Segment n11 = new Segment(irsContext);
		n11.addElement("N1");
		n11.addElement("OT");  // N101: Origin Terminal
		n11.addElement("");    // N102: Not Used
		n11.addElement("TC");  // N103: Qualifies next element as FCN
		n11.addElement(MainApp.preferences.getFCNcontrolNum());
		to_reporting.addSegment(n11);
		stCount++;
		
		// Next N2 identifies the Carrier 
		Segment n12 = new Segment(irsContext);
		n12.addElement("N1");
		n12.addElement("CA");               // N101: Identifies Carrier
		n12.addElement(businessfirst4);     // N102: Carrier Name, Bottini 
		n12.addElement("24");               // N103: Qualifies next element as EIN
		n12.addElement(MainApp.preferences.getEIN()); // N104: Carrier's EIN 
		to_reporting.addSegment(n12);
		stCount++;
		
		// Next N3 identifies the Position Holder 
		Segment n13 = new Segment(irsContext);
		n13.addElement("N1");
		n13.addElement("ON");               // N101: Identifies Position Holder
		n13.addElement(businessfirst4);     // N102: Postion Holder, Bottini  
		n13.addElement("24");               // N103: Qualifies next element as EIN
		n13.addElement(MainApp.preferences.getEIN()); // N104: Position Holder's EIN  
		to_reporting.addSegment(n13);
		stCount++;
		
		// Next N4 identifies the Ship To Location
		// Assumption NY is always Ship to
		Segment n14 = new Segment(irsContext);
		n14.addElement("N1");
		n14.addElement("ST");               // N101: Ship To
		n14.addElement(MainApp.preferences.getState());  // N102: Carrier Name ??? Bottini ??? 
		to_reporting.addSegment(n14);
		stCount++;
		
		// This FGS loop begins individual shipments within the TFS loop. It is repeated 
		// when one of the following changes:
		// Bill of Lading (Document) Number
		// Bill of Lading Date (Date Shipped)
		// Vessel Official Number
		// Gallons 
		int size = MainApp.CurrentFiling.IncludeDisbursements.size(); 
		for (int i=0; i<size; i++) {
			if (MainApp.CurrentFiling.IncludeDisbursements.elementAt(i)) {
				Transaction t = MainApp.CurrentFiling.Disbursements.elementAt(i); 
				// Identifies the document number being reported 
				Segment fgs = new Segment(irsContext);
				fgs.addElement("FGS");
				fgs.addElement("D");       // FGS01: Schedule of detail
				fgs.addElement("BM");      // FGS02: Qualifies next element as Document Number
				fgs.addElement(t.RefNum.trim());  // FGS03: The Document number
				to_reporting.addSegment(fgs);
				stCount++;
				
				// Provides unique sequence
				Segment ref = new Segment(irsContext);
				ref.addElement("REF");
				ref.addElement("55");    // REF01: Qualifies next element as sequence
				ref.addElement(uniqueChar+hhmmss.format(today)); // REF02: Sequence number
				uniqueChar++;
				to_reporting.addSegment(ref);
				stCount++;
				
				Segment dtm = new Segment(irsContext);
				dtm.addElement("DTM");
				dtm.addElement("095");       // DTM01: identifies document date
				dtm.addElement(yyyymmddFormatter.format(t.TransDate)); // DTM02: Document Date
				to_reporting.addSegment(dtm);
				stCount++;
				
				// This TIA carries the Gallons of the transaction
				Segment tia = new Segment(irsContext);
				tia.addElement("TIA");
				tia.addElement("5005");  // TIA01: Gallons reported
				tia.addElement("");      // TIA02: Not used    
				tia.addElement("");      // TIA03: Not used
				tia.addElement(t.AffectOnInventory.abs().toString()); // TIA04: ending gallons
				tia.addElement("GA");    // TIA05: Qualifies TIA04 as gallons
				to_reporting.addSegment(tia);
				stCount++;
			}
		}  
	}
	
	/**
	 * Creates the detail for all bulk receipts
	 * @param c
	 *//*
	private void doDetailForBulkReceiptsNEW(String carrier) {
		int size = MainApp.CurrentFiling.IncludeReceipts.size(); 
		//String masterVessel = c.vessel;
		String vessel = "";
		for (int i=0; i<size; i++) {
			vessel = MainApp.CurrentFiling.ReceiptsCarrier.elementAt(i).trim();
			//System.out.println("OK vessel="+vessel);
			//System.out.println("MainApp.CurrentFiling.IncludeReceipts.elementAt("+i+")="+MainApp.CurrentFiling.IncludeReceipts.elementAt(i));
			if (MainApp.CurrentFiling.IncludeReceipts.elementAt(i) && getCarrierForVessel(vessel).carrier.trim().equalsIgnoreCase(carrier)) { 
				Transaction t = MainApp.CurrentFiling.Receipts.elementAt(i); 
				// Identifes the document number being reported 
				Segment fgs = new Segment(irsContext);
				fgs.addElement("FGS");
				fgs.addElement("D");              // FGS01: Schedule of detail
				fgs.addElement("BM");             // FGS02: Qualifies next element as Document Number
				fgs.addElement(t.RefNum.trim());  // FGS03: The Document number
				to_reporting.addSegment(fgs);
				stCount++;
				
				// Provides unique sequence
				Segment ref = new Segment(irsContext);
				ref.addElement("REF");
				ref.addElement("55");    // REF01: Qualifies next element as sequence
				ref.addElement(uniqueChar+hhmmss.format(today)); // REF02: Sequence number
				uniqueChar++;
				to_reporting.addSegment(ref);
				stCount++;
				
				Segment dtm = new Segment(irsContext);
				dtm.addElement("DTM");
				dtm.addElement("095");   // DTM01: identifies document date
				dtm.addElement(yyyymmddFormatter.format(t.TransDate)); // DTM02: Document Date
				to_reporting.addSegment(dtm);
				stCount++;
				
				Segment n1 = new Segment(irsContext);
				n1.addElement("N1");
				n1.addElement("FV");        // N101: Identifies N102 as vessel
				n1.addElement(c.vessel);    // N102: Vessel
				n1.addElement(c.vesselEin); // N103: Vessel Official Number
				to_reporting.addSegment(n1);
				stCount++;
												
				// This TIA carries the Gallons of the transaction
				Segment tia = new Segment(irsContext);
				tia.addElement("TIA");
				tia.addElement("5005");  // TIA01: Gallons reported
				tia.addElement("");      // TIA02: Not used    
				tia.addElement("");      // TIA03: Not used
				tia.addElement(t.AffectOnInventory.abs().toString()); // TIA04: ending gallons
				tia.addElement("GA");    // TIA05: Qualifies TIA04 as gallons
				to_reporting.addSegment(tia);
				stCount++;
			}
		}  // end loop  
	}*/
	
	/**
	 * Creates the detail for all bulk receipts
	 * @param c
	 */
	private void doBulkCarrierReceiptDetails(Carrier c) {
		int size = MainApp.CurrentFiling.IncludeReceipts.size(); 
		String masterVessel = c.vessel;
		String vessel = "";
		for (int i=0; i<size; i++) {
			vessel = MainApp.CurrentFiling.ReceiptsCarrier.elementAt(i).trim();
			//System.out.println("OK vessel="+vessel);
			//System.out.println("MainApp.CurrentFiling.IncludeReceipts.elementAt("+i+")="+MainApp.CurrentFiling.IncludeReceipts.elementAt(i));
			if (MainApp.CurrentFiling.IncludeReceipts.elementAt(i) && vessel.equalsIgnoreCase(masterVessel.trim())) { 
				Transaction t = MainApp.CurrentFiling.Receipts.elementAt(i); 
				// Identifes the document number being reported 
				Segment fgs = new Segment(irsContext);
				fgs.addElement("FGS");
				fgs.addElement("D");              // FGS01: Schedule of detail
				fgs.addElement("BM");             // FGS02: Qualifies next element as Document Number
				fgs.addElement(t.RefNum.trim());  // FGS03: The Document number
				to_reporting.addSegment(fgs);
				stCount++;
				
				// Provides unique sequence
				Segment ref = new Segment(irsContext);
				ref.addElement("REF");
				ref.addElement("55");    // REF01: Qualifies next element as sequence
				ref.addElement(uniqueChar+hhmmss.format(today)); // REF02: Sequence number
				uniqueChar++;
				to_reporting.addSegment(ref);
				stCount++;
				
				Segment dtm = new Segment(irsContext);
				dtm.addElement("DTM");
				dtm.addElement("095");   // DTM01: identifies document date
				dtm.addElement(yyyymmddFormatter.format(t.TransDate)); // DTM02: Document Date
				to_reporting.addSegment(dtm);
				stCount++;
				
				Segment n1 = new Segment(irsContext);
				n1.addElement("N1");
				n1.addElement("FV");        // N101: Identifies N102 as vessel
				n1.addElement(c.vessel);    // N102: Vessel
				n1.addElement(c.vesselEin); // N103: Vessel Official Number
				to_reporting.addSegment(n1);
				stCount++;
												
				// This TIA carries the Gallons of the transaction
				Segment tia = new Segment(irsContext);
				tia.addElement("TIA");
				tia.addElement("5005");  // TIA01: Gallons reported
				tia.addElement("");      // TIA02: Not used    
				tia.addElement("");      // TIA03: Not used
				tia.addElement(t.AffectOnInventory.abs().toString()); // TIA04: ending gallons
				tia.addElement("GA");    // TIA05: Qualifies TIA04 as gallons
				to_reporting.addSegment(tia);
				stCount++;
			}
		}  // end loop  
	}
	
	/**
	 * Creates the details for all non-bulk receipts
	 * @param masterVessel
	 */
	private void doNonBulkReceiptDetails(String masterVessel) {
		int size = MainApp.CurrentFiling.IncludeReceipts.size(); 
		String vessel = "";
		for (int i=0; i<size; i++) {
			vessel = MainApp.CurrentFiling.ReceiptsCarrier.elementAt(i).trim();
			//System.out.println("OK vessel="+vessel);
			//System.out.println("MainApp.CurrentFiling.IncludeReceipts.elementAt("+i+")="+MainApp.CurrentFiling.IncludeReceipts.elementAt(i));
			if (MainApp.CurrentFiling.IncludeReceipts.elementAt(i) && vessel.equalsIgnoreCase(masterVessel.trim())) { 
				Transaction t = MainApp.CurrentFiling.Receipts.elementAt(i); 
				// Identifes the document number being reported 
				Segment fgs = new Segment(irsContext);
				fgs.addElement("FGS");
				fgs.addElement("D");              // FGS01: Schedule of detail
				fgs.addElement("BM");             // FGS02: Qualifies next element as Document Number
				fgs.addElement(t.RefNum.trim());  // FGS03: The Document number
				to_reporting.addSegment(fgs);
				stCount++;
				
				// Provides unique sequence
				Segment ref = new Segment(irsContext);
				ref.addElement("REF");
				ref.addElement("55");    // REF01: Qualifies next element as sequence
				ref.addElement(uniqueChar+hhmmss.format(today)); // REF02: Sequence number
				uniqueChar++;
				to_reporting.addSegment(ref);
				stCount++;
				
				Segment dtm = new Segment(irsContext);
				dtm.addElement("DTM");
				dtm.addElement("095");   // DTM01: identifies document date
				dtm.addElement(yyyymmddFormatter.format(t.TransDate)); // DTM02: Document Date
				to_reporting.addSegment(dtm);
				stCount++;
				
				// This TIA carries the Gallons of the transaction
				Segment tia = new Segment(irsContext);
				tia.addElement("TIA");
				tia.addElement("5005");  // TIA01: Gallons reported
				tia.addElement("");      // TIA02: Not used    
				tia.addElement("");      // TIA03: Not used
				tia.addElement(t.AffectOnInventory.abs().toString()); // TIA04: ending gallons
				tia.addElement("GA");    // TIA05: Qualifies TIA04 as gallons
				to_reporting.addSegment(tia);
				stCount++;
			}
		}  // end loop  
	}
	
	/**
	 * This checks to see what type of activity there is to report. It sets the boolean
	 * values nonBulkReceipts, bulkRecipts, disbursements 
	 */
	private void checkActivities() {
		// check receipts for activity
		int size = MainApp.CurrentFiling.IncludeReceipts.size(); 
		String vessel = "";
		for (int i=0; i<size; i++) {
			vessel = MainApp.CurrentFiling.ReceiptsCarrier.elementAt(i).trim();
		    if (vessel.equalsIgnoreCase("Truck")) {
		    	nonBulkReceipts=true;
		        break;
		    }
		    else {
		    	bulkRecipts=true; 
		    }	
		} 
		
		// check disbursements for activity 
		size = MainApp.CurrentFiling.IncludeDisbursements.size(); 
		for (int i=0; i<size; i++) {
			if (MainApp.CurrentFiling.IncludeDisbursements.elementAt(i)) {
				disburements=true;
				break;
			}
		}	
	}
	
	/**
	 * Returns all of the vessels for a carrier 
	 * @param carrier to search
	 * @return 
	 */
	private Vector<Carrier> getCarrierVessels(String masterCarrier) {
		Vector<Carrier> carriers = MainApp.preferences.getCarrierTableV();
		Vector<Carrier> returnV = new Vector<Carrier>();
    	for (int i=0; i<carriers.size(); i++) {
			if (carriers.elementAt(i).carrier.equals(masterCarrier)) {
				returnV.addElement(carriers.elementAt(i)); 
			}
		}
		return returnV;
	}
	
	/**
	 * Returns the value of the BTI13 element
	 * @return
	 */
	public String getBti13() {
		return bti13;
	}
	
	/**
	 * Returns the value of the BTI14 element
	 * @return
	 */
	public String getBti14() {
	    return bti14;
	}
	
	public String getEdiReport() {
		return ediReport;
	}
		

}
