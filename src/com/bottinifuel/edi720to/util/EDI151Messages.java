package com.bottinifuel.edi720to.util;

/**************************************************************************
*
*   File name: EDI151Messages
*   List of all the 151 Acknowledgement messages 
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

public class EDI151Messages {
	
	/**
	 * Returns the description for a PBI01 primary element error code 
	 * 
	 * @param the primary message number reported in PBI01 
	 * @return the Description in a string 
	 */
	public static String getPBI01PrimaryDescription(int message) {
   		switch (message)
		{
   		case 1001:
   			return "Tax Information Code";
   		case  1003:
   			return "Fixed Format Code";
   		case 1004:
   			return "Quantity";
   		case 1005:
   			return "Unit of Measure";
   		case 2017:
   			return "Permit Qualifier Code";
   		case 2018:
   			return "Transaction Purpose Code";
   		case 2019:
   			return "Transaction Type Code";
   		case 2020:
   			return "Reference ID Qualifier";
   		case 2021:
   			return "Reference ID";
   		case 2022:
   			return "Reference ID Qualifier";
   		case 2023:
   			return "Reference ID";
   		case 2024:
   			return "ID Code Qualifier";
   		case 2025:
   			return "ID Code";
   		case 2026:
   			return "Reference ID Qualifier";
   		case 2027:
   			return "Reference ID";
   		case 2028:
   			return "Reference ID";
   		case 2029:
   			return "Reference ID";
   		case 2030:
   			return "Reference ID";
   		case  2031:
   			return "Reference ID";
   		case  2032:
   			return "Reference ID";
   		case  2033:
   			return "Assigned ID";
   		case  2034:
   			return "Reference ID Qualifier";
   		case  2035:
   			return "Reference ID";
   		case  2036:
   			return "ID Code Qualifier";
   		case  2037:
   			return "ID Code";
   		case  4007:
   			return "Date";
   		case  9999:
   			return "Out of Balance";
   		case  5001:
   			return "Invalid ST/SE";
   		case  6001:
   			return "Entity ID Code";
   		case  6002:
   			return "Information Provider Name";
   		case 6003:
   			return "Identification Code Qualifier";
   		case  6004:
   			return "Identification Code";
   		case  6005:
   			return "Contact Function Code";
   		case  6006:
   			return "Contact Name";
   		case  6007:
   			return "Telephone Number Qual";
   		case  6008:
   			return "Telephone Number";
   		case  6009:
   			return "Fax Number Qualifier";
   		case  6010:
   			return "Fax Number";
   		case  6011:
   			return "E-mail Qualifier";
   		case  6012:
   			return "E-mail Address";
   		case  6013:
   			return "Address Information";
   		case  6014:
   			return "City";
   		case  6015:
   			return "State or Province";
   		case  6016:
   			return "Zip Code";
   		case  6017:
   			return "Country";
   		case  6888:
   			return "Invalid use of Foreign Flag code";
   		case  6999:
   			return "Invalid Non-bulk Carrier";
   		default:
			return "";	
        }
	}
	
	/**
	 * Returns the Element ID for a PBI01 primary element error code 
	 * 
	 * @param the primary message number reported in PBI01 
	 * @return the Element ID in a string 
	 */
	public static String getPBI01PrimaryElementID(int message) {
   		switch (message)
		{
   		case 1001:
   			return "TIA01";
   		case  1003:
   			return "TIA03";
   		case 1004:
   			return "TIA04";
   		case 1005:
   			return "TIA05";
   		case 2017:
   			return "";   // ID does not exist
   		case 2018:
   			return "BTI13";
   		case 2019:
   			return "BTI14";
   		case 2020:
   			return "TFS01";
   		case 2021:
   			return "TFS02";
   		case 2022:
   			return "TFS03";
   		case 2023:
   			return "TFS04";
   		case 2024:
   			return "TFS05";
   		case 2025:
   			return "TFS06";
   		case 2026:
   			return "REF01";
   		case 2027:
   			return "REF02";
   		case 2028:
   			return "REF03";
   		case 2029:
   			return "REF04_C4001";
   		case 2030:
   			return "REF04_C4002";
   		case  2031:
   			return "REF04_C4003";
   		case  2032:
   			return "REF04_C4004";
   		case  2033:
   			return "FGS01";
   		case  2034:
   			return "FGS02";
   		case  2035:
   			return "FGS03";
   		case  2036:
   			return "BTI03";
   		case  2037:
   			return "BTI12";
   		case  4007:
   			return "DTM02";
   		case  9999:
   			return "";   // ID does not exist
   		case  5001:
   			return "";   // ID does not exist
   		case  6001:
   			return "N101";
   		case  6002:
   			return "N102";
   		case 6003:
   			return "N103";
   		case  6004:
   			return "N104";
   		case  6005:
   			return "PER01";
   		case  6006:
   			return "PER02";
   		case  6007:
   			return "PER03";
   		case  6008:
   			return "PER04";
   		case  6009:
   			return "PER05";
   		case  6010:
   			return "PER06";
   		case  6011:
   			return "PER07";
   		case  6012:
   			return "PER08";
   		case  6013:
   			return "N301";
   		case  6014:
   			return "N401";
   		case  6015:
   			return "N402";
   		case  6016:
   			return "N403";
   		case  6017:
   			return "N404";
   		case  6888:
   			return "N104";
   		case  6999:
   			return "N104";
   		default:
			return "";	
        }
	}
	
	/**
	 * Returns the description for a PBI01 secondary element error code 
	 * 
	 * @param the Secondary message number reported in PBI01 
	 * @return the Description in a string 
	 */
	public static String getPBI01SecondaryDescription(int message) {
   		switch (message)
		{
		case 1: 
   			return "Invalid";
   		case 2: 
   			return "Invalid Based on Related Data";
   		case 3:
   			return "Nonnumeric";
   		case 4:
   			return "Calculation Error";
   		case 5:
   			return "Missing";
   		case 6:
   			return "Required due to Related Data";
   		case 7:
   			return "Not Found";
   		case 8:
   			return "Format Error";
   		case 9:
   			return "Negative";
   		case 10:
   			return "Duplicate";
   		case 11:
   			return "Tolerance";
   		case 12:  
   			return "Out of Range";
   		default:
			return "";	
	    }
	}
	
	/**
	 * Returns the primary description paragraph for a PBI03 error code 
	 * 
	 * @param the primary message number reported in PBI03 
	 * @return the primary description paragraph in a string 
	 */
	public static String getPBI03PrimaryDescription(int message) {
		switch (message)
		{
		case 999: 
			return "Transaction is Out of Balance";
		case 100:  
			return "The field is mandatory, but does not contain a value";
		case 101:  
			return "The field contains an invalid value";
		case 102:  
			return "The field contains an invalid date or a date in the future";
		case 103:  
			return "The field is mandatory for amended submission, but does not contain a value";
		case 104:  
			return "The field is mandatory for initial submission, but does not contain a value";
		case 105:  
			return "The telephone number is incomplete";
		case 106:  
			return "The field contains an invalid data type";
		case 109:  
			return "The field contains a value different from ISA13";
		case 111:  
			return "The N1 segment for Vessel Official number must be present";
		case 112:  
			return "The N1 segment for Position Holder must be present";
		case 113:  
			return "The N1 segment of Point of Origin must be present";
		case 114:  
			return "The N1 segment for Carrier must be present";
		case 115:  
			return "The N1 segment for Point of Destination must be present";
		case 116:  
			return "The N1 segment for Consignor must be present for transactions";
		case 117:  
			return "All dates must be equal to or less than today’s date";
		case 118:  
			return "Ticket dates cannot be any older than 1 year";
		case 121:  
			return "If a terminal shows either receipts or disbursements a TOR ending" +
		           " inventory report is required";
		case 122:  
			return "If the filing company is a terminal operator and a carrier and the" +
		           " terminal operator report shows carrier activity for that company on" +
			       " the schedules, then a CCR report is required";
		case 123:  
			return "If an EDI file is transmitted to the IRS that does not have a TOR or CCR" +
		           " section but has schedule activity, the file is incomplete. The file needs" +
			       " to be corrected and resubmitted";
		case 124:  
			return "If an EDI file is transmitted to the IRS that has a TOR or CCR section but" +
		           " has no schedule activity and has not indicated in the TOR or CCR section" +
			       " that the company has no business activity, the file is incomplete. The file "+
		           " needs to be corrected and resubmitted";
		case 125:  
			return "If the transaction is a terminal receipt, then the net gallons value is required." +
		           " The gross gallons value is optional";
		case 126:  
			return "If the transaction is a bulk terminal disbursement, then the net gallons value" +
		           " is required. The gross gallons value is optional";
		case 127:  
			return "If the transaction is a non-bulk disbursement reported by the operator then the" +
		           " net gallons value is required";
		case 128:  
			return "If the transaction is a non-bulk disbursement reported by the operator then the" +
		           " gross gallons value is required";
		case 129:  
			return "If the transaction is a carrier delivery, then the net gallons value is required." +
		           " Gross gallons are optional";
		case 130:
			return "If the transaction is a terminal receipt for a carrier then the net gallons value" +
		           " is required";
		case 131:  
			return "Information is invalid because of related information in the TFS loop";
		case 132:  
			return "Information is invalid because of related information in the FGS loop";
		case 133:  
			return "Duplicate Originals";
		case 134:  
			return "Duplicate Sequence Numbers";
		case 135:  
			return "Missing Sequence Number";
		default:
			return "";
		}
	}
	
	/**
	 * Returns the description for a PBI03 secondary element error code 
	 * 
	 * @param the Secondary message number reported in PBI03 
	 * @return the Description in a string 
	 */
	public static String getPBI03SecondaryDescription(int message) {
   		switch (message)
		{
		case 1:
			return "Fatal Error. Out of Balance. File not accepted by the IRS as a filed return";
		case 2: 
			return "Correction Error – Error must be corrected and resubmitted prior to next months" +
		           " filing. File is accepted as a filed return";
		case 3:  
			return "Minor Error (Warning message) – Information Provider will not have to resubmit" +
		           " the correction, just correct the system for next month’s filing. File is" +
			       " accepted as a filed return";
		case 4: 
			return "Informational Message: An information only message is provided";
		default:
			return "";
		}
	}
	
	/**
	 * Returns the segment description for a PBI04 error code 
	 * 
	 * @param the message number reported in PBI04 
	 * @return the Description in a string 
	 */
	public static String getPBI04Description(String message) {
		if (message.equals("00000"))
			return "Information Message";
		if (message.equals("E0000")) 
			return "Taxpayer EIN";
		if (message.equals("E0001")) 
			return "Transaction Set Control Number";
		if (message.equals("E0002")) 
			return "Total Net Gallons Reported in Information Return";
		if (message.equals("E0003")) 
			return "Ending Inventory Net Gallons";
		if (message.equals("E0004")) 
			return "Total Net Gallons Transported";
		if (message.equals("E0005")) 
			return "Net Gallons";
		if (message.equals("E0006")) 
			return "Gross Gallons";
		if (message.equals("E0007")) 
			return "Information Provider Name";
		if (message.equals("E0008")) 
			return "rigin Terminal";
		if (message.equals("E0009"))
			return "Ship From State";
		if (message.equals("E0010"))
			return "Consignor";
		if (message.equals("E0011")) 
			return "Carrier Name";
		if (message.equals("E0012")) 
			return "Destination Terminal";
		if (message.equals("E0013")) 
			return "Ship To State";
		if (message.equals("E0014")) 
			return "Period End Date";
		if (message.equals("E0015")) 
			return "Inventory Date";
		if (message.equals("E0016")) 
			return "Document Date";
		if (message.equals("E0017")) 
			return "Position Holder";
		if (message.equals("E0018")) 
			return "637 Number";
		if (message.equals("E0019")) 
			return "Relationship to Information";
		if (message.equals("E0020")) 
			return "Sequence Number";
		if (message.equals("E0021")) 
			return "No Activity";
		if (message.equals("E0022")) 
			return "Information Provider Location";
		if (message.equals("E0023")) 
			return "Terminal Operator Report (TOR)";
		if (message.equals("E0024")) 
			return "Carrier Report (CCR)";
		if (message.equals("E0025")) 
			return "Schedules";
		if (message.equals("E0026")) 
			return "Ending Inventory Loop";
		if (message.equals("E0027")) 
			return "Shipping document Loop";
		if (message.equals("E0028")) 
			return "Carrier EIN";
		if (message.equals("E0029")) 
			return "Change of Terminal Operator Date";
		if (message.equals("E0030")) 
			return "Carrier EIN for non-bulk terminal disbursements – may be required by states";
		if (message.equals("E0031")) 
			return "Exchange Party";
		if (message.equals("E0032")) 
			return "Vessel Official Number";
		if (message.equals("E0050")) 
			return "ST/SE Control Numbers";
		// if we get here the code was not found
		return ""; 
	}
	
	/**
	 * Returns the acceptance or rejection message in the BTA01 element 
	 * @param message found in BTA01 
	 * @return Message translated to a string 
	 */
	public static String getBTA01Message(String message) {
		if (message.equals("AD")) 
			return "Transmission accepted with errors";
		else if (message.equals("AT"))
			return "Transmission accepted with no errors";
		else if (message.equals("RD"))
			return "Transaction rejected";
		// if we get here the code was not found
		return ""; 
	}
	
}  // end class
