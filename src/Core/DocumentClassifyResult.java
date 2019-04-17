package Core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by muwang on 4/12/2019.
 */
public class DocumentClassifyResult {
    public List<Document> warnDocuments;

    public List<File> uncertainFiles;

    public DocumentClassifyResult() {
        this.warnDocuments = new ArrayList<>();
        this.uncertainFiles = new ArrayList<>();
    }
}
