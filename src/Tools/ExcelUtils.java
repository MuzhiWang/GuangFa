package Tools;

import Core.TitleCollection;
import Settings.FileSettings;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.Iterator;


/**
 * Created by muwang on 4/15/2019.
 */
public final class ExcelUtils {
    public static void readExcelToTitleCollection(String filePath, TitleCollection titleCollection) throws Exception {
        ExcelUtils.checkExcelFilePath(filePath);

        try {
            FileInputStream file = new FileInputStream(filePath);
            XSSFWorkbook wb = new XSSFWorkbook(file);
            Sheet sheet = wb.getSheetAt(0);
            Row row;
            Cell cell;

            Iterator<Row> rowIterator = sheet.rowIterator();
            while (rowIterator.hasNext()) {
                row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    cell = cellIterator.next();

                    switch (cell.getCellType()) {
                        case STRING:
                            String str = cell.getStringCellValue();
                            if (ExcelUtils.isDigitTitleString(str)) {
                                int cIndex = cell.getColumnIndex();
                                Cell nextCell = cell.getRow().getCell(cIndex + 1);
                                System.out.printf("Cell is String : %s, next cell: %s\n", str, nextCell.getStringCellValue());
                            } else if (ExcelUtils.isChineseTitleString(str))
                                System.out.println("Chinese title: " + str);
                            break;
                        default:
                            break;
                    }
                }
            }

            file.close();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static boolean isChineseTitleString(String str) {
        String[] strs = str.split(" ");
        for (String s : strs) {
            if (StringUtils.isLetterChinese(s) && s.matches(FileSettings.ExcelCellSettings.CHINESE_TITLE_FORMAT)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDigitTitleString(String str) {
        str = ExcelUtils.getUniformString(str);
        return str.matches(FileSettings.ExcelCellSettings.TITLE_FORMAT);
    }

    public static String getUniformString(String str) {
        return str.replace("\\s+", "").replaceAll(FileSettings.ExcelCellSettings.TITLE_SPLITTER, "-");
    }

    private static void checkExcelFilePath(String filePath) throws Exception {
        boolean failedToCheck = false;
        if (filePath == null || filePath.isEmpty()) {
            failedToCheck = true;
        } else {
            String[] pathStrs = filePath.trim().split("\\.");
            if (!pathStrs[pathStrs.length - 1].matches(FileSettings.EXCEL_FORMAT_STR)) {
                failedToCheck = true;
            }
        }

        if (failedToCheck) {
            throw new Exception("The file is not excel file: " + filePath);
        }
    }
}
