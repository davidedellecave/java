package com.s2e.gwcr.mockup;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.s2e.gwcr.model.BdiFile;
import com.s2e.gwcr.model.BdiFileList;

import ddc.commons.jack.JackUtil;
import ddc.files.scan.ScanUtil;

@WebServlet("/download/*")
public class Download extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Download() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		MockupUtils.checkEnvVar();		
		
		String filename = null;
		if (request.getPathInfo() != null) {
			filename = request.getPathInfo().replaceAll("/", "");
		}

		// download file
		if (filename != null && filename.length() > 0) {			
			Path path = MockupUtils.getReposPath().resolve(filename);
			
			resp.setContentType("application/octet-stream");
			resp.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", filename));
			InputStream in = new FileInputStream(path.toFile());
			if (!Files.exists(path)) {
				resp.setContentType("text/plain");
				in = new ByteArrayInputStream(("file not found:" + path.toString()).getBytes());
			}
			resp.setContentType("application/json");
			IOUtils.copy(in, resp.getOutputStream());
		} else {
			
			BdiFileList list = new BdiFileList();
			List<Path> paths;
			try {
				paths = ScanUtil.getFiles(MockupUtils.getReposPath());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new IOException(e);
			}
			for (Path p : paths) {
				BdiFile f = new BdiFile();
				list.getFiles().add(f);
				f.setId("");
				f.setPath("");
				
				f.setFileName(p.getFileName().toString());
				f.setIsDirectory(Files.isDirectory(p));
				f.setIsOther(false);
				f.setPermissions(644);
				f.setIsRegularFile(Files.isRegularFile(p));
				f.setIsSymbolicLink(Files.isSymbolicLink(p));
				f.setLastModifiedTime(Files.getLastModifiedTime(p).toMillis());				
				f.setSize(Files.size(p));
			}
			
			String data = JackUtil.convAsPrettifiedString(list);
			
			
			
			resp.getWriter().append(data);
		}
	}

}
