package utilities.ApkPath;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Apk {
    public static final Path API_DEMOS_APK;

    static {
        String filePath = "src/main/resources/mlwallet-app.apk";
        API_DEMOS_APK = Paths.get(filePath).toAbsolutePath().normalize();
    }

    private Apk() {
        throw new AssertionError("The TestApk class should not be instantiated.");
    }
}