package xsf.downloader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.apache.log4j.Logger;

import xsf.config.GlobalConfigJson;
import xsf.config.model.Config;
import xsf.config.model.Probe;
import xsf.db.local.LocalDatabaseManager;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;

public class FileDownloader {
	final static Logger logger = Logger.getLogger(FileDownloader.class);
	
	private String XSF_FOLDER = "/runtime/xmlenricher/Scans/processed/"; 
	
	public void downloadFile() throws IOException {
		Config config = GlobalConfigJson.getInstance().getConfig();
		int probeCount = 0;
		for( Probe probe: config.getProbes() ) {
			if(logger.isInfoEnabled()){
			    logger.info( String.format("Downloading file from Probe : %s [%s/%s]", probe, ++probeCount, config.getProbes().size()));
			}	
			downloadFile( probe.getHostName(), String.format("%s%s", probe.getUdPath(), XSF_FOLDER), probe.getDomain(), probe.getUserName(), probe.getPasswordDecrypted());
		}
		LocalDatabaseManager.getInstance().shutdown();
		if(logger.isInfoEnabled()){
		    logger.info("Downloading Complete");
		}			
	}
	
	
	public void downloadFile( String hostName, String path, String domain, String userName, String password ) throws IOException {
		LocalDatabaseManager ldm = LocalDatabaseManager.getInstance();
		String destinationPath = GlobalConfigJson.getInstance().getConfig().getXsfPath() + "/";
		
		NtlmPasswordAuthentication smbAuth = new NtlmPasswordAuthentication(domain, userName, password);
		String fullPath = String.format("smb://%s/%s", hostName, path );
		//System.out.println( fullPath );
		//System.out.println( String.format("%s - %s - %s", domain, userName, password) );
		
		SmbFile smbPath = new SmbFile(fullPath, smbAuth);
		SmbFile[] smbFiles = smbPath.listFiles();
		int totalFileCount = smbFiles.length;
		int fileCount = 0;
		for( SmbFile smbFile : smbFiles ){
			fileCount++;
			String fileName = smbFile.getName();
			long modifyDate = smbFile.getLastModified();
			long modifyDateCache = ldm.getModifyDate(hostName, fileName);
			if(logger.isInfoEnabled()){
			    logger.info(String.format("Downloading file (%s/%s): fileName:%s, modifyDate:%s, modifyDateCache:%s", fileCount, totalFileCount, fileName, new Date(modifyDate), new Date(modifyDateCache )));
			}
			if( modifyDate != modifyDateCache ) {
				copyFile(smbFile, destinationPath );
				ldm.addfile(hostName, fileName, modifyDate);
				Date d = new Date();
				ldm.updateFetchDate(hostName, fileName, d.getTime() );
				if(logger.isInfoEnabled()){
				    logger.info(String.format("Downloading file (%s/%s): fileName:%s, modifyDate:%s, modifyDateCache:%s - COMPLETE", fileCount, totalFileCount, fileName, new Date(modifyDate), new Date(modifyDateCache )));
				}
			} else {
				if(logger.isInfoEnabled()){
				    logger.info(String.format("Downloading file (%s/%s): fileName:%s, modifyDate:%s, modifyDateCache:%s - SKIPPED", fileCount, totalFileCount, fileName, new Date(modifyDate), new Date(modifyDateCache )));
				}				
			}
		}
	}
		
	private void copyFile( SmbFile smbFile, String destinationPath ) throws IOException {
		OutputStream os = new FileOutputStream(destinationPath + smbFile.getName());
		InputStream is = smbFile.getInputStream();
        int bufferSize = 5096;
        byte[] b = new byte[bufferSize];
        int noOfBytes = 0;
        while( (noOfBytes = is.read(b)) != -1 ) {
            os.write(b, 0, noOfBytes);
        }
        os.close();
        is.close();
	}
	
	public static boolean listFile( String hostName, String path, String domain, String userName, String password ) throws IOException {
		NtlmPasswordAuthentication smbAuth = new NtlmPasswordAuthentication(domain, userName, password);

		//String fullPath = "smb://ucmdb.demo.hpadvantageinc.com/C$/HP/UCMDB/DataFlowProbe/runtime/xmlenricher/Scans/processed/";
		String fullPath = String.format("smb://%s/%s", hostName, path );
		SmbFile smbPath = new SmbFile(fullPath, smbAuth);
		SmbFile[] smbFiles = smbPath.listFiles();
		for( SmbFile smbFile : smbFiles ){
			System.out.println( smbFile.getName() );
			//System.out.println( smbFile.getURL() );
			//System.out.println( new Date(smbFile.getLastModified()) );
			
			System.out.println( "c:/Temp/xsf/" + smbFile.getName() );
			//SmbFile dest = new SmbFile("smb://c:/Temp/xsf/" + smbFile.getName(), new NtlmPasswordAuthentication("AMERICAS","rehan","myPass") );
			SmbFile dest = new SmbFile("c:/Temp/xsf/" + smbFile.getName());
			//dest.createNewFile();
			//System.out.println( dest.canWrite() );
			smbFile.copyTo(dest);
			
			/*
			OutputStream os = new FileOutputStream("c:/Temp/xsf/" + smbFile.getName());
			InputStream is = smbFile.getInputStream();
            int bufferSize = 5096;
            byte[] b = new byte[bufferSize];
            int noOfBytes = 0;
            while( (noOfBytes = is.read(b)) != -1 ) {
                os.write(b, 0, noOfBytes);
            }
            os.close();
            is.close();
			*/
			
			
		}
		/*
		
		//String[] fileNameList = new String;
 		if(smbFile.exists()) {
			String[] list = smbFile.list();
			//smbFile.
			for( String file : list )
				System.out.println( file );
		}
		*/
 		return true;
	}
	
	/**
	 * ************************ NON WORKING
	 * @param fileContent
	 * @param fileName
	 * @param userName
	 * @param password
	 * @return
	 */
    public static boolean copyFiles(String fileContent, String fileName, String userName, String password ) {
        boolean successful = false;
         try{
        	 	String NETWORK_FOLDER = "smb://ucmdb.demo.hpadvantageinc.com/C$/HP";
                String user = userName + ":" + password;
                System.out.println("User: " + user);
 
                NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(user);
                String path = NETWORK_FOLDER + fileName;
                System.out.println("Path: " +path);
 
                SmbFile sFile = new SmbFile(path, auth);
                SmbFileOutputStream sfos = new SmbFileOutputStream(sFile);
                sfos.write(fileContent.getBytes());
 
                successful = true;
                sfos.close();
                System.out.println("Successful" + successful);
            } catch (Exception e) {
                successful = false;
                e.printStackTrace();
            }
        return successful;
    }	

}
