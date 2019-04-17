package UX;

import Core.DocumentClassifyResult;
import Core.Document;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by muwang on 4/12/2019.
 */
public class RevisedFilesWindow {

    private DocumentClassifyResult result;

    public RevisedFilesWindow(DocumentClassifyResult result) {
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

        ListView<Label> warnList = new ListView<>();
        ListView<Label> uncertainList = new ListView<>();

        Label warnListLabel = new Label();
        warnListLabel.setText("Files need to be revised:");
        warnListLabel.setTextFill(Color.FIREBRICK);
        Label uncertainListLabel = new Label();
        uncertainListLabel.setText("Files name are not recognized:");
        uncertainListLabel.setTextFill(Color.FIREBRICK);

        if (result.warnDocuments.isEmpty()) {
            warnList.setVisible(false);
            warnListLabel.setVisible(false);
        } else {
            ObservableList<Label> warnDocuments = FXCollections.observableArrayList();
            for (Document warnFile : result.warnDocuments) {
                Label label = this.createClickActionFileLabel(warnFile.path);
                warnDocuments.add(label);
            }
            warnList.setItems(warnDocuments);
        }

        if (result.uncertainFiles.isEmpty()) {
            uncertainList.setVisible(false);
            uncertainListLabel.setVisible(false);
        } else {
            ObservableList<Label> uncertainFiles = FXCollections.observableArrayList();
            for (File uncertainFile : result.uncertainFiles) {
                Label label = this.createClickActionFileLabel(uncertainFile.getAbsolutePath());
                uncertainFiles.add(label);
            }
            uncertainList.setItems(uncertainFiles);
        }

        Button confirmButton = new Button();
        confirmButton.setText("Close");
        confirmButton.setOnAction(e -> {
            window.close();
        });

        grid.add(warnListLabel, 0, 0, 4, 1);
        grid.add(warnList, 0, 1, 4, 4);
        grid.add(uncertainListLabel, 0, 5, 4, 1);
        grid.add(uncertainList, 0, 6, 4, 4);
        grid.add(confirmButton, 0, 10, 4, 1);

        Scene scene = new Scene(grid, 640, 480);
        window.setScene(scene);

        window.show();
    }

    private Label createClickActionFileLabel(String filePath) {
        Label label = new Label();
        label.setText(filePath);
        label.setOnMouseClicked(e -> {
            if (e.getClickCount() >= 2) {
                File f = new File(filePath);
                try {
                    Desktop.getDesktop().open(f.getParentFile());
                } catch (IOException e1) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Could not open " + f.getParentFile().getAbsolutePath());
                    alert.show();
                }
            }
        });
        return label;
    }
}
