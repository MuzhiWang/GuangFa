package Tools;

import Core.File;

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
    public static List<File> getAllFiles() {
        String path = Utils.getCurrentPath();
        java.io.File curDir = new java.io.File(path);
        List<java.io.File> allFiles = Utils.getFiles(curDir);

        return allFiles.stream().map(x -> new File(x.getName(), x.getPath())).collect(Collectors.toList());
    }

    private static List<java.io.File> getFiles(java.io.File curDir) {
        java.io.File[] filesList = curDir.listFiles();
        List<java.io.File> res = new ArrayList<>();

        for (java.io.File file : filesList) {
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

        try {
            FileInputStream is = new FileInputStream(oldPath);
            FileOutputStream os = new FileOutputStream(newPath);

            int b;
            while ((b = is.read()) != -1) {
                os.write(b);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public static boolean checkStrIsNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static void createWholePathIfNotExist(String pathStr) {
        Path path = FileSystems.getDefault().getPath(pathStr);
        if (Files.notExists(path)) {
            java.io.File folder = new java.io.File(pathStr);
            folder.mkdirs();
        }
    }
}
