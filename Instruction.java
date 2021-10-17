package project;

public enum Instruction {
	NOP(0) {
		@Override
		public void execute(int arg) {
			processor.incIP();
			// increment the IP by calling processor.incIP();
		}
	},
	NOT(1) {
		@Override
		public void execute(int arg) {
			processor.applyNot();
			processor.incIP();
			// applyNot in the processor
			// increment the IP
		}		
	},
	LOD_IMM(2) {
		@Override
		public void execute(int arg) {
			int temp = arg;
			processor.setAcc(temp);
			processor.incIP();
			// set the accumulator of the processor to temp
			// increment the IP
		}
	},
	LOD_DIR(3) {
		@Override
		public void execute(int arg) {
			int temp = memory.getData(arg);
			processor.setAcc(temp);
			processor.incIP();
			// set the accumulator of the processor to temp
			// increment the IP
		}		
	},
	LOD_IND(4) {
		@Override
		public void execute(int arg) {
			int temp = memory.getData(memory.getData(arg));
			processor.setAcc(temp);
			processor.incIP();
			// set the accumulator of the processor to temp
			// increment the IP
		}		
	},
	STO_DIR(5) {
		@Override
		public void execute(int arg) {
			int index = arg;
			int value = processor.getAcc();
			memory.setData(index, value);
			processor.incIP();
			// call setData on memory with index and value
			// increment the IP
		}
	},
	STO_IND(6) {
		@Override
		public void execute(int arg) {
			int index = memory.getData(arg);
			int value = processor.getAcc();
			memory.setData(index, value);
			processor.incIP();

			// call setData on memory with index and value
			// increment the IP
		}
	},
	ADD_IMM(7) {
		@Override
		public void execute(int arg) {
			int temp = arg;
			processor.setAcc(processor.getAcc()+temp);
			processor.incIP();
			// change the accumulator by adding temp
			// increment the IP
		}				
	},
	ADD_DIR(8) {
		@Override
		public void execute(int arg) {
			int temp = memory.getData(arg);
			processor.setAcc(processor.getAcc()+temp);
			processor.incIP();
			// change the accumulator by adding temp
			// increment the IP
		}						
	},
	ADD_IND(9) {
		@Override
		public void execute(int arg) {
			int temp = memory.getData(memory.getData(arg));
			processor.setAcc(processor.getAcc()+temp);
			processor.incIP();
			// change the accumulator by adding temp
			// increment the IP
		}								
	},
	SUB_IMM(10) {
		@Override
		public void execute(int arg) {
			int temp = arg;
			processor.setAcc(processor.getAcc()-temp);
			processor.incIP();
		}	
		// copy from ADD_IMM and change + to -
	},
	SUB_DIR(11) {
		@Override
		public void execute(int arg) {
			int temp = memory.getData(arg);
			processor.setAcc(processor.getAcc()-temp);
			processor.incIP();
		}
		// copy from ADD_DIR and change + to -
	},
	SUB_IND(12) {
		@Override
		public void execute(int arg) {
			int temp = memory.getData(memory.getData(arg));
			processor.setAcc(processor.getAcc()-temp);
			processor.incIP();
		}
		// copy from ADD_IND and change + to -
	},
	MUL_IMM(13) {
		@Override
		public void execute(int arg) {
			int temp = arg;
			processor.setAcc(processor.getAcc()*temp);
			processor.incIP();
		}
		// copy from ADD_IMM and change + to *
	},
	MUL_DIR(14) {
		@Override
		public void execute(int arg) {
			int temp = memory.getData(arg);
			processor.setAcc(processor.getAcc()*temp);
			processor.incIP();
		}
		// copy from ADD_DIR and change + to *
	},
	MUL_IND(15) {
		@Override
		public void execute(int arg) {
			int temp = memory.getData(memory.getData(arg));
			processor.setAcc(processor.getAcc()*temp);
			processor.incIP();
		}
		// copy from ADD_IND and change + to *
	},
	DIV_IMM(16) {
		@Override
		public void execute(int arg) {
			int temp = arg;
			if(temp == 0) {
				throw new DivideByZeroException("Division by zero");
			}
			processor.setAcc(processor.getAcc()/temp);
			processor.incIP();
		}
		// copy from ADD_IMM and change + to / but if temp is 0 throw new DivideByZeroException("Division by zero")
	},
	DIV_DIR(17) {
		@Override
		public void execute(int arg) {
			int temp = memory.getData(arg);
			if(temp == 0) {
				throw new DivideByZeroException("Division by zero");
			}
			processor.setAcc(processor.getAcc()/temp);
			processor.incIP();
		}
		// copy from ADD_DIR and change + to / but if temp is 0 throw new DivideByZeroException("Division by zero")
	},
	DIV_IND(18) {
		@Override
		public void execute(int arg) {
			int temp = memory.getData(memory.getData(arg));
			if(temp == 0) {
				throw new DivideByZeroException("Division by zero");
			}
			processor.setAcc(processor.getAcc()/temp);
			processor.incIP();
		}	
		// copy from ADD_IND and change + to / but if temp is 0 throw new DivideByZeroException("Division by zero")
	},
	AND_IMM(19) {
		@Override
		public void execute(int arg) {			   // temp * temp1 implements this:
			int temp = arg;				   // if temp is TRUE
			int temp1 = processor.getAcc();		   // and temp1 is TRUE
			if(temp * temp1 != 0) processor.setAcc(1); // set accumulator to TRUE
			else processor.setAcc(0); 	// otherwise FALSE

			processor.incIP();
			// increment the IP			   
		}		
	},
	AND_DIR(20) {
		@Override
		public void execute(int arg) {
			int temp = memory.getData(arg);
			int temp1 = processor.getAcc();		   // and temp1 is TRUE
			if(temp * temp1 != 0) processor.setAcc(1); // set accumulator to TRUE
			else processor.setAcc(0); 	// otherwise FALSE

			processor.incIP();
			// copy the rest from AND_IMM
		}		
	},
	AND_IND(21) {
		@Override
		public void execute(int arg) {
			int temp = memory.getData(memory.getData(arg));
			int temp1 = processor.getAcc();		   // and temp1 is TRUE
			if(temp * temp1 != 0) processor.setAcc(1); // set accumulator to TRUE
			else processor.setAcc(0); 	// otherwise FALSE

			processor.incIP();
			// copy the rest from AND_IMM
		}				
	},
	CMPL_DIR(22) {
		@Override
		public void execute(int arg) {
			int temp = memory.getData(arg);

			if(temp < 0) {
				processor.setAcc(1);
			}
			else {
				processor.setAcc(0);
			}
			// if temp < 0 set accumulator to 1
			// set accumulator to 0
			processor.incIP();
			// increment the IP
		}		
	},
	CMPL_IND(23) {
		@Override
		public void execute(int arg) {
			int temp = memory.getData(memory.getData(arg));
			if(temp < 0) {
				processor.setAcc(1);
			}
			else {
				processor.setAcc(0);
			}

			processor.incIP();
			// copy the rest from CMPL_DIR
		}				
	},
	CMPZ_DIR(24) {
		@Override
		public void execute(int arg) {
			int temp = memory.getData(arg);
			if(temp == 0) {
				processor.setAcc(1);
			}
			else {
				processor.setAcc(0);
			}
			// if temp is 0 set accumulator to 1
			// set accumulator to 0
			processor.incIP();
			// increment the IP
		}				
	},
	CMPZ_IND(25) {
		@Override
		public void execute(int arg) {
			int temp = memory.getData(memory.getData(arg));
			if(temp == 0) {
				processor.setAcc(1);
			}
			else {
				processor.setAcc(0);
			}
			processor.incIP();
			// copy the rest from CMPZ_DIR
		}						
	},
	JUMP_IMM(26) { // this and the next 2 JUMPS is relative to the current ip
		@Override
		public void execute(int arg) {
			int temp = arg;
			int ip = processor.getIP();

			processor.setIP(temp+ip);
			// set the IP of the processor to temp + ip
		}				
	},
	JUMP_DIR(27) {
		@Override
		public void execute(int arg) {
			int temp = memory.getData(arg);
			int ip = processor.getIP();

			processor.setIP(temp+ip);
			// copy the rest form JUMP_IMM
		}						
	},
	JUMP_IND(28) {
		@Override
		public void execute(int arg) {
			int temp = memory.getData(memory.getData(arg));
			int ip = processor.getIP();

			processor.setIP(temp+ip);
			// copy the rest form JUMP_IMM
		}								
	},
	JUMP_ABS(29) { // special absolute addressing
		@Override
		public void execute(int arg) {
			processor.setIP(memory.getData(arg));
		}						
	},
	JMPZ_IMM(30) {
		public void execute(int arg) {
			if(processor.getAcc() == 0) {
				int temp = arg;
				int ip = processor.getIP();

				processor.setIP(temp+ip);
			}
			else {
				processor.incIP();
			}
		}
		// if accumulator is 0 copy JUMP_IMM
		// else only increment the IP
	},
	JMPZ_DIR(31) {
		public void execute(int arg) {
			if(processor.getAcc() == 0) {
				int temp = memory.getData(arg);
				int ip = processor.getIP();

				processor.setIP(temp+ip);
			}
			else {
				processor.incIP();
			}

		}
		// if accumulator is 0 copy JUMP_DIR
		// else only increment the IP
	},
	JMPZ_IND(32) {
		public void execute(int arg) {
			if(processor.getAcc() == 0) {
				int temp = memory.getData(memory.getData(arg));
				int ip = processor.getIP();

				processor.setIP(temp+ip);
			}
			else {
				processor.incIP();
			}
		}
		// if accumulator is 0 copy JUMP_IND
		// else only increment the IP
	},
	JMPZ_ABS(33) {
		public void execute(int arg) {
			if(processor.getAcc() == 0) {
				processor.setIP(memory.getData(arg));
			}
			else {
				processor.incIP();
			}
		}
		// if accumulator is 0 copy JUMP_ABS
		// else only increment the IP
	},
	HALT(34) {
		@Override
		public void execute(int arg) {
			halt.accept();			
		}
	};

	private int opcode;
	private static Processor processor;
	private static Memory memory;
	private static HaltCommand halt;

	// define private static fields
	// Processor processor, Memory memory, HaltCommand halt
	// define a private int field opcode

	/**
	 * constructor
	 */
	private Instruction(int op) {
		opcode = op;
	}

	// get the Instruction for the specific opcode
	public static Instruction getInstruction(int opcode) {
		return Instruction.values()[opcode]; // values() is a method all enums
	}

	public abstract void execute(int arg);

	public int getOpcode() {
		return opcode;
	}


	public static void setProcessor(Processor processor) {
		Instruction.processor = processor;
	}

	public static void setMemory(Memory memory) {
		Instruction.memory = memory;
	}

	public static void setHalt(HaltCommand halt) {
		Instruction.halt = halt;
	}
	
	public static String getText(byte opCode, int arg) {
		String op = values()[opCode].toString();
		String a = "" + (arg < 0?"-":"") +
				Integer.toHexString(Math.abs(arg)).toUpperCase();
		if(op.contains("IND")) {
			a = "[[" + a + "]]";
			op = op.replace("_IND", "");
		} else if(op.contains("DIR")) {
			// change a and op
			a = "[" + a + "]";
			op = op.replace("_DIR", "");
		} else if(op.contains("IMM")) {
			// change op
			op = op.replace("_IMM", "");
		} else if(op.contains("ABS")) {
			// change a and op
			a = "{" + a + "}";
			op = op.replace("_ABS", "");
		}
		if(Assembler.noArgMnemonics.contains(op)) return op;
		else return op + " " + a;
	}
	

	// provide 3 static setter methods for the 3 static fields.
}
