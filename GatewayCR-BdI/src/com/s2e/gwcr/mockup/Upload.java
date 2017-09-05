package com.s2e.gwcr.mockup;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;


@WebServlet("/upload/*")
public class Upload extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Upload() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		MockupUtils.checkEnvVar();		
		
		String filename = null;
		if (request.getPathInfo() != null) {
			filename = request.getPathInfo().replaceAll("/", "");
		}
		
		Path path = MockupUtils.getReposPath().resolve(filename);
		FileOutputStream out = new FileOutputStream(path.toFile());
		IOUtils.copy(request.getInputStream(), out); 
		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		String appContext = request.getContextPath();
//		String servletPath = request.getServletPath();
		String filename = null;
		if (request.getPathInfo() != null) {
			filename = request.getPathInfo().replaceAll("/", "");
		}
		Path path = MockupUtils.getReposPath().resolve(filename);
		
		FileOutputStream out = new FileOutputStream(path.toFile());
		IOUtils.copy(request.getInputStream(), out); 
		out.close();
	}
}