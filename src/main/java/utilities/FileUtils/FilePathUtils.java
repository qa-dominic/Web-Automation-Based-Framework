package utilities.FileUtils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FilePathUtils {
    private final String fileName;

    public FilePathUtils(String fileName) {
        this.fileName = fileName;
    }

    public String getAbsolutePath() {
        Path basePath = Paths.get("").toAbsolutePath();
        Path filePath = basePath.resolve(getPathFromResources()).normalize();
        return filePath.toString();
    }

    private String getPathFromResources() {
        return ".\\src\\test\\java\\resources\\" + fileName;
    }

}