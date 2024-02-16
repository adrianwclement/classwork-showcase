package darwin;

import java.io.*;
import java.util.ArrayList;
// import java.util.Scanner;

/**
 * The individual creatures in the world are all representatives of some
 * species class and share certain common characteristics, such as the species
 * name and the program they execute. Rather than copy this information into
 * each creature, this data can be recorded once as part of the description for
 * a species and then each creature can simply include the appropriate species
 * reference as part of its internal data structure.
 * 
 * Note: The instruction addresses start at one, not zero.
 */
public class Species {
	private String name;
	private String color;
	private char speciesChar; // the first character of Species name
	private ArrayList<Instruction> program;
	private int programIterator;

	/**
	 * Create a species for the given fileReader. 
	 * 
	 * @throws IOException when file is unreadable
	 */
	public Species(BufferedReader fileReader) {
		try {
			// read name of file, read color, find species char
			String inputName = fileReader.readLine();
			this.name = inputName;
			String inputColor = fileReader.readLine();
			this.color = inputColor;
			this.speciesChar = name.charAt(0);

			// initialize new array list to hold instructtions
			this.program = new ArrayList<Instruction>();
			
			// check for end of instructions
			boolean whiteSpace = false;
			while (whiteSpace != true) {
				String line = fileReader.readLine();
				programIterator++;
				// see if next line is empty
				if (line == null || line.equals("")) {
					whiteSpace = true;
				// see if next line contains an opcode and address
				} else if (line.contains(" ")) {
					String[] instructionParts = line.split(" ");
					String opCode = instructionParts[0];
					String address = instructionParts[1];
					findInstruction(opCode, address);	
				// only if next line contains an opcode	
				} else {
					String opCode = line;
					String address = "";
					findInstruction(opCode, address);
				}
			}
		// catch issues with file reading
		} catch (IOException e) {
			System.out.println(
				"Could not read file '"
					+ fileReader
					+ "'");
			System.exit(1);
			
		}
	}
	/**
	 * Create a species for the given fileReader.
	 * @param opcode the string that corresponds to a given command number
	 * @param address the string that corresponds to a given step of the program
	 */
	public void findInstruction(String opCode, String address) {
		// switch through each possible opcode and add an instruction
		// method is the same for each case, assign an int based on opcode input, add to array list
		// sometimes also assigns new address to move to assigned step in program if address prompts
		switch (opCode) {
			case "hop":
				int code1 = 1;
				Instruction instruction1 = new Instruction(code1);
				program.add(instruction1);
				break;
			case "left": 
				int code2 = 2;
				Instruction instruction2 = new Instruction(code2);
				program.add(instruction2);
				break;
			case "right": 
				int code3 = 3;
				Instruction instruction3 = new Instruction(code3);
				program.add(instruction3);
				break;
			case "infect":
				int code4 = 4;
				if (!address.equals("")) {
					int addresscode4 = Integer.parseInt(address);
					Instruction instruction4 = new Instruction(code4, addresscode4);
					program.add(instruction4);
				} else {	
					Instruction instruction4 = new Instruction(code4, 1);
					program.add(instruction4);
				}
				break;
			case "ifempty":
				int code5 = 5;
				int addressCode5 = Integer.parseInt(address);
				Instruction instruction5 = new Instruction(code5, addressCode5);
				program.add(instruction5);
				break;
			case "ifwall":
				int code6 = 6;
				int addressCode6 = Integer.parseInt(address);
				Instruction instruction6 = new Instruction(code6, addressCode6);
				program.add(instruction6);
				break;
			case "ifsame":
				int code7 = 7;
				int addressCode7 = Integer.parseInt(address);
				Instruction instruction7 = new Instruction(code7, addressCode7);
				program.add(instruction7);
				break;
			case "ifenemy":
				int code8 = 8;
				int addressCode8 = Integer.parseInt(address);
				Instruction instruction8 = new Instruction(code8, addressCode8);
				program.add(instruction8);
				break;
			case "ifrandom":
				int code9 = 9;
				int addressCode9 = Integer.parseInt(address);
				Instruction instruction9 = new Instruction(code9, addressCode9);
				program.add(instruction9);
				break;
			case "go":
				int code10 = 10;
				int addressCode10 = Integer.parseInt(address);
				Instruction instruction10 = new Instruction(code10, addressCode10);
				program.add(instruction10);
				break;
			case "if2enemy":
				int code11 = 11;
				int addressCode11 = Integer.parseInt(address);
				Instruction instruction11 = new Instruction(code11, addressCode11);
				program.add(instruction11);
				break;						
		}
	}


	/**
	* Return the char for the species
	*/
	public char getSpeciesChar() {
		return speciesChar;
	}

	/**
	 * Return the name of the species.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return the color of the species.
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Return the number of instructions in the program.
	 */
	public int programSize() {
		return program.size();
	}

	/**
	 * Return an instruction from the program.
	 * @pre 0 <= i <= programSize().
	 * @post returns instruction i of the program.
	 */
	public Instruction programStep(int i) {
		// System.out.println(program.get(i-1));
		return program.get(i - 1);
	}

	/**
	 * Return a String representation of the program.
	 * 
	 * do not change
	 */
	public String programToString() {
		String s = "";
		for (int i = 1; i <= programSize(); i++) {
			s = s + (i) + ": " + programStep(i) + "\n";
		}
		return s;
	}

	/**
	 * Return step of that instructions current species is on
	 */
	public int getStep() {
		return programIterator;
	}
// species tests can be found in AutoGraderComptest.java
}
