package execution;

import definition.Project;
import utils.FileReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

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
}
