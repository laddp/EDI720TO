/**
 * 
 */
package com.bottinifuel.edi720to;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.bottinifuel.Energy.Info.Product;

/**
 * @author laddp
 *
 */
public class Transaction implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8959889145574295925L;
	public final BigDecimal   AffectOnInventory;
	public final Date         TransDate;
	public final Product      ProductCode;
	public final String       RefNum;
	public final String       Warehouse;
	
	public Transaction(BigDecimal qty, Date date, Product prod, String ref, String warehouse)
	{
		// Quantities are rounded to nearest gallon
		AffectOnInventory = qty.setScale(0, BigDecimal.ROUND_HALF_EVEN);
		TransDate         = date;
		ProductCode       = prod;
		RefNum            = ref;
		Warehouse         = warehouse;
	}
}
