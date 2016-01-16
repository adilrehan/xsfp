package xsf.processor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPInputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

import xsf.config.GlobalConfigJson;
import xsf.model.Directory;
import xsf.model.File;
import xsf.model.XsfData;

public class XsfProcessor {
	
	final static Logger logger = Logger.getLogger(XsfProcessor.class);

	public void process(String xsfPath) throws FileNotFoundException, IOException, SQLException, ClassNotFoundException, XMLStreamException {
		logger.info("Start Processing: " + xsfPath);
		Date date1 = new Date();
		XsfData xsfData = processXsfFile(xsfPath);
		Date date2 = new Date();
		int fileCount = saveXsfData( xsfData );
		Date date3 = new Date();
		logger.info("Finished Processing: " + xsfPath + ": " + xsfData.getHostName() + ", " + (date2.getTime() - date1.getTime()) + ", " + (date3.getTime() - date2.getTime()) + ", " + fileCount);
		//System.out.println("Server/Parsing/Loading/Count: " + xsfData.getHostName() + ", " + (date2.getTime() - date1.getTime()) + ", " + (date3.getTime() - date2.getTime()) + ", " + fileCount );
	}
	
	public void archiveFile( String xsfPath ) throws IOException {
		logger.info("Start Archieve: " + xsfPath);
		Path processedPath = Paths.get(GlobalConfigJson.getInstance().getConfig().getXsfPathAchieve());
		if( !processedPath.toFile().isDirectory() ) {
			processedPath.toFile().mkdir();
		}
		java.io.File file = Paths.get(xsfPath).toFile();
		Files.move( file.toPath(), processedPath.resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
		logger.info("Finished Archieve: " + xsfPath);
	}

	public int saveXsfData( XsfData xsfData ) throws SQLException, ClassNotFoundException {
		String hostName = xsfData.getHostName();
		if(logger.isInfoEnabled()){
			logger.info( String.format("Saving: %s", hostName) );
		}
		
		Class.forName( GlobalConfigJson.getInstance().getConfig().getJdbcDriver());
		Connection connection = DriverManager.getConnection( GlobalConfigJson.getInstance().getConfig().getDbUrl(), GlobalConfigJson.getInstance().getConfig().getDbUser(), GlobalConfigJson.getInstance().getConfig().getDbPassword());
		Statement statement = connection.createStatement();
		connection.setAutoCommit(false);
		
		int fileCount = 0;
		
		//System.out.println( "xsfData.getDirectories(): " + xsfData.getDirectories().size()); 
		for( Directory directory : xsfData.getDirectories() ) {
			StringBuffer sb = new StringBuffer();
			for( File file : directory.getFiles() ) {
				String sqlString = getSQLStatement( hostName, directory, file );
				if(logger.isDebugEnabled()){
				    logger.debug( sqlString );
				}
				//System.out.println( file + ":  " + sqlString );
				statement.executeUpdate(sqlString);
				
				sb.append(sqlString);
				//statement.addBatch(sqlString);
				++fileCount;
				if( (fileCount % 1000) == 0  ) {
					if(logger.isInfoEnabled()){
						logger.info( String.format("[%s - %s] %s/%s", Thread.currentThread().getId(), xsfData.getFileName(), fileCount, xsfData.getFileCount()) );
					}
				}				
			}
			//statement.executeBatch();
			if( sb.length() > 0 ) {
				//statement.executeUpdate(sb.toString());
				//statement.executeBatch();
			}
		}
		
		if(logger.isInfoEnabled()){
			logger.info( String.format("Saving Finished: %s", hostName) );
		}		
		return fileCount;
	}
	
	private String getSQLStatement( String hostName, Directory directory, File file ) {
		String tableName = "xsf_file";
		StringBuffer sb = new StringBuffer();
		sb.append("INSERT INTO ");
		sb.append(tableName);
		sb.append("( hostname, dir_name, dir_date, dir_attr, dir_link, dir_contains, dir_msdos, file_name, file_created, file_modified, file_accessed, file_size, file_attr, file_signature, file_versionid, file_pkgindex, file_arctype, file_exetype, file_flag, file_msdos, file_languageCode, file_realName, file_dosName, file_description, file_productVersion, file_fileVersion, file_originalFilename, file_companyName, file_productName, file_internalName, file_fileDescription, file_legalCopyright ) VALUES( ");
		sb.append(getSqlValue(hostName )).append(", ");
		sb.append(getSqlValue(directory.getName())).append(", ");
		
		sb.append(dateToString(directory.getDate())).append(", ");
		
		sb.append(getSqlValue(directory.getAttribute())).append(", ");
		sb.append(getSqlValue(directory.getLink())).append(", ");
		sb.append(getSqlValue(directory.getContains())).append(", ");
		sb.append(getSqlValue(directory.getMsDos())).append(", ");
		
		sb.append(getSqlValue(file.getName())).append(", ");
		
		sb.append(dateToString(file.getCreated())).append(", ");
		sb.append(dateToString(file.getModified())).append(", ");
		sb.append(dateToString(file.getAccessed())).append(", ");
		
		sb.append(file.getSize()).append(", ");
		sb.append(getSqlValue(file.getAttribute())).append(", ");
		sb.append(getSqlValue(file.getSignature())).append(", ");
		sb.append(getSqlValue(file.getVersionId())).append(", ");
	
		sb.append(getSqlValue(file.getPackageIndex())).append(", ");
		sb.append(getSqlValue(file.getArchType())).append(", ");
		sb.append(getSqlValue(file.getExeType())).append(", ");
		sb.append(getSqlValue(file.getFlag())).append(", ");
		sb.append(getSqlValue(file.getMsDos())).append(", ");
		
		sb.append(getSqlValue(file.getLanguageCode())).append(", ");
		sb.append(getSqlValue(file.getRealName())).append(", ");
		sb.append(getSqlValue(file.getDosName())).append(", ");
		sb.append(getSqlValue(file.getDescription())).append(", ");
		sb.append(getSqlValue(file.getProductVersion())).append(", ");
		sb.append(getSqlValue(file.getFileVersion())).append(", ");
		sb.append(getSqlValue(file.getOriginalFilename())).append(", ");
		sb.append(getSqlValue(file.getCompanyName())).append(", ");
		sb.append(getSqlValue(file.getProductName())).append(", ");
		sb.append(getSqlValue(file.getInternalName())).append(", ");
		sb.append(getSqlValue(file.getFileDescription())).append(", ");
		sb.append(getSqlValue(file.getLegalCopyright())).append(") ");
		
		return sb.toString();
	}


	public XsfData processXsfFile( String xsfPath ) throws FileNotFoundException, IOException, XMLStreamException {
		if(logger.isInfoEnabled()){
			logger.info( String.format("Parsing: %s", xsfPath) );
		}
		int fileCount = 0;
		//System.out.println( new Date() );
		GZIPInputStream in = new GZIPInputStream(new FileInputStream(xsfPath));
		XsfData xsfData = new XsfData();
		xsfData.setFileName(xsfPath);
		Directory currentDirectory = null;
		File currentFile = null;
		String tagContent = null;
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(in);
		while(xmlStreamReader.hasNext()){
			int event = xmlStreamReader.next();
			switch(event){
				case XMLStreamConstants.START_ELEMENT:
					if ("dir".equalsIgnoreCase(xmlStreamReader.getLocalName())){
						currentDirectory = new Directory();
						currentDirectory.setName(xmlStreamReader.getAttributeValue("", "name"));
						currentDirectory.setDate(toDate(xmlStreamReader.getAttributeValue("", "date")));
						currentDirectory.setAttribute(xmlStreamReader.getAttributeValue("", "attr"));
						currentDirectory.setLink(xmlStreamReader.getAttributeValue("", "link"));
						currentDirectory.setContains(xmlStreamReader.getAttributeValue("", "contains"));
						currentDirectory.setMsDos(xmlStreamReader.getAttributeValue("", "msdos"));
					}
					if ("file".equalsIgnoreCase(xmlStreamReader.getLocalName())){
						currentFile = new File();
						currentFile.setName(xmlStreamReader.getAttributeValue("", "name"));
						currentFile.setCreated(toDate(xmlStreamReader.getAttributeValue("", "created")));
						currentFile.setModified(toDate(xmlStreamReader.getAttributeValue("", "modifiled")));
						currentFile.setAccessed(toDate(xmlStreamReader.getAttributeValue("", "accessed")));
						currentFile.setSize(toInteger(xmlStreamReader.getAttributeValue("", "size")));
						currentFile.setAttribute(xmlStreamReader.getAttributeValue("", "attr"));
						currentFile.setSignature(xmlStreamReader.getAttributeValue("", "signature"));
						currentFile.setPackageIndex(xmlStreamReader.getAttributeValue("", "pkgindex"));
						currentFile.setArchType(xmlStreamReader.getAttributeValue("", "arctype"));
						currentFile.setExeType(xmlStreamReader.getAttributeValue("", "exetype"));
						currentFile.setVersionId(xmlStreamReader.getAttributeValue("", "versionid"));
						currentFile.setFlag(xmlStreamReader.getAttributeValue("", "flag"));
						currentFile.setMsDos(xmlStreamReader.getAttributeValue("", "msdos"));
					}
					if ("verinfo".equalsIgnoreCase(xmlStreamReader.getLocalName())){
						String verInfoName = xmlStreamReader.getAttributeValue("", "name");
						String verInfoValue = xmlStreamReader.getAttributeValue("", "value");
						
						if( "Language Code".equalsIgnoreCase(verInfoName)) { currentFile.setLanguageCode(verInfoValue); }
						if( "Real Name".equalsIgnoreCase(verInfoName)) { currentFile.setRealName(verInfoValue); }
						if( "DOS 8.3 Name".equalsIgnoreCase(verInfoName)) { currentFile.setDosName(verInfoValue); }
						if( "Description".equalsIgnoreCase(verInfoName)) { currentFile.setDescription(verInfoValue); }
						if( "Product Version".equalsIgnoreCase(verInfoName)) { currentFile.setProductVersion(verInfoValue); }
						if( "File Version".equalsIgnoreCase(verInfoName)) { currentFile.setFileVersion(verInfoValue); }
						if( "Original Filename".equalsIgnoreCase(verInfoName)) { currentFile.setOriginalFilename(verInfoValue); }
						if( "Company Name".equalsIgnoreCase(verInfoName)) { currentFile.setCompanyName(verInfoValue); }
						if( "Product Name".equalsIgnoreCase(verInfoName)) { currentFile.setProductName(verInfoValue); }
						if( "Internal Name".equalsIgnoreCase(verInfoName)) { currentFile.setInternalName(verInfoValue); }
						if( "File Description".equalsIgnoreCase(verInfoName)) { currentFile.setFileDescription(verInfoValue); }
						if( "Legal Copyright".equalsIgnoreCase(verInfoName)) { currentFile.setLegalCopyright(verInfoValue); }
					}					
					break;
				case XMLStreamConstants.CHARACTERS:
					tagContent = xmlStreamReader.getText().trim();
					break;
				case XMLStreamConstants.END_ELEMENT:
					if( "hwLocalMachineID".equalsIgnoreCase(xmlStreamReader.getLocalName())) {
						xsfData.setMachineId(tagContent);
					}
					if( "hwIPHostName".equalsIgnoreCase(xmlStreamReader.getLocalName())) {
						xsfData.setHostName(tagContent);
					}
					if( "hwIPDomain".equalsIgnoreCase(xmlStreamReader.getLocalName())) {
						xsfData.setDomainName(tagContent);
					}	
					if( "hwNICIPAddress".equalsIgnoreCase(xmlStreamReader.getLocalName())) {
						xsfData.addIpAddress(tagContent);
					}					
					if( "dir".equalsIgnoreCase(xmlStreamReader.getLocalName())) {
						xsfData.addDirectory(currentDirectory);
					}	
					if( "file".equalsIgnoreCase(xmlStreamReader.getLocalName())) {
						currentDirectory.addFile(currentFile);
						fileCount++;
					}					
					break;
				case XMLStreamConstants.START_DOCUMENT:
					break;
			}
		}
		xsfData.setFileCount(fileCount);
		
		if(logger.isInfoEnabled()){
			logger.info( String.format("Parsing Complete: %s", xsfPath) );
		}
		return xsfData;
	}
	
	
	private Date toDate( String dateString ) {
		if( dateString == null ) {
			return null;
		} else {
			try {
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Date date = format.parse(dateString);
				return date;
			} catch( Exception ex ) {
				return null;
			}
		}
	}
	private int toInteger( String intString ) {
		if( intString == null ) {
			return 0;
		} else {
			try {
				int i = Integer.parseInt(intString);
				return i;
			} catch( Exception ex ) {
				return 0;
			}
		}
	}	
	private String dateToString( Date date ) {
		if( date == null) {
			return null;
		}
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return String.format("TO_TIMESTAMP('%s', 'YYYY-MM-DD HH24:MI:SS.FF')", df.format(date));
		//return df.format(date);
	}
	private String getSqlValue( String value ) {
		if( value == null ) {
			return "null";
		} else {
			return String.format("'%s'", value.replace("'", "''"));
		}
	}
	
}
