package Tools;

import Settings.FileSettings;
import Settings.TestSettings;
import TestUtils.TestUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * Created by muwang on 4/9/2019.
 */
public class UtilsTests {
    private final String TestPath = TestSettings.TEST_PATH + "\\UtilsTests";

    @After
    public void Clear() throws Exception {
//        Utils.deleteFolder(TestPath);
    }

    @Test
    public void getAllFilesTest() {
        List<File> files = Utils.getAllFiles(FileSettings.DIRECTORY_PATH);
        for (File f : files) {
            System.out.printf("The file path: %s \n", f.getAbsolutePath());
        }
    }

    @Test
    public void copyFileTest() throws Exception {
        String oldPath = TestPath;
        String newPath = TestPath + "\\Copy\\";

        Utils.createWholePathIfNotExist(newPath);

        File file = TestUtils.createFile(oldPath, "1-1.txt");

        Utils.copyFile(file.getAbsolutePath(), newPath + file.getName());

        File[] copyFile = new File(newPath).listFiles();
        Assert.assertEquals(1, copyFile.length);
        Assert.assertEquals("1-1.txt", copyFile[0].getName());
    }

    @Test
    public void moveFileTest() throws Exception {
        String oldPath = TestPath;
        String newPath = TestPath + "\\Move\\";

        Utils.createWholePathIfNotExist(newPath);

        File file = TestUtils.createFile(oldPath, "1-1.txt");

        Utils.moveFile(file.getAbsolutePath(), newPath + file.getName());

        File[] movedFile = new File(newPath).listFiles();
        Assert.assertEquals(1, movedFile.length);
        Assert.assertEquals("1-1.txt", movedFile[0].getName());
        Assert.assertFalse(file.exists());
    }
}