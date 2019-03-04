package res;

import java.io.InputStream;

public class ResourseManager {
    public static InputStream getResourceByPath(String path) {
        return ResourseManager.class.getResourceAsStream(path);
    }
}
