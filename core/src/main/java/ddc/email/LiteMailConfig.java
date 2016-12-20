package ddc.email;

import java.util.ArrayList;
import java.util.List;

import ddc.util.Clone;


public class LiteMailConfig {
	private String smtpHost="smtp.host.com";
	private String popHost="pop3.host.com";
	private int port = -1;//25, 587
	private String username="username";
	private String password="password";
	private String from="from@mail.com";
	private String subject="subject";
	private String body="body";
	private List<String> to = new ArrayList<>();
	private List<String> cc = new ArrayList<>();
	private List<String> bcc = new ArrayList<>();
	private boolean htmlEnabled = false;
	private boolean ssl = false;
	private String bodyTemplate="template-body";
	private String subjectTemplate="template-subject";
	//
	
	public LiteMailConfig clone() throws CloneNotSupportedException {
		return (LiteMailConfig) Clone.clone(this);
	}
	
	
	public String getSmtpHost() {
		return smtpHost;
	}
	public boolean isHtmlEnabled() {
		return htmlEnabled;
	}
	public void setHtmlEnabled(boolean htmlEnabled) {
		this.htmlEnabled = htmlEnabled;
	}
	public boolean isSsl() {
		return ssl;
	}
	public void setSsl(boolean ssl) {
		this.ssl = ssl;
	}
	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public List<String> getTo() {
		return to;
	}
	public void setTo(List<String> to) {
		this.to = to;
	}
	public void addTo(String to) {
		this.to.add(to);
	}
	public List<String> getCc() {
		return cc;
	}
	public void setCc(List<String> cc) {
		this.cc = cc;
	}
	public List<String> getBcc() {
		return bcc;
	}
	public void setBcc(List<String> bcc) {
		this.bcc = bcc;
	}
	public String getBodyTemplate() {
		return bodyTemplate;
	}
	public void setBodyTemplate(String bodyTemplate) {
		this.bodyTemplate = bodyTemplate;
	}
	public String getSubjectTemplate() {
		return subjectTemplate;
	}
	public void setSubjectTemplate(String subjectTemplate) {
		this.subjectTemplate = subjectTemplate;
	}


	public String getPopHost() {
		return popHost;
	}


	public void setPopHost(String popHost) {
		this.popHost = popHost;
	}

}
