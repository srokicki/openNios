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
package openDLX.config;

import java.util.Properties;

import openDLX.gui.Preference;

public class ArchCfg
{

    // forwarding implies the two boolean: use_forwarding and use_load_stall_bubble
    public static boolean use_forwarding = Preference.pref.getBoolean(Preference.forwardingPreferenceKey, true);

    // TODO: rename variable
    public static boolean  use_load_stall_bubble = Preference.pref.getBoolean(Preference.mipsCompatibilityPreferenceKey, true);


    public static final String[] GP_NAMES_NIOS =
    {
        "r0 ", "r1 ", "r2 ", "r3 ", "r4 ", "r5 ", "r6 ", "r7 ", "r8 ", "r9 ", "r10", "r11", "r12", "r13", "r14", "r15",
        "r16", "r17", "r18", "r19", "r20", "r21", "r22", "r23", "et", "bt", "gp", "sp", "fp", "ea", "sstatus", "ra"
    };

//   public static String assemblerPath = "/home/nanjing/Documents/lab2/nios2-elf-as";

    public static String assemblerPath = "external_bin/lnx-nios2-elf-as";

//   public static String assemblerPath = "/local/altera/14.0/nios2eds/bin/gnu/H-x86_64-pc-linux-gnu/bin/nios2-elf-as";
   // public static String assemblerPath = "c:\\altera\\12.1sp1\\nios2eds\\bin\\gnu\\H-i686-mingw32\\bin\\nios2-elf-as";

    public static int max_cycles = Preference.pref.getInt(Preference.maxCyclesPreferenceKey, 10000);
    
    public static int execute_stage = Preference.pref.getInt("numberexecutestage", 3);

    public static int memory_stage = Preference.pref.getInt("numbermemorystage", 2);

    
    public static void registerArchitectureConfig(Properties config)
    {
        ArchCfg.use_forwarding = getUseForwardingCfg(config);
        ArchCfg.use_load_stall_bubble = getUseLoadStallBubble(config);
    }


    private static boolean getUseForwardingCfg(Properties config)
    {
            return (((config.getProperty("use_forwarding")).toLowerCase()).equals("true"))
                    || ((config.getProperty("use_forwarding")).equals("1"));
    }

    private static boolean getUseLoadStallBubble(Properties config)
    {
        return (((config.getProperty("use_load_stall_bubble")).toLowerCase()).equals("true"))
                    || ((config.getProperty("use_load_stall_bubble")).equals("1"));
    }

    public static String getRegisterDescription(int reg_id)
    {
        return GP_NAMES_NIOS[reg_id];
    }

    public static int getRegisterCount()
    {

        return 32;
    }

}
