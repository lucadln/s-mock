import crud.WebSocketServerListener;
import lombok.extern.log4j.Log4j;

import static spark.Spark.*;
import static spark.Spark.init;

@Log4j
public class SMock {
    public static void main(String[] args) {

        // Start the WebSocket server that communicates with the frontend
        try {
            port(8888);
            webSocketIdleTimeoutMillis(0);
            webSocket("/s-mock", WebSocketServerListener.class);
            init();
            log.info("Started the backend server on port 8888.");
        } catch (Exception ex) {
            log.error(ex);
        }
    }
}
