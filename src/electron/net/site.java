package electron.net;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import electron.data.database;
import electron.generators.HTMLGenerator;
import electron.utils.logger;

/**
 * Server builder and worker
 */
public class site {
	static String index;
	/**
	 * Start method for server
	 * @throws IOException - server exceptions
	 */
	public static void load() throws IOException {
		HttpServer server = HttpServer.create(new InetSocketAddress(database.getPort()), 0);
		server.createContext("/", new MainHandler());
		server.setExecutor(null);	
        server.start();
		index = HTMLGenerator.generateIndex();
		index=index.replace("null", "");
		logger.log("Server started on "+server.getAddress());
	}
public static class MainHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        //Get URI question
        URI uri = exchange.getRequestURI();
        String request = uri.toString();
        try {
        	//Format URI to UTF8
            request = java.net.URLDecoder.decode(request, StandardCharsets.UTF_8.name());
        //Default browser request, skipping it.
        if(request.contains("favicon.ico")) {return;}
        logger.debug("[REQUEST]: "+request);
        request=request.replace("/", "");
        //Check request type
        if(!request.contains("teacher")) {
        	//Request type - main page
        	logger.debug("[REQUEST]: sending default page.");
        	sendResponse(exchange, index,200);
        	return;
        }
        //Request type - teacher page
        //Find teacher name
        String teacher = request.replace("teacher:", "");
        logger.debug("[REQUEST]: teacher name is "+teacher);
        //In URI appears automatically "?". We need to delete it.
        teacher=teacher.replace("?", "");
        //Is teacher exists
        if(!database.info.toJSONString().contains(teacher)) {
        	//teacher not exists - sending 404 error
        	sendResponse(exchange,"404 - not found",404);
        	return;
        }
        //teacher exists - sending teacher page
        String anser = HTMLGenerator.generateTeacher(teacher);
        sendResponse(exchange,anser,200);
        } catch (UnsupportedEncodingException e) {
            // not going to happen - value came from JDK's own StandardCharsets
        }
    }
}

public static void sendResponse(HttpExchange exchange, String response,int code) throws IOException {
    exchange.sendResponseHeaders(code, response.getBytes().length);
    OutputStream outputStream = exchange.getResponseBody();
    outputStream.write(response.getBytes());
    outputStream.close();
}
}