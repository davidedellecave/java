package ddc.csv;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;

public class LiteCsv {

	public void read() throws IOException {
//		Reader in = new FileReader("/Users/dellecave/Downloads/ranking_2016-02-12.xls");
		
		String sPath = "/Users/dellecave/data/test/ranking_2016-02-12.csv";
				
		URL url = new File(sPath).toURI().toURL();
		final Reader reader = new InputStreamReader(new BOMInputStream(url.openStream()));
		final CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader());

		
//		Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
//		CSVParser parser = new CSVParser(
//			      new FileReader("test.csv"), 
//			      CSVFormat.DEFAULT.withHeader());
		
//		  for (CSVRecord record : parser) {
//			    System.out.printf("%st%sn", 
//			      record.get("COL1"), 
//			      record.get("COL2"));
//			  }
//			  parser.close();
//		CSVParser parser = new CSVParser(
//	      in, 
//	      CSVFormat.EXCEL.withHeader());
			  
		
//		Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);		
		for (CSVRecord record : parser) {
			System.out.println(record.toString());
//			String lastName = record.get("Last Name");
//			String firstName = record.get("First Name");
		}
	}
	
	public static void main(String[] args) throws IOException  {
		LiteCsv c = new LiteCsv();
		c.read();
	}
}
