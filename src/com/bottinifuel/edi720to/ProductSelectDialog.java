package com.bottinifuel.edi720to;

/**************************************************************************
*
*   File name: ProductSelectDialog
*   Displays a dialog asking the user to select the #2 fuel type they are reporting
* 	 
* @author carlonc
* 
*************************************************************************/
/* Change Log:
* 
*   Date         Description                                        Pgmr
*  ------------  ------------------------------------------------   -----
*  Feb 28,2013   Intial Dev...                                     carlonc 
*************************************************************************/

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.bottinifuel.Energy.Info.Product;

public class ProductSelectDialog {
	
	String selected = "";
	
	public ProductSelectDialog(Product prod, JPanel parentPanel){
		JPanel panel = new JPanel();  
	    GridBagLayout gbl_panel = new GridBagLayout();
	    gbl_panel.columnWidths = new int[]{0, 0, 0};
	    gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0};
	    gbl_panel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
	    gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
	    panel.setLayout(gbl_panel);
			    
	    ButtonGroup radioButtonGroup=new ButtonGroup();
	    JRadioButton b1=new JRadioButton("Low Sulfur "+prod.getDescription(), true);
	    b1.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e)
            {
                selected = "low";
            }
        });
	    GridBagConstraints gbc_b1 = new GridBagConstraints();
	    gbc_b1.anchor = GridBagConstraints.FIRST_LINE_START;//.EAST;
		//gbc_b1.insets = new Insets(0, 0, 5, 0);
		gbc_b1.gridx = 0;
		gbc_b1.gridy = 0;
		radioButtonGroup.add(b1);
        panel.add(b1, gbc_b1);
	    		    
	    JRadioButton b2=new JRadioButton(prod.getDescription(),false);
	    b2.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e)
            {
                selected = "2";
            }
        });
	    GridBagConstraints gbc_b2 = new GridBagConstraints();
	    gbc_b2.anchor = GridBagConstraints.LINE_START;
		//gbc_b2.insets = new Insets(0, 0, 5, 0);
		gbc_b2.gridx = 0;
		gbc_b2.gridy = 1;
		radioButtonGroup.add(b2);
		panel.add(b2, gbc_b2);
	   		    
	    JOptionPane.showOptionDialog(parentPanel, panel,  
	        "Select Product Type", JOptionPane.OK_CANCEL_OPTION, 
	        JOptionPane.QUESTION_MESSAGE, null, null, null);  
	}
	
	/**
	 * Returns the user's selection, "low" or "2"
	 * @return
	 */
	public String getSelection() {
		return selected;
	}	
}
