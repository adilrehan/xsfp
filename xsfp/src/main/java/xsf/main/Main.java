package xsf.main;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import xsf.config.GlobalConfigJson;
import xsf.config.model.Config;
import xsf.config.model.Probe;
import xsf.db.local.LocalDatabaseManager;
import xsf.downloader.FileDownloader;
import xsf.processor.WorkItemDispatcher;
import xsf.processor.XsfProcessorWorkerThread;
import xsf.web.WebMain;


public class Main {
	final static Logger logger = Logger.getLogger(Main.class);

	public static void main(String[] args) throws Exception {
		System.out.println( GlobalConfigJson.getInstance().getConfig() );
		if( args.length < 1 ) {
			System.out.println("No argument specified.");
			printOptions();

		} else {
			if( "-pc".equalsIgnoreCase(args[0])) {
				printConfigFile();
			} else if( "-ec".equalsIgnoreCase(args[0])) {
				encryptConfigFile();
			} else if( "-web".equalsIgnoreCase(args[0])) {
				startWebPortal();
			} else if( "-px".equalsIgnoreCase(args[0])) {
				processXsfFiles();
			} else if( "-dx".equalsIgnoreCase(args[0])) {
				downloadXsfFiles();
			} else if( "-dc".equalsIgnoreCase(args[0])) {
				clearXsfFileDownloadCache();				
			} else {
				System.out.println("Invalid argument specified. " + args[0]);
				printOptions();				
			}
		}
	}
	
	private static void printOptions() {
		System.out.println(" -pc ==> Print config");
		System.out.println(" -ec ==> Encrypt config");
		System.out.println(" -web ==> Start web console");
		System.out.println(" -px ==> Process xsf files");
		System.out.println(" -dx ==> Download xsf files");
		System.out.println(" -dc ==> Clear download xsf files cache");
	}
	
	/**
	 *  Starts main web portal
	 * @throws Exception 
	 */
	public static void startWebPortal() throws Exception {
		// WebMain.startWeb();
		WebMain.startWeb2();
	}
	

	/**
	 *  Command Line Function - Print Config file manually
	 */
	public static void printConfigFile() {
		Config config = GlobalConfigJson.getInstance().getConfig();
		System.out.println( config );
		for( Probe probe: config.getProbes() ) {
			System.out.println( probe );
		}
	}
	
	/**
	 *  Command Line Function - Encrypt Config file manually
	 */
	public static void encryptConfigFile() {
		boolean ditry = false;
		Config config = GlobalConfigJson.getInstance().getConfig();
		if( !config.isDbPasswordEncrypted() ) {
			ditry = true;
			System.out.println("Encrypting database password");
			config.setDbPasswordEncrypted(config.getDbPassword());
		}
		for( Probe probe: config.getProbes() ) {
			if( !probe.isPasswordEncrypted() ) {
				ditry = true;
				System.out.println( String.format("Encrypting probe password [%s]",probe.getHostName()));
				probe.setPasswordEncrypted(probe.getPassword());
			}
		}
		if( ditry ) {
			System.out.println("Saving configuration file");
			GlobalConfigJson.getInstance().saveConfig(config);
		} else {
			System.out.println("No encryption needed");
		}
	}
	
	/**
	 *  Command Line Function - Process all XSF files manually
	 */
	public static void processXsfFiles() {
		WorkItemDispatcher workItemDispatcher = WorkItemDispatcher.getInstance();
		List<XsfProcessorWorkerThread> workItems =  workItemDispatcher.getWorkItems();
		workItemDispatcher.processWorkItems(workItems);	
	}
	
	/**
	 * Command Line Function - Cleans up Xsf file download cache manually
	 */
	public static void clearXsfFileDownloadCache( ) throws ClassNotFoundException, SQLException {
		LocalDatabaseManager ldm = LocalDatabaseManager.getInstance();
		ldm.dropSchema();
		ldm.createSchema();
		ldm.shutdown();
	}
	
	/**
	 *  Command Line Function - Downloads XSF files manually
	 */
	public static void downloadXsfFiles() throws IOException {
		Config config = GlobalConfigJson.getInstance().getConfig();
		 GlobalConfigJson.getInstance().saveConfig(config);
		 FileDownloader fileDownloader = new FileDownloader();
		 fileDownloader.downloadFile();	
	}
}
