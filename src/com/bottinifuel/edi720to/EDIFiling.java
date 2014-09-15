/**
 * 
 */
package com.bottinifuel.edi720to;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.bottinifuel.Energy.Info.Product;
import com.bottinifuel.edi720to.util.Abbreviation;


/**
 * @author laddp
 *
 * Data that needs to be persisted across sessions
 * 
 * Serializable so that we can just save/restore this object from file to save a session
 *
 */
/* Change Log:
* 
*   Date         Description                                        Pgmr
*  ------------  ------------------------------------------------   -----
*  Feb 12,2013   Added stuff for application                        carlonc 
*                Look for comment 021212
*************************************************************************/
public class EDIFiling  implements Serializable {
	private static final long serialVersionUID = 2L;

	transient protected EDI720TO MainApp;
	transient protected boolean  UnsavedChanges;	
	transient protected File     SaveFile;
	
	public final Abbreviation  Warehouse;
	public final List<Product> ProductCodes;
	
	public final Calendar StartDate;
	public final Calendar EndDate;
	public final Date ediStart;
	public final Date ediEnd;
		
	protected boolean    UsePreviousFiling = true;
	protected File       PreviousFiling = null;
	protected BigInteger PreviousStartingGallons = BigInteger.ZERO;
	protected BigInteger ManualStartingGallons = BigInteger.ZERO;
	protected BigInteger ClosingInventory = BigInteger.ZERO;
	
	protected Vector<Transaction> Receipts;
	protected Vector<Transaction> Disbursements;
	protected Vector<Transaction> Adjustments;
	
	protected Vector<Boolean> IncludeReceipts;
	protected Vector<Boolean> IncludeDisbursements;
	protected Vector<Boolean> IncludeAdjustments;
	
	protected Vector<String> ReceiptsCarrier;
	
	// vector of all filings for this object, 813 Original, 997 ACK, 151 ACK and
	// any corrections submittes. They will be in the order they were send or 
	// received
	protected Vector<String> Filings;  // this is a vector of all the filings for 
	
	protected BigDecimal TotalReceipts      = new BigDecimal(0);
	protected BigDecimal TotalDisbursements = new BigDecimal(0);
	protected BigDecimal TotalAdjustments   = new BigDecimal(0);
	
	protected String selected2Product; 
		
	public EDIFiling(EDI720TO app,
			Abbreviation whse,
			List<Product> products,
			Date start, Date end, String selected2Product ) 
	{
		MainApp      = app;
		UnsavedChanges = true;
		SaveFile = null;

		Warehouse    = whse;
		ProductCodes = products;
		ediStart = start;  
		ediEnd = end;
		this.selected2Product=selected2Product;
		
		// NOTE: Sybase types for these are SMALLDATETIME, which only have resolution of 1 minute
		//       Therefore, if you make non-zero seconds, it rounds to the next minute and you
		//       don't get what you think
		StartDate = Calendar.getInstance();
		StartDate.setTime(start);
		StartDate.set(Calendar.HOUR_OF_DAY, 0);
		StartDate.set(Calendar.MINUTE,      0);
		StartDate.set(Calendar.SECOND,      0);
		StartDate.set(Calendar.MILLISECOND, 0);
		
		EndDate = Calendar.getInstance();
		EndDate.setTime(end);
		EndDate.set(Calendar.HOUR_OF_DAY, 23);
		EndDate.set(Calendar.MINUTE,      59);
		EndDate.set(Calendar.SECOND,      0);
		EndDate.set(Calendar.MILLISECOND, 0);
		
		Receipts      = new Vector<Transaction>();
		Disbursements = new Vector<Transaction>();
		Adjustments   = new Vector<Transaction>();

		IncludeReceipts      = new Vector<Boolean>();
		IncludeDisbursements = new Vector<Boolean>();
		IncludeAdjustments   = new Vector<Boolean>();
		
        ReceiptsCarrier      = new Vector<String>();
        Filings	             = new Vector<String>();
		
		try
		{
			Statement s = MainApp.EnergyInfo.getStatement();
			String queryStart = 
					"SELECT " +
					  "RECEIPTS.transfer_whse, " +
					  "RECEIPTS.whse_code, " +
					  "RECEIPTS.stock_id, " +
					  "RECEIPTS.date_received, " +
					  "RECEIPTS.reference_number, " +
					  "RECEIPTS.qty_received ";
			queryStart +=
					"FROM RECEIPTS " +
					"WHERE ";

	        Timestamp sqlStart = new Timestamp(StartDate.getTimeInMillis());
	        Timestamp sqlEnd   = new Timestamp(EndDate.getTimeInMillis());
			queryStart +=
					  "RECEIPTS.date_received >= '" + sqlStart + "' AND " +
					  "RECEIPTS.date_received <= '" + sqlEnd + "' AND ";

			String queryOrder =
					" ORDER BY RECEIPTS.date_received, RECEIPTS.transfer_whse";

			String negAdjSel =
					"SELECT " +
						"ORDER_LINE.order_num, " +
						"ORDER_LINE.order_line, " +
						"ORDER_LINE.order_qty, " +
						"ORDER_LINE.ship_date " +
					"FROM " +
						"ORDER_LINE_WAREHOUSE " +
					    "INNER JOIN " +
							"(ORDER_HEADER INNER JOIN ORDER_LINE " +
					        "ON ORDER_HEADER.order_num = ORDER_LINE.order_num) " +
						"ON (ORDER_LINE_WAREHOUSE.order_line = ORDER_LINE.order_line) " +
					        " AND (ORDER_LINE_WAREHOUSE.order_num = ORDER_LINE.order_num) " +
					"WHERE " + 
						"ORDER_HEADER.non_sale_order='Y' AND " +
						"ORDER_LINE_WAREHOUSE.whse_code='" + Warehouse.Abbreviation + "' AND " +
						"ORDER_LINE.ship_date >= '" + sqlStart + "' AND " +
						"ORDER_LINE.ship_date <= '" + sqlEnd   + "' AND ";

			for (Product prod : ProductCodes)
			{
				String prodWhere =
						"(RECEIPTS.mfg_code='"   + prod.getManufacturer() + "' AND " + 
				         "RECEIPTS.group_code='" + prod.getGroup()        + "' AND " +
				         "RECEIPTS.stock_id='"   + prod.getStockID()      + "') AND ";

				String adjustmentsWhere =
						  "RECEIPTS.adjustment_receipt = 'N' AND ";
				
				String warehouseWhere =
						  "RECEIPTS.whse_code='" + Warehouse.Abbreviation + "' ";

				ResultSet receipts = s.executeQuery(queryStart + adjustmentsWhere + prodWhere + warehouseWhere + queryOrder);
				while (receipts.next())
				{
					Transaction t = new Transaction(
							new BigDecimal(receipts.getString("qty_received")), 
							receipts.getDate("date_received"), 
							prod,
							receipts.getString("reference_number"),
							receipts.getString("transfer_whse"));
					Receipts.add(t);
					IncludeReceipts.add(new Boolean(true));
					ReceiptsCarrier.add(""); 
					TotalReceipts = TotalReceipts.add(t.AffectOnInventory);
				}

				warehouseWhere =
						  "RECEIPTS.transfer_whse='" + Warehouse.Abbreviation + "' ";

				ResultSet disbrs = s.executeQuery(queryStart + adjustmentsWhere + prodWhere + warehouseWhere + queryOrder);
				while (disbrs.next())
				{
					Transaction t = new Transaction(
							new BigDecimal(disbrs.getString("qty_received")).negate(),
							disbrs.getDate("date_received"),
							prod,
							disbrs.getString("reference_number"),
							disbrs.getString("whse_code"));
					Disbursements.add(t);
					IncludeDisbursements.add(new Boolean(true));
				    TotalDisbursements = TotalDisbursements.add(t.AffectOnInventory);
				}

				adjustmentsWhere =
						  "RECEIPTS.adjustment_receipt = 'Y' AND ";
				ResultSet posAdj = s.executeQuery(queryStart + adjustmentsWhere  + prodWhere + warehouseWhere + queryOrder);
				while (posAdj.next())
				{
					Transaction t = new Transaction(
							new BigDecimal(posAdj.getString("qty_received")), 
							posAdj.getDate("date_received"),
							prod,
							posAdj.getString("reference_number"),
							"");
					Adjustments.add(t);
					IncludeAdjustments.add(new Boolean(true));
					TotalAdjustments = TotalAdjustments.add(t.AffectOnInventory);
				}

				String negAdjWhere =
						    "ORDER_LINE.mfg_code='"   + prod.getManufacturer() + "' AND " +
						    "ORDER_LINE.group_code='" + prod.getGroup()        + "' AND " +
						    "ORDER_LINE.stock_id='"   + prod.getStockID()      + "' " +
						"ORDER BY ORDER_LINE.ship_date";
				
				ResultSet negAdj = s.executeQuery(negAdjSel + negAdjWhere);
				while (negAdj.next())
				{
					Transaction t = new Transaction(
							new BigDecimal(negAdj.getString("order_qty")).negate(),
							negAdj.getDate("ship_date"),
							prod,
							negAdj.getString("order_num") + "-" + negAdj.getString("order_line"),
							"");
					Adjustments.add(t);
					IncludeAdjustments.add(new Boolean(true));
					TotalAdjustments = TotalAdjustments.add(t.AffectOnInventory);
				}
			}
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(MainApp.mainFrame,
					"SQL error: " + e.toString(), 
					"SQL Error",
					JOptionPane.ERROR_MESSAGE);
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(MainApp.mainFrame,
					"ADD Energy connection error: " + e.toString(), 
					"ADD Energy connection error",
					JOptionPane.ERROR_MESSAGE);
		}
				
		return;
	}
	
	/**
	 * Returns the start date of the EDI filing, YYMMDD format
	 * @return
	 */
	public String getYYMMDDStartDate() { // new for 021212
		Format formatter = new SimpleDateFormat("yyMMdd");   
        return formatter.format(ediStart);  
	}
	
	/**
	 * Returns the start date of the EDI filing, YYYYMMDD format
	 * @return
	 */
	public String getYYYYMMDDStartDate() { // new for 021212
		Format formatter = new SimpleDateFormat("yyyyMMdd");   
        return formatter.format(ediStart);  
	}
	
	/**
	 * Returns the end date of the EDI filing, YYYYMMDD format
	 * @return
	 */
	public String getYYYYMMDDEndDate() { // new for 021212
		Format formatter = new SimpleDateFormat("yyyyMMdd");   
        return formatter.format(ediEnd);  
	}
	
	/**
	 * Returns the EDI filing start time in HHMM format
	 * @return
	 */
	public String getHHMMStartTime() { // new for 021212
		Format formatter = new SimpleDateFormat("HHmm");   
        return formatter.format(ediStart);   
	}
	
	/**
	 * Returns the month and year of the EDI filing, MMYYYY format
	 * @return
	 */
	public String getMMYYYYDate() { // new for 021212
		Format formatter = new SimpleDateFormat("MMyyyy");   
        return formatter.format(ediEnd);  
	}
	
}
