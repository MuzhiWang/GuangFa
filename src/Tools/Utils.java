package Tools;

import Core.File;

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
}
