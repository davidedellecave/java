package ddc.csv;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URL;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.StringUtils;

import ddc.util.Chronometer;

public class LiteCsv {

	public static void main(String[] args) throws IOException {
		Chronometer chron = new Chronometer();
		LiteCsv c = new LiteCsv();
		
		String source = "/Users/dellecave/data/anobii/anobii.csv.gz";
		
		c.read(args[0]);
		System.out.println(chron.toString());
	}
	
	public void read(String source) throws IOException {
		String target = source + ".out.gz";
		char newSep = ',';
		char replaceSep = ' ';
		File fTarget = new File(target);

		URL url = new File(source).toURI().toURL();
		final Reader reader = new InputStreamReader(new BOMInputStream( new GZIPInputStream(url.openStream())));
		final CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader());
		
		GZIPOutputStream out = new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(fTarget)));

		Iterator<CSVRecord> iter = parser.iterator();
		while (iter.hasNext()) {
			CSVRecord record = (CSVRecord) iter.next();			
			for (int i=0; i<record.size(); i++) {
				String v = record.get(i);
				v= StringUtils.replaceChars(v, newSep, replaceSep);
				out.write(v.getBytes());
				if (i<record.size()-1) {
					out.write(',');	
				}				
			}
			out.write('\n');
		}
		out.close();
		parser.close();
	}

}
