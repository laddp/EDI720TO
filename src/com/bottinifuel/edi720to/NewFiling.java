package com.bottinifuel.edi720to;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.bottinifuel.Energy.Info.Product;
import com.bottinifuel.edi720to.util.Abbreviation;
import com.toedter.calendar.JDateChooser;

public class NewFiling extends JDialog implements ListSelectionListener, PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5312867591493063785L;
	
	private final EDI720TO MainApp;

	protected JComboBox<Abbreviation> WarehouseLocation;
	protected JList<Product>          ProductCodes;
	protected JDateChooser            StartDate;
	protected JDateChooser            EndDate;
	
	protected boolean okPressed;
	
	protected String selected2Product = "";
	private JButton okButton;
	private JPanel contentPanel;  // make it global for showProductDialog
	private int riverIndex = 0;  // to allow default selection 
		
	/**
	 * Create the dialog.
	 */
	public NewFiling(EDI720TO app) {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		MainApp = app;

		// Compute default start and end dates
		Calendar compute = Calendar.getInstance();
		compute.set(Calendar.DAY_OF_MONTH, 1);
		compute.add(Calendar.DATE, -1);
		Date endOfLastMonth = compute.getTime();
		compute.set(Calendar.DAY_OF_MONTH, 1);
		Date startOfLastMonth = compute.getTime();
		
		setTitle("New filing");
		setModal(true);
		
		contentPanel = new JPanel();
		setBounds(100, 100, 502, 414);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 1.0};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblStartDate = new JLabel("Start Date:");
			GridBagConstraints gbc_lblStartDate = new GridBagConstraints();
			gbc_lblStartDate.anchor = GridBagConstraints.EAST;
			gbc_lblStartDate.insets = new Insets(0, 0, 5, 5);
			gbc_lblStartDate.gridx = 0;
			gbc_lblStartDate.gridy = 0;
			contentPanel.add(lblStartDate, gbc_lblStartDate);
		}
		{
			StartDate = new JDateChooser(null, "MM/dd/yyyy");
			StartDate.getDateEditor().addPropertyChangeListener(this);
			GridBagConstraints gbc_dateChooser_1 = new GridBagConstraints();
			gbc_dateChooser_1.fill = GridBagConstraints.HORIZONTAL;
			gbc_dateChooser_1.insets = new Insets(0, 0, 5, 0);
			gbc_dateChooser_1.gridx = 1;
			gbc_dateChooser_1.gridy = 0;
			contentPanel.add(StartDate, gbc_dateChooser_1);
			
			StartDate.setDate(startOfLastMonth);
		}
		{
			JLabel lblEndDate = new JLabel("End Date:");
			GridBagConstraints gbc_lblEndDate = new GridBagConstraints();
			gbc_lblEndDate.anchor = GridBagConstraints.EAST;
			gbc_lblEndDate.insets = new Insets(0, 0, 5, 5);
			gbc_lblEndDate.gridx = 0;
			gbc_lblEndDate.gridy = 1;
			contentPanel.add(lblEndDate, gbc_lblEndDate);
		}
		{
			EndDate = new JDateChooser(null, "MM/dd/yyyy");
			EndDate.getDateEditor().addPropertyChangeListener(this);
			GridBagConstraints gbc_dateChooser = new GridBagConstraints();
			gbc_dateChooser.insets = new Insets(0, 0, 5, 0);
			gbc_dateChooser.fill = GridBagConstraints.HORIZONTAL;
			gbc_dateChooser.gridx = 1;
			gbc_dateChooser.gridy = 1;
			contentPanel.add(EndDate, gbc_dateChooser);

			EndDate.setDate(endOfLastMonth);
		}
		{
			JLabel lblWarehouse = new JLabel("Warehouse:");
			GridBagConstraints gbc_lblWarehouse = new GridBagConstraints();
			gbc_lblWarehouse.insets = new Insets(0, 0, 5, 5);
			gbc_lblWarehouse.anchor = GridBagConstraints.EAST;
			gbc_lblWarehouse.gridx = 0;
			gbc_lblWarehouse.gridy = 2;
			contentPanel.add(lblWarehouse, gbc_lblWarehouse);
		}
		{
			WarehouseLocation = new JComboBox<Abbreviation>(warehouseList());
			GridBagConstraints gbc_comboBox = new GridBagConstraints();
			gbc_comboBox.insets = new Insets(0, 0, 5, 0);
			gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
			gbc_comboBox.gridx = 1;
			gbc_comboBox.gridy = 2;
			contentPanel.add(WarehouseLocation, gbc_comboBox);
			WarehouseLocation.setSelectedIndex(riverIndex); //setSelectedItem("81 - (RIVER/VAN WAGNER)");
		}	
		{
			JLabel lblProductCodes = new JLabel("Product Codes:");
			GridBagConstraints gbc_lblProductCodes = new GridBagConstraints();
			gbc_lblProductCodes.anchor = GridBagConstraints.NORTHEAST;
			gbc_lblProductCodes.insets = new Insets(0, 0, 0, 5);
			gbc_lblProductCodes.gridx = 0;
			gbc_lblProductCodes.gridy = 3;
			contentPanel.add(lblProductCodes, gbc_lblProductCodes);
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			GridBagConstraints gbc_scrollPane = new GridBagConstraints();
			gbc_scrollPane.fill = GridBagConstraints.BOTH;
			gbc_scrollPane.gridx = 1;
			gbc_scrollPane.gridy = 3;
			contentPanel.add(scrollPane, gbc_scrollPane);
			{
				ProductCodes = new JList<Product>(productList().toArray(new Product[100]));
				ProductCodes.addListSelectionListener(this);
				scrollPane.setViewportView(ProductCodes);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setEnabled(false);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						handleClose(true);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						handleClose(false);
					}
				});
				buttonPane.add(cancelButton);
			}
		}
	}

	private Vector<Abbreviation> warehouseList()
	{
		Vector<Abbreviation> rc = new Vector<Abbreviation>();
		
		int i = 0;
		
		Statement stmt = null;
		try
		{
			stmt = MainApp.EnergyInfo.getStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT WAREHOUSE_INFO.warehouse_code, WAREHOUSE_INFO.description " +
					"FROM WAREHOUSE INNER JOIN WAREHOUSE_INFO ON WAREHOUSE.whse_code = WAREHOUSE_INFO.warehouse_code " +
					"WHERE WAREHOUSE.mfg_code='LIQ' " +
					"GROUP BY WAREHOUSE_INFO.warehouse_code, WAREHOUSE_INFO.description " +
					"ORDER BY WAREHOUSE_INFO.warehouse_code ");
	        while (rs.next())
	        {
	        	Abbreviation warehouse = new Abbreviation("(" + rs.getString(2).trim() + ")", rs.getString(1).trim());
	        	rc.add(warehouse);
	        	if (rs.getString(1).trim().equals("81"))
	        		riverIndex = i;  // save index to select it for the user
	        	i++;
	        }
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(this,
					"<html>Database error in warehouseList():<br>" + e.toString(), 
					"Database Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(this,
					"<html>ADD Energy connection error:<br>" + e.toString(), 
					"ADD Energy connection error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
		finally
		{
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(this,
							"<html>Database error in warehouseList():<br>" + e.toString(), 
							"Database Error",
							JOptionPane.ERROR_MESSAGE);
				}
		}
		return rc;
	}

	private List<Product> productList()
	{
		try
		{
			List<Product> rc = MainApp.EnergyInfo.GetProducts("LIQ", null, null, 0);
			return rc;
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(this,
					"<html>Database error in productList():<br>" + e.toString(), 
					"Database Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(this,
					"<html>ADD Energy connection error:<br>" + e.toString(), 
					"ADD Energy connection error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}
	
	private void handleClose(boolean ok)
	{
		okPressed = ok;
		this.setVisible(false);
	}

	private void updateOkButton()
	{
		if (okButton == null)
			return;
		Date start = null;
		if (StartDate != null)
			start = StartDate.getDate();
		Date end = null;
		if (EndDate != null)
			end = EndDate  .getDate();
		
		if (start != null &&
			end   != null &&
			ProductCodes != null &&
			ProductCodes.getSelectedIndex() != -1)
			okButton.setEnabled(true);
		else
			okButton.setEnabled(false);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		updateOkButton();
		Product s = ProductCodes.getModel().getElementAt(ProductCodes.getSelectedIndex());
    	if (s.getPostingCode()==2) {
    		showProductDialog(s);
    	}
	}
	
	private void showProductDialog(Product prod) {
		ProductSelectDialog pd = new ProductSelectDialog(prod, contentPanel);
		selected2Product = pd.getSelection();
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		updateOkButton();
	}
}
