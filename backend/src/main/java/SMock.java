import crud.WebSocketServerListener;
import lombok.extern.log4j.Log4j;
import spark.Service;

@Log4j
public class SMock {
    public static void main(String[] args) {

        // Start the WebSocket server that communicates with the frontend
        Service backendServer = Service.ignite();
        try {
            backendServer.port(8888);
            backendServer.webSocketIdleTimeoutMillis(0);
            backendServer.webSocket("/s-mock", WebSocketServerListener.class);
            backendServer.init();
            log.info("Started the backend server on port 8888.");
        } catch (Exception ex) {
            log.error(ex);
        }
    }
}
