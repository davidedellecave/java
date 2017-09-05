package com.s2e.gwcr.mockup;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


public class _Upload2 extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		String description = request.getParameter("description"); // Retrieves <input type="text" name="description">
		Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
		System.out.print(request.getParts());
		String fileName = "file";// Paths.get(getSubmittedFileName(filePart)).getFileName().toString(); // MSIE fix.
//		InputStream fileContent = filePart.getInputStream();

		FileOutputStream out = new FileOutputStream("/Users/davide/tmp/" + fileName);
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		String description = request.getParameter("description"); // Retrieves <input type="text" name="description">
//		Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
//		System.out.print(request.getParts());
//		String fileName = "file";// Paths.get(getSubmittedFileName(filePart)).getFileName().toString(); // MSIE fix.
//		InputStream fileContent = filePart.getInputStream();
//
//		FileOutputStream out = new FileOutputStream("/Users/davide/tmp/" + fileName);
		try {
			byte[] data = getByteArrayFromUploadFile(request);
			FileOutputStream out = new FileOutputStream("/Users/davide/tmp/" + "upload.pdf");
			out.write(data);
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String getSubmittedFileName(Part part) {
		for (String cd : part.getHeader("content-disposition").split(";")) {
			if (cd.trim().startsWith("filename")) {
				String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
				return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
			}
		}
		return null;
	}
	
	private byte[] getByteArrayFromUploadFile(HttpServletRequest request)throws Exception{
	    ServletFileUpload upload = new ServletFileUpload();

	    // Parse the request
	    FileItemIterator iter = null;
	    try{
	        iter = upload.getItemIterator(request);
	    }catch (Exception e) {
	        System.out.println("Error occured during getting iterator");
	    }
	    byte[] readBytes = null;
	    while (iter.hasNext()) {
	        FileItemStream item = null;
	        try{
	            item = iter.next();
	         }catch (Exception e) {
	            System.out.println("Error occured during Iteration");                
	        }

	        String name = item.getFieldName();

//	        if (!item.isFormField()) {
	            BufferedInputStream stream = new BufferedInputStream(item.openStream());
	            readBytes= new byte[stream.available()];
	            System.out.println("total Number of Bytes:"+ readBytes.length);             
	            stream.read(readBytes);
//	        }
	    }
	   return readBytes;
	}
}
