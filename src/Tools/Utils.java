package Tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by muwang on 4/4/2019.
 */
public final class Utils {
    public static List<File> getAllFilesInCurrentDirectory() {
        String path = Utils.getCurrentPath();
        return Utils.getAllFiles(path);
    }

    public static List<File> getAllFiles(String path) {
        File curDir = new File(path);
        return Utils.getFiles(curDir);
    }

    private static List<File> getFiles(File curDir) {
        File[] filesList = curDir.listFiles();
        List<File> res = new ArrayList<>();

        for (File file : filesList) {
            if (file.isDirectory()) {
                res.addAll(Utils.getFiles(file));
            } else {
                res.add(file);
            }
        }

        return res;
    }

    public static String getCurrentPath() {
        return ".";
    }

    public static void copyFile(String oldPath, String newPath) throws Exception {
        if (Utils.checkStrIsNullOrEmpty(oldPath) || Utils.checkStrIsNullOrEmpty(newPath)) {
            throw new Exception("path should not be empty");
        }

        Utils.createWholePathIfNotExist(Utils.getFilePath(newPath));

        try {
            FileInputStream is = new FileInputStream(oldPath);
            FileOutputStream os = new FileOutputStream(newPath);

            int b;
            while ((b = is.read()) != -1) {
                os.write(b);
            }

            is.close();
            os.close();
        } catch (Exception e) {
            System.out.printf("Copy failed. Old path: %s, new path: %s", oldPath, newPath);
            throw e;
        }
    }

    public static boolean checkStrIsNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static void createWholePathIfNotExist(String pathStr) {
        if (Utils.pathExist(pathStr)) {
            File folder = new File(pathStr);
            folder.mkdirs();
        }
    }

    public static boolean pathExist(String pathStr) {
        Path path = FileSystems.getDefault().getPath(pathStr);
        return Files.notExists(path);
    }

    public static void deleteFolder(String folderPath) throws Exception {
        Utils.checkStrIsNullOrEmpty(folderPath);
        File folder = new File(folderPath);
        if (folder.exists()) {
            for (File file : folder.listFiles()) {
                if (file.isDirectory()) {
                    Utils.deleteFolder(file.getAbsolutePath());
                } else {
                    file.delete();
                }
            }
        }
        folder.delete();

    }

    public static boolean isCharDigit(char ch) {
        return ch - '0' >= 0 && ch - '9' <= 0;
    }

    // Query "4项目规划 表1" as "4"
    public static String queryFilePathInName(String fileName) {
        StringBuilder sb = new StringBuilder();
        for (char ch : fileName.toCharArray()) {
            if (Utils.isCharDigit(ch)) {
                sb.append(ch);
            } else {
                break;
            }
        }
        return sb.toString();
    }

    private static String getFilePath(String absolutePath) {
        int idx = absolutePath.lastIndexOf('\\');
        return absolutePath.substring(0, idx);
    }
}
