package project;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

public class Assembler {

	public final static Set<String> mnemonics = Set.of("NOP","NOT","HALT","LOD","STO",
			"ADD","SUB","MUL","DIV","AND","CMPL","CMPZ","JUMP","JMPZ");

	public final static Set<String> noArgMnemonics = Set.of("NOP","NOT","HALT");

	public final static Set<String> noImmedMnemonics = Set.of("STO","CMPL","CMPZ");

	public final static Set<String> jumpMnemonics = Set.of("JUMP", "JMPZ");

	public static int assemble(String inputFileName, String outputFileName, StringBuilder error) {
		String[] source = null;
		try (Stream<String> lines = Files.lines(Paths.get(inputFileName))) {
			source = lines.toArray(String[]::new);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(error == null)
			throw new IllegalArgumentException
			("Coding error: the error buffer is null");
		Set<String> errors = new TreeSet<>();
		Encoding.resetNumNoArg();
		List<Encoding> output = new ArrayList<>();

		for(int i = 0; i < source.length; i++) {
			if(source[i].trim().length() == 0) {
				errors.add("Error on line " + (i+1) + ": Illegal blank line in the source file");
				continue;
			}
			if(source[i].length() != source[i].trim().length()) {
				errors.add("Error on line " + (i+1) + ": Illegal white space at the start or end of line");
				continue;
			}
			String[] parts = source[i].split("\\s+");
			if(parts.length != 1 && parts.length !=2) {
				errors.add("Error on line " + (i+1) + ": Too many arguments");
				continue;
			}
			if(!mnemonics.contains(parts[0].toUpperCase())) {
				errors.add("Error on line " + (i+1) + ": Mnemonic must be in upper case");
				continue;
			}
			if(mnemonics.contains(parts[0].toUpperCase())) {
				if(!mnemonics.contains(parts[0])) {
					errors.add("Error on line " + (i+1) + ": Mnemonic must be in upper case");
					continue;
				}
			}
			if(parts.length == 1 && noArgMnemonics.contains(parts[0]) ) {
				output.add(new Encoding(true,(byte)Instruction.valueOf(parts[0]).getOpcode(), 0));
				continue;
			}
			if(parts.length == 2) {
				if(noArgMnemonics.contains(parts[0])) {
					errors.add("Error on line " + (i+1) + ": Illegal argument");
					continue;
				}
				String mode = "";
				int arg = 0;

				if(parts[1].startsWith("[[") ) {
					if(!parts[1].endsWith("]]")) {
						errors.add("Error on line " + (i+1) + ": The argument [[...]] in badly formatted");
						continue;
					}
					else {
						parts[1] = parts[1].substring(2, parts[1].length() - 2) ;
						mode = "_IND";
					}
				}
				else if(parts[1].startsWith("[")) {
					if(!parts[1].endsWith("]")) {
						errors.add("Error on line " + (i+1) + ": The argument [...] in badly formatted");
						continue;
					}
					else {
						parts[1]= parts[1].substring(1, parts[1].length() - 1);
						mode = "_DIR";
					}
				}
				else if(parts[1].startsWith("{") ) {
					if(!parts[1].endsWith("}")) {
						errors.add("Error on line " + (i+1) + ": The argument {...} in badly formatted");
						continue;
					}
					else {
						parts[1]= parts[1] .substring(1, parts[1].length() - 1);
						mode = "_ABS";
					}
				}
				else {
					mode = "_IMM";
				}
				try {
					arg = Integer.parseInt(parts[1], 16);
				}
				catch(NumberFormatException e) {
					errors.add("Error on line " + (i+1) + ": The argument is not a number");
				}
				if(mode == "_IMM" && noImmedMnemonics.contains(parts[0])) {
					errors.add("Error on line " + (i+1) + ": Illegal argument for a no-argument mnemonic");
					continue;
				}
				if(mode == "_ABS" &&  !jumpMnemonics.contains(parts[0])) {
					errors.add("Error on line " + (i+1) + ": Illegal argument for a no-argument mnemonic");
					continue;
				}
				output.add(new Encoding(false, (byte)Instruction.valueOf(parts[0] + mode).getOpcode(), arg));
			}

		}
		if(errors.size() == 0) {
			int bytesNeeded = Encoding.getNumNoArg() +
					5*(output.size()-Encoding.getNumNoArg());
			ByteBuffer buff = ByteBuffer.allocate(bytesNeeded);
			output.stream()
			.forEach(instr -> {
				buff.put(instr.getOpcode());
				if(!instr.isNoArg()) {
					buff.putInt(instr.getArg());
				}
			});
			buff.rewind(); // go back to the beginning of the buffer before writing
			boolean append = false;
			try (FileChannel wChannel = new FileOutputStream(
					new File(outputFileName), append).getChannel()){
				wChannel.write(buff);
				wChannel.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Map<Integer, String> map = new TreeMap<>();
			for(String str : errors) {
			    Scanner scan = new Scanner(str.substring(14, str.indexOf(':')));
			    map.put(scan.nextInt(), str); // this code finds the line number in the error message
			}
			for(int i : map.keySet()) {
			    error.append(map.get(i) + "\n");
			} 
		}
		return errors.size();

	}

	public static void main(String[] args) {
		StringBuilder error = new StringBuilder();
		System.out.println("Enter the name of the file in the \"pasm\" folder without extension: ");
		try (Scanner keyboard = new Scanner(System.in)) { 
			String filename = keyboard.nextLine();
			int i = Assembler.assemble("pasm/" +filename + ".pasm", 
					"pexe/" + filename + ".pexe", error);
			System.out.println("error = " + error);
			System.out.println("result = " + i);
		}
		for(int i = 2; i < 16; ++i) {
			String filename = (i<10?"0":"") + i;
			Assembler.assemble("pasm/z0" + filename + "e.pasm", 
					"pexe/" + filename + ".pexe", error);
			error.append("=====================\n");
		}
		System.out.println(error);

	}
}

