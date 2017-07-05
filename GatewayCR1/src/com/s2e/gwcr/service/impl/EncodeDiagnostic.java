package com.s2e.gwcr.service.impl;

public class EncodeDiagnostic {
	private byte[] zipBytes = null;
	private byte[] p7eBytes = null;
	private byte[] p7mBytes = null;

	public byte[] getZipBytes() {
		return zipBytes;
	}

	public void setZipBytes(byte[] zipBytes) {
		this.zipBytes = zipBytes;
	}

	public byte[] getP7eBytes() {
		return p7eBytes;
	}

	public void setP7eBytes(byte[] p7eBytes) {
		this.p7eBytes = p7eBytes;
	}

	public byte[] getP7mBytes() {
		return p7mBytes;
	}

	public void setP7mBytes(byte[] p7mBytes) {
		this.p7mBytes = p7mBytes;
	}

}
