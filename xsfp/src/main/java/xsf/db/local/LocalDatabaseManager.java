package xsf.db.local;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.hsqldb.server.Server;

public class LocalDatabaseManager {
	final static Logger logger = Logger.getLogger(LocalDatabaseManager.class);
	
	private static LocalDatabaseManager thisInstance;
	
	public static LocalDatabaseManager getInstance() {
		if( thisInstance == null ) {
			thisInstance = new LocalDatabaseManager();
		}
		return thisInstance;
	}

	private Server hsqlServer = null;

	private LocalDatabaseManager() {
		start();
	}

	private void start() {
		if( hsqlServer == null ) {
			if(logger.isInfoEnabled()){
			    logger.info("Starting local database");
			}		
			hsqlServer = new Server();
			hsqlServer.setLogWriter(null);
			hsqlServer.setSilent(true);
			hsqlServer.setDatabaseName(0, "xsf");
			hsqlServer.setDatabasePath(0, "file:xsfdb/xsfdb");
			hsqlServer.start();	
		}
	}
	
	public void shutdown() {
		if(logger.isInfoEnabled()){
		    logger.info("Shutting down local database");
		}
		hsqlServer.stop();
		hsqlServer = null;
	}

	private Connection connection = null;
	
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		if( connection == null ) {
			Class.forName("org.hsqldb.jdbcDriver");
			connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/xsf", "sa", "");
		}
		return connection;
	}
	
	public boolean execute( String sqlStatement ) {
		try {
			Connection connection = getConnection();
			connection.prepareStatement(sqlStatement).execute();
			return true;
		} catch( SQLException ex ) {
			ex.printStackTrace();
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		}
		return false;
	}
	
	public ResultSet executeQuery( String sqlStatement ) {
		ResultSet rs = null;
		try {
			Connection connection = getConnection();
			rs = connection.prepareStatement(sqlStatement).executeQuery();
		} catch( SQLException ex ) {
			ex.printStackTrace();
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		}
		return rs;
	}

	public String executeQueryReturnSingleValue( String sqlStatement )  {
		ResultSet rs = executeQuery( sqlStatement );
		try {
			if( rs.next() ) {
				return rs.getString(1);
			} else {
				return null;
			}
		} catch( Exception ex ) {
			ex.printStackTrace();
			return null;
		}
	}
	public long executeQueryReturnNumericValue( String sqlStatement ) {
		String value = executeQueryReturnSingleValue( sqlStatement );
		if( value == null ) {
			return 0;
		} else {
			return (new Long(value)).longValue();
		}
	}	

	/***
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	
	public void initializeDatabase() throws ClassNotFoundException, SQLException {
		if(logger.isInfoEnabled()){
		    logger.info("Initializing local database");
		}
		long value = executeQueryReturnNumericValue("select count(*) from INFORMATION_SCHEMA.tables where table_name='XSFSOURCEFILES'");
		if( value < 1 ) {
			if(logger.isInfoEnabled()){
			    logger.info("Creating schema for local database");
			}
			createSchema();
		}
	}
		
	public void createSchema() throws ClassNotFoundException, SQLException {
		Connection connection = getConnection();
		connection.prepareStatement("drop table xsfSourceFiles if exists;").execute();
		connection.prepareStatement("create table xsfSourceFiles( hostname varchar(256) NOT NULL, fileName varchar(256) NOT NULL, modifyDate BIGINT, fetchDate BIGINT, processDate BIGINT,  PRIMARY KEY (hostname, fileName) );").execute();
	}
	
	public void dropSchema() throws ClassNotFoundException, SQLException {
		if(logger.isInfoEnabled()){
		    logger.info("Droping schema for local database");
		}
		Connection connection = getConnection();
		connection.prepareStatement("drop table xsfSourceFiles if exists;").execute();
	}		
	
	
	public void addfile( String hostName, String fileName, long modifyDate) {
		String sqlStatement = String.format("SELECT COUNT(*) FROM xsfSourceFiles WHERE hostname='%s' and fileName='%s';", hostName, fileName );
		long value = executeQueryReturnNumericValue(sqlStatement);
		//System.out.println( ">>>> " + value );
		if( value < 1 ) {
			if(logger.isDebugEnabled()){
			    logger.debug( String.format("Adding record : %s - %s", hostName, fileName));
			}
			sqlStatement = String.format("INSERT INTO xsfSourceFiles( hostname, fileName, modifyDate ) values ('%s', '%s', %s);", hostName, fileName, modifyDate );
			execute(sqlStatement);
		} else {
			if(logger.isDebugEnabled()){
			    logger.debug(String.format("Updating record : %s - %s", hostName, fileName));
			}			
			updateModifyDate(hostName, fileName, modifyDate);
		}
	}
	
	public long getModifyDate( String hostName, String fileName ) {
		String sqlStatement = String.format("SELECT modifyDate FROM xsfSourceFiles WHERE hostname='%s' and fileName='%s';", hostName, fileName );
		long value = executeQueryReturnNumericValue(sqlStatement);		
		return value;
	}
	
	public void updateModifyDate( String hostName, String fileName, long modifyDate) {
		String sqlStatement = String.format("update xsfSourceFiles set modifyDate=%s where hostname='%s' and fileName='%s';", modifyDate, hostName, fileName );
		execute(sqlStatement);
	}	
	
	public void updateFetchDate( String hostName, String fileName, long fetchDate) {
		String sqlStatement = String.format("update xsfSourceFiles set fetchDate=%s where hostname='%s' and fileName='%s';", fetchDate, hostName, fileName );
		execute(sqlStatement);
	}		

	public void updateProcessDate( String hostName, String fileName, long processDate) {
		String sqlStatement = String.format("update xsfSourceFiles set processDate=%s where hostname='%s' and fileName='%s';", processDate, hostName, fileName );
		execute(sqlStatement);	
	}
	
	public void printData() {
		try {
			ResultSet rs = executeQuery("select hostname, fileName, modifyDate, fetchDate, processDate from xsfSourceFiles;");
			while( rs.next() ) {
				if(logger.isDebugEnabled()){
				    logger.debug(String.format("hostname: %s, fileName: %s, modifyDate: %s, fetchDate=%s, processDate:%s", rs.getString(1), rs.getString(2), rs.getLong(3), rs.getLong(4), rs.getLong(5)));
				}			
			}
		} catch( SQLException ex ) {
			ex.printStackTrace();
		} 
	}
	

}
