package utils;

import definition.Project;
import com.google.gson.Gson;

/**
 * This class provides methods to create a Project object from a JSON
 * formatted String and to convert to String a Project object.
 */
public class ProjectSerializer {
    public static String convertProjectToString(Project project) {
        return new Gson().toJson(project);
    }

    public static Project convertStringToProject(String jsonString) {
        return new Gson().fromJson(jsonString, Project.class);
    }
}
