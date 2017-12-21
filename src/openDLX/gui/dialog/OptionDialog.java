/*******************************************************************************
 * openDLX - A DLX/MIPS processor simulator.
 * Copyright (C) 2013 The openDLX project, University of Augsburg, Germany
 * Project URL: <https://sourceforge.net/projects/opendlx>
 * Development branch: <https://github.com/smetzlaff/openDLX>
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program, see <LICENSE>. If not, see
 * <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package openDLX.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import openDLX.config.ArchCfg;
import openDLX.gui.MainFrame;
import openDLX.gui.Preference;

@SuppressWarnings("serial")
public class OptionDialog extends JDialog implements ActionListener
{
    // two control buttons, press confirm to save selected options
    private JButton confirm;
    private JButton cancel;

    // checkBoxes
    private JCheckBox forwardingCheckBox;
    private JCheckBox mipsCompatibilityCheckBox;

    /*
     * JComboBox may be represented by Vectors or Arrays of Objects (Object [])
     * we have chosen "String[]" to be the representation (in fact - String) for
     * the data within AsmFileLoader-class , but Vector is appropriate as well.
     */

    //input text fields
    private JTextField assemblerTextField;

    private JTextField maxCyclesTextField;
    private JTextField numberExecuteTextField;

    public OptionDialog(Frame owner)
    {
        //calls modal constructor, set to "false" to make dialog non-modal
        super(owner, true);
        setLayout(new BorderLayout());
        setTitle("options");
        //control buttons
        confirm = new JButton("OK");
        confirm.addActionListener(this);
        cancel = new JButton("Cancel");
        cancel.addActionListener(this);
        //the panel containing all the control buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(confirm);
        buttonPanel.add(cancel);
        add(buttonPanel, BorderLayout.SOUTH);

        /*instantiate all the components that you'd like to use as input,
         * as well as any labels describing them, HERE: */

        
        
        
        /*create a checkboxes
         *
         * checkboxes don't need a label -> the name is part of the constructor
         *-> its a single element, hence it doesn't need a JPanel  */
        forwardingCheckBox = new JCheckBox("Use Forwarding");
        forwardingCheckBox.setSelected(Preference.pref.getBoolean(Preference.forwardingPreferenceKey, true)); // load current value

        mipsCompatibilityCheckBox = new JCheckBox("MIPS compatibility mode (requires forwarding)");
        mipsCompatibilityCheckBox.setSelected(Preference.pref.getBoolean(Preference.mipsCompatibilityPreferenceKey, true)); // load current value

        // disable MIPS compatibility if no forwarding is active
        if (!forwardingCheckBox.isSelected())
        {
            mipsCompatibilityCheckBox.setSelected(false);
        }

        /*create a JComboBoxes
         *
         * JComboBox need a Object[] or Vector as data representation
         Furthermore the  JComboBox gets a JLabel, describing it,
         * -> put both components into a JPanel*/

 
        //surrounding panel
        JPanel bpTypeListPanel = new JPanel();
        //add the label
        //add the box itself

        /*create a  JTextFields
         * the field and a JLabel description
         */
        
        JLabel assemblerTextFieldDescription = new JLabel("Assembler: ");
        assemblerTextField = new JTextField(40);
        assemblerTextField.setText(ArchCfg.assemblerPath);
        JPanel assemblerTextFieldPannel = new JPanel();
        assemblerTextFieldPannel.add(assemblerTextFieldDescription);
        assemblerTextFieldPannel.add(assemblerTextField);

        // Max Cycles
        JLabel maxCyclesTextFieldDescription = new JLabel("Maximum Cycles: ");
        // the number in constructor means the number of lines in textfield
        maxCyclesTextField = new JTextField(10);
        //load current text from ArchCfg
        maxCyclesTextField.setText((new Integer(ArchCfg.max_cycles)).toString());
        //surrounding panel, containing both JLabel and JTextField
        JPanel maxCyclesTextFieldPanel = new JPanel();
        //add the label
        maxCyclesTextFieldPanel.add(maxCyclesTextFieldDescription);
        //add the field itself
        maxCyclesTextFieldPanel.add(maxCyclesTextField);
        
        
        //Execute stages
        JLabel executeTextFieldDescription = new JLabel("Execute stages: ");
        // the number in constructor means the number of lines in textfield
        numberExecuteTextField = new JTextField(10);
        //load current text from ArchCfg
        numberExecuteTextField.setText((new Integer(ArchCfg.execute_stage)).toString());
        //surrounding panel, containing both JLabel and JTextField
        JPanel numberExecuteTextFieldPanel = new JPanel();
        //add the label
        numberExecuteTextFieldPanel.add(executeTextFieldDescription);
        //add the field itself
        numberExecuteTextFieldPanel.add(numberExecuteTextField);

        
        //this panel contains all input components = top level panel
        JPanel optionPanel = new JPanel();
        optionPanel.setLayout(new GridLayout(0, 1));

        //dont forget adding the components to the panel !!!

        optionPanel.add(assemblerTextFieldPannel);
        optionPanel.add(forwardingCheckBox);
        optionPanel.add(maxCyclesTextFieldPanel);
        optionPanel.add(numberExecuteTextFieldPanel);

        //adds the top-level-panel to the Dialog frame
        add(optionPanel, BorderLayout.CENTER);

        //dialog appears in the middle of the MainFrame
        setLocationRelativeTo(owner);
        pack();
        setResizable(false);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        //just close the dialog
        if (e.getSource().equals(cancel))
        {
            setVisible(false);
            dispose();
        }

        /* get all the values, assign them to data within ArchCfg
         * and save them as preference for future use
         */
        if (e.getSource().equals(confirm))
        {
            Preference.pref.putBoolean(Preference.forwardingPreferenceKey,
                    forwardingCheckBox.isSelected());
            
            if (forwardingCheckBox.isSelected())
            {
                    ArchCfg.use_load_stall_bubble = true;
            }
            else
            {
                ArchCfg.use_forwarding = false;
                ArchCfg.use_load_stall_bubble = false;
            }

            propagateFWToMenu(forwardingCheckBox.isSelected());
         
            ArchCfg.assemblerPath = assemblerTextField.getText();
            ArchCfg.max_cycles = Integer.parseInt(maxCyclesTextField.getText());
            Preference.pref.put(Preference.maxCyclesPreferenceKey, maxCyclesTextField.getText());
            
            ArchCfg.execute_stage = Integer.parseInt(numberExecuteTextField.getText());
            Preference.pref.put(Preference.numberExecuteStage, numberExecuteTextField.getText());

            setVisible(false);
            dispose();
        }
    }

    private void propagateFWToMenu(boolean forwarding_enabled)
    {
        MainFrame.getInstance().getForwardingMenuItem().setSelected(forwarding_enabled);
    }

}
