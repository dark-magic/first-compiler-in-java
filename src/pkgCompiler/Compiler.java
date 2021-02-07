package pkgCompiler;

import pkgNameLookup.NameLookupTable;
import pkgParser.Block;
import pkgScanner.Scanner;

public class Compiler {

	public static void main(String[] args) {
		String filename = "test_file.c"; // default file to compile

		if (args.length == 1)
			filename = args[0]; // optional parameter
		try {
			if (compile(filename))
				System.out.println(filename + " successfully compiled");
			else
				System.out.println(filename + " NOT successfully compiled");
		} catch (java.io.FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static boolean compile(String filename) throws java.io.FileNotFoundException {
		Block b = new Block(new Scanner(filename));
		
		boolean isOk = b.parse();
		
		if(isOk) {
			System.out.println("parse successfull!");
			isOk = b.checkSemantics(new NameLookupTable());
			
			if(isOk) {
				System.out.println("semantic check successfull!");
				isOk = b.generate(0);
				
				if(isOk) {
					System.out.println("generate successfull!");
				}
			}
		}
		
		return isOk;
	}

}
