package elfParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import niosSimulator.NiosMemory;
import niosSimulator.NiosValue32;
import niosSimulator.NiosValue8;

public class ElfFileParser {
	

	
	public ElfFileParser(NiosMemory memory){
		byte[] elfBytes=null;
		try {
			elfBytes = Files.readAllBytes(Paths.get("./", "./file.elf"));
		} catch (IOException e) {
			System.err.println("Error while loading elf file !");
			e.printStackTrace();
		}
		
		byte instructionSize = elfBytes[4];
		byte endianness = elfBytes[5];
		byte abi = elfBytes[7];
		
		
		
		long placeOfSectionTable = toUnsignedInt(elfBytes[32], elfBytes[33], elfBytes[34], elfBytes[35]);
		long sizeOfSectionHeader = toUnsignedShort(elfBytes[46], elfBytes[47]);
		long numberSections = toUnsignedShort(elfBytes[48], elfBytes[49]);
		long indexSymbolTable = toUnsignedShort(elfBytes[50], elfBytes[51]);
		ArrayList<String> names = new ArrayList<String>();
		long startOfSymbolTable = toUnsignedInt(elfBytes[(int) (placeOfSectionTable + indexSymbolTable * sizeOfSectionHeader + 16)], elfBytes[(int) (placeOfSectionTable + indexSymbolTable * sizeOfSectionHeader + 17)], elfBytes[(int) (placeOfSectionTable + indexSymbolTable * sizeOfSectionHeader + 18)], elfBytes[(int) (placeOfSectionTable + indexSymbolTable * sizeOfSectionHeader + 19)]);
		int indexElf=(int) startOfSymbolTable, indexSection=0;

		while (indexSection<numberSections){
			//We build string corresponding to section name
			String newString = "";
			while (elfBytes[(int) (indexElf)] != 0){
				newString += (char) elfBytes[indexElf];
				indexElf++;
			}
			names.add(newString);
			indexElf++;
			indexSection++;
			
		}
		int destination = 0;

		//On current version of the code, we will only write .text and .data sections on memory
		//We start by writing .text
		for (int sectionNumber=0; sectionNumber< numberSections; sectionNumber++){
				
				long indexOfName = toUnsignedInt(elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 0)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 1)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 2)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 3)]);
				
				if (getStringFrom(indexOfName+startOfSymbolTable, elfBytes).equals(".text")){
				
				long startOfTextSection = toUnsignedInt(elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 16)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 17)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 18)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 19)]);
				
				long sizeOfSection = toUnsignedInt(elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 20)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 21)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 22)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 23)]);
				

				
				for (long byteNumber=startOfTextSection; byteNumber<startOfTextSection+sizeOfSection; byteNumber++){
					memory.set(destination, new NiosValue8(toUnsignedChar(elfBytes[(int) byteNumber])));
					destination++;
				}
			}
		}

		long startOfDataSection = destination;
		//Then we do the same for data section
		for (int sectionNumber=0; sectionNumber< numberSections; sectionNumber++){
			
			long indexOfName = toUnsignedInt(elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 0)], 
					elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 1)], 
					elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 2)], 
					elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 3)]);
			
			if (getStringFrom(indexOfName+startOfSymbolTable, elfBytes).equals(".data")){
			
				long startOfTextSection = toUnsignedInt(elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 16)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 17)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 18)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 19)]);
				
				long sizeOfSection = toUnsignedInt(elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 20)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 21)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 22)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 23)]);
				
	
				
				for (long byteNumber=startOfTextSection; byteNumber<startOfTextSection+sizeOfSection; byteNumber++){
					memory.set(destination, new NiosValue8(toUnsignedChar(elfBytes[(int) byteNumber])));
					destination++;
				}
			}
			
			
		}
		//We handle relocation of .text section
		for (int sectionNumber=0; sectionNumber< numberSections; sectionNumber++){
			
			long indexOfName = toUnsignedInt(elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 0)], 
					elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 1)], 
					elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 2)], 
					elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 3)]);
			
			if (getStringFrom(indexOfName+startOfSymbolTable, elfBytes).equals(".rela.text")){
				
				long startOfRelaSection = toUnsignedInt(elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 16)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 17)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 18)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 19)]);
				
				long sizeOfSection = toUnsignedInt(elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 20)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 21)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 22)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 23)]);
				
	
				
				for (long relaNumber = 0; relaNumber<sizeOfSection; relaNumber+=12){
					long relocationAddress = toUnsignedInt(elfBytes[(int) (startOfRelaSection + relaNumber + 0)], elfBytes[(int) (startOfRelaSection + relaNumber + 1)], elfBytes[(int) (startOfRelaSection + relaNumber + 2)], elfBytes[(int) (startOfRelaSection + relaNumber + 3)]);
					long relocationValue = startOfDataSection; //Current version can only relocate a symbol
					long relocationInfo = toUnsignedInt(elfBytes[(int) (startOfRelaSection + relaNumber + 4)], elfBytes[(int) (startOfRelaSection + relaNumber + 5)], elfBytes[(int) (startOfRelaSection + relaNumber + 6)], elfBytes[(int) (startOfRelaSection + relaNumber + 7)]);
					long relocationType = relocationInfo & 0xff;
					
					if (relocationType == 11){
						//This is a %hiadj relocation
						long value = ((relocationValue >> 16) & 0xffff) + ((relocationValue >> 15) & 0x01);
						value = value << 6; //We align the value with the immediate field of IType expressions
						
						long instruction = memory.loadWord(relocationAddress).getUnsignedValue(); //This is the previous instruction
						instruction = instruction | value; //We add the immediate value
						memory.setWord(relocationAddress, new NiosValue32(instruction, false));
						
					}
					else if (relocationType == 10){
						//This is a %lo relocation
						long value = relocationValue & 0xffff;
						value = value << 6; //We align the value with the immediate field of IType expressions
						
						long instruction = memory.loadWord(relocationAddress).getUnsignedValue(); //This is the previous instruction
						instruction = instruction | value; //We add the immediate value

						memory.setWord(relocationAddress, new NiosValue32(instruction, false));
					}
					else if (relocationType == 3){
						//This is a %pcrel16 relocation
						long value = relocationValue & 0xffff;
						value = value - relocationAddress;
						value = value << 6; //We align the value with the immediate field of IType expressions
						
						long instruction = memory.loadWord(relocationAddress).getUnsignedValue(); //This is the previous instruction
						instruction = instruction | value; //We add the immediate value

						memory.setWord(relocationAddress, new NiosValue32(instruction, false));
					}
					
					System.out.println("Relocating (type " + Long.toHexString(relocationType)+ ") at address 0x" + Long.toHexString(relocationAddress) + " value 0x" + Long.toHexString(relocationValue));
					
				}
			}
		}

	}
	
	private long toUnsignedInt(byte byte1, byte byte2, byte byte3, byte byte4){
		long v1 = ((long) byte1) & 0xff;
		long v2 = ((long) byte2) & 0xff;
		long v3 = ((long) byte3) & 0xff;
		long v4 = ((long) byte4) & 0xff;
		
		return v1+ (v2<<8) + (v3<<16) + (v4<<24);
		
	}
	
	private long toUnsignedShort(byte byte1, byte byte2){
		long v1 = ((long) byte1) & 0xff;
		long v2 = ((long) byte2) & 0xff;
;
		
		return v1+ (v2<<8);
		
	}
	
	private long toUnsignedChar(byte byte1){
		long v1 = ((long) byte1) & 0xff;

		
		return v1;
		
	}
	
	private String getStringFrom(long byteOffset, byte[] elfBytes){
		String result = "";
		while (elfBytes[(int) byteOffset] != 0){
			result += (char) elfBytes[(int) byteOffset];
			byteOffset++;
		}
		return result;
	}
}
