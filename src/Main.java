import Tools.Utils;

import java.io.File;
import java.util.List;

public class Main {

    public static void main(String... args) {
        List<File> files = Utils.getAllFiles(".");

        if (files.isEmpty()) {
            System.out.println("No files found! \n");
        }

        for (File f : files) {
            System.out.printf("File found: %s\n", f.getAbsolutePath());
        }
    }
}
