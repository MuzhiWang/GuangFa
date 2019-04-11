package Core;

import Settings.TestSettings;
import TestUtils.TestUtils;
import Tools.Utils;
import org.junit.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by muwang on 4/8/2019.
 */

public class DocumentsClassifierTests {
    private final String TestPath = TestSettings.TEST_PATH + "\\DocumentsClassifierTests\\MockData";

    @After
    public void clear() throws Exception {
//        Utils.deleteFolder(TestPath);
    }

    @Test
    public void resetTest() throws Exception {
        DocumentsClassifier dc = new DocumentsClassifier(".");
        Document doc = dc.getRoot();
        String folderName = "test";
        doc.addFolder(new Document(folderName, "/1/2/", true));

        dc.reset();

        Assert.assertFalse(dc.getRoot().existFolder(folderName));
    }

    @Test
    public void filePathRegExTest() {
        String fileName = "1-2-3-4.txt";
        String[] strs = fileName.split("(?i)\\.");

        Assert.assertEquals("1-2-3-4", strs[0]);
        Assert.assertEquals("txt", strs[1]);
    }

    @Test
    public void getFilePathTest() throws Exception {
        String testPath = TestPath + "\\FilePathTest";
        Utils.createWholePathIfNotExist(testPath);

        File file = TestUtils.createFile(testPath, "1-1.txt");

        String[] filePaths = DocumentsClassifier.getFilePath(file);

        System.out.println("\n");
        for (String s : filePaths) {
            System.out.printf("%s\t", s);
        }
        System.out.println("\n");
    }

    @Test
    public void classifyTest() throws Exception {
        String testDataFolder = TestPath + "\\ClassifyTestData_GeneralTest";
        Utils.createWholePathIfNotExist(testDataFolder);

        TestUtils.createFile(testDataFolder, "1-1abc.txt");
        TestUtils.createFile(testDataFolder, "1-2abc.txt");
        TestUtils.createFile(testDataFolder, "1-3abc.txt");

        String targetFolder = TestPath + "\\ClassifyTestResult_GeneralTest";
        DocumentsClassifier dc = new DocumentsClassifier(targetFolder);
        File folder = new File(testDataFolder);
        List<File> files = Arrays.asList(folder.listFiles());

        dc.classify(files);
    }


    @Test
    public void classifyTest_TwoFilesWithSameFilePath() throws Exception {
        String testDataFolder = TestPath + "\\ClassifyTestData_TwoFilesWithSameFilePath";
        Utils.createWholePathIfNotExist(testDataFolder);

        TestUtils.createFile(testDataFolder, "1-1abc.txt");
        TestUtils.createFile(testDataFolder, "1-1xyz.txt");
        TestUtils.createFile(testDataFolder, "1-3abc.txt");

        String targetFolder = TestPath + "\\ClassifyTestResult_TwoFilesWithSameFilePath";
        DocumentsClassifier dc = new DocumentsClassifier(targetFolder);
        File folder = new File(testDataFolder);
        List<File> files = Arrays.asList(folder.listFiles());

        dc.classify(files);
    }

    @Test
    public void classifyTest_ThreeFilesWithSameFilePath() throws Exception {
        String testDataFolder = TestPath + "\\ClassifyTestData_ThreeFilesWithSameFilePath";
        Utils.createWholePathIfNotExist(testDataFolder);

        TestUtils.createFile(testDataFolder, "1-1abc.txt");
        TestUtils.createFile(testDataFolder, "1-1abcd.txt");
        TestUtils.createFile(testDataFolder, "1-1abcde.txt");
        TestUtils.createFile(testDataFolder, "1-1abcdef.txt");

        String targetFolder = TestPath + "\\ClassifyTestResult_ThreeFilesWithSameFilePath";
        DocumentsClassifier dc = new DocumentsClassifier(targetFolder);
        File folder = new File(testDataFolder);
        List<File> files = Arrays.asList(folder.listFiles());

        dc.classify(files);
    }

    @Test
    public void classifyTest_File1_2_3_4Exist() throws Exception {
        String testDataFolder = TestPath + "\\ClassifyTestData_File1_2_3_4Exist";
        Utils.createWholePathIfNotExist(testDataFolder);

        TestUtils.createFile(testDataFolder, "1-2-3-4abc.txt");
        TestUtils.createFile(testDataFolder, "1-2abc.txt");
        TestUtils.createFile(testDataFolder, "1-1abc.txt");

        String targetFolder = TestPath + "\\ClassifyTestResult_File1_2_3_4Exist";
        DocumentsClassifier dc = new DocumentsClassifier(targetFolder);
        File folder = new File(testDataFolder);
        List<File> files = Arrays.asList(folder.listFiles());

        dc.classify(files);
    }

    @Test
    public void classifyTest_File1_2_3Exist() throws Exception {
        String testDataFolder = TestPath + "\\ClassifyTestData_File1_2_3Exist";
        Utils.createWholePathIfNotExist(testDataFolder);

        TestUtils.createFile(testDataFolder, "1-2-3abc.txt");
        TestUtils.createFile(testDataFolder, "1-2abc.txt");
        TestUtils.createFile(testDataFolder, "1-1abc.txt");

        String targetFolder = TestPath + "\\ClassifyTestResult_File1_2_3Exist";
        DocumentsClassifier dc = new DocumentsClassifier(targetFolder);
        File folder = new File(testDataFolder);
        List<File> files = Arrays.asList(folder.listFiles());

        dc.classify(files);
    }

    @Test
    public void classifyTest_File1_2Exist() throws Exception {
        String testDataFolder = TestPath + "\\ClassifyTestData_File1_2Exist";
        Utils.createWholePathIfNotExist(testDataFolder);

        TestUtils.createFile(testDataFolder, "1-2abc.txt");
        TestUtils.createFile(testDataFolder, "1-2-3abc.txt");
        TestUtils.createFile(testDataFolder, "1-1abc.txt");

        String targetFolder = TestPath + "\\ClassifyTestResult_File1_2Exist";
        DocumentsClassifier dc = new DocumentsClassifier(targetFolder);
        File folder = new File(testDataFolder);
        List<File> files = Arrays.asList(folder.listFiles());

        dc.classify(files);
    }
}
