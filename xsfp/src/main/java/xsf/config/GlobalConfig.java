package xsf.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Properties;


public class GlobalConfig {
	private static GlobalConfig thisInstance = null;
	
	private GlobalConfig() {
		String configFile = "config.properties";
		Properties prop = new Properties();
		InputStream in = getClass().getResourceAsStream("/" + configFile);
		try {
			prop.load(in);
			in.close();
			this.xsfPath = prop.getProperty("xsfPath");
			this.processedXsfPath = prop.getProperty("processedXsfPath");
			this.jdbcDriver = prop.getProperty("jdbcDriver");
			this.dbUrl = prop.getProperty("dbUrl");
			this.dbUser = prop.getProperty("dbUser");
			this.dbPassword = new String(Base64.getDecoder().decode(prop.getProperty("dbPassword"))) ;
			String temp = prop.getProperty("threadPoolSize");
			this.threadPoolSize = Integer.parseInt(temp);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		
	}
	
	public static GlobalConfig getInstance() {
		if( thisInstance == null ) {
			thisInstance = new GlobalConfig();
		}
		return thisInstance;
	}
	
	public GlobalConfig getThisInstance() {
		return thisInstance;
	}

	public String getXsfPath() {
		return xsfPath;
	}

	public String getProcessedXsfPath() {
		return processedXsfPath;
	}

	public String getJdbcDriver() {
		return jdbcDriver;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public String getDbUser() {
		return dbUser;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public int getThreadPoolSize() {
		return threadPoolSize;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GlobalConfig [xsfPath=");
		builder.append(xsfPath);
		builder.append(", processedXsfPath=");
		builder.append(processedXsfPath);
		builder.append(", jdbcDriver=");
		builder.append(jdbcDriver);
		builder.append(", dbUrl=");
		builder.append(dbUrl);
		builder.append(", dbUser=");
		builder.append(dbUser);
		builder.append(", dbPassword=");
		builder.append("******");
		builder.append(", threadPoolSize=");
		builder.append(threadPoolSize);
		builder.append("]");
		return builder.toString();
	}

	private String xsfPath = "C:\\\\Temp\\xsf";
	private String processedXsfPath = "C:\\\\Temp\\XSF\\processed";
	private String jdbcDriver = "org.postgresql.Driver";
	private String dbUrl = "jdbc:postgresql://localhost:5439/test";
	private String dbUser = "rehan";
	private String dbPassword = "myPass";
	private int threadPoolSize = 10;

}
