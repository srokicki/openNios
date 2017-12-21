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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import niosSimulator.Instruction;
import alternateSimulator.CycleDescription;
import alternateSimulator.Simulator;
import openDLX.gui.GUI_CONST;
import openDLX.gui.MainFrame;
import openDLX.gui.internalframes.OpenDLXSimInternalFrame;
import openDLX.gui.internalframes.renderer.ClockCycleFrameTableCellRenderer;
import openDLX.gui.internalframes.util.NotSelectableTableModel;
import openDLX.util.ClockCycleLog;

@SuppressWarnings("serial")
public final class ClockCycleFrame extends OpenDLXSimInternalFrame implements GUI_CONST
{

    private final Simulator simulator;
    //frame text
    private final String addrHeaderText = "Address";
    private final String cycleColumnText = "Instr";
    private final String instHeaderText = "Instructions/Cycles";
    //default size values
    private final int instructionNameMaxColWidth = 150;
    private int block = 80;
    //tables, scrollpane and table models
    private JTable table, codeTable, addrTable, cycleTable;
    private NotSelectableTableModel model, codeModel, addrModel, cycleModel;
    private JScrollPane clockCycleScrollPane;
    private JScrollPane addrScrollPane;
    private JScrollPane codeScrollPane;
    private JScrollBar clockCycleScrollBarVertical;
    private JScrollBar addrScrollBar;
    private JScrollBar codeScrollBar;
    
    private JScrollPane cycleScrollPane;
    private JScrollBar cycleScrollBar;

    //private JScrollBar clockCycleScrollBarHorizontal;

    public ClockCycleFrame(String title)
    {
        super(title, true);
        simulator = MainFrame.getInstance().getSimulator();
        initialize();
    }

    @Override
    public void initialize()
    {
        super.initialize();
        setLayout(new BorderLayout());

        //Cycle count
        codeModel = new NotSelectableTableModel();
        codeTable = new JTable(codeModel);
        codeTable.setFocusable(false);
        codeTable.setShowGrid(false);
        codeTable.getTableHeader().setReorderingAllowed(false);
        codeTable.setShowHorizontalLines(true);
        codeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        codeTable.setSelectionBackground(Color.LIGHT_GRAY);
        codeModel.addColumn(cycleColumnText);
        TableColumnModel tcm = codeTable.getColumnModel();
        tcm.getColumn(0).setMaxWidth(instructionNameMaxColWidth);
        tcm.getColumn(0).setMinWidth(instructionNameMaxColWidth);
        codeScrollPane = new JScrollPane(codeTable);
        codeScrollPane.setPreferredSize(new Dimension(tcm.getColumn(0).getMaxWidth(),
                codeScrollPane.getPreferredSize().height));
        codeScrollBar = codeScrollPane.getVerticalScrollBar();
        codeScrollBar.addAdjustmentListener(new AdjustmentListener()
        {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e)
            {
                clockCycleScrollBarVertical.setValue(e.getValue());
                addrScrollBar.setValue(e.getValue());
                cycleScrollBar.setValue(e.getValue());
            }

        });
        
        //Code Table	
        cycleModel = new NotSelectableTableModel();
        cycleTable = new JTable(codeModel);
        cycleTable.setFocusable(false);
        cycleTable.setShowGrid(false);
        cycleTable.getTableHeader().setReorderingAllowed(false);
        cycleTable.setShowHorizontalLines(true);
        cycleTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        cycleTable.setSelectionBackground(Color.LIGHT_GRAY);
        cycleTable.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				int start = cycleTable.getSelectedRow();
				int size = cycleTable.getSelectedRowCount();
				codeTable.setRowSelectionInterval(start, start+size-1);
				addrTable.setRowSelectionInterval(start, start+size-1);
				table.setRowSelectionInterval(start, start+size-1);
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				int start = cycleTable.getSelectedRow();
				int size = cycleTable.getSelectedRowCount();
				codeTable.setRowSelectionInterval(start, start+size-1);
				addrTable.setRowSelectionInterval(start, start+size-1);
				table.setRowSelectionInterval(start, start+size-1);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {

			}
		});;
        cycleModel.addColumn(instHeaderText);
        TableColumnModel tcm1 = cycleTable.getColumnModel();
        tcm1.getColumn(0).setMaxWidth(instructionNameMaxColWidth);
        tcm1.getColumn(0).setMinWidth(instructionNameMaxColWidth);
        cycleScrollPane = new JScrollPane(cycleTable);
        cycleScrollPane.setPreferredSize(new Dimension(tcm1.getColumn(0).getMaxWidth(),
        		cycleScrollPane.getPreferredSize().height));
        cycleScrollBar = cycleScrollPane.getVerticalScrollBar();
        cycleScrollBar.addAdjustmentListener(new AdjustmentListener()
        {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e)
            {
                clockCycleScrollBarVertical.setValue(e.getValue());
                addrScrollBar.setValue(e.getValue());
                codeScrollBar.setValue(e.getValue());
            }

        });
        
        //Address Table
        addrModel = new NotSelectableTableModel();
        addrTable = new JTable(addrModel);
        addrTable.setFocusable(false);
        addrTable.setShowGrid(false);
        addrTable.getTableHeader().setReorderingAllowed(false);
        addrTable.setShowHorizontalLines(true);
        addrTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        addrTable.setSelectionBackground(Color.LIGHT_GRAY);
        addrTable.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				int start = addrTable.getSelectedRow();
				int size = addrTable.getSelectedRowCount();
				codeTable.setRowSelectionInterval(start, start+size-1);
				cycleTable.setRowSelectionInterval(start, start+size-1);
				table.setRowSelectionInterval(start, start+size-1);
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				int start = addrTable.getSelectedRow();
				int size = addrTable.getSelectedRowCount();
				codeTable.setRowSelectionInterval(start, start+size-1);
				cycleTable.setRowSelectionInterval(start, start+size-1);
				table.setRowSelectionInterval(start, start+size-1);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {

			}
		});;

        addrModel.addColumn(addrHeaderText);
        TableColumnModel tcm2 = addrTable.getColumnModel();
        tcm2.getColumn(0).setMaxWidth(instructionNameMaxColWidth);
        tcm2.getColumn(0).setMinWidth(instructionNameMaxColWidth);
        addrScrollPane = new JScrollPane(addrTable);
        addrScrollPane.setPreferredSize(new Dimension(tcm2.getColumn(0).getMaxWidth(),
                addrScrollPane.getPreferredSize().height));
        addrScrollBar = addrScrollPane.getVerticalScrollBar();
        addrScrollBar.addAdjustmentListener(new AdjustmentListener()
        {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e)
            {
                clockCycleScrollBarVertical.setValue(e.getValue());
                codeScrollBar.setValue(e.getValue());
                cycleScrollBar.setValue(e.getValue());
            }

        });

        //scroll pane and frame
        clockCycleScrollPane = makeTableScrollPane();
        add(addrScrollPane, BorderLayout.EAST);
        add(codeScrollPane, BorderLayout.WEST);
        add(cycleScrollPane, BorderLayout.WEST);
        add(clockCycleScrollPane, BorderLayout.CENTER);
        pack();
        setVisible(true);

    }

    private JScrollPane makeTableScrollPane()
    {
        //Clock Cycle Table
        model = new NotSelectableTableModel();
        table = new JTable(model);
        table.setFocusable(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getTableHeader().setReorderingAllowed(false);
        table.setDefaultRenderer(Object.class, new ClockCycleFrameTableCellRenderer());
        table.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				int start = table.getSelectedRow();
				int size = table.getSelectedRowCount();
				codeTable.setRowSelectionInterval(start, start+size-1);
				addrTable.setRowSelectionInterval(start, start+size-1);
				cycleTable.setRowSelectionInterval(start, start+size-1);
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				int start = table.getSelectedRow();
				int size = table.getSelectedRowCount();
				codeTable.setRowSelectionInterval(start, start+size-1);
				addrTable.setRowSelectionInterval(start, start+size-1);
				cycleTable.setRowSelectionInterval(start, start+size-1);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {

			}
		});;
        clockCycleScrollPane = new JScrollPane(table);
        clockCycleScrollBarVertical = clockCycleScrollPane.getVerticalScrollBar();
        clockCycleScrollBarVertical.addAdjustmentListener(new AdjustmentListener()
        {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e)
            {
                addrScrollBar.setValue(e.getValue());
                codeScrollBar.setValue(e.getValue());
            }

        });

        clockCycleScrollPane.setPreferredSize(new Dimension(3 * block,
                clockCycleScrollPane.getPreferredSize().height));
        return clockCycleScrollPane;

    }

    @Override
    public void update()
    {

        for (CycleDescription oneCycleDescription : ClockCycleLog.getCycleLog().subList(model.getColumnCount(), ClockCycleLog.getCycleLog().size()))
        {
        	
        	
        	
            if (!oneCycleDescription.isStall() && oneCycleDescription.getFetchedInstruction().getPC() != -1)
			    if (!oneCycleDescription.isStall()){
			        addrModel.addRow(new String[] {"0x" + Long.toHexString(oneCycleDescription.getFetchedInstruction().getPC())});
			        codeModel.addRow(new String[] { oneCycleDescription.getFetchedInstruction().toString() });
			        model.addRow(new String[] { "" });
			        cycleModel.addRow(new String[] {Long.toString(oneCycleDescription.getFetchedInstruction().getCycleNumber())});
			    }
            

		    model.addColumn(oneCycleDescription.getCycleNumber());

		    final HashMap<Instruction, String> pipelineMap =  oneCycleDescription.getPipelineMap();
		    
		    for (Instruction instr : pipelineMap.keySet())
		        for (int instructionNumber = cycleModel.getRowCount() - 1; instructionNumber >= 0; --instructionNumber)
		        	if (cycleModel.getValueAt(instructionNumber, 0).equals(Long.toString(instr.getCycleNumber())))
		                model.setValueAt(pipelineMap.get(instr), instructionNumber, oneCycleDescription.getCycleNumber());

			
        }
        for (int j = 0; j < table.getColumnModel().getColumnCount(); ++j)
        {
            TableColumn column = table.getColumnModel().getColumn(j);
            column.setMaxWidth(30);
            column.setResizable(false);
        }

        // clockCycleScrollBarHorizontal.setValue(clockCycleScrollBarHorizontal.getMaximum());
        table.scrollRectToVisible(table.getCellRect(table.getRowCount() - 1, table.getColumnCount() - 1, true));
        codeTable.scrollRectToVisible(codeTable.getCellRect(table.getRowCount() - 1, 0, true));
        addrTable.scrollRectToVisible(addrTable.getCellRect(addrTable.getRowCount() - 1, 0, true));
        cycleTable.scrollRectToVisible(cycleTable.getCellRect(cycleTable.getRowCount() - 1, 0, true));


    }

    @Override
    public void clean()
    {
        setVisible(false);
        dispose();
    }


}
