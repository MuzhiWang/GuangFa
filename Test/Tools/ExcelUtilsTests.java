package Tools;

import Core.TitleCollection;
import Settings.TestSettings;
import org.junit.Test;

/**
 * Created by muwang on 4/16/2019.
 */
public class ExcelUtilsTests {
    private static final String TEST_EXCEL_FILE = TestSettings.TEST_MATERIALS_PATH + "\\3-1-4 IPO项目测试模板.xlsx";

    @Test
    public void readExcelTest() throws Exception {
        TitleCollection titleCollection = new TitleCollection(TestSettings.TEST_PATH + "\\ExcelUtilsTests");
        ExcelUtils.readExcelToTitleCollection(TEST_EXCEL_FILE, titleCollection, false);
    }



//    @Test
//    public void
}
