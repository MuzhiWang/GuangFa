package Core;

import Settings.FileSettings;
import Tools.ExcelUtils;
import Tools.StringUtils;
import Tools.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by muwang on 4/17/2019.
 */
public class TitleEntryClassifier {
    private String inputPath;

    private String outputPath;

    public TitleCollection titleCollection;

    public TitleEntryClassifier(String inputPath, String outputPath) throws Exception {
        this.inputPath = inputPath;
        this.outputPath = outputPath;
        this.titleCollection = new TitleCollection(outputPath);
    }

    public void classify(String excelPath, boolean readAllSheets) throws Exception {
        if (!this.checkInputPath().inputPathValid) {
            return;
        }

        this.initialize(excelPath, readAllSheets);

        File root = new File(this.inputPath);
        for (File partFolder : root.listFiles()) {
            String partName = partFolder.getName();
            String partChineseNum = StringUtils.getChineseNumberInString(partName);
            String digitNum = StringUtils.mapChineseNumberToDigit(partChineseNum);

            // Error case: parts not exist.
            if (!this.titleCollection.partsMap.containsKey(digitNum)) {
                this.titleCollection.errorCollection.notExistParts.add(
                        String.format("Part doesn't exist: %s\n", partName)
                );
            } else {
                TitleEntry partEntry = this.titleCollection.partsMap.get(digitNum);
                List<File> allFiles = Utils.getAllFiles(partFolder.getAbsolutePath());
                for (File file : allFiles) {
                    TitleEntry curEntry = partEntry;
                    String fileName = file.getName();
                    String titleStr = StringUtils.getTitleInString(fileName);
                    // Error case: No title string match
                    if (titleStr == "") {
                        this.titleCollection.errorCollection.fileNameException.add(
                                String.format("File name not match pattern: %s\n", file.getAbsolutePath())
                        );
                    } else {
                        String[] titlePath = titleStr.split(FileSettings.FILE_FORMAT_SPLITTER);
                        String curTitlePath;
                        boolean invalid = false;
                        for (int i = 0; i < titlePath.length - 1; ++i) {
                            curTitlePath = titlePath[i];
                            // Error case: File path doesn't exist.
                            if (!curEntry.existFolder(curTitlePath)) {
                                this.titleCollection.errorCollection.notExistFiles.add(
                                        String.format("File path not exist: %s\n", file.getAbsolutePath())
                                );
                                invalid = true;
                                break;
                            }
                            // Error case: "1-1-1" file exist. Then find "1-1-1-1" file.
                            else if (curEntry.existFile(curTitlePath)) {
                                this.titleCollection.errorCollection.notExistFiles.add(
                                        String.format("File path not exist: %s\n", file.getAbsolutePath())
                                );
                                invalid = true;
                                break;
                            }
                            curEntry = curEntry.maps.get(curTitlePath);
                        }

                        if (invalid) {
                            continue;
                        }

                        curTitlePath = titlePath[titlePath.length - 1];
                        // Error case: No file in Excel exist.
                        if (!curEntry.existFile(curTitlePath)) {
                            this.titleCollection.errorCollection.notExistFiles.add(
                                    String.format("No file with such path exist in Excel: %s\n", file.getAbsolutePath())
                            );
                        }
                        // Error case: "1/1/1" folder exist. Then find "1-1-1" file.
                        else if (curEntry.existFolder(curTitlePath)) {
                            this.titleCollection.errorCollection.incorrectPathFiles.add(
                                    String.format("Incorrect path file: %s\n", file.getAbsolutePath())
                            );
                        } else {
                            TitleEntry fileEntry = curEntry.maps.get(curTitlePath);
                            // Error case: File with same name already copied.
                            if (fileEntry.fileExist()) {
                                this.titleCollection.errorCollection.duplicatedFiles.add(
                                        String.format("File with same path already copied: %s\n", file.getAbsolutePath())
                                );
                            } else {
                                String newPath = curEntry.path + "\\" + fileName;
                                Utils.copyFile(file.getAbsolutePath(), newPath);
                                fileEntry.addFile(file);
                                System.out.printf("Move files if not exist from: %s, to: %s\n", file.getAbsolutePath(), curEntry.path);
                            }
                        }
                    }
                }
            }
        }

        this.checkAllFilesInTitleCollection();
    }

    public CheckInputPathResult checkInputPath() {
        File inputFolder = new File(this.inputPath);
        CheckInputPathResult result = new CheckInputPathResult();

        if (!inputFolder.exists() || inputFolder.listFiles() == null) {
            result.setEmptyFolder(inputFolder);
            return result;
        }

        for (File file : inputFolder.listFiles()) {
            if (file.isFile()) {
                result.setExistFiles(file);
            } else {
                String folderName = file.getName();
                if (folderName.matches(FileSettings.ExcelCellSettings.CHINESE_TITLE_PART_FORMAT)) {
                    result.setNotMatchPartName(file.getName());
                }
            }
        }

        return result;
    }

    private void initialize(String excelPath, boolean readAllSheets) throws Exception {
        ExcelUtils.readExcelToTitleCollection(excelPath, this.titleCollection, readAllSheets);

        Utils.deleteFolder(this.outputPath);
        Utils.createWholePathIfNotExist(this.outputPath);
    }

    private void checkAllFilesInTitleCollection() throws Exception {
        List<TitleEntry> uncheckedFiles = new ArrayList<>();
        for (TitleEntry titleEntry : this.titleCollection.partsMap.values()) {
            uncheckedFiles.addAll(this.checkTitleEntry(titleEntry));
        }
        for (TitleEntry titleEntry : uncheckedFiles) {
            this.titleCollection.errorCollection.notExistFiles.add(
                    String.format("The file queried from Excel doesn't exist in input folder: %s\n", titleEntry.path)
            );
        }
    }

    private List<TitleEntry> checkTitleEntry(TitleEntry titleEntry) throws Exception {
        List<TitleEntry> uncheckedEntries = new ArrayList<>();
        if (titleEntry.isFile) {
            if (!titleEntry.fileExist()) {
                uncheckedEntries.add(titleEntry);
            }
        } else {
            for (TitleEntry subEntry : titleEntry.maps.values()) {
                uncheckedEntries.addAll(this.checkTitleEntry(subEntry));
            }
        }
        return uncheckedEntries;
    }

    public class CheckInputPathResult {
        public boolean inputPathValid = true;
        public boolean existFiles = false;
        public boolean notMatchPartName = false;
        public boolean emptyFolder = false;
        public List<String> invalidLog = new ArrayList<>();

        public void setExistFiles(File file) {
            this.existFiles = true;
            this.inputPathValid = false;
            this.invalidLog.add(String.format("File exist in root path: %s\n", file.getAbsolutePath()));
        }

        public void setNotMatchPartName(String name) {
            this.notMatchPartName = true;
            this.inputPathValid = false;
            this.invalidLog.add(String.format("Root name not match: %s\n", name));
        }

        public void setEmptyFolder(File folder) {
            this.emptyFolder = true;
            this.inputPathValid = false;
            this.invalidLog.add(String.format("The input folder is empty: %s\n", folder.getAbsolutePath()));
        }
    }
}
