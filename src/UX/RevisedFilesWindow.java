package UX;

import Core.ClassifyResult;
import Core.Document;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by muwang on 4/12/2019.
 */
public class RevisedFilesWindow {

    private ClassifyResult result;

    public RevisedFilesWindow(ClassifyResult result) {
        this.result = result;
    }

    public void show() {
        final Stage window = new Stage();
        final GridPane grid = new GridPane();
        window.initModality(Modality.APPLICATION_MODAL);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setHgap(20);
        grid.setPadding(new Insets(20, 20, 20, 20));

        ListView<String> warnList = new ListView<>();
        ListView<String> uncertainList = new ListView<>();

        ObservableList<String> warnDocuments = FXCollections.observableArrayList();
        for (Document warnFile : result.warnDocuments) {
            warnDocuments.add(warnFile.path);
        }
        warnList.setItems(warnDocuments);

        ObservableList<String> uncertainFiles = FXCollections.observableArrayList();
        for (File uncertainFile : result.uncertainFiles) {
            uncertainFiles.add(uncertainFile.getAbsolutePath());
        }
        uncertainList.setItems(uncertainFiles);

        grid.add(warnList, 0, 1);
        grid.add(uncertainList, 0, 2);

        Scene scene = new Scene(grid, 480, 600);
        window.setScene(scene);

        window.show();
    }
}
