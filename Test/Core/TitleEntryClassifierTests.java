package Core;

import Settings.TestSettings;
import TestUtils.TestUtils;
import Tools.Utils;
import org.junit.After;
import org.junit.Test;

/**
 * Created by muwang on 4/17/2019.
 */
public class TitleEntryClassifierTests {

    private static final String TEST_EXCEL_FILE = TestSettings.TEST_MATERIALS_PATH + "\\3-1-4 IPO项目测试模板.xlsx";

    private static final String INPUT_PATH = TestSettings.TEST_PATH + "\\input_folder";

    private static final String OUTPUT_PATH = TestSettings.TEST_PATH + "\\output_folder";

    @After
    public void clear() throws Exception {
//        Utils.deleteFolder(INPUT_PATH);
    }

    @Test
    public void classifyTest() throws Exception {
        String inputFolder = INPUT_PATH + "\\第一章";
        Utils.createWholePathIfNotExist(inputFolder);
        Utils.createWholePathIfNotExist(OUTPUT_PATH);

        TestUtils.createFile(inputFolder, "1-1-1 项目.txt");
        TestUtils.createFile(inputFolder, "1-2-2 进程.txt");
        TestUtils.createFile(inputFolder, "1-2-3 报告.txt");

        TitleEntryClassifier classifier = new TitleEntryClassifier(INPUT_PATH, OUTPUT_PATH);
        classifier.classify(TEST_EXCEL_FILE, false);
    }
}
