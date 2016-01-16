package xsf.web.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import xsf.processor.WorkItemDispatcher;
import xsf.processor.XsfProcessorWorkerThread;

@SuppressWarnings("serial")
public class WorkItemDispatcherServlet extends HttpServlet {

    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
    	String alertMessage = "";
    	String command = request.getParameter("command");
    	if( "process".equalsIgnoreCase(command)) {
    		WorkItemDispatcher.getInstance().processWorkItems(WorkItemDispatcher.getInstance().getWorkItems());
    		alertMessage = "<div class=\"alert alert-success\"> Processing Started </div>";
    	}
    	if( "reset".equalsIgnoreCase(command)) {
    		int count = WorkItemDispatcher.getInstance().resetAll();
    		if( count > 0  ) {
    			alertMessage = "<div class=\"alert alert-danger\"> <strong>Error!</strong> Still Processing " + count + " Files</div>";
    		}
    	}    	
    	if( "purge".equalsIgnoreCase(command)) {
    		int count = WorkItemDispatcher.getInstance().purge( "finished" );
    		if( count > 0  ) {
    			alertMessage = "<div class=\"alert alert-success\"> Purged Successfully Completed Files : " + count + " </div>";
    		}
    	}     	
    	
    	
    	response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println( getHtmlHeader());
        response.getWriter().println( alertMessage);
        List<XsfProcessorWorkerThread> workItems = WorkItemDispatcher.getInstance().getWorkItems();
        response.getWriter().println( getHtmlWorkItem(workItems));
        response.getWriter().println( getHtmlFooter());
    }
    
    private String getHtmlWorkItem( List<XsfProcessorWorkerThread> workItems ) {
    	StringBuffer sb = new StringBuffer();
    	
    	sb.append("<table id=\"xsfTable\" class=\"table table-striped\">");
    	sb.append("<thead>");
    	sb.append("  <tr>");
    	sb.append("    <th>File</th>");
        sb.append("    <th data-sortable=\"true\">Status</th>");
        sb.append("    <th data-sortable=\"true\">HostName</th>");
        sb.append("    <th>Queue Time</th>");
        sb.append("    <th>Start Time</th>");
        sb.append("    <th>End Time</th>");
        sb.append("    <th>Result</th>");
        sb.append("    <th>Statistics</th>");
        sb.append("  </tr>");
        sb.append("</thead>");
        sb.append("<tbody>");
        
        for( XsfProcessorWorkerThread workItem : workItems ) {
        	
            sb.append("<tr>");
            sb.append("<td>").append(workItem.getXsfFileName()).append("</td>");
            sb.append("<td>").append(workItem.getStatus()).append("</td>");
            sb.append("<td>").append(workItem.getHostName()).append("</td>");
            sb.append("<td>").append(dateToStrig(workItem.getQueueTime())).append("</td>");
            sb.append("<td>").append(dateToStrig(workItem.getStartTime())).append("</td>");
            sb.append("<td>").append(dateToStrig(workItem.getEndTime())).append("</td>");
            sb.append("<td>").append(workItem.getResult()).append("</td>");
            sb.append("<td>").append(workItem.getStatistics()).append("</td>");
            sb.append("</tr>");
        }
        
        sb.append("</tbody>");
        sb.append("</table>");
    	
    	return sb.toString();
    }
    
    private String dateToStrig( Date date ) {
    	SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
    	if( date == null ) {
    		return "";
    	} else {
    		return format.format(date);
    	}
    }
    
    private String getHtmlHeader() {
    	StringBuffer sb = new StringBuffer();
		sb.append("<!DOCTYPE HTML>");
		sb.append("<html lang=\"en-US\">");
		sb.append("    <head>");
		sb.append("        <meta charset=\"UTF-8\">");
		sb.append("        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
		
//		sb.append("        <!-- Latest compiled and minified CSS -->");
//		sb.append("        <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css\">");
//		sb.append(" ");
//		sb.append("        <!-- Optional theme -->");
//		sb.append("        <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css\">");
//		sb.append("        <!-- Latest compiled and minified JavaScript -->");
//		sb.append("        <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js\"></script>");
		
		/** AAAA **/
//		sb.append("<link href=\"http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css\" rel=\"stylesheet\">");
//		sb.append("<script src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js\"></script>");
//		sb.append("<link rel=\"stylesheet\" href=\"http://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css\"></style>");
//		sb.append("<script type=\"text/javascript\" src=\"http://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js\"></script>");
//		sb.append("<script type=\"text/javascript\" src=\"http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js\"></script>");
		
		//sb.append("        <link rel=\"stylesheet\" type=\"text/css\" href=\"css/bootstrap-theme.min.css\"/>");
		sb.append("        <link rel=\"stylesheet\" type=\"text/css\" href=\"/css/bootstrap.min.css\"/>");
		sb.append("        <script type=\"text/javascript\" src=\"/js/jquery-2.1.4.min.js\" /></script>");
		sb.append("        <link rel=\"stylesheet\" type=\"text/css\" href=\"/css/jquery.dataTables.min.css\"/>");
		sb.append("        <script type=\"text/javascript\" src=\"/js/jquery.dataTables.min.js\"></script>");
		sb.append("        <script type=\"text/javascript\" src=\"/js/bootstrap.min.js\"></script>");
		
		
		sb.append("        <title>XSF Proccessor</title>");
		sb.append("    </head>");  
		sb.append("    <body>");
		//sb.append("    	   <h1>XSF Proccessor</h1>");
    	sb.append("        <div class=\"container\">");
   	
//    	sb.append("  		<div class=\"btn-group btn-group\">");
//    	sb.append("  		    <a href=\"?command=refresh\" class=\"btn btn-primary\">Refesh</a>");
//    	sb.append("  		    <a href=\"?command=process\" class=\"btn btn-primary\">Process</a>");
//   	    sb.append("  		</div>");
    	 
   	    sb.append("  		<nav class=\"navbar navbar-default\">");
   	    sb.append("  		<div class=\"container-fluid\">");
   	    sb.append("  		<div class=\"navbar-header\">");
   	    sb.append("  			<a class=\"navbar-brand\" href=\"/\">XSF Processor</a>");
   	    sb.append("  		</div>");
   	    sb.append("  		<div>");
   	    sb.append("  		<ul class=\"nav navbar-nav\">");
        sb.append("  			<li class=\"active\"><a href=\"?command=refresh\">Refersh</a></li>");
   	    sb.append("  			<li class=\"active\"><a href=\"?command=process\">Process</a></li>");
   	    sb.append("  			<li class=\"active\"><a href=\"?command=reset\">Reset</a></li>");
   	    sb.append("  			<li class=\"active\"><a href=\"?command=purge\">Purge</a></li>");   	    
   	    sb.append("  		</ul>");
   	    sb.append("  		</div>");
   	    sb.append("  		</div>");
   	    sb.append("  		</nav>");

    	return sb.toString();
    }
    
    private String getHtmlFooter() {
    	StringBuffer sb = new StringBuffer();
    	sb.append("        </div>");
    	
    	sb.append("<script>");
    	sb.append("$(document).ready(function(){");
    	sb.append("    $('#xsfTable').dataTable();");
    	sb.append("});");
    	sb.append("</script>");
    	
    	sb.append("    </body>");
    	sb.append("</html>");
    	return sb.toString();
    }
}
