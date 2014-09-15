/**
 * 
 */
package com.bottinifuel.edi720to;

import org.pb.x12.Context;
import org.pb.x12.Segment;
import org.pb.x12.X12Simple;

/**
 * @author laddp
 *
 */
public class EDITOtest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Context irsContext = new Context('\\', '~', '^');
		X12Simple to_reporting = new X12Simple(irsContext);
		
		Segment isa_header = new Segment(irsContext);
		isa_header.addElement("ISA");              // segment ID
		isa_header.addElement("03");               // Auth info qualifier
		isa_header.addElement("1234567890");       // Auth info
		isa_header.addElement("01");               // Security info qualifier
		isa_header.addElement("1234567890");       // Security info
		isa_header.addElement("32");               // Interchange Sender ID qualifier
		isa_header.addElement("141420673      ");  // Interchange Sender ID
		isa_header.addElement("01");               // Interchange Receiver ID qualifier
		isa_header.addElement("040539587      ");  // Interchange Receiver ID
		isa_header.addElement("120127");           // Interchange date
		isa_header.addElement("1455");             // Interchange time
		isa_header.addElement("|");                // Repetition separator
		isa_header.addElement("00403");            // Interchange control version
		isa_header.addElement("000000001");        // Control number
		isa_header.addElement("0");                // Ack request
		isa_header.addElement("T");                // Usage indicator
		isa_header.addElement("^");                // Sub-element separator
		
		to_reporting.addSegment(isa_header);

		Segment gs_header = new Segment(irsContext);
		gs_header.addElement("GS");              // segment ID
		gs_header.addElement("TF");              // Function identifier
		gs_header.addElement("xxxxxxxxxxxxxxx"); // Application senders code
		gs_header.addElement("040539587050");    // Application receivers code
		gs_header.addElement("20120127");        // Date
		gs_header.addElement("1455");            // Time
		gs_header.addElement("1");               // Group control number
		gs_header.addElement("X");               // Responsible agency code
		gs_header.addElement("004030");          // Version id code
		
		to_reporting.addSegment(gs_header);
		
		
		Segment st_header = new Segment(irsContext);
		st_header.addElement("ST");
		st_header.addElement("813"); // set type
		st_header.addElement("0001"); // set id
		st_header.addElement("0200"); // version number
		
		to_reporting.addSegment(st_header);
		
		Segment bti = new Segment(irsContext);
		bti.addElement("BTI");
		bti.addElement("T6");        // Tax filing
		bti.addElement("050");       // All Fuels Tax filing
		bti.addElement("47");        // Tax authority
		bti.addElement("040539587"); // IRS DUNS #
		bti.addElement("20120127");  // Set date
		bti.addElement("MORG");      // Name control ID
		bti.addElement("24");        // ID code qualifier
		bti.addElement("141420673"); // EIN number
		bti.addElement("49");        // State ID number
		bti.addElement("xx");        // License/permit number
		bti.addElement("48");        // IRS EFID number
		bti.addElement("xxxxxxxxx"); // 637 registration number
		bti.addElement("00");        // Purpose: 00 - original filing

		to_reporting.addSegment(bti);
		
		Segment dtm = new Segment(irsContext);
		dtm.addElement("DTM");
		dtm.addElement("20111231");
		
		to_reporting.addSegment(dtm);
		
		Segment tia = new Segment(irsContext);
		tia.addElement("TIA");
		tia.addElement("5001");
		tia.addElement("");
		tia.addElement("");
		tia.addElement("1000");
		tia.addElement("GA");
		
		to_reporting.addSegment(tia);
		
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
