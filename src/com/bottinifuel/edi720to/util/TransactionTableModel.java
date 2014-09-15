/**
 * 
 */
package com.bottinifuel.edi720to.util;

import java.awt.Component;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.bottinifuel.Energy.Info.Product;
import com.bottinifuel.edi720to.Transaction;

/**
 * @author laddp
 *
 */
public class TransactionTableModel extends AbstractTableModel {

	private final Vector<Transaction> Transactions;
	private final Vector<Boolean>     IncludeTransaction;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7957786677191638095L;

	public TransactionTableModel(Vector<Transaction> transactions, Vector<Boolean> include)
	{
		Transactions = transactions;
		IncludeTransaction = include;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return 6;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return Transactions.size();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int row, int column) {
		if (row > Transactions.size() - 1)
			return null;
		Transaction t = Transactions.elementAt(row);
		switch (column)
		{
		case 0:
			return IncludeTransaction.elementAt(row);
		case 1:
			return t.TransDate;
		case 2:
			return t.RefNum;
		case 3:
			return t.ProductCode;
		case 4:
			return t.Warehouse;
		case 5:
			return t.AffectOnInventory;
		default:
			return null;
		}
	}
	
	@Override
	public String getColumnName(int column)
	{
		switch (column)
		{
		case 0:
			return "Include";
		case 1:
			return "Date";
		case 2:
			return "RefNum";
		case 3:
			return "Product";
		case 4:
			return "Warehouse";
		case 5:
			return "Quantity";
		default:
			return "Unknown";
		}
	}
	
	@Override
	public Class<?> getColumnClass(int column)
	{
		switch (column)
		{
		case 0:
			return Boolean.class;
		case 1:
			return Date.class;
		case 2:
			return String.class;
		case 3:
			return Product.class;
		case 4:
			return String.class;
		case 5:
			return BigDecimal.class;
		default:
			return Object.class;
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int col)
	{
		if (col == 0)
			return true;
		else
			return false;
	}
	
	@Override
	public void setValueAt(Object value, int row, int col)
	{
        if (col == 0)
        {
        	IncludeTransaction.set(row, (Boolean)value);
        	fireTableCellUpdated(row, col);
        }
    }
	
	public static void initColumnSizes(JTable table) {
        TransactionTableModel model = (TransactionTableModel)table.getModel();
        TableCellRenderer headerRenderer =
            table.getTableHeader().getDefaultRenderer();

        for (int i = 0; i < 5; i++)
        {
            TableColumn column = table.getColumnModel().getColumn(i);

            Component comp = headerRenderer.getTableCellRendererComponent(
                                 null, column.getHeaderValue(),
                                 false, false, 0, 0);
            int headerWidth = comp.getPreferredSize().width;

            int cellWidth;
            if (model.Transactions.size() > 0)
            {
            	comp = table.getDefaultRenderer(model.getColumnClass(i)).
            			getTableCellRendererComponent(
            					table, model.getValueAt(0, i),
            					false, false, 0, i);
            	cellWidth = comp.getPreferredSize().width;
            }
            else
            	cellWidth = 0;

            column.setPreferredWidth(Math.max(headerWidth, cellWidth));
        }
	}
	
	public int getColumnNum(String columnName)
	{
		if (columnName.equals("Include"))
			return 0;
        else if (columnName.equals("Date"))
        	return 1;
        else if (columnName.equals("RefNum"))
        	return 2;
        else if (columnName.equals("Product"))
        	return 3;
        else if (columnName.equals("Warehouse"))
        	return 4;
        else if (columnName.equals("Quantity"))
        	return 5;
        else 
			return -1;
	}
}
