package xsf.processor;


import java.nio.file.Paths;
import java.util.Date;

import org.apache.log4j.Logger;

import xsf.model.XsfData;

public class XsfProcessorWorkerThread implements Runnable {
	
	final static Logger logger = Logger.getLogger(XsfProcessorWorkerThread.class);
	
	public String getXsfPath() {
		return xsfPath;
	}
	
	public String getXsfFileName() {
		return xsfFileName;
	}	
	
	public String getHostName() {
		return hostName;
	}	

	public String getStatus() {
		return status;
	}

	public Date getQueueTime() {
		return queueTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public String getResult() {
		return result;
	}

	public String getStatistics() {
		return statistics;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("XsfProcessorWorkerThread [xsfPath=");
		builder.append(xsfPath);
		builder.append(", xsfFileName=");
		builder.append(xsfFileName);
		builder.append(", hostName=");
		builder.append(hostName);
		builder.append(", status=");
		builder.append(status);
		builder.append(", queueTime=");
		builder.append(queueTime);
		builder.append(", startTime=");
		builder.append(startTime);
		builder.append(", endTime=");
		builder.append(endTime);
		builder.append(", result=");
		builder.append(result);
		builder.append(", statistics=");
		builder.append(statistics);
		builder.append("]");
		return builder.toString();
	}
	private String xsfPath;
	private String xsfFileName;
	private String hostName;
	private String status;
	private Date queueTime;
	private Date startTime;
	private Date endTime;
	private String result;
	private String statistics;
	
	public XsfProcessorWorkerThread( String xsfPath ) {
		this.xsfPath = xsfPath;
		this.xsfFileName = Paths.get(this.xsfPath).getFileName().toString();
		this.status = "Queued";
		this.queueTime = new Date();
		if(logger.isDebugEnabled()){
			logger.debug( String.format("Queueing : %s", this.xsfFileName) );
		}
	}
	
	public void run() { 
		this.startTime = new Date();
		this.status = "Processing";
		if(logger.isDebugEnabled()){
			logger.debug( String.format("Processing : %s", this.toString()) );
		}
		try {
			XsfProcessor xsfProcessor = new XsfProcessor();
			XsfData xsfData = xsfProcessor.processXsfFile(this.xsfPath);
			this.hostName = xsfData.getHostName();
			int fileCount = xsfProcessor.saveXsfData(xsfData);
			xsfProcessor.archiveFile(this.xsfPath);
			this.result = "Success";
			this.statistics = "{ fileCount: " + fileCount + "}";
		} catch( Exception ex ) {
			logger.error(String.format("Error Processing: %s | %s", ex.getMessage(), this.toString()), ex);
			this.result = "Failure: ex.getLocalizedMessage()";
		}
		this.endTime = new Date();
		this.status = "Finished";
		if(logger.isDebugEnabled()){
			logger.debug( String.format("Processing Finished: %s", this.toString()) );
		}		
	}
	

}
