package crud;

import definition.Project;
import lombok.extern.log4j.Log4j;
import org.apache.logging.log4j.core.util.IOUtils;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import execution.AppController;
import utils.ProjectSerializer;

@Log4j
@WebSocket
public class BackendServer {

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
        log.info("The backend server received the message: " + message);

        if (message.equals("READ_PROJECT")) {
            appController = new AppController();

            log.debug("Reading project...");
            String projectFileContent = IOUtils.toString(appController.getProjectFileContent());

            log.debug("The project content is: " + projectFileContent);
            log.debug("Sending the project content to the frontend...");

            Message responseMessage = new Message("READ_PROJECT_RESPONSE", projectFileContent);
            session.getRemote().sendString(responseMessage.toString());

            log.debug("Project content successfully sent to the frontend!");
        } else if (message.startsWith("START_MOCK")) {
            log.info("Received instructions to start a mock service...");

            if (appController == null) {
                appController = new AppController();
            }

            // The message contains the project json
            String projectJson = message.substring(11);
            Project project = ProjectSerializer.convertStringToProject(projectJson);

            // Start mock
            boolean mockStarted = appController.startMock(project);

            // Inform the frontend whether the mock started or not
            Message responseMessage;
            if (mockStarted) {
                responseMessage = new Message("START_MOCK_RESPONSE", "{\"success\":true}");
            } else {
                responseMessage = new Message("START_MOCK_RESPONSE", "{\"success\":false}");
            }
            session.getRemote().sendString(responseMessage.toString());
        } else if (message.equals("STOP_MOCK")) {
            log.debug("Received instructions to stop the mock service...");

            boolean mockStarted = appController.stopMock();
            Message responseMessage;
            if (mockStarted) {
                responseMessage = new Message("STOP_MOCK_RESPONSE", "{\"success\":true}");
            } else {
                responseMessage = new Message("STOP_MOCK_RESPONSE", "{\"success\":false}");
            }
            session.getRemote().sendString(responseMessage.toString());
        } else {
            log.error("Unknown command '" + message + "'. S-Mock will ignore it...");
        }
    }

    @OnWebSocketError
    public void throwError(Throwable error) {
        error.printStackTrace();
    }
}
