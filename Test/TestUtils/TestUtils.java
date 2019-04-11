package TestUtils;

import java.io.File;

/**
 * Created by muwang on 4/11/2019.
 */
public class TestUtils {

    public static File createFile(String testPath, String fileName) throws Exception{
        File file = new File(testPath + "\\" + fileName);
        file.createNewFile();
        return file;
    }
}
