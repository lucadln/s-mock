package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;

public class FileReader {
    public static BufferedReader getFileContent(String fileName) throws FileNotFoundException {
        return new BufferedReader(new java.io.FileReader(fileName));
    }
}
