package alternateSimulator;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

import alternateSimulator.CycleDescription;
import alternateSimulator.InterfaceGUI;
import niosSimulator.Decode;
import niosSimulator.Execute;
import niosSimulator.Fetch;
import niosSimulator.Instruction;
import niosSimulator.Memory;
import niosSimulator.NiosMemory;
import niosSimulator.RegisterFile;
import niosSimulator.WriteBack;
import niosSimulator.niosOpCode;
import niosSimulator.niosOpxCode;
import openDLX.config.ArchCfg;
import openDLX.exception.PipelineException;
import openDLX.gui.GUI_CONST;
import openDLX.util.Statistics;


import elfParser.ElfFileParser;

public class Simulator{


	private Properties config;
    private Statistics stat;
    private boolean caught_break = false;
    private int clock_cycle;
    private int sim_cycles;
    private boolean finished;
    private boolean isFetchStalled;
    
    private long numberFetch;
    
    private NiosMemory memory;
    private RegisterFile registerFile;
    private ArrayList<Fetch> fetchStages;
    public ArrayList<Fetch> getFetchStages() {
		return fetchStages;
	}



	public ArrayList<Decode> getDecodeStages() {
		return decodeStages;
	}



	public ArrayList<Execute> getExecuteStages() {
		return executeStages;
	}



	public ArrayList<Memory> getMemoryStages() {
		return memoryStages;
	}



	public ArrayList<WriteBack> getWriteBackStages() {
		return writeBackStages;
	}

	private ArrayList<Decode> decodeStages;
    private ArrayList<Execute> executeStages;
    private ArrayList<Memory> memoryStages;
    private ArrayList<WriteBack> writeBackStages;
  
    
    private InterfaceGUI gui;
    
        /**
         * @param args
         */
        public void openDLXCmdl_main()
        {

            while (!finished)
            {
                try
                {
                    step();
                }
                catch (PipelineException e)
                {
                    e.printStackTrace();
                    stopSimulation(true);
                }
            }
        }



        public Simulator() throws PipelineException
        {
            config = new Properties();
            this.gui = new InterfaceGUI();
            this.stat = Statistics.getInstance();

//            
//            setDefaultConfigParameters(config);
//
//            ArchCfg.registerArchitectureConfig(config);
//            
//            if (ArchCfg.max_cycles!= Integer.parseInt(config.getProperty("cycles")))
//            	config.put("cycles", ArchCfg.max_cycles);
//            
//            if (ArchCfg.use_forwarding != Boolean.parseBoolean(config.getProperty("use_forwarding")))
//            	config.put("use_forwarding", ArchCfg.use_forwarding);
//            
//            System.out.println("Configuration is: " + config.toString());
//
//            LoggerConfigurator.getInstance().configureLogger(config.getProperty("log4j"), config.getProperty("log_file"));
//
//            gui.addLogInfoMessage("Configuration is: " + config.toString());
//            gui.addLogInfoMessage("loading:" + config.getProperty("file"));
//
//
//            
//            ArchCfg.execute_stage = new Integer(config.getProperty("numberexecutestage"));

            sim_cycles = 15000;


            /******************************
             * Initialization of the simulator
             ******************************/
            
            //First we initialize data structures
            this.fetchStages = new ArrayList<Fetch>();
            this.decodeStages = new ArrayList<Decode>();
            this.executeStages = new ArrayList<Execute>();
            this.memoryStages = new ArrayList<Memory>();
            this.writeBackStages = new ArrayList<WriteBack>();
            
            int numberExecute = 3;
            int numberMemory = 2;
            
            
            this.registerFile = new RegisterFile();
            this.memory = new NiosMemory();
            
            this.fetchStages.add(new Fetch(registerFile, memory, true));
            this.decodeStages.add(new Decode(registerFile));
            for (int i=0; i<numberExecute; i++)
            	this.executeStages.add(new Execute(registerFile, i==numberExecute-1));
            for (int i=0; i<numberMemory; i++)
            	this.memoryStages.add(new Memory(registerFile, memory, i==numberMemory-1));
            this.writeBackStages.add(new WriteBack(registerFile));
            
            //Initialization of memory
            initializeMemory();
            initializePipeline();

            gui.clearCycleLog();

            this.numberFetch = 0;
            
        }

        private void initializeMemory() {
        	new ElfFileParser(memory);
        }



		/*******************************
         * Function that perform one step on the simulator
         ******************************/
        public void step() throws PipelineException
        {
            if (clock_cycle < sim_cycles && !caught_break)
            {

            	//We store the current fetched instruction number to detect stall
            	long numberFetchedInstrBefore = this.numberFetch;
            	//We perform the cycle in the pipeline
                caught_break = simulateCycle();


                stat.countCycle();
                
                CycleDescription cycleDescription = new CycleDescription(clock_cycle);
                cycleDescription.addPipelineState(this.fetchStages.get(0).getCurrentInstruction(), GUI_CONST.FETCH);
                cycleDescription.addPipelineState(this.decodeStages.get(0).getCurrentInstruction(), GUI_CONST.DECODE);
                for (Execute oneExecuteStage : executeStages)
                	cycleDescription.addPipelineState(oneExecuteStage.getCurrentInstruction(), GUI_CONST.EXECUTE);
                for (Memory oneMemoryStage : memoryStages)
                	cycleDescription.addPipelineState(oneMemoryStage.getCurrentInstruction(), GUI_CONST.MEMORY);
                cycleDescription.addPipelineState(this.writeBackStages.get(0).getCurrentInstruction(), GUI_CONST.WRITEBACK);
                cycleDescription.setFetchedInstruction(this.fetchStages.get(0).getCurrentInstruction());
                if (numberFetchedInstrBefore == this.numberFetch)
                	cycleDescription.setStall(true);


                gui.addCycleLog(cycleDescription);            	
            }
            else if (caught_break)
            {


                // -print out selected memory
                // -check assumptions of configuration file
                finished = true;
            }
            else
            {


                finished = true;
            }

            clock_cycle++;
        }

        /**
         * Simulates one cycle of the pipeline
         *
         * @return true if a break instruction was caught and the simulation shall
         * be stopped, else false.
         * @throws DecodeStageException
         */
        public boolean simulateCycle() throws PipelineException
        {
            boolean caught_break = false;

        	
            
            this.writeBackStages.get(0).setCurrentInstruction(this.memoryStages.get(this.memoryStages.size() - 1).getCurrentInstruction());
            
            for (int i=this.memoryStages.size()-1; i>0; i--)
            	this.memoryStages.get(i).setCurrentInstruction(this.memoryStages.get(i-1).getCurrentInstruction());
            this.memoryStages.get(0).setCurrentInstruction(this.executeStages.get(this.getExecuteStages().size() - 1).getCurrentInstruction());

            for (int i=this.executeStages.size()-1; i>0; i--)
            	this.executeStages.get(i).setCurrentInstruction(this.executeStages.get(i-1).getCurrentInstruction());


            //We check if decode step is stalled to decide if execute stage take result from decode or a bubble
            if (this.decodeStages.get(0).isStalled()){
            	this.executeStages.get(0).setCurrentInstruction(createBubble());
            	//We do not modify the current instruction in decode and fetch stages
            }
            else {
            	this.executeStages.get(0).setCurrentInstruction(this.decodeStages.get(0).getCurrentInstruction());
                this.decodeStages.get(0).setCurrentInstruction(this.fetchStages.get(0).getCurrentInstruction());
                this.fetchStages.get(0).doFetch(this.numberFetch);
                this.numberFetch++;
            }
            
            //We check if a jump has been made
            if (this.executeStages.get(this.executeStages.size() - 1).hasJumped()){
            	
            	//If we just made a freeze, we need to refetch instruction
                if (this.decodeStages.get(0).isStalled()){
	            	this.fetchStages.get(0).doFetch(this.numberFetch);
	                this.numberFetch++;
                }

            	this.decodeStages.get(0).setCurrentInstruction(createBubble());
            	for (Execute oneExecuteStage : this.executeStages)
            		oneExecuteStage.setCurrentInstruction(createBubble());
            }
            else if (this.decodeStages.get(0).hasJumped()){
            	//We clear previous cycle (current decode)
            	this.decodeStages.get(0).setCurrentInstruction(createBubble());
            	
            }
        	this.decodeStages.get(0).setStalled(false);


            //We perform each step
            this.writeBackStages.get(0).doStep();
            
            for (Memory oneMemory : this.memoryStages)
            	oneMemory.doStep();

            for (Execute oneExecute : this.executeStages)
            	oneExecute.doStep();
            
            this.decodeStages.get(0).doStep();
  
            //We need to verify if system stall or not. 
            ArrayList<Integer> writtenRegisters = new ArrayList<Integer>();
            for (Memory oneMemory : this.memoryStages){
            	int usedRegister = oneMemory.getCurrentInstruction().getWrittenRegister();
            	if (usedRegister != -1)
            		writtenRegisters.add(usedRegister);
            }
            
            for (Execute oneExecute : this.executeStages){
            	int usedRegister = oneExecute.getCurrentInstruction().getWrittenRegister();
            	if (usedRegister != -1)
            		writtenRegisters.add(usedRegister);
            }
            
            writtenRegisters.retainAll(this.decodeStages.get(0).getCurrentInstruction().getUsedRegisters());
            if (writtenRegisters.size() != 0)
            	this.decodeStages.get(0).setStalled(true);
            
            //We check if WB stage is executing a trap.
            if (writeBackStages.get(0).getCurrentInstruction().getOp().equals(niosOpCode.RTYPE) && writeBackStages.get(0).getCurrentInstruction().getOpx().equals(niosOpxCode.trap))
            	this.finished = true;
            
            return caught_break;
        }

        private Instruction createBubble(){
        	//Nop instruction is op=0x3a opx=0x2c
        	return new Instruction(0x0e03a, -1, -1);
        }
        
        public int getSimCycles()
        {
            return this.sim_cycles;
        }

        public int getCurrentCycle()
        {
            return this.clock_cycle;
        }

        private void initializePipeline()
        {
           this.fetchStages.get(0).setCurrentInstruction(createBubble());
           this.decodeStages.get(0).setCurrentInstruction(createBubble());
           
           for (Execute oneExecute : executeStages)
        	   oneExecute.setCurrentInstruction(createBubble());
           
           for (Memory oneMemory : this.memoryStages)
        	   oneMemory.setCurrentInstruction(createBubble());
           
           this.writeBackStages.get(0).setCurrentInstruction(createBubble());
        }



        private void setDefaultConfigParameters(Properties config) throws PipelineException
        {
            if (!config.containsKey("file"))
            {
                config.setProperty("file", "./config.cfg");
            }

            if (!config.containsKey("entry_point"))
            {
            	config.setProperty("entry_point", "0");
            }

            if (!config.containsKey("code_start_addr"))
            {
//                config.setProperty("code_start_addr", "0x00400174");
            	config.setProperty("code_start_addr", "0");
            }

            if (!config.containsKey("cycles"))
            {
                config.setProperty("cycles", Integer.toString(ArchCfg.max_cycles));
            }
            
            if (!config.containsKey("numberexecutestage"))
            {
                config.setProperty("numberexecutestage", Integer.toString(ArchCfg.execute_stage));
            }

            if (!config.containsKey("log4j"))
            {
                config.setProperty("log4j", "./log4j.properties");
            }

            if (!config.containsKey("print_file"))
            {
                config.setProperty("print_file", "printf.out");
            }

            if (!config.containsKey("memory_latency"))
            {
                config.setProperty("memory_latency", "0");
            }

            if (!config.containsKey("icache_use"))
            {
                config.setProperty("icache_use", "0");
            }

            if (!config.containsKey("dcache_use"))
            {
                config.setProperty("dcache_use", "0");
            }

            if (!config.containsKey("isa_type"))
            {
                // default ISA is MIPS
                config.setProperty("isa_type", "MIPS");
            }

            if (!config.containsKey("use_forwarding"))
            {
                config.setProperty("use_forwarding", "TRUE");

            }

        }

        public boolean isFinished()
        {
            return finished;
        }

        public Properties getConfig()
        {
            return config;
        }

        public void stopSimulation(boolean error)
        {

            finished = true;
        }
        
        public NiosMemory getMemory(){
        	return this.memory;
        }
        
        public RegisterFile getRegisterFile(){
        	return this.registerFile;
        }

}
