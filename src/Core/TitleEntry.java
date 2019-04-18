package Core;

import Tools.Utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TitleEntry {
    public String path;

    public String comment;

    public Map<String, TitleEntry> maps;

    public boolean isFile;

    public File file;

    public TitleEntry(String path, String comment, boolean isFile) {
        this.path = this.createFullFolderPath(path, comment);
        this.comment = comment;
        this.isFile = isFile;
        this.file = null;
        if (!isFile) {
            this.maps = new HashMap<>();
        }
    }

    public boolean addTitleEntry(String titleIndex, TitleEntry entry, boolean createRealFolder) {
        if (this.maps.containsKey(titleIndex)) {
            return false;
        }
        this.maps.put(titleIndex, entry);

        // Create real folders in path.
        if (!entry.isFile && createRealFolder) {
            this.createFoldersInWholePath(entry.path);
        }
        return true;
    }

    public boolean existEntries() {
        return !this.maps.isEmpty();
    }

    public boolean existFile(String titleIndex) {
        return this.maps.containsKey(titleIndex) && this.maps.get(titleIndex).isFile;
    }

    public boolean existFolder(String titileIndex) {
        return this.maps.containsKey(titileIndex) && !this.maps.get(titileIndex).isFile;
    }

    public void changeFileToFolder(boolean createRealFolder) {
        if (!this.isFile) return;
        this.isFile = false;
        this.maps = new HashMap<>();
        this.file = null;

        if (createRealFolder) {
            Utils.createWholePathIfNotExist(this.path);
        }
    }

    public boolean addFile(File file) {
        if (this.isFile) {
            this.file = file;
        }
        return this.isFile;
    }

    public boolean fileExist() throws Exception {
        if (!this.isFile) {
            throw new Exception("This is folder");
        }
        return this.file != null;
    }

    private void createFoldersInWholePath(String path) {
        Utils.createWholePathIfNotExist(path);
    }

    private String createFullFolderPath(String path, String comment) {
        return comment == null || comment.isEmpty() ? path : path + " " + comment;
    }
}
