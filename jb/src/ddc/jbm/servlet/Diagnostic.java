package ddc.jbm.servlet;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ddc.jbm.diagnostic.ResponseUtils;

/**
 * Servlet implementation class ProbeServlet
 */
public class Diagnostic extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Diagnostic() {
		super();
	}

	public static String DATE_PATTERN_ISO = "yyyy-MM-dd_HH:mm:ss";
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<String, String> map = new HashMap<String, String>();
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN_ISO);			
			map.put("timestamp", (formatter.format(new Date())));
			map.put("app-context", request.getContextPath());
			map.put("servlet", this.getClass().getName());
			map.put("host", java.net.InetAddress.getLocalHost().getHostName());
			map.put("server-name", request.getServerName());
			map.put("request-uri", request.getRequestURI());
			
			for (Map.Entry<String, String> e : System.getenv().entrySet()) {
				map.put(e.getKey(), e.getValue());	
			}
		} catch (UnknownHostException e) {
			map.put("exception", e.getMessage());
		}
		ResponseUtils u = new ResponseUtils("", "=", "[", "]", ",\n");
		//ResponseUtils u = new ResponseUtils("", "=", "[", "]", ", ");
		//ResponseUtils u = new ResponseUtils("\"", "\":", "\"", "\"", ", ");
//		ResponseUtils u = new ResponseUtils("", "=", "", "", "\n");
		response.getWriter().print(u.toString(map));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
