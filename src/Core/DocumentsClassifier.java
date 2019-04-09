package Core;

import Settings.FileSettings;
import Tools.Utils;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
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
    public void classify(List<File> files) throws Exception {
        for (File file : files) {
            String[] filePaths = DocumentsClassifier.getFilePath(file);
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
                    if (!cur.existSubFolder(curFolderName)) {
                        pathExist = false;
                        cur.addDocument(new Document(curFolderName, pathSB.toString(), true));
                    }
                    cur = cur.getDocument(curFolderName);
                }
            }

            if (!pathExist) {
                Utils.createWholePathIfNotExist(pathSB.toString());
            }

            String curFileName = filePaths[i];
            pathSB.append("\\" + curFileName + "." + filePaths[++i]);

            // Same folder name in cur directory.
            if (cur.existSubFolder(curFileName)) {

            }
            // Same file name in cur directory.
            else if (cur.existFile(curFileName)) {

            } else {
                Utils.copyFile(file.getPath(), pathSB.toString());
                cur.addDocument(new Document(curFileName, pathSB.toString(), false));
            }
        }
    }

    public void reset() {
        this.root = new Document(this.targetFolder.getName(), this.targetFolder.getPath(), true);
    }

    public Document getRoot() {
        return this.root;
    }

    /*
    * Based on file name's format, such as "1_2_3_4.pdf",
    * we want to get file path as {"1", "2", "3", "4", "pdf"}, the "4" should be the file name.
    * */
    protected static String[] getFilePath(File file) throws Exception {
        String fileName = file.getName();
        String[] fileNameStrs = fileName.split("\\.");
        if (!DocumentsClassifier.checkFileName(fileNameStrs)) {
            throw new Exception("invalid file names");
        }

        List<String> strs = new ArrayList<>(Arrays.asList(fileNameStrs[0].split(FileSettings.FILE_FORMAT_SPLITTER)));
        strs.add(fileNameStrs[fileNameStrs.length - 1]);
        return strs.toArray(new String[strs.size()]);
    }

    private static boolean checkFileName(String[] fileNameStrs) {
        if (fileNameStrs == null || fileNameStrs.length < 2) {
            return false;
        }
        return true;
    }
}
