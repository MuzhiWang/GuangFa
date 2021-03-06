package Core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by muwang on 4/17/2019.
 */
public class TitleErrorCollection {
    public List<String> notExistTitleEntries;

    public List<String> duplicatedTitleEntries;

    public List<String> notExistParts;

    public List<String> duplicatedParts;

    public List<String> notExistSections;

    public List<String> duplicatedSections;

    public List<String> duplicatedFiles;

    // Error case: Same path name Folder exists.
    // e.g. "1-1-1-1" exists which means "1/1-1/1-1-1" folder exist. Then find "1-1-1" file.
    public List<String> incorrectPathFiles;

    public List<String> fileNameException;

    public List<String> notExistFiles;

    public List<String> notExistPaths;

    public List<String> otherErrors;

    public TitleErrorCollection() {
        this.reset();
    }

    public void reset() {
        this.notExistTitleEntries = new ArrayList<>();
        this.duplicatedTitleEntries = new ArrayList<>();
        this.notExistParts = new ArrayList<>();
        this.duplicatedParts = new ArrayList<>();
        this.notExistSections = new ArrayList<>();
        this.duplicatedSections = new ArrayList<>();
        this.otherErrors = new ArrayList<>();
        this.duplicatedFiles = new ArrayList<>();
        this.incorrectPathFiles = new ArrayList<>();
        this.fileNameException = new ArrayList<>();
        this.notExistFiles = new ArrayList<>();
        this.notExistPaths = new ArrayList<>();
    }

    public List<String> listAllErrors() {
        List<String> allErrors = new ArrayList<>();
        allErrors.addAll(notExistTitleEntries);
        allErrors.addAll(duplicatedTitleEntries);
        allErrors.addAll(notExistParts);
        allErrors.addAll(duplicatedParts);
        allErrors.addAll(notExistSections);
        allErrors.addAll(duplicatedSections);
        allErrors.addAll(otherErrors);
        allErrors.addAll(duplicatedFiles);
        allErrors.addAll(incorrectPathFiles);
        allErrors.addAll(fileNameException);
        allErrors.addAll(notExistFiles);
        allErrors.addAll(notExistPaths);

        return allErrors;
    }

    public class TitleError {
        public ErrorPosition errorPosition;
        public File file;
        public String errorPath;
        public String errorInformation;
    }

    public enum ErrorPosition {
        Excel,
        InputFolder
    }
}
