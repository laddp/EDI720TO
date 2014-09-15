package com.bottinifuel.edi720to;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.NumberFormatter;

import com.bottinifuel.Energy.Info.InfoFactory;
import com.bottinifuel.Energy.Info.Product;
import com.bottinifuel.edi720to.util.Abbreviation;
import com.bottinifuel.edi720to.util.ReceiptsTableModel;
import com.bottinifuel.edi720to.util.TransactionTableModel;

//NOTE find barge info at: http://cgmix.uscg.mil/PSIX/PSIXSearch.aspx

public class EDI720TO {
	
	private boolean DEBUG = true;
	
	private static final String NO_DATA_LOADED_STRING = "- No data loaded -";
	protected JFrame mainFrame;
	private   JTable receiptsTable;
	private   JTable disbursementsTable;
	private   JTable adjustmentsTable;
	
	private   JFileChooser Chooser = null;
	
	protected Preferences preferences = new Preferences(this);
	protected InfoFactory EnergyInfo = null;

	protected EDIFiling         CurrentFiling = null;
	private JFormattedTextField txtPreviousFilingFile;
	private JFormattedTextField txtManualStartInventory;
	private JLabel              lblDates;
	private JLabel              lblWarehouseInfo;
	private JLabel              lblProductInfo;
	private JRadioButton        rdbtnPreviousFiling;
	private JRadioButton        rdbtnManuallyEntered;
	private JLabel              lblStartingInventory;
	private JLabel              lblDisbursements;
	private JLabel              lblReceipts;
	private JFormattedTextField txtEndingInventory;
	private JLabel              lblEndingInventory;
	private JLabel              lblAdjustments;
	private JLabel              lblDifference;
	private JLabel              lblBottomSummary;
	private JButton             btnFileBrowse;
	private JMenuItem           mntmSave;
	private JMenuItem           mntmSaveAs;
	private JMenuItem           mntmUploadReturn;
	private JMenuItem           mntmOpen997Return;
	private JMenuItem           mntmOpen151Return;
	
	private boolean runListener = true;
	private BigDecimal netGallons;
	
	public ReceiptsTableModel receiptsModel;
	public TransactionTableModel disbursementsModel;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					@SuppressWarnings("unused")
					EDI720TO window = new EDI720TO();
				} catch (Exception e) {
					System.out.println("Uncaught exception:\\n" + e);
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public EDI720TO() {
		// Build the window
		initialize();
		mainFrame.setVisible(true);
		
		// Make database connection
		initDB();
	}
	
	protected void initDB()
	{
		String server   = preferences.getDBserver();
		int    port     = preferences.getDBport();
		String user     = preferences.getDBuser();
		String password = preferences.getDBpassword();
		int    number   = preferences.getDBnumber();
		
		if (server != null && port != 0 && user != null && password != null && number != 0)
		{
			try {
				EnergyInfo = new InfoFactory(server, port, "AR" + number, user, password);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(mainFrame,
						"<html>Error connecting to Energy Database:<br>" + e.toString() + "<br>", 
						"Database Connect Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		else
		{
			JOptionPane.showMessageDialog(mainFrame,
					"<html>Incomplete database information.<br>Please enter database information and restart.", 
					"Database Connect Incomplete",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		mainFrame = new JFrame();
		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				doExit();
			}
		});
		mainFrame.setTitle("ADD E3 IRS 720-TO Filing Tool");
		mainFrame.setBounds(100, 100, 668, 461);
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		mainFrame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic('F');
		menuBar.add(mnFile);
		
		JMenuItem mntmNewMonthlyReturn = new JMenuItem("New monthly return...");
		mntmNewMonthlyReturn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doNewReturn();
			}
		});
		mntmNewMonthlyReturn.setMnemonic('N');
		mntmNewMonthlyReturn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mnFile.add(mntmNewMonthlyReturn);
		
		JMenuItem mntmOpenExistingReturn = new JMenuItem("Open existing return...");
		mntmOpenExistingReturn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doOpen();
			}
		});
		mntmOpenExistingReturn.setMnemonic('O');
		mntmOpenExistingReturn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnFile.add(mntmOpenExistingReturn);
		
		JMenuItem mntmUpdateExistingReturn = new JMenuItem("Update existing return...");
		mntmUpdateExistingReturn.setMnemonic('U');
		mntmUpdateExistingReturn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_MASK));
		mnFile.add(mntmUpdateExistingReturn);
		 
		mntmOpen997Return = new JMenuItem("Open 997 Ack for Verify...");
		mntmOpen997Return.setEnabled(false);
		mntmOpen997Return.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File file = fileChooser();
				if (file!=null)
				   doVerify997Ack(file);	
			}
		});
		mntmOpen997Return.setMnemonic('V');
		mntmOpen997Return.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
		mnFile.add(mntmOpen997Return);
		
		mntmOpen151Return = new JMenuItem("Open 151 Ack for Verify...");
		mntmOpen151Return.setEnabled(false);
		mntmOpen151Return.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File file = fileChooser();
				if (file!=null)
				   doVerify151Ack(file);	
			}
		});
		mntmOpen151Return.setMnemonic('C'); // c for certify
		mntmOpen151Return.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		mnFile.add(mntmOpen151Return);
		
		mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doSave(true);
			}
		});
		mntmSave.setEnabled(false);
		mntmSave.setMnemonic('S');
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnFile.add(mntmSave);
		
		mntmSaveAs = new JMenuItem("Save As...");
		mntmSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSave(false);
			}
		});
		mntmSaveAs.setEnabled(false);
		mntmSaveAs.setMnemonic('A');
		mnFile.add(mntmSaveAs);
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		
		JMenuItem mntmPreferences = new JMenuItem("Preferences...");
		mntmPreferences.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				preferences.setVisible(true);
			}
		});
		mntmPreferences.setMnemonic('P');
		mnFile.add(mntmPreferences);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doExit();
			}
		});
		mntmExit.setMnemonic('x');
		mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		mnFile.add(mntmExit);
		
		JMenu mnSendreceive = new JMenu("Communications");
		mnSendreceive.setMnemonic('C');
		menuBar.add(mnSendreceive);
		
		mntmUploadReturn = new JMenuItem("Create EDI return...");
		mntmUploadReturn.setEnabled(false);
		mntmUploadReturn.setMnemonic('U');
		mnSendreceive.add(mntmUploadReturn);
		mntmUploadReturn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createOriginalFiling();
			}
		});
				
		JMenuItem mntmDownloadResults = new JMenuItem("Download Results...");
		mntmDownloadResults.setMnemonic('D');
		mnSendreceive.add(mntmDownloadResults);
		
		JMenu mnHelp = new JMenu("Help");
		mnHelp.setMnemonic('H');
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About...");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(mainFrame,
						"EDI 720TO Filing Program\n(c)2012 Patrick Ladd, Bottini Fuel\nv0.1", 
						"About EDI 720TO",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mntmAbout.setMnemonic('A');
		mnHelp.add(mntmAbout);
		mainFrame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel bottomSummaryPanel = new JPanel();
		mainFrame.getContentPane().add(bottomSummaryPanel, BorderLayout.SOUTH);
		bottomSummaryPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		lblBottomSummary = new JLabel("");
		bottomSummaryPanel.add(lblBottomSummary);
		
		JTabbedPane tabFrame = new JTabbedPane(JTabbedPane.TOP);
		mainFrame.getContentPane().add(tabFrame, BorderLayout.CENTER);
		
		JPanel summaryPanel = new JPanel();
		tabFrame.addTab("Summary", null, summaryPanel, null);
		GridBagLayout gbl_summaryPanel = new GridBagLayout();
		gbl_summaryPanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_summaryPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_summaryPanel.columnWeights = new double[]{0.0, 1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_summaryPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		summaryPanel.setLayout(gbl_summaryPanel);
		
		JLabel lblInventorySummary = new JLabel("Inventory Summary:");
		lblInventorySummary.setFont(new Font("Tahoma", Font.BOLD, 15));
		GridBagConstraints gbc_lblInventorySummary = new GridBagConstraints();
		gbc_lblInventorySummary.gridwidth = 5;
		gbc_lblInventorySummary.insets = new Insets(0, 0, 5, 0);
		gbc_lblInventorySummary.gridx = 0;
		gbc_lblInventorySummary.gridy = 0;
		summaryPanel.add(lblInventorySummary, gbc_lblInventorySummary);
		
		lblDates = new JLabel(NO_DATA_LOADED_STRING);
		GridBagConstraints gbc_lblDates = new GridBagConstraints();
		gbc_lblDates.gridwidth = 5;
		gbc_lblDates.insets = new Insets(0, 0, 5, 0);
		gbc_lblDates.gridx = 0;
		gbc_lblDates.gridy = 1;
		summaryPanel.add(lblDates, gbc_lblDates);
		
		lblWarehouseInfo = new JLabel("");
		GridBagConstraints gbc_lblWarehouseInfo = new GridBagConstraints();
		gbc_lblWarehouseInfo.gridwidth = 5;
		gbc_lblWarehouseInfo.insets = new Insets(0, 0, 5, 0);
		gbc_lblWarehouseInfo.gridx = 0;
		gbc_lblWarehouseInfo.gridy = 2;
		summaryPanel.add(lblWarehouseInfo, gbc_lblWarehouseInfo);
		
		lblProductInfo = new JLabel("");
		GridBagConstraints gbc_lblProductInfo = new GridBagConstraints();
		gbc_lblProductInfo.gridwidth = 5;
		gbc_lblProductInfo.insets = new Insets(0, 0, 5, 0);
		gbc_lblProductInfo.gridx = 0;
		gbc_lblProductInfo.gridy = 3;
		summaryPanel.add(lblProductInfo, gbc_lblProductInfo);
		
		JLabel lblStartingInventoryLabel = new JLabel("Starting Inventory:");
		GridBagConstraints gbc_lblStartingInventoryLabel = new GridBagConstraints();
		gbc_lblStartingInventoryLabel.anchor = GridBagConstraints.EAST;
		gbc_lblStartingInventoryLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblStartingInventoryLabel.gridx = 0;
		gbc_lblStartingInventoryLabel.gridy = 5;
		summaryPanel.add(lblStartingInventoryLabel, gbc_lblStartingInventoryLabel);
		
		lblStartingInventory = new JLabel("");
		lblStartingInventory.setBackground(Color.LIGHT_GRAY);
		GridBagConstraints gbc_lblStartingInventory = new GridBagConstraints();
		gbc_lblStartingInventory.anchor = GridBagConstraints.EAST;
		gbc_lblStartingInventory.insets = new Insets(0, 0, 5, 5);
		gbc_lblStartingInventory.gridx = 1;
		gbc_lblStartingInventory.gridy = 5;
		summaryPanel.add(lblStartingInventory, gbc_lblStartingInventory);
		
		rdbtnPreviousFiling = new JRadioButton("Previous filing:");
		rdbtnPreviousFiling.setEnabled(false);
		rdbtnPreviousFiling.addChangeListener(new btnGroupChangeListener());
		GridBagConstraints gbc_rdbtnPreviousFiling = new GridBagConstraints();
		gbc_rdbtnPreviousFiling.anchor = GridBagConstraints.WEST;
		gbc_rdbtnPreviousFiling.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnPreviousFiling.gridx = 2;
		gbc_rdbtnPreviousFiling.gridy = 5;
		summaryPanel.add(rdbtnPreviousFiling, gbc_rdbtnPreviousFiling);
		
		txtPreviousFilingFile = new JFormattedTextField();
		txtPreviousFilingFile.setEnabled(false);
		GridBagConstraints gbc_txtPreviousFilingFile = new GridBagConstraints();
		gbc_txtPreviousFilingFile.insets = new Insets(0, 0, 5, 5);
		gbc_txtPreviousFilingFile.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPreviousFilingFile.gridx = 3;
		gbc_txtPreviousFilingFile.gridy = 5;
		summaryPanel.add(txtPreviousFilingFile, gbc_txtPreviousFilingFile);
		txtPreviousFilingFile.setColumns(10);
		txtPreviousFilingFile.getDocument().addDocumentListener(new txtChangeListener());
		
		btnFileBrowse = new JButton("Browse...");
		btnFileBrowse.setEnabled(false);
		GridBagConstraints gbc_btnFileBrowse = new GridBagConstraints();
		gbc_btnFileBrowse.insets = new Insets(0, 0, 5, 0);
		gbc_btnFileBrowse.gridx = 4;
		gbc_btnFileBrowse.gridy = 5;
		btnFileBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doOpen();
				setPreviousFiling();
			}
		});
		summaryPanel.add(btnFileBrowse, gbc_btnFileBrowse);
		
		rdbtnManuallyEntered = new JRadioButton("Manually entered:");
		rdbtnManuallyEntered.setEnabled(false);
		rdbtnManuallyEntered.addChangeListener(new btnGroupChangeListener());
		GridBagConstraints gbc_rdbtnManuallyEntered = new GridBagConstraints();
		gbc_rdbtnManuallyEntered.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnManuallyEntered.anchor = GridBagConstraints.WEST;
		gbc_rdbtnManuallyEntered.gridx = 2;
		gbc_rdbtnManuallyEntered.gridy = 6;
		summaryPanel.add(rdbtnManuallyEntered, gbc_rdbtnManuallyEntered);
		
		NumberFormatter nf = new NumberFormatter(NumberFormat.getNumberInstance());
		nf.setMinimum(0.0);
		
		txtManualStartInventory = new JFormattedTextField(nf);
		txtManualStartInventory.setEnabled(false);
		GridBagConstraints gbc_txtManualStartInventory = new GridBagConstraints();
		gbc_txtManualStartInventory.insets = new Insets(0, 0, 5, 5);
		gbc_txtManualStartInventory.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtManualStartInventory.gridx = 3;
		gbc_txtManualStartInventory.gridy = 6;
		summaryPanel.add(txtManualStartInventory, gbc_txtManualStartInventory);
		txtManualStartInventory.setColumns(10);
		txtManualStartInventory.getDocument().addDocumentListener(new txtChangeListener());

		ButtonGroup bg = new ButtonGroup();
		bg.add(rdbtnPreviousFiling);
		bg.add(rdbtnManuallyEntered);
		
		JLabel lblReceiptsLabel = new JLabel("Receipts:");
		GridBagConstraints gbc_lblReceiptsLabel = new GridBagConstraints();
		gbc_lblReceiptsLabel.anchor = GridBagConstraints.EAST;
		gbc_lblReceiptsLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblReceiptsLabel.gridx = 0;
		gbc_lblReceiptsLabel.gridy = 7;
		summaryPanel.add(lblReceiptsLabel, gbc_lblReceiptsLabel);
		
		lblReceipts = new JLabel("");
		GridBagConstraints gbc_lblReceipts = new GridBagConstraints();
		gbc_lblReceipts.anchor = GridBagConstraints.EAST;
		gbc_lblReceipts.insets = new Insets(0, 0, 5, 5);
		gbc_lblReceipts.gridx = 1;
		gbc_lblReceipts.gridy = 7;
		summaryPanel.add(lblReceipts, gbc_lblReceipts);
		
		JLabel lblDisbursementsLabel = new JLabel("Disbursements:");
		GridBagConstraints gbc_lblDisbursementsLabel = new GridBagConstraints();
		gbc_lblDisbursementsLabel.anchor = GridBagConstraints.EAST;
		gbc_lblDisbursementsLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblDisbursementsLabel.gridx = 0;
		gbc_lblDisbursementsLabel.gridy = 8;
		summaryPanel.add(lblDisbursementsLabel, gbc_lblDisbursementsLabel);
		
		lblDisbursements = new JLabel("");
		GridBagConstraints gbc_lblDisbursements = new GridBagConstraints();
		gbc_lblDisbursements.anchor = GridBagConstraints.EAST;
		gbc_lblDisbursements.insets = new Insets(0, 0, 5, 5);
		gbc_lblDisbursements.gridx = 1;
		gbc_lblDisbursements.gridy = 8;
		summaryPanel.add(lblDisbursements, gbc_lblDisbursements);
		
		JLabel lblAdjustmentsLabel = new JLabel("Adjustments:");
		GridBagConstraints gbc_lblAdjustmentsLabel = new GridBagConstraints();
		gbc_lblAdjustmentsLabel.anchor = GridBagConstraints.EAST;
		gbc_lblAdjustmentsLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblAdjustmentsLabel.gridx = 0;
		gbc_lblAdjustmentsLabel.gridy = 9;
		summaryPanel.add(lblAdjustmentsLabel, gbc_lblAdjustmentsLabel);
		
		lblAdjustments = new JLabel("");
		GridBagConstraints gbc_lblAdjustments = new GridBagConstraints();
		gbc_lblAdjustments.anchor = GridBagConstraints.ABOVE_BASELINE_TRAILING;
		gbc_lblAdjustments.insets = new Insets(0, 0, 5, 5);
		gbc_lblAdjustments.gridx = 1;
		gbc_lblAdjustments.gridy = 9;
		summaryPanel.add(lblAdjustments, gbc_lblAdjustments);
		
		JLabel lblEndingInventoryLabel = new JLabel("Ending Inventory:");
		GridBagConstraints gbc_lblEndingInventoryLabel = new GridBagConstraints();
		gbc_lblEndingInventoryLabel.anchor = GridBagConstraints.EAST;
		gbc_lblEndingInventoryLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblEndingInventoryLabel.gridx = 0;
		gbc_lblEndingInventoryLabel.gridy = 10;
		summaryPanel.add(lblEndingInventoryLabel, gbc_lblEndingInventoryLabel);
		
		lblEndingInventory = new JLabel("");
		GridBagConstraints gbc_lblEndingInventry = new GridBagConstraints();
		gbc_lblEndingInventry.anchor = GridBagConstraints.EAST;
		gbc_lblEndingInventry.insets = new Insets(0, 0, 5, 5);
		gbc_lblEndingInventry.gridx = 1;
		gbc_lblEndingInventry.gridy = 10;
		summaryPanel.add(lblEndingInventory, gbc_lblEndingInventry);
		
		txtEndingInventory = new JFormattedTextField(nf);
		txtEndingInventory.setEnabled(false);
		GridBagConstraints gbc_txtEndingInventory = new GridBagConstraints();
		gbc_txtEndingInventory.gridwidth = 2;
		gbc_txtEndingInventory.insets = new Insets(0, 0, 5, 5);
		gbc_txtEndingInventory.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtEndingInventory.gridx = 2;
		gbc_txtEndingInventory.gridy = 10;
		summaryPanel.add(txtEndingInventory, gbc_txtEndingInventory);
		txtEndingInventory.setColumns(10);
		txtEndingInventory.getDocument().addDocumentListener(new txtChangeListener());
		
		JLabel lblDifferenceLabel = new JLabel("Difference:");
		GridBagConstraints gbc_lblDifferenceLabel = new GridBagConstraints();
		gbc_lblDifferenceLabel.anchor = GridBagConstraints.EAST;
		gbc_lblDifferenceLabel.insets = new Insets(0, 0, 0, 5);
		gbc_lblDifferenceLabel.gridx = 0;
		gbc_lblDifferenceLabel.gridy = 11;
		summaryPanel.add(lblDifferenceLabel, gbc_lblDifferenceLabel);
		
		lblDifference = new JLabel("");
		GridBagConstraints gbc_lblDifference = new GridBagConstraints();
		gbc_lblDifference.anchor = GridBagConstraints.EAST;
		gbc_lblDifference.insets = new Insets(0, 0, 0, 5);
		gbc_lblDifference.gridx = 1;
		gbc_lblDifference.gridy = 11;
		summaryPanel.add(lblDifference, gbc_lblDifference);
		
		JPanel receiptsPanel = new JPanel();
		tabFrame.addTab("Receipts", null, receiptsPanel, null);
		receiptsPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JScrollPane receiptsScrollPane = new JScrollPane();
		receiptsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		receiptsPanel.add(receiptsScrollPane);
		
		receiptsTable = new JTable();
		receiptsScrollPane.setViewportView(receiptsTable);
		receiptsTable.setFillsViewportHeight(true);
		receiptsTable.setAutoCreateRowSorter(true);
		
		JPanel disbursementsPanel = new JPanel();
		tabFrame.addTab("Disbursements", null, disbursementsPanel, null);
		disbursementsPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JScrollPane disbursementsScrollPane = new JScrollPane();
		disbursementsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		disbursementsPanel.add(disbursementsScrollPane);
		
		disbursementsTable = new JTable();
		disbursementsScrollPane.setViewportView(disbursementsTable);
		disbursementsTable.setFillsViewportHeight(true);
		disbursementsTable.setAutoCreateRowSorter(true);
		
		JPanel adjustmentsPanel = new JPanel();
		tabFrame.addTab("Adjustments", null, adjustmentsPanel, null);
		adjustmentsPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JScrollPane adjustmentsScrollPane = new JScrollPane();
		adjustmentsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		adjustmentsPanel.add(adjustmentsScrollPane);
		
		adjustmentsTable = new JTable();
		adjustmentsScrollPane.setViewportView(adjustmentsTable);
		adjustmentsTable.setFillsViewportHeight(true);
		adjustmentsTable.setAutoCreateRowSorter(true);
		
	}

	
	private void doExit()
	{
		if (CurrentFiling != null && CurrentFiling.UnsavedChanges)
		{
			int rc = JOptionPane.showConfirmDialog(mainFrame, "Return not saved, exit anyway?", "Confirm exit", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (rc == JOptionPane.NO_OPTION)
				return;
		}
		System.exit(0);
	}
	
	private void doNewReturn()
	{
		if (CurrentFiling != null && CurrentFiling.UnsavedChanges)
		{
			int rc = JOptionPane.showConfirmDialog(mainFrame, "Current return not saved, discard it without saving?", "Confirm New", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (rc == JOptionPane.NO_OPTION)
				return;
		}
		
		NewFiling newf = new NewFiling(this);
		newf.setVisible(true);
		if (newf.okPressed)
		{
			//runListener = false;
			clearCurrentReturn();
			
			lblWarehouseInfo.setText(newf.WarehouseLocation.getSelectedItem().toString());

			String productList = null;
			List<Product> products = new LinkedList<Product>();
			for (Object o : newf.ProductCodes.getSelectedValuesList())
			{
				Product p = (Product)o;
				products.add(p);
				if (productList == null)
					productList = p.toString();
				else
					productList += " // " + p.toString();
			}
							
			lblProductInfo.setText(productList);
					
			CurrentFiling = 
					new EDIFiling(this, 
							(Abbreviation)newf.WarehouseLocation.getSelectedItem(), 
							products, 
							newf.StartDate.getDate(), 
							newf.EndDate.getDate(), newf.selected2Product); 
							
			filingLoaded(); // this turns listener back on so no need to do it here
		}
	}

	private void filingLoaded()
	{ 
		/*JComboBox test = new JComboBox();
		test.addItem("test1");
		test.addItem("test1");
		test.addItem("test1");
		test.addItem("test1");
		CurrentFiling.ReceiptsCarrier.setSize(0);
		for (int i=0; i<CurrentFiling.IncludeReceipts.size(); i++) {
			System.out.println(i);
			CurrentFiling.ReceiptsCarrier.addElement(test);	
			
		}*/
		
		runListener = false;
		lblDates.setText(CurrentFiling.StartDate.getTime() + " - " + CurrentFiling.EndDate.getTime());

		
		receiptsModel = new ReceiptsTableModel(CurrentFiling.Receipts,        CurrentFiling.IncludeReceipts, CurrentFiling.ReceiptsCarrier);
				
		receiptsTable     .setModel(receiptsModel);
		// cfc: add listener stuff 
		receiptsModel.addTableModelListener(new TableModelListener() {
		      public void tableChanged(TableModelEvent e) {
		    	  /*if (e.getColumn()==receiptsModel.getColumnNum("Vessels"))
		    		  receiptsModel.setValueAt(e.getSource(), e.getLastRow(), e.getColumn());
		    	  else */
		    	  adjustReceipts(e);  
		      }
		});
		
		disbursementsModel = new TransactionTableModel(CurrentFiling.Disbursements, CurrentFiling.IncludeDisbursements);
		disbursementsTable.setModel(disbursementsModel);
		// cfc: add listener stuff 
		disbursementsModel.addTableModelListener(new TableModelListener() {
		      public void tableChanged(TableModelEvent e) {
		    	  adjustDisbursements(e);
		      }
		});
				
		adjustmentsTable  .setModel(new TransactionTableModel(CurrentFiling.Adjustments,   CurrentFiling.IncludeAdjustments));
		
		ReceiptsTableModel   .initColumnSizes(receiptsTable);
		// set up cell editor and renderer for the JComboBox
		TableColumn vesselColumn = receiptsTable.getColumnModel().getColumn(receiptsModel.findColumn("Vessel")); 
		vesselColumn.setCellEditor(new DefaultCellEditor(preferences.getCarrierComboBox()));
		//Set up tool tips  
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setToolTipText("Select the delivery vessel");
        vesselColumn.setCellRenderer(renderer);
						
        TransactionTableModel.initColumnSizes(disbursementsTable);
		TransactionTableModel.initColumnSizes(adjustmentsTable);
		
		rdbtnPreviousFiling.setEnabled(true); 
		rdbtnPreviousFiling.setSelected(CurrentFiling.UsePreviousFiling);
		txtPreviousFilingFile.setEnabled(CurrentFiling.UsePreviousFiling);
		
		lblWarehouseInfo.setText(CurrentFiling.Warehouse.toString());
		lblProductInfo.setText(CurrentFiling.ProductCodes.toString());
		
		txtPreviousFilingFile.setText(CurrentFiling.PreviousStartingGallons.toString());
		
		rdbtnManuallyEntered.setEnabled(true); 
		rdbtnManuallyEntered.setSelected(!CurrentFiling.UsePreviousFiling);
		txtManualStartInventory.setEnabled(!CurrentFiling.UsePreviousFiling);
		txtManualStartInventory.setText(CurrentFiling.ManualStartingGallons.toString());
		
		txtEndingInventory.setEnabled(true); 
		txtEndingInventory.setText(CurrentFiling.ClosingInventory.toString());
		
		mntmSave         .setEnabled(true);
		mntmSaveAs       .setEnabled(true);
		mntmOpen997Return.setEnabled(true);
		mntmOpen151Return.setEnabled(true);
		
        
		runListener = true;
		recalcTotals();
		//System.out.println("Current EDI report: "+CurrentFiling.OriginalReport);
	}
	
	private void clearCurrentReturn()
	{
		runListener = false;
		lblDates.setText(NO_DATA_LOADED_STRING);
		lblWarehouseInfo.setText("");
		lblProductInfo  .setText("");
		
		txtPreviousFilingFile  .setText("");
		txtPreviousFilingFile  .setEnabled(false);
		txtManualStartInventory.setText("");
		txtManualStartInventory.setEnabled(false);
		txtEndingInventory     .setText("");
		txtEndingInventory     .setEnabled(false);
		
		rdbtnPreviousFiling.setSelected(false);
		rdbtnPreviousFiling.setEnabled(false);
		rdbtnManuallyEntered.setSelected(false);
		rdbtnManuallyEntered.setEnabled(false);
		
		lblStartingInventory.setText("");
		lblReceipts         .setText("");
		lblDisbursements    .setText("");
		lblAdjustments      .setText("");
		lblEndingInventory  .setText("");
		lblDifference       .setText("");
		
		lblBottomSummary    .setText("");

		mntmSave         .setEnabled(false);
		mntmSaveAs       .setEnabled(false);
		mntmOpen997Return.setEnabled(false);
		mntmOpen151Return.setEnabled(false);
		runListener = true;
	}
	
	private void recalcTotals()
	{
		if (!runListener) 
			return;
		
		BigDecimal startingInv;
		if (rdbtnManuallyEntered.isSelected())
		{
			CurrentFiling.PreviousStartingGallons = BigInteger.ZERO; 
			String start = txtManualStartInventory.getText();
			startingInv = new BigDecimal(start.length() > 0 ? start.replaceAll(",", "") : "0");
			if (CurrentFiling != null)
				CurrentFiling.ManualStartingGallons = startingInv.toBigInteger();
		}
		else // use previous file
		{   
			CurrentFiling.ManualStartingGallons = BigInteger.ZERO; 
			String start = txtPreviousFilingFile.getText();
			startingInv = new BigDecimal(start.length() > 0 ? start.replaceAll(",", "") : "0");
			if (CurrentFiling != null)
				CurrentFiling.PreviousStartingGallons = startingInv.toBigInteger();
		}

		lblStartingInventory.setText(NumberFormat.getInstance().format(startingInv));
		lblReceipts         .setText(NumberFormat.getInstance().format(CurrentFiling.TotalReceipts));
		lblDisbursements    .setText(NumberFormat.getInstance().format(CurrentFiling.TotalDisbursements));
		lblAdjustments      .setText(NumberFormat.getInstance().format(CurrentFiling.TotalAdjustments));

		String end = txtEndingInventory.getText();
						
		BigDecimal endingInv = new BigDecimal(end.length() > 0 ? end.replaceAll(",", "") : "0");
		lblEndingInventory.setText(NumberFormat.getInstance().format(endingInv));
		if (CurrentFiling != null)
			CurrentFiling.ClosingInventory = endingInv.toBigInteger();
		
		BigDecimal difference = startingInv
				.add(CurrentFiling.TotalReceipts)
				.add(CurrentFiling.TotalDisbursements)
				.add(CurrentFiling.TotalAdjustments);
		difference = difference.subtract(endingInv);
		
		// figure net gallons, add everything up, used in TIA04
		//netGallons = startingInv, dont include starting inv
		netGallons = CurrentFiling.TotalReceipts	
				    .add(CurrentFiling.TotalDisbursements.abs())
				    .add(CurrentFiling.TotalAdjustments)
				    .add(endingInv);
				    
		// allow upload once the difference is 0
		if (difference.equals(BigDecimal.valueOf(0))) {
			mntmUploadReturn.setEnabled(true);
		}
		else {
			mntmUploadReturn.setEnabled(false);
		}
			
		
		lblDifference.setText(NumberFormat.getInstance().format(difference));
		
		lblBottomSummary.setText("Summary: " +
				startingInv + " + " +
				CurrentFiling.TotalReceipts + " " +
				CurrentFiling.TotalDisbursements + " " +
				(CurrentFiling.TotalAdjustments.compareTo(BigDecimal.ZERO) >= 0 ? " + " : " ") +
				CurrentFiling.TotalAdjustments + " = " +
				difference);
	}
	
	/**
	 * Called when the user has clicked a checkbox in the Include column of the Receipts panel.
	 * It re-adjusts Receipt totals 
	 * @param The TableModelEvent 
	 */
	private void adjustReceipts(TableModelEvent e) {
		  if ((Boolean)receiptsModel.getValueAt(e.getFirstRow(), e.getColumn())) {
			  //System.out.println("Add          ="+receiptsTable.getModel().getValueAt(e.getFirstRow(), 5));
			  CurrentFiling.TotalReceipts=CurrentFiling.TotalReceipts.add((BigDecimal)receiptsModel.getValueAt(e.getFirstRow(), receiptsModel.getColumnNum("Quantity")));
		  }
		  else {
			  //System.out.println("Subtract     ="+receiptsTable.getModel().getValueAt(e.getFirstRow(), 5));
			  CurrentFiling.TotalReceipts=CurrentFiling.TotalReceipts.subtract((BigDecimal)receiptsModel.getValueAt(e.getFirstRow(), receiptsModel.getColumnNum("Quantity")));
		  }
		 recalcTotals();
	}
	
	/**
	 * Called when the user has clicked a checkbox in the Include column of the Disbursements panel. 
	 * It re-adjusts disbursement totals 
	 * @param The TableModelEvent 
	 */
	private void adjustDisbursements(TableModelEvent e) {
		  if ((Boolean)disbursementsModel.getValueAt(e.getFirstRow(), e.getColumn())) {
			  CurrentFiling.TotalDisbursements=CurrentFiling.TotalDisbursements.add((BigDecimal)disbursementsModel.getValueAt(e.getFirstRow(), disbursementsModel.getColumnNum("Quantity")));
		  }
		  else {
			  CurrentFiling.TotalDisbursements=CurrentFiling.TotalDisbursements.subtract((BigDecimal)disbursementsModel.getValueAt(e.getFirstRow(), disbursementsModel.getColumnNum("Quantity")));
		  }
		  recalcTotals();
	}
	
	/**
	 * Called when the user has selected Upload return. It creates the EDI file 
	 */
	private void createOriginalFiling() {
	   EDICreateOriginalFiling of = new EDICreateOriginalFiling(this);
	   // Create the file name
	   // format is Name_Control_SenderID_transactionSet_FilingPeriod_"T"est or "P"roduction_Qualifier from BTI13 or BTI14.edi
	   // Name Control is first four chars of the Business Name
	   // Sender ID is the FCN number
	   // Transaction set is 813
	   // Filing Period is formatted mmccyy
	   // Use T for test or P when filing Production
	   // BTI13 or BTI14 qualifier
	   String btiQual;
	   if (!of.getBti13().equals(""))
	      btiQual = of.getBti13();
	   else 
		   btiQual = of.getBti14();
	   String fileName = preferences.getSaveDir() + 
	   preferences.getBusinessName().substring(0,4) +
	   "_" +
	   preferences.getFCNcontrolNum() +
	   "_" +
	   "813" +
	   "_" +
	   CurrentFiling.getMMYYYYDate() +
	   "_" +
	   preferences.getTestMode() +
	   "_" +
	   btiQual +
	   ".edi";

	   File file = new File(fileName);
	   boolean fileExists = file.exists();
	   if (fileExists) {
		   int answer = JOptionPane.showConfirmDialog(mainFrame,
				    "<html>File "+fileName+"<br>already exists, over write file?<br>", 
				    "Duplicate File",                  
		            JOptionPane.YES_NO_OPTION,   
		            JOptionPane.QUESTION_MESSAGE);
		  if (answer == JOptionPane.NO_OPTION)                                 
		           return;  /// user does not wish to overwrite                                      
		   
	   }
		   
	   try {
	         BufferedWriter out = new 
	         BufferedWriter(new FileWriter(fileName));
	         out.write(of.getEdiReport());
	         out.close();
	         if (!fileExists) {  // don't show another dialog 
		         JOptionPane.showMessageDialog(mainFrame,
		 				"<html>Filing Report saved as<br>"+fileName+"<br>", 
		 				"Filing Report Save",
		 				JOptionPane.INFORMATION_MESSAGE);
	         }
	 
	      }
	      catch (IOException e) {
	    	  JOptionPane.showMessageDialog(mainFrame,
		 				"<html>Filing Report failed<br>"+e.getMessage()+"<br>", 
		 				"Filing Report Error",
		 				JOptionPane.INFORMATION_MESSAGE);
	      }
	   
	   // save report to object 
	   CurrentFiling.Filings.addElement(of.getEdiReport()); 
	   	   
	   if (CurrentFiling != null && CurrentFiling.UnsavedChanges)
		{
			JOptionPane.showConfirmDialog(mainFrame, "Report has been created but you have not saved the current return", "Please save ", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			doSave(false);
		}
	   else 
		   doSave(true);  // resave the CurrentFiling with the Original Report included
	   
	   // use this to write out segments on individual lines
	   if (DEBUG) {
		   try {
			   String filing = of.getEdiReport();
			   filing = filing.replace("\\", "\\*");
			   String[] ediReport = filing.split("\\*");
			   String tempFile = preferences.getSaveDir()+"tempedi.txt";
			   System.out.println("Save edi tempFile="+tempFile);
			   file = new File(tempFile);
			   if (fileExists) {
				   file.delete();
			   }
			   BufferedWriter outTemp = new BufferedWriter(new FileWriter(tempFile));
			   for (String s: ediReport) {
				  outTemp.write(s+"\n");
			   }
			   outTemp.close();
	       } catch (Exception e) { System.out.println("temp.edi write failed "+e);}   
       }
	}  
	
	public BigDecimal getNetGallons() {
		return netGallons;
	}
	
	private class txtChangeListener implements DocumentListener
	{
		@Override
		public void changedUpdate(DocumentEvent e) {
			UpdateValue(e);
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			UpdateValue(e);
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			UpdateValue(e);
		}
		
		private void UpdateValue(DocumentEvent e)
		{
			recalcTotals();
		}
		
	}
	
	private class btnGroupChangeListener implements ChangeListener
	{
		@Override
		public void stateChanged(ChangeEvent e)
		{
			if (!runListener)
				return;
			if (!rdbtnPreviousFiling.isEnabled())
				return;
			if (rdbtnPreviousFiling.isSelected())
			{
				txtPreviousFilingFile.setEnabled(true);
				txtManualStartInventory.setEnabled(false);
				
				txtManualStartInventory.setText(""); 
				txtPreviousFilingFile.requestFocus();
				
				btnFileBrowse.setEnabled(true);
				CurrentFiling.UsePreviousFiling = true;
			}
			else // Manually Entered is selected
			{
				txtPreviousFilingFile.setEnabled(false);
				txtManualStartInventory.setEnabled(true);
				
				txtPreviousFilingFile.setText("");
				txtManualStartInventory.requestFocus(true);
				
				btnFileBrowse.setEnabled(false);
				CurrentFiling.UsePreviousFiling = false;
			}
			recalcTotals();
		}
	}

	private void doSave(boolean sameFile)
	{
		File saveFile;
		
		// old way, not forcing the .ser at the end
		/*if (!sameFile || CurrentFiling.SaveFile == null)
		{
			if (Chooser == null)
				Chooser = new JFileChooser();
			int returnVal = Chooser.showSaveDialog(mainFrame);
	        if (returnVal != JFileChooser.APPROVE_OPTION)
	        	return;
	        CurrentFiling.SaveFile = saveFile = Chooser.getSelectedFile();
		}
		else
			saveFile = CurrentFiling.SaveFile;*/

		// new way forces the .ser at the end of all saved files
		if (!sameFile || CurrentFiling.SaveFile == null)
		{
			if (Chooser == null) {
				Chooser = new JFileChooser();
				Chooser.setCurrentDirectory(new File(preferences.getSaveDir()));
				Chooser.setFileFilter(new FileNameExtensionFilter("Saved filings", "ser"));
			}	
			int returnVal = Chooser.showSaveDialog(mainFrame);
	        if (returnVal != JFileChooser.APPROVE_OPTION)
	        	return;
	        String file = Chooser.getSelectedFile().toString();
	        
	        if (file.endsWith(".ser"))
	           CurrentFiling.SaveFile = saveFile = Chooser.getSelectedFile();
	        else {
	        	file = file+".ser";
	        	CurrentFiling.SaveFile = saveFile = new File(file);
	        }
		}
		else
			saveFile = CurrentFiling.SaveFile;
		// set UnsavedChanges
		CurrentFiling.UnsavedChanges = false;
		
		try
		{
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(saveFile));
			out.writeObject(CurrentFiling);
			out.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}	
	}
	
	private void doOpen()
	{
		if (CurrentFiling != null && CurrentFiling.UnsavedChanges)
		{
			int rc = JOptionPane.showConfirmDialog(mainFrame, "Current changes not saved, continue with the open?", "Confirm open", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (rc == JOptionPane.NO_OPTION)
				return;
		}
		
		if (Chooser == null) {
			Chooser = new JFileChooser();
			Chooser.setCurrentDirectory(new File(preferences.getSaveDir()));
			Chooser.setFileFilter(new FileNameExtensionFilter("Saved filings", "ser"));
		}	
		int returnVal = Chooser.showOpenDialog(mainFrame);
        if (returnVal != JFileChooser.APPROVE_OPTION)
        	return;
        
        ObjectInputStream in = null;
        try
        {
        	clearCurrentReturn();
        	
            File openFile = Chooser.getSelectedFile();
			in = new ObjectInputStream(new FileInputStream(openFile));
			CurrentFiling = (EDIFiling)in.readObject();
			
			CurrentFiling.MainApp        = this;
			CurrentFiling.UnsavedChanges = false;
			CurrentFiling.SaveFile       = openFile;
						
			filingLoaded();
        }
        catch (FileNotFoundException e)
        {
			e.printStackTrace();
		}
        catch (IOException e)
        {
			e.printStackTrace();
		}
        catch (ClassNotFoundException e)
        {
			e.printStackTrace();
		}
        finally
        {
        	if (in != null)
        	{
				try
        		{
					in.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
        	}
        }
	}
	
	private File fileChooser() {
		JFileChooser Chooser = new JFileChooser();
		Chooser.setCurrentDirectory(new File(preferences.getSaveDir()));
		int returnVal = Chooser.showOpenDialog(mainFrame);
        if (returnVal != JFileChooser.APPROVE_OPTION)
        	return null;
         return Chooser.getSelectedFile();
	}
	
	public void setPreviousFiling(){
		// todo f make sure this does not load the entire previous data
		txtPreviousFilingFile.setEditable(true);
		rdbtnPreviousFiling.setSelected(true);
		txtPreviousFilingFile.setText(CurrentFiling.ClosingInventory.toString());
		
		txtManualStartInventory.setEnabled(false);
		txtManualStartInventory.setText("");
				
		recalcTotals();
	}
	
	/**
	 * Call this to verify a 997 Acknownledgement to an EDI Filing
	 * @param The EDI filing to use for the compare
	 * @return
	 */
	public int doVerify997Ack(File file) {	
	   int rc = 0;	
	   
	   // set  the 997 acknowledgement
	   Verify997Ack verify997 = new Verify997Ack(this);
	   rc = verify997.set997Ack(file);
	   if (rc != 0) 
		   return rc;
	   
	   // last element in Filings is the report needing verify
	   rc = verify997.setFiling(CurrentFiling.Filings.lastElement());
	   if (rc != 0) 
		   return rc;
	   
	   rc = verify997.startVerify();
	   return rc;
	}  
	
	/**
	 * Call this to verify a 151 Acknownledgement to an EDI Filing
	 * @param The EDI filing to use for the compare
	 * @return
	 */
	public int doVerify151Ack(File file) {	
		// TODO: make sure 997 has been verified first
	   int rc = 0;	
	   
	   // set  the 151 acknowledgement
	   Verify151Ack verify151 = new Verify151Ack(this);
	   rc = verify151.set151Ack(file);
	   if (rc != 0) 
		   return rc;
	   
	   // last element in Filings is the report needing verify
	   rc = verify151.setFiling(CurrentFiling.Filings.lastElement());
	   if (rc != 0) 
		   return rc;
	   
	   rc = verify151.startVerify();
	   return rc;
	}   
}
