package UX;

import Core.TitleErrorCollection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by muwang on 4/18/2019.
 */
public class TitleEntryRevisedFilesWindow {
    private TitleErrorCollection titleErrorCollection;

    public TitleEntryRevisedFilesWindow(TitleErrorCollection titleErrorCollection) {
        this.titleErrorCollection = titleErrorCollection;
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


        ObservableList<Label> observableList = FXCollections.observableArrayList();
        for (String errorStr : this.titleErrorCollection.listAllErrors()) {
            Label label = new Label();
            label.setText(errorStr);
            observableList.add(label);
        }
        warnList.setItems(observableList);


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
}
