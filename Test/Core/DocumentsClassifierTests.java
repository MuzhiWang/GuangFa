package Core;

import Settings.FileSettings;
import Tools.Utils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by muwang on 4/8/2019.
 */

public class DocumentsClassifierTests {
    @Test
    public void resetTest() throws Exception {
        DocumentsClassifier dc = new DocumentsClassifier(".");
        Document doc = dc.getRoot();
        String folderName = "test";
        doc.addDocument(new Document(folderName, "/1/2/", true));

        dc.reset();

        Assert.assertFalse(dc.getRoot().existSubFolder(folderName));
    }

    @Test
    public void getFilePathTest() throws Exception {
        String testPath = FileSettings.DIRECTORY_PATH + "\\PathTest";
        Utils.createWholePathIfNotExist(testPath);

        File file = new File(testPath + "\\1_1.txt");
        file.createNewFile();

        String[] filePaths = DocumentsClassifier.getFilePath(file);

        System.out.println("\n");
        for (String s : filePaths) {
            System.out.printf("%s\t", s);
        }
        System.out.println("\n");
    }

    @Test
    public void classifyTest() throws Exception {
        DocumentsClassifier dc = new DocumentsClassifier(FileSettings.DIRECTORY_PATH + "\\Copy");
        File folder = new File(FileSettings.DIRECTORY_PATH);
        List<File> files = Arrays.asList(folder.listFiles());

        dc.classify(files);
    }
}
