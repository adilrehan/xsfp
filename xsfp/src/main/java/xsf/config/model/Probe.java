package xsf.config.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import xsf.encryption.Encryptor;

public class Probe {
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Probe [hostName=");
		builder.append(hostName);
		builder.append(", udPath=");
		builder.append(udPath);
		builder.append(", domain=");
		builder.append(domain);
		builder.append(", userName=");
		builder.append(userName);
		builder.append(", password=");
		builder.append(password);
		builder.append("]");
		return builder.toString();
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getUdPath() {
		return udPath;
	}
	public void setUdPath(String udPath) {
		this.udPath = udPath;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@JsonIgnore
	public String getPasswordDecrypted() {
		if( isPasswordEncrypted() ) {
			return Encryptor.decrypt(this.password.substring(5));
		} else {
			return this.password;
		}
	}
	
	@JsonIgnore
	public void setPasswordEncrypted( String passwordNew ) {
		System.out.println("*** setDbPasswordEncrypted >>>> " + passwordNew);
		this.password = "[ENC]" + Encryptor.encrypt(passwordNew);
	}
	
	@JsonIgnore
	public boolean isPasswordEncrypted() {
		if( this.password != null && this.password.length() > 5 &&  this.password.startsWith("[ENC]") ) {
			return true;
		} else {
			return false;
		}		
	}	
	
	
	private String hostName;
	private String udPath;
	private String domain;
	private String userName;
	private String password;
	

}
