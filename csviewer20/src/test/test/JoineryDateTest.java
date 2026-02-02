package test.test;

import java.io.IOException;
import java.util.Iterator;

import joinery.DataFrame;

public class JoineryDateTest {

	public static void main(String[] args) {
		DataFrame<Object> df;
		try {
			//read a csv file
			df = DataFrame.readCsv("testdata/SimpleDateInput.txt");
			// print first few lines and the last
			System.out.println(df);
		
			// columns() returns a set, which can be iterated
			Iterator<Object> it;
			  it = df.columns().iterator();
			while (it.hasNext())
				System.out.print(it.next() + "\t");
			System.out.println();
			
			// using the get(row, col) method can get elements
			// as Object then toString() and then parse to int
			/*
			int value;
			for (int row = 0; row < 100; row++) {
				for (int col = 0; col < df.size(); col++) {
					value = Integer.parseInt(df.get(row,  col).toString());
					System.out.print("\t" + value);
				}
				System.out.println();
			}
			*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

}
