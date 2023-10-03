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

public class site {
	static String index;
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
        // Получение URI запроса
        URI uri = exchange.getRequestURI();
        String request = uri.toString();
        try {
            request = java.net.URLDecoder.decode(request, StandardCharsets.UTF_8.name());
        if(request.contains("favicon.ico")) {return;}
        logger.debug("[REQUEST]: "+request);
        request=request.replace("/", "");
        request=request.replace("/favicon.ico", "");
        if(!request.contains("teacher")) {
        	logger.debug("[REQUEST]: sending default page.");
        	sendResponse(exchange, index,200);
        	return;
        }
        String teacher = request.replace("teacher:", "");
        logger.debug("[REQUEST]: teacher name is "+teacher);
        //в url автоматически доставляется ?, а он нам мешает. убираем.
        teacher=teacher.replace("?", "");
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