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
package openDLX.gui.command.systemLevel;

import openDLX.config.ArchCfg;
import openDLX.gui.MainFrame;
import openDLX.gui.command.Command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class CommandCompileCode implements Command
{

    private File codeFile = null; // in 
    private File configFile = null; // out
    private MainFrame mf;

    public CommandCompileCode(MainFrame mf, File in)
    {
        codeFile = in;
        this.mf = mf;
    }

    @Override
    public void execute()
    {
    	try
    	{
            if (codeFile != null)
            {
            	
            	File oldElfFile = new File("./file.elf");
            	if (oldElfFile.exists())
            		oldElfFile.delete();
            	
                String codeFilePath = codeFile.getAbsolutePath().replace("\\", "/");
                Runtime rt = Runtime.getRuntime();
                
                InputStream is;
                
                String system = System.getProperty("os.name");
                System.out.println("Current system is : " + system);
                if (system.matches(".*Windows.*"))
            		is = this.getClass().getResource("/external_bin/win-nios2-elf-as").openStream();
                else if (system.matches(".*Mac.*"))
        			is = this.getClass().getResource("/external_bin/mac-nios2-elf-as").openStream();
        		else
        			is = this.getClass().getResource("/external_bin/lnx-nios2-elf-as").openStream();
                			
                
                OutputStream os = new FileOutputStream("./nios2-elf-as");
                
                byte[] b = new byte[2048];
                int length;

                while ((length = is.read(b)) != -1) {
                    os.write(b, 0, length);
                }

                is.close();
                os.close();
                
                File file = new File("./nios2-elf-as");
                file.setExecutable(true);
                
                Process ps = rt.exec("./nios2-elf-as" + " " + codeFilePath + " -o ./file.elf");
                ps.waitFor();
                ArrayList<String> errorMessage = getStringFromInputStream(ps.getErrorStream());
                
                if (errorMessage.size() != 0){
                
	                String errorDisplayed = "";
	                for (String oneLine : errorMessage){
//	                	int index = 0;
//	                	while (oneLine.charAt(index) != ':')
//	                		oneLine = oneLine.substring(1);
//	                	
//	            		oneLine = oneLine.substring(1);
//	            		if (oneLine.matches("[0-9]+.*")){
//	            			String lineString = "";
//	                    	while (oneLine.charAt(index) != ':'){
//	                    		lineString += oneLine.charAt(0);
//	                    		oneLine = oneLine.substring(1);
//	                    	}
//	                		oneLine = oneLine.substring(1);
//	
//	            			int line = Integer.parseInt(lineString);
	            			errorDisplayed += oneLine + "\n";
//	            		}
	                }
	                JOptionPane.showMessageDialog(mf, errorDisplayed);
	                
                }
            }     
        }
        catch (Exception e)
        {
            System.err.println(e.toString());
            e.printStackTrace();
            JOptionPane.showMessageDialog(mf, "Compiling/Assembling Code Failed");
        }
    }
    
	// convert InputStream to String
	private static ArrayList<String> getStringFromInputStream(InputStream is) {

		ArrayList<String> result = new ArrayList<String>();
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
				result.add(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return result;

	}

    public File getConfigFile()
    {
        return configFile;
    }

}
