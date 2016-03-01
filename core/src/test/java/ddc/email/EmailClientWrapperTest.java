package ddc.email;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import ddc.util.DateUtil;
import ddc.util.FileUtil;

public class EmailClientWrapperTest {

	@Before
	public void setUp() throws Exception {
	}

//	Host = fast.smtpok.com
//			Port = 25, 80, 1025-5000
//			Username = s14955_0
//			Password = trasmessa via SMS
//			Sender Email = info.cemit@cemit.it

//	private String getBody() throws IOException {
//		Path path = Paths.get("/Users/dellecave/dev/git/innovation_prove/infohub-console/src/test/conf/it.ame.ih.console.abilityart/report-email.html");
//		String template = FileUtil.loadContent(path);
//		
//		template = template.replace("${date}", DateUtil.formatNowToISO());
//		template = template.replace("${entity}", "TCustomer");
//		template = template.replace("${sourcefile}", "sourcefile");
//		template = template.replace("${errorfile}", "errorfile");
//		template = template.replace("${processed}", "-1");
//		template = template.replace("${succedeed}",  "-1");
//		template = template.replace("${failed}",  "-1");
//		return template;
//	}
//	
//	private String getSubject() throws IOException {
//		String template = "SPAM Ingestion - ${entity} - Processed:[${processed}] Failed:[${failed}]";
//		template = template.replace("${date}", DateUtil.formatNowToISO());
//		template = template.replace("${entity}", "TCustomer");
//		template = template.replace("${sourcefile}", "sourcefile");
//		template = template.replace("${errorfile}", "errorfile");
//		template = template.replace("${processed}", "-1");
//		template = template.replace("${succedeed}",  "-1");
//		template = template.replace("${failed}",  "-1");		
//		return template;
//	}
//	
//	@Test
//	public void testSend() throws LiteMailException, IOException {
//		LiteMailConfig c = new LiteMailConfig();
//		c.setSmtpHost("fast.smtpok.com");
//		//SMTP – port 25 or 2525 or 587
//		c.setPort(25);
//		c.setUsername("s14955_0");
//		c.setPassword("dgHIH.DqRm");
//		c.setFrom("info.cemit@cemit.it");
//		c.setSubject(getSubject());		
//		c.addTo("davide.dellecave@gmail.com");
//		c.addTo("davide.dellecave@mondadori.it");		
//		c.setBody(getBody());
//		c.setHtmlEnabled(true);
////		c.setSsl(true);
//		LiteMail e = new LiteMail();
//		e.send(c);
//	}
	
	@Test
	public void testSend2() throws LiteMailException, IOException {
		LiteMailConfig c = new LiteMailConfig();
		c.setSmtpHost("smtp100.ext.armada.it");
		//SMTP – port 25 or 2525 or 587
		c.setPort(25);
		c.setUsername("SMTP-BASIC-9476");
		c.setPassword("m877UsKn8s");
		c.setFrom("info@medisportgottardo.it");
		c.addTo("davide.dellecave@gmail.com");
		c.setSubject("Test subject");		
		c.setBody("Test body");
		c.setHtmlEnabled(false);
//		c.setSsl(true);
		LiteMail e = new LiteMail();
		e.send(c);
	}
}
