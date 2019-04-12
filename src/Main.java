import java.io.File;

import javafx.application.Application;
import javafx.beans.value.ObservableListValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class Main extends Application implements EventHandler<ActionEvent> {

    private Button inputFolderButton, outputFolderButton;
    private Stage mainWindow;
    private DirectoryChooser directoryChooser;

    public static void main(String... args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.mainWindow = primaryStage;
        this.mainWindow.setTitle("哈哈哈广发发发");
        this.directoryChooser = new DirectoryChooser();
        this.directoryChooser.setTitle("Select pdf files root folder");

        Text inputText = new Text();
        inputText.setText("Please select input folder");
        Text outputText = new Text();
        outputText.setText("Please select output folder");

        this.inputFolderButton = new Button();
        this.inputFolderButton.setText("Input Browser");
        this.inputFolderButton.setOnAction(e -> {
            File dir = directoryChooser.showDialog(this.mainWindow);
            if (dir != null) {
                inputText.setText(dir.getAbsolutePath());
            } else {
                inputText.setText("Filed to select directory!");
            }
        });

        this.outputFolderButton = new Button();
        this.outputFolderButton.setText("Output Browser");
        this.outputFolderButton.setOnAction(e -> {
            File dir = directoryChooser.showDialog(this.mainWindow);
            if (dir != null) {
                outputText.setText(dir.getAbsolutePath());
            } else {
                outputText.setText("Filed to select directory!");
            }
        });

        ListView<String> warnList = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList("1-1.pdf", "1-2.pdf", "1-3.pdf");
        warnList.setItems(items);

        BorderPane layout = this.getMainLayerout();
        ((VBox)layout.getTop()).getChildren().addAll(inputText, inputFolderButton, outputText, outputFolderButton);
        ((GridPane)layout.getCenter()).getChildren().addAll(warnList);

        Scene scene = new Scene(layout, 300, 250);
        primaryStage.setScene(scene);

        this.mainWindow.show();
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == this.inputFolderButton) {
            this.inputFolderButton.setText("Oh yes gogogogo!");
        }
    }

    private BorderPane getMainLayerout() {
        BorderPane mainLayout = new BorderPane();

        VBox top = new VBox();
        top.setAlignment(Pos.CENTER);

        GridPane center = new GridPane();

        HBox bottom = new HBox();
        bottom.setAlignment(Pos.CENTER);

        mainLayout.setTop(top);
        mainLayout.setBottom(bottom);
        mainLayout.setCenter(center);

        return mainLayout;
    }
}
