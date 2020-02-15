package crud;

import lombok.extern.log4j.Log4j;
import org.apache.logging.log4j.core.util.IOUtils;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import execution.AppController;

@Log4j
@WebSocket
public class WebSocketServerListener {

    AppController appController = null;

    // Store sessions if you want to, for example, broadcast a message to all users
    private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();

    @OnWebSocketConnect
    public void connected(Session session) {
        sessions.add(session);
        log.debug("New connection to the server established.");
    }

    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        sessions.remove(session);
        log.debug("Session closed. " + reason);
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
        log.debug("The backend server received the message: " + message);

        if (message.equals("READ PROJECT")) {
            appController = new AppController();

            log.debug("Reading project...");
            String projectFileContent = IOUtils.toString(appController.getProjectFileContent());

            log.debug("The project content is: " + projectFileContent);
            log.debug("Sending the project content to the frontend...");
            session.getRemote().sendString(projectFileContent);

            log.debug("Project content successfully sent to the frontend!");
        } else if (message.startsWith("START MOCK")) {
            String projectJson = message.substring(8);
            log.info(projectJson);

            if (appController == null) {
                appController = new AppController();
            }
//            appController.executeProject(projectJson, new WebSocketExecutionListener(session.getRemote()));
        } else {
            log.error("Unknown command '" + message + "'. S-Mock will ignore it...");
        }
    }

    @OnWebSocketError
    public void throwError(Throwable error) {
        error.printStackTrace();
    }
}
