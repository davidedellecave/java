package com.s2e.gwcr.model;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import com.s2e.gwcr.model.entity.AbiContext;

public class Pack {	
	private String name = "";
	private String path = "";
	private AbiContext abi;
	private X509Certificate remoteCert;
	private X509Certificate localCert;
	private PrivateKey localPrivateKey;
	private PackType messageType;
	private PackMetadata medadata;
	private byte[] data;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public AbiContext getAbi() {
		return abi;
	}
	public void setAbi(AbiContext abi) {
		this.abi = abi;
	}
	public X509Certificate getRemoteCert() {
		return remoteCert;
	}
	public void setRemoteCert(X509Certificate remoteCert) {
		this.remoteCert = remoteCert;
	}
	public X509Certificate getLocalCert() {
		return localCert;
	}
	public void setLocalCert(X509Certificate localCert) {
		this.localCert = localCert;
	}
	public PrivateKey getLocalPrivateKey() {
		return localPrivateKey;
	}
	public void setLocalPrivateKey(PrivateKey localPrivateKey) {
		this.localPrivateKey = localPrivateKey;
	}
	public PackType getMessageType() {
		return messageType;
	}
	public void setMessageType(PackType messageType) {
		this.messageType = messageType;
	}
	public PackMetadata getMedadata() {
		return medadata;
	}
	public void setMedadata(PackMetadata medadata) {
		this.medadata = medadata;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "path:["+ path+ "] name:[" + name+ "] abi:[" + abi+"] type:[" + messageType.toString()+ "]";
	}
}
