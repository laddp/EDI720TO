package com.bottinifuel.edi720to;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Vector;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;

import com.bottinifuel.edi720to.util.Abbreviation;
import com.bottinifuel.edi720to.util.CarrierTableModel;
import com.bottinifuel.edi720to.util.Util;
//import javax.swing.JList;
//import javax.swing.ListSelectionModel;
//import com.bottinifuel.edi720to.util.TransactionTableModel;
//import javax.swing.event.ListSelectionEvent;

public class Preferences extends JDialog {

	private static final long serialVersionUID = 1953598307400723093L;

	private final EDI720TO MainApp;
	
	private java.util.prefs.Preferences prefsStore = java.util.prefs.Preferences.userNodeForPackage(this.getClass());
	private static final String TAG_PREF_COUNTRY = "Country";
	private static final String TAG_PREF_ZIP = "Zip";
	private static final String TAG_PREF_STATE = "State";
	private static final String TAG_PREF_CITY = "City";
	private static final String TAG_PREF_MAILING_ADDR2 = "MailingAddr2";
	private static final String TAG_PREF_MAILING_ADDR1 = "MailingAddr1";
	private static final String TAG_PREF_BUSINESS_NAME = "BusinessName";
	private static final String TAG_PREF_637ID = "637ID";
	private static final String TAG_PREF_EIN = "EIN";
	private static final String TAG_PREF_TEST_MODE = "TestMode";
	private static final String TAG_PREF_COPY_APPLICATION_FROM_EIN = "CopyApplicationFromEIN";
	private static final String TAG_PREF_APPLICATION_ID = "ApplicationID";
	private static final String TAG_PREF_COPY_INTERCHANGE_FROM_EIN = "CopyInterchangeFromEIN";
	private static final String TAG_PREF_INTERCHANGE_ID = "InterchangeID";
	private static final String TAG_PREF_SECURITY_CODE = "SecurityCode";
	private static final String TAG_PREF_AUTH_CODE = "AuthCode";
	private static final String TAG_PREF_GENERAL_EMAIL = "GeneralEmail";
	private static final String TAG_PREF_GENERAL_FAX = "GeneralFax";
	private static final String TAG_PREF_GENERAL_TELEPHONE = "GeneralTelephone";
	private static final String TAG_PREF_GENERAL_NAME = "GeneralName";
	private static final String TAG_PREF_EDI_EMAIL = "EDIemail";
	private static final String TAG_PREF_EDI_FAX = "EDIfax";
	private static final String TAG_PREF_EDI_TELEPHONE = "EDItelephone";
	private static final String TAG_PREF_EDI_NAME = "EDIname";
	private static final String TAG_PREF_DBSERVER = "DBserver";
	private static final String TAG_PREF_DBPORT = "DBport";
	private static final String TAG_PREF_DBUSER = "DBuser";
	private static final String TAG_PREF_DBPASSWORD = "DBpassword";
	private static final String TAG_PREF_DBNUMBER = "DBnumber";
	private static final String TAG_PREF_FCN_NAME = "FCNname";
	private static final String TAG_PREF_FCN_NUM = "FCNnum";
	private static final String TAG_PREF_FCN_STATE = "FCNstate";
	private static final String TAG_SAVE_DIR = "SaveDir";
	private static final String TAG_CARRIER = "Carrier";
	
	//**************************
	// Fields for taxpayer frame
	//**************************
	private String EIN;
	private String Id637; 
	private String BusinessName;
	private String MailingAddr1;
	private String MailingAddr2;
	private String City;
	private String State;
	private String Zip;
	private String Country;

	private JFormattedTextField txtEIN;
	private JFormattedTextField txtId637;
	private JTextField txtBusinessName;
	private JTextField txtMailingAddr1;
	private JTextField txtMailingAddr2;
	private JTextField txtCity;
		
	private JComboBox<Abbreviation> comboState;
	private JFormattedTextField     txtZip;
	private JComboBox<Abbreviation> comboCountry;

	//************************
	// Fields for Sender frame
	//************************
	private String  AuthCode;
	private String  SecurityCode;
	private String  InterchageID;
	private boolean CopyInterchangeFromEIN;
	private String  ApplicationID;
	private boolean CopyApplicationFromEIN;
	private boolean TestMode;

	private JFormattedTextField txtAuthCode;
	private JFormattedTextField txtSecurityCode;
	private JFormattedTextField txtInterchangeID;
	private JCheckBox  chkCopyInterchangeFromEIN;
	private JFormattedTextField txtAppplicationID;
	private JCheckBox  chkCopyApplicationFromEIN;
	private JCheckBox  chkTestMode;

	//*********************************
	// Fields for General Contact Frame	
	//*********************************
	private String GeneralName;
	private String GeneralTelephone;
	private String GeneralFax;
	private String GeneralEmail;

	private JTextField txtGeneralName;
	private JFormattedTextField txtGeneralTelephone;
	private JFormattedTextField txtGeneralFax;
	private JFormattedTextField txtGeneralEmail;

	//*****************************
	// Fields for EDI Contact Frame
    //*****************************
	private String EDIname;
	private String EDItelephone;
	private String EDIfax;
	private String EDIemail;

	private JTextField txtEDIname;
	private JFormattedTextField txtEDItelephone;
	private JFormattedTextField txtEDIfax;
	private JFormattedTextField txtEDIemail;

	private JTable carrierTable;
	private JButton btnAddCarrier;
	private JButton btnRemoveCarrier;

	private JScrollPane scrollPane;

	//*****************************
	// Fields for Database frame
	//*****************************
	private String DBserver;
	private int    DBport;
	private String DBuser;
	private String DBpassword;
	private int    DBnumber;
	
	//****************************
	// fields for Save Directory
	//****************************
	private JTextField txtSaveDir;
	String saveDir;
	
	private JFormattedTextField     txtDBserver;
	private JFormattedTextField     txtDBport;
	private JFormattedTextField     txtDBuser;
	private JPasswordField          txtDBpassword;
	private JFormattedTextField     txtDBnumber;
	
	//***********************************************
	// Fields for Terminal Control Information (FCN)
	//***********************************************
	private String FCNname;  // Terminal FCN Name 
	private String FCNnum;   // FCN Control Number;
	private String FCNstate; // State of FCN 
	
	private JTextField txtFCNname;   
	private JTextField txtFCNnum;    
	private JTextField txtFCNstate;  
	
	//**********************
	// Fields for Carrier
	//**********************
	private String            carrierString;
	private CarrierTableModel carrierModel;
	private JComboBox<String> jcbCarrierList;
	//private int               selectedRow;
	private Vector<Carrier>   carrierTableV = new Vector<Carrier>(); // vector for carrier table
			
	/**
	 * Create the dialog.
	 */
	/**
	 * @param app
	 */
	public Preferences(EDI720TO app) {
		MainApp = app;
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		//(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setTitle("Preferences");
		setBounds(100, 100, 561, 398);
		getContentPane().setLayout(new BorderLayout());

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.LEFT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnImport = new JButton("Import...");
				btnImport.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent arg0)
					{
						JFileChooser chooser = new JFileChooser();
						chooser.setApproveButtonText("Import");
						chooser.setApproveButtonMnemonic('I');
						chooser.setDialogTitle("Import Preferences");
						chooser.setCurrentDirectory(new File(getSaveDir()));
						//chooser.setFileFilter(new FileNameExtensionFilter("Saved Preferences", "per"));
						int rc = chooser.showOpenDialog(rootPane);
						if (rc == JFileChooser.APPROVE_OPTION)
						{
							File f = chooser.getSelectedFile();
							try {
								java.util.prefs.Preferences.importPreferences(new FileInputStream(f));
							} catch (FileNotFoundException e) {
								JOptionPane.showMessageDialog(rootPane, 
										"File not found: " + f.getAbsolutePath() + " - " + e.getMessage(),
										"File not found",
										JOptionPane.ERROR_MESSAGE);
							} catch (IOException e) {
								JOptionPane.showMessageDialog(rootPane, 
										"Error while reading file: " + f.getAbsolutePath() + " - " + e.getMessage(),
										"Error reading file",
										JOptionPane.ERROR_MESSAGE);
							} catch (InvalidPreferencesFormatException e) {
								JOptionPane.showMessageDialog(rootPane, 
										"Invalid file format: " + f.getAbsolutePath() + " - " + e.getMessage(),
										"Invalid file format",
										JOptionPane.ERROR_MESSAGE);
							}
							loadPreferencesFromStore();
						}
					}
				});
				buttonPane.add(btnImport);
			}
			{
				JButton btnExport = new JButton("Export...");
				btnExport.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent arg0)
					{
						JFileChooser chooser = new JFileChooser();
						chooser.setApproveButtonText("Export");
						chooser.setApproveButtonMnemonic('E');
						chooser.setDialogTitle("Export Preferences");
						chooser.setCurrentDirectory(new File(getSaveDir()));
						int rc = chooser.showOpenDialog(rootPane);
						if (rc == JFileChooser.APPROVE_OPTION)
						{
							File f = chooser.getSelectedFile();
							try {
								prefsStore.exportSubtree(new FileOutputStream(f));
							} catch (FileNotFoundException e) {
								JOptionPane.showMessageDialog(rootPane, 
										"File not found: " + f.getAbsolutePath() + " - " + e.getMessage(),
										"File not found",
										JOptionPane.ERROR_MESSAGE);
							}
							catch (IOException e)
							{
								JOptionPane.showMessageDialog(rootPane, 
										"Error while writing file: " + f.getAbsolutePath() + " - " + e.getMessage(),
										"Error writing file",
										JOptionPane.ERROR_MESSAGE);
							} catch (BackingStoreException e) {
								JOptionPane.showMessageDialog(rootPane, 
										"Error extracting preferences" + e.getMessage(),
										"Error extracting preferences",
										JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				});
				buttonPane.add(btnExport);
			}
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						doOk();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						doCancel();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		{
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			getContentPane().add(tabbedPane, BorderLayout.CENTER);
			{
				JPanel taxIDpanel = new JPanel();
				tabbedPane.addTab("Taxpayer ID", null, taxIDpanel, null);
				FlowLayout flowLayout = (FlowLayout) taxIDpanel.getLayout();
				flowLayout.setAlignment(FlowLayout.LEFT);
				{
					JPanel taxIDcontentPanel = new JPanel();
					taxIDpanel.add(taxIDcontentPanel);
					GridBagLayout gbl_taxIDcontentPanel = new GridBagLayout();
					gbl_taxIDcontentPanel.columnWidths = new int[]{0, 300, 0};
					gbl_taxIDcontentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
					gbl_taxIDcontentPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
					gbl_taxIDcontentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
					taxIDcontentPanel.setLayout(gbl_taxIDcontentPanel);
					JLabel lblEIN = new JLabel("EIN");
					lblEIN.setDisplayedMnemonic('E');
					GridBagConstraints gbc_lblEIN = new GridBagConstraints();
					gbc_lblEIN.anchor = GridBagConstraints.EAST;
					gbc_lblEIN.insets = new Insets(0, 0, 5, 5);
					gbc_lblEIN.gridx = 0;
					gbc_lblEIN.gridy = 0;
					taxIDcontentPanel.add(lblEIN, gbc_lblEIN);
					txtEIN = new JFormattedTextField(Util.EINFormatter);
					lblEIN.setLabelFor(txtEIN);
					txtEIN.setColumns(10);
					GridBagConstraints gbc_txtEIN = new GridBagConstraints();
					gbc_txtEIN.anchor = GridBagConstraints.WEST;
					gbc_txtEIN.insets = new Insets(0, 0, 5, 0);
					gbc_txtEIN.gridx = 1;
					gbc_txtEIN.gridy = 0;
					taxIDcontentPanel.add(txtEIN, gbc_txtEIN);
					JLabel lbl637id = new JLabel("637 ID");
					lbl637id.setDisplayedMnemonic('6');
					GridBagConstraints gbc_lbl637id = new GridBagConstraints();
					gbc_lbl637id.anchor = GridBagConstraints.EAST;
					gbc_lbl637id.insets = new Insets(0, 0, 5, 5);
					gbc_lbl637id.gridx = 0;
					gbc_lbl637id.gridy = 1;
					taxIDcontentPanel.add(lbl637id, gbc_lbl637id);
					try {
						txtId637 = new JFormattedTextField(new MaskFormatter("##-UU-####-######"));
					} catch (ParseException e) {
					}
					lbl637id.setLabelFor(txtId637);
					GridBagConstraints gbc_txt637ID = new GridBagConstraints();
					gbc_txt637ID.insets = new Insets(0, 0, 5, 0);
					gbc_txt637ID.anchor = GridBagConstraints.WEST;
					gbc_txt637ID.gridx = 1;
					gbc_txt637ID.gridy = 1;
					taxIDcontentPanel.add(txtId637, gbc_txt637ID);
					txtId637.setColumns(20);
					JLabel lblBusinessName = new JLabel("Business Name");
					lblBusinessName.setDisplayedMnemonic('B');
					GridBagConstraints gbc_lblBusinessName = new GridBagConstraints();
					gbc_lblBusinessName.anchor = GridBagConstraints.EAST;
					gbc_lblBusinessName.insets = new Insets(0, 0, 5, 5);
					gbc_lblBusinessName.gridx = 0;
					gbc_lblBusinessName.gridy = 2;
					taxIDcontentPanel.add(lblBusinessName, gbc_lblBusinessName);
					txtBusinessName = new JTextField();
					lblBusinessName.setLabelFor(txtBusinessName);
					GridBagConstraints gbc_txtBusinessName = new GridBagConstraints();
					gbc_txtBusinessName.anchor = GridBagConstraints.WEST;
					gbc_txtBusinessName.insets = new Insets(0, 0, 5, 0);
					gbc_txtBusinessName.gridx = 1;
					gbc_txtBusinessName.gridy = 2;
					taxIDcontentPanel.add(txtBusinessName, gbc_txtBusinessName);
					txtBusinessName.setColumns(35);
					JLabel lblMailingAddr1 = new JLabel("Mailing Address Line 1");
					lblMailingAddr1.setDisplayedMnemonic('M');
					GridBagConstraints gbc_lblMailingAddr1 = new GridBagConstraints();
					gbc_lblMailingAddr1.anchor = GridBagConstraints.EAST;
					gbc_lblMailingAddr1.insets = new Insets(0, 0, 5, 5);
					gbc_lblMailingAddr1.gridx = 0;
					gbc_lblMailingAddr1.gridy = 3;
					taxIDcontentPanel.add(lblMailingAddr1, gbc_lblMailingAddr1);
					txtMailingAddr1 = new JTextField();
					lblMailingAddr1.setLabelFor(txtMailingAddr1);
					GridBagConstraints gbc_txtMailingAddr1 = new GridBagConstraints();
					gbc_txtMailingAddr1.anchor = GridBagConstraints.WEST;
					gbc_txtMailingAddr1.insets = new Insets(0, 0, 5, 0);
					gbc_txtMailingAddr1.gridx = 1;
					gbc_txtMailingAddr1.gridy = 3;
					taxIDcontentPanel.add(txtMailingAddr1, gbc_txtMailingAddr1);
					txtMailingAddr1.setColumns(35);
					JLabel lblMailingAddr2 = new JLabel("Mailing Address Line 2");
					lblMailingAddr2.setDisplayedMnemonic('2');
					GridBagConstraints gbc_lblMailingAddr2 = new GridBagConstraints();
					gbc_lblMailingAddr2.anchor = GridBagConstraints.EAST;
					gbc_lblMailingAddr2.insets = new Insets(0, 0, 5, 5);
					gbc_lblMailingAddr2.gridx = 0;
					gbc_lblMailingAddr2.gridy = 4;
					taxIDcontentPanel.add(lblMailingAddr2, gbc_lblMailingAddr2);
					txtMailingAddr2 = new JTextField();
					lblMailingAddr2.setLabelFor(txtMailingAddr2);
					GridBagConstraints gbc_txtMailingAddr2 = new GridBagConstraints();
					gbc_txtMailingAddr2.anchor = GridBagConstraints.WEST;
					gbc_txtMailingAddr2.insets = new Insets(0, 0, 5, 0);
					gbc_txtMailingAddr2.gridx = 1;
					gbc_txtMailingAddr2.gridy = 4;
					taxIDcontentPanel.add(txtMailingAddr2, gbc_txtMailingAddr2);
					txtMailingAddr2.setColumns(35);
					JLabel lblCity = new JLabel("City");
					lblCity.setDisplayedMnemonic('C');
					GridBagConstraints gbc_lblCity = new GridBagConstraints();
					gbc_lblCity.anchor = GridBagConstraints.EAST;
					gbc_lblCity.insets = new Insets(0, 0, 5, 5);
					gbc_lblCity.gridx = 0;
					gbc_lblCity.gridy = 5;
					taxIDcontentPanel.add(lblCity, gbc_lblCity);
					txtCity = new JTextField();
					lblCity.setLabelFor(txtCity);
					GridBagConstraints gbc_txtCity = new GridBagConstraints();
					gbc_txtCity.anchor = GridBagConstraints.WEST;
					gbc_txtCity.insets = new Insets(0, 0, 5, 0);
					gbc_txtCity.gridx = 1;
					gbc_txtCity.gridy = 5;
					taxIDcontentPanel.add(txtCity, gbc_txtCity);
					txtCity.setColumns(30);
					JLabel lblState = new JLabel("State");
					lblState.setDisplayedMnemonic('S');
					GridBagConstraints gbc_lblState = new GridBagConstraints();
					gbc_lblState.anchor = GridBagConstraints.EAST;
					gbc_lblState.insets = new Insets(0, 0, 5, 5);
					gbc_lblState.gridx = 0;
					gbc_lblState.gridy = 6;
					taxIDcontentPanel.add(lblState, gbc_lblState);
					comboState = new JComboBox<Abbreviation>(Util.USstateAbbrevs);
					lblState.setLabelFor(comboState);
					GridBagConstraints gbc_comboState = new GridBagConstraints();
					gbc_comboState.anchor = GridBagConstraints.WEST;
					gbc_comboState.insets = new Insets(0, 0, 5, 0);
					gbc_comboState.gridx = 1;
					gbc_comboState.gridy = 6;
					taxIDcontentPanel.add(comboState, gbc_comboState);
					JLabel lblZipCode = new JLabel("Zip Code");
					lblZipCode.setDisplayedMnemonic('Z');
					GridBagConstraints gbc_lblZipCode = new GridBagConstraints();
					gbc_lblZipCode.anchor = GridBagConstraints.EAST;
					gbc_lblZipCode.insets = new Insets(0, 0, 5, 5);
					gbc_lblZipCode.gridx = 0;
					gbc_lblZipCode.gridy = 7;
					taxIDcontentPanel.add(lblZipCode, gbc_lblZipCode);
					try {
						txtZip = new JFormattedTextField(new MaskFormatter("#####"));
					} catch (ParseException e) {
					}
					lblZipCode.setLabelFor(txtZip);
					GridBagConstraints gbc_txtZip = new GridBagConstraints();
					gbc_txtZip.anchor = GridBagConstraints.WEST;
					gbc_txtZip.insets = new Insets(0, 0, 5, 0);
					gbc_txtZip.gridx = 1;
					gbc_txtZip.gridy = 7;
					taxIDcontentPanel.add(txtZip, gbc_txtZip);
					txtZip.setColumns(11);
					JLabel lblCountry = new JLabel("Country");
					lblCountry.setDisplayedMnemonic('C');
					GridBagConstraints gbc_lblCountry = new GridBagConstraints();
					gbc_lblCountry.anchor = GridBagConstraints.EAST;
					gbc_lblCountry.insets = new Insets(0, 0, 0, 5);
					gbc_lblCountry.gridx = 0;
					gbc_lblCountry.gridy = 8;
					taxIDcontentPanel.add(lblCountry, gbc_lblCountry);
					comboCountry = new JComboBox<Abbreviation>(Util.CountryAbbrevs);
					lblCountry.setLabelFor(comboCountry);
					GridBagConstraints gbc_comboCountry = new GridBagConstraints();
					gbc_comboCountry.anchor = GridBagConstraints.WEST;
					gbc_comboCountry.gridx = 1;
					gbc_comboCountry.gridy = 8;
					taxIDcontentPanel.add(comboCountry, gbc_comboCountry);
				}
			}
			{
				JPanel senderPanel = new JPanel();
				tabbedPane.addTab("Sender ID", null, senderPanel, null);
				senderPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
				{
					JPanel senderContentPanel = new JPanel();
					senderPanel.add(senderContentPanel);
					GridBagLayout gbl_senderContentPanel = new GridBagLayout();
					gbl_senderContentPanel.columnWidths = new int[]{0, 0, 0, 0};
					gbl_senderContentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
					gbl_senderContentPanel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
					gbl_senderContentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
					senderContentPanel.setLayout(gbl_senderContentPanel);
					JLabel lblAuthCode = new JLabel("Authorization Code");
					lblAuthCode.setDisplayedMnemonic('A');
					GridBagConstraints gbc_lblAuthCode = new GridBagConstraints();
					gbc_lblAuthCode.anchor = GridBagConstraints.EAST;
					gbc_lblAuthCode.insets = new Insets(0, 0, 5, 5);
					gbc_lblAuthCode.gridx = 0;
					gbc_lblAuthCode.gridy = 0;
					senderContentPanel.add(lblAuthCode, gbc_lblAuthCode);
					try {
						txtAuthCode = new JFormattedTextField(new MaskFormatter("AAAAAAAAAA"));
					} catch (ParseException e1) {
					}
					lblAuthCode.setLabelFor(txtAuthCode);
					txtAuthCode.setColumns(10);
					GridBagConstraints gbc_authCode = new GridBagConstraints();
					gbc_authCode.anchor = GridBagConstraints.WEST;
					gbc_authCode.insets = new Insets(0, 0, 5, 5);
					gbc_authCode.gridx = 1;
					gbc_authCode.gridy = 0;
					senderContentPanel.add(txtAuthCode, gbc_authCode);
					JLabel lblSecurityCode = new JLabel("Security Code");
					lblSecurityCode.setDisplayedMnemonic('S');
					GridBagConstraints gbc_lblSecurityCode = new GridBagConstraints();
					gbc_lblSecurityCode.anchor = GridBagConstraints.EAST;
					gbc_lblSecurityCode.insets = new Insets(0, 0, 5, 5);
					gbc_lblSecurityCode.gridx = 0;
					gbc_lblSecurityCode.gridy = 1;
					senderContentPanel.add(lblSecurityCode, gbc_lblSecurityCode);
					try {
						txtSecurityCode = new JFormattedTextField(new MaskFormatter("AAAAAAAAAA"));
					} catch (ParseException e1) {
					}
					lblSecurityCode.setLabelFor(txtSecurityCode);
					txtSecurityCode.setColumns(10);
					GridBagConstraints gbc_securityCode = new GridBagConstraints();
					gbc_securityCode.anchor = GridBagConstraints.WEST;
					gbc_securityCode.insets = new Insets(0, 0, 5, 5);
					gbc_securityCode.gridx = 1;
					gbc_securityCode.gridy = 1;
					senderContentPanel.add(txtSecurityCode, gbc_securityCode);
					JLabel lblInterchangeID = new JLabel("Interchange Sender ID");
					lblInterchangeID.setDisplayedMnemonic('I');
					GridBagConstraints gbc_lblInterchangeID = new GridBagConstraints();
					gbc_lblInterchangeID.anchor = GridBagConstraints.EAST;
					gbc_lblInterchangeID.insets = new Insets(0, 0, 5, 5);
					gbc_lblInterchangeID.gridx = 0;
					gbc_lblInterchangeID.gridy = 2;
					senderContentPanel.add(lblInterchangeID, gbc_lblInterchangeID);
					txtInterchangeID = new JFormattedTextField(Util.EINFormatter);
					lblInterchangeID.setLabelFor(txtInterchangeID);
					txtInterchangeID.setColumns(15);
					GridBagConstraints gbc_InterchangeID = new GridBagConstraints();
					gbc_InterchangeID.anchor = GridBagConstraints.WEST;
					gbc_InterchangeID.insets = new Insets(0, 0, 5, 5);
					gbc_InterchangeID.gridx = 1;
					gbc_InterchangeID.gridy = 2;
					senderContentPanel.add(txtInterchangeID, gbc_InterchangeID);
					chkCopyInterchangeFromEIN = new JCheckBox("Copy from EIN");
					chkCopyInterchangeFromEIN.addChangeListener(new ChangeListener() {
						public void stateChanged(ChangeEvent arg0) {
							if (chkCopyInterchangeFromEIN.isSelected()) {
								txtInterchangeID.setText(getEIN());
								txtInterchangeID.setEnabled(false);
							}
							else {
								txtInterchangeID.setText("");
								txtInterchangeID.setEnabled(true);
							}	
						}
					});
					GridBagConstraints gbc_btnCopyInterchangeFromEIN = new GridBagConstraints();
					gbc_btnCopyInterchangeFromEIN.insets = new Insets(0, 0, 5, 0);
					gbc_btnCopyInterchangeFromEIN.gridx = 2;
					gbc_btnCopyInterchangeFromEIN.gridy = 2;
					senderContentPanel.add(chkCopyInterchangeFromEIN, gbc_btnCopyInterchangeFromEIN);
					JLabel lblApplicationID = new JLabel("Application Sender ID");
					lblApplicationID.setDisplayedMnemonic('I');
					GridBagConstraints gbc_lblApplicationID = new GridBagConstraints();
					gbc_lblApplicationID.anchor = GridBagConstraints.EAST;
					gbc_lblApplicationID.insets = new Insets(0, 0, 5, 5);
					gbc_lblApplicationID.gridx = 0;
					gbc_lblApplicationID.gridy = 3;
					senderContentPanel.add(lblApplicationID, gbc_lblApplicationID);
					txtAppplicationID = new JFormattedTextField(Util.EINFormatter);
					lblApplicationID.setLabelFor(txtAppplicationID);
					txtAppplicationID.setColumns(15);
					GridBagConstraints gbc_AppplicationID = new GridBagConstraints();
					gbc_AppplicationID.anchor = GridBagConstraints.WEST;
					gbc_AppplicationID.insets = new Insets(0, 0, 5, 5);
					gbc_AppplicationID.gridx = 1;
					gbc_AppplicationID.gridy = 3;
					senderContentPanel.add(txtAppplicationID, gbc_AppplicationID);
					chkCopyApplicationFromEIN = new JCheckBox("Copy from EIN");
					chkCopyApplicationFromEIN.addChangeListener(new ChangeListener() {
						public void stateChanged(ChangeEvent e) {
							if (chkCopyApplicationFromEIN.isSelected()) {
								txtAppplicationID.setText(getEIN());
								txtAppplicationID.setEnabled(false);
							}	
							else {
								txtAppplicationID.setText("");
								txtAppplicationID.setEnabled(true);
							}	
						}
					});
					GridBagConstraints gbc_lblCopyApplicationFromEIN = new GridBagConstraints();
					gbc_lblCopyApplicationFromEIN.insets = new Insets(0, 0, 5, 0);
					gbc_lblCopyApplicationFromEIN.gridx = 2;
					gbc_lblCopyApplicationFromEIN.gridy = 3;
					senderContentPanel.add(chkCopyApplicationFromEIN, gbc_lblCopyApplicationFromEIN);
					JLabel lblTestMode = new JLabel("Test mode?");
					lblTestMode.setDisplayedMnemonic('T');
					GridBagConstraints gbc_lblTestMode = new GridBagConstraints();
					gbc_lblTestMode.anchor = GridBagConstraints.EAST;
					gbc_lblTestMode.insets = new Insets(0, 0, 0, 5);
					gbc_lblTestMode.gridx = 0;
					gbc_lblTestMode.gridy = 4;
					senderContentPanel.add(lblTestMode, gbc_lblTestMode);
					chkTestMode = new JCheckBox("");
					lblTestMode.setLabelFor(chkTestMode);
					chkTestMode.setHorizontalAlignment(SwingConstants.LEFT);
					GridBagConstraints gbc_testMode = new GridBagConstraints();
					gbc_testMode.anchor = GridBagConstraints.WEST;
					gbc_testMode.insets = new Insets(0, 0, 0, 5);
					gbc_testMode.gridx = 1;
					gbc_testMode.gridy = 4;
					senderContentPanel.add(chkTestMode, gbc_testMode);
				}
			}
			{
				JPanel generalContactPanel = new JPanel();
				FlowLayout flowLayout = (FlowLayout) generalContactPanel.getLayout();
				flowLayout.setAlignment(FlowLayout.LEFT);
				tabbedPane.addTab("General Contact", null, generalContactPanel, null);
				{
					JPanel generalContactContentPanel = new JPanel();
					generalContactPanel.add(generalContactContentPanel);
					GridBagLayout gbl_generalContactContentPanel = new GridBagLayout();
					gbl_generalContactContentPanel.columnWidths = new int[]{0, 0, 0};
					gbl_generalContactContentPanel.rowHeights = new int[]{0, 0, 0, 0, 0};
					gbl_generalContactContentPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
					gbl_generalContactContentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
					generalContactContentPanel.setLayout(gbl_generalContactContentPanel);
					JLabel lblGeneralName = new JLabel("Name");
					lblGeneralName.setDisplayedMnemonic('N');
					GridBagConstraints gbc_lblGeneralName = new GridBagConstraints();
					gbc_lblGeneralName.anchor = GridBagConstraints.EAST;
					gbc_lblGeneralName.insets = new Insets(0, 0, 5, 5);
					gbc_lblGeneralName.gridx = 0;
					gbc_lblGeneralName.gridy = 0;
					generalContactContentPanel.add(lblGeneralName, gbc_lblGeneralName);
					txtGeneralName = new JTextField();
					lblGeneralName.setLabelFor(txtGeneralName);
					GridBagConstraints gbc_txtGeneralName = new GridBagConstraints();
					gbc_txtGeneralName.anchor = GridBagConstraints.WEST;
					gbc_txtGeneralName.insets = new Insets(0, 0, 5, 0);
					gbc_txtGeneralName.gridx = 1;
					gbc_txtGeneralName.gridy = 0;
					generalContactContentPanel.add(txtGeneralName, gbc_txtGeneralName);
					txtGeneralName.setColumns(35);
					JLabel lblGeneralTelephone = new JLabel("Telephone Number");
					lblGeneralTelephone.setDisplayedMnemonic('T');
					GridBagConstraints gbc_lblGeneralTelephone = new GridBagConstraints();
					gbc_lblGeneralTelephone.anchor = GridBagConstraints.EAST;
					gbc_lblGeneralTelephone.insets = new Insets(0, 0, 5, 5);
					gbc_lblGeneralTelephone.gridx = 0;
					gbc_lblGeneralTelephone.gridy = 1;
					generalContactContentPanel.add(lblGeneralTelephone, gbc_lblGeneralTelephone);
					txtGeneralTelephone = new JFormattedTextField(Util.TelephoneFormatter);
					lblGeneralTelephone.setLabelFor(txtGeneralTelephone);
					GridBagConstraints gbc_txtGeneralTelephone = new GridBagConstraints();
					gbc_txtGeneralTelephone.anchor = GridBagConstraints.WEST;
					gbc_txtGeneralTelephone.insets = new Insets(0, 0, 5, 0);
					gbc_txtGeneralTelephone.gridx = 1;
					gbc_txtGeneralTelephone.gridy = 1;
					generalContactContentPanel.add(txtGeneralTelephone, gbc_txtGeneralTelephone);
					txtGeneralTelephone.setColumns(14);
					JLabel lblGeneralFax = new JLabel("Fax Number");
					lblGeneralFax.setDisplayedMnemonic('F');
					GridBagConstraints gbc_lblGeneralFax = new GridBagConstraints();
					gbc_lblGeneralFax.anchor = GridBagConstraints.EAST;
					gbc_lblGeneralFax.insets = new Insets(0, 0, 5, 5);
					gbc_lblGeneralFax.gridx = 0;
					gbc_lblGeneralFax.gridy = 2;
					generalContactContentPanel.add(lblGeneralFax, gbc_lblGeneralFax);
					txtGeneralFax = new JFormattedTextField(Util.TelephoneFormatter);
					lblGeneralFax.setLabelFor(txtGeneralFax);
					GridBagConstraints gbc_txtGeneralFax = new GridBagConstraints();
					gbc_txtGeneralFax.anchor = GridBagConstraints.WEST;
					gbc_txtGeneralFax.insets = new Insets(0, 0, 5, 0);
					gbc_txtGeneralFax.gridx = 1;
					gbc_txtGeneralFax.gridy = 2;
					generalContactContentPanel.add(txtGeneralFax, gbc_txtGeneralFax);
					txtGeneralFax.setColumns(10);
					JLabel lblGeneralEmail = new JLabel("Email Address");
					lblGeneralEmail.setDisplayedMnemonic('E');
					GridBagConstraints gbc_lblGeneralEmail = new GridBagConstraints();
					gbc_lblGeneralEmail.anchor = GridBagConstraints.EAST;
					gbc_lblGeneralEmail.insets = new Insets(0, 0, 0, 5);
					gbc_lblGeneralEmail.gridx = 0;
					gbc_lblGeneralEmail.gridy = 3;
					generalContactContentPanel.add(lblGeneralEmail, gbc_lblGeneralEmail);
					txtGeneralEmail = new JFormattedTextField();
					lblGeneralEmail.setLabelFor(txtGeneralEmail);
					GridBagConstraints gbc_txtGeneralEmail = new GridBagConstraints();
					gbc_txtGeneralEmail.anchor = GridBagConstraints.WEST;
					gbc_txtGeneralEmail.gridx = 1;
					gbc_txtGeneralEmail.gridy = 3;
					generalContactContentPanel.add(txtGeneralEmail, gbc_txtGeneralEmail);
					txtGeneralEmail.setColumns(40);
				}
			}
			{
				JPanel EDIcoordinatorPanel = new JPanel();
				FlowLayout flowLayout = (FlowLayout) EDIcoordinatorPanel.getLayout();
				flowLayout.setAlignment(FlowLayout.LEFT);
				tabbedPane.addTab("EDI Coordinator", null, EDIcoordinatorPanel, null);
				{
					JPanel EDIcoordinatorContentPanel = new JPanel();
					EDIcoordinatorPanel.add(EDIcoordinatorContentPanel);
					GridBagLayout gbl_EDIcoordinatorContentPanel = new GridBagLayout();
					gbl_EDIcoordinatorContentPanel.columnWidths = new int[]{0, 0, 0};
					gbl_EDIcoordinatorContentPanel.rowHeights = new int[]{0, 0, 0, 0, 0};
					gbl_EDIcoordinatorContentPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
					gbl_EDIcoordinatorContentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
					EDIcoordinatorContentPanel.setLayout(gbl_EDIcoordinatorContentPanel);
					JLabel lblEDIname = new JLabel("Name");
					lblEDIname.setDisplayedMnemonic('N');
					GridBagConstraints gbc_lblEDIname = new GridBagConstraints();
					gbc_lblEDIname.anchor = GridBagConstraints.EAST;
					gbc_lblEDIname.insets = new Insets(0, 0, 5, 5);
					gbc_lblEDIname.gridx = 0;
					gbc_lblEDIname.gridy = 0;
					EDIcoordinatorContentPanel.add(lblEDIname, gbc_lblEDIname);
					txtEDIname = new JTextField();
					lblEDIname.setLabelFor(txtEDIname);
					txtEDIname.setColumns(35);
					GridBagConstraints gbc_txtEDIname = new GridBagConstraints();
					gbc_txtEDIname.anchor = GridBagConstraints.WEST;
					gbc_txtEDIname.insets = new Insets(0, 0, 5, 0);
					gbc_txtEDIname.gridx = 1;
					gbc_txtEDIname.gridy = 0;
					EDIcoordinatorContentPanel.add(txtEDIname, gbc_txtEDIname);
					JLabel lblEDItelephone = new JLabel("Telephone Number");
					lblEDItelephone.setDisplayedMnemonic('T');
					GridBagConstraints gbc_lblEDItelephone = new GridBagConstraints();
					gbc_lblEDItelephone.anchor = GridBagConstraints.EAST;
					gbc_lblEDItelephone.insets = new Insets(0, 0, 5, 5);
					gbc_lblEDItelephone.gridx = 0;
					gbc_lblEDItelephone.gridy = 1;
					EDIcoordinatorContentPanel.add(lblEDItelephone, gbc_lblEDItelephone);
					txtEDItelephone = new JFormattedTextField(Util.TelephoneFormatter);
					lblEDItelephone.setLabelFor(txtEDItelephone);
					txtEDItelephone.setColumns(14);
					GridBagConstraints gbc_txtEDItelephone = new GridBagConstraints();
					gbc_txtEDItelephone.anchor = GridBagConstraints.WEST;
					gbc_txtEDItelephone.insets = new Insets(0, 0, 5, 0);
					gbc_txtEDItelephone.gridx = 1;
					gbc_txtEDItelephone.gridy = 1;
					EDIcoordinatorContentPanel.add(txtEDItelephone, gbc_txtEDItelephone);
					JLabel lblEDIfax = new JLabel("Fax Number");
					lblEDIfax.setDisplayedMnemonic('F');
					GridBagConstraints gbc_lblEDIfax = new GridBagConstraints();
					gbc_lblEDIfax.anchor = GridBagConstraints.EAST;
					gbc_lblEDIfax.insets = new Insets(0, 0, 5, 5);
					gbc_lblEDIfax.gridx = 0;
					gbc_lblEDIfax.gridy = 2;
					EDIcoordinatorContentPanel.add(lblEDIfax, gbc_lblEDIfax);
					txtEDIfax = new JFormattedTextField(Util.TelephoneFormatter);
					lblEDIfax.setLabelFor(txtEDIfax);
					txtEDIfax.setColumns(10);
					GridBagConstraints gbc_txtEDIfax = new GridBagConstraints();
					gbc_txtEDIfax.anchor = GridBagConstraints.WEST;
					gbc_txtEDIfax.insets = new Insets(0, 0, 5, 0);
					gbc_txtEDIfax.gridx = 1;
					gbc_txtEDIfax.gridy = 2;
					EDIcoordinatorContentPanel.add(txtEDIfax, gbc_txtEDIfax);
					JLabel lblEDIemail = new JLabel("Email Address");
					lblEDIemail.setDisplayedMnemonic('E');
					GridBagConstraints gbc_lblEDIemail = new GridBagConstraints();
					gbc_lblEDIemail.anchor = GridBagConstraints.EAST;
					gbc_lblEDIemail.insets = new Insets(0, 0, 0, 5);
					gbc_lblEDIemail.gridx = 0;
					gbc_lblEDIemail.gridy = 3;
					EDIcoordinatorContentPanel.add(lblEDIemail, gbc_lblEDIemail);
					txtEDIemail = new JFormattedTextField();
					lblEDIemail.setLabelFor(txtEDIemail);
					txtEDIemail.setColumns(40);
					GridBagConstraints gbc_txtEDIemail = new GridBagConstraints();
					gbc_txtEDIemail.anchor = GridBagConstraints.WEST;
					gbc_txtEDIemail.gridx = 1;
					gbc_txtEDIemail.gridy = 3;
					EDIcoordinatorContentPanel.add(txtEDIemail, gbc_txtEDIemail);
				}
			}
			{
				JPanel carrierInfoPanel = new JPanel();
				FlowLayout flowLayout_1 = (FlowLayout) carrierInfoPanel.getLayout();
				flowLayout_1.setAlignment(FlowLayout.LEFT);
				tabbedPane.addTab("Carrier Information", null, carrierInfoPanel, null);
				{
					JPanel carrierInfoContentPanel = new JPanel();
					carrierInfoPanel.add(carrierInfoContentPanel);
					GridBagLayout gbl_carrierInfoContentPanel = new GridBagLayout();
					gbl_carrierInfoContentPanel.columnWidths = new int[]{0, 0, 0};
					gbl_carrierInfoContentPanel.rowHeights = new int[]{90, 0, 0};
					gbl_carrierInfoContentPanel.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
					gbl_carrierInfoContentPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
					carrierInfoContentPanel.setLayout(gbl_carrierInfoContentPanel);
					{
						scrollPane = new JScrollPane();
						GridBagConstraints gbc_scrollPane = new GridBagConstraints();
						gbc_scrollPane.anchor = GridBagConstraints.WEST;
						gbc_scrollPane.fill = GridBagConstraints.VERTICAL;
						gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
						gbc_scrollPane.gridx = 0;
						gbc_scrollPane.gridy = 0;
						carrierInfoContentPanel.add(scrollPane, gbc_scrollPane);
						{
							carrierTable = new JTable();
							scrollPane.setViewportView(carrierTable);
							carrierTable.setFillsViewportHeight(true);
							carrierTable.setAutoCreateRowSorter(true);
							carrierTable.getModel().addTableModelListener(new TableModelListener() {
							      public void tableChanged(TableModelEvent e) {
							    	  carrierTable.getModel().setValueAt(e.getSource(), e.getLastRow(), e.getColumn());
							      }
							});
							/*// colltest
							Vector<Carrier> v = new Vector();
							v.add(new Carrier("Vessel1", "Vessel EIN1", "Carrier1", "CarrierEin1"));
							v.add(new Carrier("Vessel2", "Vessel EIN2", "Carrier2", "CarrierEin2"));
							v.add(new Carrier("Vessel3", "Vessel EIN3", "Carrier3", "CarrierEin3"));
							v.add(new Carrier("Vessel4", "Vessel EIN4", "Carrier4", "CarrierEin4"));
							v.add(new Carrier("Vessel5", "Vessel EIN5", "Carrier5", "CarrierEin5"));
						    copyToCarrierJList(v);
							carrierModel = new CarrierTableModel(v);
							carrierTable.setModel(carrierModel);
							carrierModel.addTableModelListener(new TableModelListener() {
							      public void tableChanged(TableModelEvent e) {
							    	  selectedRow = carrierTable.getSelectedRow();
							      }
							});	*/
						}
					}
					
					JPanel panel = new JPanel();
					GridBagConstraints gbc_panel = new GridBagConstraints();
					gbc_panel.anchor = GridBagConstraints.NORTHWEST;
					gbc_panel.insets = new Insets(0, 0, 5, 0);
					gbc_panel.gridx = 1;
					gbc_panel.gridy = 0;
					carrierInfoContentPanel.add(panel, gbc_panel);
					GridBagLayout gbl_panel = new GridBagLayout();
					gbl_panel.columnWidths = new int[]{71, 71, 0};
					gbl_panel.rowHeights = new int[]{73, 0, 0};
					gbl_panel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
					gbl_panel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
					panel.setLayout(gbl_panel);
					{
						btnAddCarrier = new JButton("Add...");
						GridBagConstraints gbc_btnAddCarrier = new GridBagConstraints();
						gbc_btnAddCarrier.insets = new Insets(0, 0, 0, 5);
						gbc_btnAddCarrier.gridx = 0;
						gbc_btnAddCarrier.gridy = 0;
						btnAddCarrier.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								doAddCarrier();
							}
						});
						panel.add(btnAddCarrier, gbc_btnAddCarrier);
					}
					{
						btnRemoveCarrier = new JButton("Remove");
						GridBagConstraints gbc_btnRemoveCarrier = new GridBagConstraints();
						gbc_btnRemoveCarrier.gridx = 0;
						gbc_btnRemoveCarrier.gridy = 1;
						btnRemoveCarrier.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								doRemoveCarrier();
							}
						});
						panel.add(btnRemoveCarrier, gbc_btnRemoveCarrier);
					}
				}
			}
			{
				JPanel databasePanel = new JPanel();
				FlowLayout fl_databasePanel = (FlowLayout) databasePanel.getLayout();
				fl_databasePanel.setAlignment(FlowLayout.LEFT);
				tabbedPane.addTab("Database", null, databasePanel, null);
				{
					JPanel databaseContentPanel = new JPanel();
					databasePanel.add(databaseContentPanel);
					GridBagLayout gbl_databaseContentPanel = new GridBagLayout();
					gbl_databaseContentPanel.columnWidths = new int[]{0, 0, 0};
					gbl_databaseContentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
					gbl_databaseContentPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
					gbl_databaseContentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
					databaseContentPanel.setLayout(gbl_databaseContentPanel);
					JLabel lbDBserver = new JLabel("E3 Server Name/IP");
					lbDBserver.setDisplayedMnemonic('S');
					GridBagConstraints gbc_lbDBserver = new GridBagConstraints();
					gbc_lbDBserver.anchor = GridBagConstraints.EAST;
					gbc_lbDBserver.insets = new Insets(0, 0, 5, 5);
					gbc_lbDBserver.gridx = 0;
					gbc_lbDBserver.gridy = 0;
					databaseContentPanel.add(lbDBserver, gbc_lbDBserver);
					txtDBserver = new JFormattedTextField();
					lbDBserver.setLabelFor(txtDBserver);
					GridBagConstraints gbc_txtDBserver = new GridBagConstraints();
					gbc_txtDBserver.anchor = GridBagConstraints.WEST;
					gbc_txtDBserver.insets = new Insets(0, 0, 5, 0);
					gbc_txtDBserver.gridx = 1;
					gbc_txtDBserver.gridy = 0;
					databaseContentPanel.add(txtDBserver, gbc_txtDBserver);
					txtDBserver.setColumns(30);
					JLabel lblDBport = new JLabel("Port");
					lblDBport.setDisplayedMnemonic('P');
					GridBagConstraints gbc_lblDBport = new GridBagConstraints();
					gbc_lblDBport.anchor = GridBagConstraints.EAST;
					gbc_lblDBport.insets = new Insets(0, 0, 5, 5);
					gbc_lblDBport.gridx = 0;
					gbc_lblDBport.gridy = 1;
					databaseContentPanel.add(lblDBport, gbc_lblDBport);
					//NumberFormatter portFormat = new NumberFormatter(NumberFormat.getIntegerInstance());
					NumberFormat nform  = NumberFormat.getNumberInstance();
					nform.setGroupingUsed(false);  // no comma in the port
					NumberFormatter portFormat = new NumberFormatter(nform);
					portFormat.setMinimum(1);
					portFormat.setMaximum(65535);
					portFormat.setAllowsInvalid(false);
					txtDBport = new JFormattedTextField(portFormat);
					lblDBport.setLabelFor(txtDBport);
					GridBagConstraints gbc_txtDBport = new GridBagConstraints();
					gbc_txtDBport.insets = new Insets(0, 0, 5, 0);
					gbc_txtDBport.anchor = GridBagConstraints.WEST;
					gbc_txtDBport.gridx = 1;
					gbc_txtDBport.gridy = 1;
					databaseContentPanel.add(txtDBport, gbc_txtDBport);
					txtDBport.setColumns(10);
					JLabel lblDBuser = new JLabel("User Name");
					lblDBuser.setDisplayedMnemonic('U');
					GridBagConstraints gbc_lblDBuser = new GridBagConstraints();
					gbc_lblDBuser.anchor = GridBagConstraints.EAST;
					gbc_lblDBuser.insets = new Insets(0, 0, 5, 5);
					gbc_lblDBuser.gridx = 0;
					gbc_lblDBuser.gridy = 2;
					databaseContentPanel.add(lblDBuser, gbc_lblDBuser);
					txtDBuser = new JFormattedTextField();
					lblDBuser.setLabelFor(txtDBuser);
					GridBagConstraints gbc_txtDBuser = new GridBagConstraints();
					gbc_txtDBuser.anchor = GridBagConstraints.WEST;
					gbc_txtDBuser.insets = new Insets(0, 0, 5, 0);
					gbc_txtDBuser.gridx = 1;
					gbc_txtDBuser.gridy = 2;
					databaseContentPanel.add(txtDBuser, gbc_txtDBuser);
					txtDBuser.setColumns(15);
					JLabel lblDBpassword = new JLabel("Password");
					lblDBpassword.setDisplayedMnemonic('P');
					GridBagConstraints gbc_lblDBpassword = new GridBagConstraints();
					gbc_lblDBpassword.anchor = GridBagConstraints.EAST;
					gbc_lblDBpassword.insets = new Insets(0, 0, 5, 5);
					gbc_lblDBpassword.gridx = 0;
					gbc_lblDBpassword.gridy = 3;
					databaseContentPanel.add(lblDBpassword, gbc_lblDBpassword);
					txtDBpassword = new JPasswordField();
					lblDBpassword.setLabelFor(txtDBpassword);
					txtDBpassword.setColumns(15);
					GridBagConstraints gbc_txtDBpassword = new GridBagConstraints();
					gbc_txtDBpassword.anchor = GridBagConstraints.WEST;
					gbc_txtDBpassword.insets = new Insets(0, 0, 5, 0);
					gbc_txtDBpassword.gridx = 1;
					gbc_txtDBpassword.gridy = 3;
					databaseContentPanel.add(txtDBpassword, gbc_txtDBpassword);
					JLabel lblDBnumber = new JLabel("AR Database Number");
					lblDBnumber.setDisplayedMnemonic('N');
					GridBagConstraints gbc_lblDBnumber = new GridBagConstraints();
					gbc_lblDBnumber.anchor = GridBagConstraints.EAST;
					gbc_lblDBnumber.insets = new Insets(0, 0, 0, 5);
					gbc_lblDBnumber.gridx = 0;
					gbc_lblDBnumber.gridy = 4;
					databaseContentPanel.add(lblDBnumber, gbc_lblDBnumber);
					NumberFormatter nf = new NumberFormatter(NumberFormat.getIntegerInstance());
					nf.setMinimum(1);
					nf.setMaximum(999);
					nf.setAllowsInvalid(false);
					txtDBnumber = new JFormattedTextField(nf);
					lblDBnumber.setLabelFor(txtDBnumber);
					GridBagConstraints gbc_txtDBnumber = new GridBagConstraints();
					gbc_txtDBnumber.insets = new Insets(0, 0, 5, 0);
					gbc_txtDBnumber.anchor = GridBagConstraints.WEST;
					gbc_txtDBnumber.gridx = 1;
					gbc_txtDBnumber.gridy = 4;
					databaseContentPanel.add(txtDBnumber, gbc_txtDBnumber);
					txtDBnumber.setColumns(4);
					
					JLabel lblSaveDir = new JLabel("Filing Save Directory");
					lblSaveDir.setDisplayedMnemonic('D');
					GridBagConstraints gbc_lblSaveDir = new GridBagConstraints();
					gbc_lblSaveDir.anchor = GridBagConstraints.EAST;
					gbc_lblSaveDir.insets = new Insets(0, 0, 5, 5);
					gbc_lblSaveDir.gridx = 0;
					gbc_lblSaveDir.gridy = 5;
					databaseContentPanel.add(lblSaveDir, gbc_lblSaveDir);
					
					txtSaveDir = new JTextField();
					lblSaveDir.setLabelFor(txtSaveDir);
					GridBagConstraints gbc_txtSaveDir = new GridBagConstraints();
					gbc_txtSaveDir.anchor = GridBagConstraints.WEST;
					gbc_txtSaveDir.gridx = 1;
					gbc_txtSaveDir.gridy = 5;
					databaseContentPanel.add(txtSaveDir, gbc_txtSaveDir);
					txtSaveDir.setColumns(35);
				}
			}
			// New panel added for Terminal Control (FCN) Information
			{
				JPanel fcnPanel = new JPanel();
				FlowLayout flowLayout = (FlowLayout) fcnPanel.getLayout();
				flowLayout.setAlignment(FlowLayout.LEFT);
				tabbedPane.addTab("Terminal Control", null, fcnPanel, null);
				{
					JPanel fcnContentPanel = new JPanel();
					fcnPanel.add(fcnContentPanel);
					
					GridBagLayout gbl_fcnContentPanel = new GridBagLayout();
					gbl_fcnContentPanel.columnWidths = new int[]{0, 0, 0};
					gbl_fcnContentPanel.rowHeights = new int[]{0, 0, 0, 0, 0};
					gbl_fcnContentPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
					gbl_fcnContentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
					fcnContentPanel.setLayout(gbl_fcnContentPanel);
					JLabel lblFcnName = new JLabel("Terminal Name");
					lblFcnName.setDisplayedMnemonic('N');
					GridBagConstraints gbc_lblFcnName = new GridBagConstraints();
					gbc_lblFcnName.anchor = GridBagConstraints.EAST;
					gbc_lblFcnName.insets = new Insets(0, 0, 5, 5);
					gbc_lblFcnName.gridx = 0;
					gbc_lblFcnName.gridy = 0;
					fcnContentPanel.add(lblFcnName, gbc_lblFcnName);
					
					txtFCNname = new JTextField();
					//txtFcnName.setLabelFor(txtFcnName);
					GridBagConstraints gbc_txtFcnName = new GridBagConstraints();
					gbc_txtFcnName.anchor = GridBagConstraints.WEST;
					gbc_txtFcnName.insets = new Insets(0, 0, 5, 0);
					gbc_txtFcnName.gridx = 1;
					gbc_txtFcnName.gridy = 0;
					fcnContentPanel.add(txtFCNname, gbc_txtFcnName);
					txtFCNname.setColumns(35);
					
					JLabel lblFcn = new JLabel("FCN");
					lblFcn.setDisplayedMnemonic('F');
					GridBagConstraints gbc_lblFcn = new GridBagConstraints();
					gbc_lblFcn.anchor = GridBagConstraints.EAST;
					gbc_lblFcn.insets = new Insets(0, 0, 5, 5);
					gbc_lblFcn.gridx = 0;
					gbc_lblFcn.gridy = 1;
					fcnContentPanel.add(lblFcn, gbc_lblFcn);
					
					txtFCNnum = new JTextField();
					lblFcn.setLabelFor(txtFCNnum);
					GridBagConstraints gbc_txtFcnNo = new GridBagConstraints();
					gbc_txtFcnNo.anchor = GridBagConstraints.WEST;
					gbc_txtFcnNo.insets = new Insets(0, 0, 5, 0);
					gbc_txtFcnNo.gridx = 1;
					gbc_txtFcnNo.gridy = 1;
					fcnContentPanel.add(txtFCNnum, gbc_txtFcnNo);
					txtFCNnum.setColumns(9);
					
					JLabel lblFcnState = new JLabel("State");
					lblFcnState.setDisplayedMnemonic('S');
					GridBagConstraints gbc_lblFcnState = new GridBagConstraints();
					gbc_lblFcnState.anchor = GridBagConstraints.EAST;
					gbc_lblFcnState.insets = new Insets(0, 0, 5, 5);
					gbc_lblFcnState.gridx = 0;
					gbc_lblFcnState.gridy = 2;
					fcnContentPanel.add(lblFcnState, gbc_lblFcnState);
					
					txtFCNstate = new JTextField();
					lblFcnState.setLabelFor(txtFCNstate);
					GridBagConstraints gbc_txtFcnState = new GridBagConstraints();
					gbc_txtFcnState.anchor = GridBagConstraints.WEST;
					gbc_txtFcnState.insets = new Insets(0, 0, 5, 0);
					gbc_txtFcnState.gridx = 1;
					gbc_txtFcnState.gridy = 2;
					fcnContentPanel.add(txtFCNstate, gbc_txtFcnState);
					txtFCNstate.setColumns(9);
				}
			}  
		}
		loadPreferencesFromStore();
		copyPrefsToWidgets();
	}
	
	public String getEIN() {
		return EIN;
	}

	public String getId637() {
		return Id637;
	}

	public String getBusinessName() {
		return BusinessName;
	}

	public String getMailingAddr1() {
		return MailingAddr1;
	}

	public String getMailingAddr2() {
		return MailingAddr2;
	}

	public String getCity() {
		return City;
	}

	public String getState() {
		return State;
	}

	public String getZip() {
		return Zip;
	}

	public String getCountry() {
		return Country;
	}

	public String getAuthCode() {
		return AuthCode;
	}

	public String getSecurityCode() {
		return SecurityCode;
	}

	public String getInterchageID() {
		if (CopyInterchangeFromEIN)
			return EIN;
		else
			return InterchageID;
	}

	public String getApplicationID() {
		if (CopyApplicationFromEIN)
			return EIN;
		else
			return ApplicationID;
	}

	public String getGeneralName() {
		return GeneralName;
	}

	public String getGeneralTelephone() {
		return GeneralTelephone;
	}

	public String getGeneralFax() {
		return GeneralFax;
	}

	public String getGeneralEmail() {
		return GeneralEmail;
	}

	public String getEDIname() {
		return EDIname;
	}

	public String getEDItelephone() {
		return EDItelephone;
	}

	public String getEDIfax() {
		return EDIfax;
	}

	public String getEDIemail() {
		return EDIemail;
	}

	/**
	 * @return the dBserver
	 */
	public String getDBserver() {
		return DBserver;
	}

	/**
	 * @return the dBport
	 */
	public int getDBport() {
		return DBport;
	}

	/**
	 * @return the dBuser
	 */
	public String getDBuser() {
		return DBuser;
	}

	/**
	 * @return the dBpassword
	 */
	public String getDBpassword() {
		return DBpassword;
	}
	
	public String getTestMode() {
		if (TestMode)
			return "T";
		return "P";
	}
	
	public String getSaveDir() {
	   // Make sure the save directory ends with a file separator	
	   if (!saveDir.endsWith(File.separator)) {
		  saveDir = saveDir + File.separator; 
	   }
	   return saveDir;
	}

	/**
	 * @return the dBnumber
	 */
	public int getDBnumber() {
		return DBnumber;
	}
	
	public String getFCNcontrolNum() {
		return FCNnum;
	}

	private void copyWidgetsToPrefs()
	{
		EIN          = txtEIN         .getText();
		Id637        = txtId637       .getText();
		BusinessName = txtBusinessName.getText();
		MailingAddr1 = txtMailingAddr1.getText();
		MailingAddr2 = txtMailingAddr2.getText();
		City         = txtCity        .getText();
		State        = ((Abbreviation)comboState  .getSelectedItem()).Abbreviation;
		Zip          = txtZip         .getText();
		Country      = ((Abbreviation)comboCountry.getSelectedItem()).Abbreviation;

		AuthCode               = txtAuthCode              .getText();
		SecurityCode           = txtSecurityCode          .getText();
		InterchageID           = txtInterchangeID         .getText();
		CopyInterchangeFromEIN = chkCopyInterchangeFromEIN.isSelected();
		ApplicationID          = txtAppplicationID        .getText();
		CopyApplicationFromEIN = chkCopyInterchangeFromEIN.isSelected();
		TestMode               = chkTestMode              .isSelected();

		GeneralName      = txtGeneralName     .getText();
		GeneralTelephone = txtGeneralTelephone.getText();
		GeneralFax       = txtGeneralFax      .getText();
		GeneralEmail     = txtGeneralEmail    .getText();

		EDIname      = txtEDIname     .getText();
		EDItelephone = txtEDItelephone.getText();
		EDIfax       = txtEDIfax      .getText();
		EDIemail     = txtEDIemail    .getText();

		DBserver   = txtDBserver.getText();
		DBport     = Integer.parseInt(txtDBport.getText());
		DBuser     = txtDBuser.getText();
		DBpassword = new String(txtDBpassword.getPassword());
		DBnumber   = Integer.parseInt(txtDBnumber.getText());
		
		FCNname    = txtFCNname .getText();   
		FCNnum     = txtFCNnum.getText();    
		FCNstate   = txtFCNstate.getText();  
		
		saveDir    = txtSaveDir.getText();
		
	}
	
	private void savePreferencesToStore()
	{
		prefsStore.put(TAG_PREF_EIN,           EIN);
		prefsStore.put(TAG_PREF_637ID,         Id637);
		prefsStore.put(TAG_PREF_BUSINESS_NAME, BusinessName);
		prefsStore.put(TAG_PREF_MAILING_ADDR1, MailingAddr1);
		prefsStore.put(TAG_PREF_MAILING_ADDR2, MailingAddr2);
		prefsStore.put(TAG_PREF_CITY,          City);
		prefsStore.put(TAG_PREF_STATE,         State);
		prefsStore.put(TAG_PREF_ZIP,           Zip);
		prefsStore.put(TAG_PREF_COUNTRY,       Country);

		prefsStore.put       (TAG_PREF_AUTH_CODE,                 AuthCode);
		prefsStore.put       (TAG_PREF_SECURITY_CODE,             SecurityCode);
		prefsStore.put       (TAG_PREF_INTERCHANGE_ID,            InterchageID);
		prefsStore.putBoolean(TAG_PREF_COPY_INTERCHANGE_FROM_EIN, CopyInterchangeFromEIN);
		prefsStore.put       (TAG_PREF_APPLICATION_ID,            ApplicationID);
		prefsStore.putBoolean(TAG_PREF_COPY_APPLICATION_FROM_EIN, CopyApplicationFromEIN);
		prefsStore.putBoolean(TAG_PREF_TEST_MODE,                 TestMode);

		prefsStore.put(TAG_PREF_GENERAL_NAME,      GeneralName);
		prefsStore.put(TAG_PREF_GENERAL_TELEPHONE, GeneralTelephone);
		prefsStore.put(TAG_PREF_GENERAL_FAX,       GeneralFax);
		prefsStore.put(TAG_PREF_GENERAL_EMAIL,     GeneralEmail);

		prefsStore.put(TAG_PREF_EDI_NAME,      EDIname);
		prefsStore.put(TAG_PREF_EDI_TELEPHONE, EDItelephone);
		prefsStore.put(TAG_PREF_EDI_FAX,       EDIfax);
		prefsStore.put(TAG_PREF_EDI_EMAIL,     EDIemail);
		
		prefsStore.put   (TAG_PREF_DBSERVER,   DBserver);
		prefsStore.putInt(TAG_PREF_DBPORT,     DBport);
		prefsStore.put   (TAG_PREF_DBUSER,     DBuser);
		prefsStore.put   (TAG_PREF_DBPASSWORD, DBpassword);
		prefsStore.putInt(TAG_PREF_DBNUMBER,   DBnumber);
		
		prefsStore.put(TAG_PREF_FCN_NAME,      FCNname);
		prefsStore.put(TAG_PREF_FCN_NUM,       FCNnum);
		prefsStore.put(TAG_PREF_FCN_STATE,     FCNstate);
		
		prefsStore.put(TAG_SAVE_DIR, saveDir);
		prefsStore.put(TAG_CARRIER, carrierToString());
	}

	private void copyPrefsToWidgets()
	{
		txtEIN         .setText(EIN);
		txtId637       .setText(Id637);
		txtBusinessName.setText(BusinessName);
		txtMailingAddr1.setText(MailingAddr1);
		txtMailingAddr2.setText(MailingAddr2);
		txtCity        .setText(City);
		txtZip         .setText(Zip);
		
		for (Abbreviation a : Util.USstateAbbrevs)
			if (a.Abbreviation.equals(State))
			{
				comboState.setSelectedItem(a);
				break;
			}
		for (Abbreviation a : Util.CountryAbbrevs)
			if (a.Abbreviation.equals(Country))
			{
				comboCountry.setSelectedItem(a);
				break;
			}

		txtAuthCode              .setText(AuthCode);
		txtSecurityCode          .setText(SecurityCode);
		txtInterchangeID         .setText(InterchageID);
		txtInterchangeID         .setEnabled(!CopyInterchangeFromEIN);
		chkCopyInterchangeFromEIN.setSelected(CopyInterchangeFromEIN);
		txtAppplicationID        .setText(ApplicationID);
		txtAppplicationID        .setEnabled(!CopyApplicationFromEIN);
		chkCopyApplicationFromEIN.setSelected(CopyApplicationFromEIN);
		chkTestMode              .setSelected(TestMode);

		txtGeneralName     .setText(GeneralName);
		txtGeneralTelephone.setText(GeneralTelephone);
		txtGeneralFax      .setText(GeneralFax);
		txtGeneralEmail    .setText(GeneralEmail);

		txtEDIname     .setText(EDIname);
		txtEDItelephone.setText(EDItelephone);
		txtEDIfax      .setText(EDIfax);
		txtEDIemail    .setText(EDIemail);
		
		txtDBserver  .setText(DBserver);
		txtDBport    .setText(Integer.toString(DBport));
		txtDBuser    .setText(DBuser);
		txtDBpassword.setText(DBpassword);
		txtDBnumber  .setText(Integer.toString(DBnumber));
		
		txtFCNname .setText(FCNname);   
		txtFCNnum  .setText(FCNnum);    
		txtFCNstate.setText(FCNstate);  
		
		txtSaveDir .setText(saveDir);
	}
	
	private void loadPreferencesFromStore()
	{
		EIN          = prefsStore.get(TAG_PREF_EIN, "");
		Id637        = prefsStore.get(TAG_PREF_637ID, ""); 
		BusinessName = prefsStore.get(TAG_PREF_BUSINESS_NAME, "");
		MailingAddr1 = prefsStore.get(TAG_PREF_MAILING_ADDR1, "");
		MailingAddr2 = prefsStore.get(TAG_PREF_MAILING_ADDR2, "");
		City         = prefsStore.get(TAG_PREF_CITY, "");
		State        = prefsStore.get(TAG_PREF_STATE, "NY");
		Zip          = prefsStore.get(TAG_PREF_ZIP, "");
		Country      = prefsStore.get(TAG_PREF_COUNTRY, "USA");

		AuthCode               = prefsStore.get       (TAG_PREF_AUTH_CODE, "");
		SecurityCode           = prefsStore.get       (TAG_PREF_SECURITY_CODE, "");
		InterchageID           = prefsStore.get       (TAG_PREF_INTERCHANGE_ID, "");
		CopyInterchangeFromEIN = prefsStore.getBoolean(TAG_PREF_COPY_INTERCHANGE_FROM_EIN, true);
		ApplicationID          = prefsStore.get       (TAG_PREF_APPLICATION_ID, "");
		CopyApplicationFromEIN = prefsStore.getBoolean(TAG_PREF_COPY_APPLICATION_FROM_EIN, true);
		TestMode               = prefsStore.getBoolean(TAG_PREF_TEST_MODE, false);
		
		GeneralName      = prefsStore.get(TAG_PREF_GENERAL_NAME, "");
		GeneralTelephone = prefsStore.get(TAG_PREF_GENERAL_TELEPHONE, "");
		GeneralFax       = prefsStore.get(TAG_PREF_GENERAL_FAX, "");
		GeneralEmail     = prefsStore.get(TAG_PREF_GENERAL_EMAIL, "");
		
		EDIname      = prefsStore.get(TAG_PREF_EDI_NAME, "");
		EDItelephone = prefsStore.get(TAG_PREF_EDI_TELEPHONE, "");
		EDIfax       = prefsStore.get(TAG_PREF_EDI_FAX, "");
		EDIemail     = prefsStore.get(TAG_PREF_EDI_EMAIL, "");
		
		DBserver   = prefsStore.get   (TAG_PREF_DBSERVER,   "");
		DBport     = prefsStore.getInt(TAG_PREF_DBPORT,     5000);
		DBuser     = prefsStore.get   (TAG_PREF_DBUSER,     "");
		DBpassword = prefsStore.get   (TAG_PREF_DBPASSWORD, "");
		DBnumber   = prefsStore.getInt(TAG_PREF_DBNUMBER,   1);
		
		FCNname  = prefsStore.get(TAG_PREF_FCN_NAME,  "");
		FCNnum   = prefsStore.get(TAG_PREF_FCN_NUM,   "");
		FCNstate = prefsStore.get(TAG_PREF_FCN_STATE, "");
		
		saveDir = prefsStore.get(TAG_SAVE_DIR, "");
		carrierString = prefsStore.get(TAG_CARRIER, "");
		setCarrierInfo(); // this sets up the carrier table panel
	}
	
	/**
	 * This reads in the carrier String, and does two things,
	 * It creates the Vector for the Carrier Panel table and
	 * creates the JComboBox for the Receipts table, with the Vessel name only 
	 */
	private void setCarrierInfo() 
	{
		// String has format of:
		// vessel-vesselEin-carrier-carrierEin~vessel-vesselEin-carrier-carrierEin;
		// Carrier elements separated with - and each New carrier separated by ~
		carrierTableV.setSize(0); // always make sure this vector starts out with no elements
		Vector<String> listV = new Vector<String>(); 
		listV.addElement(""); // add blank as first item
		try {
			String[] carrierList = carrierString.split("~"); 
			for(String s : carrierList) {
				String[] carrierElements = s.split("-");
				if (carrierElements.length == 5) {
					Carrier c = new Carrier (carrierElements[0], carrierElements[1], carrierElements[2], carrierElements[3], carrierElements[4]);
					carrierTableV.addElement(c);    // add for table model
					listV.addElement(c.vessel);     // this is used for the JComboBox below 
				}	
			}
		}
		//catch (java.util.regex.PatternSyntaxException e) {
		catch (Exception e) {
			JOptionPane.showMessageDialog(rootPane,
	 				"<html>Preferences failed to load properly: <br>"+e.getMessage()+"<br>", 
	 				"Preferences Load Fail",
	 				JOptionPane.INFORMATION_MESSAGE);
		}
		
		// create the table for the Carrier Panel
		if (carrierModel==null) {
			carrierModel = new CarrierTableModel(carrierTableV);
			carrierTable.setModel(carrierModel);
			carrierTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			carrierModel.addTableModelListener(new TableModelListener() {
			      public void tableChanged(TableModelEvent e) {
			    	 // TODO: get foreign flag vessel working 
			    	  //selectedRow = carrierTable.getSelectedRow();
			    	 /* Carrier c = carrierModel.getRow(carrierTable.getSelectedRow());
			    	  String flag = c.vesselFlag.toUpperCase();
			    	  if ((!flag.equals("US")) && (!flag.equals("U.S.")) && (!flag.equals("UNITED STATES")) && (!flag.equals("USA"))) {
			    		  JOptionPane.showMessageDialog(rootPane,
			  	 				"<html>A non US Vessel flag require the Vessel EIN to be the EIN of the<br>Importer of Record(IOR) who must be a US person registered on a 637.<br>", 
			  	 				"Preferences Load Fail",
			  	 				JOptionPane.INFORMATION_MESSAGE);
			    	  }
			    	 	  */
			      }
			});	
		}
		else  {
			carrierModel.fireDataChanged(); // fires fireTableDataChanged to redraw table
		}	
		
		// create the JComboBox used in the receipt table
		if (jcbCarrierList==null) {
		   jcbCarrierList = new JComboBox<String>(listV);
		   jcbCarrierList.setEditable(false);
		   //jcbCarrierList.setSelectedIndex(0);
		   /*jcbCarrierList.addActionListener (new ActionListener () {
			    public void actionPerformed(ActionEvent e) {
			    	String s  = jcbCarrierList.getSelectedItem().toString();
			    }
			});*/
		}
	}
	
	public JComboBox<String> getCarrierComboBox() {
		return jcbCarrierList; 
	}
	/**
	 * Returns the Carrier table information in a String 
	 * @return
	 */
	private String carrierToString() {
		String prefString = "";
		Carrier c; 
		int rows = carrierModel.getRowCount();
		for (int i=0; i<rows; i++) {
			c = carrierModel.getRow(i);
			prefString = prefString+c.vessel+"-"+c.vesselEin+"-"+c.carrier+"-"+c.carrierEin+"-"+c.vesselFlag+"~";
		}
		return prefString;
	    //return "Vessel1-Vessel EIN1-Carrier1-CarrierEin1-Vessel1Flag~Vessel2-Vessel EIN2-Carrier2-CarrierEin2-Vessel2Flag~Vessel3-Vessel EIN3-Carrier3-CarrierEin3-Vessel3Flag~Vessel4-Vessel EIN4-Carrier4-CarrierEin4-Vessel1Flag";
	}
	
	/**
	 * Returns a vector of Carriers
	 * @returns
	 */
	public Vector<Carrier> getCarrierTableV() {
		return carrierTableV;
	}

	private void doCancel()
	{
		this.setVisible(false);
		copyPrefsToWidgets();
	}
	
	private void doOk()
	{
		this.setVisible(false);
		copyWidgetsToPrefs();
		savePreferencesToStore();
		MainApp.initDB();
	}
	
	private void doAddCarrier() {
		Carrier c = new Carrier("", "", "", "", ""); 
		carrierModel.setValueAt(c, carrierModel.getRowCount(), 0);
		carrierModel.addRow(c);
	}
	
	private void doRemoveCarrier() {
		carrierModel.removeRow(carrierTable.getSelectedRow());
	}

}
