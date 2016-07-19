package ddc.core.db.mongodb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class MongoDbConfig {
	private List<String> replicasetList=new ArrayList<String>();
	private String database="";
	private String username = "";
	private String password = "";
	
	public List<String> getReplicasetList() {
		return replicasetList;
	}
	public void setReplicasetList(String replicasetList) {		
		this.replicasetList = Arrays.asList( StringUtils.split(replicasetList, ','));
	}
	
	public MongoDbConfig addReplicasetList(String host) {
		this.replicasetList.add(host);
		return this;
	}	
	public void setReplicasetList(List<String> replicasetList) {
		this.replicasetList = replicasetList;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
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
	public boolean hasCredential() {
		return (username!=null && password!=null && username.length()>0 && password.length()>0);
	}
	public String getReplicaSetList() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
