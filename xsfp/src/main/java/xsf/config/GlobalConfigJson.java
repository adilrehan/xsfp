package xsf.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import xsf.config.model.Config;

public class GlobalConfigJson {
	final static Logger logger = Logger.getLogger(GlobalConfigJson.class);
	
	private static GlobalConfigJson thisInstance;
	private Config config;
	
	private GlobalConfigJson() {
	}
	
	public static GlobalConfigJson getInstance() {
		if( thisInstance == null ) {
			thisInstance = new GlobalConfigJson();
		}
		return thisInstance;
	}
	
	public Config getConfig() {
		if( config == null ) {
			config = readConfig();
		}
		return this.config;
	}

	public void saveConfig( Config config ) {
		String configPath = getClass().getResource("/config.json").getFile().toString();
		if(logger.isDebugEnabled()){
		    logger.debug("Saving configuration file: " + configPath );
		}
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(config);
			File file = new File(configPath);
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(jsonInString);
			fileWriter.flush();
			fileWriter.close();
		} catch( IOException e) {
			logger.error("Error saving configuration file: " + e.getLocalizedMessage(), e);
			e.printStackTrace();
		}
	}		
	
	public Config readConfig() {
		if(logger.isDebugEnabled()){
		    logger.debug("Loading configuration file: " + getClass().getResource("/config.json").getFile().toString());
		}
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			Config config = mapper.readValue(getClass().getResourceAsStream("/config.json"), Config.class);
			
			return config;
		} catch( IOException e) {
			logger.error("Error loacong configuration file: " + e.getLocalizedMessage(), e);
			e.printStackTrace();
		}
		return null;
	}	
	
	
	
}
