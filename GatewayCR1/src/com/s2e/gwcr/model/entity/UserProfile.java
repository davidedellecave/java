package com.s2e.gwcr.model.entity;

import java.util.ArrayList;
import java.util.List;

public class UserProfile {
	private String fullname;
	private String ccn;
	private String phone;
	private String email;
	private List<UserAccount> accounts = new ArrayList<>();
	private List<UserProfile> delegates = new ArrayList<>();
	private List<AbiContext> abis = new ArrayList<>();
	
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getCcn() {
		return ccn;
	}
	public void setCcn(String ccn) {
		this.ccn = ccn;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<UserAccount> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<UserAccount> accounts) {
		this.accounts = accounts;
	}
	public List<UserProfile> getDelegates() {
		return delegates;
	}
	public void setDelegates(List<UserProfile> delegates) {
		this.delegates = delegates;
	}
	public List<AbiContext> getAbis() {
		return abis;
	}
	public void setAbis(List<AbiContext> abis) {
		this.abis = abis;
	}
	
	
	
}
