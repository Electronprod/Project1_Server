package electron.net;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import electron.data.FileOptions;
import electron.data.database;
import electron.generators.HTMLGenerator;
import electron.utils.Finder;
import electron.utils.logger;

/**
 * Server builder and worker
 */
public class site {
	static String index;
	static String find;
	/**
	 * Start method for server
	 * @throws IOException - server exceptions
	 */
	public static void load() throws IOException {
		logger.log("[SERVER]: creating server socket...");
		HttpServer server = HttpServer.create(new InetSocketAddress(database.getPort()), 0);
		server.createContext("/", new MainHandler());
		server.setExecutor(null);	
        server.start();
        logger.log("[SERVER]: socket created.");
		index = HTMLGenerator.generateIndex();
		index=index.replace("null", "");
		find = HTMLGenerator.generateFind();
		logger.debug("[SEARCH_SYSTEM]: removing duplicates from search system.");
		Finder.removeDuplicates(database.allnames);
		logger.log("[SERVER]: started on "+server.getAddress());
	}
public static class MainHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        //Get URI
        URI uri = exchange.getRequestURI();
        String request = uri.toString();
        try {
        	//Format URI to UTF8
            request = java.net.URLDecoder.decode(request, StandardCharsets.UTF_8.name());
        //Default browser request, skipping it.
        if(request.contains("favicon.ico")) {return;}
        logger.debug("[SERVER]: request: "+request);
        request=request.replace("/", "");
        //Check request type
        if(request.contains("teacher")) {
        	//Request type - teacher page
            teacherRequest(request,exchange);
            return;
        }
        if(request.contains("searchform")) {
        	//Request type - find page
            findRequest(exchange);
            return;
        }
        if(request.contains("search")) {
        	//Request type - search page
        	searchRequest(request,exchange);
        	return;
        }
        if(request.contains("student")) {
        	//Request type - search page
        	studentRequest(exchange,request);
        	return;
        }
        if(request.contains("js") || request.contains("css") || request.contains("html")) {
        	//Request type - resources for page from browser
        	sendResponse(exchange,getResource(request),200);
        	return;
        }
        indexRequest(exchange);
        } catch (UnsupportedEncodingException e) {
            // not going to happen - value came from JDK's own StandardCharsets
        }
    }
}
private static void studentRequest(HttpExchange exchange,String request) throws IOException {
	logger.debug("[STUDENT_REQUEST]: finding...");
	//Find name
    String name = request.replace("student:", "");
    //In URI appears automatically "?". We need to delete it.
    name=name.replace("?", "");
	//Check, is it student or teacher
    if(!name.contains(":")) {
    	//It is teacher - calling teacher method
    	request=request.replace("student:", "teacher:");
    	teacherRequest(request,exchange);
    	return;
    }
    String studentname = name.split(" : ")[0];
    String classname = name.split(" : ")[1];
    //It is student - generating his time table
    String html = HTMLGenerator.generateStudent(classname,studentname);
    sendResponse(exchange, html,200);
}
private static void indexRequest(HttpExchange exchange) throws IOException {
	sendResponse(exchange, index,200);
}
private static void findRequest(HttpExchange exchange) throws IOException {
	sendResponse(exchange, find,200);
}
private static void searchRequest(String request,HttpExchange exchange) throws IOException {
	logger.debug("[SEARCH_REQUST]: finding...");
	//Find search request
	String search = request.replace("search?name=", "");
	logger.debug("[SEARCH_REQUST]: found name: "+search);
	//Find most similar string from database
	String found = Finder.findMostSimilarString(search, database.allnames);
	logger.debug("[SEARCH_REQUST][FOUND]: "+ found);
	//Generating HTML for founded item
	String html = HTMLGenerator.generateSearchResults(found);
	sendResponse(exchange,html,200);	
}
private static void teacherRequest(String request,HttpExchange exchange) throws IOException {
	logger.debug("[TEACHER_REQUEST]: finding...");
	//Find teacher name
    String teacher = request.replace("teacher:", "");
    //In URI appears automatically "?". We need to delete it.
    teacher=teacher.replace("?", "");
    //Is teacher exists
    if(!database.info.toJSONString().contains(teacher)) {
    	//teacher not exists - sending 404 error
    	sendResponse(exchange,"404 - not found",404);
    	logger.debug("[TEACHER_REQUEST]: teacher not found");
    	return;
    }
    //teacher exists - sending teacher page
    String anser = HTMLGenerator.generateTeacher(teacher);
    sendResponse(exchange,anser,200);
    logger.debug("[TEACHER_REQUEST]: teacher found, sent page to remote user.");
}
public static String getResource(String fname) {
	logger.debug("[RESOURCE_SYSTEM]: asked file: "+fname);
	String r = FileOptions.getFileLine(new File(fname));
	r.replace("/", "");
	return r;
}
public static void sendResponse(HttpExchange exchange, String response,int code) throws IOException {
    exchange.sendResponseHeaders(code, response.getBytes().length);
    OutputStream outputStream = exchange.getResponseBody();
    outputStream.write(response.getBytes());
    outputStream.close();
}
}