package Tools;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by muwang on 4/15/2019.
 */
public final class ExcelUtils {
    public static void readExcel(String filePath) throws Exception {
        ExcelUtils.checkExcelFilePath(filePath);
        File file = new File(filePath);

        POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));

    }

    private static void checkExcelFilePath(String filePath) {

    }
}
