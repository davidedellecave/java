package com.s2e.gwcr.model.entity;

public class CertificateItem {
	private String alias;
	private CertificateTypeEnum type;
	private byte[] data;
	
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public CertificateTypeEnum getType() {
		return type;
	}
	public void setType(CertificateTypeEnum type) {
		this.type = type;
	} 
	
}
