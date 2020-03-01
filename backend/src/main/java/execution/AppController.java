package execution;

import crud.WebSocketServerListener;
import definition.Project;
import lombok.extern.log4j.Log4j;
import spark.Service;
import utils.FileReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

@Log4j
public class AppController {
    private Project project = null;

    public BufferedReader getProjectFileContent() throws FileNotFoundException {
        String projectPath = new File("").getAbsolutePath();
        return FileReader.getFileContent(projectPath.concat("/test-project.json"));
    }

    /**
     * Method to read the project from a file
     * @throws FileNotFoundException
     */
    public void readProject() throws FileNotFoundException {
//        ProjectReader projectReader = new ProjectReader();
//        project = projectReader.deserializeProject(getProjectFileContent());
    }

    /**
     * Method to read the project from a String
     * @param projectJson
     * @return
     */
    public Project readProject(String projectJson) {
//        ProjectReader projectReader = new ProjectReader();
//        return projectReader.deserializeProject(new StringReader(projectJson));
        return new Project();
    }

    public void executeProject() throws IOException, URISyntaxException {
//        ExecutionDelegator executionDelegator = new ExecutionDelegator(listener);
//        executionDelegator.executeProject(project);
    }

    public void executeSelectedElement(String projectJson, String guid) throws IOException, URISyntaxException {
//        Project project = readProject(projectJson);
//
//        ExecutionDelegator executionDelegator = new ExecutionDelegator(listener);
//        executionDelegator.executeSelectedElement(project, guid);
    }

    public void executeProject(String projectJson) throws IOException, URISyntaxException {
//        Project project = readProject(projectJson);
//        ExecutionDelegator executionDelegator = new ExecutionDelegator(listener);
//        executionDelegator.executeProject(project);
    }

    public boolean startMock(Project project) {
        Service mockServer = Service.ignite();
        try {
            mockServer.port(project.getPort());
            mockServer.webSocketIdleTimeoutMillis(0);
            mockServer.webSocket("/s-mock", WebSocketServerListener.class);
            mockServer.init();
            log.info("Started the websocket mock on port " + project.getPort() + ".");

            return true;
        } catch (Exception ex) {
            log.error(ex);

            return false;
        }
    }

    public boolean stopMock() {
        return true;
    }
}
