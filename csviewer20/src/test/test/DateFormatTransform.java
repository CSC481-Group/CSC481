package test.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class DateFormatTransform {
	private String fileToProc;
	private int[] fieldsWithDate = {3, 10 ,11};
	private DateTimeFormatter inFormatter = 
			DateTimeFormatter.ofPattern("M/d/yyyy h:mm:ss");
			//DateTimeFormatter.ofPattern("M/d/yyyy"); //for simple test
	private DateTimeFormatter outFormatter = 
			DateTimeFormatter.ofPattern("yyyy/MM/dd");
	private int fieldCount;
	
	public DateFormatTransform(String fileToProc) {
		this.fileToProc = fileToProc;
	}
	
	public DateFormatTransform() {
		fileToProc = "data/qAnimalFamilyInfoAllInUnicode.txt";
	}
	
	public void transformAndSave(String fileToSave) {
		try {
			Scanner input = new Scanner(new File(fileToProc));
			
			PrintWriter output = new PrintWriter(fileToSave);
			
			LocalDate dateToTransform;
			String[] fields = input.nextLine().split(",");
			fieldCount = fields.length;
			printTransformedLine(output, fields);
			
			int i = 0;
			while (input.hasNextLine()) {
				fields = input.nextLine().split(",");
				
				for (i=0; i<fieldsWithDate.length; i++) {
					if (fields[fieldsWithDate[i]] != null && 
							fields[fieldsWithDate[i]].length() > 1) {
						dateToTransform = LocalDate.parse(
								fields[fieldsWithDate[i]], this.inFormatter);
						fields[fieldsWithDate[i]] = 
								outFormatter.format(dateToTransform);
					}
				}
				
				printTransformedLine(output, fields);
			}
						
			System.out.println("Processed " + i + "lines...");
			
			output.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void printTransformedLine(PrintWriter output, String[] fields) {
		int i;
		//for (i=0; i<fields.length-1; i++)
		//	output.print(fields[i] + ",");
		// starting i from 1 to skip the ID field
		for (i=1; i<this.fieldCount-1; i++)
			output.print(((i<fields.length)?fields[i]:"") + ",");
		output.println((i==fields.length-1)?fields[i]:"");
	}

	public static void main(String[] args) {
		//DateFormatTransform.quickTest();
		
		DateFormatTransform dft = new DateFormatTransform();
		dft.transformAndSave("data/test1.txt");
	}

	static void quickTest() {
		try {
			Scanner input = new Scanner(
					new File("data/DateTestInput.txt"));
			LocalDate dob, dodOrR;
			String[] fields;
			//String text; 
			//LocalDate parsedDate = LocalDate.parse(text, inFormatter);
			
			DateTimeFormatter inFormatter = 
					DateTimeFormatter.ofPattern("M/d/yyyy");
			DateTimeFormatter outFormatter = 
					DateTimeFormatter.ofPattern("yyyy/MM/dd");
			
			int[] indexes = {1, 3, 9, 10, 11};
			fields = input.nextLine().split(",");
			print(fields, indexes);
			while (input.hasNextLine()) {
				fields = input.nextLine().split(",");
				dob = LocalDate.parse(fields[3], inFormatter);
				fields[3] = outFormatter.format(dob);
				if (fields[10] != null && fields[10].length() > 1) {
						dodOrR = LocalDate.parse(fields[10], inFormatter);
						fields[10] = outFormatter.format(dodOrR);
				}
				if (fields[11] != null && fields[11].length() > 1) {
					dodOrR = LocalDate.parse(fields[11], inFormatter);
					fields[11] = outFormatter.format(dodOrR);
				}
				
				/*
				System.out.println(fields[1] + "@" + fields[3] +
				    " " + fields[9] + "@" + fields[10] + "/" + fields[11]);
				*/
				print(fields, indexes);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void print(String[] fields, int[] indexes) {
		int i;
		for (i=0; i<indexes.length-1; i++)
			System.out.print(fields[indexes[i]] + ",");
		System.out.println(fields[indexes[i]]);
	}

}
