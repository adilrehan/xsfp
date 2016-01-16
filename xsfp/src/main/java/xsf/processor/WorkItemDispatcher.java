package xsf.processor;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import xsf.config.GlobalConfigJson;

public class WorkItemDispatcher {
	final static Logger logger = Logger.getLogger(WorkItemDispatcher.class);
	
	private List<XsfProcessorWorkerThread> workItems = Collections.synchronizedList(new ArrayList<XsfProcessorWorkerThread>());
	private boolean processing = false;
	
	private static WorkItemDispatcher workItemDispatcher = null;
	
	
	
	private WorkItemDispatcher() {
	}
	
	public static WorkItemDispatcher getInstance() {
		if( workItemDispatcher == null ) {
			workItemDispatcher = new WorkItemDispatcher();
		}
		return workItemDispatcher;
	}
	
	public List<XsfProcessorWorkerThread> getWorkItems() {
		if( workItems != null ) {
			if( workItems.size() != 0 ) {
				return workItems;
			}
		}
		
		if(logger.isDebugEnabled()){
		    logger.debug("Getting Work Items From Path");
		}
		Path basePath = Paths.get(GlobalConfigJson.getInstance().getConfig().getXsfPath());
		workItems = new ArrayList<XsfProcessorWorkerThread>();
		File folder = basePath.toFile();
		for(File file: folder.listFiles() ) {
			if( file.isFile() && file.getName().toLowerCase().endsWith(".xsf") ) {
				workItems.add( new XsfProcessorWorkerThread(file.getAbsolutePath()));
			}
		}
		if(logger.isDebugEnabled()){
		    logger.debug("Getting Work Items From Path Finished");
		}		
		return workItems;
	}	

	public void processWorkItems( List<XsfProcessorWorkerThread> workItems ) {
		processWorkItems( workItems, false );
	}
	
	public void processWorkItems( List<XsfProcessorWorkerThread> workItems, boolean blocking ) {
		if( processing ) {
			logger.warn("Processing already started");
			return;
		} else {
			processing = true;
		}
		if(logger.isInfoEnabled()){
		    logger.info("Proccsing Work Items");
		}
		int workItemCount = 0;		
		ExecutorService executor = Executors.newFixedThreadPool( GlobalConfigJson.getInstance().getConfig().getThreadPoolSize());
		for( XsfProcessorWorkerThread workItem : workItems ) {
			executor.execute(workItem);
			workItemCount++;
		}
		executor.shutdown();
		if( blocking ) {
			if(logger.isDebugEnabled()){
			    logger.debug("Waiting for Work Items Processing Completion");
			}
			while (!executor.isTerminated()) {
	        }
		}
		//processing = false;
		if(logger.isInfoEnabled()){
		    logger.info("Proccsing Work Items Fininshed - Total Work Items: " + workItemCount);
		}		
	}	
	
	public int resetAll() {
		if( processing ) {
			int count = 0;
			for( XsfProcessorWorkerThread workItem:  this.workItems ) {
				if( "queued".equalsIgnoreCase(workItem.getStatus()) || "processing".equalsIgnoreCase(workItem.getStatus())  ) {
					count ++;
				}
			}
			if( count > 0 ) {
				logger.warn("Cannot reselt, process is still running " + count + " work items");
				return count;
			} else {
				workItems = new ArrayList<XsfProcessorWorkerThread>();
				processing = false;
			}
		} else {
			workItems = new ArrayList<XsfProcessorWorkerThread>();
			processing = false;
		}
		return 0;
	}

	public int purge( String status ) {
		int count = 0;
		for( XsfProcessorWorkerThread workItem:  this.workItems ) {
			if( status.toLowerCase().equalsIgnoreCase(workItem.getStatus())  ) {
				count ++;
				this.workItems.remove(workItem);
			}
		}
		return count;
	}	
	
}
