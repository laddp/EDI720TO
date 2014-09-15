/**
 * 
 */
package com.bottinifuel.edi720to;

/**
 * @author laddp
 *
 */
public class Carrier 
{
	/**
	 * 
	 */
	//private static final long serialVersionUID = -8959889145574295925L;
	
	public String vessel;
	public String vesselEin;
	public String carrier; 
	public String carrierEin;
	public String vesselFlag;
	
	
	
	public Carrier(String vessel, String vesselEin, String carrier, String carrierEin, String vesselFlag)
	{
		this.vessel = vessel;
		this.vesselEin = vesselEin;
		this.carrier = carrier;
		this.carrierEin = carrierEin;
		this.vesselFlag = vesselFlag;
	}
}
