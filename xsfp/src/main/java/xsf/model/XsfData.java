package xsf.model;

import java.util.ArrayList;
import java.util.List;

public class XsfData {
	
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getMachineId() {
		return machineId;
	}
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public List<String> getIpAddresses() {
		return ipAddresses;
	}
	public void setIpAddresses(List<String> ipAddresses) {
		this.ipAddresses = ipAddresses;
	}

	public List<Directory> getDirectories() {
		return directories;
	}
	public void setDirectories(List<Directory> directories ) {
		this.directories = directories;
	}	
	
	public void addIpAddress( String ipAddress ) {
		this.ipAddresses.add(ipAddress);
	}

	public void addDirectory( Directory directory ) {
		this.directories.add(directory);
	}

	public int getFileCount() {
		return fileCount;
	}
	public void setFileCount(int fileCount) {
		this.fileCount = fileCount;
	}	
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("XsfData [hostName=");
		builder.append(hostName);
		builder.append(", machineId=");
		builder.append(machineId);
		builder.append(", domainName=");
		builder.append(domainName);
		builder.append(", ipAddresses=");
		builder.append(ipAddresses);
		builder.append(", directories=");
		builder.append(directories);
		builder.append(", fileCount=");
		builder.append(fileCount);
		builder.append(", fileName=");
		builder.append(fileName);
		builder.append("]");
		return builder.toString();
	}

	private String hostName;
	private String machineId;
	private String domainName;
	private List<String> ipAddresses = new ArrayList<String>();
	private List<Directory> directories = new ArrayList<Directory>();
	private int fileCount;
	private String fileName;

}
