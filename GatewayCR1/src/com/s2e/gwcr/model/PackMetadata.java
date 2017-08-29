package com.s2e.gwcr.model;

public class PackMetadata {
	private String newFilePath;
	private String Partner;
	private String MessageType;
	private String Survey;
	public String getNewFilePath() {
		return newFilePath;
	}
	public void setNewFilePath(String newFilePath) {
		this.newFilePath = newFilePath;
	}
	public String getPartner() {
		return Partner;
	}
	public void setPartner(String partner) {
		Partner = partner;
	}
	public String getMessageType() {
		return MessageType;
	}
	public void setMessageType(String messageType) {
		MessageType = messageType;
	}
	public String getSurvey() {
		return Survey;
	}
	public void setSurvey(String survey) {
		Survey = survey;
	}
	
//	Metadati da inviare:
//		"Flow_userVars.Partner": codice partner (cioè il codice intermediario segnalante)
//		"Flow_userVars.Survey": codice della rilevazione
//		"Flow_userVars.MessageType": tipo di messaggio
//		"Flow_userVars.ReportingDate": data contabile
//		"newFilePath": percorso di destinazione del file, specifico per la rilevazione CR e AS,
//		es. /upload/CR/filename.p7e.
}
