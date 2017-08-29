package com.s2e.gwcr.model.entity;

import java.util.ArrayList;
import java.util.List;

public class AbiContext {
	private String code;
	private String description;
	private List<CertificateItem> certificates = new ArrayList<>();
	private String bdiEndpoint;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<CertificateItem> getCertificates() {
		return certificates;
	}
	public void setCertificates(List<CertificateItem> certificates) {
		this.certificates = certificates;
	}
	public String getBdiEndpoint() {
		return bdiEndpoint;
	}
	public void setBdiEndpoint(String bdiEndpoint) {
		this.bdiEndpoint = bdiEndpoint;
	}
	
	@Override
	public String toString() {
		return "code:["+ code+ "] endpoint:[" + bdiEndpoint+ "] certificates#:[" + certificates.size()+"]";
	}

}
