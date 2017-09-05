package ddc.template.rest.web.controller;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ddc.BuildInfo;

@RestController
public class Info {

	//http://localhost:6580/rest3/api/
	@RequestMapping("/")
	public String welcome() throws IOException {
		Properties props = BuildInfo.getInfo();
		return getHtml(props);
	}
	
	private String getHtml(Properties props) {
		StringBuilder sb = new StringBuilder("<html><table>");
	    Enumeration<?> e = props.propertyNames();
	    while (e.hasMoreElements()) {
	      String key = (String) e.nextElement();
	      String value = props.getProperty(key);
	      sb.append("<tr><td>").append(key).append("</td><td>").append(value).append("</td></tr>");
	    }
	    sb.append("</table></html>");
	    return sb.toString();
	}

//	// http://localhost:6580/rest3/api/hello/pippo
//	@RequestMapping("/hello/{player}")
//	public ViewDomain message(@PathVariable String player) {
//		ViewDomain msg = new ViewDomain(player, "! Hello " + player);
//		return msg;
//	}
}
