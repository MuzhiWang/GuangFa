package Core;

import Settings.FileSettings;
import Tools.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by muwang on 4/5/2019.
 */
public class DocumentsClassifier {

    private File targetFolder;
    private Document root;

    public DocumentsClassifier(String targetFolder) throws Exception {
        if (Utils.checkStrIsNullOrEmpty(targetFolder)) {
            throw new Exception("empty target folder");
        }

        Utils.createWholePathIfNotExist(targetFolder);
        this.targetFolder = new File(targetFolder);
        this.reset();
    }

    /*
    * Serveral steps to classify all files based on name.
    *   1). If the file's path doesn't exist, create this path and copy the file to there.
    *   2). If the file's path exist.
    *       a. If the file exist.
    *           i). Create a directory with file name.
    *           ii). Rename the original file and move it to the sub directory.
    *           iii). Rename the target file and move it to the sub directory.
    *       b. If the file doesn't exist, copy the file to there.
    * */
    /// Return warning files.
    public List<Document> classify(List<File> files) throws Exception {
        List<Document> warnFiles = new ArrayList<>();
        for (File file : files) {
            // file path as {"1", "2", "3", "4项目规划 表1", "pdf"},
            String[] filePaths = DocumentsClassifier.getFilePath(file);
            String fileFormat = "." + filePaths[filePaths.length - 1];
            Document cur = this.root;
            boolean pathExist = true;

            StringBuilder pathSB = new StringBuilder(this.targetFolder.getPath());
            int i = 0;

            for (; i < filePaths.length - 2; ++i) {
                String curFolderName = filePaths[i];
                pathSB.append("\\" + curFolderName);

                // No scenario cur folder will be file.
                if (!cur.isFolder) {
                    throw new Exception("Cur path must be folder. Please check data");
                } else {
                    // Edge case: if "1-2abc.pdf" exist, but current file is "1-2-3xyz.pdf"
                    //  1). Rename the "1-2xyc.pdf" -> "1-2xyz====NeedToBeRevised.pdf"
                    //  2). Move "1-2xyz====NeedToBeRevised.pdf" from "1" folder to "1/2" folder.
                    //  3). Log and warn.
                    if (cur.files.containsKey(curFolderName)) {
                        Utils.createWholePathIfNotExist(pathSB.toString());
                        cur.addFolder(new Document(curFolderName, pathSB.toString(), true));

                        int index = 0;
                        for (Document existFile : cur.files.get(curFolderName).values()) {
                            String newName = Utils.appendStringInFileName(existFile.name, FileSettings.UNCERTERN_FILE_SUFFIX);
                            newName = Utils.appendIndex(newName, index++);
                            String newPath = pathSB.toString() + "\\" + newName;
                            Document newFile = new Document(newName, newPath, false);

                            Utils.moveFile(existFile.path, newPath);
                            cur.getFolder(curFolderName).addFile(newFile);

                            this.warnFileNeedToBeRevised(newFile, warnFiles);
                        }

                        cur.files.remove(curFolderName);
                    }

                    if (!cur.existFolder(curFolderName)) {
                        pathExist = false;
                        cur.addFolder(new Document(curFolderName, pathSB.toString(), true));
                    }
                    cur = cur.getFolder(curFolderName);
                }
            }

            if (!pathExist) {
                Utils.createWholePathIfNotExist(pathSB.toString());
            }

            // File name maybe as "1-2-3-4项目规划 表1.pdf".
            String curFileName = file.getName();
            String curFilePathInName = Utils.getLastFilePathInName(curFileName);

            // Edge case: if "1-2-3abc.pdf" exist, but current file is "1-2xyz.pdf"
            //  1). Rename the "1-2xyc.pdf" -> "1-2xyz====NeedToBeRevised.pdf"
            //  2). Move "1-2xyz====NeedToBeRevised.pdf" to "1/2" folder.
            //  3). Log and warn.
            if (cur.existFolder(curFilePathInName)) {
                curFileName = Utils.appendStringInFileName(curFileName, FileSettings.UNCERTERN_FILE_SUFFIX);
                int samePathFolderCount = cur.getFolder(curFilePathInName).files.size();
                curFileName = Utils.appendIndex(curFileName, samePathFolderCount);
                cur = cur.getFolder(curFilePathInName);

                pathSB.append("\\" + curFilePathInName + "\\" + curFileName);
                Document newFile = new Document(curFileName, pathSB.toString(), false);

                Utils.copyFile(file.getAbsolutePath(), pathSB.toString());
                cur.addFile(newFile);

                this.warnFileNeedToBeRevised(newFile, warnFiles);
            }
            // Edge case: Exist file with same path name in cur folder.
            // e.g. "1-2-3abc.pdf" && "1-2-3xyc.pdf".
            //  1). Rename these two files as "1-2-3abc======NeedToBeRevised1.pdf" && "1-2-3xyc======NeedToBeRevised2.pdf".
            //  2). Move these files into "1/2/3" folder.
            else if (cur.existFileWithSamePath(curFileName)) {
                pathSB.append("\\" + curFilePathInName);
                cur.addFolder(new Document(curFilePathInName, pathSB.toString(), true));
                Utils.createWholePathIfNotExist(pathSB.toString());

                // Rename and move all exist "1-2-3xxxx.pdf" from "1/2" to "1/2/3"
                int index = 0;
                for (Document existFile : cur.files.get(curFilePathInName).values()) {
                    String newName = Utils.appendStringInFileName(existFile.name, FileSettings.UNCERTERN_FILE_SUFFIX);
                    newName = Utils.appendIndex(newName, index++);
                    String newPath = pathSB.toString() + "\\" + newName;
                    Document newFile = new Document(newName, newPath, false);

                    Utils.moveFile(existFile.path, newPath);
                    cur.getFolder(curFilePathInName).addFile(newFile);

                    this.warnFileNeedToBeRevised(newFile, warnFiles);
                }
                cur.files.remove(curFilePathInName);

                cur = cur.getFolder(curFilePathInName);
                String newName = Utils.appendStringInFileName(curFileName, FileSettings.UNCERTERN_FILE_SUFFIX);
                newName = Utils.appendIndex(newName, index);
                String newPath = pathSB.toString() + "\\" + newName;
                Document newFile = new Document(curFileName, newPath, false);

                Utils.copyFile(file.getAbsolutePath(), newFile.path);
                cur.addFile(newFile);

                this.warnFileNeedToBeRevised(newFile, warnFiles);
            } else {
                String newPath = pathSB.toString() + "\\" + curFileName;
                Document newFile = new Document(curFileName, newPath.toString(), false);
                Utils.copyFile(file.getAbsolutePath(), newFile.path);
                cur.addFile(newFile);
            }
        }
        return warnFiles;
    }

    public void reset() {
        this.root = new Document(this.targetFolder.getName(), this.targetFolder.getPath(), true);
    }

    public Document getRoot() {
        return this.root;
    }

    /*
    * Based on file name's format, such as "1_2_3_4项目规划 表1.pdf",
    * we want to get file path as {"1", "2", "3", "4项目规划 表1", "pdf"}, the "4" should be the file name.
    * */
    protected static String[] getFilePath(File file) throws Exception {
        String fileName = file.getName();

        // Split to {"1_2_3_4项目规划 表1", "pdf"}
        String[] fileNameStrs = fileName.split("\\.");
        if (!DocumentsClassifier.checkFileName(fileNameStrs)) {
            throw new Exception("invalid file names");
        }

        // Split "1_2_3_4项目规划 表1" to {"1", "2", "3", "4项目规划 表1"}
        String[] fileNameStrsPart1 = fileNameStrs[0].split(FileSettings.FILE_FORMAT_SPLITTER);

        List<String> strs = new ArrayList<>(Arrays.asList(fileNameStrsPart1));
        strs.add(fileNameStrs[fileNameStrs.length - 1]);
        return strs.toArray(new String[strs.size()]);
    }

    private static boolean checkFileName(String[] fileNameStrs) {
        if (fileNameStrs == null || fileNameStrs.length < 2) {
            return false;
        }
        return true;
    }

    private void warnFileNeedToBeRevised(Document file, List<Document> warnFiles) {
        warnFiles.add(file);
        System.out.printf("\n File need to be revised. File path: %s \n", file.path);
    }
}
