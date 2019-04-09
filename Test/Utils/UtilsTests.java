package Utils;

import Settings.FileSettings;
import Tools.Utils;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * Created by muwang on 4/9/2019.
 */
public class UtilsTests {
    @Test
    public void getAllFilesTest() {
        List<File> files = Utils.getAllFiles(FileSettings.DIRECTORY_PATH);
        for (File f : files) {
            System.out.printf("The file path: %s \n", f.getAbsolutePath());
        }
    }

    @Test
    public void copyFileTest() throws Exception {
        String oldPath = FileSettings.DIRECTORY_PATH;
        String newPath = FileSettings.DIRECTORY_PATH + "\\Copy\\";
        List<File> files = Utils.getAllFiles(oldPath);
        File file;
        if (files == null || files.isEmpty()) {
            file = new File(oldPath + "\\1_1.txt");
            file.createNewFile();
        } else {
            file = files.get(0);
        }

        Utils.copyFile(file.getAbsolutePath(), newPath + file.getName());

        Utils.deleteFolder(newPath);
    }
}