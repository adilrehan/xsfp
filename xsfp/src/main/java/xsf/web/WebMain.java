package xsf.web;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.webapp.WebAppContext;

import xsf.config.GlobalConfigJson;
import xsf.config.model.Config;
import xsf.web.servlet.WorkItemDispatcherServlet;

public class WebMain {
	
	public static void startWeb() throws Exception {
		Config config = GlobalConfigJson.getInstance().getConfig();
		String webappDirLocation = config.getWebDeploy();
	    Server server = new Server(config.getWebPort());
	    WebAppContext root = new WebAppContext();
	 
	    root.setContextPath("/");
	    root.setDescriptor(webappDirLocation + "/WEB-INF/web.xml");
	    root.setResourceBase(webappDirLocation);
	    root.setParentLoaderPriority(true);
	    server.setHandler(root);
	 
	    server.start();
	    server.join();
	}
	
	public static void startWeb2() throws Exception {
		Config config = GlobalConfigJson.getInstance().getConfig();
		Server server = new Server(config.getWebPort());
		
		ServletHandler servlet = new ServletHandler();
		servlet.addServletWithMapping(WorkItemDispatcherServlet.class, "/xsf/*");

	    WebAppContext root = new WebAppContext();
	    root.setContextPath("/");
	    root.setDescriptor(config.getWebDeploy() + "/WEB-INF/web.xml");
	    root.setResourceBase(config.getWebDeploy());
	    root.setParentLoaderPriority(true);
	    
	    //root.getSecurityHandler().setRealmName("");
	    
	    ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] { root, servlet });
        server.setHandler(contexts);
        
                
	    server.start();
	    server.join();
	}
	
	
}
