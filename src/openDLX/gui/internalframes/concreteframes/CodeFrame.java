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
package openDLX.gui.internalframes.concreteframes;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import niosSimulator.Execute;
import niosSimulator.Memory;
import alternateSimulator.Simulator;
import openDLX.gui.MainFrame;
import openDLX.gui.internalframes.OpenDLXSimInternalFrame;
import openDLX.gui.internalframes.factories.tableFactories.CodeTableFactory;
import openDLX.gui.internalframes.util.TableSizeCalculator;

@SuppressWarnings("serial")
public final class CodeFrame extends OpenDLXSimInternalFrame
{

    private final Simulator simulator;
    private JTable codeTable;
    private long IFValue;
    private long IDValue;
    private ArrayList<Long> EXValues;
    private ArrayList<Long> MemValues;
    private long WBValue;

    public CodeFrame(String title)
    {
        super(title, false);
        simulator = MainFrame.getInstance().getSimulator();
        EXValues = new ArrayList<Long>();
        MemValues = new ArrayList<Long>();
        initialize();
    }

    @Override
    public void update()
    {
        IFValue = simulator.getFetchStages().get(0).getCurrentInstruction().getPC();
        IDValue = simulator.getDecodeStages().get(0).getCurrentInstruction().getPC();
        EXValues.clear();
        for(Execute oneExecute : simulator.getExecuteStages())
        	EXValues.add(oneExecute.getCurrentInstruction().getPC());
        
        MemValues.clear();
        for(Memory oneMemory : simulator.getMemoryStages())
        	MemValues.add(oneMemory.getCurrentInstruction().getPC());
        
        WBValue = simulator.getWriteBackStages().get(0).getCurrentInstruction().getPC();

        TableModel model = codeTable.getModel();
        for (int row = 0; row < model.getRowCount(); ++row)
        {
        	String addrString = (String) model.getValueAt(row, 0);
        	if (addrString.startsWith("IF") || addrString.startsWith("ID") || addrString.startsWith("EX") || addrString.startsWith("MEM") || addrString.startsWith("WB") || addrString.startsWith("  "))
        		addrString = addrString.substring(4);
        	
        	long addr = Long.parseLong(addrString, 16);
        	
            if (addr == IFValue)
            {
            	addrString = "IF  " + addrString;

                // move IF row into focus - i.e. scroll to IF-row
                if (codeTable.getParent() != null)
                    codeTable.scrollRectToVisible(codeTable.getCellRect(row, 0, true));
            }
            else if (addr == IDValue)
            	addrString = "ID  " + addrString;
            else if (EXValues.contains(addr))
            	addrString = "EX  " + addrString;
            else if (MemValues.contains(addr))
            	addrString = "MEM " + addrString;
            else if (addr == WBValue)
            	addrString = "WB  " + addrString;
            else
            	addrString = "    " + addrString;

            model.setValueAt(addrString, row, 0);
        }
    }

    @Override
    protected void initialize()
    {
        super.initialize();
        //make the scrollpane
        codeTable = new CodeTableFactory(simulator).createTable();
        JScrollPane scrollpane = new JScrollPane(codeTable);
        scrollpane.setFocusable(false);
        codeTable.setFillsViewportHeight(true);
        TableSizeCalculator.setDefaultMaxTableSize(scrollpane, codeTable,
                TableSizeCalculator.SET_SIZE_WIDTH);
        //config internal frame
        setLayout(new BorderLayout());
        add(scrollpane, BorderLayout.CENTER);
        pack();
        setVisible(true);
    }

    @Override
    public void clean()
    {
        setVisible(false);
        dispose();
    }

}
