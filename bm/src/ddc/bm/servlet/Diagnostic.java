package ddc.bm.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

		Map<String, String> map = new TreeMap<String, String>();
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN_ISO);
			map.put("timestamp", (formatter.format(new Date())));
			map.put("req-app-context", request.getContextPath());
			map.put("req-servlet", this.getClass().getName());
			map.put("req-host", java.net.InetAddress.getLocalHost().getHostName());
			map.put("req-server-name", request.getServerName());
			map.put("req-request-uri", request.getRequestURI());
			map.put("req-path-info", request.getPathInfo());
			map.put("req-servlet-path", request.getServletPath());

			for (Map.Entry<Object, Object> e : System.getProperties().entrySet()) {
				map.put("sys-prop-" + String.valueOf(e.getKey()), String.valueOf(e.getValue()));
			}
			String confFile = System.getProperties().getProperty("java.util.logging.config.file");
			if (confFile != null) {
				Properties props = new Properties();
				props.load(new FileInputStream(new File(confFile)));
				for (Map.Entry<Object, Object> e : props.entrySet()) {
					map.put("log-prop-" + String.valueOf(e.getKey()), String.valueOf(e.getValue()));
				}
			}

			for (Map.Entry<String, String> e : System.getenv().entrySet()) {
				map.put("sys-env-" + e.getKey(), e.getValue());
			}


			map.put("app-tenant-id", Environment.getTenantId().toString());
		} catch (Throwable e) {
			map.put("exception", e.getMessage());
		}
		ResponseUtils u = new ResponseUtils("", "=", "[", "]", "\n\n");
		// ResponseUtils u = new ResponseUtils("", "=", "[", "]", ", ");
		// ResponseUtils u = new ResponseUtils("\"", "\":", "\"", "\"", ", ");
		// ResponseUtils u = new ResponseUtils("", "=", "", "", "\n");
		response.getWriter().print(u.toString(map));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
