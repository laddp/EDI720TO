/**
 * 
 */
package com.bottinifuel.edi720to.util;

/**************************************************************************
*
*  File name: CarrierTableModel
*  Defines the Carrier Table Model
* 	 
* @author carlonc
* 
*************************************************************************/
/* Change Log:
* 
*   Date         Description                                        Pgmr
*  ------------  ------------------------------------------------   -----
*  Mar 04,2013   Intial Dev...                                     carlonc 
*************************************************************************/

import java.awt.Component;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import com.bottinifuel.edi720to.Carrier;

/**
 * @author laddp
 *
 */
public class CarrierTableModel extends AbstractTableModel {

	private final Vector<Carrier> Carriers;
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -7957786677191638095L;

	public CarrierTableModel(Vector<Carrier> carriers)
	{
		Carriers = carriers;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return 5;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return Carriers.size();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int row, int column) {
		if (row > Carriers.size() - 1) {
			return null;
		}	
		Carrier c = Carriers.elementAt(row);
		switch (column)
		{
		case 0:
			return c.vessel;
		case 1:
			return c.vesselEin;
		case 2:
			return c.carrier;
		case 3:
			return c.carrierEin;	
		case 4:
			return c.vesselFlag;	
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
			return "Vessel";
		case 1:
			return "Vessel EIN";
		case 2:
			return "Carrier";	
		case 3:
			return "Carrier EIN";	
		case 4:
			return "Flag";		
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
			return String.class;
		case 1:
			return String.class;
		case 2:
			return String.class;
		case 3:
			return String.class;	
		case 4:
			return String.class;	
		default:
			return Object.class;
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int col)
	{
		return true;
		/*if (col == 0)
			return false;
		else if (col == 1)
			return false;
		else if (col == 2)
			return false;
		else if (col == 3)
			return false;
		else
			return false;*/
	}
	
	@Override
	public void setValueAt(Object value, int row, int col)
	{
		if (row > Carriers.size() - 1)
			return;
		Carrier c = Carriers.elementAt(row);
		if (col==0)
			c.vessel=value.toString();
		if (col==1)
			c.vesselEin=value.toString();
		if (col==2)
			c.carrier=value.toString();
		if (col==3)
			c.carrierEin=value.toString();
		if (col==4)
		    c.vesselFlag=value.toString();
		fireTableDataChanged();
    }
	
	public static void initColumnSizes(JTable table) {
        CarrierTableModel model = (CarrierTableModel)table.getModel();
        TableCellRenderer headerRenderer =
            table.getTableHeader().getDefaultRenderer();

        for (int i = 0; i < 4; i++)
        {
            TableColumn column = table.getColumnModel().getColumn(i);

            Component comp = headerRenderer.getTableCellRendererComponent(
                                 null, column.getHeaderValue(),
                                 false, false, 0, 0);
            int headerWidth = comp.getPreferredSize().width;

            int cellWidth;
            if (model.Carriers.size() > 0)
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
        if (columnName.equals("Vessel"))
			return 0;
        else if (columnName.equals("Vessel EIN"))
        	return 1;
        else if (columnName.equals("Carrier"))
        	return 2;
        else if (columnName.equals("Carrier EIN"))
        	return 3;
        else if (columnName.equals("Flag"))
        	return 4;
        else 
			return -1;
	}
	
	public void addRow(Object value) {
		Carriers.addElement((Carrier)value);
		fireTableDataChanged();
	}
	
	public Carrier getRow(int row) {
		if (row > Carriers.size()-1) {
			return null;
		}	
		return Carriers.elementAt(row);
	}
	
	public void removeRow(int row) {
		if (row > Carriers.size() - 1) {
			return;
		}	
		Carriers.removeElementAt(row);
        fireTableRowsDeleted(row, row);
    }
	
	public void fireDataChanged() {
		fireTableDataChanged();
	}
		
}
