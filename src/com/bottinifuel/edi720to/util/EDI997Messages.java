package com.bottinifuel.edi720to.util;

/**************************************************************************
*
*   File name: EDI997Messages
*   List of all the 997 Acknowledgement messages
* 	 
* @author carlonc
* 
*************************************************************************
* Change Log:
* 
*   Date         Description                                        Pgmr
*  ------------  ------------------------------------------------   -----
*  Feb 28,2013   Intial Dev...                                     carlonc 
*************************************************************************/
public class EDI997Messages {
	
	public static String getAK3Message(int message) {
   		switch (message)
		{
		case 1:
			return "Unrecognized segment ID";
		case 2:
			return "Unexpected segment";
		case 3:
			return "Mandatory segment missing";
		case 4:
			return "Loop occurs over maximum times";
		case 5:
			return "Segment exceeds maximum use";
		case 6:
			return "Segment not in defined transaction set";
		case 7:
			return "Segment not in proper sequence";
		case 8:
			return "Segment has data element errors";
		default:
			return "";
		}
   	}
   	
   	public static String getAK4Message(int message) {
   		switch (message)
		{
		case 1:   
			return "Mandatory data element missing";
		case 2: 
			return "Conditional required data element missing";
		case 3: 
			return "Too many data elements";
		case 4:   
			return "Data element too short";
		case 5:  
			return "Data element too long";
		case 6: 
			return "Invalid character in data element";
		case 7: 
			return "Invalid code value";
		case 8: 
            return "Invalid date";                            
		case 9: 
			return "Invalid time";
		case 10: 
			return "Exclusion condition violated";
		case 12: 
			return "Too many repetitions";
		case 13: 
            return "Too many components";
		default:
			return "";
	 }
  }
   	
  public static String getAK5Message(int message) {
   	  switch (message)
	  {	
   	  case 1:  
   		  return "Transaction set not supported";
   	  case 2:  
   		  return "Transaction set trailer missing";
   	  case 3: 
   		  return "Transaction set control number in header and trailer do not match";
   	  case 4:  
   		  return "Number of included segments does not match actual count";
   	  case 5: 
   		  return "One or more segments in error";
   	  case 6:  
   		  return "Missing or invalid transaction set identifier";
   	  case 7: 
   		  return "Missing or invalid transaction set control number";
   	  case 8:   
   		  return "Authentication key name unknown";
   	  case 10:  
   		  return "Requested Service (authentication or encryption)” not available";
   	  case 13:  
   		  return "Message authentication code failed";
   	  case 23:  
   		  return "Transaction set control number not unique within the functional group";
   	  default:
		return "";
	  }
  } 
  
  public static String getAK9Message(int message) {
 		switch (message)
		{
 		case 1:
 			return "Functional group not supported"; 
 		case 2: 
 			return "Functional group version not supported";
 		case 3:   
 			return "Functional Group Trailer Missing";
 		case 4:
 			return "Group control number in the functional group header and trailer do not agree";
 		case 5: 
 			return "Number of included transaction sets does not  match actual count";
 		case 6:  
 			return "Group control number violates syntax";
 		case 10:    
 			return "Authentication key name unknown";
 		case 12:  
 			return "Requested Service (authentication or encryption) not available"; 
 		case 18:  
 			return "Message authentication code failed";
 		case 23:  
 			return "Transaction set control number not unique within the functional group";
 		default:
 			return "";	
		}
  }
   	
}	
