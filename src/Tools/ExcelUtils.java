package Tools;

import Core.TitleCollection;
import Core.TitleEntry;
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
    public static void readExcelToTitleCollection(String excelFilePath, TitleCollection titleCollection) throws Exception {
        ExcelUtils.checkExcelFilePath(excelFilePath);

        try {
            FileInputStream file = new FileInputStream(excelFilePath);
            XSSFWorkbook wb = new XSSFWorkbook(file);
            Iterator<Sheet> sheetIterator = wb.sheetIterator();
            while (sheetIterator.hasNext()) {
                Sheet sheet = sheetIterator.next();
                Row row;
                Cell cell;

                Iterator<Row> rowIterator = sheet.rowIterator();
                TitleEntry curPartEntry = null;
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
                                    String comment = nextCell.getStringCellValue();
                                    String uniformTitleStr = StringUtils.getUniformString(str);
                                    String[] titlePath = uniformTitleStr.split(FileSettings.ExcelCellSettings.TITLE_SPLITTER);
                                    TitleEntry curEntry = curPartEntry;
                                    String curPathStr;

                                    for (int i = 0; i < titlePath.length - 1; ++i) {
                                        curPathStr = titlePath[i];

                                        // Case1: Same path name File exist, change it to Folder.
                                        // Such as "1-1" as file exist, then find "1-1-1", then "1-1" should be changed to Folder
                                        if (curEntry.existFile(curPathStr)) {
                                            curEntry.maps.get(curPathStr).changeFileToFolder();
                                        } else if (!curEntry.existFolder(curPathStr)) {
                                            String newPath = curEntry.path + "\\" + uniformTitleStr;
                                            TitleEntry newEntry = new TitleEntry(newPath, comment, false);
                                            curEntry.addTitleEntry(curPathStr, newEntry);
                                        }

                                        curEntry = curEntry.maps.get(curPathStr);
                                    }

                                    curPathStr = titlePath[titlePath.length - 1];
                                    // Error case: Same path file exist.
                                    // e.g. "1-1-1" file exists, then find another "1-1-1".
                                    if (curEntry.existFile(curPathStr)) {
                                        titleCollection.errorCollection.duplicatedFiles.add(
                                                String.format("Duplicate files exist: in part: %s, %s, %s", curPartEntry.path, uniformTitleStr, comment)
                                        );
                                    }
                                    // Error case: Same path name Folder exists.
                                    // e.g. "1-1-1-1" exists which means "1/1-1/1-1-1" folder exist. Then find "1-1-1" file.
                                    else if (curEntry.existFolder(curPathStr)) {
                                        titleCollection.errorCollection.incorrectPathFiles.add(
                                                String.format("Incorrect path file: in part: %s, %s, %s", curPartEntry.path, uniformTitleStr, comment)
                                        );
                                    } else {
                                        String newPath = String.format("%s\\%s", curEntry.path, uniformTitleStr);
                                        TitleEntry newEntry = new TitleEntry(newPath, comment, true);
                                        curEntry.addTitleEntry(curPathStr, newEntry);
                                    }
                                    System.out.printf("Cell is String : %s, next cell: %s\n", str, nextCell.getStringCellValue());
                                } else if (ExcelUtils.isChineseTitleString(str)) {
                                    String[] splitStrs = str.split(FileSettings.EMPTY_SPACE_FORMAT);
                                    String title = splitStrs[0], comment = splitStrs[1];
                                    String chineseNum = StringUtils.getChineseNumberInString(title);
                                    String digitNum = StringUtils.mapChineseNumberToDigit(chineseNum);

                                    if (title.indexOf(FileSettings.ExcelCellSettings.TITLE_PART) > -1) {
                                        // Error case: Same part exist.
                                        if (titleCollection.partsMap.containsKey(digitNum)) {
                                            titleCollection.errorCollection.duplicatedParts.add(title);
                                        } else {
                                            titleCollection.addPart(digitNum, title, comment);
                                            curPartEntry = titleCollection.partsMap.get(digitNum);
                                        }
                                    } else if (title.indexOf(FileSettings.ExcelCellSettings.TITLE_SECTION) > -1) {
                                        // Error case: part not select yet.
                                        if (curPartEntry == null) {
                                            titleCollection.errorCollection.notExistParts.add(
                                                    String.format("第XX部分 not exist for: %s, %s", title, comment)
                                            );
                                        } else {
                                            TitleEntry partEntry = curPartEntry;
                                            // Error case: section exist
                                            if (partEntry.existFolder(digitNum)) {
                                                titleCollection.errorCollection.duplicatedSections.add(
                                                        String.format("Sections exist for: %s, %s", title, comment)
                                                );
                                            } else {
                                                String newPath = partEntry.path + "\\" + title;
                                                TitleEntry newEntry = new TitleEntry(newPath, comment, false);
                                                partEntry.addTitleEntry(digitNum, newEntry);
                                            }
                                        }
                                    } else {
                                        // Error case: Chinese title neither parts nor sections.
                                        titleCollection.errorCollection.otherErrors.add(
                                                String.format("Title neither 部分 nor 章: %s", title)
                                        );
                                    }
                                    System.out.println("Chinese title: " + str);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            file.close();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static boolean isChineseTitleString(String str) {
        String[] strs = str.split(FileSettings.EMPTY_SPACE_FORMAT);
        for (String s : strs) {
            if (StringUtils.isLetterChinese(s) && s.matches(FileSettings.ExcelCellSettings.CHINESE_TITLE_FORMAT)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDigitTitleString(String str) {
        str = StringUtils.getUniformString(str);
        return str.matches(FileSettings.ExcelCellSettings.TITLE_FORMAT);
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
