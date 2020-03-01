package execution;

import lombok.extern.log4j.Log4j;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Log4j
@WebSocket
public class WebSocketServerMock {

    // Store sessions if you want to, for example, broadcast a message to all users
    private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();

    @OnWebSocketConnect
    public void connected(Session session) {
        sessions.add(session);
        log.info("New connection to websocket mock server established.");
    }

    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        sessions.remove(session);
        log.info("Session closed. " + reason);
    }

    @OnWebSocketMessage
    public void message(Session session, String message) {
        try {
            onMessageReceived(session, message);
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    private void onMessageReceived(Session session, String message) throws IOException, URISyntaxException {
        log.info("The mock server received the message: " + message);
    }

    @OnWebSocketError
    public void throwError(Throwable error) {
        error.printStackTrace();
    }
}
