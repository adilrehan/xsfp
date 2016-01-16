package xsf.config.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import xsf.encryption.Encryptor;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Config [xsfPath=");
		builder.append(xsfPath);
		builder.append(", xsfPathAchieve=");
		builder.append(xsfPathAchieve);
		builder.append(", xsfTableName=");
		builder.append(xsfTableName);
		builder.append(", jdbcDriver=");
		builder.append(jdbcDriver);
		builder.append(", dbUrl=");
		builder.append(dbUrl);
		builder.append(", dbUser=");
		builder.append(dbUser);
		builder.append(", dbPassword=");
		builder.append(dbPassword);
		builder.append(", threadPoolSize=");
		builder.append(threadPoolSize);
		builder.append(", webDeploy=");
		builder.append(webDeploy);
		builder.append(", webPort=");
		builder.append(webPort);
		builder.append(", webSsl=");
		builder.append(webSsl);
		builder.append(", probes=");
		builder.append(probes);
		builder.append("]");
		return builder.toString();
	}
	
	public String getXsfPath() {
		return xsfPath;
	}
	public void setXsfPath(String xsfPath) {
		this.xsfPath = xsfPath;
	}
	public String getXsfPathAchieve() {
		return xsfPathAchieve;
	}
	public void setXsfPathAchieve(String xsfPathAchieve) {
		this.xsfPathAchieve = xsfPathAchieve;
	}	
	public String getXsfTableName() {
		return xsfTableName;
	}
	public void setXsfTableName(String xsfTableName) {
		this.xsfTableName = xsfTableName;
	}
	public String getJdbcDriver() {
		return jdbcDriver;
	}
	public void setJdbcDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}
	public String getDbUrl() {
		return dbUrl;
	}
	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}
	public String getDbUser() {
		return dbUser;
	}
	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}
	public String getDbPassword() {
		return dbPassword;
	}
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	public int getThreadPoolSize() {
		return threadPoolSize;
	}
	public void setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}
	public List<Probe> getProbes() {
		return probes;
	}
	public void setProbes(List<Probe> probes) {
		this.probes = probes;
	}
	
	@JsonIgnore
	public String getDbPasswordDecrypted() {
		if( isDbPasswordEncrypted() ) {
			return Encryptor.decrypt(this.dbPassword.substring(5));
		} else {
			return this.dbPassword;
		}
	}
	
	@JsonIgnore
	public void setDbPasswordEncrypted( String dbPasswordNew ) {
		System.out.println("*** setDbPasswordEncrypted >>>> " + dbPasswordNew);
		this.dbPassword = "[ENC]" + Encryptor.encrypt(dbPasswordNew);
	}
	
	@JsonIgnore
	public boolean isDbPasswordEncrypted() {
		if( this.dbPassword != null && this.dbPassword.length() > 5 &&  this.dbPassword.startsWith("[ENC]") ) {
			return true;
		} else {
			return false;
		}		
	}

	public String getWebDeploy() {
		return webDeploy;
	}
	public void setWebDeploy(String webDeploy) {
		this.webDeploy = webDeploy;
	}
	public int getWebPort() {
		return webPort;
	}
	public void setWebPort(int webPort) {
		this.webPort = webPort;
	}
	public String getWebSsl() {
		return webSsl;
	}
	public void setWebSsl(String webSsl) {
		this.webSsl = webSsl;
	}
	
	private String xsfPath = "xsf";
	private String xsfPathAchieve = "xsf/archive";
	private String xsfTableName = "XSF_FILE";
	private String jdbcDriver;
	private String dbUrl;
	private String dbUser;
	private String dbPassword;
	private int threadPoolSize = 10;
	private String webDeploy = "deploy/";
	private int webPort = 8080;
	private String webSsl = "false";
	private List<Probe> probes;

}
